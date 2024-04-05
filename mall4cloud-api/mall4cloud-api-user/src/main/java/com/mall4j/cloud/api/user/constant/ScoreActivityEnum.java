package com.mall4j.cloud.api.user.constant;

/**
 * @Date 2022年7月18日, 0018 14:13
 * @Created by eury
 */
public enum ScoreActivityEnum {

    ACTIVITY_TIME_SCORE(1 , "积分-限时折扣");

    private Integer code;

    private String name;

    ScoreActivityEnum(Integer code, String name) {
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
        ScoreActivityEnum[] enums = values();
        for( ScoreActivityEnum e : enums){
            if(e.getCode().equals( code)){
                return e.getName();
            }
        }
        return null;
    }
}
