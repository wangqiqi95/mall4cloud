package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.product.vo.search.SpuDiscountItemAppVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * spu满减满折VO
 *
 * @author YXF
 * @date 2021-03-03 09:00:09
 */
public class SpuDiscountAppVO {


	@ApiModelProperty("满减满折优惠id")
	private Long discountId;

	@ApiModelProperty("活动名称")
	private String discountName;

	@ApiModelProperty("枚举DiscountRule(0 满钱减钱 3满件打折)")
	private Integer discountRule;

	@ApiModelProperty("减免类型 0按满足最高层级减一次 1每满一次减一次")
	private Integer discountType;

	@ApiModelProperty("最多减多少")
	private Long maxReduceAmount;

    @ApiModelProperty("满减项")
    private List<SpuDiscountItemAppVO> discountItemList;

	public List<SpuDiscountItemAppVO> getDiscountItemList() {
		return discountItemList;
	}

	public void setDiscountItemList(List<SpuDiscountItemAppVO> discountItemList) {
		this.discountItemList = discountItemList;
	}

	public Long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}

	public Integer getDiscountRule() {
		return discountRule;
	}

	public void setDiscountRule(Integer discountRule) {
		this.discountRule = discountRule;
	}

	public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	public Long getMaxReduceAmount() {
		return maxReduceAmount;
	}

	public void setMaxReduceAmount(Long maxReduceAmount) {
		this.maxReduceAmount = maxReduceAmount;
	}

	@Override
	public String toString() {
		return "SpuDiscountVO{" +
				"discountId=" + discountId +
				", discountName='" + discountName + '\'' +
				", discountRule=" + discountRule +
				", discountType=" + discountType +
				", maxReduceAmount=" + maxReduceAmount +
				", discountItemList=" + discountItemList +
				'}';
	}
}
