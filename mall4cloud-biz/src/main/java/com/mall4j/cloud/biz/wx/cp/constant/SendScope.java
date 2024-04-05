package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum SendScope {
    /** **/
    ALL(0, "全体员工"),
    PART(1, "部分员工");

    private int code;
    private String txt;

    SendScope(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static SendScope get(Integer code) {
        return Arrays.stream(SendScope.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
