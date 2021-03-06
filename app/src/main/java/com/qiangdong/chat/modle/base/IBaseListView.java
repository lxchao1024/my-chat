package com.qiangdong.chat.modle.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

/**
 * Created by Meiji on 2017/7/5.
 */

public interface IBaseListView<T> extends IBaseView<T> {

    /**
     * 显示加载动画
     */
    void onShowLoading();

    /**
     * 隐藏加载
     */
    void onHideLoading();

    /**
     * 显示网络错误
     */
    void onShowNetError();

    /**
     * 设置 presenter
     */
    void setPresenter(T presenter);

    /**
     * 绑定生命周期
     */
    <T> LifecycleTransformer<T> bindToLife();

    /**
     * 设置适配器
     */
    void onSetAdapter(List<?> list);

    /**
     * 加载完毕
     */
    void onShowNoMore();
}
