package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 导购标签用户信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
public class UserLabelUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 导购标签ID
     */
    private Long labelId;

    /**
     * 用户ID
     */
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
		return "UserLabelUser{" +
				"id=" + id +
				",labelId=" + labelId +
				",userId=" + userId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
