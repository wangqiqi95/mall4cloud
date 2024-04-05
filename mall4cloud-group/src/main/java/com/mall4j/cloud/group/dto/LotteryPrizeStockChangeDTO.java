package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "抽奖活动库存变更入参实体")
public class LotteryPrizeStockChangeDTO implements Serializable {
    @ApiModelProperty(value = "奖品id")
    private Integer id;
    @ApiModelProperty(value = "活动id")
    private Integer lotteryDrawId;
    @ApiModelProperty(value = "变更类型 1增加 2减少")
    private Integer changeType;
    @ApiModelProperty(value = "变更数量")
    private Integer changeNum;
    @ApiModelProperty(value = "奖品名称")
    private String prizeName;
}
