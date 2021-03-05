package com.qiangdong.chat.modle.main;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiangdong.chat.BaseApp;
import com.qiangdong.chat.MyApp;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.main.MainBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.modle.base.BaseListFragment;
import com.qiangdong.chat.ui.activity.UserStateActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseListFragment<IMainFragment.Presenter> implements IMainFragment.View, BaseQuickAdapter.OnItemClickListener {
    private static final String TAG = "MainFragment";

    @BindView(R.id.recy_main)
    RecyclerView recyMain;
    @BindView(R.id.refresh_mine)
    SmartRefreshLayout mRefreshLayout;

    private String userId;
    private int pageNo = 0;
    private int pageSize = 10;
    private MyRecycleStaggerAdapter adapter;
    private boolean isRefresh = false;

    public MainFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        pageNo = (int) (Math.random() * 10 + 1);
        presenter.doLoadData(pageNo, userId, pageSize);
        isRefresh = true;
    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_one;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        recyMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(MainFragment.this).resumeRequests();
                } else {
                    Glide.with(MainFragment.this).pauseRequests();
                }
            }
        });
    }

    @Override
    protected void initData() throws NullPointerException {
//        StaggerLoadData(true, true);
        //创建适配器Adapter对象  参数：1.上下文2.数据加载集合
        adapter = new MyRecycleStaggerAdapter(R.layout.item_main, mainUserItems);
        adapter.setPreLoadNumber(3);
        adapter.setOnItemClickListener(this);
        //new Stagger布局管理器,布局管理器所需参数:1.规定显示几列 2.item排列方向
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        //通过布局管理器控制条目排列的顺序  true:反向显示 false:正向显示
//        staggeredGridLayoutManager.setReverseLayout(false);
        recyMain.setLayoutManager(staggeredGridLayoutManager);
        recyMain.addItemDecoration(new SpaceItemDecoration(15));
        // 设置适配器
        recyMain.setAdapter(adapter);
        Bundle bundle = getArguments();
        userId = bundle.getString("userId");
        onLoadData();
    }

    @Override
    public void fetchData() {
    }

    @Override
    public void onShowLoading() {
    }

    @Override
    public void onHideLoading() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onShowNetError() {
    }

    @Override
    public void setPresenter(IMainFragment.Presenter presenter) {
        if (null == presenter) {
            this.presenter = new MainFragmentPresenter(this);
        }
    }

    private List<UserInfoBean.UserBean> mainUserItems = new ArrayList<>();

    @Override
    public void onSetAdapter(List<?> list) {
        List<MainBean.MainDataBean> data = (List<MainBean.MainDataBean>) list;
        if (isRefresh) {
            mainUserItems.clear();
            adapter.notifyDataSetChanged();
        }
        mainUserItems.addAll(data.get(0).getMainUserItem());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadData() {
        pageNo = 0;
        presenter.doLoadData(pageNo, userId, pageSize);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo++;
        presenter.doLoadData(pageNo, userId, pageSize);
        isRefresh = false;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        UserInfoBean.UserBean result = (UserInfoBean.UserBean) adapter.getData().get(position);

//        Conversation conversation = new Conversation(Conversation.ConversationType.Single, String.valueOf(result.getUserId()));
//        Intent intent = new Intent(getActivity(), ConversationActivity.class);
//        intent.putExtra("conversation", conversation);
//        getActivity().startActivity(intent);

//        MainBean.MainDataBean.MainUserItem result = (MainBean.MainDataBean.MainUserItem) adapter.getData().get(position);
        Intent intent = new Intent();
        intent.setClass(BaseApp.getContext(), UserStateActivity.class);
        intent.putExtra("user", result);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        MyApp.getContext().startActivity(intent);
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = 10;
            outRect.top = 20;
        }
    }
}
