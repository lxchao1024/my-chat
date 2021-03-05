package com.qiangdong.chat.net.api;


import com.qiangdong.chat.bean.login.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ILogInApi {
    /**
     * 获取段子正文内容
     * http://www.toutiao.com/api/article/feed/?category=essay_joke&as=A115C8457F69B85&cp=585F294B8845EE1
     */
    @POST("user/login")
    Observable<LoginBean> Login(
            @Query("account") String account,
            @Query("clientId") String clientId,
            @Query("password") String password);

    /**
//     * 获取段子评论
//     * http://m.neihanshequ.com/api/get_essay_comments/?group_id=编号&count=数量&offset=偏移量
//     */
//    @GET("http://m.neihanshequ.com/api/get_essay_comments/?count=20")
//    @Headers({"User-Agent:" + Constant.USER_AGENT_MOBILE})
//    Observable<JokeCommentBean> getJokeComment(
//            @Query("group_id") String groupId,
//            @Query("offset") int offset);
}
