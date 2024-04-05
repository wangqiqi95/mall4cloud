package com.mall4j.cloud.coupon.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("券包管理分页查询条件实体")
public class CouponPackPageDTO extends PageDTO implements Serializable {
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动状态")
    private Integer status;
    @ApiModelProperty("适用门店id")
    private String shopIds;
}
