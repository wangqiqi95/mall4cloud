package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum UserStaffCpRelationStatusEunm {
    /** **/

    BIND(1, "正常"),
    ASSIGNING(2, "继承中"),
    DEL(3, "删除");
    private int code;
    private String txt;

    UserStaffCpRelationStatusEunm(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static UserStaffCpRelationStatusEunm get(Integer code) {
        return Arrays.stream(UserStaffCpRelationStatusEunm.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
