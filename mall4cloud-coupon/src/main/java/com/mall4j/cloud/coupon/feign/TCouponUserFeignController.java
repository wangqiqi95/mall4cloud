package com.mall4j.cloud.coupon.feign;

import com.mall4j.cloud.api.coupon.dto.SyncPointConvertCouponDto;
import com.mall4j.cloud.api.coupon.feign.TCouponUserFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 用户优惠券
 * @Author axin
 * @Date 2023-02-10 14:44
 **/
@RestController
@Slf4j
public class TCouponUserFeignController implements TCouponUserFeignClient {
    @Resource
    private TCouponUserService tCouponUserService;

    @Override
    public ServerResponseEntity<List<SyncPointConvertCouponDto>> getSyncPointConvertCouponByBatchIds(@RequestBody List<Long> batchIds) {
        return ServerResponseEntity.success(tCouponUserService.getSyncPointConvertCouponByBatchIds(batchIds));
    }
}
