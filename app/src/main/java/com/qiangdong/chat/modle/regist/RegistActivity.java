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
                // ??????????????????
                .setRangDate(-2209017600000L, 7258089600000L)
                // ??????pickerview??????
                .setInterceptor((pickerView, params) -> {
                    pickerView.setVisibleItemCount(3);
                    // ???????????????????????????
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
        // ???????????? ??????????????????????????????api????????????
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())//??????.PictureMimeType.ofAll()?????????.ofImage()?????????.ofVideo()?????????.ofAudio()
////                .theme()//????????????(????????????????????????) ????????????demo values/styles??? ?????????R.style.picture.white.style
////                .maxSelectNum(1)// ???????????????????????? int
////                .minSelectNum(1)// ?????????????????? int
//                .imageSpanCount(4)// ?????????????????? int
//                .selectionMode(PictureConfig.SINGLE)// ?????? or ?????? PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewImage(true)// ????????????????????? true or false
////                .previewVideo()// ????????????????????? true or false
////                .enablePreviewAudio() // ????????????????????? true or false
//                .isCamera(true)// ???????????????????????? true or false
//                .imageFormat(PictureMimeType.PNG)// ??????????????????????????????,??????jpeg
//                .isZoomAnim(true)// ?????????????????? ???????????? ??????true
//                .sizeMultiplier(0.5f)// glide ?????????????????? 0~1?????? ????????? .glideOverride()??????
//                .setOutputCameraPath("/CustomPath")// ???????????????????????????,?????????
//                .enableCrop(false)// ???????????? true or false
//                .compress(false)// ???????????? true or false
////                .glideOverride()// int glide ???????????????????????????????????????????????????????????????????????????????????????
////                .withAspectRatio()// int ???????????? ???16:9 3:2 3:4 1:1 ????????????
//                .hideBottomControls(false)// ????????????uCrop??????????????????????????? true or false
//                .isGif(false)// ????????????gif?????? true or false
////                .compressSavePath(getPath())//????????????????????????
//                .freeStyleCropEnabled(false)// ???????????????????????? true or false
//                .circleDimmedLayer(false)// ?????????????????? true or false
//                .showCropFrame(false)// ?????????????????????????????? ???????????????????????????false   true or false
//                .showCropGrid(false)// ?????????????????????????????? ???????????????????????????false    true or false
//                .openClickSound(false)// ???????????????????????? true or false
////                .selectionMedia()// ???????????????????????? List<LocalMedia> list
//                .previewEggs(true)// ??????????????? ????????????????????????????????????(???????????????????????????????????????????????????) true or false
////                .cropCompressQuality()// ?????????????????? ??????90 int
//                .minimumCompressSize(100)// ??????100kb??????????????????
//                .synOrAsy(true)//??????true?????????false ?????? ????????????
////                .cropWH()// ??????????????????????????????????????????????????????????????? int
////                .rotateEnabled() // ??????????????????????????? true or false
////                .scaleEnabled()// ????????????????????????????????? true or false
////                .videoQuality()// ?????????????????? 0 or 1 int
////                .videoMaxSecond(15)// ??????????????????????????????or?????????????????? int
////                .videoMinSecond(10)// ??????????????????????????????or?????????????????? int
////                .recordVideoSecond()//?????????????????? ??????60s int
////                .isDragFrame(false)// ????????????????????????(??????)
//                .forResult(PictureConfig.CHOOSE_REQUEST);//????????????onActivityResult code
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
