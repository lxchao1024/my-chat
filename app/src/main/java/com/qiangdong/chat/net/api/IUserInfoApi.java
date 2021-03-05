package com.qiangdong.chat.net.api;

import com.qiangdong.chat.bean.BaseBean;
import com.qiangdong.chat.bean.userinfo.TrendsBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IUserInfoApi {
    //获取个人信息
    @GET("/user/center/get")
    Observable<UserInfoBean> getUserInfo(
            @Query("userId") String password);

    //关注
    @POST("user/follow/add")
    Observable<BaseBean> follow(
            @Query("followId") int followId,
            @Query("userId") int userId);

    //更新
    @POST("user/center/update")
    Observable<BaseBean> update(
            @Query("birthday") String birthday,
            @Query("gender") int gender,//0:女，1:男，2:未知
            @Query("headImg") String headImg,
            @Query("userId") int userId,
            @Query("maritalStatus") int maritalStatus,//情感状态 0保密 1单身 2热恋 3已婚 4离异
            @Query("nickname") String nickname,
            @Query("profile") String profile,
            @Query("work") String work,
            @Query("height") int height,
            @Query("weight") int weight,
            @Query("hobby") String hobby);


    //用戶動態
    @GET("user/dynamic/list")
    Observable<TrendsBean> getDynamic(
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize,
            @Query("userId") int userId);

    //用戶動態
    @POST("user/dynamic/add")
    Observable<BaseBean> sendDynamic(
            @Query("context") String context,
            @Query("image") List<String> image,
            @Query("userId") int userId);


}
