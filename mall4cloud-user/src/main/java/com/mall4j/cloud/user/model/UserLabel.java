package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 导购标签信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
public class UserLabel extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 导购ID
     */
    private Long staffId;

    /**
     * 标签名称
     */
    private String labelName;

    /**
     * 标签类型 1手动 2系统
     */
    private Integer labelType;

    /**
     * 系统标签ID
     */
    private Long tagId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public Integer getLabelType() {
		return labelType;
	}

	public void setLabelType(Integer labelType) {
		this.labelType = labelType;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	@Override
	public String toString() {
		return "UserLabel{" +
				"id=" + id +
				",storeId=" + storeId +
				",staffId=" + staffId +
				",labelName=" + labelName +
				",labelType=" + labelType +
				",tagId=" + tagId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
