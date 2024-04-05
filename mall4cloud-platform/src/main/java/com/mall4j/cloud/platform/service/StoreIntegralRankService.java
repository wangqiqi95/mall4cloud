package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.api.platform.vo.StoreIntegralRankVO;

/**
 * @Description 门店积分抵现榜单
 * @Author axin
 * @Date 2023-02-16 14:42
 **/
public interface StoreIntegralRankService {
    /**
     * 获取门店积分抵现榜单
     * @return
     */
    StoreIntegralRankVO getStoreIntegralRank();
}
