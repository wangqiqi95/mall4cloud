package com.mall4j.cloud.api.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author ZengFanChang
 * @Date 2022/01/16
 */
@Data
public class DistributionQueryDTO {
    @ApiModelProperty("商品名称")
    private String productName;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("退单号")
    private String refundNo;
    @ApiModelProperty("分销佣金状态 0-待结算 1-已结算")
    private Integer distributionStatus;
    @ApiModelProperty("下单人手机号")
    private String buyMobile;
    @ApiModelProperty("下单门店")
    private Long buyStoreId;
    @ApiModelProperty("下单人ID")
    private Long buyUserId;
    @ApiModelProperty("分销关系 1分享关系 2服务关系 3自主下单 4代客下单")
    private Integer distributionRelation;
    @ApiModelProperty("分销用户类型 1-导购 2-微客")
    private List<Integer> distributionUserTypes;
    @ApiModelProperty("分销用户ID集合")
    private List<Long> distributionUserIds;
    @ApiModelProperty("导购工号")
    private String distributionStaffNo;
    @ApiModelProperty("导购手机号")
    private String distributionStaffMobile;
    @ApiModelProperty("威客卡号")
    private String distributionUserNo;
    @ApiModelProperty("威客手机号")
    private String distributionUserMobile;
    @ApiModelProperty("分销门店ID")
    private Long distributionStoreId;
    @ApiModelProperty("订单类型")
    private Integer orderType;
    @ApiModelProperty("发展用户ID")
    private Long developingUserId;
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
    @ApiModelProperty("订单ID集合")
    private List<Long> orderIds;

    @ApiModelProperty("分销佣金入账结算开始时间")
    private Date distributionStartTime;
    @ApiModelProperty("分销佣金入账结算结束时间")
    private Date distributionEndTime;
    @ApiModelProperty("发展佣金入账结算开始时间")
    private Date developingStartTime;
    @ApiModelProperty("发展佣金入账结算结束时间")
    private Date developingEndTime;

    @ApiModelProperty("是否发展订单 true是 false否")
    private Boolean isDevelopingOrder;

    @ApiModelProperty("是否退款订单 true是 false否")
    private Boolean isRefundOrder;

    @ApiModelProperty("状态集合")
    private List<Integer> statusList;

}
