package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum AssignStatusEunm {
    /** **/

    CREATE(0, "待分配"),
    SUCCESS(1, "分配成功"),
    FAIL(2, "分配失败"),
    ASSIGNING(3, "分配中");
    private int code;
    private String txt;

    AssignStatusEunm(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static AssignStatusEunm get(Integer code) {
        return Arrays.stream(AssignStatusEunm.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
