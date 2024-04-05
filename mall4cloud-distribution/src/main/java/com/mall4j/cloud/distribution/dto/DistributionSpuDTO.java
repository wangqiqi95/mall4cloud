package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分销商品关联信息DTO
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public class DistributionSpuDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分销商品表")
    private Long distributionSpuId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("奖励id")
    private Long awardId;

    @ApiModelProperty("状态(0:商家下架 1:商家上架 2:违规下架 3:平台审核)")
    private Integer state;

    @ApiModelProperty("奖励方式(0 按比例 1 按固定数值)")
    private Integer awardMode;

    @ApiModelProperty("上级奖励设置(0 关闭 1开启)")
    private Integer parentAwardSet;

	@ApiModelProperty("奖励数额(奖励方式为0时，表示百分比，为1时代表实际奖励金额）")
	private Long awardNumbers;

	@ApiModelProperty("上级奖励数额(奖励方式为0时，表示百分比，为1时代表实际奖励金额）")
	private Long parentAwardNumbers;

    @ApiModelProperty("操作人id")
    private Long modifier;

	public Long getAwardNumbers() {
		return awardNumbers;
	}

	public void setAwardNumbers(Long awardNumbers) {
		this.awardNumbers = awardNumbers;
	}

	public Long getParentAwardNumbers() {
		return parentAwardNumbers;
	}

	public void setParentAwardNumbers(Long parentAwardNumbers) {
		this.parentAwardNumbers = parentAwardNumbers;
	}

	public Long getDistributionSpuId() {
		return distributionSpuId;
	}

	public void setDistributionSpuId(Long distributionSpuId) {
		this.distributionSpuId = distributionSpuId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getAwardId() {
		return awardId;
	}

	public void setAwardId(Long awardId) {
		this.awardId = awardId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAwardMode() {
		return awardMode;
	}

	public void setAwardMode(Integer awardMode) {
		this.awardMode = awardMode;
	}

	public Integer getParentAwardSet() {
		return parentAwardSet;
	}

	public void setParentAwardSet(Integer parentAwardSet) {
		this.parentAwardSet = parentAwardSet;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	@Override
	public String toString() {
		return "DistributionSpuDTO{" +
				"distributionSpuId=" + distributionSpuId +
				", shopId=" + shopId +
				", spuId=" + spuId +
				", awardId=" + awardId +
				", state=" + state +
				", awardMode=" + awardMode +
				", parentAwardSet=" + parentAwardSet +
				", awardNumbers=" + awardNumbers +
				", parentAwardNumbers=" + parentAwardNumbers +
				", modifier=" + modifier +
				'}';
	}
}
