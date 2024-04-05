package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 素材 互动雷达标签VO
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
public class CpMaterialLableVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("素材id")
    private Long matId;

    @ApiModelProperty("浏览多少秒开始统计")
    private Integer interactionSecond;

    @ApiModelProperty("互动达标记录标签id")
    private String radarLabalId;

    @ApiModelProperty("互动达标记录标签name")
    private String radarLabalName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getMatId() {
		return matId;
	}

	public void setMatId(Long matId) {
		this.matId = matId;
	}

	public Integer getInteractionSecond() {
		return interactionSecond;
	}

	public void setInteractionSecond(Integer interactionSecond) {
		this.interactionSecond = interactionSecond;
	}

	public String getRadarLabalId() {
		return radarLabalId;
	}

	public void setRadarLabalId(String radarLabalId) {
		this.radarLabalId = radarLabalId;
	}

	public String getRadarLabalName() {
		return radarLabalName;
	}

	public void setRadarLabalName(String radarLabalName) {
		this.radarLabalName = radarLabalName;
	}

	@Override
	public String toString() {
		return "CpMaterialLableVO{" +
				"id=" + id +
				",matId=" + matId +
				",interactionSecond=" + interactionSecond +
				",radarLabalId=" + radarLabalId +
				",radarLabalName=" + radarLabalName +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
