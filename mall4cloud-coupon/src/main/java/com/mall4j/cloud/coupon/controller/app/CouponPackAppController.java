package com.mall4j.cloud.coupon.controller.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.dto.CouponPackDrawDTO;
import com.mall4j.cloud.coupon.service.CouponPackService;
import com.mall4j.cloud.coupon.vo.CouponPackInfoVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/coupon_pack")
@Api(tags = "app-优惠券包活动")
public class CouponPackAppController {

    @Resource
    private CouponPackService couponPackService;


    @GetMapping("/info/{activityId}")
    public ServerResponseEntity<CouponPackInfoVO> info(@PathVariable Long activityId){
        return couponPackService.info(activityId);
    }

    @PostMapping("/draw")
    public ServerResponseEntity<Void> draw(@RequestBody CouponPackDrawDTO param){
        Long userId = AuthUserContext.get().getUserId();
        param.setUserId(userId);

        return couponPackService.draw(param);
    }
}
