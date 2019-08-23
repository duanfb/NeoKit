package com.neo.baselib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author neo.duan
 * @date 2018/07/09
 * @desc 弹出对话框工具类：show方法都带左边取消按钮，showSingleButton没有左边取消按钮
 */
public class MaterialDialogUtils {

    /**
     * 弹出单选按钮对话框
     */
    public static void showSingleButton(Context context, String content,
                                        DialogInterface.OnClickListener positiveCallBack) {
        showSingleButton(context, content, "YES", positiveCallBack);
    }

    /**
     * 弹出单选按钮对话框
     */
    public static void showSingleButton(Context context, String content, String positiveText,
                                        DialogInterface.OnClickListener positiveCallBack) {
        show(context, content, "", null, positiveText, positiveCallBack);
    }

    /**
     * 弹出单选按钮对话框
     */
    public static void show(Context context, String content, DialogInterface.OnClickListener positiveCallBack) {
        show(context, content, "YES", positiveCallBack);
    }

    /**
     * 弹出带取消按钮的对话框
     */
    public static void show(Context context, String content, String positiveText,
                            DialogInterface.OnClickListener positiveCallBack) {
        show(context, content, "NO", null, positiveText, positiveCallBack);
    }

    public static void show(Context context, String content,
                            String negativeText,
                            DialogInterface.OnClickListener negativeCallBack,
                            String positiveText,
                            DialogInterface.OnClickListener positiveCallBack) {
        show(context, content,
                negativeText, negativeCallBack,
                positiveText, positiveCallBack,
                false);
    }

    public static void show(Context context, String content,
                            String negativeText,
                            DialogInterface.OnClickListener negativeCallBack,
                            String positiveText,
                            DialogInterface.OnClickListener positiveCallBack,
                            boolean cancelable) {

        createMaterialDialog(context, content, negativeText,
                negativeCallBack, positiveText, positiveCallBack, cancelable)
                .show();
    }

    public static AlertDialog createMaterialDialog(Context context, String content,
                                                   String negativeText,
                                                   DialogInterface.OnClickListener negativeCallBack,
                                                   String positiveText,
                                                   DialogInterface.OnClickListener positiveCallBack,
                                                   boolean cancelable) {
        return new AlertDialog.Builder(context)
                //弹框内容配置
                .setMessage(content)
                //左按钮配置
                .setNegativeButton(negativeText, negativeCallBack)
                //右按钮配置
                .setPositiveButton(positiveText, positiveCallBack)
                .setCancelable(cancelable).create();
    }
}
