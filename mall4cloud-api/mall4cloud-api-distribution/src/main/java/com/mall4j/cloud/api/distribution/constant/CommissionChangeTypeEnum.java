package com.mall4j.cloud.api.distribution.constant;

/**
 * @Author ZengFanChang
 * @Date 2021/12/11
 */
public enum CommissionChangeTypeEnum {

    /**
     * 增加待结算佣金
     */
    ADD_WAIT_SETTLE(1, 1, "增加待结算佣金"),
    /**
     * 扣减待结算佣金
     */
    REDUCE_WAIT_SETTLE(2, -1, "扣减待结算佣金"),
    /**
     * 增加可提现佣金
     */
    ADD_CAN_WITHDRAW(3, 1, "增加可提现佣金"),
    /**
     * 扣减可提现佣金
     */
    REDUCE_CAN_WITHDRAW(4, -1, "扣减可提现佣金"),
    /**
     * 增加已提现佣金
     */
    ADD_ALREADY_WITHDRAW(5, 1, "增加已提现佣金"),
    /**
     * 扣减已提现佣金
     */
    REDUCE_ALREADY_WITHDRAW(6, -1, "扣减已提现佣金"),
    /**
     * 增加已提现需退还佣金
     */
    ADD_WITHDRAW_NEED_REFUND(7, 1, "增加已提现需退还佣金"),
    /**
     * 扣减已提现需退还佣金
     */
    REDUCE_WITHDRAW_NEED_REFUND(8, -1, "扣减已提现需退还佣金");

    private Integer type;

    private Integer operation;

    private String desc;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    CommissionChangeTypeEnum(Integer type, Integer operation, String desc) {
        this.type = type;
        this.operation = operation;
        this.desc = desc;
    }

    public static CommissionChangeTypeEnum instance(Integer type) {
        CommissionChangeTypeEnum[] enums = values();
        for (CommissionChangeTypeEnum changeTypeEnum : enums) {
            if (changeTypeEnum.getType().equals(type)) {
                return changeTypeEnum;
            }
        }
        return null;
    }

}
