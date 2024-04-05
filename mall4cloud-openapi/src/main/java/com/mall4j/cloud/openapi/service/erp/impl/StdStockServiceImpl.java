package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdStockDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.product.dto.ErpSkuStockDTO;
import com.mall4j.cloud.api.product.dto.ErpStockDTO;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("stdStockService")
public class StdStockServiceImpl implements IStdHandlerService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(StdShipServiceImpl.class);

	// TODO
	private static final String method = "std.universal.stock";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	OnsMQTemplate productStockSyncTemplate;

	/**
	 * 方法描述：中台调用接口处理逻辑
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
			List<StdStockDto> requestDatas = null;
			if (StringUtils.isBlank(bodyStr) || CollectionUtils.isEmpty(requestDatas = JSONObject.parseArray(bodyStr, StdStockDto.class))) {
				stdResult = StdResult.fail("请求参数为空");
				return stdResult;
			}
			for (StdStockDto requestData : requestDatas) {
				if ((stdResult = requestData.check()).fail()) {
					return stdResult;
				}
			}
			productStockSyncTemplate.syncSend(convert(requestDatas));
		} catch (Exception e) {
			logger.error(requestId + "-中台标准库存接口调用处理异常", e);
			stdResult = StdResult.fail("处理失败");
		} finally {
			logger.info("中台标准库存接口-{}-query请求参数:{},json请求参数:{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, stdResult, System.currentTimeMillis() - start);
		}
		return stdResult;
	}

	public ErpStockDTO convert(List<StdStockDto> data) {
		ErpStockDTO erpStockDTO = new ErpStockDTO();
		List<ErpSkuStockDTO> erpSkuStockDTOList = data.stream().map(stdStockDto -> {
			ErpSkuStockDTO erpSkuStockDTO = new ErpSkuStockDTO();
			erpSkuStockDTO.setSkuCode(stdStockDto.getSkuCode());
			erpSkuStockDTO.setProductCode(stdStockDto.getProductCode());
			if(stdStockDto.getAvailableStockNum()<0){
				erpSkuStockDTO.setAvailableStockNum(0);
			}else{
				erpSkuStockDTO.setAvailableStockNum(stdStockDto.getAvailableStockNum());
			}
			erpSkuStockDTO.setStockType(stdStockDto.getStockType());
			erpSkuStockDTO.setStoreCode(stdStockDto.getStoreCode());
			erpSkuStockDTO.setSyncType(stdStockDto.getSyncType());
			return erpSkuStockDTO;
		}).collect(Collectors.toList());
		erpStockDTO.setStockDTOList(erpSkuStockDTOList);
		return erpStockDTO;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		register(method, this);
	}
}
