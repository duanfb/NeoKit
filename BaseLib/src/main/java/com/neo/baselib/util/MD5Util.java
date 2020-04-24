package com.neo.baselib.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MD5Util {

    /**
     * MD5加密，32位
     */
    public static String md5(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        md5.update(str.getBytes());
        byte[] md5Bytes = md5.digest();
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        String result = hexValue.toString();
        result = result.toLowerCase(Locale.getDefault());
        return result;
    }

    /**
     * MD5加密16位
     *
     * @param plainText
     * @return
     */
    public static String Md516(String plainText) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (byte b1 : b) {
                i = b1;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            // result = buf.toString(); //md5 32bit
            // result = buf.toString().substring(8, 24))); //md5 16bit
            result = buf.toString().substring(8, 24);
            // System.out.println("md5 16bit: " + buf.toString().substring(8,
            // 24));
            // System.out.println("md5 32bit: " + buf.toString() );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.toUpperCase(Locale.getDefault());
        }
        return "";
    }
}
