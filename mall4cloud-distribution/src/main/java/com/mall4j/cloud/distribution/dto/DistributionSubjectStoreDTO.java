package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-专题门店DTO
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public class DistributionSubjectStoreDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("专题ID")
    private Long subjectId;

    @ApiModelProperty("门店ID")
    private Long storeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Override
	public String toString() {
		return "DistributionSubjectStoreDTO{" +
				"id=" + id +
				",subjectId=" + subjectId +
				",storeId=" + storeId +
				'}';
	}
}
