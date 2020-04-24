package com.neo.baselib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.UUID;

/**
 * Author: neo.duan
 * Date: 2017/02/18
 * Desc: 有关设备工具类
 */
public class DeviceUtils {

    /**
     * 获取设备MAC Permission: android.permission.ACCESS_WIFI_STATE
     */
    public static String getMAC(Context context) {
        WifiManager wManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wManager.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取设备ID
     */
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取当前的imei, 可能为空 Permission: android.permission.READ_PHONE_STATE
     */
    public static String getImei(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前的imsi, 可能为空 Permission: android.permission.READ_PHONE_STATE
     */
    public static String getImsi(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = telephonyManager.getSubscriberId();
            if (imsi == null) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取设备厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 是否是电信卡 Permission: android.permission.READ_PHONE_STATE
     */
    public static boolean isCTC(Context context) {
        String imsi = getImsi(context);
        return isCTC(imsi);
    }

    /**
     * 是否是电信卡
     * Permission: android.permission.READ_PHONE_STATE
     */
    public static boolean isCTC(String imsi) {
        //中国电信网络编号：46003、46005、46011（国家码+网号）
        return !TextUtils.isEmpty(imsi)
                && (imsi.startsWith("46003")
                || (imsi.startsWith("46005"))
                || imsi.startsWith("46011"));
    }

    /**
     * 是否是移动卡 Permission: android.permission.READ_PHONE_STATE
     *
     * @param context
     * @return
     */
    public static boolean isCMCC(Context context) {
        String imsi = getImsi(context);
        return isCMCC(imsi);
    }

    /**
     * 是否是移动卡
     */
    public static boolean isCMCC(String imsi) {
        //中国移动网络编号：46000、46002、46007 （国家码+网号）
        return !TextUtils.isEmpty(imsi)
                && (imsi.startsWith("46000")
                || (imsi.startsWith("46002"))
                || imsi.startsWith("46007"));
    }

    /**
     * 是否是联通卡 Permission: android.permission.READ_PHONE_STATE
     */
    public static boolean isCUC(Context context) {
        String imsi = getImsi(context);
        return isCUC(imsi);
    }

    /**
     * 是否是联通卡
     */
    public static boolean isCUC(String imsi) {
        //中国联通网络编号：46001、46006（国家码+网号）
        return !TextUtils.isEmpty(imsi)
                && (imsi.startsWith("46001")
                || imsi.startsWith("46006"));
    }

    /**
     * 判断是否是平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取状态栏的高度，系统默认高度为25dp
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        java.lang.reflect.Field field;
        int x;
        int statusBarHeight;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ScreenUtils.dpToPxInt(context, 25);
    }

    public static int getAndroidSDKVersionInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
     */
    public static boolean isEmulator(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
            return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取手机的唯一识别号
     */
    public static String getUUID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取屏幕宽
     */
    public static int getScreenWidth(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            return display.getWidth();
        } else {
            return getScreenPoint(context).x;
        }
    }

    /**
     * 获取屏幕高
     */
    public static int getScreenHeight(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            return display.getHeight();
        } else {
            return getScreenPoint(context).y;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenPoint(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        Display display = wm.getDefaultDisplay();
        display.getSize(point);
        return point;
    }

    /**
     * 获取手机基站信息
     */
    public static int[] getStationInfo(Context context) {
        if (isCMCC(context) || isCUC(context)) {
            //移动和联通卡
            return getCmccStationInfo(context);
        } else if (isCTC(context)) {
            //电信卡
            return getCtcStationInfo(context);
        } else {
            return new int[2];
        }
    }


    /**
     * 联通移动获取基站信息
     */
    public static int[] getCmccStationInfo(Context context) {
        int[] stationInfo = new int[2];
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            CellLocation cel = tel.getCellLocation();
            int nPhoneType = tel.getPhoneType();
            //移动联通 GsmCellLocation
            if (nPhoneType == TelephonyManager.PHONE_TYPE_GSM && cel instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
                int nGSMCID = gsmCellLocation.getCid();
                if (nGSMCID > 0) {
                    if (nGSMCID != 65535) {
                        //cell
                        stationInfo[0] = nGSMCID;
                        //lac
                        stationInfo[1] = gsmCellLocation.getLac();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stationInfo;
    }

    /**
     * 获取电信基站信息
     */
    public static int[] getCtcStationInfo(Context context) {
        int[] stationInfo = new int[2];
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            CellLocation cel = tel.getCellLocation();
            int nPhoneType = tel.getPhoneType();
            //电信   CdmaCellLocation
            if (nPhoneType == TelephonyManager.PHONE_TYPE_CDMA && cel instanceof CdmaCellLocation) {
                Log.e("电信", "-----------------》电信");
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
                //系统识别码，地级市只有一个
                int sid = cdmaCellLocation.getSystemId();
                //系统识别码，地级市可能有一到三个
                int nid = cdmaCellLocation.getNetworkId();
                //小区基站id
                int bid = cdmaCellLocation.getBaseStationId();
                stationInfo[0] = bid;
                stationInfo[1] = nid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stationInfo;
    }
}
