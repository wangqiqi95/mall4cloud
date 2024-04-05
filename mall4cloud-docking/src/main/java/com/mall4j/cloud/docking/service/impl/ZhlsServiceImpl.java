package com.mall4j.cloud.docking.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall4j.cloud.api.docking.dto.zhls.product.BaseProductRespDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.CategoriesReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.SaveSkuDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.BaseRecommendDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetRespDto;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.docking.service.IZhlsService;
import com.mall4j.cloud.docking.utils.ZhlsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 有数商品推荐
 * @Author axin
 * @Date 2023-03-09 16:07
 **/
@Service
public class ZhlsServiceImpl implements IZhlsService {

    private final static String REDIS_ZHLS_DATA_SOURCE_KEY="mall4cloud_docking:zhls_data_source_id";

    @Override
    public BaseProductRespDto<Void> productCategoriesAdd(CategoriesReqDto reqDto) {
        reqDto.setDataSourceId(getDataSourceId());
        String post = ZhlsUtil.clients().post(ZhlsUtil.HOST+ZhlsUtil.PRODUCT_CATEGORIES_URI, JSON.toJSONString(reqDto));
        return JSON.parseObject(post,new TypeReference<BaseProductRespDto<Void>>(){});
    }

    @Override
    public BaseProductRespDto<Void> skuInfoAdd(SaveSkuDto reqDto) {
        reqDto.setDataSourceId(getDataSourceId());
        String post = ZhlsUtil.clients().post(ZhlsUtil.HOST+ZhlsUtil.ADD_SKU_INFO_URI, JSON.toJSONString(reqDto));
        return JSON.parseObject(post,new TypeReference<BaseProductRespDto<Void>>(){});
    }

    @Override
    public BaseProductRespDto<Void> skuInfoUpdate(SaveSkuDto reqDto) {
        reqDto.setDataSourceId(getDataSourceId());
        String post = ZhlsUtil.clients().post(ZhlsUtil.HOST+ZhlsUtil.UPDATE_SKU_INFO_URI, JSON.toJSONString(reqDto));
        return JSON.parseObject(post,new TypeReference<BaseProductRespDto<Void>>(){});
    }

    @Override
    public BaseRecommendDto<RecommendGetRespDto> recommendGet(RecommendGetReqDto reqDto) {
        String post = ZhlsUtil.clients().post(ZhlsUtil.RECOMMEND_HOST+ZhlsUtil.PRODUCT_RECOMMEND_URI, JSON.toJSONString(reqDto));
        BaseRecommendDto<RecommendGetRespDto> baseRecommendDto = JSON.parseObject(post, new TypeReference<BaseRecommendDto<RecommendGetRespDto>>(){});
        if(!baseRecommendDto.isSuccess()){
            throw new LuckException("获取推荐商品失败");
        }
        return baseRecommendDto;
    }


    private String getDataSourceId() {
        String dataSourceId=RedisUtil.get(REDIS_ZHLS_DATA_SOURCE_KEY);
        if(StringUtils.isBlank(dataSourceId)){
            dataSourceId=ZhlsUtil.clients().getSourceId();
            RedisUtil.set(REDIS_ZHLS_DATA_SOURCE_KEY,dataSourceId,60*60*24);
        }
        return dataSourceId;
    }
}
