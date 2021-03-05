package com.qiangdong.chat.ui.activity;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiangdong.chat.R;
import java.util.List;

public class PicturesAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public PicturesAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.pickture);
        Glide.with(mContext).asBitmap().error(R.mipmap.errorimg).load(item).into(imageView);
    }
}
