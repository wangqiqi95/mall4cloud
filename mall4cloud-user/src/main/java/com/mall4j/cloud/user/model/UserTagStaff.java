package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 导购标签关系信息
 *
 * @author ZengFanChang
 * @date 2022-02-13 22:25:55
 */
public class UserTagStaff extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 导购ID
     */
    private Long staffId;

    /**
     * 标签ID
     */
    private Long tagId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	@Override
	public String toString() {
		return "UserTagStaff{" +
				"id=" + id +
				",staffId=" + staffId +
				",tagId=" + tagId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
