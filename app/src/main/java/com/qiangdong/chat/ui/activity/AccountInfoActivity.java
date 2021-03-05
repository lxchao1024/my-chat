package com.qiangdong.chat.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.login.LoginBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.modle.regist.RegistActivity;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.ILogInApi;
import com.qiangdong.chat.net.api.IUserInfoApi;
import com.qiangdong.chat.ui.login.LoginActivity;
import com.qiangdong.chat.ui.main.MainActivity;
import com.qiangdong.chat.utils.FileManager;
import com.qiangdong.chat.utils.IUpLoadCallBack;
import com.qiangdong.chat.widget.BottomDialog;
import com.qiangdong.chat.widget.CircleImageView;

import org.jaaksi.pickerview.picker.TimePicker;
import org.jaaksi.pickerview.topbar.ITopBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import cn.wildfirechat.model.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AccountInfoActivity extends WfcBaseActivity implements TimePicker.OnTimeSelectListener, BottomDialog.OnclickResult {


    @BindView(R.id.user_header)
    CircleImageView userHeader;
    @BindView(R.id.account_sex)
    TextView accountSex;
    @BindView(R.id.account_nick)
    EditText accountNick;
    @BindView(R.id.account_birth)
    TextView accountBirth;
    @BindView(R.id.account_editsign)
    EditText accountEditsign;
    @BindView(R.id.userMarried)
    TextView userMarried;

    @Override
    protected int contentLayout() {
        return R.layout.activity_account_info;
    }

    private String imgName;
    UserInfoBean.UserBean userInfo;

    @Override
    protected void afterViews() {
        super.afterViews();
        setTitle("个人详情");
        selectTime();
        userInfo = (UserInfoBean.UserBean) getIntent().getSerializableExtra("userInfo");

        Glide.with(this).asBitmap().load(userInfo.getHeadImg()).error(R.mipmap.errorimg).into(userHeader);
        accountNick.setText(userInfo.getNickname());
        accountSex.setText(userInfo.getGender() == 0 ? "女" : "男");
        accountBirth.setText(TextUtils.isEmpty(userInfo.getBirthday()) ? "" : userInfo.getBirthday());
        userMarried.setText(userInfo.getMaritalStatus() == 0 ? "单身" : "离异");
        accountEditsign.setText(userInfo.getProfile());

        imgName = userInfo.getAccount()+System.currentTimeMillis() + ".png";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("AccountInfoActivity", "onDestroy");
        saveInfo();
    }

    @Override
    protected int menu() {
        return R.menu.saveuserinfo;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            saveInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("CheckResult")
    private void saveInfo() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("正在保存...")
                .progress(true, 10)
                .cancelable(false)
                .build();
        dialog.show();
        String birth = accountBirth.getText().toString();
        String sex = accountSex.getText().toString();
        int gender = 0;
        if (sex.equals("男")) {
            gender = 1;
        } else if (sex.equals("未知")) {
            gender = 2;
        }
        String profile = accountEditsign.getText().toString();
        RetrofitFactory.getRetrofit().create(IUserInfoApi.class).update(birth, gender, imgName, Integer.parseInt(userInfo.getUserId()), marriedState, userInfo.getNickname(), profile, "", 0, 0, "")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(token -> token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        Toast.makeText(AccountInfoActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AccountInfoActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }, ErrorAction.error());
    }

    List<Map<String, String>> list = new ArrayList<>();
    private String[] sex = {"男", "女", "未知"};
    private String[] married = {"保密", "单身", "热恋", "已婚", "离异"};//情感状态 0保密 1单身 2热恋 3已婚 4离异

    @OnClick({R.id.user_header, R.id.con_sex, R.id.con_birth, R.id.userMarried})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_header:
                ImagePicker.picker().pick(this, 100);
                break;
            case R.id.con_sex:
                list.clear();
                for (String s : sex) {
                    Map<String, String> map = new HashMap<>();
                    map.put("item", s);
                    list.add(map);
                }
                dialog(list);
                break;
            case R.id.con_birth:
                mTimePicker.show();
                break;
            case R.id.userMarried:
                list.clear();
                for (String s : married) {
                    Map<String, String> map = new HashMap<>();
                    map.put("item", s);
                    list.add(map);
                }
                dialog(list);
                break;
        }
    }

    private void dialog(List<Map<String, String>> list) {
        BottomDialog dialog = new BottomDialog(list, this);
        dialog.show(getSupportFragmentManager(), "account");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            String imagePath = ImageUtils.genThumbImgFile(images.get(0).path).getAbsolutePath();
            FileManager.uploadFile(imgName, imagePath, new IUpLoadCallBack() {
                @Override
                public void onSuccess(String remoteUrl) {
                    Glide.with(AccountInfoActivity.this).asBitmap().load(remoteUrl).into(userHeader);
                    Toast.makeText(AccountInfoActivity.this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(String message) {
                    Toast.makeText(AccountInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(long uploaded, long total) {

                }
            });
        }
    }

    View pickerTool;
    TimePicker mTimePicker;

    private void selectTime() {
        mTimePicker = new TimePicker.Builder(this, TimePicker.TYPE_DATE, this)
                .setSelectedDate(System.currentTimeMillis())
                // 设置时间区间
                .setRangDate(-2209017600000L, 7258089600000L)
                // 设置pickerview样式
                .setInterceptor((pickerView, params) -> {
                    pickerView.setVisibleItemCount(3);
                    // 将年月设置为循环的
                    int type = (int) pickerView.getTag();
                    if (type == TimePicker.TYPE_YEAR || type == TimePicker.TYPE_MONTH) {
                        pickerView.setIsCirculation(true);
                    }
                }).create();
        mTimePicker.setTopBar(new ITopBar() {
            @Override
            public View getTopBarView() {
                pickerTool = View.inflate(AccountInfoActivity.this, R.layout.pickerview_toolbar, null);
                return pickerTool;
            }

            @Override
            public View getBtnCancel() {
                return pickerTool.findViewById(R.id.btn_cancel);
            }

            @Override
            public View getBtnConfirm() {
                mTimePicker.onCancel();
                return pickerTool.findViewById(R.id.btn_confirm);
            }

            @Override
            public TextView getTitleView() {
                return pickerTool.findViewById(R.id.picker_title);
            }
        });

    }

    @Override
    public void onTimeSelect(TimePicker picker, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = format.format(date);
        accountBirth.setText(strDate);

    }

    int marriedState;

    @Override
    public void onClickResult(String select) {
//情感状态 0保密 1单身 2热恋 3已婚 4离异
        if (select.equals("保密")) {
            marriedState = 0;
            userMarried.setText(select);
        } else if (select.equals("单身")) {
            userMarried.setText(select);
            marriedState = 1;
        } else if (select.equals("热恋")) {
            userMarried.setText(select);
            marriedState = 2;
        } else if (select.equals("已婚")) {
            userMarried.setText(select);
            marriedState = 3;
        } else if (select.equals("离异")) {
            userMarried.setText(select);
            marriedState = 4;
        } else if (select.equals("男")) {
            accountSex.setText(select);
        } else if (select.equals("女")) {
            accountSex.setText(select);
        } else if (select.equals("未知")) {
            accountSex.setText(select);
        }

    }
}
