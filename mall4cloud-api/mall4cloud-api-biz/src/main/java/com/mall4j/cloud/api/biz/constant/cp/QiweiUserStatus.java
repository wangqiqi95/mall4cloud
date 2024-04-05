package com.mall4j.cloud.api.biz.constant.cp;

import java.util.Arrays;

public enum QiweiUserStatus {
    /**企微用户状态 1-已激活 2-已禁用 4-未激活 5-退出企业 **/
    ALIVE(1, "已激活"),
    DISABLED(2, "已禁用"),
    NOT_ALIVE(4, "未激活"),
    DEL(5, "退出企业"),
    REMOVE(-1, "离职交接完成");

    private int code;
    private String txt;

    QiweiUserStatus(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static QiweiUserStatus get(Integer code) {
        return Arrays.stream(QiweiUserStatus.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
