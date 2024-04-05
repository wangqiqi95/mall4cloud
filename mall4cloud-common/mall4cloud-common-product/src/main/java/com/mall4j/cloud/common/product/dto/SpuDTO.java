package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * spu信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SpuDTO{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("spuId")
	private Long spuId;

	@ApiModelProperty("品牌ID")
	private Long brandId;

	@NotNull(message = "分类不能为空")
	@ApiModelProperty("分类ID")
	private Long categoryId;

	@NotNull(message = "平台一级分类不能为空")
	@ApiModelProperty(value = "平台一级分类id")
	private Long primaryCategoryId;

	@NotNull(message = "平台二级分类不能为空")
	@ApiModelProperty(value = "平台二级分类id")
	private Long secondaryCategoryId;


	@ApiModelProperty("店铺分类ID")
	private Long shopCategoryId;

	@NotNull(message = "商品国际化信息不能为空")
	@ApiModelProperty("店铺分类ID")
	private List<SpuLangDTO> spuLangList;

	@ApiModelProperty("spu名称")
	private String name;

	@ApiModelProperty("卖点")
	private String sellingPoint;

	@NotNull(message = "商品轮播图不能为空")
	@ApiModelProperty("商品介绍主图 多个图片逗号分隔")
	private String imgUrls;

	@NotNull(message = "商品主图不能为空")
	@ApiModelProperty("商品主图")
	private String mainImgUrl;

	@ApiModelProperty("市场价")
	private Long marketPriceFee;

	@NotNull(message = "售价不能为空")
	@ApiModelProperty("售价")
	private Long priceFee;

	@ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
	private Integer status;

	@ApiModelProperty("商品属性值列表")
	private List<SpuAttrValueDTO> spuAttrValues;

	@NotEmpty(message = "sku信息不能为空")
	@ApiModelProperty("商品规格列表")
	private List<SkuDTO> skuList;

	@ApiModelProperty("商品详情")
	private String detail;

	@NotNull(message = "总库存不能为空")
	@ApiModelProperty("总库存")
	private Integer totalStock;

	@NotNull(message = "配送方式不能为空")
	@ApiModelProperty("配送方式json见TransportModeVO")
	private String deliveryMode;

	@NotNull(message = "运费模板不能为空")
	@ApiModelProperty("运费模板id")
	private Long deliveryTemplateId;

	@ApiModelProperty("商品类型(0普通商品 1拼团 2秒杀 3积分)")
	private Integer spuType;

	@ApiModelProperty("活动id(关联prod_type)")
	private Long activityId;

	@ApiModelProperty("是否为组合商品0普通商品，1组合商品")
	private Integer isCompose;

	@ApiModelProperty("商品视频")
	private String video;

	@ApiModelProperty("积分价格")
	private Long scoreFee;

	@ApiModelProperty("sku是否含有图片 0无 1有")
	private Integer hasSkuImg;

	@ApiModelProperty("分组id")
	private Long tagId;

	@ApiModelProperty("序号")
	private Integer seq;

	@ApiModelProperty("spuId列表(商品上下架：批量操作时，用此参数)(批量处理参数)")
	private List<Long> spuIds;

	@ApiModelProperty("店铺id")
	private Long shopId;
	@ApiModelProperty("是否限购  默认不限购 0-不限购 1- 限购")
	private Boolean isLimit;
	@ApiModelProperty("限制数量")
	private Integer limitNumber;
	@ApiModelProperty("限制方式 1-每人限购 2-每次限购")
	private Integer limitType;

	private String spuCode;

	@Size(max = 10,message = "简称最大10个字")
	private String abbr;

	@ApiModelProperty("活动筛选类型(多选逗号分隔): 1限时调价 2会员日 3拼团")
	private String searchActivityType;

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public Long getScoreFee() {
		return scoreFee;
	}

	public void setScoreFee(Long scoreFee) {
		this.scoreFee = scoreFee;
	}

	public List<SpuAttrValueDTO> getSpuAttrValues() {
		return spuAttrValues;
	}

	public void setSpuAttrValues(List<SpuAttrValueDTO> spuAttrValues) {
		this.spuAttrValues = spuAttrValues;
	}

	public List<SkuDTO> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<SkuDTO> skuList) {
		this.skuList = skuList;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public String getSellingPoint() {
		return sellingPoint;
	}

	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
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

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Long getMarketPriceFee() {
		return marketPriceFee;
	}

	public void setMarketPriceFee(Long marketPriceFee) {
		this.marketPriceFee = marketPriceFee;
	}

	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	public String getMainImgUrl() {
		return mainImgUrl;
	}

	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
	}

	public Integer getHasSkuImg() {
		return hasSkuImg;
	}

	public void setHasSkuImg(Integer hasSkuImg) {
		this.hasSkuImg = hasSkuImg;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public List<Long> getSpuIds() {
		return spuIds;
	}

	public void setSpuIds(List<Long> spuIds) {
		this.spuIds = spuIds;
	}

	public List<SpuLangDTO> getSpuLangList() {
		return spuLangList;
	}

	public void setSpuLangList(List<SpuLangDTO> spuLangList) {
		this.spuLangList = spuLangList;
	}

	@Override
	public String toString() {
		return "SpuDTO{" +
				"spuId=" + spuId +
				", brandId=" + brandId +
				", categoryId=" + categoryId +
				", shopCategoryId=" + shopCategoryId +
				", spuLangList='" + spuLangList +
				", name='" + name + '\'' +
				", sellingPoint='" + sellingPoint + '\'' +
				", imgUrls='" + imgUrls + '\'' +
				", mainImgUrl='" + mainImgUrl + '\'' +
				", marketPriceFee=" + marketPriceFee +
				", priceFee=" + priceFee +
				", status=" + status +
				", spuAttrValues=" + spuAttrValues +
				", skuList=" + skuList +
				", detail='" + detail + '\'' +
				", totalStock=" + totalStock +
				", deliveryMode='" + deliveryMode + '\'' +
				", deliveryTemplateId=" + deliveryTemplateId +
				", spuType=" + spuType +
				", activityId=" + activityId +
				", isCompose=" + isCompose +
				", video='" + video + '\'' +
				", scoreFee=" + scoreFee +
				", hasSkuImg=" + hasSkuImg +
				", tagId=" + tagId +
				", seq=" + seq +
				", spuIds=" + spuIds +
				", shopId=" + shopId +
				'}';
	}
}
