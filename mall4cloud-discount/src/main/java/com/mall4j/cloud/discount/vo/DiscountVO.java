package com.mall4j.cloud.discount.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 满减满折优惠VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-16 16:25:09
 */
public class DiscountVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

	@JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("店铺logo")
    private String shopLogo;

    @ApiModelProperty("活动名称")
    private String discountName;

    @ApiModelProperty("枚举DiscountRule(0 满钱减钱 3满件打折)")
    private Integer discountRule;

    @ApiModelProperty("减免类型 0按满足最高层级减一次 1每满一次减一次")
    private Integer discountType;

    @ApiModelProperty("适用门店数")
    private Integer applyShopNum;

    @ApiModelProperty("是否全部门店 0 否 1是")
    private Integer isAllShop;

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

	@JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("手机端活动图片")
    private String mobilePic;

	@JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("pc端活动列表图片")
    private String pcPic;

	@JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("pc端活动背景图片")
    private String pcBackgroundPic;

    @ApiModelProperty("满减项")
    private List<DiscountItemVO> discountItemList;

	@ApiModelProperty("商品Id列表")
	private List<Long> spuIds;

    @ApiModelProperty("可用商品列表，如果为全部则为空")
    private List<SpuSearchVO> spuList;

	@ApiModelProperty(value = "在多少秒后过期")
	private Integer expiresIn;

	@ApiModelProperty(value = "在多少秒后开始")
	private Integer startIn;

    @ApiModelProperty("满减项")
    private List<DiscountShopVO> discountShops;

    public List<DiscountShopVO> getDiscountShops() {
        return discountShops;
    }

    public void setDiscountShops(List<DiscountShopVO> discountShops) {
        this.discountShops = discountShops;
    }

    public Integer getApplyShopNum() {
        return applyShopNum;
    }

    public void setApplyShopNum(Integer applyShopNum) {
        this.applyShopNum = applyShopNum;
    }

    public Integer getIsAllShop() {
        return isAllShop;
    }

    public void setIsAllShop(Integer isAllShop) {
        this.isAllShop = isAllShop;
    }

    public List<Long> getSpuIds() {
		return spuIds;
	}

	public void setSpuIds(List<Long> spuIds) {
		this.spuIds = spuIds;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<SpuSearchVO> getSpuList() {
		return spuList;
	}

	public void setSpuList(List<SpuSearchVO> spuList) {
		this.spuList = spuList;
	}

	public List<DiscountItemVO> getDiscountItemList() {
		return discountItemList;
	}

	public void setDiscountItemList(List<DiscountItemVO> discountItemList) {
		this.discountItemList = discountItemList;
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

	public String getShopLogo() {
		return shopLogo;
	}

	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Integer getStartIn() {
		return startIn;
	}

	public void setStartIn(Integer startIn) {
		this.startIn = startIn;
	}

	@Override
	public String toString() {
		return "DiscountVO{" +
				"discountId=" + discountId +
				", shopId=" + shopId +
				", shopName='" + shopName + '\'' +
				", shopLogo='" + shopLogo + '\'' +
				", discountName='" + discountName + '\'' +
				", discountRule=" + discountRule +
				", discountType=" + discountType +
				", suitableSpuType=" + suitableSpuType +
				", maxReduceAmount=" + maxReduceAmount +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", status=" + status +
				", mobilePic='" + mobilePic + '\'' +
				", pcPic='" + pcPic + '\'' +
				", pcBackgroundPic='" + pcBackgroundPic + '\'' +
				", discountItemList=" + discountItemList +
				", spuIds=" + spuIds +
				", spuList=" + spuList +
				", expiresIn=" + expiresIn +
				", startIn=" + startIn +
				'}';
	}
}
