package com.mall4j.cloud.api.order.bo;

import com.mall4j.cloud.common.order.vo.OrderItemVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021/2/5
 */
@Data
public class OrderBO {
    @ApiModelProperty(value = "订单号")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;
}
