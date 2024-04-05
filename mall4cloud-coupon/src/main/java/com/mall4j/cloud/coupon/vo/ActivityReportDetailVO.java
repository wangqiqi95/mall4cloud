package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活动报表详情信息
 * @author shijing
 * @date 2022-03-27
 */
@Data
@ApiModel(description = "活动报表详情信息")
public class ActivityReportDetailVO implements Serializable {

    @ApiModelProperty(value = "券ID")
    private Long id;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "领券数")
    private Integer receiveNum;

    @ApiModelProperty(value = "核销数")
    private Integer writeOffNum;

    @ApiModelProperty(value = "活动收入")
    private BigDecimal activityIncome;

    @ApiModelProperty(value = "券过期数")
    private Integer overdueNum;

    @ApiModelProperty(value = "累计核销率")
    private BigDecimal writeOffRatio;

    /**
     * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
     */
    @ApiModelProperty(value = "优惠券来源信息： 1：小程序添加/2：CRM同步优惠券")
    private Integer couponSourceType;


}
