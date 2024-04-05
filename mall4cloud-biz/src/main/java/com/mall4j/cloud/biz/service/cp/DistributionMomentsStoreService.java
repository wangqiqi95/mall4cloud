package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.DistributionMomentsStore;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 分销推广-朋友圈门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionMomentsStoreService {

    /**
     * 分页获取分销推广-朋友圈门店列表
     *
     * @param pageDTO 分页参数
     * @return 分销推广-朋友圈门店列表分页数据
     */
    PageVO<DistributionMomentsStore> page(PageDTO pageDTO);

    /**
     * 根据分销推广-朋友圈门店id获取分销推广-朋友圈门店
     *
     * @param id 分销推广-朋友圈门店id
     * @return 分销推广-朋友圈门店
     */
    DistributionMomentsStore getById(Long id);

    /**
     * 保存分销推广-朋友圈门店
     *
     * @param distributionMomentsStore 分销推广-朋友圈门店
     */
    void save(DistributionMomentsStore distributionMomentsStore);

    /**
     * 更新分销推广-朋友圈门店
     *
     * @param distributionMomentsStore 分销推广-朋友圈门店
     */
    void update(DistributionMomentsStore distributionMomentsStore);

    /**
     * 根据分销推广-朋友圈门店id删除分销推广-朋友圈门店
     *
     * @param id 分销推广-朋友圈门店id
     */
    void deleteById(Long id);

    List<DistributionMomentsStore> listByMomentsId(Long momentsId);

    List<DistributionMomentsStore> listByStoreId(Long storeId);

    int countByMomentsId(Long momentsId);

    void deleteByMomentsIdNotInStoreIds(Long momentsId, List<Long> storeIds);

    void deleteByMomentsId(Long momentsId);

}
