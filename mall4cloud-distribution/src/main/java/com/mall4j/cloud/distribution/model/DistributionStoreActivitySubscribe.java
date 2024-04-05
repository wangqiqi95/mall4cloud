package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 门店活动-用户订阅
 *
 * @author gww
 * @date 2022-01-28 23:24:49
 */
public class DistributionStoreActivitySubscribe extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 会员id
     */
    private Long userId;

    /**
     * 是否订阅 0-否 1-是
     */
    private Integer isSubscribe;

    /**
     * 省编码
     */
    private String provinceCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(Integer isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	@Override
	public String toString() {
		return "DistributionStoreActivitySubscribe{" +
				"id=" + id +
				",orgId=" + orgId +
				",userId=" + userId +
				",isSubscribe=" + isSubscribe +
				",provinceCode=" + provinceCode +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
