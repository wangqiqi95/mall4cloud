package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.order.dto.SubmitOrderDTO;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 拼团订单
 *
 * @author FrozenWatermelon
 * @date 2021-04-07 10:39:32
 */
public class SubmitGroupOrderDTO extends SubmitOrderDTO {

	@ApiModelProperty(value = "拼团团队id，（如果用户为参团则需要填写对应的拼团团队Id(groupTeamId)，如果为用户为开团,拼团团队Id(groupTeamId)为0）")
	@NotNull(message = "拼团团队id不能为空")
	private Long groupTeamId;


	public Long getGroupTeamId() {
		return groupTeamId;
	}

	public void setGroupTeamId(Long groupTeamId) {
		this.groupTeamId = groupTeamId;
	}

	@Override
	public String toString() {
		return "GroupOrderDTO{" +
				"groupTeamId=" + groupTeamId +
				'}';
	}
}
