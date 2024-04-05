package com.mall4j.cloud.product.service;

import com.mall4j.cloud.api.product.dto.ErpPriceDTO;
import com.mall4j.cloud.api.product.dto.ErpSkuPriceDTO;
import com.mall4j.cloud.api.product.dto.ErpSkuStockDTO;

import java.util.Date;
import java.util.List;

/**
 * @Date 2022年4月11日, 0011 10:14
 * @Created by eury
 * 处理erp价格同步
 */
public interface SpuPriceService {

    /**
     * 保存门店库存
     * @param skuPriceDTOList
     */
    void skuStoreSync(List<ErpSkuPriceDTO> skuPriceDTOList,Date now);

    /**
     * 市场价（吊牌价）
     */
    void marketPriceSync(List<ErpSkuPriceDTO> skuPriceDTOList,Date now);

    /**
     * 活动价
     */
    void activityPriceSync(List<ErpSkuPriceDTO> skuPriceDTOList,Date now);

    /**
     * 保护价
     */
    void protectPriceSync(List<ErpSkuPriceDTO> skuPriceDTOList,Date now);

    void savePirceLog(List<ErpSkuPriceDTO> skuPriceDTOList,Date now);

    void saveStockLog(List<ErpSkuStockDTO> skuStockDTOS, Date now);
}
