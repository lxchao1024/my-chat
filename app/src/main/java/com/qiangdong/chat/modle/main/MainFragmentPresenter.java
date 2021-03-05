package com.qiangdong.chat.modle.main;

import android.annotation.SuppressLint;

import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.bean.main.MainBean;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IMainApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainFragmentPresenter implements IMainFragment.Presenter {
    private IMainFragment.View view;

    private List<MainBean.MainDataBean> commentsList = new ArrayList<>();

    public MainFragmentPresenter(IMainFragment.View view) {
        this.view = view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void doLoadData(int pageNo, String userId, int pageSize) {

        RetrofitFactory.getRetrofit().create(IMainApi.class).getMainData(pageNo, userId, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(mainBean -> mainBean.getData())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(view.bindToLife())
                .subscribe(recentCommentsBeen -> {
                    if (recentCommentsBeen.size() > 0) {
                        doSetAdapter(recentCommentsBeen);
                    } else {
                        doShowNoMore();
                    }
                }, throwable -> {
                    doShowNetError();
                    ErrorAction.print(throwable);
                });

    }

    @Override
    public void doLoadMoreData() {
//        doLoadData();
    }

    @Override
    public void doSetAdapter(List<MainBean.MainDataBean> mainDataBeans) {
        mainDataBeans.addAll(mainDataBeans);
        view.onSetAdapter(mainDataBeans);
        view.onHideLoading();
    }

    @Override
    public void doShowNoMore() {
        view.onHideLoading();
        if (commentsList.size() > 0) {
            view.onShowNoMore();
        }
    }

    @Override
    public void doRefresh() {
        if (commentsList.size() != 0) {
            commentsList.clear();
        }
//        doLoadData();
    }

    @Override
    public void doShowNetError() {
        view.onHideLoading();
        view.onShowNetError();
    }
}
