package com.mall4j.cloud.coupon.controller.app;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.constant.Auth;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.dto.AppCouponListDTO;
import com.mall4j.cloud.coupon.dto.BatchCouponListDTO;
import com.mall4j.cloud.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.coupon.dto.StaffSendCouponDTO;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.MyCouponDetailVO;
import com.mall4j.cloud.coupon.vo.MyCouponListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

/**
 * 优惠券
 *
 * @author shijing
 * @date 2022-02-06
 */
@Slf4j
@RestController("appTCouponUserController")
@RequestMapping("/t_coupon_user")
@Api(tags = "app-用户优惠券")
public class TCouponUserController {
    @Resource
    private TCouponUserService tCouponUserService;
    @Resource
    private PushCouponActivityService pushCouponActivityService;

    @PostMapping("/my_coupon_list")
    @ApiOperation(value = "用户优惠券列表")
    public ServerResponseEntity<PageInfo<MyCouponListVO>> myCouponList(@RequestBody AppCouponListDTO param) {
        param.setUserId(AuthUserContext.get().getUserId());
        return tCouponUserService.myCouponList(param);
    }

    @GetMapping("/my_coupon_detail")
    @ApiOperation(value = "用户优惠券详情")
    public ServerResponseEntity<MyCouponDetailVO> myCouponDetail(@RequestParam("id") Long id) {
        return tCouponUserService.myCouponDetail(id);
    }

    @PostMapping("/coupon_list_by_batch")
    @ApiOperation(value = "根据批次查询用户优惠券详情")
    public ServerResponseEntity<PageInfo<MyCouponListVO>> getCouponListByBatchId(@RequestBody BatchCouponListDTO param) {
        param.setUserId(AuthUserContext.get().getUserId());
        return tCouponUserService.getCouponListByBatchId(param);
    }

    @PostMapping("/write_off_coupon")
    @ApiOperation(value = "核销券码")
    public ServerResponseEntity<Void> writeOff(@RequestParam("code") String code,@RequestParam("type") Short type) {
        return tCouponUserService.writeOff(code,type, AuthUserContext.get().getUserId());
    }

    @PostMapping("/receive_coupon")
    @ApiOperation(value = "领取优惠券")
    public ServerResponseEntity<Void> receive(@RequestBody StaffSendCouponDTO param, @RequestParam Long storeId) {
        if (Objects.isNull(param.getActivityId())) {
            throw new LuckException("活动id不能为空");
        }
        if (Objects.isNull(param.getCouponId())) {
            throw new LuckException("优惠券id不能为空");
        }
        param.setShopId(storeId);

        return pushCouponActivityService.sendCoupon(param);
    }


}
