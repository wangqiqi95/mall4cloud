package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum FlagEunm {
    /** **/
    DELETE(1, "已删除"),
    USE(0, "未删除");

    private int code;
    private String txt;

    FlagEunm(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static FlagEunm get(Integer code) {
        return Arrays.stream(FlagEunm.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
