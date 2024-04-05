package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 导购标签关系信息VO
 *
 * @author ZengFanChang
 * @date 2022-02-13 22:25:55
 */
public class UserTagStaffVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("导购ID")
    private Long staffId;

    @ApiModelProperty("标签ID")
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
		return "UserTagStaffVO{" +
				"id=" + id +
				",staffId=" + staffId +
				",tagId=" + tagId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
