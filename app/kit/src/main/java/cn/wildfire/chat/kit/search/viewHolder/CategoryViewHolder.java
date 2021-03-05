package cn.wildfire.chat.kit.search.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qiangdong.chat.R;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.categoryTextView)
    TextView categoryTextView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(String category) {
        if (TextUtils.isEmpty(category)) {
            categoryTextView.setVisibility(View.GONE);
            return;
        }
        categoryTextView.setVisibility(View.VISIBLE);
        categoryTextView.setText(category);
    }
}
