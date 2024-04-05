package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum TaskType {
    /** **/
    GROUP(0, "群活码"),
    TASK(1, "群发任务"),
    GROUP_TAG(2, "标签建群"),
    ;

    private int code;
    private String txt;

    TaskType(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static TaskType get(Integer code) {
        return Arrays.stream(TaskType.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
