package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.product.model.SpuDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品详情信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SpuDetailMapper extends BaseMapper<SpuDetail> {

    /**
     * 保存商品详情信息
     *
     * @param spuDetail 商品详情信息
     */
    void save(@Param("spuDetail") SpuDetail spuDetail);

    /**
     * 批量更新商品详情信息
     *
     * @param spuDetailList 商品详情信息
     */
    void batchUpdate(@Param("spuDetailList") List<SpuDetail> spuDetailList);

    /**
     * 根据商品详情信息id删除商品详情信息
     *
     * @param spuId
     */
    void deleteById(@Param("spuId") Long spuId);

    /**
     * 批量保存
     *
     * @param spuDetailList
     */
    void batchSave(@Param("spuDetailList") List<SpuDetail> spuDetailList);

    /**
     * 批量删除
     *
     * @param spuId
     * @param langList
     */
    void deleteBatchBySpuIdAndLang(@Param("spuId") Long spuId, @Param("langList") List<Integer> langList);
}
