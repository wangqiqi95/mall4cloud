package com.mall4j.cloud.coupon.controller.platform;


import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.CouponUserDTO;
import com.mall4j.cloud.coupon.service.CouponUserService;
import com.mall4j.cloud.coupon.vo.CouponUserVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 优惠券
 *
 * @author cl
 * @date 2020-05-08 13:55:56
 */
@RestController("platformCouponController")
@RequestMapping("/p/coupon")
@Api(tags = "platform-优惠券")
public class CouponController {

    @Autowired
    private CouponUserService couponUserService;

    @GetMapping("/page_coupon_user")
    @ApiOperation(value = "获取某个用户的优惠券明细", notes = "获取某个用户的优惠券明细")
    public ServerResponseEntity<PageVO<CouponUserVO>> platformCouponUserPage(@Valid PageDTO pageDTO, CouponUserDTO couponUserDTO) {
        PageVO<CouponUserVO> couponPage = couponUserService.getPageByUserId(pageDTO, couponUserDTO);
        return ServerResponseEntity.success(couponPage);
    }



}
