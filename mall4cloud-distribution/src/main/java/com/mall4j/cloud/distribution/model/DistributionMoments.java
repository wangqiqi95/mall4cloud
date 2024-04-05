package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public class DistributionMoments extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

	@ApiModelProperty("标题")
    private String title;

	@ApiModelProperty("可见开始时间")
    private Date startTime;

	@ApiModelProperty("可见结束时间")
    private Date endTime;

	@ApiModelProperty("头像地址")
    private String avatarUrl;

	@ApiModelProperty("文案")
    private String descHtml;

	@ApiModelProperty("评论")
    private String comment;

	@ApiModelProperty("选择商品类型 0全部商品 1部分商品")
    private Integer productType;

	@ApiModelProperty("选择门店类型 0全部门店 1部分门店")
    private Integer storeType;

	@ApiModelProperty("分销类型 0全部 1导购 2威客")
    private Integer distributionType;

	@ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

	@ApiModelProperty("是否置顶 1是 0否")
	private Integer top;

	@ApiModelProperty("置顶时间")
	private Date topTime;

	@ApiModelProperty("朋友圈商品集合")
	private List<DistributionMomentsProduct> productList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getDescHtml() {
		return descHtml;
	}

	public void setDescHtml(String descHtml) {
		this.descHtml = descHtml;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	public Integer getDistributionType() {
		return distributionType;
	}

	public void setDistributionType(Integer distributionType) {
		this.distributionType = distributionType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Date getTopTime() {
		return topTime;
	}

	public void setTopTime(Date topTime) {
		this.topTime = topTime;
	}

	public List<DistributionMomentsProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<DistributionMomentsProduct> productList) {
		this.productList = productList;
	}

	@Override
	public String toString() {
		return "DistributionMoments{" +
				"id=" + id +
				", title='" + title + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", avatarUrl='" + avatarUrl + '\'' +
				", descHtml='" + descHtml + '\'' +
				", comment='" + comment + '\'' +
				", productType=" + productType +
				", storeType=" + storeType +
				", distributionType=" + distributionType +
				", status=" + status +
				", top=" + top +
				", topTime=" + topTime +
				'}';
	}
}
