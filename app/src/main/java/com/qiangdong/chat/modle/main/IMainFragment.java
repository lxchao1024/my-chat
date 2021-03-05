package com.qiangdong.chat.modle.main;

import com.qiangdong.chat.bean.main.MainBean;
import com.qiangdong.chat.modle.base.IBaseListView;
import com.qiangdong.chat.modle.base.IBasePresenter;
import com.qiangdong.chat.modle.base.IBaseView;

import java.util.List;

public interface IMainFragment {

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
        void doLoadData(int pageNo,String userId,int pageSize);

        /**
         * 再起请求数据
         */
        void doLoadMoreData();

        /**
         * 设置适配器
         */
        void doSetAdapter(List<MainBean.MainDataBean> commentsBeanList);

        /**
         * 加载完毕
         */
        void doShowNoMore();
    }
}
