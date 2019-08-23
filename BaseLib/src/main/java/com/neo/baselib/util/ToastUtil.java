package com.neo.baselib.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * Author: neo.duan
 * Date: 2017/02/18
 * Desc: 吐司工具类
 */
public class ToastUtil {

    public static Toast toast;

    private ToastUtil() {
        throw new AssertionError();
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }


    private static void show(Context context, CharSequence text, int duration) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(text + "")) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(context, text, duration);
        } else {
            toast.setDuration(duration);
            toast.setText(text);
        }
        toast.show();
    }
}