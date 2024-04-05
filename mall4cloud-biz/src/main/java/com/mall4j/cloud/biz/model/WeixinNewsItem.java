package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信图文模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:35
 */
public class WeixinNewsItem extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private String id;

    /**
     * 图文id
     */
    private String newstemplateId;

    /**
     * 图文缩略图的media_id
     */
    private String thumbMediaId;

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 图片路径
     */
    private String imagePath;

    /**
     * 内容
     */
    private String content;

    /**
     * 摘要
     */
    private String abstracts;

    /**
     * 
     */
    private String description;

    /**
     * 素材顺序
     */
    private String orderNo;

    /**
     * 图文：news；外部url：url
     */
    private String newType;

    /**
     * 原文链接
     */
    private String url;

    /**
     * 外部链接
     */
    private String externalUrl;

    /**
     * 是否显示封面：'1':显示,'0':不显示
     */
    private String showCoverPic;

    /**
     * 小程序素材id
     */
    private String maTemplateId;

    /**
     * 创建人名称
     */
    private String createBy;

    /**
     * 修改人名称
     */
    private String updateBy;

    /**
     * 删除标识0-正常,1-已删除
     */
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewstemplateId() {
		return newstemplateId;
	}

	public void setNewstemplateId(String newstemplateId) {
		this.newstemplateId = newstemplateId;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getNewType() {
		return newType;
	}

	public void setNewType(String newType) {
		this.newType = newType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public String getShowCoverPic() {
		return showCoverPic;
	}

	public void setShowCoverPic(String showCoverPic) {
		this.showCoverPic = showCoverPic;
	}

	public String getMaTemplateId() {
		return maTemplateId;
	}

	public void setMaTemplateId(String maTemplateId) {
		this.maTemplateId = maTemplateId;
	}

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

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "WeixinNewsItem{" +
				"id=" + id +
				",newstemplateId=" + newstemplateId +
				",thumbMediaId=" + thumbMediaId +
				",title=" + title +
				",author=" + author +
				",imagePath=" + imagePath +
				",content=" + content +
				",abstracts=" + abstracts +
				",description=" + description +
				",orderNo=" + orderNo +
				",newType=" + newType +
				",url=" + url +
				",externalUrl=" + externalUrl +
				",showCoverPic=" + showCoverPic +
				",maTemplateId=" + maTemplateId +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
