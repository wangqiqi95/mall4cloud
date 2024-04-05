package com.mall4j.cloud.docking.skq_erp.service;

import com.mall4j.cloud.api.docking.skq_erp.dto.PushOrderDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushRefundDto;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface IStdOrderService {

	/**
	 * 推订单到中台
	 *
	 * @param pushOrderDto 待推送的订单
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	ServerResponseEntity<String> pushOrder(List<PushOrderDto> pushOrderDto);


	/**
	 * 推退单到中台
	 *
	 * @param pushRefundDtos 待推送的退单
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	ServerResponseEntity<String> pushRefund(List<PushRefundDto> pushRefundDtos);
}
