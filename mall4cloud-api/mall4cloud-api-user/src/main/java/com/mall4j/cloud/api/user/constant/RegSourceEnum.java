package com.mall4j.cloud.api.user.constant;

/**
 * @Date 2023年4月13日, 0018 14:13
 * @Created axin
 */
public enum RegSourceEnum {

    XXY(0 , "小程序"),
    ETO(1 , "ETO"),
    SPH(2 , "视频号"),
    WM(3 , "微盟"),
    ETO_H5(4 , "ETO-H5");

    private Integer code;

    private String desc;

    RegSourceEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getDesc( Integer code){
        RegSourceEnum[] enums = values();
        for( RegSourceEnum e : enums){
            if(e.getCode().equals( code)){
                return e.getDesc();
            }
        }
        return null;
    }
}
