package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum AssignWayEunm {
    /** **/
    CUST(0, "按客户"),
    GROUP(1, "按群分配");

    private int code;
    private String txt;

    AssignWayEunm(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static AssignWayEunm get(Integer code) {
        return Arrays.stream(AssignWayEunm.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
