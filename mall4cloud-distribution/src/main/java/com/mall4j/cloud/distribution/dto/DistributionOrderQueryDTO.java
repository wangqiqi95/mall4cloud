package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author ZengFanChang
 * @Date 2022/01/16
 */
@Data
public class DistributionOrderQueryDTO {

    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("退单号")
    private String refundNo;
    @ApiModelProperty("分销佣金状态")
    private Integer distributionStatus;
    @ApiModelProperty("下单人手机号")
    private String buyMobile;
    @ApiModelProperty("下单门店")
    private Long buyStoreId;
    @ApiModelProperty("分销关系 1分享关系 2服务关系 3自主下单 4代客下单")
    private Integer distributionRelation;
    @ApiModelProperty("分销用户类型 1-导购 2-微客")
    private Integer distributionUserType;
    @ApiModelProperty("分销员工号")
    private String distributionUserNo;
    @ApiModelProperty("分销员手机号")
    private String distributionUserMobile;
    @ApiModelProperty("分销门店ID")
    private Long distributionStoreId;
    @ApiModelProperty("订单类型")
    private Integer orderType;
    @ApiModelProperty("发展用户工号")
    private String developingUserNo;
    @ApiModelProperty("发展用户手机号")
    private String developingUserMobile;
    @ApiModelProperty("发展用户门店ID")
    private Long developingStoreId;
    @ApiModelProperty("支付开始时间")
    private Date payStartTime;
    @ApiModelProperty("支付结束时间")
    private Date payEndTime;
    @ApiModelProperty("下单开始时间")
    private Date createStartTime;
    @ApiModelProperty("下单结束时间")
    private Date createEndTime;

}
