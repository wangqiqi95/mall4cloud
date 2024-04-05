package com.mall4j.cloud.api.biz.constant.cp;

import java.util.Arrays;

public enum GroupCustInfoType {
    //1 - 企业成员,2 - 外部联系人
    ALIVE(1, "企业成员"),
    DISABLED(2, "外部联系人");

    private int code;
    private String txt;

    GroupCustInfoType(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static GroupCustInfoType get(Integer code) {
        return Arrays.stream(GroupCustInfoType.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
