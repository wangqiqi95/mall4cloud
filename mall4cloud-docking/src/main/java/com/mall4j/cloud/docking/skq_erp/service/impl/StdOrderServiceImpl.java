package com.mall4j.cloud.docking.skq_erp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushOrderDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushRefundDto;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdOrderService;
import com.mall4j.cloud.docking.utils.StdClients;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("stdOrderService")
public class StdOrderServiceImpl implements IStdOrderService {

	private static final Logger logger = LoggerFactory.getLogger(StdClients.class);

	private static final String PUSH_ORDER_URI= "/api/ip/std/xxy/service";
	private static final String PUSH_ORDER_METHOD= "std.universal.pushOrder";

	private static final String PUSH_REFUND_URI= "/api/ip/std/xxy/service";
	private static final String PUSH_REFUND_METHOD= "std.universal.pushRefund";

	/**
	 * 推订单到中台
	 *
	 * @param pushOrderDto 待推送的订单
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	@Override
	public ServerResponseEntity<String> pushOrder(List<PushOrderDto> pushOrderDto) {
		if (CollectionUtils.isEmpty(pushOrderDto)) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}

		JSONObject body = new JSONObject();
		body.put("data", pushOrderDto);
		String s = StdClients.clients().postStd(PUSH_ORDER_URI, PUSH_ORDER_METHOD, body.toJSONString());
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity<String> fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("推送订单至中台无响应");
			return fail;
		}
		JSONObject resp = JSONObject.parseObject(s);
		Boolean isSuccess = resp.getBoolean("isSuccess");
		String errorMsg = resp.getString("errorMsg");
		if (isSuccess != null && isSuccess) {
			JSONObject data = resp.getJSONObject("data");
			String requestId = data.getString("requestId");
			return ServerResponseEntity.success(requestId);
		}
		ServerResponseEntity<String> fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(StringUtils.isBlank(errorMsg) ? "推送订单至中台失败" : errorMsg);
		return fail;
	}

	/**
	 * 推退单到中台
	 *
	 * @param pushRefundDtos 待推送的退单
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	@Override
	public ServerResponseEntity<String> pushRefund(List<PushRefundDto> pushRefundDtos) {
		if (CollectionUtils.isEmpty(pushRefundDtos)) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}

		JSONObject body = new JSONObject();
		body.put("data", pushRefundDtos);
		String s = StdClients.clients().postStd(PUSH_REFUND_URI, PUSH_REFUND_METHOD, body.toJSONString());
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity<String> fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("推送退单至中台无响应");
			return fail;
		}
		JSONObject resp = JSONObject.parseObject(s);
		Boolean isSuccess = resp.getBoolean("isSuccess");
		String errorMsg = resp.getString("errorMsg");
		if (isSuccess != null && isSuccess) {
			JSONObject data = resp.getJSONObject("data");
			String requestId = data.getString("requestId");
			return ServerResponseEntity.success(requestId);
		}
		ServerResponseEntity<String> fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(StringUtils.isBlank(errorMsg) ? "推送退单至中台失败" : errorMsg);
		return fail;
	}

}
