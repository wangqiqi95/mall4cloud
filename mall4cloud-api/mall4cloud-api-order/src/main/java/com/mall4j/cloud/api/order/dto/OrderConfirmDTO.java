package com.mall4j.cloud.api.order.dto;

import com.mall4j.cloud.common.order.dto.ShopCartItemDTO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhangjie
 */
@Data
public class OrderConfirmDTO {
    @ApiModelProperty(value = "订单优惠券类型")
    private OrderActivityDTO activityDTO;


    @ApiModelProperty(value = "提交订单参数")
    private List<OrderConfirmItemDTO> orderConfirmItemDTOList;

    @ApiModelProperty(value = "使用优惠券限制 0-不使用 1-不限制使用 2-仅使用抵扣券 3-仅使用折扣券")
    private Integer useCoupon;


    @ApiModelProperty(value = "地址ID，0为默认地址")
    @NotNull(message = "地址不能为空")
    private Long addrId;

    @NotNull(message = "配送类型不能为空")
    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递 4:同城配送")
    private Integer dvyType;


    @ApiModelProperty(value = "用户是否改变了优惠券的选择，如果用户改变了优惠券的选择，则完全根据传入参数进行优惠券的选择")
    private Integer userChangeCoupon;


    @ApiModelProperty(value = "订单类型：4 一口价")
    private Integer orderType;

    @ApiModelProperty(value = "门店id")
    private Long storeId;
    @ApiModelProperty(value = "触点号")
    private String tentacleNo;
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "实际总值", required = true)
    private Long actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Long total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "订单优惠金额(所有店铺优惠金额和使用积分抵现相加)", required = true)
    private Long orderReduce;

}
