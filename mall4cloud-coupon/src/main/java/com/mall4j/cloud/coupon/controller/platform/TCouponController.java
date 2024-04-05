package com.mall4j.cloud.coupon.controller.platform;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.model.TCouponCode;
import com.mall4j.cloud.coupon.model.TCouponUser;
import com.mall4j.cloud.coupon.service.TCouponService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.CouponListVO;
import com.mall4j.cloud.coupon.vo.MyCouponListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 优惠券 *
 * @author shijing
 * @date 2022-01-03 14:55:56
 */
@RestController("platformTCouponController")
@RequestMapping("/p/tcoupon")
@Api(tags = "platform-优惠券(新)")
public class TCouponController {
    @Resource
    private TCouponService tCouponService;
    @Resource
    private TCouponUserService tCouponUserService;


    @PostMapping("/list")
    @ApiOperation(value = "优惠券列表")
    public ServerResponseEntity<PageInfo<CouponListVO>> list(@RequestBody CouponListDTO param) {
        return tCouponService.list(param);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增优惠券")
    public ServerResponseEntity<Void> save(@RequestBody CouponDetailDTO param) {
        return tCouponService.save(param);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "优惠券详情")
    public ServerResponseEntity<CouponDetailDTO> detail(@RequestParam Long id) {
        return tCouponService.detail(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改优惠券")
    public ServerResponseEntity<Void> update(@RequestBody CouponDetailDTO param) {
        tCouponService.update(param);
        tCouponService.removeCacheByCouponId(param.getId());
        return ServerResponseEntity.success();
    }

    @PostMapping("/addCode")
    @ApiOperation(value = "新增券码库存")
    public ServerResponseEntity<Void> addCode(@RequestBody CouponDetailDTO param) {
        return tCouponService.addCode(param);
    }

    @GetMapping("/failure")
    @ApiOperation(value = "失效优惠券")
    public ServerResponseEntity<Void> failure(@RequestParam Long id) {
        return tCouponService.failure(id);
    }

    @PostMapping("/importCode")
    @ApiOperation(value = "导入券码")
    public ServerResponseEntity<List<String>> importCode(@RequestParam("file") MultipartFile file) {
        return tCouponService.importCode(file);
    }

    @PostMapping("/coupon_list")
    @ApiOperation(value = "优惠券列表")
    public ServerResponseEntity<List<TCoupon>> couponList(@RequestBody List<Long> ids) {
        return tCouponService.selectCouponByIds(ids);
    }

    @PostMapping("/code_list")
    @ApiOperation(value = "券码查询列表")
    public ServerResponseEntity<PageInfo<TCouponCode>> couponCodeList(@RequestBody CodeListDTO param) {
        return tCouponService.couponCodeList(param);
    }

    @PostMapping("/coupon_shop_list")
    @ApiOperation(value = "优惠券门店列表")
    public ServerResponseEntity<PageVO<SelectedStoreVO>> couponShopList(@RequestBody SpuListDTO param)  {
        return tCouponService.couponShopList(param);
    }

    @PostMapping("/syncCoupons")
    @ApiOperation(value = "优惠券门店列表")
    public void syncCoupons() {
        tCouponService.syncCoupons();
    }

}
