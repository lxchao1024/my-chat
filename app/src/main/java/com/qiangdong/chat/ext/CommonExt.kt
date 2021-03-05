package com.qiangdong.chat.ext

import android.view.View
import com.qiangdong.chat.rx.BaseSubscriber
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @author lixiangchao
 * @date 2019/11/5
 * @version 1.0.0
 */
fun <T> Observable<T>.excute(subscriber: BaseSubscriber<T>) {
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber)
}

/**
  *  扩展点击事件
  */
fun View.click(listener: View.OnClickListener): View {
    setOnClickListener(listener)
    return this
}