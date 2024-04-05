package com.mall4j.cloud.order.constant;

/**
 * 发票状态 1.申请中 2.已开票
 * @author Pineapple
 * @date 2021/8/2 8:51
 */
public enum OrderInvoiceState {

    /**
     * 申请中
     */
    APPLICATION(1),
    /**
     * 已开票
     */
    ISSUED(2);

    private final Integer num;

    OrderInvoiceState(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static OrderInvoiceState instance(Integer value) {
        OrderInvoiceState[] enums = values();
        for (OrderInvoiceState stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
