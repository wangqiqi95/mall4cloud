package com.mall4j.cloud.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VeekerStatusEnum {

    STATUS_0(0,"禁用"),
    STATUS_1(1,"启用"),
    STATUS_2(2,"拉黑");

    private final Integer status;
    private final String desc;

    public static String desc(Integer status) {
        VeekerStatusEnum[] values = VeekerStatusEnum.values();
        for (VeekerStatusEnum veekerStatusEnum : values) {
            if (veekerStatusEnum.status == status) {
                return veekerStatusEnum.getDesc();
            }
        }
        return null;
    }

}
