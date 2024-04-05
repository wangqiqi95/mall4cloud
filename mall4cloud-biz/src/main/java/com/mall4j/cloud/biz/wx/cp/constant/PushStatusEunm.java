package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum PushStatusEunm {
    /** **/
    CREATE(0, "待发送"),
    SUCCESS(1, "发送成功"),
    FAIL(2, "发送失败"),
    COMPLETE(3, "接替完成");
    private int code;
    private String txt;

    PushStatusEunm(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static PushStatusEunm get(Integer code) {
        return Arrays.stream(PushStatusEunm.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }
}
