package com.qiangdong.chat.net.api;

import com.qiangdong.chat.bean.payvip.PayBean;
import com.qiangdong.chat.bean.payvip.PayInfoBean;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IPayApi {
    @POST("/pay/dokypay/getInfo")
    Observable<PayBean> payInfo(
            @Query("payId") int payId,//支付金额ID
            @Query("payType") int payType,//支付类型 1dokypay 2 谷歌支付
            @Query("userId") int userId);//用户ID

    String url = "https://gatewaybeta.dokypay.com/clientapi/unifiedorder";

    @POST(url)
    Observable<PayInfoBean> payTest(
            @Query("amount") String amount,//支付金额ID
            @Query("appId") String appId,//支付类型 1dokypay 2 谷歌支付
            @Query("country") String country,//支付类型 1dokypay 2 谷歌支付
            @Query("currency") String currency,//支付类型 1dokypay 2 谷歌支付
            @Query("description") String description,//支付类型 1dokypay 2 谷歌支付
            @Query("merTransNo") String merTransNo,//支付类型 1dokypay 2 谷歌支付
            @Query("notifyUrl") String notifyUrl,//支付类型 1dokypay 2 谷歌支付
            @Query("pmId") String pmId,//支付类型 1dokypay 2 谷歌支付
            @Query("prodName") String prodName,//支付类型 1dokypay 2 谷歌支付
//            @Query("returnUrl") String returnUrl,//支付类型 1dokypay 2 谷歌支付
            @Query("sign") String sign,//支付类型 1dokypay 2 谷歌支付
            @Query("version") String version);//用户ID
}
