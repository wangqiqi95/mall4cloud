package com.mall4j.cloud.product.service;

import com.mall4j.cloud.api.product.dto.ErpSkuStockDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.product.dto.ZhlsCommodityReqDto;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.vo.ZhlsCommodityVO;

import java.util.List;

/**
 * @Description 有数商品信息上报
 * @Author axin
 * @Date 2023-05-05 14:27
 **/
public interface ZhlsCommodityService {

    /**
     * 类目添加or变更
     * @param categories
     */
    void productCategoriesAdd(List<Category> categories,Integer isDeleted);

    /**
     * 添加商品
     * @param skuIds
     */
    void skuInfoAdd(List<Long> skuIds);

    /**
     * 商品上下架
     * @param skuIds
     * @param status 0 下架 1上架
     */
    void skuInfoUpdateStatus(List<Long> skuIds,Integer status);

    /**
     * 更新库存
     * @param reqDtos
     */
    void skuInfoUpdateStock(List<ErpSkuStockDTO> reqDtos);

    /**
     * 更新价格
     * @param skuIds
     */
    void skuInfoUpdatePrice(List<Long> skuIds);


    /**
     * 商品推荐
     * @return
     */
    ZhlsCommodityVO zhlsRecommendList(Long storeId, ZhlsCommodityReqDto reqDto);
}
