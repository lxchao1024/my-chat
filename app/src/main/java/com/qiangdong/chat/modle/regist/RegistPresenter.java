package com.qiangdong.chat.modle.regist;

import android.annotation.SuppressLint;

import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.bean.login.LoginBean;
import com.qiangdong.chat.bean.regist.RegistBean;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IRegistApi;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegistPresenter implements IRegist.Presenter {
    private IRegist.View view;

    RegistPresenter(IRegist.View view) {
        this.view = view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void doLoadData(String... jokeId_Count) {
        /**
         * 对参数进行处理然后发出请求
         */
//
//        RetrofitFactory.getRetrofit().create(IRegistApi.class).regist("", "", "")
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .map(new Function<RegistBean, List<RegistBean>>() {
//                    @Override
//                    public List<RegistBean> apply(@NonNull RegistBean jokeCommentBean) throws Exception {
//                        return null;
//                    }
//                })
//                .compose(view.<List<RegistBean>>bindToLife())
//                .subscribe(new Consumer<List<RegistBean>>() {
//                    @Override
//                    public void accept(@NonNull List<RegistBean> recentCommentsBeen) throws Exception {
//                        if (recentCommentsBeen.size() > 0) {
//                            doSetAdapter(recentCommentsBeen);
//                        } else {
//                            doShowNoMore();
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        doShowNetError();
//                        ErrorAction.print(throwable);
//                    }
//                });
    }

    @Override
    public void doLoadMoreData() {

    }

    @Override
    public void doSetAdapter(List<RegistBean> registBeans) {

    }

    @Override
    public void doShowNoMore() {

    }

    @Override
    public void doRefresh() {

    }

    @Override
    public void doShowNetError() {

    }
}
