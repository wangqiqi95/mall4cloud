package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum CustAssignType {
    /** **/
    SALE_MAN(0, "按客户导购"),
    STAFF(1, "指定员工");

    private int code;
    private String txt;

    CustAssignType(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static CustAssignType get(Integer code) {
        return Arrays.stream(CustAssignType.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
