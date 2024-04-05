package com.mall4j.cloud.coupon.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.coupon.dto.SpuListDTO;
import com.mall4j.cloud.coupon.mapper.TCouponShopMapper;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.model.TCouponShop;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponService;
import com.mall4j.cloud.coupon.vo.CouponDetailForShoppersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券
 *
 * @author shijing
 * @date 2022-02-06
 */
@Slf4j
@RestController("appTCouponController")
@RequestMapping("/ma/t_coupon")
@Api(tags = "app-优惠券(新)")
public class TCouponController {

    @Resource
    private TCouponService tCouponService;
    @Autowired
    private PushCouponActivityService pushCouponActivityService;

    @Autowired
    private TCouponShopMapper tCouponShopMapper;


    @PostMapping("/coupon_list")
    @ApiOperation(value = "优惠券列表")
    public ServerResponseEntity<List<TCoupon>> couponList(@RequestBody List<Long> ids) {
        return tCouponService.selectCouponByIds(ids);
    }

    @GetMapping("/coupon_detail")
    @ApiOperation(value = "详情")
    @ApiImplicitParam(name = "id", value = "卡券id", required = true)
    public ServerResponseEntity<CouponDetailForShoppersVO> couponDetail(@RequestParam("id") Long id) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        Long userId = null;
        if (Objects.nonNull(userInfoInTokenBO)) {
            userId = userInfoInTokenBO.getUserId();
        }
        return pushCouponActivityService.detailForShoppers(id, userId);
    }

    @PostMapping("/coupon_spu_list")
    @ApiOperation(value = "优惠券商品列表")
    public ServerResponseEntity<PageVO<SpuCommonVO>> couponSpuList(@RequestBody SpuListDTO param)  {
        return tCouponService.couponSpuList(param);
    }

    @PostMapping("/coupon_shop_list")
    @ApiOperation(value = "查询门店列表")
    public ServerResponseEntity<PageVO<SelectedStoreVO>> couponShopList(@RequestBody SpuListDTO param)  {
        //过滤以下状态门店不展示
        if(CollectionUtil.isEmpty(param.getSlcNames())){
            List<String> slcNames=new ArrayList<>();
            slcNames.add("已关店");
            slcNames.add("已转店");
            slcNames.add("已删除");
            param.setSlcNames(slcNames);
        }
        return tCouponService.couponShopList(param);
    }
}
