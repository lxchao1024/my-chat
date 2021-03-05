package com.qiangdong.chat.ui.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.qiangdong.chat.Config;
import com.qiangdong.chat.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.WfcWebViewActivity;

public class AboutActivity extends WfcBaseActivity {

    @BindView(R.id.infoTextView)
    TextView infoTextView;

    @Override
    protected int contentLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void afterViews() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            String info = packageInfo.packageName + "\n"
                    + packageInfo.versionCode + " " + packageInfo.versionName + "\n"
                    + Config.IM_SERVER_HOST + " " + Config.IM_SERVER_PORT + "\n"
                    + Config.APP_SERVER_ADDRESS + "\n"
                    + Config.ICE_ADDRESS + " " + Config.ICE_USERNAME + " " + Config.ICE_PASSWORD + "\n";

            infoTextView.setText(info);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.introOptionItemView)
    public void intro() {
        WfcWebViewActivity.loadUrl(this, getString(R.string.function_introduction), "http://docs.wildfirechat.cn/");
    }

    @OnClick(R.id.agreementOptionItemView)
    public void agreement() {
        WfcWebViewActivity.loadUrl(this, getString(R.string.user_agreement), "http://www.wildfirechat.cn/firechat_user_agreement.html");
    }

    @OnClick(R.id.privacyOptionItemView)
    public void privacy() {
        WfcWebViewActivity.loadUrl(this, getString(R.string.terms_of_use_and_privacy_policy), "http://www.wildfirechat.cn/firechat_user_privacy.html");
    }
}
