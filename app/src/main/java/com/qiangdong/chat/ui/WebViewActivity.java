package com.qiangdong.chat.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.qiangdong.chat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.WfcBaseActivity;

public class WebViewActivity extends WfcBaseActivity {


    @BindView(R.id.webview)
    WebView webview;

    private String url;
    private String data;

    @Override
    protected int contentLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onStart() {
        super.onStart();
        url = getIntent().getStringExtra("url");
        data = getIntent().getStringExtra("data");
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(data)) {
            webview.postUrl(url, data.getBytes());
        }
    }
}
