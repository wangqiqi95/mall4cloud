package com.mall4j.cloud.platform.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 触点内容信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public class TentacleContent extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 组织节点ID
     */
    private Long orgId;

	/**
	 * 触点号
	 */
	private String tentacleNo;

    /**
     * 触点ID
     */
    private Long tentacleId;

    /**
     * 内容标题
     */
    private String contentTitle;

    /**
     * 内容类型
     */
    private Integer contentType;

    /**
     * 内容唯一标识
     */
    private Long contentId;

    /**
     * 具体营销内容
     */
    private String content;

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

	public String getTentacleNo() {
		return tentacleNo;
	}

	public void setTentacleNo(String tentacleNo) {
		this.tentacleNo = tentacleNo;
	}

	public Long getTentacleId() {
		return tentacleId;
	}

	public void setTentacleId(Long tentacleId) {
		this.tentacleId = tentacleId;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		return "TentacleContent{" +
				"id=" + id +
				", orgId=" + orgId +
				", tentacleNo='" + tentacleNo + '\'' +
				", tentacleId=" + tentacleId +
				", contentTitle='" + contentTitle + '\'' +
				", contentType=" + contentType +
				", contentId=" + contentId +
				", content='" + content + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				'}';
	}
}
