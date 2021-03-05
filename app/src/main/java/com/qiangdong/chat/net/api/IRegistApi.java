package com.qiangdong.chat.net.api;

import com.qiangdong.chat.bean.regist.RegistBean;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRegistApi {
    /**
     * 获取段子正文内容
     * http://www.toutiao.com/api/article/feed/?category=essay_joke&as=A115C8457F69B85&cp=585F294B8845EE1
     */
    @POST("/user/register")
    Observable<RegistBean> regist(
            @Query("clientId") String clientId,
            @Query("nickname") String maxBehotTime,
            @Query("birthday") String birthday,
            @Query("countryCode") String cp,
            @Query("gender") int gender,
            @Query("headImg") String imgurl);
}
