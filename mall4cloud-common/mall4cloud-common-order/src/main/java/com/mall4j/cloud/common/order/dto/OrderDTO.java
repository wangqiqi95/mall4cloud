package com.mall4j.cloud.common.order.dto;

import com.mall4j.cloud.common.order.dto.ShopCartItemDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单参数
 *
 * @author FrozenWatermelon
 * @date 2020-12-04 11:27:35
 */
@Data
public class OrderDTO {

    @ApiModelProperty(value = "立即购买时提交的商品项,如果该值为空，则说明是从购物车进入，如果该值不为空则说明为立即购买")
    private ShopCartItemDTO shopCartItem;


    @ApiModelProperty(value = "代客下单时提交的商品项")
    private List<ShopCartItemDTO> shopCartItemList;

    @ApiModelProperty(value = "地址ID，0为默认地址")
    @NotNull(message = "地址不能为空")
    private Long addrId;

    @NotNull(message = "配送类型不能为空")
    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递 4:同城配送")
    private Integer dvyType;

    @ApiModelProperty(value = "用户选择的自提点id")
    private Long stationId;

    @ApiModelProperty(value = "用户是否改变了优惠券的选择，如果用户改变了优惠券的选择，则完全根据传入参数进行优惠券的选择")
    private Integer userChangeCoupon;

    @ApiModelProperty(value = "优惠券id数组")
    private List<String> couponIds;

    @ApiModelProperty(value = "用户是否选择积分抵现(0不使用 1使用 默认不使用)")
    private Integer isScorePay;

    @ApiModelProperty(value = "用户是否自己选择使用多少积分，为空则为默认全部使用")
    private Long userUseScore;

    @ApiModelProperty(value = "订单类型：0团购")
    private Integer orderType;

    @ApiModelProperty(value = "门店id")
    private Long storeId;
    @ApiModelProperty(value = "触点号")
    private String tentacleNo;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty("选择赠品信息")
    private GiftProdDTO giftProdDTO;

    @ApiModelProperty(value = "价格类型")
    private Integer priceType;
    @ApiModelProperty(value = "优惠券类型")
    private Integer couponType;
    @ApiModelProperty(value = "视频号,跟踪ID，有效期十分钟，会影响主播归因、分享员归因等，需创建订单前调用，调用生成订单 api 时需传入该参数")
    private String traceId;
    
    @ApiModelProperty(value = "纸质券码")
    private String couponCode;


    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }

    public Integer getUserChangeCoupon() {
        return userChangeCoupon;
    }

    public void setUserChangeCoupon(Integer userChangeCoupon) {
        this.userChangeCoupon = userChangeCoupon;
    }


    public Integer getIsScorePay() {
        return isScorePay;
    }

    public void setIsScorePay(Integer isScorePay) {
        this.isScorePay = isScorePay;
    }

    public Integer getDvyType() {
        return dvyType;
    }

    public void setDvyType(Integer dvyType) {
        this.dvyType = dvyType;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getUserUseScore() {
        return userUseScore;
    }

    public void setUserUseScore(Long userUseScore) {
        this.userUseScore = userUseScore;
    }

    public ShopCartItemDTO getShopCartItem() {
        return shopCartItem;
    }

    public void setShopCartItem(ShopCartItemDTO shopCartItem) {
        this.shopCartItem = shopCartItem;
    }



    @Override
    public String toString() {
        return "OrderDTO{" +
                ", shopCartItem=" + shopCartItem +
                ", addrId=" + addrId +
                ", userChangeCoupon=" + userChangeCoupon +
                ", couponIds=" + couponIds +
                ", isScorePay=" + isScorePay +
                ", dvyType=" + dvyType +
                ", stationId=" + stationId +
                ", userUseScore=" + userUseScore +
                '}';
    }
}
