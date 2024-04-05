package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "预售活动分页实体")
public class AdvanceActivityPageDTO extends PageDTO implements Serializable {
    @ApiModelProperty("商品Id")
    private Long commodityId;
    @ApiModelProperty("活动状态")
    private Integer status;
    @ApiModelProperty("适用门店id")
    private Long shopId;
}
