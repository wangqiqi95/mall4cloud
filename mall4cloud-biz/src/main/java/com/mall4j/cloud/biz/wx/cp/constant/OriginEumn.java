package com.mall4j.cloud.biz.wx.cp.constant;

import java.util.Arrays;

public enum OriginEumn {
    /** **/
    MAT(0, "素材附件"),
    GROUP_TASK(1, "群发附件");
    private int code;
    private String txt;

    OriginEumn(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static OriginEumn get(Integer code) {
        return Arrays.stream(OriginEumn.values()).filter(e ->code==e.getCode()).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
