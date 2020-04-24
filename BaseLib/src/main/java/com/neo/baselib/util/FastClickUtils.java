package com.neo.baselib.util;


import android.util.Log;

/**
 * @author neo.duan
 * @date 2018/7/27 13:31
 * @desc 防止重复点击工具类
 */
public class FastClickUtils {

    private static long lastClickTime = 0;
    private static int lastButtonId = -1;

    /**
     * 时间间隔
     */
    private static long DIFF = 1000;

    /**
     * 判断两次点击的间隔，如果小于1s，则认为是多次无效点击（任意两个view，固定时长1s）
     */
    public static boolean isFastClick() {
        return isFastClick(-1, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击（任意两个view，自定义间隔时长）
     */
    public static boolean isFastClick(long diff) {
        return isFastClick(-1, diff);
    }

    /**
     * 判断两次点击的间隔，如果小于1s，则认为是多次无效点击（同一个view，固定时长1s）
     */
    public static boolean isFastClick(int buttonId) {
        return isFastClick(buttonId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击（同一按钮，自定义间隔时长）
     */
    public static boolean isFastClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            Log.d("isFastClick", "短时间内view被多次点击");
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

}
