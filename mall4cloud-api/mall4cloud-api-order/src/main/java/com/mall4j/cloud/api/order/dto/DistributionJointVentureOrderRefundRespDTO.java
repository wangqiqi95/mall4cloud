package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 联营分佣查询dto
 *
 * @author
 */
@ApiModel("联营分佣退款返回dto")
@Data
public class DistributionJointVentureOrderRefundRespDTO {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("退款单ID")
    private Long refundId;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("退款时间")
    private Date refundTime;

    @ApiModelProperty("退款skuID")
    private Long skuId;

    @ApiModelProperty("退款数量")
    private Integer count;

}
