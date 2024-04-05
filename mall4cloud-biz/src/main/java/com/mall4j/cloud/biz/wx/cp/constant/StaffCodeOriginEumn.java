package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum StaffCodeOriginEumn {
    /** **/
    BACK(0, "后台创建"),
    MINI(1, "小程序创建");
    private int code;
    private String txt;

    StaffCodeOriginEumn(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static StaffCodeOriginEumn get(Integer code) {
        return Arrays.stream(StaffCodeOriginEumn.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
