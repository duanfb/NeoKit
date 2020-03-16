package com.neo.kit.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;

import com.neo.kit.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author neo.duan
 * @date 2020/3/16
 * @description 格式化时间工具类
 */
public class TimeUtils {

    /**
     * 发布时间（detail=true）：今天（包含刚刚、X分钟前、X小时前）、昨天、前天、x/x
     * 播放时间（detail=false）：今天、昨天、前天、较早
     *
     * @param when   时间戳
     * @param detail 是否包含详细信息
     * @return 返回时间字符串
     */
    public static String getPrettyTime(Context context, long when, boolean detail) {
        Resources resources = context.getResources();
        Calendar nowTime = Calendar.getInstance();
        long deltaMillis = nowTime.getTimeInMillis() - when;

        // 小于一分钟显示"刚刚"
        if (deltaMillis < TimeUnit.MINUTES.toMillis(1) && detail) {
            return resources.getString(R.string.date_just_now);
        }

        // 小于一小时显示"X分钟前"
        if (deltaMillis < TimeUnit.HOURS.toMillis(1) && detail) {
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(deltaMillis);
            return resources.getString(R.string.date_minute_ago, minutes);
        }

        // 计算今天的时间戳
        long todayMillis = TimeUnit.HOURS.toMillis(nowTime.get(Calendar.HOUR_OF_DAY)) +
                TimeUnit.MINUTES.toMillis(nowTime.get(Calendar.MINUTE)) +
                TimeUnit.SECONDS.toMillis(nowTime.get(Calendar.SECOND)) +
                nowTime.get(Calendar.MILLISECOND);

        if (deltaMillis < todayMillis) {
            if (detail) {
                // 小于一天显示"X小时前"
                long hours = TimeUnit.MILLISECONDS.toHours(deltaMillis);
                return resources.getString(R.string.date_hour_ago, hours);
            } else {
                // 小于一天显示"今天"
                return resources.getString(R.string.date_today);
            }
        }

        int days = (int) TimeUnit.MILLISECONDS.toDays(deltaMillis - todayMillis);
        if (days == 0) {
            // "昨天"
            return resources.getString(R.string.date_yesterday);
        } else if (days == 1) {
            // "前天"
            return resources.getString(R.string.date_the_day_before_yesterday);
        } else {
            if (detail) {
                // 直接显示几号"x/x"
                return DateFormat.format(resources.getString(R.string.date_format), when).toString();
            } else {
                // 直接显示"较早"
                return resources.getString(R.string.date_earlier);
            }
        }
    }

}
