package com.mall4j.cloud.order.dto.multishop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author FrozenWatermelon
 */
@Data
public class OrderRefundDTO {
    /**
     * 退款ID
     */
    @NotNull(message = "退款ID不能为空")
    private Long refundId;

    /**
     * 处理状态（2:同意，3:不同意）
     */
    @NotNull(message = "处理状态不能为空")
    private Integer refundSts;

    /**
     * 退款原因
     */
    private String rejectMessage;

    /**
     * 退款地址标识（ID）
     */
    private Long shopRefundAddrId;

    /**
     * 商家备注
     */
    private String sellerMsg;

    /**
     * 是否收到货
     */
    @NotNull(message = "是否收到货不能为空")
    private Boolean isReceived;

    @ApiModelProperty("是否退运费 0不处理， 1退 2不退")
    private Integer returnFreightFlag = 0;

    @ApiModelProperty("退单退货记录id")
    private Long refundAddrId;

    @ApiModelProperty(value = "物流公司ID", required = true)
    private Long deliveryCompanyId;

    @ApiModelProperty(value = "物流公司名称", required = true)
    private String deliveryName;

    @ApiModelProperty(value = "物流单号", required = true)
    @Length(max = 50, message = "物流单号长度不能超过50")
    private String deliveryNo;


    @ApiModelProperty(value = "外部售后编号",hidden = true)
    private Long outRefundId;
    @ApiModelProperty(value = "订单来源 0普通订单 1直播订单 2视频号3.0订单 3 视频号4.0订单",hidden = true)
    private Integer orderSource;
}
