package com.qiangdong.chat;

import androidx.annotation.NonNull;

import com.qiangdong.chat.bean.LoadingBean;
import com.qiangdong.chat.bean.LoadingEndBean;
import com.qiangdong.chat.bean.regist.RegistBean;
import com.qiangdong.chat.binder.LoadingEndViewBinder;
import com.qiangdong.chat.binder.LoadingViewBinder;
import com.qiangdong.chat.binder.regist.RegistBinder;

import me.drakeet.multitype.MultiTypeAdapter;

public class Register {
    public static void registerRegistItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(RegistBean.class, new RegistBinder());
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }
}
