package com.qiangdong.chat.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.login.LoginBean;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.ILogInApi;
import com.qiangdong.chat.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * use {@link SMSLoginActivity} instead
 */
public class LoginActivity extends WfcBaseActivity {
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.accountEditText)
    EditText accountEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_go_regist)
    TextView tvGoRegist;

    @Override
    protected int contentLayout() {
        return R.layout.login_activity_account;
    }

    @Override
    protected boolean showHomeMenuItem() {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        setTitle("");
//        TextView textView = (TextView) toolbar.getChildAt(0);//主标题
//        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
//        textView.setGravity(Gravity.CENTER);//水平居中，CENTER，即水平也垂直，自选
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        String account = sp.getString("account", "");
        accountEditText.setText(account);
    }

    @OnTextChanged(value = R.id.accountEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputAccount(Editable editable) {
        if (!TextUtils.isEmpty(passwordEditText.getText()) && !TextUtils.isEmpty(editable)) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    @OnTextChanged(value = R.id.passwordEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputPassword(Editable editable) {
        if (!TextUtils.isEmpty(accountEditText.getText()) && !TextUtils.isEmpty(editable)) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    @OnClick({R.id.loginButton, R.id.tv_go_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                login();
                break;
            case R.id.tv_go_regist:
                finish();
                break;
        }
    }

    @SuppressLint("CheckResult")
    void login() {
        /**
         * 预设账号密码
         //         */
//        accountEditText.setText("11036210");
//        passwordEditText.setText("123456");

        String account = accountEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(getResources().getString(R.string.logining))
                .progress(true, 10)
                .cancelable(false)
                .build();
        dialog.show();
        try {
            String clientId = ChatManagerHolder.gChatManager.getClientId();
            RetrofitFactory.getRetrofit().create(ILogInApi.class).Login(account, clientId, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(token -> token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                        dialog.dismiss();
                        if (bean.getCode() == 200) {
                            LoginBean.loginData data = bean.getData().get(0);
                            Toast.makeText(this,getString(R.string.login_success),Toast.LENGTH_SHORT).show();
                            //TODO 链接服务器
                            ChatManagerHolder.gChatManager.connect(String.valueOf(data.getUserId()), data.getToken());
                            SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                            sp.edit()
                                    .putString("id", String.valueOf(data.getUserId()))
                                    .putString("token", data.getToken())
                                    .apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, ErrorAction.error());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

}
