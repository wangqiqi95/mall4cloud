package com.mall4j.cloud.product.service;

import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.product.dto.SpuSkuPriceDTO;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.product.dto.SkuPriceDTO;

import java.util.List;

/**
 * @Date 2022年4月11日, 0011 10:14
 * @Created by eury
 * 处理erp价格同步
 */
public interface SpuSkuPricingPriceService {

    List<SkuTimeDiscountActivityVO> getStoreSpuAndSkuPrice(Long storeId, List<SpuSkuPriceDTO> skuPriceDTOs);

}
