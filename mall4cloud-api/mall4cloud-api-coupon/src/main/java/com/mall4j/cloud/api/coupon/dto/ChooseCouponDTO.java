package com.mall4j.cloud.api.coupon.dto;

import com.mall4j.cloud.common.order.vo.ShopCartVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 选择优惠券
 *
 * @author FrozenWatermelon
 * @date 2020/12/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChooseCouponDTO {
    
    private Long userId;
    
    private Long storeId;
    
    @ApiModelProperty(value = "用户是否改变了优惠券的选择，如果用户改变了优惠券的选择，则完全根据传入参数进行优惠券的选择")
    private Integer userChangeCoupon;
    
    @ApiModelProperty(value = "优惠券id数组")
    private List<String> couponIds;
    
    @ApiModelProperty(value = "购物车信息")
    List<ShopCartVO> shopCarts;
    
    private Long totalTransfee;
    
    private String couponCode;
    
    public ChooseCouponDTO(Integer userChangeCoupon, Long storeId,List<String> couponIds, List<ShopCartVO> shopCarts, Long totalTransfee, String couponCode) {
        this.userChangeCoupon = userChangeCoupon;
        this.storeId = storeId;
        this.couponIds = couponIds;
        this.shopCarts = shopCarts;
        this.totalTransfee = totalTransfee;
        this.couponCode = couponCode;
    }
}
