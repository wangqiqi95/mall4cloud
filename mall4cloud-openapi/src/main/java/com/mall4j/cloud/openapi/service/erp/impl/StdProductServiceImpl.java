package com.mall4j.cloud.openapi.service.erp.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.openapi.constant.RedisConstant;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdProductDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.product.dto.ErpSyncDTO;
import com.mall4j.cloud.api.product.dto.SkuErpSyncDTO;
import com.mall4j.cloud.api.product.dto.SpuErpSyncDTO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.PrincipalUtil;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类描述：基础商品信息同步
 *
 * @date 2022/1/16 9:38：26
 */
@Service("stdProductService")
public class StdProductServiceImpl implements IStdHandlerService, InitializingBean {


	private static final Logger logger = LoggerFactory.getLogger(StdProductServiceImpl.class);

	private static final String method = "std.universal.pro";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	OnsMQTemplate productSyncTemplate;

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
			List<StdProductDto> stdProductDtos = null;
			if (StringUtils.isBlank(bodyStr) || CollectionUtils.isEmpty(stdProductDtos = JSONObject.parseArray(bodyStr, StdProductDto.class))) {
				stdResult = StdResult.fail("请求参数为空");
				return stdResult;
			}
			for (StdProductDto stdProductDto : stdProductDtos) {
				if ((stdResult = stdProductDto.check()).fail()) {
					return stdResult;
				}
			}
//			productSyncTemplate.syncSend(convert(stdProductDtos),"erpProdSync");

			productSyncTemplate.syncSend(convert(stdProductDtos), RocketMqConstant.ERP_PRODUCT_TAG);

		} catch (Exception e) {
			logger.error(requestId + "-中台商品基础信息同步接口调用处理异常", e.getMessage());
			stdResult = StdResult.fail("处理失败");
		} finally {
			logger.info("中中台商品基础信息同步接口-{}-query请求参数:{},json请求参数:{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, stdResult, System.currentTimeMillis() - start);
		}
		return stdResult;
	}

	public static void main(String[] s){
//		logger.info(getDiscount("124L","7.00"));
	}

	private String getDiscount(String spuCode,String discount){
		logger.info("中台商品基础信息同步1 商品货号【{}】 渠道折扣等级【{}】",spuCode,discount);
		if(StrUtil.isBlank(discount)){
			return "0";
		}
		if (!PrincipalUtil.isMaximumOfTwoDecimal(discount) && !discount.equals("0")) {
			discount="0";
			logger.error("最多是保留2位小数的数值");
		}
		if(NumberUtil.parseDouble(discount)<0 || NumberUtil.parseDouble(discount)<0.99){
			discount="0";
			logger.error("小于0，默认为0");
		}
		logger.info("中台商品基础信息同步2 商品货号【{}】 渠道折扣等级【{}】",spuCode,discount);
		return discount;
	}

	private ErpSyncDTO convert(List<StdProductDto> data) {
		//赋值商品编码
		data.forEach(stdProductDto -> {
			String name = stdProductDto.getName();
//			String spuCode = name.substring(0, name.indexOf("-"));
			String spuCode=name.split("-").length>1?name.substring(0, name.indexOf("-")):name;
			stdProductDto.setSpuCode(spuCode);
		});
		//进行商品同步
		Map<String, List<StdProductDto>> spuMap = data.stream().collect(Collectors.groupingBy(StdProductDto::getSpuCode));
		//只进行商品主数据同步
		ArrayList<SpuErpSyncDTO> spuDTOList = new ArrayList<>();
		spuMap.forEach((spuCode, skuList) -> {
			SpuErpSyncDTO spuDTO = new SpuErpSyncDTO();
			spuDTO.setName(spuCode);
			spuDTO.setSpuCode(spuCode);
			spuDTO.setStatus(0);
			StdProductDto stdProductDto = skuList.stream().min(Comparator.comparingInt(StdProductDto::getPricelist)).get();
			Long minPrice = Long.valueOf(stdProductDto.getPricelist() * 100);
			spuDTO.setMarketPriceFee(minPrice);
			spuDTO.setPriceFee(minPrice);
			spuDTO.setStyleCode(stdProductDto.getStyleType());
			spuDTO.setSex(stdProductDto.getSex());
			//商品渠道(R线下渠道 T电商渠道 L清货渠道)
			spuDTO.setChannelName(stdProductDto.getChannelName());
			//校验折扣等级数据规范
			spuDTO.setDiscount(getDiscount(spuCode,stdProductDto.getDiscount()));

			List<SkuErpSyncDTO> skuErpSyncDTOList = skuList.stream().map(stdProductDto1 -> {
				SkuErpSyncDTO skuErpSyncDTO = new SkuErpSyncDTO();
				BeanUtils.copyProperties(stdProductDto1, skuErpSyncDTO);
				skuErpSyncDTO.setColorName(stdProductDto1.getColorName());
				skuErpSyncDTO.setSizeName(stdProductDto1.getSizeName());
				//价格为0 默认999
				if(stdProductDto1.getPricelist()<=0){
					stdProductDto1.setPricelist(999);
				}
				skuErpSyncDTO.setPriceFee(stdProductDto1.getPricelist().longValue() * 100);
				skuErpSyncDTO.setStatus(Objects.equals("Y", stdProductDto1.getIsactive()) ? 1 : 0);
				skuErpSyncDTO.setColorCode(stdProductDto1.getColorCode());
				skuErpSyncDTO.setMarketPriceFee(stdProductDto1.getPricelist().longValue() * 100);
				skuErpSyncDTO.setSkuCode(stdProductDto1.getNo());
				skuErpSyncDTO.setIntsCode(stdProductDto1.getIntscode());
				skuErpSyncDTO.setForcode(stdProductDto1.getForcode());
				skuErpSyncDTO.setPriceCode(stdProductDto1.getName());
				skuErpSyncDTO.setStyleCode(stdProductDto1.getStyleType());
				skuErpSyncDTO.setSex(stdProductDto1.getSex());
				//商品渠道(R线下渠道 T电商渠道 L清货渠道)
				skuErpSyncDTO.setChannelName(stdProductDto1.getChannelName());
				//校验折扣等级数据规范
				skuErpSyncDTO.setDiscount(getDiscount(stdProductDto1.getNo(),stdProductDto1.getDiscount()));
				return skuErpSyncDTO;
			}).collect(Collectors.toList());
			spuDTO.setSkuList(skuErpSyncDTOList);
			spuDTOList.add(spuDTO);
		});
		ErpSyncDTO erpSyncDTO = new ErpSyncDTO();
		erpSyncDTO.setDtoList(spuDTOList);
		return erpSyncDTO;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		register(method, this);
	}
}
