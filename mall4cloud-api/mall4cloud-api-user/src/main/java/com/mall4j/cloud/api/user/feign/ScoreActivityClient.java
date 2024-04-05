package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.dto.UserTageAddDto;
import com.mall4j.cloud.api.user.vo.ScoreConvertVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 积分兑换活动
 * @author shijing
 */
@FeignClient(value = "mall4cloud-user",contextId = "score-activity")
public interface ScoreActivityClient {

    /**
     * 通过活动id获取适用门店
     * @param activityId 活动id
     * @return 门店id集合
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/getShops")
    ServerResponseEntity<List<Long>> getShops(@RequestParam("activityId") Long activityId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/check/coupon/convert")
    ServerResponseEntity<List<ScoreConvertVO>> checkScoreConvertByCoupon(@RequestParam("couponId") Long couponId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/get/convert")
    ServerResponseEntity<ScoreConvertVO> getConvert(@RequestParam("convertId") Long convertId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/get/coupon/ids/by/convert")
    ServerResponseEntity<List<Long>> getCouponIdListByConvertId(@RequestParam("convertId") Long convertId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/get/coupon/id/and/convert")
    ServerResponseEntity<ScoreConvertVO> getScoreConvertAndCouponList(@RequestParam("convertId") Long convertId);

}
