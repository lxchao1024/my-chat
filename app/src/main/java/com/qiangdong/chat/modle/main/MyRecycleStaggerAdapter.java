package com.qiangdong.chat.modle.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjt2325.cameralibrary.util.ScreenUtils;
import com.qiangdong.chat.Constant;
import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.main.MainBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IMainApi;
import com.qiangdong.chat.net.api.IRegistApi;
import com.qiangdong.chat.ui.main.MainActivity;

import org.w3c.dom.Text;

import java.util.List;

import cn.wildfire.chat.kit.ChatManagerHolder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyRecycleStaggerAdapter extends BaseQuickAdapter<UserInfoBean.UserBean, BaseViewHolder> {

    //创建构造方法；一个需要接受两个参数，上下文，集合对象（包含了我们所需要的数据
    public MyRecycleStaggerAdapter(int layoutResId, List<UserInfoBean.UserBean> mList) {
        super(layoutResId, mList);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoBean.UserBean item) {
//        helper.setText(R.id.item_stagger_tv_name, item.getNickname());
        ImageView mIcon = helper.getView(R.id.item_stagger_iv_icon);
        RelativeLayout mItemRoot = helper.getView(R.id.item_root);
        final int width = (ScreenUtils.getScreenWidth(mContext) - 44) / 2;
        final int height = (int) (width / 0.8);
        mItemRoot.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        Glide.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .load(item.getHeadImg())
                .transition(BitmapTransitionOptions.withCrossFade())
                .error(R.mipmap.errorimg).into(mIcon);
        ImageView sex = helper.getView(R.id.item_stagger_iv_sex);
        TextView age = helper.getView(R.id.item_stagger_tv_age);
        TextView location = helper.getView(R.id.item_stagger_tv_location);
        age.setText(String.valueOf(item.getAge()));
        sex.setImageResource(item.getGender() == 0 ? R.drawable.woman : R.drawable.man);
        location.setText((int) (Math.random() * 100 + 1) + "km");
        ImageView hello = helper.getView(R.id.item_stagger_iv_say_hello);
        TextView showHello = helper.getView(R.id.item_stagger_tv_say_hello);
        hello.setOnClickListener(v -> {
            sayHello(item, showHello, hello);
        });
    }

    @SuppressLint("CheckResult")
    private void sayHello(UserInfoBean.UserBean item, TextView showHello, ImageView hello) {
        SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        String userId = sp.getString("id", "0");
        RetrofitFactory.getRetrofit().create(IMainApi.class).sayHello(Integer.parseInt(userId), Constant.channelId, Integer.parseInt(item.getUserId()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(registBean -> registBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        Toast.makeText(mContext,"已向对方打招呼，请等待回应",Toast.LENGTH_SHORT).show();
                        showHello.setVisibility(View.VISIBLE);
                        hello.setImageResource(R.mipmap.icon_say_hello_selected);
                    } else {
                        Toast.makeText(mContext,bean.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }, ErrorAction.error());
    }

}
