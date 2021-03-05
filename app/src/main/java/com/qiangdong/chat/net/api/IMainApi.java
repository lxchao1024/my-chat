package com.qiangdong.chat.net.api;

import com.qiangdong.chat.bean.main.HelloBean;
import com.qiangdong.chat.bean.main.MainBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IMainApi {
    @POST("home/user/list")
    Observable<MainBean> getMainData(
            @Query("pageNo") int pageNo,
            @Query("userId") String userId,
            @Query("pageSize") int pageSize);

    @POST("message/sayHello")
    Observable<HelloBean> sayHello(
            @Query("userId") int userId,
            @Query("channel") String channel,
            @Query("receiveId") int receiveId);

}
