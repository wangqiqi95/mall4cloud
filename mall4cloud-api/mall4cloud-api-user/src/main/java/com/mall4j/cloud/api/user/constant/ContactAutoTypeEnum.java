package com.mall4j.cloud.api.user.constant;

/**
 * 0自动通过/1手动通过
 */
public enum ContactAutoTypeEnum {

    ADD_EXTERNAL_CONTACT(0 , "自动通过"),//自动
    EDIT_EXTERNAL_CONTACT(1 , "手动通过");//人工

    private Integer code;

    private String desc;

    ContactAutoTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc( Integer code){
        ContactAutoTypeEnum[] enums = values();
        for( ContactAutoTypeEnum e : enums){
            if(e.getCode().equals( code)){
                return e.getDesc();
            }
        }
        return null;
    }

    public static Integer getCode( String value){
        ContactAutoTypeEnum[] enums = values();
        for( ContactAutoTypeEnum e : enums){
            if(e.getDesc().equals( value)){
                return e.getCode();
            }
        }
        return null;
    }
}
