package com.qiangdong.chat.net.api;

import com.qiangdong.chat.bean.regist.DefuatlNickNameBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IDefualtNickName {

    @GET("user/register/getNickname")
    Observable<DefuatlNickNameBean> getDefualtNickName(
            @Query("countryCode") String password);
}
