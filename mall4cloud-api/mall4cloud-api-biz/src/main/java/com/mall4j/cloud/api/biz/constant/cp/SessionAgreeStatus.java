package com.mall4j.cloud.api.biz.constant.cp;

import java.util.Arrays;

public enum SessionAgreeStatus {

    //同意:"Agree"，不同意:"Disagree"
    ALIVE("Agree", "同意"),
    DISABLED("Disagree", "不同意");

    private String code;
    private String txt;

    SessionAgreeStatus(String code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static SessionAgreeStatus get(String code) {
        return Arrays.stream(SessionAgreeStatus.values()).filter(e ->code.equals(e.getCode())).findFirst().orElse(null);
    }

    public String getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
