package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.openapi.skq_erp.config.OpenApiStdParams;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdShipDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.order.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.order.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.feign.OrderStatusFeignClient;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：中台发货推送信息处理逻辑
 *
 * @date 2022/1/6 23:36：30
 */
@Service("stdShipService")
public class StdShipServiceImpl implements IStdHandlerService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(StdShipServiceImpl.class);

	private static final String method = "std.universal.ship";

	@Autowired
	OrderStatusFeignClient orderStatusFeignClient;
	@Autowired
	SkuFeignClient skuFeignClient;
	@Autowired
	OpenApiStdParams openApiStdParams;

	@Autowired
	OrderFeignClient orderFeignClient;

	/**
	 * 方法描述：中台发货推送信息处理逻辑
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
			List<StdShipDto> requestDatas = null;
			if (StringUtils.isBlank(bodyStr) || CollectionUtils.isEmpty(requestDatas = JSONObject.parseArray(JSON.parseObject(bodyStr).getString("data"), StdShipDto.class))) {
				stdResult = StdResult.fail("请求参数为空");
				return stdResult;
			}
			for (StdShipDto requestData : requestDatas) {
				if ((stdResult = requestData.check()).fail()) {
					return stdResult;
				}
			}
			for (StdShipDto requestData : requestDatas) {
				List<StdShipDto.Delivery_order_item> delivery_order_item_list = requestData.getDelivery_order_items();
				if (!CollectionUtils.isEmpty(delivery_order_item_list)) {
					List<OrderDeliveryDto> deliveryDtos = new ArrayList<>();
                    OrderDeliveryDto deliveryDto = new OrderDeliveryDto();
//                    deliveryDto.setOrderNo(Long.parseLong(requestData.getTid()));
                    deliveryDto.setOrderNo(requestData.getTid());
                    deliveryDto.setDeliveryCode(requestData.getCompany_code());
                    deliveryDto.setLogisticsName(requestData.getLogistics_company_name());
                    deliveryDto.setLogisticNo(requestData.getOut_sid());
                    List<DeliveryOrderItemDTO> selectOrderItems = new ArrayList<>(1);
					for (StdShipDto.Delivery_order_item delivery_order_item : delivery_order_item_list) {
						DeliveryOrderItemDTO deliveryOrderItemDTO = new DeliveryOrderItemDTO();
						deliveryOrderItemDTO.setChangeNum(delivery_order_item.getReal_num());
						deliveryOrderItemDTO.setOrderItemId(Long.parseLong(delivery_order_item.getItem_id()));
						if (!openApiStdParams.isIsTest()) {
							//因为存在场景，可能为买家直接联系客服修改尺码发货，这里中台发货的商品会跟订单中实际商品sku不一致，这里取orderItem中的商品sku走流程
							ServerResponseEntity<OrderItemVO> itemResponse = orderFeignClient.getOrderItem(deliveryOrderItemDTO.getOrderItemId());
							if(itemResponse.isSuccess()){
								ServerResponseEntity<SkuVO> skuVOServerResponseEntity = skuFeignClient.insiderGetById(itemResponse.getData().getSkuId());
								if (skuVOServerResponseEntity.isSuccess()) {
									logger.info("中台发货-查询sku对象信息：{}",skuVOServerResponseEntity);
									SkuVO data = skuVOServerResponseEntity.getData();
									deliveryOrderItemDTO.setPic(data.getImgUrl());
									deliveryOrderItemDTO.setSpuName(data.getSpuName());
									deliveryOrderItemDTO.setSkuId(data.getSkuId());
									deliveryOrderItemDTO.setSpuId(data.getSpuId());
								}
							}

//							if (StringUtils.isNotBlank(delivery_order_item.getSku_id())) {
//								ServerResponseEntity<SkuVO> skuVOServerResponseEntity = skuFeignClient.insiderGetBySkuCode(delivery_order_item.getSku());
//								if (skuVOServerResponseEntity.isSuccess()) {
//									logger.info("中台发货-查询sku对象信息：{}",skuVOServerResponseEntity);
//									SkuVO data = skuVOServerResponseEntity.getData();
//									deliveryOrderItemDTO.setPic(data.getImgUrl());
//									deliveryOrderItemDTO.setSpuName(data.getSpuName());
//									deliveryOrderItemDTO.setSkuId(data.getSkuId());
//									deliveryOrderItemDTO.setSpuId(data.getSpuId());
//								}
//							}
						}
						selectOrderItems.add(deliveryOrderItemDTO);
					}
                    deliveryDto.setSelectOrderItems(selectOrderItems);
                    deliveryDtos.add(deliveryDto);
					if (!openApiStdParams.isIsTest()) {
						logger.info("中台发货-feign调用参数对象：{}",deliveryDtos);
						responseEntity = orderStatusFeignClient.orderDeliverys(deliveryDtos);
						if (responseEntity.isFail()) {
							stdResult = StdResult.fail(responseEntity.getMsg());
							logger.info("中台发货-{}-feign调用处理失败，stdShipDto:{},feign请求参数为:{},feign响应为:{}", requestId, requestData, deliveryDtos, responseEntity);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(requestId + "-中台发货调用处理异常", e);
			stdResult = StdResult.fail("处理失败");
		} finally {
			logger.info("中台发货-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
		}
		return stdResult;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		register(method, this);
	}
}
