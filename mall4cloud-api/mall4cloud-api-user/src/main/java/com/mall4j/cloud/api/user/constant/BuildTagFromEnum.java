package com.mall4j.cloud.api.user.constant;

/**
 * 给客户打标签动作枚举
 */
public enum BuildTagFromEnum {

    STAFF_CODE(0 , "渠道活码"),
    AUTO_GROUP(1 , "自动拉群"),
    WX_MP(2 , "公众号"),
    MATERIAL_BROWSE(3 , "素材浏览"),
    KEY_WORD(4,"敏感词命中"),
    REQUEST_H5_API(5,"h5调用"),
    REQUEST_PLATFORM_API(6,"后台调用");

    private Integer code;

    private String desc;

    BuildTagFromEnum(Integer code, String desc) {
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
        BuildTagFromEnum[] enums = values();
        for( BuildTagFromEnum e : enums){
            if(e.getCode().equals( code)){
                return e.getDesc();
            }
        }
        return null;
    }

    public static Integer getCode( String value){
        BuildTagFromEnum[] enums = values();
        for( BuildTagFromEnum e : enums){
            if(e.getDesc().equals( value)){
                return e.getCode();
            }
        }
        return null;
    }
}
