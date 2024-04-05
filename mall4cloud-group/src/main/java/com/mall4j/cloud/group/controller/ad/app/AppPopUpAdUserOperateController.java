package com.mall4j.cloud.group.controller.ad.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.PopUpAdClickDTO;
import com.mall4j.cloud.group.dto.PopUpAdCouponReceiveDTO;
import com.mall4j.cloud.group.service.PopUpAdUserOperateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ma/pop/up/ad/user/operate")
@Api(tags = "小程序端弹窗广告用户操作相关接口")
@Slf4j
public class AppPopUpAdUserOperateController {

    @Autowired
    private PopUpAdUserOperateService popUpAdUserOperateService;


    @PostMapping("/click")
    @ApiOperation("点击广告")
    public ServerResponseEntity getThePopUpAdTreeByUser(@RequestBody PopUpAdClickDTO popUpAdClickDTO){
        return popUpAdUserOperateService.click(popUpAdClickDTO);
    }


    @PostMapping("/receive/coupon")
    @ApiOperation("领取广告附属优惠券")
    public ServerResponseEntity popUpAdCouponReceive(@RequestBody PopUpAdCouponReceiveDTO popUpAdCouponReceiveDTO){
        return popUpAdUserOperateService.couponReceive(popUpAdCouponReceiveDTO);
    }


}
