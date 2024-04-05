package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 积分活动表
 *
 * @author FrozenWatermelon
 * @date 2022-04-14 15:16:46
 */
public class ScoreConvert extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long convertId;

    /**
     * 标题
     */
    private String convertTitle;

    /**
     * 积分兑换数
     */
    private Long convertScore;

    /**
     * 积分活动封面
     */
    private String convertUrl;

    /**
     * 限制兑换总数
     */
    private Long maxAmount;

    /**
     * 库存
     */
    private Integer stocks;

    /**
     * 每人限制兑换数
     */
    private Long personMaxAmount;

    /**
     * 兑换活动种类（0：积分换物/1：积分换券）
     */
    private Integer convertType;

    /**
     * 积分换券活动类型（0：积分兑礼/1：积分换券）
     */
    private Integer type;

    /**
     * 兑换状态（0：启用/1：停用）
     */
    private Integer convertStatus;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 是否全部门店
     */
    private Integer isAllShop;

    /**
     * 是否全部门店（兑换门店）
     */
    private Integer isAllConvertShop;

    /**
     * 是否全部门店（优惠券）
     */
    private Integer isAllCouponShop;

    /**
     * 描述
     */
    private String description;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品图片
     */
    private String commodityImgUrl;

    /**
     * 发货方式（0：邮寄/1：门店自取）
     */
    private Integer deliveryType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建人ID
     */
    private Long createId;

    /**
     * 
     */
    private String createName;

    /**
     * 修改人ID
     */
    private Long updateId;

    /**
     * 
     */
    private String updateName;

    /**
     * 是否删除（0：未删除/1：已删除）
     */
    private Integer del;

    /**
     * 版本号
     */
    private Integer version;

	public Long getConvertId() {
		return convertId;
	}

	public void setConvertId(Long convertId) {
		this.convertId = convertId;
	}

	public String getConvertTitle() {
		return convertTitle;
	}

	public void setConvertTitle(String convertTitle) {
		this.convertTitle = convertTitle;
	}

	public Long getConvertScore() {
		return convertScore;
	}

	public void setConvertScore(Long convertScore) {
		this.convertScore = convertScore;
	}

	public String getConvertUrl() {
		return convertUrl;
	}

	public void setConvertUrl(String convertUrl) {
		this.convertUrl = convertUrl;
	}

	public Long getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Long maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Integer getStocks() {
		return stocks;
	}

	public void setStocks(Integer stocks) {
		this.stocks = stocks;
	}

	public Long getPersonMaxAmount() {
		return personMaxAmount;
	}

	public void setPersonMaxAmount(Long personMaxAmount) {
		this.personMaxAmount = personMaxAmount;
	}

	public Integer getConvertType() {
		return convertType;
	}

	public void setConvertType(Integer convertType) {
		this.convertType = convertType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getConvertStatus() {
		return convertStatus;
	}

	public void setConvertStatus(Integer convertStatus) {
		this.convertStatus = convertStatus;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Integer getIsAllShop() {
		return isAllShop;
	}

	public void setIsAllShop(Integer isAllShop) {
		this.isAllShop = isAllShop;
	}

	public Integer getIsAllConvertShop() {
		return isAllConvertShop;
	}

	public void setIsAllConvertShop(Integer isAllConvertShop) {
		this.isAllConvertShop = isAllConvertShop;
	}

	public Integer getIsAllCouponShop() {
		return isAllCouponShop;
	}

	public void setIsAllCouponShop(Integer isAllCouponShop) {
		this.isAllCouponShop = isAllCouponShop;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityImgUrl() {
		return commodityImgUrl;
	}

	public void setCommodityImgUrl(String commodityImgUrl) {
		this.commodityImgUrl = commodityImgUrl;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
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

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "ScoreConvert{" +
				"convertId=" + convertId +
				",convertTitle=" + convertTitle +
				",convertScore=" + convertScore +
				",convertUrl=" + convertUrl +
				",maxAmount=" + maxAmount +
				",stocks=" + stocks +
				",personMaxAmount=" + personMaxAmount +
				",convertType=" + convertType +
				",type=" + type +
				",convertStatus=" + convertStatus +
				",sort=" + sort +
				",couponId=" + couponId +
				",isAllShop=" + isAllShop +
				",isAllConvertShop=" + isAllConvertShop +
				",isAllCouponShop=" + isAllCouponShop +
				",description=" + description +
				",commodityName=" + commodityName +
				",commodityImgUrl=" + commodityImgUrl +
				",deliveryType=" + deliveryType +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",createId=" + createId +
				",createName=" + createName +
				",createTime=" + createTime +
				",updateId=" + updateId +
				",updateName=" + updateName +
				",updateTime=" + updateTime +
				",del=" + del +
				",version=" + version +
				'}';
	}
}
