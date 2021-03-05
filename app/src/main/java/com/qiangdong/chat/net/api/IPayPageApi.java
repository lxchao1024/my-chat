package com.qiangdong.chat.net.api;

import com.qiangdong.chat.bean.payvip.PayPageBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPayPageApi {

    @GET("/pay/getPageInfo")
    Observable<PayPageBean> payPageInfo(
            @Query("channel") String payId,//渠道号
            @Query("userId") int payType);//用户ID
}
