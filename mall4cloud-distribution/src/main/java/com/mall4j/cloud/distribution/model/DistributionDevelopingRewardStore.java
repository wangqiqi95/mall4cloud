package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-发展奖励门店
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:38:24
 */
public class DistributionDevelopingRewardStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 发展奖励活动ID
     */
    private Long developingRewardId;

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

	public Long getDevelopingRewardId() {
		return developingRewardId;
	}

	public void setDevelopingRewardId(Long developingRewardId) {
		this.developingRewardId = developingRewardId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Override
	public String toString() {
		return "DistributionDevelopingRewardStore{" +
				"id=" + id +
				",developingRewardId=" + developingRewardId +
				",storeId=" + storeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
