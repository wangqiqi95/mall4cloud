package com.mall4j.cloud.api.biz.constant.cp;

import java.util.Arrays;

public enum GroupCustInfoAdmin {
    //是否管理员：0否/1是
    NO(0, "否"),
    YES(1, "是");

    private int code;
    private String txt;

    GroupCustInfoAdmin(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static GroupCustInfoAdmin get(Integer code) {
        return Arrays.stream(GroupCustInfoAdmin.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
