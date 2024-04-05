package com.mall4j.cloud.biz.wx.cp.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum MaterialMsgType {
    /** **/
    TXT("text", "文本"),
    IMAGE("image", "图片"),
    VIDEO("video", "视频"),
    MINIPROGRAM("miniprogram", "小程序"),
    H5("h5", "H5");

    private String code;
    private String txt;

    MaterialMsgType(String code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    public  static MaterialMsgType get(String code) {
        if(StringUtils.isEmpty(code)) {
            return null;
        }
        return Arrays.stream(MaterialMsgType.values()).filter(e ->code.equals(e.getCode())).findFirst().orElse(null);
    }

    public String getCode() {
        return code;
    }

    public String getTxt() {
        return txt;
    }

}
