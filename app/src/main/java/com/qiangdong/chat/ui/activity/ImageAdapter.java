package com.qiangdong.chat.ui.activity;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiangdong.chat.R;

import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context mContext;

    public ImageAdapter(int layoutResId, @Nullable List<String> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView image = helper.getView(R.id.image);
        Glide.with(mContext).asBitmap().load(item).error(R.mipmap.default_image).into(image);
    }
}
