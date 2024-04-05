package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum CodeType {
    /** **/
    BATCH(0, "批量单人"),
    SINGLE(1, "单人"),
    DOUBLE(2, "多人");

    private int code;
    private String txt;

    CodeType(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static CodeType get(Integer code) {
        return Arrays.stream(CodeType.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
