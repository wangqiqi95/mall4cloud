package com.mall4j.cloud.coupon.dto;

import com.mall4j.cloud.api.coupon.constant.CouponSourceType;
import com.mall4j.cloud.coupon.model.TCouponSpu;
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
public class CouponDetailDTO {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "优惠券名称")
	private String name;

	@ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
	private Short kind;

	@ApiModelProperty(value = "券码集合")
	private List<String> codes;

	@ApiModelProperty(value = "是否全部门店")
	private Boolean isAllShop;

	@ApiModelProperty(value = "门店集合")
	private List<Long> shops;

	@ApiModelProperty(value = "优惠券类型（0：抵用券/1：折扣券）")
	private Short type;

	@ApiModelProperty(value = "价格类型（0：吊牌价/1：实付金额）")
	private Short priceType;

	@ApiModelProperty(value = "折扣券最大抵扣金额")
	private BigDecimal maxDeductionAmount;

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

	@ApiModelProperty(value = "商品限制件数  当类型为数量区间时存最大值")
	private Integer commodityLimitMaxNum;

	@ApiModelProperty(value = "适用商品类型（0：不限/1：指定商品SPU/2：指定商品不可用SPU 3：/指定商品SKU 4：/指定商品不可用SKU）")
	private Short commodityScopeType;

	@ApiModelProperty("当适用商品类型为 1，2时 商品id列表")
	private List<Long> commodities;

	@ApiModelProperty(value = "新加逻辑 适用商品 分类类型（1：不限/2：指定分类可用）")
	private Short categoryScopeType;

	@ApiModelProperty(value = "新加逻辑 适用商品分类集合")
	private List<String> categorys;

	@ApiModelProperty("当适用商品类型为 3，4时 商品集合")
	private List<TCouponSpu> spus;

	@ApiModelProperty(value = "sku pricecode集合",hidden = true)
	private List<String> priceCodes;

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
	private Date startTime;

	@ApiModelProperty(value = "生效结束时间")
	private Date endTime;
	@ApiModelProperty(value = "领券后x天起生效")
	private Integer afterReceiveDays;
	@ApiModelProperty(value = "是否有商品原折扣限制")
	private Integer disNoles;
	@ApiModelProperty(value = "折扣限制值（例如7折维护 7 ）")
	private BigDecimal disNolesValue;
	@ApiModelProperty(value = "是否支持0元商品单")
	private Integer issharePaytype;

	/**
	 * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
	 */
	private int sourceType = CouponSourceType.SELF_ADD.getType();

	/**
	 * crm优惠券id
	 */
	private String crmCouponId;

	@ApiModelProperty(value = "券码总数")
	private Integer codeSum;

	@ApiModelProperty(value = "券码库存")
	private Integer codeStock;


}
