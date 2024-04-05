package com.mall4j.cloud.api.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券详情信息
 * @author shijing
 * @date 2022-01-03 14:55:56
 */
@Data
@ApiModel(description = "优惠券详情信息")
public class CouponDetailVO {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "优惠券名称")
	private String name;

	@ApiModelProperty(value = "第三方券码")
	private String otherCouponCode;

	@ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
	private Short kind;

	@ApiModelProperty(value = "券码集合")
	private List<String> codes;

	@ApiModelProperty(value = "券码库存")
	private Integer codeNum;

	@ApiModelProperty(value = "优惠券类型（0：抵用券/1：折扣券）")
	private Short type;

	@ApiModelProperty(value = "价格类型（0：吊牌价/1：实付金额）")
	private Integer priceType;

	@ApiModelProperty(value = "抵用金额")
	private BigDecimal reduceAmount;

	@ApiModelProperty(value = "折扣力度")
	private BigDecimal couponDiscount;

	@ApiModelProperty(value = "金额限制类型（0：不限/1：满额）")
	private Short amountLimitType;

	@ApiModelProperty(value = "限制金额")
	private BigDecimal amountLimitNum;

	@ApiModelProperty(value = "商品限制类型（0：不限/1：不超过/2：不少于）")
	private Short commodityLimitType;

	@ApiModelProperty(value = "商品限制件数")
	private Integer commodityLimitNum;

	@ApiModelProperty(value = "适用商品类型（0：不限/1：指定商品）")
	private Short commodityScopeType;

	@ApiModelProperty("商品id列表")
	private List<Long> commodities;

	@ApiModelProperty(value = "限定商品总数")
	private Integer commodityNum;

	@ApiModelProperty(value = "适用范围（0：不限/1：线上/2：线下）")
	private Short applyScopeType;

	@ApiModelProperty(value = "优惠券封面")
	private String coverUrl;

	@ApiModelProperty(value = "优惠券说明")
	private String description;

	@ApiModelProperty(value = "优惠券备注")
	private String note;

	@ApiModelProperty(value = "生效时间类型（1：固定时间/2：领取后生效）")
	private Short timeType;

	@ApiModelProperty(value = "生效开始时间")
	private Date startTime;

	@ApiModelProperty(value = "生效结束时间")
	private Date endTime;

	@ApiModelProperty(value = "领券后x天起生效")
	private Integer afterReceiveDays;

}
