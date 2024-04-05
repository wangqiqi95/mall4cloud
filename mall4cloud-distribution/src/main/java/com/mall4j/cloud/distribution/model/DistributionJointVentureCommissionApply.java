package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 联营分佣申请
 *
 * @author Zhang Fan
 * @date 2022/8/5 14:43
 */
@Data
public class DistributionJointVentureCommissionApply extends BaseModel {
    @ApiModelProperty("联营分佣申请id")
    private Long id;

    @ApiModelProperty("申请编号")
    private String applyNo;

    @ApiModelProperty("订单时间上界")
    private Date orderTimeUpperBound;

    @ApiModelProperty("订单时间下界")
    private Date orderTimeLowerBound;

    @ApiModelProperty("订单成交金额")
    private Long orderTurnover;

    @ApiModelProperty("订单退款金额")
    private Long orderRefundTurnover;

    @ApiModelProperty("分佣比例")
    private Long commissionRate;

    @ApiModelProperty("分佣金额")
    private Long commissionAmount;

    @ApiModelProperty("分佣退款金额")
    private Long commissionRefundAmount;

    @ApiModelProperty("客户id")
    private Long customerId;

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty("客户手机号")
    private String customerPhone;

    @ApiModelProperty("状态 0-待审核 1-待付款 2-付款成功 9-审核失败")
    private Integer status;

    @ApiModelProperty("审核人id")
    private Long auditUserId;

    @ApiModelProperty("审核时间")
    private Date auditTime;

    @ApiModelProperty("请求id（第三方打款平台返回）")
    private String requestId;

    @ApiModelProperty("请求响应信息（第三方打款平台返回）")
    private String requestRespInfo;

    @ApiModelProperty("订单id集合")
    private List<Long> selectedOrderIdList;

    @ApiModelProperty("发佣失败原因")
    private String transferFailReason;
}