package com.mall4j.cloud.order.feign;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.mall4j.cloud.api.order.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.order.dto.OrderRefundDto;
import com.mall4j.cloud.api.order.feign.OrderStatusFeignClient;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.service.OrderStatusFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderStatusFeignController implements OrderStatusFeignClient {

	@Autowired
	OrderStatusFeignService orderStatusFeignService;
	/**
	 * 方法描述：中台订单发货
	 *
	 * @param orderDeliveryDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2021-12-28 18:10:48
	 */
    @Override
    public ServerResponseEntity orderDelivery(OrderDeliveryDto orderDeliveryDto) {
		return orderStatusFeignService.orderDelivery(orderDeliveryDto);
	}

	@Override
    public ServerResponseEntity orderDeliverys(List<OrderDeliveryDto> orderDeliveryDtos) {
		ServerResponseEntity serverResponseEntity = ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
    	if (!CollectionUtils.isEmpty(orderDeliveryDtos)) {
			for (OrderDeliveryDto orderDeliveryDto : orderDeliveryDtos) {
				serverResponseEntity = orderStatusFeignService.orderDelivery(orderDeliveryDto);
				if (serverResponseEntity.isFail()) {
					break;
				}
			}
		}
		return serverResponseEntity;
	}

	@Override
	public ServerResponseEntity<Void> returnMoney(OrderRefundDto orderRefundParam) {
		return orderStatusFeignService.returnMoney(orderRefundParam);
	}
}
