package com.neo.kit.upgrade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;

/**
 * APP 更新工具类
 * AppUpdateUtils
 */
public class AppUpdateUtil {
    private static final String TAG = AppUpdateUtil.class.getSimpleName();
    private static String PACKAGE_NAME = AppUtils.getAppPackageName();
    private static final String APK_NAME = "app.apk";
    private static final String DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .getAbsolutePath() + "/%s/";
    private static final String URL = "https://www.me-app.net/api/1.0/exportApp?appID=%1s&platform=1&branchSign=%2s";

    private static final int DOWNLOAD_START = 10001;
    private static final int DOWNLOAD_PROGRESS = 10002;
    private static final int DOWNLOAD_SUCCESS = 10003;
    private static final int DOWNLOAD_FAIL = 10004;
    /**
     * 是否正在检测更新
     */
    private static boolean isCheckUpdating;
    private final static Application APPLICATION = Utils.getApp();
    private static String APK_DIR;
    private static String API_URL;
    private final int NOTIFY_ID = new Random().nextInt();

    private VersionResponse versionResponse;
    private ProgressDialog progressDialog;
    private float fileSize;
    private float downloadSize;

    public static void checkVersion(boolean isSilent) {
        checkVersion("", isSilent);
    }

    public static void checkVersion(String branch, boolean isSilent) {
        checkVersion(PACKAGE_NAME, branch, isSilent);
    }

    /**
     * 检查更新
     *
     * @param packageName 应用包名
     * @param branch      分支
     * @param isSilent    是否静默检查更新
     */
    public static void checkVersion(String packageName, String branch, boolean isSilent) {
        if (isCheckUpdating) {
            HuiToast.showToast(APPLICATION, APPLICATION.getString(R.string.fp_phone_check_updating));
            return;
        }
        new AsyncTask<Void, Void, VersionResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //请求前初始化参数
                isCheckUpdating = true;
                PACKAGE_NAME = packageName;
                APK_DIR = String.format(DIR, packageName);
                API_URL = String.format(URL, packageName, branch);
            }

