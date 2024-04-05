package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.openapi.skq_erp.config.OpenApiStdParams;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdRefundInDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.order.dto.OrderRefundDto;
import com.mall4j.cloud.api.order.feign.OrderStatusFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 类描述：中台退货入库推送信息处理逻辑
 *
 * @date 2022/1/7 0:07：08
 */
@Service("stdRefundInService")
public class StdRefundInServiceImpl implements IStdHandlerService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(StdRefundInServiceImpl.class);

	private static final String method = "std.universal.refundIn";

	@Autowired
	OrderStatusFeignClient orderStatusFeignClient;

	@Autowired
	OpenApiStdParams openApiStdParams;
	/**
	 * 方法描述：中台退货入库处理逻辑
	 *
	 * @param commonReq
	 * @param bodyStr
	 * @return com.mall4j.cloud.api.openapi.skq_erp.response.StdResult
	 * @date 2022-01-06 23:35:00
	 */
	@Override
	public StdResult stdHandler(StdCommonReq commonReq, String bodyStr) {
		long start = System.currentTimeMillis();
		String requestId = UuidUtils.generateUuid();
		StdResult stdResult = StdResult.success();
		ServerResponseEntity responseEntity = null;
		try {
			List<StdRefundInDto> stdRefundInDtos = null;
			if (StringUtils.isBlank(bodyStr) || CollectionUtils.isEmpty(stdRefundInDtos = JSONObject.parseArray(JSON.parseObject(bodyStr).getString("data"), StdRefundInDto.class))) {
				stdResult = StdResult.fail("请求参数为空");
				return stdResult;
			}
			for (StdRefundInDto stdRefundInDto : stdRefundInDtos) {
				if ((stdResult = stdRefundInDto.check()).fail()) {
					return stdResult;
				}
			}
			for (StdRefundInDto stdRefundInDto : stdRefundInDtos) {
				OrderRefundDto orderRefundDto = new OrderRefundDto();
//				orderRefundDto.setRefundId(Long.valueOf(stdRefundInDto.getRefund_id()));
				orderRefundDto.setRefundId(stdRefundInDto.getRefund_id());
				orderRefundDto.setIsReceived(Boolean.TRUE);
				orderRefundDto.setRefundSts(2);
				if (!openApiStdParams.isIsTest()) {
					responseEntity = orderStatusFeignClient.returnMoney(orderRefundDto);
					if (responseEntity.isFail()) {
						stdResult = StdResult.fail(responseEntity.getMsg());
						logger.info("中台退货入库-{}-feign调用处理失败，stdRefundInDto:{},feign请求参数为:{},feign响应为:{}", requestId, stdRefundInDto, orderRefundDto,
								responseEntity);
					}
				}
			}
		} catch (Exception e) {
			logger.error(requestId + "-中台退货入库调用处理异常", e);
			stdResult = StdResult.fail("处理失败");
		} finally {
			logger.info("中台退货入库-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
		}
		return stdResult;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		register(method, this);
	}
}
