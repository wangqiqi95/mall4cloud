package com.mall4j.cloud.api.discount.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.ShopCartFlagVO;
import com.mall4j.cloud.common.order.vo.ShopCartVO;
import com.mall4j.cloud.common.order.vo.ShopCartWithAmountVO;
import com.mall4j.cloud.common.product.vo.SpuDiscountAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/14
 */
@FeignClient(value = "mall4cloud-discount",contextId ="discount")
public interface DiscountFeignClient {

	/**
	 * 计算满减，并重新组合购物车
	 * @param shopCarts 购物项
	 * @return void
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/calculateDiscountAndMakeUpShopCart")
	ServerResponseEntity<List<ShopCartVO>> calculateDiscountAndMakeUpShopCart(@RequestBody List<ShopCartVO> shopCarts);

    /**
     * 计算满减，并重新组合购物车
     * @param storeId 门店id
     * @param shopCarts 购物项
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/calculateDiscountAndMakeUpShopCartByStoreId")
    ServerResponseEntity<List<ShopCartVO>> calculateDiscountAndMakeUpShopCartByStoreId(@RequestParam("storeId") Long storeId, @RequestBody ShopCartFlagVO shopCartFlagVO);


	/**
	 * 计算满减，并组合购物车 + 购物车金额
	 * @param shopCartWithAmountVO 购物车列表和运费信息
     * @return void
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/discount/calculateDiscountAndMakeUpShopCartAndAmount")
	ServerResponseEntity<ShopCartWithAmountVO> calculateDiscountAndMakeUpShopCartAndAmount(@RequestBody ShopCartWithAmountVO shopCartWithAmountVO);

	/**
	 * 商品详情的满减活动列表
	 * @param shopId
	 * @param spuId
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/spuDiscountList")
	ServerResponseEntity<List<SpuDiscountAppVO>> spuDiscountList(@RequestParam("shopId") Long shopId, @RequestParam("spuId") Long spuId);

	/**
	 * 处理商品下线
	 *
	 * @param spuIds 商品id列表
	 * @param shopIds 店铺id
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/handleSpuOffline")
	ServerResponseEntity<Void> handleSpuOffline(@RequestParam("spuIds") List<Long> spuIds, @RequestParam("shopIds") List<Long> shopIds);

	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/hystrixCommandTest")
	ServerResponseEntity<Void> hystrixCommandTest();
}
