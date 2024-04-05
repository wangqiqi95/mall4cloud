package com.mall4j.cloud.delivery.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 运费项和运费城市关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class Transcity extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long transcityId;

    /**
     * 运费项id
     */
    private Long transfeeId;

    /**
     * 城市id
     */
    private Long cityId;

	public Long getTranscityId() {
		return transcityId;
	}

	public void setTranscityId(Long transcityId) {
		this.transcityId = transcityId;
	}

	public Long getTransfeeId() {
		return transfeeId;
	}

	public void setTransfeeId(Long transfeeId) {
		this.transfeeId = transfeeId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@Override
	public String toString() {
		return "Transcity{" +
				"transcityId=" + transcityId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",transfeeId=" + transfeeId +
				",cityId=" + cityId +
				'}';
	}
}
