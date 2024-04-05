package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum StatusType {
    /** **/
    YX(1, "有效"),
    WX(0, "无效");

    private int code;
    private String txt;

    StatusType(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static StatusType get(Integer code) {
        return Arrays.stream(StatusType.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
