package com.qiangdong.chat.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.qiangdong.chat.Constant;
import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.modle.regist.RegistActivity;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IPublicApi;
import com.qiangdong.chat.utils.UserSourceImp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import cn.wildfirechat.UserInfoCallBack;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private static String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int REQUEST_CODE_DRAW_OVERLAY = 101;

    private SharedPreferences sharedPreferences;
    private String id;
    private String token;

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        hideStatusBar();

        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        token = sharedPreferences.getString("token", null);

        if (checkPermission()) {
            new Handler().postDelayed(this::showNextScreen, 1000);
        } else {
            requestPermissions(permissions, 100);
        }
    }

    private boolean checkPermission() {
        boolean granted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                granted = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    break;
                }
            }
        }
        return granted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "需要相关权限才能正常使用", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        showNextScreen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DRAW_OVERLAY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    showNextScreen();
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private void showNextScreen() {
        RetrofitFactory.getRetrofit().create(IPublicApi.class).getToken(id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(token -> token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        Constant.UPTOKEN = bean.getData().get(0).getUpToken();
                        Constant.chatNumber = bean.getData().get(0).getChatNumber();
                        Constant.dokyPay = bean.getData().get(0).getDokyPay();
                        Constant.googlePay = bean.getData().get(0).getGooglePay();
                        Constant.rabbit = bean.getData().get(0).getRabbit();
                        Constant.sayHelloNumber = bean.getData().get(0).getSayHelloNumber();
                    }
                    if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(token)) {

                        UserSourceImp.getInstance().init(id, new UserInfoCallBack() {
                            @Override
                            public void onUserCallback(UserInfo userInfo) {
                                ChatManager.Instance().setUserSource(UserSourceImp.getInstance());
                                showMain();
                            }
                        });
                    } else {
                        showLogin();
                    }
                }, ErrorAction.error());

    }

    private void showMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showLogin() {
        Intent intent;
        intent = new Intent(this, RegistActivity.class);
        startActivity(intent);
        finish();
    }
}
