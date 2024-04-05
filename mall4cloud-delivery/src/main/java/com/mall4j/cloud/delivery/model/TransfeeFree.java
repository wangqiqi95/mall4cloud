package com.mall4j.cloud.delivery.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 指定条件包邮项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class TransfeeFree extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 指定条件包邮项id
     */
    private Long transfeeFreeId;

    /**
     * 运费模板id
     */
    private Long transportId;

    /**
     * 包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
     */
    private Integer freeType;

    /**
     * 需满金额
     */
    private Long amount;

    /**
     * 包邮x件/重量/体积
     */
    private Double piece;

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
		return "TransfeeFree{" +
				"transfeeFreeId=" + transfeeFreeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",transportId=" + transportId +
				",freeType=" + freeType +
				",amount=" + amount +
				",piece=" + piece +
				'}';
	}
}
