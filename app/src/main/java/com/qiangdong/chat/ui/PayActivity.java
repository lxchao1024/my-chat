package com.qiangdong.chat.ui;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IPayApi;
import com.qiangdong.chat.net.api.IPayPageApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.WfcBaseActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayActivity extends WfcBaseActivity {


    @BindView(R.id.payTest)
    TextView payTest;

    @Override
    protected int contentLayout() {
        return R.layout.activity_pay;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onStart() {
        super.onStart();
        RetrofitFactory.getRetrofit().create(IPayPageApi.class).payPageInfo("1", 108)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(token -> token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean != null) {

                    } else {

                    }
                }, ErrorAction.error());
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.payTest)
    public void onViewClicked() {
        RetrofitFactory.getRetrofit().create(IPayApi.class).payInfo(1, 1, 108)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(token -> {
                    if (token.getCode() == 200) {
                        return token.getData().get(0);
                    }
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean != null) {
                        payTest(bean.getAmount(), bean.getOrderId(), bean.getTxnToken(), bean.getMid());
//                        webview.setVisibility(View.VISIBLE);
                    }

                }, ErrorAction.error());
    }


    private int ActivityRequestCode = 100;
    private String postUrl = "https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage";//测试环境
//    private String postUrl = "https://securegw.paytm.in/theia/api/v1/showPaymentPage";//正式环境

    private void payTest(String amount, String orderid, String txnToken, String mid) {
        try {
            Intent paytmIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putDouble("nativeSdkForMerchantAmount", Double.parseDouble(amount));
            bundle.putString("orderid", orderid);
            bundle.putString("txnToken", txnToken);
            bundle.putString("mid", mid);
            paytmIntent.setComponent(new ComponentName("net.one97.paytm",
                    "net.one97.paytm.AJRJarvisSplash"));
            paytmIntent.putExtra("paymentmode", 2); // You must have to pass hard coded 2 here, Else your transaction would not proceed.
            paytmIntent.putExtra("bill", bundle);
            startActivityForResult(paytmIntent,
                    ActivityRequestCode);
//ActivityRequestCode to be defined by merchant.
        } catch (ActivityNotFoundException e) {
//In case of Paytm app not installed, kindly handle
            //  ActivityNotFoundException occurred while invoking Paytm Activity.
            StringBuilder postData = new StringBuilder();
            postData.append("MID=")
                    .append(mid)
                    .append("&txnToken=")
                    .append(txnToken)
                    .append("&ORDER_ID=")
                    .append(orderid);
            Intent intent = new Intent();
            intent.putExtra("url", postUrl + "?mid=" + mid + "&orderId=" + orderid);
            intent.putExtra("data", postData.toString());
            intent.setClass(this, WebViewActivity.class);
            startActivity(intent);
//            webview.postUrl(postUrl + "?mid=" + mid + "&orderId=" + orderid,
//                    postData.toString().getBytes());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") +
                    data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }

}
