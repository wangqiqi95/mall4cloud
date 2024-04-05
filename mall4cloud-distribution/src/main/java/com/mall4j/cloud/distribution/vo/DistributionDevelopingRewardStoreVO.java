package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-发展奖励门店VO
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:38:24
 */
public class DistributionDevelopingRewardStoreVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("发展奖励活动ID")
    private Long developingRewardId;

    @ApiModelProperty("门店ID")
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
		return "DistributionDevelopingRewardStoreVO{" +
				"id=" + id +
				",developingRewardId=" + developingRewardId +
				",storeId=" + storeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
