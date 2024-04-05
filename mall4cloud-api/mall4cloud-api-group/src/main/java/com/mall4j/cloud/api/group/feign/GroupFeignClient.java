package com.mall4j.cloud.api.group.feign;

import com.mall4j.cloud.api.group.feign.dto.*;
import com.mall4j.cloud.api.group.feign.vo.OrderGiftInfoAppVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.product.vo.GroupActivitySpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * @author YXF
 * @date 2021/3/29
 */
@FeignClient(value = "mall4cloud-group",contextId ="group")
public interface GroupFeignClient {

	/**
	 * 获取活动开始的时间（预热的活动开始时间为当前）
	 * @param activityId 活动id
	 * @return void
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/getActivityStartTime")
	ServerResponseEntity<Date> getActivityStartTime(@RequestParam("activityId") Long activityId);


	/**
	 * 根据商品id获取团购商品信息
	 * @param spuIds 商品id
	 * @return void
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/groupSpuListBySpuIds")
	ServerResponseEntity<List<GroupActivitySpuVO>> groupSpuListBySpuIds(@RequestParam("spuIds") List<Long> spuIds);

	/**
	 * 根据商品ids下线所有的团购活动
	 * @param spuIds 商品ids
	 * @return 返回结果
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/offlineGroupBySpuIds")
	ServerResponseEntity<Void> offlineGroupBySpuIds(@RequestBody List<Long> spuIds);

	@PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/UserPerfectDataActivity")
	ServerResponseEntity<Void> userPerfectDataActivity(@RequestBody UserPerfectDataActivityDTO params);

	@PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/UserFollowWechatActivity")
	ServerResponseEntity<Void> userFollowWechatActivity(@RequestBody UserFollowWechatActivityDTO params);

	@PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/giftInfo")
	ServerResponseEntity<List<OrderGiftInfoAppVO>> giftInfo(@RequestBody List<OrderGiftInfoAppDTO> param);

	@PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/reduceStock")
	ServerResponseEntity<Void> reduceStock(@RequestBody List<OrderGiftReduceAppDTO> param);

	@PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/unLockStock")
	ServerResponseEntity<Void> unLockStock(@RequestBody List<OrderGiftReduceAppDTO> param);

	@PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/gift/orderGiftInfo")
	ServerResponseEntity<ShopCartOrderMergerVO> orderGiftInfo(@RequestBody ShopCartOrderMergerVO shopCartOrderMerger);
//	/**
//	 * 计算满减，并组合购物车 + 购物车金额
//	 * @param shopCartItems 购物项
//	 * @return void
//	 */
//	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/discount/calculateDiscountAndMakeUpShopCartAndAmount")
//	ServerResponseEntity<ShopCartWithAmountVO> calculateDiscountAndMakeUpShopCartAndAmount(@RequestBody List<ShopCartItemVO> shopCartItems);
//
//	/**
//	 * 商品详情的满减活动列表
//	 * @param spuId
//	 * @return
//	 */
//	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/spuDiscountList")
//	ServerResponseEntity<List<SpuDiscountAppVO>> spuDiscountList(@RequestParam("shopId") Long shopId, @RequestParam("spuId") Long spuId);
}
