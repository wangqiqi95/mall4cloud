package com.mall4j.cloud.api.order.vo;


/**
 * 用户购物数据
 * @author cl
 * @date 2021-08-16 13:10:02
 */
public class UserShoppingDataVO {

    /**
     * 用户消费笔数
     */
    private Long expenseNumber;

    /**
     * 用户消费金额, (分)
     */
    private Long sumOfConsumption;

    public Long getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(Long expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public Long getSumOfConsumption() {
        return sumOfConsumption;
    }

    public void setSumOfConsumption(Long sumOfConsumption) {
        this.sumOfConsumption = sumOfConsumption;
    }

    @Override
    public String toString() {
        return "UserShoppingDataVO{" +
                "expenseNumber=" + expenseNumber +
                ", sumOfConsumption=" + sumOfConsumption +
                '}';
    }
}
