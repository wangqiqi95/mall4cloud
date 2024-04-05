package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-朋友圈门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public class DistributionMomentsStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 门店ID
     */
    private Long storeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMomentsId() {
		return momentsId;
	}

	public void setMomentsId(Long momentsId) {
		this.momentsId = momentsId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Override
	public String toString() {
		return "DistributionMomentsStore{" +
				"id=" + id +
				",momentsId=" + momentsId +
				",storeId=" + storeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
