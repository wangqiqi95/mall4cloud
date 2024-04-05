package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.SkuAddrVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuSkuAttrValueVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuStore;
import com.mall4j.cloud.product.vo.SpuExcelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SkuStoreService extends IService<SkuStore> {

    void deleteBySpuIdAndStoreId(Long spuId, long mainShop);

    void deleteBySpuIdAndSkuId(@Param("skuIds") List<Long> skuIds);

    List<SkuStore> getSkuStoresByStoreId(long storeId);

    List<SkuStore> getElSkuStoresByStoreId(long storeId);

    void openSkuStore(List<String> openSkuStores);

}
