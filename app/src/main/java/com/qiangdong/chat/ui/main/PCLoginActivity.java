package com.qiangdong.chat.ui.main;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qiangdong.chat.Config;
import com.qiangdong.chat.R;
import com.qiangdong.chat.ui.login.model.PCSession;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.net.OKHttpHelper;
import cn.wildfire.chat.kit.net.SimpleCallback;
import cn.wildfire.chat.kit.user.UserViewModel;

public class PCLoginActivity extends WfcBaseActivity {
    private String token;
    private PCSession pcSession;
    @BindView(R.id.confirmButton)
    Button confirmButton;

    @Override
    protected void beforeViews() {
        token = getIntent().getStringExtra("token");
        if (TextUtils.isEmpty(token)) {
            finish();
        }
    }

    @Override
    protected int contentLayout() {
        return R.layout.pc_login_activity;
    }

    @Override
    protected void afterViews() {
        scanPCLogin(token);
    }

    @OnClick(R.id.confirmButton)
    void confirmPCLogin() {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        confirmPCLogin(token, userViewModel.getUserId());
    }

    private void scanPCLogin(String token) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("处理中")
                .progress(true, 100)
                .build();
        dialog.show();
        String url = Config.APP_SERVER_ADDRESS + "/scan_pc";
        url += "/" + token;
        OKHttpHelper.post(url, null, new SimpleCallback<PCSession>() {
            @Override
            public void onUiSuccess(PCSession pcSession) {
                if (isFinishing()) {
                    return;
                }
                dialog.dismiss();
                PCLoginActivity.this.pcSession = pcSession;
                if (pcSession.getStatus() == 1) {
                    confirmButton.setEnabled(true);
                } else {
                    Toast.makeText(PCLoginActivity.this, "status: " + pcSession.getStatus(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onUiFailure(int code, String msg) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(PCLoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void confirmPCLogin(String token, String userId) {
        String url = Config.APP_SERVER_ADDRESS + "/confirm_pc";

        Map<String, String> params = new HashMap<>(2);
        params.put("user_id", userId);
        params.put("token", token);
        OKHttpHelper.post(url, params, new SimpleCallback<PCSession>() {
            @Override
            public void onUiSuccess(PCSession pcSession) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(PCLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onUiFailure(int code, String msg) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(PCLoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
