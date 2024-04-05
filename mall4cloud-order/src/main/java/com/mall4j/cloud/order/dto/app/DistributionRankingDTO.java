package com.mall4j.cloud.order.dto.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/02/13
 */
@Data
public class DistributionRankingDTO {

    @ApiModelProperty("导购ID")
    private Long staffId;

    @ApiModelProperty("导购名称")
    private String staffName;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("销售业绩")
    private Long totalSales;

    @ApiModelProperty("佣金")
    private Long commission;

}
