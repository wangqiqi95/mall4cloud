package com.mall4j.cloud.docking.skq_sqb.controller;


import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByCancelOrder;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByPurchase;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByQueryResult;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByRefund;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBCancelOrderResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBPurchaseResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBQueryResultResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBRefundResp;
import com.mall4j.cloud.api.docking.skq_sqb.feign.LitePosApiFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_sqb.service.LitePosService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对接收钱吧轻Pos相关接口
 */
@RestController
@Api(tags = "对接收钱吧轻Pos相关接口")
public class LitePosApiController implements LitePosApiFeignClient {
	
	@Autowired
	private LitePosService litePosService;
	
	@Override
	public ServerResponseEntity<SQBPurchaseResp> purchase(SQBBodyByPurchase sqbBodyByPurchase){
		return litePosService.purchase(sqbBodyByPurchase);
	}

	@Override
	public ServerResponseEntity<SQBRefundResp> refund(SQBBodyByRefund sqbBodyByRefund) {
		return litePosService.refund(sqbBodyByRefund);
	}

	@Override
	public ServerResponseEntity<SQBCancelOrderResp> cancelOrderOperation(SQBBodyByCancelOrder sqbBodyByCancelOrder) {
		return litePosService.cancelOrderOperation(sqbBodyByCancelOrder);
	}

	@Override
	public ServerResponseEntity<SQBQueryResultResp> queryResult(SQBBodyByQueryResult sqbBodyByQueryResult) {
		return litePosService.queryResult(sqbBodyByQueryResult);
	}

}
