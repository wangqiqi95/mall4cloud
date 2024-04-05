package com.mall4j.cloud.coupon.controller.platform;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.model.TCouponCode;
import com.mall4j.cloud.coupon.model.TCouponUser;
import com.mall4j.cloud.coupon.service.TCouponService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.CouponListVO;
import com.mall4j.cloud.coupon.vo.MyCouponListVO;
import com.mall4j.cloud.coupon.vo.UserCouponListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 用户优惠券 *
 * @author shijing
 * @date 2022-03-17 14:55:56
 */
@RestController("platformTCouponUserController")
@RequestMapping("/p/tcouponUser")
@Api(tags = "platform-用户优惠券(新)")
public class TCouponUserController {
    @Resource
    private TCouponUserService tCouponUserService;

    @PostMapping("/coupon_list")
    @ApiOperation(value = "用户优惠券列表")
    public ServerResponseEntity<PageInfo<UserCouponListVO>> userCouponList(@RequestBody AppCouponListDTO param) {
        return tCouponUserService.userCouponList(param);
    }

    @GetMapping("/selectOrderNo")
    @ApiOperation(value = "查询某个时间段已核销的企业券信息")
    public ServerResponseEntity<List<TCouponUser>> selectOrderNo(@RequestParam("startTime") Date startTime, @RequestParam("endTime")  Date endTime ) {
        return tCouponUserService.selectOrderNo(startTime,endTime);
    }

    @PostMapping("/systemReceiveCoupon")
    @ApiOperation(value = "系统发券")
    public ServerResponseEntity<Void> systemReceiveCoupon(@RequestBody SystemCouponDTO param) {
        return tCouponUserService.batchReceiveCoupon(param);
    }

    @GetMapping("/isUseEnterpriseCoupon")
    @ApiOperation(value = "查看订单是否使用企业券")
    public ServerResponseEntity<Boolean> isUseEnterpriseCoupon(@RequestParam("orderNo")Long orderNo) {
        Boolean useEnterpriseCoupon = tCouponUserService.isUseEnterpriseCoupon(orderNo);
        return ServerResponseEntity.success(useEnterpriseCoupon);
    }

}
