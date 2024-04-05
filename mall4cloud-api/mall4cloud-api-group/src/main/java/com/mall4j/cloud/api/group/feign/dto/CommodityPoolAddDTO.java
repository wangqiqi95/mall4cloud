package com.mall4j.cloud.api.group.feign.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommodityPoolAddDTO {

    private List<Long> spuIdList;

    private List<Long> shopIdList;

    private Integer activityChannel;

    private Long activityId;

    private Date beginTime;

    private Date endTime;
}
