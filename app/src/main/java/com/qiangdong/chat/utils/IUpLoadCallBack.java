package com.qiangdong.chat.utils;

public interface IUpLoadCallBack {
    void onSuccess(String remoteUrl);

    void onFailed(String message);

    void onProgress(final long uploaded, final long total);

}
