package com.qiangdong.chat.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.userinfo.TrendsBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IUserInfoApi;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.WfcBaseActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TrendsActivity extends WfcBaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.trendsRecycle)
    RecyclerView trendsRecycle;

    int pageNo = 0;
    int pageSize = 10;
    boolean isRefresh = false;

    @Override
    protected int contentLayout() {
        return R.layout.activity_trends;
    }

    private UserInfoBean.UserBean userBean;
    private TrendsAdapaper adapter;
    private List<TrendsBean.TrendsInfo.TrendsContext> list = new ArrayList<>();

    @Override
    protected void afterViews() {
        super.afterViews();
        setTitle("个人动态");
        userBean = (UserInfoBean.UserBean) getIntent().getSerializableExtra("userInfo");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trendsRecycle.setLayoutManager(layoutManager);
        adapter = new TrendsAdapaper(R.layout.item_trends, list, this, userBean);
        trendsRecycle.setAdapter(adapter);
        getTrends();
    }


    @SuppressLint("CheckResult")
    public void getTrends() {
        RetrofitFactory.getRetrofit().create(IUserInfoApi.class).getDynamic(pageNo, pageSize, Integer.parseInt(userBean.getUserId()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(treandBean -> treandBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        List<TrendsBean.TrendsInfo.TrendsContext> data = bean.getData().get(0).result;
                        if (isRefresh) {
                            list.clear();
                        }
                        list.addAll(data);
                        adapter.notifyDataSetChanged();
                    }
                }, throwable -> {
                    ErrorAction.print(throwable);
                });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo++;
        isRefresh = false;
        getTrends();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 0;
        isRefresh = true;
        getTrends();

    }

    @Override
    protected int menu() {
        return R.menu.menu_trends;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_trends) {
            Intent intent = new Intent(this, SendTrendsActivity.class);
            intent.putExtra("userInfo", userBean);
            startActivity(intent);
        }
        return true;
    }
}
