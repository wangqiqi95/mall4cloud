package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.product.bo.SkuWithStockBO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuStore;
import com.mall4j.cloud.product.vo.BatchSkuStorePriceCodeVO;
import com.mall4j.cloud.product.vo.BatchSkuStoreVO;
import com.mall4j.cloud.product.vo.SkuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkuStoreMapper extends BaseMapper<SkuStore> {
    void deleteBySpuIdAndStoreId(@Param("spuId") Long spuId, @Param("storeId") long storeId);

    void deleteBySpuIdAndSkuId(@Param("skuIds") List<Long> skuIds);

    void updateSkuStoreBatchByPriceCode(@Param("skus") List<Sku> skus);

    void updateSkuBatch(@Param("skus") List<Sku> skus, @Param("storeId") long storeId);

    List<SkuStore> getSkuStoresByStoreId(@Param("storeId") long storeId);

    /**
     * 价签同步商品、商品sku
     * @param storeId
     * @return
     */
    List<SkuStore> getElSkuStoresByStoreId(@Param("storeId") long storeId);

    void addStockByOrder(@Param("skuWithStocks") List<SkuWithStockBO> allSkuWithStocks,@Param("storeId") Long storeId);

    List<SkuStore> getChannelPirceSkuStores(@Param("spuId") Long spuId,
                                            @Param("priceCode") String priceCode);

    List<SkuStore> getCancelChannelPriceSkuStores(@Param("channelName") String channelName);

    List<SkuVo> getPriceFeeErrorSkus(@Param("discount") double discount,@Param("storeId") Long storeId,@Param("pageAdapter") PageAdapter pageAdapter);

    List<BatchSkuStoreVO> batchSkuCodeStatus(@Param("priceCode") String priceCode);

    List<BatchSkuStorePriceCodeVO> batchSkuPriceCode();

    void batchSkuPriceCodeTempStatus(@Param("priceCode") String priceCode);
}
