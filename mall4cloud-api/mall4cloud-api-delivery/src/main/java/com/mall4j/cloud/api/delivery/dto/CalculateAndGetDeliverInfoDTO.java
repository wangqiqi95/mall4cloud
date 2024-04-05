package com.mall4j.cloud.api.delivery.dto;

import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/15
 */
public class CalculateAndGetDeliverInfoDTO {


    private Long userId;

    @ApiModelProperty(value = "地址ID，0为默认地址")
    @NotNull(message = "地址不能为空")
    private Long addrId;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递 4:同城配送")
    private Integer dvyType;

    @ApiModelProperty(value = "用户选择的自提点id")
    private Long stationId;

    @ApiModelProperty(value = "购物项")
    private List<ShopCartItemVO> shopCartItems;

    public CalculateAndGetDeliverInfoDTO() {
    }

    public CalculateAndGetDeliverInfoDTO(Long addrId, Integer dvyType, Long stationId, List<ShopCartItemVO> shopCartItems) {
        this.addrId = addrId;
        this.dvyType = dvyType;
        this.stationId = stationId;
        this.shopCartItems = shopCartItems;
    }

    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
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

    public List<ShopCartItemVO> getShopCartItems() {
        return shopCartItems;
    }

    public void setShopCartItems(List<ShopCartItemVO> shopCartItems) {
        this.shopCartItems = shopCartItems;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CalculateAndGetDeliverInfoDTO{" +
                "addrId=" + addrId +
                ", dvyType=" + dvyType +
                ", stationId=" + stationId +
                ", shopCartItems=" + shopCartItems +
                '}';
    }
}
