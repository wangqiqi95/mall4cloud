package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.model.DistributionRecommendActivity;
import com.mall4j.cloud.distribution.model.DistributionRecommendSpu;
import com.mall4j.cloud.distribution.vo.DistributionRecommendActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 主推商品活动端Mybatis-plus映射
 *
 * @author EricJeppesen
 * @date 2022-10-18 14:44:00
 */
public interface DistributionRecommendActivityMapper {


    /**
     * 创建推广产品活动
     *
     * @param distributionRecommendActivity 推广产品活动
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    Integer create(DistributionRecommendActivity distributionRecommendActivity);

    /**
     * 创建推广产品活动下属的SPU
     *
     * @param activityId 活动主体
     * @param spuIds     商品SPU标识
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    Integer createSpuRelates(@Param("activityId") Long activityId, @Param("spuIds") List<Long> spuIds);

    /**
     * 创建推广产品活动关联的店铺
     *
     * @param activityId 活动主体标识
     * @param shopIds    门店标识
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    Integer createShopRelates(@Param("activityId") Long activityId, @Param("shopIds") List<Long> shopIds);

    /**
     * 修改推广产品活动中
     * 参与商品SPU的数量
     * 参与门店的数量
     *
     * @param activityId        活动主体标识
     * @param updateSpuAmount   是否更新参与活动SPU数量
     * @param updateStoreAmount 是否更新参与活动门店
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    Integer modifyCache(@Param("activityId") Long activityId, @Param("updateStoreAmount") Integer updateStoreAmount, @Param("updateSpuAmount") Integer updateSpuAmount);

    /**
     * 根据标识获取活动主体数据
     *
     * @param id 活动主体标识
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    DistributionRecommendActivity getById(@Param("id") Long id);

    /**
     * 根据活动主体标识删除作用域下的门店
     *
     * @param activityId 活动主体标识
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    Integer deleteShopRelatesByActivityId(@Param("activityId") Long activityId);

    /**
     * 根据活动主体标识删除作用于下的SPU商品
     *
     * @param activityId 活动主体标识
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    Integer deleteSpuRelatesByActivityId(@Param("activityId") Long activityId);

    /**
     * 修改推广产品活动
     *
     * @param distributionRecommendActivity 推广产品活动
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    Integer modify(DistributionRecommendActivity distributionRecommendActivity);

    /**
     * 查询活动{@link DistributionRecommendActivityQueryDTO} activityCondition查询数据库中匹配数据
     *
     * @param activityCondition 查询查询
     * @return 查询结果
     */
    List<DistributionRecommendActivityVO> selectActivity(DistributionRecommendActivityQueryDTO activityCondition);


    /**
     * 根据主推商品活动标识查询作用域下的门店
     *
     * @param activityId 活动标识
     * @return 作用域下的门店标识
     */
    List<Long> listActivityShopRelates(@Param("activityId") Long activityId);

    /**
     * 根据主推商品活动主体标识查询作用域下的商品SPU
     *
     * @param activityId 主推商品活动标识
     * @return 作用域下的Spu标识
     */
    List<Long> listActivitySpuRelates(@Param("activityId") Long activityId);

    /**
     * 修改活动状态
     *
     * @param id             主推商品活动标识
     * @param activityStatus 状态（1上线、0下线）
     * @return 数据库影响行数
     */
    Integer modifyActivityStatus(@Param("id") Long id, @Param("activityStatus") Integer activityStatus);

    /**
     * 获取正在进行的活动是否有全部商品参与
     *
     * @param distributionRecommendSpuQueryDTO 查询数据
     * @return 数量
     */
    Long getNumberOfAllSpuRunningActivity(@Param("queryDTO") DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO);

    /**
     * 获取正在进行的活动的SPU
     *
     * @param distributionRecommendSpuQueryDTO 查询数据
     * @return SPUId
     */
    List<Long> listActivatingSpu(@Param("queryDTO") DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO);
}
