package com.mall4j.cloud.delivery.vo;

import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 指定条件包邮项VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class TransfeeFreeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("指定条件包邮项id")
    private Long transfeeFreeId;

    @ApiModelProperty("运费模板id")
    private Long transportId;

    @ApiModelProperty("包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）")
    private Integer freeType;

    @ApiModelProperty("需满金额")
    private Long amount;

    @ApiModelProperty("包邮x件/重量/体积")
    private Double piece;

	@ApiModelProperty(value = "指定条件包邮城市项",required=true)
	private List<AreaVO> freeCityList;

	public List<AreaVO> getFreeCityList() {
		return freeCityList;
	}

	public void setFreeCityList(List<AreaVO> freeCityList) {
		this.freeCityList = freeCityList;
	}

	public Long getTransfeeFreeId() {
		return transfeeFreeId;
	}

	public void setTransfeeFreeId(Long transfeeFreeId) {
		this.transfeeFreeId = transfeeFreeId;
	}

	public Long getTransportId() {
		return transportId;
	}

	public void setTransportId(Long transportId) {
		this.transportId = transportId;
	}

	public Integer getFreeType() {
		return freeType;
	}

	public void setFreeType(Integer freeType) {
		this.freeType = freeType;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Double getPiece() {
		return piece;
	}

	public void setPiece(Double piece) {
		this.piece = piece;
	}

	@Override
	public String toString() {
		return "TransfeeFreeVO{" +
				"transfeeFreeId=" + transfeeFreeId +
				", transportId=" + transportId +
				", freeType=" + freeType +
				", amount=" + amount +
				", piece=" + piece +
				", freeCityList=" + freeCityList +
				'}';
	}
}
