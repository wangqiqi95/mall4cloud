package com.mall4j.cloud.biz.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;

/**
 *
 *
 * @author YXF
 * @date 2020-12-04 16:15:02
 */
public class AttachFileGroup extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long attachFileGroupId;

    /**
     * 店铺id
     */
    private Long shopId;

	/**
	 * 用户uid
	 */
	private Long uid;

	/**
     * 分组名称
     */
    private String name;

	/**
	 * 1:图片 2:视频 3:文件
	 */
	private Integer type;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getAttachFileGroupId() {
		return attachFileGroupId;
	}

	public void setAttachFileGroupId(Long attachFileGroupId) {
		this.attachFileGroupId = attachFileGroupId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AttachFileGroup{" +
				"attachFileGroupId=" + attachFileGroupId +
				", shopId=" + shopId +
				", uid=" + uid +
				", name='" + name + '\'' +
				", type=" + type +
				'}';
	}
}
