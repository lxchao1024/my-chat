package com.qiangdong.chat.binder.regist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.regist.RegistBean;

import me.drakeet.multitype.ItemViewBinder;

public class RegistBinder extends ItemViewBinder<RegistBean, RegistBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return null;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull RegistBean registBean) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
