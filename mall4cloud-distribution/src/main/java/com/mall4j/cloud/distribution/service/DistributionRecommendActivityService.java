package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.model.DistributionRecommendActivity;
import com.mall4j.cloud.distribution.vo.DistributionRecommendActivityDetailVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendActivityVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuListVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuVO;

import java.util.List;

/**
 * 主推商品活动服务接口
 *
 * @author EricJeppesen
 * @date 2022/10/18 14:54
 */
public interface DistributionRecommendActivityService {

    /**
     * 创建主推商品活动
     *
     * @param recommendActivityDTO 主推商品活动
     * @param creatorId            创建人（标识）
     * @param creatorName          创建人（姓名）
     * @return {@link java.lang.Long} 返回主键标识
     */
    Long createActivity(DistributionRecommendActivityDTO recommendActivityDTO, Long creatorId, String creatorName);

    /**
     * 修改主推商品活动
     *
     * @param recommendActivityDTO 主推商品活动修改内容
     * @param modifierId           修改人（标识）
     * @param modifierName         修改人（姓名）
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    Integer modifyActivity(DistributionRecommendActivityDTO recommendActivityDTO, Long modifierId, String modifierName);

    /**
     * 查询主推商品活动
     *
     * @param pageDTO                               分页基础条件
     * @param distributionRecommendActivityQueryDTO 查询条件
     * @return 列表展示类
     */
    PageVO<DistributionRecommendActivityVO> listActivity(PageDTO pageDTO, DistributionRecommendActivityQueryDTO distributionRecommendActivityQueryDTO);

    /**
     * 根据活动标识查询主推商品活动
     *
     * @param id 活动标识
     * @return 数据库受影响行数
     */
    DistributionRecommendActivityDetailVO getById(Long id);

    /**
     * 根据活动标识变更活动
     *
     * @param id                  主推商品活动标识
     * @param enableDisableStatus 修改状态(只能为1开启,0关闭)
     * @return 数据库受影响行数
     */
    Integer modifyActivityStatus(Long id, Integer enableDisableStatus);

    /**
     * 分页查询出商品Spu数据
     *
     * @param pageDTO                          分页数据
     * @param distributionRecommendSpuQueryDTO 查询条件
     * @return 查询结果
     */
    PageVO<DistributionRecommendSpuListVO> listGoodsSpuForShoppingGuideAndWeike(PageDTO pageDTO, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO);

}
