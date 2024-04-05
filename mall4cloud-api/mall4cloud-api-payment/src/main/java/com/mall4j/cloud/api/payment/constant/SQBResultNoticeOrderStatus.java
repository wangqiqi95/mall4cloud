package com.mall4j.cloud.api.payment.constant;

/**
 * 收钱吧回调订单状态
 */
public enum SQBResultNoticeOrderStatus {
    
    /*
         产生回调时订单状态可能有：  0：已取消，4：操作完成，6：操作失败，7：已终止
     */
    CANCELED(0,"已取消"),
    SUCCESS(4,"操作完成"),
    FAIL(6,"操作失败"),
    TERMINATED(7,"已终止");
    
    private final Integer value;
    
    private final String desc;
    
    public Integer value() {
        return value;
    }
    
    public String desc() {
        return desc;
    }
    
    SQBResultNoticeOrderStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    public static SQBResultNoticeOrderStatus instance(Integer value) {
        SQBResultNoticeOrderStatus[] enums = values();
        for (SQBResultNoticeOrderStatus statusEnum : enums) {
            if (statusEnum.value.equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}