            @Override
            protected VersionResponse doInBackground(Void... voids) {
                try {
                    URL url = new URL(API_URL);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();

                    return pullParseXml(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return VersionResponse.empty();
            }

            @Override
            protected void onPostExecute(VersionResponse response) {
                super.onPostExecute(response);
                isCheckUpdating = false;
                Activity activity = getCurrentActivity();
                if (activity != null && !activity.isFinishing()) {
                    String versionName = StringUtil.null2Length0(response.versionName);
                    String minVersionCode = StringUtil.null2Length0(response.minVersionCode);

                    if (versionName.compareTo(AppUtils.getAppVersionName()) > 0) {
                        try {
                            response.forceUpdate = AppUtils.getAppVersionCode() >= Long.parseLong(minVersionCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        AppUpdateUtil updateUtil = new AppUpdateUtil();
                        updateUtil.showVersionDialog(activity, response);
                    } else if (!isSilent) {
                        HuiToast.showToast(activity.getApplicationContext(), activity.getString(R.string.fp_phone_already_new_version));
                    }
                }
            }
        }.execute();
    }

    private static VersionResponse pullParseXml(InputStream stream) {
        try {
            //获取一个解析实例
            XmlPullParser parse = XmlPullParserFactory.newInstance().newPullParser();
            //设置输入流的编码格式
            parse.setInput(stream, "UTF-8");
            // 当前事件的类型
            int eventType = parse.getEventType();

            String name;
            VersionResponse versionResponse = VersionResponse.empty();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    name = parse.getName();
                    if ("VersionName".equals(name)) {
                        versionResponse.versionName = parse.nextText();
                    } else if ("UpdateInfo".equals(name)) {
                        versionResponse.updateInfo = parse.nextText();
                    } else if ("minVersionCode".equals(name)) {
                        versionResponse.minVersionCode = parse.nextText();
                    } else if ("Address".equals(name)) {
                        versionResponse.apkUrl = parse.nextText();
                    }
                }
                eventType = parse.next();
            }
            return versionResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return VersionResponse.empty();
    }

    @SuppressLint("CheckResult")
    private void showVersionDialog(Activity activity, VersionResponse response) {
        this.versionResponse = response;
        new VersionDialog(activity)
                .setVersionName(response.versionName)
                .setMsg(response.updateInfo)
                .setForceUpdate(response.forceUpdate)
                .setOnClickListener(which -> {
                    if (1 == which) {
                        PermissionUtils.permission(PermissionConstants.STORAGE).callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> permissionsGranted) {
                                //允许权限
                                downloadApk(response.apkUrl);
                            }

                            @Override
                            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                                if (permissionsDeniedForever.size() == 2) {
                                    //不再询问，跳转到设置页
                                    DialogUtil.show(activity, activity.getString(R.string.fp_phone_storage_permissions), activity.getString(R.string.fp_phone_cancel), (dialog, which12) -> {
                                                //取消后重新显示版本更新Dialog,防止用户设置完权限后，升级框不在了
                                                showVersionDialog(getCurrentActivity(), response);
                                            },
                                            activity.getString(R.string.fp_phone_go_setting), (dialog, which1) -> {
                                                PermissionUtils.launchAppDetailsSettings();

                                                //去设置后重新显示版本更新Dialog,防止用户设置完权限后，升级框不在了
                                                mUiHandler.postDelayed(() -> showVersionDialog(getCurrentActivity(), response), 1000);
                                            });
                                }
                            }
                        }).request();
                    }
                }).show();
    }

    @SuppressLint("HandlerLeak")
    Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //下载前
                case DOWNLOAD_START:
                    isCheckUpdating = true;
                    String downloading = APPLICATION.getString(R.string.fp_phone_downloading);
                    notification(APPLICATION, NOTIFY_ID, downloading, downloading);

                    HuiToast.showToast(APPLICATION, APPLICATION.getString(R.string.fp_phone_downloading));
                    //下载中
                case DOWNLOAD_PROGRESS:
                    if (versionResponse.forceUpdate) {
                        showProgressDialog((Integer) msg.obj);
                    }
                    break;
                //下载成功
                case DOWNLOAD_SUCCESS:
                    isCheckUpdating = false;
                    cancelNotification(APPLICATION, NOTIFY_ID);
                    dismissProgressDialog();

                    installApk((File) msg.obj);
                    break;
                //下载失败
                case DOWNLOAD_FAIL:
                    isCheckUpdating = false;
                    cancelNotification(APPLICATION, NOTIFY_ID);
                    dismissProgressDialog();

                    HuiToast.showToast(APPLICATION, APPLICATION.getString(R.string.fp_phone_download_fail));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 下载新版本程序，需要子线程
     */
    private void downloadApk(final String apkPath) {
        //启动子线程下载任务
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                //发送开始下载
                mUiHandler.sendMessage(Message.obtain(mUiHandler, DOWNLOAD_START, 0));

                File file = getFileFromServer(apkPath);
                if (file == null || file.length() <= 0) {
                    mUiHandler.sendMessage(Message.obtain(mUiHandler, DOWNLOAD_FAIL, 0));
                } else {
                    mUiHandler.sendMessage(Message.obtain(mUiHandler, DOWNLOAD_SUCCESS, file));
                }
            } catch (Exception e) {
                //下载apk失败
                e.printStackTrace();
                mUiHandler.sendMessage(Message.obtain(mUiHandler, DOWNLOAD_FAIL, 0));
            }
        });
    }

    private void showProgressDialog(int progress) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getCurrentActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage(APPLICATION.getString(R.string.fp_phone_downloading));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setOnDismissListener(dialog -> {
                Activity activity = getCurrentActivity();
                if (activity != null && !activity.isFinishing()) {
                    showVersionDialog(getCurrentActivity(), versionResponse);
                }
            });
        }
        progressDialog.setProgressNumberFormat(String.format(Locale.CHINA, "%.1f/%.1fM", downloadSize, fileSize));
        progressDialog.setProgress(progress);

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    private File getFileFromServer(String uri) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        InputStream is = null;
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            HttpURLConnection conn;
            try {
                URL url = new URL(uri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(8000);
                //获取到文件的大小
                long contentLength = conn.getContentLength();
                if (contentLength < 0) {
                    contentLength = getContentLengthFromHeader(conn);
                }
                LogUtil.e(TAG, "contentLen=" + contentLength);
                //更新包总大小
                fileSize = (float) contentLength / 1024 / 1024;
                is = conn.getInputStream();

                FileUtils.createOrExistsDir(APK_DIR);
                File file = new File(APK_DIR, APK_NAME);
                FileUtils.createFileByDeleteOldFile(file);

                fos = new FileOutputStream(file);
                bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                //下载进度百分比
                int percentage;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    downloadSize = (float) total / 1024 / 1024;
                    percentage = (int) ((downloadSize / fileSize) * 100f);

                    //发送更新进度
                    mUiHandler.sendMessage(Message.obtain(mUiHandler, DOWNLOAD_PROGRESS, percentage));
                }

                return file;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * 从头文件得到下载内容的大小
     */
    private long getContentLengthFromHeader(HttpURLConnection urlConnection) {
        List<String> values = urlConnection.getHeaderFields().get("content-Length");
        if (values != null && !values.isEmpty()) {
            String sLength = (String) values.get(0);
            if (sLength != null) {
                return Long.parseLong(sLength, 10);
            }
        }
        return -1;
    }

    private void installApk(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 7.0 以上
            start7Install(file);
        } else {
            //安卓7.0以下，直接安装
            installApp(file);
        }
    }

    /**
     * 普通调起安装
     */
    private void installApp(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            APPLICATION.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            HuiToast.showToast(APPLICATION, APPLICATION.getString(R.string.fp_phone_app_install_fail));
        }
    }

    /**
     * 7.0安装应用
     */
    private void start7Install(File file) {
        try {
            Uri uri = FileProvider.getUriForFile(APPLICATION, PACKAGE_NAME + ".fileProvider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            // 必须现set,然后再add,因为set是覆盖，add是追加
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            APPLICATION.startActivity(install);
        } catch (Exception e) {
            e.printStackTrace();
            HuiToast.showToast(APPLICATION, APPLICATION.getString(R.string.fp_phone_app_install_fail));
        }
    }

    private static Activity getCurrentActivity() {
        return ActivityManager.getInstance().getTopActivity();
    }

    private static void notification(Context context, int notifyId, String channelId, String content) {
        Notification.Builder builder = new Notification.Builder(context);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 设置PendingIntent
        builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_NO_CREATE))
                // 设置下拉列表里的标题
                .setContentTitle(context.getString(R.string.app_name))
                // 设置状态栏内的小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                // 设置上下文内容
                .setContentText(content)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && null != notificationManager) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, "down_channel",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);

            // Android O版本之后需要设置该通知的channelId
            builder.setChannelId(channelId);
        }
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        if (notificationManager != null) {
            notificationManager.notify(notifyId, notification);
        }
    }

    private static void cancelNotification(Context context, int notifyId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifyId);
    }

    private static class VersionResponse implements Serializable {
        public String versionName;
        public String apkUrl;
        public String minVersionCode;
        public String updateInfo;
        public boolean forceUpdate;

        public static VersionResponse empty() {
            return new VersionResponse();
        }
    }
}
