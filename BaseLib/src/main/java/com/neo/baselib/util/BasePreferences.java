
package com.neo.baselib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;

/**
 * @author : neo.duan
 * @date : 	 2016/7/25
 * @desc : preferences操作基类
 */
public class BasePreferences {

    /**
     * 文件名
     */
    protected static final String PREFERENCES_NAME = "SharedPreferences";

    /**
     * 保存数据
     *
     * @param key
     * @return boolean true:成功 false:失败
     */
    public static void putData(Context context, String key, Object value) {
        saveData(context, PREFERENCES_NAME, key, value);
    }

    /**
     * 从文件中获取数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(Context context, String key, Object defValue) {
        if (context == null) {
            return defValue;
        }
        return getData(context, PREFERENCES_NAME, key, defValue);
    }

    /**
     * 移除指定KEY
     *
     * @param key
     */
    public static void removeKey(Context context, String key) {
        remove(context, PREFERENCES_NAME, key);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        clear(context, PREFERENCES_NAME);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        putData(context, key, value);
    }

    public static void putInt(Context context, String key, int value) {
        putData(context, key, value);
    }

    public static void putLong(Context context, String key, long value) {
        putData(context, key, value);
    }

    public static void putString(Context context, String key, String value) {
        putData(context, key, value);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return (Boolean) getData(context, key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        return (Integer) getData(context, key, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        return (Long) getData(context, key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        return (String) getData(context, key, defValue);
    }

    /**
     * 保存数据
     *
     * @param context
     * @param fileName
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public static void saveData(Context context, String fileName, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Set<?>) {
            editor.putStringSet(key, (Set<String>) value);
        } else {
            if (value == null) {
                editor.putString(key, "");
            } else {
                editor.putString(key, String.valueOf(value));
            }
        }
        editor.apply();
    }

    /**
     * 取值
     *
     * @param context
     * @param fileName
     * @param key
     * @param defValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getData(Context context, String fileName, String key, Object defValue) {
        if (context == null || fileName == null) {
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Set<?>) {
            return sp.getStringSet(key, (Set<String>) defValue);
        } else {
            if (defValue == null) {
                return sp.getString(key, "");
            }
            return sp.getString(key, String.valueOf(defValue));
        }
    }

    /**
     * 清空
     *
     * @param fileName
     */
    public static void clear(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 移除指定的KEY
     *
     * @param fileName
     * @param key
     */
    public static void remove(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public int getSize(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        Map<String, ?> all = sp.getAll();
        return all.size();
    }

}
