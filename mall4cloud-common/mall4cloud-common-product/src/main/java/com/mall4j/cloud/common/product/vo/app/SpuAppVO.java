package com.mall4j.cloud.common.product.vo.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.product.vo.SpuAttrValueVO;
import com.mall4j.cloud.common.product.vo.SpuActivityAppVO;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.product.model.TagActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.swing.text.html.HTMLDocument;
import java.util.List;

/**
 * spu信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
@ToString
public class SpuAppVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("spu id")
	private Long spuId;

	@ApiModelProperty("商品货号")
	private String spuCode;

	@ApiModelProperty("店铺id")
	private Long shopId;

	@ApiModelProperty("spu名称")
	private String spuName;

	@ApiModelProperty("卖点")
	private String sellingPoint;

	@ApiModelProperty("分类ID")
	private Long categoryId;

	@ApiModelProperty("店铺分类ID")
	private Long shopCategoryId;

	@JsonSerialize(using = ImgJsonSerializer.class)
	@ApiModelProperty("商品介绍主图")
	private String mainImgUrl;

	@JsonSerialize(using = ImgJsonSerializer.class)
	@ApiModelProperty("商品介绍主图 多个图片逗号分隔")
	private String imgUrls;

	@JsonSerialize(using = ImgJsonSerializer.class)
	@ApiModelProperty("商品视频")
	private String video;

	@ApiModelProperty("售价，整数方式保存")
	private Long priceFee;

	@ApiModelProperty("市场价，整数方式保存")
	private Long marketPriceFee;

	@ApiModelProperty("商品详情")
	private String detail;

	@ApiModelProperty("总库存")
	private Integer totalStock;

	@ApiModelProperty("总库存")
	private Integer channelsStock;

	@ApiModelProperty("规格属性")
	private List<SpuAttrValueAppVO> spuAttrValues;

	@ApiModelProperty("sku列表")
	private List<SkuAppVO> skus;

	@ApiModelProperty("配送方式json见TransportModeVO")
	private String deliveryMode;

	@ApiModelProperty("运费模板id")
	private Long deliveryTemplateId;

	@ApiModelProperty("商品类型(0普通商品 1拼团 2秒杀 3积分)")
	private Integer spuType;

	@ApiModelProperty("活动id(关联prod_type)")
	private Long activityId;

	@ApiModelProperty("是否为组合商品0普通商品，1组合商品")
	private Integer isCompose;

	@ApiModelProperty("商品销量")
	private Integer saleNum;

	@ApiModelProperty("评论数量")
	private Integer commentNum;

	@ApiModelProperty("商品活动信息(JSON)")
	private SpuActivityAppVO spuActivity;

	@ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
	private Integer status;

	@ApiModelProperty("营销标签")
	private TagActivity tagActivity;

	@ApiModelProperty("是否使用会员日活动价：true false")
	private boolean memberPriceFlag=false;

	@ApiModelProperty("是否使用虚拟门店活动价：true false")
	private boolean invateStorePriceFlag=false;

	@ApiModelProperty("在会员日活动中是否可以使用优惠券标识 0否1是")
	private Integer friendlyCouponUseFlag = 1;

	@ApiModelProperty("在会员日活动中是否可以参加满减活动标识 0否1是")
	private Integer friendlyDiscountFlag = 1;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMainImgUrl() {
		return mainImgUrl;
	}

	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
	}

	public List<SkuAppVO> getSkus() {
		return skus;
	}

	public void setSkus(List<SkuAppVO> skus) {
		this.skus = skus;
	}

	public List<SpuAttrValueAppVO> getSpuAttrValues() {
		return spuAttrValues;
	}

	public void setSpuAttrValues(List<SpuAttrValueAppVO> spuAttrValues) {
		this.spuAttrValues = spuAttrValues;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	public Long getMarketPriceFee() {
		return marketPriceFee;
	}

	public void setMarketPriceFee(Long marketPriceFee) {
		this.marketPriceFee = marketPriceFee;
	}

	public String getSellingPoint() {
		return sellingPoint;
	}

	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public Long getDeliveryTemplateId() {
		return deliveryTemplateId;
	}

	public void setDeliveryTemplateId(Long deliveryTemplateId) {
		this.deliveryTemplateId = deliveryTemplateId;
	}

	public Integer getSpuType() {
		return spuType;
	}

	public void setSpuType(Integer spuType) {
		this.spuType = spuType;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Integer getIsCompose() {
		return isCompose;
	}

	public void setIsCompose(Integer isCompose) {
		this.isCompose = isCompose;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public SpuActivityAppVO getSpuActivity() {
		return spuActivity;
	}

	public void setSpuActivity(SpuActivityAppVO spuActivity) {
		this.spuActivity = spuActivity;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getShopCategoryId() {
		return shopCategoryId;
	}

	public void setShopCategoryId(Long shopCategoryId) {
		this.shopCategoryId = shopCategoryId;
	}

}
