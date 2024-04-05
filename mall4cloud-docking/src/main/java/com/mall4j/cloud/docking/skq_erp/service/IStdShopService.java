package com.mall4j.cloud.docking.skq_erp.service;

import com.mall4j.cloud.api.docking.skq_erp.dto.*;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopSpuStockVO;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface IStdShopService {

	/**
	 * 方法描述：门店档案
	 *
	 * @param getShopInfoDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult < com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo>>
	 * @date 2022-01-09 11:07:38
	 */
	ServerResponseEntity<StdPageResult<ShopInfoVo>> getShopInfo(GetShopInfoDto getShopInfoDto);

	/**
	 * 推送门店
	 * @param pushStoreDtos
	 * @return
	 */
	ServerResponseEntity<String> pushShops(List<PushStoreDto> pushStoreDtos);

	ServerResponseEntity<List<ShopSpuStockVO>> getStoresByShops(GetStoresByShopsDto storesByShopsDtoList);

	/**
	 * 积分抵现排行
	 * @param reqDto
	 * @return
	 */
    ServerResponseEntity<List<StoreIntegralRankRespDto>> queryStoreIntegralRank(StoreIntegralRankReqDto reqDto);
}
