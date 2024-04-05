package com.mall4j.cloud.api.coupon.feign;

import com.mall4j.cloud.api.coupon.dto.*;
import com.mall4j.cloud.api.coupon.vo.*;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 优惠券
 */
@FeignClient(value = "mall4cloud-coupon",contextId ="tCouponActivityCentre")
public interface TCouponActivityCentreFeignClient {


    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/tCouponActivityCentre/saveTo")
    ServerResponseEntity<Void> saveTo(@RequestBody TCouponActivityCentreAddDTO addDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/tCouponActivityCentre/updateTo")
    ServerResponseEntity<Void> updateTo(@RequestBody TCouponActivityCentreAddDTO addDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponActivityCentre/couponACList")
    ServerResponseEntity<List<CouponListVO>> couponACList(@RequestBody TCouponActivityCentreParamDTO paramDTO);

}
