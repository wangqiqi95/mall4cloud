package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.openapi.skq_erp.dto.ProductPriceSyncDto;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.product.dto.ErpPriceDTO;
import com.mall4j.cloud.api.product.dto.ErpSkuPriceDTO;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述：商品价格同步
 *
 * @date 2022/1/14 20:51：03
 */
@Service("stdPriceService")
public class StdPriceServiceImpl implements IStdHandlerService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(StdPriceServiceImpl.class);

	private static final String method = "std.universal.price";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	OnsMQTemplate productPriceSyncTemplate;

	/**
	 * 方法描述：商品价格同步
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
			List<ProductPriceSyncDto> productPriceSyncDtos = null;
			if (StringUtils.isBlank(bodyStr) || CollectionUtils.isEmpty(productPriceSyncDtos = JSONObject.parseArray(bodyStr, ProductPriceSyncDto.class))) {
				stdResult = StdResult.fail("请求参数为空");
				return stdResult;
			}
			for (ProductPriceSyncDto productPriceSyncDto : productPriceSyncDtos) {
				if ((stdResult = productPriceSyncDto.check()).fail()) {
					return stdResult;
				}
			}

			//数据优化去重
			HashMap<String, ProductPriceSyncDto> productPriceSyncDtosDTOHashMap = new HashMap<>();
			productPriceSyncDtos.forEach(item->{
				String key = item.getProductCode()+item.getPriceType()+item.getStoreCode();
				if(!productPriceSyncDtosDTOHashMap.containsKey(key)){
					productPriceSyncDtosDTOHashMap.put(key,item);
				}
			});
			productPriceSyncDtos = new ArrayList<>(productPriceSyncDtosDTOHashMap.values());

			productPriceSyncTemplate.syncSend(convert(productPriceSyncDtos));
		} catch (Exception e) {
			logger.error(requestId + "-中台中台商品价格同步推送调用处理异常", e);
			stdResult = StdResult.fail("处理失败");
		} finally {
			logger.info("中台商品价格同步推送-{}-query请求参数:{},json请求参数:{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, stdResult, System.currentTimeMillis() - start);
		}
		return stdResult;
	}

	public ErpPriceDTO convert(List<ProductPriceSyncDto> data) {
		ErpPriceDTO erpPriceDTO = new ErpPriceDTO();
		//组装数据
		HashMap<String, ErpSkuPriceDTO> stringErpSkuPriceDTOHashMap = new HashMap<>();
		data.forEach(productPriceSyncDto -> {
			ErpSkuPriceDTO erpSkuPriceDTO = new ErpSkuPriceDTO();
			String key = productPriceSyncDto.getProductCode() + productPriceSyncDto.getStoreCode();
			if (stringErpSkuPriceDTOHashMap.containsKey(key)) {
				erpSkuPriceDTO = stringErpSkuPriceDTOHashMap.get(key);
			}
			erpSkuPriceDTO.setPriceCode(productPriceSyncDto.getProductCode());
			erpSkuPriceDTO.setStoreCode(productPriceSyncDto.getStoreCode());
			erpSkuPriceDTO.setProductCode(productPriceSyncDto.getProductCode());
			//价格类型 1-吊牌价 2-保护价 3-活动价
			erpSkuPriceDTO.setPriceType(productPriceSyncDto.getPriceType());
			erpSkuPriceDTO.setPrice(productPriceSyncDto.getPrice() * 100);
//			if (productPriceSyncDto.getPriceType() == 1) {
//				erpSkuPriceDTO.setMarketPrice(productPriceSyncDto.getPrice() * 100);
//			} else if (productPriceSyncDto.getPriceType() == 2) {
//				erpSkuPriceDTO.setPrice(productPriceSyncDto.getPrice() * 100);
//			} else if (productPriceSyncDto.getPriceType() == 3) {
//				erpSkuPriceDTO.setActivityPrice(productPriceSyncDto.getPrice() * 100);
//			}
			stringErpSkuPriceDTOHashMap.put(key, erpSkuPriceDTO);
		});
		ArrayList<ErpSkuPriceDTO> erpSkuPriceDTOS = new ArrayList<>(stringErpSkuPriceDTOHashMap.values());
		erpPriceDTO.setSkuPriceDTOList(erpSkuPriceDTOS);
		return erpPriceDTO;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		register(method, this);
	}
}
