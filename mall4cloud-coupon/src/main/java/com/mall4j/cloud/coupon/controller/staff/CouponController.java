package com.mall4j.cloud.coupon.controller.staff;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.dto.AppCouponListDTO;
import com.mall4j.cloud.coupon.dto.WriteOffCouponDTO;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;


@RestController("staffCouponController")
@RequestMapping("/s/coupon")
@Api(tags = "导购小程序-送券中心")
public class CouponController {

    @Autowired
    private PushCouponActivityService pushCouponActivityService;
    @Resource
    private TCouponUserService tCouponUserService;
    @Resource
    private StaffFeignClient staffFeignClient;

    @GetMapping("/page")
    @ApiOperation(value = "列表")
    @ApiImplicitParam(name = "type", value = "优惠券类型（0：抵用券/1：折扣券）", required = true)
    public ServerResponseEntity<PageVO<CouponForShoppersVO>> listForShoppers(@Valid PageDTO pageDTO, @RequestParam("type") Integer type) {
        return pushCouponActivityService.listForShoppers(pageDTO, type);
    }

    @GetMapping("/send_coupon_detail")
    @ApiOperation(value = "详情")
    @ApiImplicitParam(name = "id", value = "卡券id", required = true)
    public ServerResponseEntity<CouponDetailForShoppersVO> detailForShoppers(@RequestParam("id") Long id) {
        return pushCouponActivityService.detailForShoppers(id, null);
    }

    @GetMapping("/receive_coupon_stats")
    @ApiOperation(value = "领券统计")
    public ServerResponseEntity<StaffReceiveCouponStatsVO> receiveCouponStats() {
        return tCouponUserService.staffReceiveCouponStats(AuthUserContext.get().getUserId());
    }

    @GetMapping("/write_off_stats")
    @ApiOperation(value = "核销统计")
    public ServerResponseEntity<StaffWriteOffCouponStatsVO> writeOffCouponStats() {
        return tCouponUserService.staffWriteOffCouponStats(AuthUserContext.get().getUserId());
    }

    @GetMapping("/ua/my_coupon_list")
    @ApiOperation(value = "用户优惠券列表")
    public ServerResponseEntity<PageVO<MyCouponListVO>> myCouponList(@Valid PageDTO pageDTO,
                                                                     @RequestParam Long userId, @RequestParam String type) {
        AppCouponListDTO param = new AppCouponListDTO();
        param.setUserId(userId);
        param.setType(type);
        param.setPageNo(pageDTO.getPageNum());
        param.setPageSize(pageDTO.getPageSize());
        PageVO<MyCouponListVO> pageVO = new PageVO<>();
        ServerResponseEntity<PageInfo<MyCouponListVO>> pageInfo = tCouponUserService.myCouponList(param);
        pageVO.setTotal(0l);
        pageVO.setList(Collections.emptyList());
        if (pageInfo.isSuccess()) {
            pageVO.setTotal(pageInfo.getData().getTotal());
            pageVO.setList(pageInfo.getData().getList());
        }
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping("/write_off_detail")
    @ApiOperation(value = "根据券码查询用户优惠券详情")
    public ServerResponseEntity<WriteOffDetailVO> writeOffCouponDetail(@RequestParam String couponCode) {
        return tCouponUserService.writeOffCouponDetail(couponCode);
    }

    @PostMapping("/write_off_coupon")
    @ApiOperation(value = "线下核销优惠券")
    public ServerResponseEntity<Void> writeOffCoupon(@Valid @RequestBody WriteOffCouponDTO param) {
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (staffResp.isSuccess() && Objects.nonNull(staffResp.getData())) {
            StaffVO staffVO = staffResp.getData();
            param.setWriteOffUserId(AuthUserContext.get().getUserId());
            param.setWriteOffUserName(staffVO.getStaffName());
            param.setWriteOffUserCode(staffVO.getStaffNo());
            param.setWriteOffUserPhone(staffVO.getMobile());
            param.setWriteOffShopId(staffVO.getStoreId());
            param.setWriteOffShopName(staffVO.getStoreName());
        }
        return tCouponUserService.writeOffCoupon(param);
    }

}
