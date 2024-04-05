package com.mall4j.cloud.platform.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 触点信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public class Tentacle extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 组织节点ID
     */
    private Long orgId;

    /**
     * 触点名称
     */
    private String tentacleName;

    /**
     * 触点code
     */
    private String tentacleCode;

	/**
	 * 业务ID 门店ID 会员ID 员工ID
	 */
	private Long businessId;

    /**
     * 触点类型 1门店 2会员 3威客 4导购
     */
    private Integer tentacleType;

    /**
     * 导购ID
     */
    private Long guideId;

    /**
     * 描述
     */
    private String description;

    /**
     * 1正常 0冻结
     */
    private Integer status;

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

	public String getTentacleName() {
		return tentacleName;
	}

	public void setTentacleName(String tentacleName) {
		this.tentacleName = tentacleName;
	}

	public String getTentacleCode() {
		return tentacleCode;
	}

	public void setTentacleCode(String tentacleCode) {
		this.tentacleCode = tentacleCode;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Integer getTentacleType() {
		return tentacleType;
	}

	public void setTentacleType(Integer tentacleType) {
		this.tentacleType = tentacleType;
	}

	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Tentacle{" +
				"id=" + id +
				", orgId=" + orgId +
				", tentacleName='" + tentacleName + '\'' +
				", tentacleCode='" + tentacleCode + '\'' +
				", businessId=" + businessId +
				", tentacleType=" + tentacleType +
				", guideId=" + guideId +
				", description='" + description + '\'' +
				", status=" + status +
				'}';
	}
}
