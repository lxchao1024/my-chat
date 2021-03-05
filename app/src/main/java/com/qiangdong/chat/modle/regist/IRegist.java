package com.qiangdong.chat.modle.regist;

import com.qiangdong.chat.bean.regist.RegistBean;
import com.qiangdong.chat.modle.base.IBaseListView;
import com.qiangdong.chat.modle.base.IBasePresenter;

import java.util.List;

public interface IRegist {

    interface View extends IBaseListView<Presenter> {

        /**
         * 请求数据
         */
        void onLoadData();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 请求数据
         */
        void doLoadData(String... jokeId_Count);

        /**
         * 再起请求数据
         */
        void doLoadMoreData();

        /**
         * 设置适配器
         */
        void doSetAdapter(List<RegistBean> commentsBeanList);

        /**
         * 加载完毕
         */
        void doShowNoMore();
    }
}
