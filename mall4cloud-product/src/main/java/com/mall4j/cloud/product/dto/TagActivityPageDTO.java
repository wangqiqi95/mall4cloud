package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TagActivityPageDTO {
    @ApiModelProperty("活动名称")
    private  String activityName;

    @ApiModelProperty("活动状态 0 未开始 1 进行中 2 3已结束  ")
    private  Integer status;

    @ApiModelProperty("门店列表")
    private List<Integer> storeList;

    @ApiModelProperty("角标类型 1固定角标")
    private  Integer tagType;
}
