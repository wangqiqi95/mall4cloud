package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 高价值会员活动
 * @author Grady_Lu
 */
@Data
@ApiModel(description = "高价值会员活动物流信息参数")
public class ChooseMemberEventRecordDTO {
    @ApiModelProperty(value = "记录id",required = true)
    private Long id;
    @ApiModelProperty(value = "发货物流公司",required = true)
    private String logisticsCompany;
    @ApiModelProperty(value = "发货物流单号",required = true)
    private String trackingNumber;
}
