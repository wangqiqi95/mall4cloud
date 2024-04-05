package com.mall4j.cloud.distribution.vo;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单分销佣金日志VO
 *
 * @author Zhang Fan
 * @date 2022/9/9 14:08
 */
@ApiModel("订单分销佣金日志VO")
@Data
public class DistributionOrderCommissionLogVO {

    @ApiModelProperty("支付时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("提现编号")
    private String withdrawOrderNo;

    @ApiModelProperty("售后单据")
    private String refundOrderNumber;

    @ApiModelProperty("订单商品金额")
    private Long amount;

    @ApiModelProperty("佣金类型 1-分销佣金 2-发展佣金")
    private Integer commissionType;

    @ApiModelProperty("佣金金额")
    private Long commissionAmount;

    @ApiModelProperty("佣金状态 1-已结算 2-已提现 3-已退还")
    private Integer commissionStatus;

    @ApiModelProperty("导购id")
    private Long userId;

    @ApiModelProperty("导购姓名")
    private String username;

    @ApiModelProperty("导购手机号")
    private String phone;

    @ApiModelProperty("导购工号")
    private String workNo;

    @ApiModelProperty("导购所属门店编码")
    private String storeCode;

    @ApiModelProperty("导购所属门店名称")
    private String storeName;

    @ApiModelProperty("订单是否使用企业券")
    private Boolean useEntCoupon;

    @ApiModelProperty("订单子项")
    private List<DistributionOrderCommissionLogItemVO> logOrderItems;
}
