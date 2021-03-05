package com.qiangdong.chat.net.api;

import com.qiangdong.chat.bean.login.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IFansApi {
    @POST("user/login")
    Observable<LoginBean> Login(
            @Query("account") String account,
            @Query("clientId") String clientId,
            @Query("password") String password);

}
