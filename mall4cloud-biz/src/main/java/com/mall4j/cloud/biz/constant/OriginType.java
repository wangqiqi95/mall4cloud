package com.mall4j.cloud.biz.constant;

import java.util.Arrays;

public enum OriginType {
    /** **/
     WEL_CONFIG(0, "通用欢迎语配置"),
     STAFF_CODE(1, "渠道活码配置"),
    MOMENTS_CONFIG(2, "朋友圈配置");

    private int code;
    private String txt;

    OriginType(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static OriginType get(Integer code) {
        return Arrays.stream(OriginType.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
