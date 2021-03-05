package com.qiangdong.chat.utils;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.qiangdong.chat.BaseApp;
import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IUserInfoApi;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.wildfirechat.UserInfoCallBack;
import cn.wildfirechat.UserSource;
import cn.wildfirechat.model.ModifyMyInfoEntry;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.GeneralCallback;
import cn.wildfirechat.remote.SearchUserCallback;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserSourceImp implements UserSource {
    UserInfo userInfo;
    private UserInfoCallBack callback;
    private volatile static UserSourceImp instance;

    private UserSourceImp() {
    }

    public static UserSourceImp getInstance() {
        if (instance == null) {
            synchronized (UserSourceImp.class) {
                if (instance == null) {
                    instance = new UserSourceImp();
                }
            }
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    @Override
    public UserInfo getUser(String userId, UserInfoCallBack callback) {

        init(userId, callback);
        Log.e("displayName", userInfo.displayName);
        return userInfo;
    }

    @SuppressLint("CheckResult")
    public void init(String userId, UserInfoCallBack callback) {
        RetrofitFactory.getRetrofit().create(IUserInfoApi.class).getUserInfo(userId)
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
                    userInfo = new UserInfo();
                    userInfo.displayName = bean.getNickname();
                    userInfo.uid = bean.getAccount();
                    userInfo.gender = bean.getGender();
                    userInfo.portrait = bean.getHeadImg();
                    userInfo.followCount = bean.getFollowCount();
                    userInfo.fansCount = bean.getFansCount();
                    userInfo.userId=bean.getUserId();
                    if (callback != null)
                        callback.onUserCallback(userInfo);
                }, ErrorAction.error());
    }

    @Override
    public void searchUser(String keyword, SearchUserCallback callback) {

    }

    @Override
    public void modifyMyInfo(List<ModifyMyInfoEntry> values, GeneralCallback callback) {

    }

    public void setCallback(UserInfoCallBack callback) {
        this.callback = callback;
    }
}
