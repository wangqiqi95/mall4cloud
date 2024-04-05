package com.mall4j.cloud.coupon.controller.platform;

import com.mall4j.cloud.api.coupon.dto.TCouponActivityCentreParamDTO;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.service.TCouponActivityCentreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

/**
 * @Date 2022年10月17日, 0017 12:05
 */
@RestController("platformTCouponActivityCentreController")
@RequestMapping("/p/tcoupon/activitycentre")
@Api(tags = "platform-活动适用优惠券")
public class TCouponActivityCentreController {

    @Autowired
    private TCouponActivityCentreService activityCentreService;

    @PostMapping("/pageList")
    @ApiOperation(value = "分页-活动适用优惠券列表")
    public ServerResponseEntity<PageVO<CouponListVO>> list(@Valid PageDTO pageDTO, @Valid @RequestBody TCouponActivityCentreParamDTO param) {
        return ServerResponseEntity.success(activityCentreService.page(pageDTO,param));
    }

    @PostMapping("/couponACList")
    @ApiOperation(value = "活动适用优惠券列表")
    public ServerResponseEntity<List<CouponListVO>> couponACList(@RequestBody TCouponActivityCentreParamDTO param) {
        return ServerResponseEntity.success(activityCentreService.couponACList(param));
    }

}
