package com.mall4j.cloud.api.user.constant;

import java.util.Objects;

/**
 */
public enum ContactChangeTypeEnum {

    NORMAL(-1 , "normal"),//正常：0/1/2
    ADD_EXTERNAL_CONTACT(0 , "add_external_contact"),//添加企业客户事件
    EDIT_EXTERNAL_CONTACT(1 , "edit_external_contact"),//编辑企业客户事件
    ADD_HALF_EXTERNAL_CONTACT(2 , "add_half_external_contact"),//外部联系人免验证添加成员事件
    DEL_EXTERNAL_CONTACT(3 , "del_external_contact"),//删除企业客户事件: 员工删除外部联系人时
    DEL_FOLLOW_USER(4 , "del_follow_user"),//删除跟进成员事件: 员工被外部联系人删除时
    TRANSFER_FAIL(5 , "transfer_fail"),//客户接替失败事件
    DEL_ALL(6 , "del_all"),//互相删除
    STAFF_DIMISSION(7 , "staff_dimission"),//绑定员工离职
    BIND_OUT(8 , "bind_out");//解绑中

    private Integer code;

    private String desc;

    ContactChangeTypeEnum(Integer code, String desc) {
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
        ContactChangeTypeEnum[] enums = values();
        for( ContactChangeTypeEnum e : enums){
            if(e.getCode().equals( code)){
                return e.getDesc();
            }
        }
        return null;
    }

    /**
     * 好友关系状态
     * 被员工删除：3
     * 被客户删除：4
     * 删除是6
     * 其他 都是正常
     */
    public static String getDescName( Integer code){
        if(Objects.isNull(code)){
            return null;
        }
        if(code< ContactChangeTypeEnum.DEL_EXTERNAL_CONTACT.getCode() ||
                code== ContactChangeTypeEnum.TRANSFER_FAIL.getCode()){
            return "正常";
        }else if(code== ContactChangeTypeEnum.DEL_EXTERNAL_CONTACT.getCode()){
            return "被员工删除";
        }else if(code== ContactChangeTypeEnum.DEL_FOLLOW_USER.getCode()){
            return "被客户删除";
        }else if(code== ContactChangeTypeEnum.DEL_ALL.getCode()){
            return "删除";
        }else if(code== ContactChangeTypeEnum.TRANSFER_FAIL.getCode()){
            return "客户接替失败";
        }
        return null;
    }

    public static Integer getCode( String value){
        ContactChangeTypeEnum[] enums = values();
        for( ContactChangeTypeEnum e : enums){
            if(e.getDesc().equals( value)){
                return e.getCode();
            }
        }
        return null;
    }
}
