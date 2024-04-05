package com.mall4j.cloud.api.biz.constant.cp;

import java.util.Arrays;

public enum GroupCustInfoJoinScene {
    //1 - 由群成员邀请入群（直接邀请入群）2 - 由群成员邀请入群（通过邀请链接入群）3 - 通过扫描群二维码入群
    DIRECT(1, "直接邀请入群"),
    INVITE(2, "通过邀请链接入群"),
    QRCODE(3, "通过扫描群二维码入群");

    private int code;
    private String txt;

    GroupCustInfoJoinScene(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static GroupCustInfoJoinScene get(Integer code) {
        return Arrays.stream(GroupCustInfoJoinScene.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
