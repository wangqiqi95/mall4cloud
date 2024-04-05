package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardStore;

import java.util.List;

/**
 * 分销推广-发展奖励门店
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:38:24
 */
public interface DistributionDevelopingRewardStoreService {

    /**
     * 分页获取分销推广-发展奖励门店列表
     *
     * @param pageDTO 分页参数
     * @return 分销推广-发展奖励门店列表分页数据
     */
    PageVO<DistributionDevelopingRewardStore> page(PageDTO pageDTO);

    /**
     * 根据分销推广-发展奖励门店id获取分销推广-发展奖励门店
     *
     * @param id 分销推广-发展奖励门店id
     * @return 分销推广-发展奖励门店
     */
    DistributionDevelopingRewardStore getById(Long id);

    /**
     * 保存分销推广-发展奖励门店
     *
     * @param distributionDevelopingRewardStore 分销推广-发展奖励门店
     */
    void save(DistributionDevelopingRewardStore distributionDevelopingRewardStore);

    /**
     * 更新分销推广-发展奖励门店
     *
     * @param distributionDevelopingRewardStore 分销推广-发展奖励门店
     */
    void update(DistributionDevelopingRewardStore distributionDevelopingRewardStore);

    /**
     * 根据分销推广-发展奖励门店id删除分销推广-发展奖励门店
     *
     * @param id 分销推广-发展奖励门店id
     */
    void deleteById(Long id);

    List<DistributionDevelopingRewardStore> listByRewardId(Long rewardId);

    List<DistributionDevelopingRewardStore> listByStoreId(Long storeId);

    void deleteByRewardIdNotInStoreIds(Long rewardId, List<Long> storeIds);

    void deleteByRewardId(Long rewardId);
}
