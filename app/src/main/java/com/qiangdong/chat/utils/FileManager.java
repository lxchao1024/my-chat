package com.qiangdong.chat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.qiangdong.chat.Constant;
import com.qiangdong.chat.MyApp;
import com.qiangdong.chat.R;
import com.qiangdong.chat.net.RetrofitFactory;
import com.qiangdong.chat.net.api.DownloadApi;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileManager {
    //    private static String base_img_url = "http://pzpgpaedo.bkt.clouddn.com/";//正式环境
    private static String base_img_url = "http://imagetest.qidianshikong.com/";//测试环境

    public static void uploadFile(String imageName, String filePath, IUpLoadCallBack callBack) {
//        String imgName = "headerImg" + System.currentTimeMillis() + ".png";
        /**
         * AccessKey： Tlja9osp7x75EooU4FAYsOHdWeW_bKl__5lqElyB
         * SecretKey： LZxFd6eNWWTyzlicJL8eT-CTbxxQf1K1mQ8Ha5zt
         */
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
//                .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config, 3);//配置3个线程数并发上传；不配置默认为3，只针对file.size>4M生效。线程数建议不超过5，上传速度主要取决于上行带宽，带宽很小的情况单线程和多线程没有区别
        if (!TextUtils.isEmpty(Constant.UPTOKEN)) {
            File data = new File(filePath);
            if (data.exists()) {
                uploadManager.put(data, imageName, Constant.UPTOKEN,
                        (key, info, res) -> {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {
                                callBack.onSuccess(base_img_url + imageName);
//                                PictureFileUtils.deleteCacheDirFile(MyApp.getContext());
//                                Toast.makeText(MyApp.getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            } else {
                                callBack.onFailed(MyApp.getContext().getString(R.string.upload_faild));
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
//                                Toast.makeText(MyApp.getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }, null);

            } else {
                callBack.onFailed(MyApp.getContext().getString(R.string.file_not_exit));
//                Toast.makeText(MyApp.getContext(), "文件不存在", Toast.LENGTH_SHORT).show();
            }
        }
    }


    ////调用系统的安装方法
    private static void installAPK(File savedFile, Context context) {
        //调用系统的安装方法
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, "com.qiangdong.chat.fileprovider", savedFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(savedFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private static String apkPath = Environment.getExternalStorageDirectory() + File.separator + "chat.apk";

    public static void downloadAPK(Context context) {
        Call<ResponseBody> call = RetrofitFactory.getRetrofit().create(DownloadApi.class).downloadFile("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file = new File(apkPath);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();

                    installAPK(file, context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
