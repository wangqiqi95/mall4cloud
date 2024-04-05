package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionMomentsStore;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * 分销推广-朋友圈门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionMomentsStoreMapper {

    /**
     * 获取分销推广-朋友圈门店列表
     *
     * @return 分销推广-朋友圈门店列表
     */
    List<DistributionMomentsStore> list();

    /**
     * 根据分销推广-朋友圈门店id获取分销推广-朋友圈门店
     *
     * @param id 分销推广-朋友圈门店id
     * @return 分销推广-朋友圈门店
     */
    DistributionMomentsStore getById(@Param("id") Long id);

    /**
     * 保存分销推广-朋友圈门店
     *
     * @param distributionMomentsStore 分销推广-朋友圈门店
     */
    void save(@Param("distributionMomentsStore") DistributionMomentsStore distributionMomentsStore);

    /**
     * 更新分销推广-朋友圈门店
     *
     * @param distributionMomentsStore 分销推广-朋友圈门店
     */
    void update(@Param("distributionMomentsStore") DistributionMomentsStore distributionMomentsStore);

    /**
     * 根据分销推广-朋友圈门店id删除分销推广-朋友圈门店
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);

    List<DistributionMomentsStore> listByMomentsId(@Param("momentsId") Long momentsId);

    void deleteByMomentsIdNotInStoreIds(@Param("momentsId") Long momentsId, @Param("storeIds") List<Long> storeIds);

    void deleteByMomentsId(@Param("momentsId") Long momentsId);

    List<DistributionMomentsStore> listByStoreId(@Param("storeId") Long storeId);

    int countByMomentsId(@Param("momentsId") Long momentsId);
}
