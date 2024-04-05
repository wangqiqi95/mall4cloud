package com.mall4j.cloud.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VeekerAuditStatusEnum {

    STATUS_0(0,"待审核"),
    STATUS_1(1,"已同意"),
    STATUS_2(2,"已拒绝");

    private final Integer status;
    private final String desc;

    public static String desc(Integer status) {
        VeekerAuditStatusEnum[] values = VeekerAuditStatusEnum.values();
        for (VeekerAuditStatusEnum veekerAuditStatusEnum : values) {
            if (veekerAuditStatusEnum.status == status) {
                return veekerAuditStatusEnum.getDesc();
            }
        }
        return null;
    }

}
