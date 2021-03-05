package com.qiangdong.chat.modle.regist;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.qiangdong.chat.ErrorAction;
import com.qiangdong.chat.R;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.IDefualtNickName;
import com.qiangdong.chat.net.api.IRegistApi;
import com.qiangdong.chat.ui.login.LoginActivity;
import com.qiangdong.chat.ui.main.MainActivity;
import com.qiangdong.chat.utils.FileManager;
import com.qiangdong.chat.utils.IUpLoadCallBack;
import com.qiangdong.chat.widget.CircleImageView;

import org.jaaksi.pickerview.picker.TimePicker;
import org.jaaksi.pickerview.topbar.ITopBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegistActivity extends WfcBaseActivity implements RadioGroup.OnCheckedChangeListener, TimePicker.OnTimeSelectListener {
    private static final String TAG = "RegistActivity";
    @BindView(R.id.btn_regist)
    Button btnRegist;
    @BindView(R.id.edit_nickName)
    EditText editNickName;
    @BindView(R.id.rad_nan)
    RadioButton radNan;
    @BindView(R.id.rad_nv)
    RadioButton radNv;
    @BindView(R.id.rad_sex)
    RadioGroup radSex;
    @BindView(R.id.userheader)
    CircleImageView userheader;
    @BindView(R.id.edit_birthday)
    EditText editBirthday;
    @BindView(R.id.btn_to_login)
    Button btnToLogin;


    @Override
    protected int contentLayout() {
        return R.layout.activity_regist;
    }

    @Override
    protected void afterViews() {
        super.afterViews();

        getDefualtNickName();
        selectTime();
        showPicker();
        radSex.setOnCheckedChangeListener(this);
        SimpleDateFormat format = null;
        format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = format.format(new Date(662659200000L));
        editBirthday.setText(strDate);
    }

    private void showPicker() {
        editBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mTimePicker.show();
                }
            }
        });
    }

    @OnClick({R.id.btn_regist, R.id.userheader, R.id.edit_birthday, R.id.btn_to_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_regist:
                String nick = editNickName.getText().toString().trim();
                String psd = editBirthday.getText().toString();
                String coun = "IND";

                if (TextUtils.isEmpty(nick)) {
                    Toast.makeText(this, getString(R.string.hint_edit_nickname), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psd)) {
                    Toast.makeText(this, getString(R.string.hint_edit_psd), Toast.LENGTH_SHORT).show();
                } else {
                    regist(nick, psd, coun, sex, imgName);
                }
                break;
            case R.id.userheader:
                pickImage();
                break;
            case R.id.edit_birthday:
                mTimePicker.show();
                break;
            case R.id.btn_to_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
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
                pickerTool = View.inflate(RegistActivity.this, R.layout.pickerview_toolbar, null);
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
        editBirthday.setText(strDate);
    }

    @SuppressLint("CheckResult")
    private void getDefualtNickName() {
        RetrofitFactory.getRetrofit().create(IDefualtNickName.class).getDefualtNickName("IND")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(registBean -> registBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        editNickName.setText(bean.getData().get(0).getNickname());
                    }
                }, ErrorAction.error());
    }

    int sex = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rad_nan:
                sex = 1;
                radNan.setTextColor(getColor(R.color.pink1));
                radNv.setTextColor(getColor(R.color.Grey1));
                break;
            case R.id.rad_nv:
                sex = 0;
                radNv.setTextColor(getColor(R.color.pink1));
                radNan.setTextColor(getColor(R.color.Grey1));
                break;
        }

    }

    @SuppressLint("CheckResult")
    private void regist(String nick, String psd, String coun, int sex, String imgurl) {
        try {
            String clientId = ChatManagerHolder.gChatManager.getClientId();
            RetrofitFactory.getRetrofit().create(IRegistApi.class).regist(clientId, nick, psd, coun, sex, imgurl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(registBean -> registBean)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                        if (bean.getCode() == 200) {
                            SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                            sp.edit().putString("account", bean.getData().get(0).getAccount())
                                    .putString("id", String.valueOf(bean.getData().get(0).getUserId()))
                                    .putString("token", bean.getData().get(0).getToken())
                                    .apply();
                            Toast.makeText(RegistActivity.this, getString(R.string.regist_success), Toast.LENGTH_SHORT).show();
                            ChatManagerHolder.gChatManager.connect(String.valueOf(bean.getData().get(0).getUserId()), bean.getData().get(0).getToken());
                            Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegistActivity.this, getString(R.string.regist_faild), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, bean.getMessage());
                        }
                    }, ErrorAction.error());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pickImage() {
        // 进入相册 以下是例子：用不到的api可以不写
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
////                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
////                .maxSelectNum(1)// 最大图片选择数量 int
////                .minSelectNum(1)// 最小选择数量 int
//                .imageSpanCount(4)// 每行显示个数 int
//                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewImage(true)// 是否可预览图片 true or false
////                .previewVideo()// 是否可预览视频 true or false
////                .enablePreviewAudio() // 是否可播放音频 true or false
//                .isCamera(true)// 是否显示拍照按钮 true or false
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
//                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
//                .enableCrop(false)// 是否裁剪 true or false
//                .compress(false)// 是否压缩 true or false
////                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
////                .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
//                .isGif(false)// 是否显示gif图片 true or false
////                .compressSavePath(getPath())//压缩图片保存地址
//                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .openClickSound(false)// 是否开启点击声音 true or false
////                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
//                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
////                .cropCompressQuality()// 裁剪压缩质量 默认90 int
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
////                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
////                .rotateEnabled() // 裁剪是否可旋转图片 true or false
////                .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
////                .videoQuality()// 视频录制质量 0 or 1 int
////                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
////                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
////                .recordVideoSecond()//视频秒数录制 默认60s int
////                .isDragFrame(false)// 是否可拖动裁剪框(固定)
//                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        ImagePicker.picker().pick(this, 100);
    }

    String imgName = "headerImg" + System.currentTimeMillis() + ".png";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            String imagePath = ImageUtils.genThumbImgFile(images.get(0).path).getAbsolutePath();
            FileManager.uploadFile(imgName, imagePath, new IUpLoadCallBack() {
                @Override
                public void onSuccess(String remoteUrl) {
                    Glide.with(RegistActivity.this).asBitmap().load(remoteUrl).into(userheader);
                    Toast.makeText(RegistActivity.this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(String message) {
                    Toast.makeText(RegistActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(long uploaded, long total) {

                }
            });
        }
    }

}
