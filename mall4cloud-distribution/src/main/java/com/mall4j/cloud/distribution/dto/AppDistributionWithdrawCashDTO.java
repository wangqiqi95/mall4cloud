package com.mall4j.cloud.distribution.dto;

import com.mall4j.cloud.common.util.PriceUtil;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author cl
 * @date 2021-08-17 09:30:56
 */
public class AppDistributionWithdrawCashDTO {

    @ApiModelProperty(value = "提现金额(元)", required = true)
    @NotNull(message = "提现金额不能为空")
    @Max(value = PriceUtil.MAX_AMOUNT, message = "最大值不可以超过" + PriceUtil.MAX_AMOUNT)
    @Min(value = 1, message = "最小提现金额为1元")
    private BigDecimal amount;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AppDistributionWithdrawCashDTO{" +
                "amount=" + amount +
                '}';
    }
}
