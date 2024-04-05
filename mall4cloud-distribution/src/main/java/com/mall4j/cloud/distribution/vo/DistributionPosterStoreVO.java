package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-海报门店VO
 *
 * @author ZengFanChang
 * @date 2022-01-03 20:00:28
 */
public class DistributionPosterStoreVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("海报ID")
    private Long posterId;

    @ApiModelProperty("门店ID")
    private Long storeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPosterId() {
		return posterId;
	}

	public void setPosterId(Long posterId) {
		this.posterId = posterId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Override
	public String toString() {
		return "DistributionPosterStoreVO{" +
				"id=" + id +
				",posterId=" + posterId +
				",storeId=" + storeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
