package com.mall4j.cloud.api.seckill.feign;

import com.mall4j.cloud.api.seckill.vo.SeckillApiVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.product.vo.SekillActivitySpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author YXF
 * @date 2021/3/29
 */
@FeignClient(value = "mall4cloud-seckill",contextId ="seckill")
public interface SeckillFeignClient {

//	/**
//	 * 获取活动开始的时间（预热的活动开始时间为当前）
//	 * @param activityId 活动id
//	 * @return void
//	 */
//	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/getActivityStartTime")
//	ServerResponseEntity<Date> getActivityStartTime(@RequestParam("spuId") Long activityId);



	/**
	 * 根据商品id获取秒杀商品信息
	 * @param selectedLot 所选批次
	 * @param spuIds 商品ids
	 * @return 秒杀商品信息
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/seckill/seckillSpuListBySpuIds")
	ServerResponseEntity<List<SekillActivitySpuVO>> seckillSpuListBySpuIds(@RequestParam(value = "selectedLot",defaultValue = "")Integer selectedLot,@RequestParam("spuIds") List<Long> spuIds);

	/**
	 * 根据活动d获取秒杀商品信息
	 * @param activityId 活动id
	 * @return 秒杀商品信息
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/seckill/getSeckillInfoById")
    ServerResponseEntity<SeckillApiVO> getSeckillInfoById(@RequestParam("activityId") Long activityId);

	/**
	 * 根据商品ids下线所有的秒杀活动
	 * @param spuIds 商品ids
	 * @return 返回结果
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/seckill/offlineSeckillBySpuIds")
    ServerResponseEntity<Void> offlineSeckillBySpuIds(@RequestBody List<Long> spuIds);
	/**
	 * 根据店铺id下线所有的秒杀活动
	 * @param shopId 店铺id
	 * @return 返回结果
	 */
	@PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/seckill/offlineSeckillByShopId")
    ServerResponseEntity<Void> offlineSeckillByShopId(@RequestBody Long shopId);
}
