package com.mall4j.cloud.group.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperateStatisticsVO {

    @ApiModelProperty(value = "浏览次数")
    private Integer browseCount;

    @ApiModelProperty(value = "浏览人数")
    private Integer browsePeopleCount;

    @ApiModelProperty(value = "点击次数")
    private Integer clickCount;

    @ApiModelProperty(value = "点击人数")
    private Integer clickPeopleCount;

}
