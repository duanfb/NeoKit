package com.neo.baselib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.neo.baselib.R;


/**
 * Author: neo.duan
 * Date: 2017/02/18
 * Desc: 有关app信息工具类
 */
public class AppUtils {

    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * 获取app版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pkgInfo != null) {
            return pkgInfo.versionName;
        }
        return "";
    }

    /**
     * 获取app version code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pkgInfo != null) {
            return pkgInfo.versionCode;
        }
        return -1;
    }

    /**
     * 获取app名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        return context.getResources().getString(R.string.app_name);
    }

    /**
     * 获取app版本号
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        try {
            return context.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo =
                        packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }
}
