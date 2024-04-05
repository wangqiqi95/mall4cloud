package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */
@Data
public class AppCouponVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

	@ApiModelProperty("活动ID")
	private Long activityId;

	@ApiModelProperty(value = "是否神券")
	private Boolean isCommon;

	@ApiModelProperty(value = "限制数量")
	private Integer stocksLimit;

	@ApiModelProperty(value = "库存")
	private Integer stocks;

	@ApiModelProperty(value = "领取比例")
	private BigDecimal proportion;

	@ApiModelProperty(value = "优惠券类型（0：抵用券/1：折扣券）")
	private Short type;

	@ApiModelProperty(value = "抵用金额")
	private BigDecimal reduceAmount;

	@ApiModelProperty(value = "折扣力度")
	private BigDecimal couponDiscount;

	@ApiModelProperty(value = "金额限制类型（0：不限/1：满额）")
	private Short amountLimitType;

	@ApiModelProperty(value = "限制金额")
	private BigDecimal amountLimitNum;

	@ApiModelProperty(value = "适用商品类型（0：不限/1：指定商品）")
	private Short commodityScopeType;

	@ApiModelProperty(value = "适用范围（0：不限/1：线上/2：线下）")
	private Short applyScopeType;

	@ApiModelProperty("商品列表")
	private List<SpuCommonVO> commodities;

	@ApiModelProperty(value = "是否全部门店")
	private Boolean isAllShop;

	@ApiModelProperty(value = "门店集合")
	private List<SelectedStoreVO> shops;

	@ApiModelProperty(value = "是否个人限制")
	private Boolean isPersonLimit;

	@ApiModelProperty(value = "个人限制数量")
	private Integer limitNum;

	@ApiModelProperty(value = "用户是否已经领取")
	private Boolean receiveStatus = true;

}
