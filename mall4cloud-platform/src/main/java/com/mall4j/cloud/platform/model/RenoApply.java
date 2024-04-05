package com.mall4j.cloud.platform.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 装修适用门店
 *
 * @author FrozenWatermelon
 * @date 2022-01-27 02:00:54
 */
public class RenoApply extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 装修id
     */
    private Long renoId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 0-未删除，1-已删除
     */
    private Integer isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRenoId() {
		return renoId;
	}

	public void setRenoId(Long renoId) {
		this.renoId = renoId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "RenoApply{" +
				"id=" + id +
				",renoId=" + renoId +
				",storeId=" + storeId +
				",isDeleted=" + isDeleted +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
