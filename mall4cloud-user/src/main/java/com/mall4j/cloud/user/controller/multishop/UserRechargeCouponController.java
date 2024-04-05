package com.mall4j.cloud.user.controller.multishop;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserRechargeCouponDTO;
import com.mall4j.cloud.user.model.UserRechargeCoupon;
import com.mall4j.cloud.user.service.UserRechargeCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 余额优惠券关联表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@RestController("multishopUserRechargeCouponController")
@RequestMapping("/m/user_recharge_coupon")
@Api(tags = "余额优惠券关联表")
public class UserRechargeCouponController {

    @Autowired
    private UserRechargeCouponService userRechargeCouponService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取余额优惠券关联表列表", notes = "分页获取余额优惠券关联表列表")
	public ServerResponseEntity<PageVO<UserRechargeCoupon>> page(@Valid PageDTO pageDTO) {
		PageVO<UserRechargeCoupon> userRechargeCouponPage = userRechargeCouponService.page(pageDTO);
		return ServerResponseEntity.success(userRechargeCouponPage);
	}

	@GetMapping
    @ApiOperation(value = "获取余额优惠券关联表", notes = "根据rechargeId获取余额优惠券关联表")
    public ServerResponseEntity<UserRechargeCoupon> getByRechargeId(@RequestParam Long rechargeId) {
        return ServerResponseEntity.success(userRechargeCouponService.getByRechargeId(rechargeId));
    }

    @PostMapping
    @ApiOperation(value = "保存余额优惠券关联表", notes = "保存余额优惠券关联表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserRechargeCouponDTO userRechargeCouponDTO) {
        UserRechargeCoupon userRechargeCoupon = mapperFacade.map(userRechargeCouponDTO, UserRechargeCoupon.class);
        userRechargeCoupon.setRechargeId(null);
        userRechargeCouponService.save(userRechargeCoupon);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新余额优惠券关联表", notes = "更新余额优惠券关联表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserRechargeCouponDTO userRechargeCouponDTO) {
        UserRechargeCoupon userRechargeCoupon = mapperFacade.map(userRechargeCouponDTO, UserRechargeCoupon.class);
        userRechargeCouponService.update(userRechargeCoupon);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除余额优惠券关联表", notes = "根据余额优惠券关联表id删除余额优惠券关联表")
    public ServerResponseEntity<Void> delete(@RequestParam Long rechargeId) {
        userRechargeCouponService.deleteById(rechargeId);
        return ServerResponseEntity.success();
    }
}
