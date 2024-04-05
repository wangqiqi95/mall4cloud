package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 触点作业批次VO
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public class TentacleMarketingPlanVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("组织节点ID")
    private Long orgId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("批次作业描述")
    private String description;

    @ApiModelProperty("具体营销内容")
    private String content;

    @ApiModelProperty("内容标题")
    private String contentName;

    @ApiModelProperty("内容id")
    private Long contentId;

    @ApiModelProperty("内容类型 1:公众号关注二维码,2:激活领卡")
    private Integer contentType;

    @ApiModelProperty("关联触点类型")
    private Integer tentacleType;

    @ApiModelProperty("关联触点")
    private String tentacles;

    @ApiModelProperty("二维码尺寸")
    private String qrcodeSize;

    @ApiModelProperty("接受二维码邮箱")
    private String qrcodeMailbox;

    @ApiModelProperty("下载二维码zip包地址")
    private String qrcodeUrl;

    @ApiModelProperty("二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值")
    private String qrcodeType;

    @ApiModelProperty("二维码生效开始时间")
    private Date qrcodeStartTime;

    @ApiModelProperty("二维码生效结束时间")
    private Date qrcodeEndTime;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public Integer getTentacleType() {
		return tentacleType;
	}

	public void setTentacleType(Integer tentacleType) {
		this.tentacleType = tentacleType;
	}

	public String getTentacles() {
		return tentacles;
	}

	public void setTentacles(String tentacles) {
		this.tentacles = tentacles;
	}

	public String getQrcodeSize() {
		return qrcodeSize;
	}

	public void setQrcodeSize(String qrcodeSize) {
		this.qrcodeSize = qrcodeSize;
	}

	public String getQrcodeMailbox() {
		return qrcodeMailbox;
	}

	public void setQrcodeMailbox(String qrcodeMailbox) {
		this.qrcodeMailbox = qrcodeMailbox;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public String getQrcodeType() {
		return qrcodeType;
	}

	public void setQrcodeType(String qrcodeType) {
		this.qrcodeType = qrcodeType;
	}

	public Date getQrcodeStartTime() {
		return qrcodeStartTime;
	}

	public void setQrcodeStartTime(Date qrcodeStartTime) {
		this.qrcodeStartTime = qrcodeStartTime;
	}

	public Date getQrcodeEndTime() {
		return qrcodeEndTime;
	}

	public void setQrcodeEndTime(Date qrcodeEndTime) {
		this.qrcodeEndTime = qrcodeEndTime;
	}

	@Override
	public String toString() {
		return "TentacleMarketingPlanVO{" +
				"id=" + id +
				",orgId=" + orgId +
				",title=" + title +
				",description=" + description +
				",content=" + content +
				",contentName=" + contentName +
				",contentId=" + contentId +
				",contentType=" + contentType +
				",tentacleType=" + tentacleType +
				",tentacles=" + tentacles +
				",qrcodeSize=" + qrcodeSize +
				",qrcodeMailbox=" + qrcodeMailbox +
				",qrcodeUrl=" + qrcodeUrl +
				",qrcodeType=" + qrcodeType +
				",qrcodeStartTime=" + qrcodeStartTime +
				",qrcodeEndTime=" + qrcodeEndTime +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
