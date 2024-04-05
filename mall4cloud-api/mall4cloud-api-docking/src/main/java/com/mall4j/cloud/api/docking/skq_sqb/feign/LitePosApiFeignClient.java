package com.mall4j.cloud.api.docking.skq_sqb.feign;

import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByCancelOrder;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByPurchase;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByQueryResult;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByRefund;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBCancelOrderResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBPurchaseResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBQueryResultResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBRefundResp;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 收钱吧轻POS销售类对外接口
 */
@FeignClient(value = "mall4cloud-docking",contextId = "litePos-Api")
public interface LitePosApiFeignClient {

	/**
	 * 购买
	 * @param sqbBodyByPurchase 购买传参
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/litePos/purchase")
	ServerResponseEntity<SQBPurchaseResp> purchase(@RequestBody SQBBodyByPurchase sqbBodyByPurchase);

	/**
	 * 退款
	 * @param sqbBodyByRefund 退款传参
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/litePos/refund")
	ServerResponseEntity<SQBRefundResp> refund(@RequestBody SQBBodyByRefund sqbBodyByRefund);

	/**
	 * 取消订单
	 * @param sqbBodyByCancelOrder 取消订单传参
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/litePos/cancelOrderOperation")
	ServerResponseEntity<SQBCancelOrderResp> cancelOrderOperation(@RequestBody SQBBodyByCancelOrder sqbBodyByCancelOrder);

	/**
	 * 销售类结果查询
	 * @param sqbBodyByQueryResult 销售类结果查询传参
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/litePos/queryResult")
	ServerResponseEntity<SQBQueryResultResp> queryResult(@RequestBody SQBBodyByQueryResult sqbBodyByQueryResult);

}
