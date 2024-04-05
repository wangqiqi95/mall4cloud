package com.mall4j.cloud.delivery.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 运费项和运费城市关联信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class TranscityVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty()
    private Long transcityId;

    @ApiModelProperty("运费项id")
    private Long transfeeId;

    @ApiModelProperty("城市id")
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
		return "TranscityVO{" +
				"transcityId=" + transcityId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",transfeeId=" + transfeeId +
				",cityId=" + cityId +
				'}';
	}
}
