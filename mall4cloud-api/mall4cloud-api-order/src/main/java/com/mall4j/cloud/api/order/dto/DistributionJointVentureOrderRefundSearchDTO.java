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
@ApiModel("联营分佣退款查询dto")
@Data
public class DistributionJointVentureOrderRefundSearchDTO {

    @ApiModelProperty("联营分佣状态 联营分佣状态 0-待分佣 1-完成分佣")
    private Integer jointVentureCommissionStatus;

    @ApiModelProperty("联营分佣退款状态 0-待分佣 1-完成分佣")
    private Integer jointVentureRefundStatus;

    @ApiModelProperty("退款时间:开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "退款开始时间不能为空")
    private Date refundStartTime;

    @ApiModelProperty("退款时间:结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "退款结束时间不能为空")
    private Date refundEndTime;

    @ApiModelProperty("门店id列表")
    private List<Long> storeIdList;

    @ApiModelProperty("订单id集合")
    private List<Long> selectedOrderIdList;



}
