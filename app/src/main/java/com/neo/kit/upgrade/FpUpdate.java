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

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

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
import com.neo.kit.upgrade.proxy.impl.DefaultUpdateChecker;
import com.neo.kit.upgrade.proxy.impl.DefaultUpdateDownloader;
import com.neo.kit.upgrade.proxy.impl.DefaultUpdateParser;
import com.neo.kit.upgrade.proxy.impl.DefaultUpdatePrompter;

import java.util.Map;
import java.util.TreeMap;

/**
 * 版本更新的入口
 *
 * @author xuexiang
 * @since 2018/6/29 下午7:47
 */
public class FpUpdate {
    private static final String TAG = FpUpdate.class.getSimpleName();

    private Application mContext;
    private static FpUpdate sInstance;

    //========全局属性==========//
    /**
     * 请求参数【比如apk-key或者versionCode等】
     */
    Map<String, Object> mParams;
    /**
     * 是否使用的是Get请求
     */
    boolean mIsGet;
    /**
     * 是否只在wifi下进行版本更新检查
     */
    boolean mIsWifiOnly;
    
    /**
     * 下载的apk文件缓存目录
     */
    String mApkCacheDir;
    //========全局更新实现接口==========//
    /**
     * 版本更新网络请求服务API
     */
    IUpdateHttpService mIUpdateHttpService;
    /**
     * 版本更新检查器【有默认】
     */
    IUpdateChecker mIUpdateChecker;
    /**
     * 版本更新解析器【有默认】
     */
    IUpdateParser mIUpdateParser;
    /**
     * 版本更新提示器【有默认】
     */
    IUpdatePrompter mIUpdatePrompter;
    /**
     * 版本更新下载器【有默认】
     */
    IUpdateDownloader mIUpdateDownloader;
    /**
     * APK安装监听【有默认】
     */
    OnInstallListener mOnInstallListener;
    /**
     * 更新出错监听【有默认】
     */
    OnUpdateFailureListener mOnUpdateFailureListener;

    //===========================初始化===================================//

    private FpUpdate() {
        mIsGet = false;
        mIsWifiOnly = true;

        mIUpdateChecker = new DefaultUpdateChecker();
        mIUpdateParser = new DefaultUpdateParser();
        mIUpdateDownloader = new DefaultUpdateDownloader();
        mIUpdatePrompter = new DefaultUpdatePrompter();
        mOnInstallListener = new DefaultInstallListener();
        mOnUpdateFailureListener = new DefaultUpdateFailureListener();
    }

