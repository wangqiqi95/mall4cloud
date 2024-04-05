package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 导购标签用户信息VO
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
public class UserLabelUserVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("导购标签ID")
    private Long labelId;

    @ApiModelProperty("用户ID")
    private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserLabelUserVO{" +
				"id=" + id +
				",labelId=" + labelId +
				",userId=" + userId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
