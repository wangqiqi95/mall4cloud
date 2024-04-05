package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 高价值会员活动
 * @author Grady_Lu
 */
@Data
@ApiModel(description = "高价值会员活动导入确认参数")
public class ChooseMemberEventRecordConfirmDTO {

    @ApiModelProperty(value = "记录id数组",required = true)
    private List<Long> ids;
}
