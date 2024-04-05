package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdPushRetDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.feign.OrderRefundFeignClient;
import com.mall4j.cloud.api.order.vo.OrderRefundVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：中台回传推订单/退单结果回执
 *
 * @date 2022/1/9 9:46：26
 */
@Service("stdPushRetService")
public class StdPushRetServiceImpl implements IStdHandlerService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(StdShipServiceImpl.class);

	private static final String method = "std.universal.pushRet";
	@Autowired
	OrderFeignClient orderFeignClient;
	@Autowired
	OrderRefundFeignClient orderRefundFeignClient;
	@Autowired
	private OnsMQTemplate stdOrderNotifyTemplate;

	/**
	 * 方法描述：中台回传推订单/退单结果回执
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

		try {
			List<StdPushRetDto> requestDatas = null;
			if (StringUtils.isBlank(bodyStr) || CollectionUtils.isEmpty(requestDatas = JSONObject.parseArray(JSON.parseObject(bodyStr).getString("data"), StdPushRetDto.class))) {
				stdResult = StdResult.fail("请求参数为空");
				return stdResult;
			}
			for (StdPushRetDto stdPushRetDto : requestDatas) {
				if ((stdResult = stdPushRetDto.check()).fail()) {
					return stdResult;
				}
			}

			for (StdPushRetDto requestData : requestDatas) {
				if (requestData.getSuccess() == null || !requestData.getSuccess()) {
					logger.info("中台接收订单失败：" + requestData);
					if("order".equals(requestData.getType())){
						ServerResponseEntity<EsOrderBO> esOrderBOServerResponse = orderFeignClient.getEsOrderByOrderNumber(requestData.getTid());
						if(esOrderBOServerResponse.isSuccess() && esOrderBOServerResponse.getData()!=null){
							stdOrderNotifyTemplate.syncSend(esOrderBOServerResponse.getData().getOrderId(), RocketMqConstant.ORDER_NOTIFY_STD_TOPIC_TAG);
						}
						logger.info("回执重推订单到中台：" + requestData);
					}
					if("refund".equals(requestData.getType())){
						ServerResponseEntity<OrderRefundVO> orderRefundVOServerResponse = orderRefundFeignClient.getOrderRefundByRefundNumber(requestData.getTid());
						if(orderRefundVOServerResponse.isSuccess() && orderRefundVOServerResponse.getData()!=null){
							OrderRefundVO orderRefundVO = orderRefundVOServerResponse.getData();
							logger.info("回执重推退单到中台:{},退单查询结果:{}",requestData,JSONObject.toJSONString(orderRefundVO));
							orderRefundFeignClient.pushRefund(orderRefundVO.getRefundId(),orderRefundVO.getReturnMoneySts());
							logger.info("回执重推退单到中台：" + requestData);
						}
					}
				}
			}

			// TODO 待确认后续处理逻辑
		} catch (Exception e) {
			logger.error(requestId + "-中台回传推订单/退单结果回执调用处理异常", e);
			stdResult = StdResult.fail("处理失败");
		} finally {
			logger.info("中台回传推订单/退单结果回执-{}-query请求参数:{},json请求参数:{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, stdResult, System.currentTimeMillis() - start);
		}
		return stdResult;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		register(method, this);
	}
}
