package com.mall4j.cloud.coupon.constant;

public enum ActivityStatusEnums {
    NOT_ENABLED(0 , "未启用"),
    ENABLED(1 , "启用"),
    IN_PROGRESS(2 , "进行中"),
    NOT_START(3 , "未开始"),
    END(4 , "已结束")
    ;

    private Integer code;

    private String name;

    ActivityStatusEnums(Integer code, String name) {
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
        ActivityStatusEnums[] enums = values();
        for( ActivityStatusEnums e : enums){
            if(e.getCode().equals( code)){
                return e.getName();
            }
        }
        return null;
    }
}
