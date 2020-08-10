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
package com.neo.kit.upgrade.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.neo.kit.upgrade._XUpdate;

import java.io.File;
import java.io.IOException;

import static com.neo.kit.upgrade.entity.UpdateError.ERROR.INSTALL_FAILED;

/**
 * APK安装工具类
 *
 * @author xuexiang
 * @since 2018/7/2 上午1:18
 */
public final class ApkInstallUtils {

    /**
     * apk安装的请求码
     */
    public static final int REQUEST_CODE_INSTALL_APP = 999;

    /**
     * 是否支持静默安装【默认是true】
     */
    private static boolean sSupportSilentInstall = true;

    public static boolean isSupportSilentInstall() {
        return sSupportSilentInstall;
    }

    private ApkInstallUtils() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    /**
     * 自适应apk安装（如果设备有root权限就自动静默安装）
     *
     * @param context
     * @param apkFile apk文件
     * @return
     */
    public static boolean install(Context context, File apkFile) throws IOException {
        return isSupportSilentInstall() ? install(context, apkFile.getCanonicalPath()) : installNormal(context, apkFile.getCanonicalPath());
    }


    /**
     * 自适应apk安装（如果设备有root权限就自动静默安装）
     *
     * @param context
     * @param filePath apk文件的路径
     * @return
     */
    public static boolean install(Context context, String filePath) {
        return installNormal(context, filePath);
    }

    /**
     * 使用系统的意图安装
     *
     * @param context
     * @param filePath file path of package
     * @return whether apk exist
     */
    private static boolean installNormal(Context context, String filePath) {
        File file = FileUtils.getFileByPath(filePath);
        return FileUtils.isFileExists(file) && installNormal(context, file);
    }

    /**
     * 使用系统的意图进行apk安装
     *
     * @param context
     * @param appFile
     * @return
     */
    private static boolean installNormal(Context context, File appFile) {
        try {
            Intent intent = getInstallAppIntent(appFile);
            if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, REQUEST_CODE_INSTALL_APP);
                } else {
                    context.startActivity(intent);
                }
                return true;
            }
        } catch (Exception e) {
            _XUpdate.onUpdateError(INSTALL_FAILED, "使用系统的意图进行apk安装失败！");
        }
        return false;
    }

    /**
     * 获取安装apk的意图
     *
     * @param appFile
     * @return
     */
    public static Intent getInstallAppIntent(File appFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //区别于 FLAG_GRANT_READ_URI_PERMISSION 跟 FLAG_GRANT_WRITE_URI_PERMISSION， URI权限会持久存在即使重启，直到明确的用 revokeUriPermission(Uri, int) 撤销。 这个flag只提供可能持久授权。但是接收的应用必须调用ContentResolver的takePersistableUriPermission(Uri, int)方法实现
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }
            Uri fileUri = FileUtils.getUriByFile(appFile);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            return intent;
        } catch (Exception e) {
            _XUpdate.onUpdateError(INSTALL_FAILED, "获取安装的意图失败！");
        }
        return null;
    }

    /**
     * 判断设备是否 root
     *
     * @return the boolean{@code true}: 是<br>{@code false}: 否
     */
    private static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }
}
