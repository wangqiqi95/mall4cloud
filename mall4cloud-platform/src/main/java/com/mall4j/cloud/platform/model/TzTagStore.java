package com.mall4j.cloud.platform.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 标签关联门店
 *
 * @author gmq
 * @date 2022-09-13 12:01:58
 */
public class TzTagStore implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 0正常 1删除
     */
    private Integer delFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "TzTagStore{" +
				"id=" + id +
				",tagId=" + tagId +
				",storeId=" + storeId +
				",delFlag=" + delFlag +
				'}';
	}
}
