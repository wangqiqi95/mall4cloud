package com.mall4j.cloud.api.coupon.feign;

import com.mall4j.cloud.api.coupon.dto.SyncPointConvertCouponDto;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description 用户优惠券
 * @Author axin
 * @Date 2023-02-10 14:28
 **/
@FeignClient(value = "mall4cloud-coupon",contextId ="tCouponUser")
public interface TCouponUserFeignClient {

    /**
     * 根据批次号获取要同步用户优惠券信息
     * @param batchIds
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/getSyncPointConvertCouponByBatchId")
    ServerResponseEntity<List<SyncPointConvertCouponDto>> getSyncPointConvertCouponByBatchIds(@RequestBody List<Long> batchIds);
}
