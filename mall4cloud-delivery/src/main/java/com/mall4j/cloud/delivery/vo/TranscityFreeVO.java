package com.mall4j.cloud.delivery.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 指定条件包邮城市项VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class TranscityFreeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("指定条件包邮城市项id")
    private Long transcityFreeId;

    @ApiModelProperty("指定条件包邮项id")
    private Long transfeeFreeId;

    @ApiModelProperty("城市id")
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
		return "TranscityFreeVO{" +
				"transcityFreeId=" + transcityFreeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",transfeeFreeId=" + transfeeFreeId +
				",freeCityId=" + freeCityId +
				'}';
	}
}
