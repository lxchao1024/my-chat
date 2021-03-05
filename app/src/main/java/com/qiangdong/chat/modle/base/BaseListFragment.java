package com.qiangdong.chat.modle.base;


import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.View;

import com.qiangdong.chat.R;
import com.qiangdong.chat.RxBus;
import com.qiangdong.chat.bean.LoadingEndBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public abstract class BaseListFragment<T extends IBasePresenter> extends LazyLoadFragment<T> implements IBaseListView<T>, OnRefreshListener, OnLoadMoreListener {

    public static final String TAG = "BaseListFragment";
    protected SmartRefreshLayout swipeRefreshLayout;
    protected MultiTypeAdapter adapter;
    protected Observable<Integer> observable;

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void onShowLoading() {
    }


    @Override
    public void onHideLoading() {
    }

    @Override
    public void fetchData() {
        observable = RxBus.getInstance().register(BaseListFragment.TAG);
        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onShowNetError() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onShowNoMore() {
    }

    @Override
    public void onRefresh(@androidx.annotation.NonNull RefreshLayout refreshLayout) {
    }


    @Override
    public void onDestroy() {
        RxBus.getInstance().unregister(BaseListFragment.TAG, observable);
        super.onDestroy();
    }

}
