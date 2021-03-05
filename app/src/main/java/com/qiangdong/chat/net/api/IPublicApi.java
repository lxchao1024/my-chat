package com.qiangdong.chat.net.api;


import com.qiangdong.chat.bean.ConfigBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPublicApi {
    /**
     * 获取段子正文内容
     * http://www.toutiao.com/api/article/feed/?category=essay_joke&as=A115C8457F69B85&cp=585F294B8845EE1
     */
    @GET("/home/initialize/info")
    Observable<ConfigBean> getToken(
            @Query("userId") String maxBehotTime);

}
