package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum AssignTypeEunm {
    /** **/
    CUST(0, "在职分配--客户"),
    DIS_CUST(1, "离职分配-客户"),
    DIS_GROUP(2, "离职分配-客群"),
    CUST_GROUP(3, "在职分配-客群");
    private int code;
    private String txt;

    AssignTypeEunm(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static AssignTypeEunm get(Integer code) {
        return Arrays.stream(AssignTypeEunm.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
