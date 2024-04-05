package com.mall4j.cloud.api.coupon.dto;

import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 平台选择优惠券
 * @author FrozenWatermelon
 * @date 2020/12/18
 */
public class PlatformChooseCouponDTO {

    private Long userId;

    @ApiModelProperty(value = "用户是否改变了优惠券的选择，如果用户改变了优惠券的选择，则完全根据传入参数进行优惠券的选择")
    private Integer userChangeCoupon;

    @ApiModelProperty(value = "优惠券id数组")
    private List<String> couponIds;

    @ApiModelProperty(value = "多个店铺订单合并在一起的合并类")
    private ShopCartOrderMergerVO shopCartOrderMergerVO;

    public PlatformChooseCouponDTO() {
    }

    public PlatformChooseCouponDTO(Integer userChangeCoupon, List<String> couponIds, ShopCartOrderMergerVO shopCartOrderMergerVO) {
        this.userChangeCoupon = userChangeCoupon;
        this.couponIds = couponIds;
        this.shopCartOrderMergerVO = shopCartOrderMergerVO;
    }

    public List<String> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<String> couponIds) {
        this.couponIds = couponIds;
    }

    public Integer getUserChangeCoupon() {
        return userChangeCoupon;
    }

    public void setUserChangeCoupon(Integer userChangeCoupon) {
        this.userChangeCoupon = userChangeCoupon;
    }

    public ShopCartOrderMergerVO getShopCartOrderMergerVO() {
        return shopCartOrderMergerVO;
    }

    public void setShopCartOrderMergerVO(ShopCartOrderMergerVO shopCartOrderMergerVO) {
        this.shopCartOrderMergerVO = shopCartOrderMergerVO;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PlatformChooseCouponDTO{" +
                "userChangeCoupon=" + userChangeCoupon +
                ", couponIds=" + couponIds +
                ", shopCartOrderMergerVO=" + shopCartOrderMergerVO +
                '}';
    }
}
