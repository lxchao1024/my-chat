package com.qiangdong.chat.rx

import io.reactivex.observers.ResourceObserver

/**
 *
 * @author lixiangchao
 * @date 2019/11/5
 * @version 1.0.0
 */
open class BaseSubscriber<T>: ResourceObserver<T>() {
    override fun onError(e: Throwable) {

    }

    override fun onComplete() {
        //关闭加载框、用这种方式关闭的最好用MVP，把BaseView传递进来
    }

    override fun onNext(t: T) {
    }
}