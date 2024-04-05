package com.mall4j.cloud.discount.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 满减满折优惠
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public class Discount extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 满减满折优惠id
     */
    private Long discountId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 活动名称
     */
    private String discountName;

    /**
     * 枚举DiscountRule(0 满钱减钱 3满件打折)
     */
    private Integer discountRule;

    /**
     * 减免类型 0按满足最高层级减一次 1每满一次减一次
     */
    private Integer discountType;

    /**
     * 适用商品类型 0全部商品参与 1指定商品参与
     */
    private Integer suitableSpuType;

    /**
     * 最多减多少
     */
    private Long maxReduceAmount;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 活动状态:(0:关闭、1:开启、2:违规下线、3:平台审核)
     */
    private Integer status;

    /**
     * 手机端活动图片
     */
    private String mobilePic;

    /**
     * pc端活动列表图片
     */
    private String pcPic;

    /**
     * pc端活动背景图片
     */
    private String pcBackgroundPic;

	/**
	 * 满减满折优惠项
	 */
	private List<DiscountItem> discountItems;

	/**
	 * 满减满折商品
	 */
	private List<DiscountSpu> discountSpus;

    /**
     * 是否全部门店 0 否 1是
     */
    private Integer isAllShop;

    public Integer getIsAllShop() {
        return isAllShop;
    }

    public void setIsAllShop(Integer isAllShop) {
        this.isAllShop = isAllShop;
    }

    public Long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}

	public Integer getDiscountRule() {
		return discountRule;
	}

	public void setDiscountRule(Integer discountRule) {
		this.discountRule = discountRule;
	}

	public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	public Integer getSuitableSpuType() {
		return suitableSpuType;
	}

	public void setSuitableSpuType(Integer suitableSpuType) {
		this.suitableSpuType = suitableSpuType;
	}

	public Long getMaxReduceAmount() {
		return maxReduceAmount;
	}

	public void setMaxReduceAmount(Long maxReduceAmount) {
		this.maxReduceAmount = maxReduceAmount;
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

	public String getMobilePic() {
		return mobilePic;
	}

	public void setMobilePic(String mobilePic) {
		this.mobilePic = mobilePic;
	}

	public String getPcPic() {
		return pcPic;
	}

	public void setPcPic(String pcPic) {
		this.pcPic = pcPic;
	}

	public String getPcBackgroundPic() {
		return pcBackgroundPic;
	}

	public void setPcBackgroundPic(String pcBackgroundPic) {
		this.pcBackgroundPic = pcBackgroundPic;
	}

	public List<DiscountItem> getDiscountItems() {
		return discountItems;
	}

	public void setDiscountItems(List<DiscountItem> discountItems) {
		this.discountItems = discountItems;
	}

	public List<DiscountSpu> getDiscountSpus() {
		return discountSpus;
	}

	public void setDiscountSpus(List<DiscountSpu> discountSpus) {
		this.discountSpus = discountSpus;
	}

	@Override
	public String toString() {
		return "Discount{" +
				"discountId=" + discountId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",discountName=" + discountName +
				",discountRule=" + discountRule +
				",discountType=" + discountType +
				",suitableSpuType=" + suitableSpuType +
				",maxReduceAmount=" + maxReduceAmount +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",status=" + status +
				",mobilePic=" + mobilePic +
				",pcPic=" + pcPic +
				",pcBackgroundPic=" + pcBackgroundPic +
				'}';
	}
}
