package com.mall4j.cloud.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 埋点数据统计类
 */
@Data
public class BurialPointStatisticsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("浏览次数")
    private Long browseNum;

    @ApiModelProperty("浏览人数")
    private Integer browsePeopleNum;
}
