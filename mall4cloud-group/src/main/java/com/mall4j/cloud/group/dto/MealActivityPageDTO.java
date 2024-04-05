package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "套餐一口价分页实体")
public class MealActivityPageDTO extends PageDTO implements Serializable {
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动状态")
    private Integer status;
    @ApiModelProperty("适用门店id集合")
    private String shopIds;
}
