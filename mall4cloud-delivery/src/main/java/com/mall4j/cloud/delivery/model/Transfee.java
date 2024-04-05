package com.mall4j.cloud.delivery.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 运费项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class Transfee extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 运费项id
     */
    private Long transfeeId;

    /**
     * 运费模板id
     */
    private Long transportId;

    /**
     * 续件数量
     */
    private Double continuousPiece;

    /**
     * 首件数量
     */
    private Double firstPiece;

    /**
     * 续件费用
     */
    private Long continuousFee;

    /**
     * 首件费用
     */
    private Long firstFee;

	public Long getTransfeeId() {
		return transfeeId;
	}

	public void setTransfeeId(Long transfeeId) {
		this.transfeeId = transfeeId;
	}

	public Long getTransportId() {
		return transportId;
	}

	public void setTransportId(Long transportId) {
		this.transportId = transportId;
	}

	public Double getContinuousPiece() {
		return continuousPiece;
	}

	public void setContinuousPiece(Double continuousPiece) {
		this.continuousPiece = continuousPiece;
	}

	public Double getFirstPiece() {
		return firstPiece;
	}

	public void setFirstPiece(Double firstPiece) {
		this.firstPiece = firstPiece;
	}

	public Long getContinuousFee() {
		return continuousFee;
	}

	public void setContinuousFee(Long continuousFee) {
		this.continuousFee = continuousFee;
	}

	public Long getFirstFee() {
		return firstFee;
	}

	public void setFirstFee(Long firstFee) {
		this.firstFee = firstFee;
	}

	@Override
	public String toString() {
		return "Transfee{" +
				"transfeeId=" + transfeeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",transportId=" + transportId +
				",continuousPiece=" + continuousPiece +
				",firstPiece=" + firstPiece +
				",continuousFee=" + continuousFee +
				",firstFee=" + firstFee +
				'}';
	}
}
