package com.qiangdong.chat.ui.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lqr.imagepicker.ImagePicker;
import com.qiangdong.chat.R;

import java.util.List;

public class SendTransAdapter extends RecyclerView.Adapter<SendTransAdapter.ViewHoder> {
    Activity mContext;
    List<String> data;

    public SendTransAdapter(Activity mContext, List<String> list) {
        this.mContext = mContext;
        this.data = list;
    }
//    public SendTransAdapter(int layoutResId, @Nullable List<String> data, Context context) {
//        super(layoutResId, data);
//        this.mContext = context;
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, String item) {
//        ImageView image = helper.getView(R.id.image);
//        if (helper.getPosition() == mData.size() - 1) {
//            Glide.with(mContext).asBitmap().error(R.mipmap.default_image).load(R.mipmap.ic_add).into(image);
//        } else {
//            Glide.with(mContext).asBitmap().error(R.mipmap.default_image).load(item).into(image);
//        }
//
//    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trens_images, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        if (position == data.size()) {
            holder.imageView.setImageResource(R.mipmap.ic_add);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.picker().enableMultiMode(11).pick(mContext, 100);
                }
            });
        } else {
            Glide.with(mContext).asBitmap().load(data.get(position)).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }


}
