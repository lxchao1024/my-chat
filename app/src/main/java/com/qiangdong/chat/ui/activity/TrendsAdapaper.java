package com.qiangdong.chat.ui.activity;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.userinfo.TrendsBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.widget.CircleImageView;


import java.util.List;

public class TrendsAdapaper extends BaseQuickAdapter<TrendsBean.TrendsInfo.TrendsContext, BaseViewHolder> {

    Context mContext;
    UserInfoBean.UserBean userBean;

    public TrendsAdapaper(int layoutResId, @Nullable List<TrendsBean.TrendsInfo.TrendsContext> data, Context context, UserInfoBean.UserBean userBean) {
        super(layoutResId, data);
        this.mContext = context;
        this.userBean = userBean;
    }

    @Override
    protected void convert(BaseViewHolder helper, TrendsBean.TrendsInfo.TrendsContext item) {
        CircleImageView heanderImg = helper.getView(R.id.header_img);
        TextView userName = helper.getView(R.id.user_name);
        TextView sexAge = helper.getView(R.id.sex_age);
        TextView trendsText = helper.getView(R.id.trends_context);
        TextView trendsTime = helper.getView(R.id.trends_time);
        RecyclerView images = helper.getView(R.id.images);

        Glide.with(mContext).asBitmap().load(userBean.getHeadImg()).error(R.mipmap.default_header).into(heanderImg);
        userName.setText(userBean.getNickname());
        sexAge.setText(userBean.getAge());
        trendsText.setText(item.context);
        trendsTime.setText(item.createTime);
        setImages(images, item.image);
    }

    private void setImages(RecyclerView recyclerView, List<String> list) {
        ImageAdapter adapter = new ImageAdapter(R.layout.trens_images, list, mContext);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
