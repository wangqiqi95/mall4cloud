package com.mall4j.cloud.api.docking.skq_erp.feign;

import com.mall4j.cloud.api.docking.skq_erp.dto.*;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopSpuStockVO;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 类描述：中台门店档案
 *
 * @date 2022/1/9 11:05：49
 */
@FeignClient(value = "mall4cloud-docking",contextId = "std-shop")
public interface StdShopFeignClient {

	/**
	 * 方法描述：门店档案
	 * @param getShopInfoDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult<com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo>>
	 * @date 2022-01-09 11:07:38
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/shop/getShopInfo")
	ServerResponseEntity<StdPageResult<ShopInfoVo>> getShopInfo(@RequestBody GetShopInfoDto getShopInfoDto);

	/**
	 * 售卖店铺仓库推送
	 * @param pushStoreDtos
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/shop/pushShops")
	ServerResponseEntity<String> pushShops(@RequestBody List<PushStoreDto> pushStoreDtos);

	/**
	 * 查询店铺对应的商品的库存
	 * */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/shops/products/store")
	ServerResponseEntity<List<ShopSpuStockVO>> getStoresByShops(@RequestBody GetStoresByShopsDto storesByShopsDtoList);

	/**
	 * 查询门店积分抵现排行榜
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/shop/queryStoreIntegralRank")
	ServerResponseEntity<List<StoreIntegralRankRespDto>> queryStoreIntegralRank(@RequestBody StoreIntegralRankReqDto reqDto);

}
