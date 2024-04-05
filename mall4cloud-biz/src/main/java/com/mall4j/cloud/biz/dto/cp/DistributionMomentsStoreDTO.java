package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分销推广-朋友圈门店DTO
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public class DistributionMomentsStoreDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("朋友圈ID")
    private Long momentsId;

    @ApiModelProperty("门店ID")
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
		return "DistributionMomentsStoreDTO{" +
				"id=" + id +
				",momentsId=" + momentsId +
				",storeId=" + storeId +
				'}';
	}
}
