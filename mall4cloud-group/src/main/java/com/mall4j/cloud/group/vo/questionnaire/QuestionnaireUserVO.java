package com.mall4j.cloud.group.vo.questionnaire;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 问卷会员名单VO
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public class QuestionnaireUserVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("")
    private Long userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "QuestionnaireUserVO{" +
				"id=" + id +
				",activityId=" + activityId +
				",userId=" + userId +
				'}';
	}
}
