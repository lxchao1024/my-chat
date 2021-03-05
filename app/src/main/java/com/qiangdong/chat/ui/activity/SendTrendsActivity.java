package com.qiangdong.chat.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.userinfo.TrendsBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IUserInfoApi;
import com.qiangdong.chat.utils.FileManager;
import com.qiangdong.chat.utils.IUpLoadCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendTrendsActivity extends WfcBaseActivity {


    @BindView(R.id.trend_content)
    EditText trendContent;
    @BindView(R.id.recy_image)
    RecyclerView recyImage;

    @Override
    protected int contentLayout() {
        return R.layout.activity_send_trends;
    }

    SendTransAdapter adapter;
    List<String> list = new ArrayList<>();
    private UserInfoBean.UserBean userBean;

    @Override
    protected void afterViews() {
        super.afterViews();
        userBean = (UserInfoBean.UserBean) getIntent().getSerializableExtra("userInfo");
        setTitle("发表动态");
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyImage.setLayoutManager(layoutManager);
        adapter = new SendTransAdapter(this, list);
        recyImage.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            for (ImageItem item : images) {
                list.add(item.path);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int menu() {
        return R.menu.saveuserinfo;
    }

    List<String> remoteUrls = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            uploadImag(0);

        }
        return true;
    }

    @SuppressLint("CheckResult")
    private void saveTrends() {
        String context = trendContent.getText().toString();
        RetrofitFactory.getRetrofit().create(IUserInfoApi.class).sendDynamic(context, remoteUrls, Integer.parseInt(userBean.getUserId()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(treandBean -> treandBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        Toast.makeText(this, "发表成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, throwable -> {
                    ErrorAction.print(throwable);
                });

    }

    private void uploadImag(final int positon) {
        if (positon > list.size() - 1) {
            if (remoteUrls.size() == list.size()) {
                Log.e("upload", "all image up load successed");
                saveTrends();
            }
        } else {
            String str = list.get(positon);
            File file = new File(str);
            String fileName = file.getName();
            FileManager.uploadFile(fileName, str, new IUpLoadCallBack() {
                int i = positon;

                @Override
                public void onSuccess(String remoteUrl) {
                    remoteUrls.add(remoteUrl);
                    uploadImag(++i);
                    Log.e("upload", fileName);
                }

                @Override
                public void onFailed(String message) {

                }

                @Override
                public void onProgress(long uploaded, long total) {

                }
            });
        }
    }
}
