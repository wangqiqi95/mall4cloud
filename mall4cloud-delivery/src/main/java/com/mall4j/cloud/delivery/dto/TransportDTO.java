package com.mall4j.cloud.delivery.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 运费模板DTO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class TransportDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("运费模板id")
    private Long transportId;

    @ApiModelProperty("运费模板名称")
    private String transName;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("收费方式（0 按件数,1 按重量 2 按体积）")
    private Integer chargeType;

    @ApiModelProperty("是否包邮 0:不包邮 1:包邮")
    private Integer isFreeFee;

    @ApiModelProperty("是否含有包邮条件 0 否 1是")
    private Integer hasFreeCondition;

	@ApiModelProperty(value = "指定条件包邮项")
	private List<TransfeeFreeDTO> transFeeFrees;

	@ApiModelProperty(value = "运费项")
	private List<TransfeeDTO> transFees;

	public List<TransfeeFreeDTO> getTransFeeFrees() {
		return transFeeFrees;
	}

	public void setTransFeeFrees(List<TransfeeFreeDTO> transFeeFrees) {
		this.transFeeFrees = transFeeFrees;
	}

	public List<TransfeeDTO> getTransFees() {
		return transFees;
	}

	public void setTransFees(List<TransfeeDTO> transFees) {
		this.transFees = transFees;
	}

	public Long getTransportId() {
		return transportId;
	}

	public void setTransportId(Long transportId) {
		this.transportId = transportId;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public Integer getIsFreeFee() {
		return isFreeFee;
	}

	public void setIsFreeFee(Integer isFreeFee) {
		this.isFreeFee = isFreeFee;
	}

	public Integer getHasFreeCondition() {
		return hasFreeCondition;
	}

	public void setHasFreeCondition(Integer hasFreeCondition) {
		this.hasFreeCondition = hasFreeCondition;
	}

	@Override
	public String toString() {
		return "TransportDTO{" +
				"transportId=" + transportId +
				",transName=" + transName +
				",shopId=" + shopId +
				",chargeType=" + chargeType +
				",isFreeFee=" + isFreeFee +
				",hasFreeCondition=" + hasFreeCondition +
				'}';
	}
}
