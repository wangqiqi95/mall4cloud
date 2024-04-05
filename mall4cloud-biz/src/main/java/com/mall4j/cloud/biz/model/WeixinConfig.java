package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信配置信息表
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 15:58:07
 */
public class WeixinConfig extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * key
     */
    private String paramKey;

    /**
     * value
     */
    private String paramValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 公众号原始id
     */
    private String appId;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 修改人
	 */
	private String updateBy;

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "WeixinConfig{" +
				"id=" + id +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",paramKey=" + paramKey +
				",paramValue=" + paramValue +
				",remark=" + remark +
				",appId=" + appId +
				'}';
	}
}
