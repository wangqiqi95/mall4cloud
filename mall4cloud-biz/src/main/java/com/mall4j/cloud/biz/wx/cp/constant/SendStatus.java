package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum SendStatus {
    /** **/
    WAIT(0, "待发送"),
    SUCCESS(1, "发送成功"),
    FAIL(2, "发送失败");

    private int code;
    private String txt;

    SendStatus(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static SendStatus get(Integer code) {
        return Arrays.stream(SendStatus.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
