package com.qiangdong.chat.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.ui.PayActivity;
import com.qiangdong.chat.ui.activity.AccountInfoActivity;
import com.qiangdong.chat.ui.activity.TrendsActivity;
import com.qiangdong.chat.ui.setting.SettingActivity;
import com.qiangdong.chat.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfire.chat.kit.widget.OptionItemView;
import cn.wildfirechat.UserInfoCallBack;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class MeFragment extends Fragment {

    public static final String TAG = "MeFragment";

    @BindView(R.id.portraitImageView)
    CircleImageView portraitImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.accountTextView)
    TextView accountTextView;

    @BindView(R.id.notificationOptionItemView)
    OptionItemView notificationOptionItem;

    @BindView(R.id.passwordOptionItemView)
    OptionItemView passwordOptionItemView;

    @BindView(R.id.settintOptionItemView)
    OptionItemView settingOptionItem;
    @BindView(R.id.followCount)
    TextView followCount;
    @BindView(R.id.fansCount)
    TextView fansCount;
    @BindView(R.id.trendsOptionItemView)
    OptionItemView trendsOptionItemView;

    private UserViewModel userViewModel;
    private UserInfoBean.UserBean userBean;
//    private UserSourceImp userSourceImp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_me, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
//        userSourceImp = new UserSourceImp();
//        UserSourceImp.getInstance().setCallback(result -> {
//            userInfo = result;
//        });
        //实例化用户model
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        //会员
        notificationOptionItem.setRootLayoutHeight(35, 20);
        notificationOptionItem.getEndImageView().setVisibility(View.VISIBLE);
        notificationOptionItem.setDividerVisibility(View.VISIBLE, 10);
        //动态
        trendsOptionItemView.setRootLayoutHeight(35, 20);
        trendsOptionItemView.getEndImageView().setVisibility(View.VISIBLE);
        trendsOptionItemView.setDividerVisibility(View.VISIBLE, 10);
        //粉丝
        passwordOptionItemView.setRootLayoutHeight(35, 20);
        passwordOptionItemView.setDividerVisibility(View.GONE);
        passwordOptionItemView.getEndImageView().setVisibility(View.VISIBLE);
        //设置
        settingOptionItem.setRootLayoutHeight(35, 20);
        settingOptionItem.setDividerVisibility(View.GONE);
        settingOptionItem.getEndImageView().setVisibility(View.VISIBLE);
//        userSourceImp.getUserInfo(userViewModel.getUserId());

        userBean = new UserInfoBean.UserBean();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            getUserInfo();
        }
    }

    private void getUserInfo() {
        ChatManager.Instance().getUserInfo(userViewModel.getUserId(), false, userInfo -> {
            Glide.with(MeFragment.this).load(userInfo.portrait).apply(new RequestOptions()
                    .placeholder(R.mipmap.avatar_def).centerCrop()).into(portraitImageView);
            nameTextView.setText(userInfo.displayName);
            accountTextView.setText(String.format("%s%s", getString(R.string.my_chat_account), userInfo.uid));
            followCount.setText(String.valueOf(userInfo.followCount));
            fansCount.setText(String.valueOf(userInfo.fansCount));

            userBean.setUserId(userInfo.userId);
            userBean.setAccount(userInfo.uid);
            userBean.setHeadImg(userInfo.portrait);
            userBean.setGender(userInfo.gender);
            userBean.setNickname(userInfo.displayName);
            userBean.setBirthday(userInfo.birthday);
            userBean.setMaritalStatus(userInfo.maritalStatus);
            userBean.setProfile(userInfo.profile);
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        getUserInfo();
//    }

    @OnClick(R.id.portraitImageView)
    void showMyInfo() {
        Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
        intent.putExtra("userInfo", userBean);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.notificationOptionItemView)
    void vip() {
        Intent intent = new Intent(getActivity(), PayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.settintOptionItemView)
    void setting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.trendsOptionItemView)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), TrendsActivity.class);
        intent.putExtra("userInfo", userBean);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
