package com.neo.baselib.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * Date: 2017/02/18
 * Desc: 吐司工具类
 *
 * @author neo.duan
 */
public class ToastUtil {

    /**
     * 全局Toast对象
     */
    private static Toast mToast;
    private static Context mApplication;
    /**
     * 创建可以处理main线程的Handler对象
     */
    private static Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            //先取消正在显示的Toast
            if (mToast != null) {
                mToast.cancel();
            }
            String message = (String) msg.obj;
            mToast = Toast.makeText(mApplication, message, msg.arg2);
            mToast.show();
        }
    };

    public static void show(Context context, String message, int duration) {
        mApplication = context;
        //将Toast需要的参数发送到消息队列
        handler.sendMessage(handler.obtainMessage(0, 0, duration, message));
    }

    public static void show(Context context, String message) {
        if (!TextUtils.isEmpty(message)) {
            show(context, message, Toast.LENGTH_SHORT);
        }
    }
}