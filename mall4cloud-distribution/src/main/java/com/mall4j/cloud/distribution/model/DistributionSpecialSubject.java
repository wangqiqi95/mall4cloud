package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-分销专题
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:56
 */
public class DistributionSpecialSubject extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 微页面ID
     */
    private Long pageId;

    /**
     * 微页面名称
     */
    private String pageName;

    /**
     * 专题名称
     */
    private String subjectName;

    /**
     * 可见开始时间
     */
    private Date startTime;

    /**
     * 可见结束时间
     */
    private Date endTime;

    /**
     * 宣传图地址
     */
    private String publicityImgUrl;

    /**
     * 选择门店类型 0全部门店 1部分门店
     */
    private Integer storeType;

    /**
     * 选择商品类型 0全部商品 1部分商品
     */
    private Integer productType;

    /**
     * 宣传文案
     */
    private String publicityText;

    /**
     * 分销类型 0全部 1导购 2威客
     */
    private Integer distributionType;

    /**
     * 状态 1启用 0禁用
     */
    private Integer status;

    /**
     * 是否自定义 1是 0否
     */
    private Integer isRec;

    /**
     * 推荐颜色
     */
    private String recColor;

    /**
     * 自定义颜色
     */
    private String customizeColor;

	/**
	 * 是否置顶
	 */
	private Integer top;

	/**
	 * 置顶时间
	 */
	private Date topTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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

	public String getPublicityImgUrl() {
		return publicityImgUrl;
	}

	public void setPublicityImgUrl(String publicityImgUrl) {
		this.publicityImgUrl = publicityImgUrl;
	}

	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public String getPublicityText() {
		return publicityText;
	}

	public void setPublicityText(String publicityText) {
		this.publicityText = publicityText;
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

	public Integer getIsRec() {
		return isRec;
	}

	public void setIsRec(Integer isRec) {
		this.isRec = isRec;
	}

	public String getRecColor() {
		return recColor;
	}

	public void setRecColor(String recColor) {
		this.recColor = recColor;
	}

	public String getCustomizeColor() {
		return customizeColor;
	}

	public void setCustomizeColor(String customizeColor) {
		this.customizeColor = customizeColor;
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

	@Override
	public String toString() {
		return "DistributionSpecialSubject{" +
				"id=" + id +
				", pageId=" + pageId +
				", pageName='" + pageName + '\'' +
				", subjectName='" + subjectName + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", publicityImgUrl='" + publicityImgUrl + '\'' +
				", storeType=" + storeType +
				", productType=" + productType +
				", publicityText='" + publicityText + '\'' +
				", distributionType=" + distributionType +
				", status=" + status +
				", isRec=" + isRec +
				", recColor='" + recColor + '\'' +
				", customizeColor='" + customizeColor + '\'' +
				", top=" + top +
				", topTime=" + topTime +
				'}';
	}
}
