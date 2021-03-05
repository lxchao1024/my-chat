package com.qiangdong.chat.widget;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.qiangdong.chat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BottomDialog extends DialogFragment {
    private View frView;
    private Window window;
    OnclickResult result;
    List<Map<String, String>> list;

    public BottomDialog(List<Map<String, String>> list, OnclickResult result) {
        this.result = result;
        this.list = list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        frView = inflater.inflate(R.layout.dialog_fr_bottom, null);
        return frView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 下面这些设置必须在此方法(onStart())中才有效

        window = getDialog().getWindow();
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 设置动画
        window.setWindowAnimations(R.style.bottomDialog);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.width = getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(params);
        ListView listView = frView.findViewById(R.id.item_list);

        listView.setAdapter(new SimpleAdapter(getActivity(), list, R.layout.item_list, new String[]{"item"}, new int[]{R.id.item_text}));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String clicked = list.get(position).get("item");
            result.onClickResult(clicked);
            BottomDialog.this.dismiss();
        });
    }

    public interface OnclickResult {
        void onClickResult(String select);
    }
}
