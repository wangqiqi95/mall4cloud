package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

/**
 * 满减满折优惠VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public class DiscountOrderVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("活动名称")
    private String discountName;

    @ApiModelProperty("枚举DiscountRule(0 满钱减钱 3满件打折)")
    private Integer discountRule;

    @ApiModelProperty("减免类型 0按满足最高层级减一次 1每满一次减一次")
    private Integer discountType;

    @ApiModelProperty("适用商品类型 0全部商品参与 1指定商品参与")
    private Integer suitableSpuType;

    @ApiModelProperty("最多减多少")
    private Long maxReduceAmount;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("活动状态:(0:关闭、1:开启、2:违规下线、3:平台审核)")
    private Integer status;

    @ApiModelProperty("手机端活动图片")
    private String mobilePic;

    @ApiModelProperty("pc端活动列表图片")
    private String pcPic;

    @ApiModelProperty("pc端活动背景图片")
    private String pcBackgroundPic;

	@ApiModelProperty("活动项")
	private List<DiscountItemOrderVO> discountItems;


	@ApiModelProperty("可用商品列表，如果为全部则为空")
	private List<Long> spuIds;

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


	@Override
	public String toString() {
		return "DiscountVO{" +
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

	public List<DiscountItemOrderVO> getDiscountItems() {
		return discountItems;
	}

	public void setDiscountItems(List<DiscountItemOrderVO> discountItems) {
		this.discountItems = discountItems;
	}

	public List<Long> getSpuIds() {
		return spuIds;
	}

	public void setSpuIds(List<Long> spuIds) {
		this.spuIds = spuIds;
	}
}
