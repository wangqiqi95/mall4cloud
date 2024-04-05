package com.mall4j.cloud.docking.service;

import com.mall4j.cloud.api.docking.dto.zhls.product.BaseProductRespDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.CategoriesReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.SaveSkuDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.BaseRecommendDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetRespDto;

/**
 * @Description 有数数据上报及推荐商品
 * @Author axin
 * @Date 2023-03-09 14:20
 **/
public interface IZhlsService {

    /**
     * 商品类目添加/变更
     */
    BaseProductRespDto<Void> productCategoriesAdd(CategoriesReqDto reqDto);


    /**
     * 商品信息添加
     */
    BaseProductRespDto<Void> skuInfoAdd(SaveSkuDto reqDto);

    /**
     * 商品信息修改
     */
    BaseProductRespDto<Void> skuInfoUpdate(SaveSkuDto reqDto);

    /**
     * 获取推荐商品
     */
    BaseRecommendDto<RecommendGetRespDto> recommendGet(RecommendGetReqDto reqDto);



}
