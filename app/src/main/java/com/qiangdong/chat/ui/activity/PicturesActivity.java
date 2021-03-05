package com.qiangdong.chat.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.main.MainBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicturesActivity extends AppCompatActivity {

    @BindView(R.id.img_pager)
    ViewPager imgPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        int postion = getIntent().getIntExtra("position", 0);
        UserInfoBean.UserBean user = (UserInfoBean.UserBean) getIntent().getSerializableExtra("user");
        List<View> views = new ArrayList<>();
        assert user != null;
        for (String url : user.getAlbum()) {
            ImageView imageView = new ImageView(this);
            Glide.with(this).asBitmap().load(url).error(R.mipmap.errorimg).into(imageView);
            views.add(imageView);
        }
        ImgPagerAdapter adapter = new ImgPagerAdapter(views);
        imgPager.setAdapter(adapter);
        imgPager.setCurrentItem(postion);
        imgPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == views.size() - 1 && positionOffsetPixels == 0) {
                    Toast.makeText(PicturesActivity.this, "已经是最后一张了", Toast.LENGTH_SHORT).show();
                } else if (position == 0 && positionOffsetPixels == 0) {
                    Toast.makeText(PicturesActivity.this, "已经是第一张了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}