    /**
     * 获取版本更新的入口
     *
     * @return 版本更新的入口
     */
    public static FpUpdate get() {
        if (sInstance == null) {
            synchronized (FpUpdate.class) {
                if (sInstance == null) {
                    sInstance = new FpUpdate();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param application
     */
    public void init(Application application) {
        mContext = application;
        UpdateError.init(mContext);
    }

    private Application getApplication() {
        testInitialize();
        return mContext;
    }

    private void testInitialize() {
        if (mContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 XUpdate.get().init() 初始化！");
        }
    }

    public static Context getContext() {
        return get().getApplication();
    }

    //===========================对外版本更新api===================================//

    /**
     * 获取版本更新构建者
     *
     * @param context
     * @return
     */
    public static UpdateManager.Builder newBuild(@NonNull Context context) {
        return new UpdateManager.Builder(context);
    }

    /**
     * 获取版本更新构建者
     *
     * @param context
     * @param updateUrl 版本更新检查的地址
     * @return
     */
    public static UpdateManager.Builder newBuild(@NonNull Context context, String updateUrl) {
        return new UpdateManager.Builder(context)
                .updateUrl(updateUrl);
    }

    //===========================属性设置===================================//

    /**
     * 设置全局的apk更新请求参数
     *
     * @param key
     * @param value
     * @return
     */
    public FpUpdate param(@NonNull String key, @NonNull Object value) {
        if (mParams == null) {
            mParams = new TreeMap<>();
        }
        Log.d(TAG, "设置全局参数, key:" + key + ", value:" + value.toString());
        mParams.put(key, value);
        return this;
    }

    /**
     * 设置全局的apk更新请求参数
     *
     * @param params
     * @return
     */
    public FpUpdate params(@NonNull Map<String, Object> params) {
        logForParams(params);
        mParams = params;
        return this;
    }

    private void logForParams(@NonNull Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("设置全局参数:{\n");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append("key = ")
                    .append(entry.getKey())
                    .append(", value = ")
                    .append(entry.getValue().toString())
                    .append("\n");
        }
        sb.append("}");
        Log.d(TAG, sb.toString());
    }


    /**
     * 设置全局版本更新网络请求服务API
     *
     * @param updateHttpService
     * @return
     */
    public FpUpdate setIUpdateHttpService(@NonNull IUpdateHttpService updateHttpService) {
        Log.d(TAG, "设置全局更新网络请求服务:" + updateHttpService.getClass().getCanonicalName());
        mIUpdateHttpService = updateHttpService;
        return this;
    }

    /**
     * 设置全局版本更新检查
     *
     * @param updateChecker 版本更新检查器
     * @return
     */
    public FpUpdate setIUpdateChecker(@NonNull IUpdateChecker updateChecker) {
        mIUpdateChecker = updateChecker;
        return this;
    }

    /**
     * 设置全局版本更新的解析器
     *
     * @param updateParser 版本更新的解析器
     * @return
     */
    public FpUpdate setIUpdateParser(@NonNull IUpdateParser updateParser) {
        mIUpdateParser = updateParser;
        return this;
    }

    /**
     * 设置全局版本更新提示器
     *
     * @param updatePrompter 版本更新提示器
     * @return
     */
    public FpUpdate setIUpdatePrompter(IUpdatePrompter updatePrompter) {
        mIUpdatePrompter = updatePrompter;
        return this;
    }

    /**
     * 设置全局版本更新下载器
     *
     * @param updateDownLoader 版本更新下载器
     * @return
     */
    public FpUpdate setIUpdateDownLoader(@NonNull IUpdateDownloader updateDownLoader) {
        mIUpdateDownloader = updateDownLoader;
        return this;
    }

    /**
     * 是否使用的是Get请求
     *
     * @param isGet
     * @return
     */
    public FpUpdate isGet(boolean isGet) {
        Log.d(TAG, "设置全局是否使用的是Get请求:" + isGet);
        mIsGet = isGet;
        return this;
    }

    /**
     * 设置是否只在wifi下进行版本更新检查
     *
     * @param isWifiOnly
     * @return
     */
    public FpUpdate isWifiOnly(boolean isWifiOnly) {
        Log.d(TAG, "设置全局是否只在wifi下进行版本更新检查:" + isWifiOnly);
        mIsWifiOnly = isWifiOnly;
        return this;
    }

    /**
     * 设置apk的缓存路径
     *
     * @param apkCacheDir
     * @return
     */
    public FpUpdate setApkCacheDir(String apkCacheDir) {
        Log.d(TAG, "设置全局apk的缓存路径:" + apkCacheDir);
        mApkCacheDir = apkCacheDir;
        return this;
    }


    //===========================apk安装监听===================================//
    /**
     * 设置安装监听
     *
     * @param onInstallListener
     * @return
     */
    public FpUpdate setOnInstallListener(OnInstallListener onInstallListener) {
        mOnInstallListener = onInstallListener;
        return this;
    }

    //===========================更新出错===================================//

    /**
     * 设置更新出错的监听
     *
     * @param onUpdateFailureListener
     * @return
     */
    public FpUpdate setOnUpdateFailureListener(@NonNull OnUpdateFailureListener onUpdateFailureListener) {
        mOnUpdateFailureListener = onUpdateFailureListener;
        return this;
    }
}
