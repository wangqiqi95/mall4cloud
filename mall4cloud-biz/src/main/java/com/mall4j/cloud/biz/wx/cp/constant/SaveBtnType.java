package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum SaveBtnType {
    /** **/
    SAVE(0, "保存"),
    SAVE_SEND(1, "保存并发送");

    private int code;
    private String txt;

    SaveBtnType(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static SaveBtnType get(Integer code) {
        return Arrays.stream(SaveBtnType.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
