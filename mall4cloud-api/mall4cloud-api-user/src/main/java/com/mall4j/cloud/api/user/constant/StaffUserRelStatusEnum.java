package com.mall4j.cloud.api.user.constant;

/**
 * 绑定关系: 1-绑定 2-解绑中 3-删除客户 4-绑定员工离职
 */
public enum StaffUserRelStatusEnum {

    BIND(1 , "bind"),//绑定
    BIND_OUT(2 , "bind_out"),//解绑中
    DEL(3 , "del_all"),//删除客户
    STAFF_DIMISSION(4 , "staff_dimission");//绑定员工离职

    private Integer code;

    private String desc;

    StaffUserRelStatusEnum(Integer code, String desc) {
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
        StaffUserRelStatusEnum[] enums = values();
        for( StaffUserRelStatusEnum e : enums){
            if(e.getCode().equals( code)){
                return e.getDesc();
            }
        }
        return null;
    }

    public static Integer getCode( String value){
        StaffUserRelStatusEnum[] enums = values();
        for( StaffUserRelStatusEnum e : enums){
            if(e.getDesc().equals( value)){
                return e.getCode();
            }
        }
        return null;
    }
}
