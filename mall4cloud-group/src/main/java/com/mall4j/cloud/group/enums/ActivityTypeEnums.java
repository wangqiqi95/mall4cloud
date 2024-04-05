package com.mall4j.cloud.group.enums;

public enum ActivityTypeEnums {
    REGISTER_ACTIVITY(1 , "注册有礼"),
    PERFECT_DATA_ACTIVITY(2 , "完善资料"),
    FOLLOW_WECHAT_ACTIVITY(3 , "关注公众号"),
    ;

    private Integer code;

    private String name;

    ActivityTypeEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getName( Integer code){
        ActivityTypeEnums[] enums = values();
        for( ActivityTypeEnums e : enums){
            if(e.getCode().equals( code)){
                return e.getName();
            }
        }
        return null;
    }
}
