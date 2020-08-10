/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.neo.kit.upgrade;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.neo.kit.upgrade.entity.DownloadEntity;
import com.neo.kit.upgrade.entity.UpdateError;
import com.neo.kit.upgrade.listener.OnInstallListener;
import com.neo.kit.upgrade.listener.OnUpdateFailureListener;
import com.neo.kit.upgrade.listener.impl.DefaultInstallListener;
import com.neo.kit.upgrade.listener.impl.DefaultUpdateFailureListener;
import com.neo.kit.upgrade.proxy.IUpdateChecker;
import com.neo.kit.upgrade.proxy.IUpdateDownloader;
import com.neo.kit.upgrade.proxy.IUpdateHttpService;
import com.neo.kit.upgrade.proxy.IUpdateParser;
import com.neo.kit.upgrade.proxy.IUpdatePrompter;

import java.io.File;
import java.util.Map;

import static com.neo.kit.upgrade.entity.UpdateError.ERROR.INSTALL_FAILED;

/**
 * 内部版本更新参数的获取
 *
 * @author xuexiang
 * @since 2018/7/10 下午4:27
 */
public final class _XUpdate {
    private static final String TAG = _XUpdate.class.getSimpleName();
    /**
     * 标志当前更新提示是否已显示
     */
    private static boolean sIsShowUpdatePrompter = false;

    public static void setIsShowUpdatePrompter(boolean isShowUpdatePrompter) {
        _XUpdate.sIsShowUpdatePrompter = isShowUpdatePrompter;
    }

    public static boolean isShowUpdatePrompter() {
        return _XUpdate.sIsShowUpdatePrompter;
    }

    //===========================属性设置===================================//

    public static Map<String, Object> getParams() {
        return FpUpdate.get().mParams;
    }

    public static IUpdateHttpService getIUpdateHttpService() {
        return FpUpdate.get().mIUpdateHttpService;
    }

    public static IUpdateChecker getIUpdateChecker() {
        return FpUpdate.get().mIUpdateChecker;
    }

    public static IUpdateParser getIUpdateParser() {
        return FpUpdate.get().mIUpdateParser;
    }

    public static IUpdatePrompter getIUpdatePrompter() {
        return FpUpdate.get().mIUpdatePrompter;
    }

    public static IUpdateDownloader getIUpdateDownLoader() {
        return FpUpdate.get().mIUpdateDownloader;
    }

    public static boolean isGet() {
        return FpUpdate.get().mIsGet;
    }

    public static boolean isWifiOnly() {
        return FpUpdate.get().mIsWifiOnly;
    }

    public static String getApkCacheDir() {
        return FpUpdate.get().mApkCacheDir;
    }

    //===========================apk安装监听===================================//

    public static OnInstallListener getOnInstallListener() {
        return FpUpdate.get().mOnInstallListener;
    }

    /**
     * 开始安装apk文件
     *
     * @param context 传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile apk文件
     */
    public static void startInstallApk(@NonNull Context context, @NonNull File apkFile) {
        startInstallApk(context, apkFile, new DownloadEntity());
    }

    /**
     * 开始安装apk文件
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    public static void startInstallApk(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
        Log.d(TAG, "开始安装apk文件, 文件路径:" + apkFile.getAbsolutePath() + ", 下载信息:" + downloadEntity);
        if (onInstallApk(context, apkFile, downloadEntity)) {
            onApkInstallSuccess(); //静默安装的话，不会回调到这里
        } else {
            onUpdateError(INSTALL_FAILED);
        }
    }

    /**
     * 安装apk
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    private static boolean onInstallApk(Context context, File apkFile, DownloadEntity downloadEntity) {
        if (FpUpdate.get().mOnInstallListener == null) {
            FpUpdate.get().mOnInstallListener = new DefaultInstallListener();
        }
        return FpUpdate.get().mOnInstallListener.onInstallApk(context, apkFile, downloadEntity);
    }

    /**
     * apk安装完毕
     */
    private static void onApkInstallSuccess() {
        if (FpUpdate.get().mOnInstallListener == null) {
            FpUpdate.get().mOnInstallListener = new DefaultInstallListener();
        }
        FpUpdate.get().mOnInstallListener.onInstallApkSuccess();
    }

    //===========================更新出错===================================//

    public static OnUpdateFailureListener getOnUpdateFailureListener() {
        return FpUpdate.get().mOnUpdateFailureListener;
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     */
    public static void onUpdateError(int errorCode) {
        onUpdateError(new UpdateError(errorCode));
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     * @param message
     */
    public static void onUpdateError(int errorCode, String message) {
        onUpdateError(new UpdateError(errorCode, message));
    }

    /**
     * 更新出现错误
     *
     * @param updateError
     */
    public static void onUpdateError(@NonNull UpdateError updateError) {
        if (FpUpdate.get().mOnUpdateFailureListener == null) {
            FpUpdate.get().mOnUpdateFailureListener = new DefaultUpdateFailureListener();
        }
        FpUpdate.get().mOnUpdateFailureListener.onFailure(updateError);
    }

}
