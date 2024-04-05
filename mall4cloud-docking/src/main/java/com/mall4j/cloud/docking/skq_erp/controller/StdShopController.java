package com.mall4j.cloud.docking.skq_erp.controller;

import com.mall4j.cloud.api.docking.skq_erp.dto.*;
import com.mall4j.cloud.api.docking.skq_erp.feign.StdShopFeignClient;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopSpuStockVO;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdShopService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：门店档案
 *
 * @date 2022/1/9 11:08：34
 */
@RestController
@Api(tags = "中台-门店档案")
public class StdShopController implements StdShopFeignClient {

	@Autowired
	IStdShopService stdShopService;

	/**
	 * 方法描述：门店档案
	 *
	 * @param getShopInfoDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult < com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo>>
	 * @date 2022-01-09 11:07:38
	 */
	@Override
	public ServerResponseEntity<StdPageResult<ShopInfoVo>> getShopInfo(GetShopInfoDto getShopInfoDto) {
		return stdShopService.getShopInfo(getShopInfoDto);
	}

	/**
	 * 售卖店铺仓库推送
	 * @param pushStoreDtos
	 * @return
	 */
	@Override
	public ServerResponseEntity<String> pushShops(List<PushStoreDto> pushStoreDtos) {
		return stdShopService.pushShops(pushStoreDtos);
	}

	@Override
	public ServerResponseEntity<List<ShopSpuStockVO>> getStoresByShops(GetStoresByShopsDto storesByShopsDtoList) {
		return stdShopService.getStoresByShops(storesByShopsDtoList);
	}

	@Override
	public ServerResponseEntity<List<StoreIntegralRankRespDto>> queryStoreIntegralRank(StoreIntegralRankReqDto reqDto) {
		return stdShopService.queryStoreIntegralRank(reqDto);
	}
}
