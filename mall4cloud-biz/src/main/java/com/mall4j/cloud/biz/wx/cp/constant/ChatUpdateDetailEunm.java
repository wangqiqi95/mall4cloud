package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum ChatUpdateDetailEunm {
    /** **/
    ADD("add_member", "成员入群"),
    DEL("del_member","成员退群"),
    CHANGE_OWNER("change_owner","群主变更"),
    CHANGE_NAME("change_name","群名变更"),
    CHANGE_NOTICE("change_notice","群公告变更");
    private String code;
    private String txt;

    ChatUpdateDetailEunm(String code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static ChatUpdateDetailEunm get(Integer code) {
        return Arrays.stream(ChatUpdateDetailEunm.values()).filter(e ->code.equals(e.getCode())).findFirst().orElse(null);
    }

    public String getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
