package com.qiangdong.chat.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.main.MainBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IUserInfoApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class UserStateActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {
    private static final String TAG = "UserStateActivity";
    @BindView(R.id.user_header)
    ImageView userHeader;
    @BindView(R.id.tv_fans_number)
    TextView tvFansNumber;
    @BindView(R.id.tv_follow_number)
    TextView tvFollowNumber;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.img_sex)
    ImageView imgSex;
    @BindView(R.id.tv_user_age)
    TextView tvUserAge;
    @BindView(R.id.recycler_pictures)
    RecyclerView recyclerPictures;
    UserInfoBean.UserBean user;
    String userId;
    @BindView(R.id.btn_follow)
    Button btnFollow;
    @BindView(R.id.btn_sendMessage)
    Button btnSendMessage;
    private List<String> defualtPictures = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        user = (UserInfoBean.UserBean) getIntent().getSerializableExtra("user");
        userId = getIntent().getStringExtra("userId");
        initView();
    }

    @SuppressLint("CheckResult")
    private void initView() {
        if (user == null) {
            RetrofitFactory.getRetrofit().create(IUserInfoApi.class).getUserInfo(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(token -> token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                        user = bean.getData().get(0);
                        Glide.with(this).asBitmap().load(user.getHeadImg()).error(R.mipmap.errorimg).into(userHeader);
                        tvUserName.setText(user.getNickname());
                        tvUserAge.setText(String.valueOf(user.getAge()));
                        tvFansNumber.setText(String.valueOf(user.getFollowCount()));
                        tvFollowNumber.setText(String.valueOf(user.getFollowCount()));
                        imgSex.setImageResource(user.getGender() == 0 ? R.drawable.woman : R.drawable.man);
                        if (user.getAlbum() == null || user.getAlbum().size() == 0) {
                            defualtPictures.add("http://imagetest.qidianshikong.com/IND/48.jpg");
                            defualtPictures.add("http://imagetest.qidianshikong.com/IND/49.jpg");
                            defualtPictures.add("http://imagetest.qidianshikong.com/IND/43.jpg");
                            user.setAlbum(defualtPictures);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerPictures.setLayoutManager(layoutManager);
                        PicturesAdapter adapter = new PicturesAdapter(R.layout.item_pictures, user.getAlbum());
                        recyclerPictures.setAdapter(adapter);
                        adapter.setOnItemClickListener(this);
                    }, ErrorAction.error());
        } else {
            Glide.with(this).asBitmap().load(user.getHeadImg()).error(R.mipmap.errorimg).into(userHeader);
            tvUserName.setText(user.getNickname());
            tvUserAge.setText(String.valueOf(user.getAge()));
            tvFansNumber.setText(String.valueOf(user.getFollowCount()));
            tvFollowNumber.setText(String.valueOf(user.getFollowCount()));
            imgSex.setImageResource(user.getGender() == 0 ? R.drawable.woman : R.drawable.man);
            if (user.getAlbum() == null || user.getAlbum().size() == 0) {
                defualtPictures.add("http://imagetest.qidianshikong.com/IND/48.jpg");
                defualtPictures.add("http://imagetest.qidianshikong.com/IND/49.jpg");
                defualtPictures.add("http://imagetest.qidianshikong.com/IND/43.jpg");
                user.setAlbum(defualtPictures);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerPictures.setLayoutManager(layoutManager);
            PicturesAdapter adapter = new PicturesAdapter(R.layout.item_pictures, user.getAlbum());
            recyclerPictures.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
        }

    }


    MaterialDialog dialog;

    @Override
    protected void onStart() {
        super.onStart();
        dialog = new MaterialDialog.Builder(this)
                .content("关注中...")
                .progress(true, 10)
                .cancelable(false)
                .build();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.setClass(this, PicturesActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("user", user);
        startActivity(intent);
    }


    @OnClick({R.id.btn_follow, R.id.btn_sendMessage})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_follow:
                dialog.show();
                follow();
                break;
            case R.id.btn_sendMessage:
                Conversation conversation = new Conversation(Conversation.ConversationType.Single, String.valueOf(user.getUserId()));
                Intent intent = new Intent(this, ConversationActivity.class);
                intent.putExtra("conversation", conversation);
                intent.putExtra("conversationTitle", user.getNickname());
                intent.putExtra("user", user);
                startActivity(intent);
                break;
        }

    }

    @SuppressLint("CheckResult")
    private void follow() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        int userid = Integer.parseInt(sp.getString("id", "0"));
        RetrofitFactory.getRetrofit().create(IUserInfoApi.class).follow(Integer.parseInt(user.getUserId()), userid)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(token -> token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    dialog.dismiss();
                    if (bean.getCode() == 200) {
                        btnFollow.setText("已关注");
                        btnFollow.setEnabled(false);
                        Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
                    } else if (bean.getCode() == 105) {
                        btnFollow.setText("已关注");
                        btnFollow.setEnabled(false);
                        Toast.makeText(this, "已关注过该用户", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "关注失败", Toast.LENGTH_SHORT).show();
                    }
                }, ErrorAction.error());
    }

}
