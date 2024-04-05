package com.mall4j.cloud.coupon.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class ChooseMemberEventMobileRelationPageDTO {

    @NotNull(message = "活动ID不能为null")
    @ApiModelProperty(value = "制定会员活动ID")
    private Long eventId;

    @ApiModelProperty(value = "用户绑定手机号")
    private String mobile;

    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码")
    private Long pageNum;

    @NotNull(message = "条数不能为空")
    @Max(value = 2000,message = "条数最大值不能超过2000")
    @ApiModelProperty(value = "条数")
    private Long pageSize;

}
