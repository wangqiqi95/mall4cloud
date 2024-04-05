package com.mall4j.cloud.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Pineapple
 * @date 2021/6/9 9:25
 */
@Data
public class OrderItemPriceErrorVO {

    private Long orderId;
    private Long userId;
    private Long storeId;
    private String orderNumber;

    private Long spuId;
    private String spuCode;
    private Long skuId;
    private String skuCode;
    private Long marketPriceFee;

    @ApiModelProperty(value = "实际总值", required = true)
    private Long actualTotal;
    @ApiModelProperty(value = "产品价格", required = true)
    private Long price;

    @ApiModelProperty(value = "订单创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "订单支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date payTime;

}
