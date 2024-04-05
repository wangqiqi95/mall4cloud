package com.mall4j.cloud.api.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionCommissionRateDTO {

    @ApiModelProperty("门店ID")
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @ApiModelProperty("商品ID集合")
    @NotNull(message = "商品ID集合不能为空")
    private List<Long> spuIdList;

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty("订单类型 0-普通订单 1-代购订单")
    private Integer limitOrderType;
}
