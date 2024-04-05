package com.mall4j.cloud.delivery.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 指定条件包邮城市项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class TranscityFree extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 指定条件包邮城市项id
     */
    private Long transcityFreeId;

    /**
     * 指定条件包邮项id
     */
    private Long transfeeFreeId;

    /**
     * 城市id
     */
    private Long freeCityId;

	public Long getTranscityFreeId() {
		return transcityFreeId;
	}

	public void setTranscityFreeId(Long transcityFreeId) {
		this.transcityFreeId = transcityFreeId;
	}

	public Long getTransfeeFreeId() {
		return transfeeFreeId;
	}

	public void setTransfeeFreeId(Long transfeeFreeId) {
		this.transfeeFreeId = transfeeFreeId;
	}

	public Long getFreeCityId() {
		return freeCityId;
	}

	public void setFreeCityId(Long freeCityId) {
		this.freeCityId = freeCityId;
	}

	@Override
	public String toString() {
		return "TranscityFree{" +
				"transcityFreeId=" + transcityFreeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",transfeeFreeId=" + transfeeFreeId +
				",freeCityId=" + freeCityId +
				'}';
	}
}
