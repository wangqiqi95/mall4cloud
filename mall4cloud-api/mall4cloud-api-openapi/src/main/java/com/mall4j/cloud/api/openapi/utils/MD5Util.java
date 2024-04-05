package com.mall4j.cloud.api.openapi.utils;

import java.security.MessageDigest;

public class MD5Util {

    // MD5加密函数
    public static String encode(String sourceString) {
        String resultString = null;
        try {
            resultString = new String(sourceString);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byte2hexString(md.digest(resultString.getBytes("UTF-8")));
        } catch (Exception ex) {
        }
        return resultString;
    }

    public static final String byte2hexString(byte[] bytes) {
        StringBuffer bf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                bf.append("0");
            }
            bf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return bf.toString();
    }
}
