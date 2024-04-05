package com.mall4j.cloud.api.coupon.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.mall4j.cloud.api.coupon.constant.CouponSourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
public class CouponDetailDTO implements Serializable {

	private static final long serialVersionUID = 6233513322060817291L;
	@ApiModelProperty(value = "couponid")
	private String id;

	private Long couponid;

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

	@ApiModelProperty(value = "抵用金额")
	private String reduceAmount;

	@ApiModelProperty(value = "折扣力度")
	private String couponDiscount;

	@ApiModelProperty(value = "折扣券最大抵扣金额（新）")
	private String maxDeductionAmount;

	@ApiModelProperty(value = "金额限制类型（0：不限/1：满额）")
	private Short amountLimitType;

	@ApiModelProperty(value = "限制金额")
	private String amountLimitNum;

	@ApiModelProperty(value = "商品限制类型（0：不限/1：不超过/2：不少于 /3:区间）")
	private Short commodityLimitType;

	@ApiModelProperty(value = "商品限制件数")
	private Integer commodityLimitNum;

	@ApiModelProperty(value = "商品限制件数最大值")
	private Integer commodityLimitMaxNum;

	@ApiModelProperty(value = "适用商品类型（0：不限/1：指定商品/2：指定商品不可用）")
	private Short commodityScopeType;

	@ApiModelProperty("商品code列表")
	private List<String> commodities;

	@ApiModelProperty(value = "适用分类类型（1：不限/2：指定分类）")
	private Short categoryScopeType;

	@ApiModelProperty("适用分类信息")
	private List<String> categorys;

	@ApiModelProperty(value = "适用门店类型（0：不限/1：指定门店）")
	private Short storeScopeType;

	@ApiModelProperty("门店code列表")
	private List<String> storeCodes;

	@ApiModelProperty(value = "限定商品总数")
	private Integer commodityNum;

	@ApiModelProperty(value = "适用范围（0：不限/1：线上/2：线下）")
	private Short applyScopeType;

	@ApiModelProperty(value = "优惠券图片标识（0：默认/1：礼品券/2：生日礼/3:员工券/4:升级礼/5：保级礼/6:服务券/7:满减券/8:折扣券/9:入会券/10:免邮券）")
	private Integer couponPicture;

	@ApiModelProperty(value = "优惠券封面")
	private String coverUrl;

	@ApiModelProperty(value = "优惠券说明")
	private String description;

	@ApiModelProperty(value = "优惠券备注")
	private String note;

	@ApiModelProperty(value = "生效时间类型（1：固定时间/2：领取后生效）")
	private Short timeType;

	@ApiModelProperty(value = "生效开始时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@ApiModelProperty(value = "生效结束时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	@ApiModelProperty(value = "领券后x天起生效")
	private Integer afterReceiveDays;

	/**
	 * 价格类型（0：吊牌价/1：实付金额）
	 */
	@ApiModelProperty(value = "价格类型（0：吊牌价/1：实付金额）")
	private Short priceType;

	@ApiModelProperty(value = "是否有商品原折扣限制 （新）")
	private Integer disNoles = 0;
	@ApiModelProperty(value = "折扣限制值（例如7折维护 7 ）（新）")
	private BigDecimal disNolesValue;
	@ApiModelProperty(value = "是否支持0元商品单 （新）")
	private Integer issharePaytype = 0;

	/**
	 * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
	 */
	private int sourceType = CouponSourceType.SELF_ADD.getType();

}
