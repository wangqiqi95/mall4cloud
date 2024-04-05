package com.mall4j.cloud.user.constant;


import lombok.Getter;

@Getter
public enum CouponFlayTypeEnum {

    YES(1),
    NO(0);

    private Integer hasCouponFlag;

    CouponFlayTypeEnum(Integer hasCouponFlag){
        this.hasCouponFlag = hasCouponFlag;
    }
}
