package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-推广海报
 *
 * @author ZengFanChang
 * @date 2021-12-20 20:26:44
 */
public class DistributionPoster extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 海报类型 1商品 2微页面 3店铺 4发展
     */
    private Integer posterType;

    /**
     * 素材ID
     */
    private Long materialId;

    /**
     * 海报名称
     */
    private String posterName;

    /**
     * 海报权重
     */
    private Integer posterIndex;

	/**
	 * 显示类型 1分享海报 2生日图文
	 */
	private Integer showType;

    /**
     * 宣传文案
     */
    private String publicityText;

    /**
     * 备注
     */
    private String remark;

    /**
     * 宣传图地址
     */
    private String publicityImgUrl;

	/**
	 * 门店类型 0全部门店 1指定门店
	 */
	private Integer storeType;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
    private Date endTime;

    /**
     * 状态 1启用 0禁用
     */
    private Integer status;

	/**
	 * 门店id集合
	 */
	private List<Long> storeIdList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPosterType() {
		return posterType;
	}

	public void setPosterType(Integer posterType) {
		this.posterType = posterType;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public String getPosterName() {
		return posterName;
	}

	public void setPosterName(String posterName) {
		this.posterName = posterName;
	}

	public Integer getPosterIndex() {
		return posterIndex;
	}

	public void setPosterIndex(Integer posterIndex) {
		this.posterIndex = posterIndex;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public String getPublicityText() {
		return publicityText;
	}

	public void setPublicityText(String publicityText) {
		this.publicityText = publicityText;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Long> getStoreIdList() {
		return storeIdList;
	}

	public void setStoreIdList(List<Long> storeIdList) {
		this.storeIdList = storeIdList;
	}

	@Override
	public String toString() {
		return "DistributionPoster{" +
				"id=" + id +
				", posterType=" + posterType +
				", materialId=" + materialId +
				", posterName='" + posterName + '\'' +
				", posterIndex=" + posterIndex +
				", showType=" + showType +
				", publicityText='" + publicityText + '\'' +
				", remark='" + remark + '\'' +
				", publicityImgUrl='" + publicityImgUrl + '\'' +
				", storeType=" + storeType +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", status=" + status +
				'}';
	}
}
