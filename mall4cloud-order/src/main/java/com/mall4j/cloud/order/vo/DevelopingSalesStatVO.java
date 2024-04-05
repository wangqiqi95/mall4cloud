package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/02/26
 */
@Data
public class DevelopingSalesStatVO {

    @ApiModelProperty("威客数量")
    private Integer witkeyNum;

    @ApiModelProperty("累计业绩")
    private Long totalSales;

    @ApiModelProperty("威客下单业绩")
    private Long witkeySales;

    @ApiModelProperty("分享下单业绩")
    private Long shareSales;

    @ApiModelProperty("订单数")
    private Integer orderNum;

}
