package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "库存变更日志实体")
public class LotteryStockChangeLogVO implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "调整时间")
    private Date optTime;
    @ApiModelProperty(value = "奖品名称")
    private String prizeName;
    @ApiModelProperty(value = "操作类型 1增加库存 2减少库存")
    private Integer optType;
    @ApiModelProperty(value = "调整数量")
    private Integer optNum;
    @ApiModelProperty(value = "调整人id")
    private Long optUserId;
    @ApiModelProperty(value = "调整人姓名")
    private Long optUserName;
}
