package com.mall4j.cloud.api.biz.constant.cp;

import java.util.Arrays;

public enum NotityTypeEunm {
    /** 好友流失提醒**/
    UNADD_QW(1, "好友流失提醒"),
    /**
     * 跟进提醒
     */
    UNREGISTER_MEMBER(2, "跟进提醒"),
    /**
     * 素材浏览提醒
     */
    INVITE_USER(3, "素材浏览"),
    /**
     * 敏感词命中提醒
     */
    SUB(4, "敏感词命中"),
    /**
     * 回复超时提醒
     */
    USER_REGISTER_SUCCESS(5, "回复超时提醒"),
    /**
     * 好友流失提醒
     */
    DELETE_USER(6, "好友流失提醒"),
    /**
     * 服务变更提醒
     */
    SERVICE_CHANGE(7, "服务变更提醒"),
    CP_PHONE_TASK(8, "手机号任务");

    private int code;
    private String txt;

    NotityTypeEunm(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static NotityTypeEunm get(Integer code) {
        return Arrays.stream(NotityTypeEunm.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
