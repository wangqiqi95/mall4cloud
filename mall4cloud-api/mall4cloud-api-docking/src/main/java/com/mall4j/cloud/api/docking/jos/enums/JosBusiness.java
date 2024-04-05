package com.mall4j.cloud.api.docking.jos.enums;

/**
 * 京东益世业务枚举
 *
 * @author Zhang Fan
 * @date 2022/8/25 16:13
 */
public enum JosBusiness {

    MicroCustomerCommission("1"), JointVentureCommission("2");

    String businessCode;

    JosBusiness(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getBusinessCode() {
        return this.businessCode;
    }
}
