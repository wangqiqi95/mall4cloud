package com.mall4j.cloud.group.constant;


/**
 * 拼团订单状态类型
 * @author FrozenWatermelon
 */
public enum GroupOrderStatusEnum {

    /** 待支付 */
    WAITING_PAY(0, "待支付"),

    /** 支付成功 */
    SUCCESS(1, "支付成功"),

    /** 失效 */
    FAIL(-1, "失效");

    private Integer code;

    private String title;

    GroupOrderStatusEnum(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer value() {
        return code;
    }

}
