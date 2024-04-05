package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionMomentsProduct;

import java.util.List;

/**
 * 分销推广-朋友圈商品
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionMomentsProductService {

    /**
     * 分页获取分销推广-朋友圈商品列表
     *
     * @param pageDTO 分页参数
     * @return 分销推广-朋友圈商品列表分页数据
     */
    PageVO<DistributionMomentsProduct> page(PageDTO pageDTO);

    /**
     * 根据分销推广-朋友圈商品id获取分销推广-朋友圈商品
     *
     * @param id 分销推广-朋友圈商品id
     * @return 分销推广-朋友圈商品
     */
    DistributionMomentsProduct getById(Long id);

    /**
     * 保存分销推广-朋友圈商品
     *
     * @param distributionMomentsProduct 分销推广-朋友圈商品
     */
    void save(DistributionMomentsProduct distributionMomentsProduct);

    /**
     * 更新分销推广-朋友圈商品
     *
     * @param distributionMomentsProduct 分销推广-朋友圈商品
     */
    void update(DistributionMomentsProduct distributionMomentsProduct);

    /**
     * 根据分销推广-朋友圈商品id删除分销推广-朋友圈商品
     *
     * @param id 分销推广-朋友圈商品id
     */
    void deleteById(Long id);

    List<DistributionMomentsProduct> listByMomentsId(Long momentsId);

    List<DistributionMomentsProduct> listByMomentsIdList(List<Long> momentsIdList);

    void deleteByMomentsIdNotInProductIds(Long momentsId, List<Long> productIds);

    void deleteByMomentsId(Long momentsId);
}
