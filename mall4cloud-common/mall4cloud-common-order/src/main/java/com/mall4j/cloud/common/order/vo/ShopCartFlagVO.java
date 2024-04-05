package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.constant.Constant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 购物车VO
 *
 * @author FrozenWatermelon
 * @date 2020-11-20 15:47:32
 */
@Data
public class ShopCartFlagVO {

	private List<ShopCartVO> shopCarts;

	private Boolean flag;
}
