package com.mall4j.cloud.api.group.feign.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("下单送赠品app实体")
public class OrderGiftInfoAppVO implements Serializable {
    @ApiModelProperty("活动id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("选择赠品数量")
    private Integer giftLimit;
    @ApiModelProperty("赠品集合")
    private List<OrderGiftStockAppVO> gifts;
}
