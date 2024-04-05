package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * spu信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SpuVO extends BaseVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("spu id")
	private Long spuId;

	@ApiModelProperty("品牌ID")
	private Long brandId;

	@ApiModelProperty("分类ID")
	private Long categoryId;

	@ApiModelProperty("店铺分类ID")
	private Long shopCategoryId;

	@ApiModelProperty("店铺id")
	private Long shopId;

	@ApiModelProperty("多语言信息列表")
	List<SpuLangVO> spuLangList;

	@ApiModelProperty("spu名称")
	private String name;

	@ApiModelProperty("卖点")
	private String sellingPoint;

	@ApiModelProperty("商品介绍主图")
	private String mainImgUrl;

	@ApiModelProperty("商品介绍主图 多个图片逗号分隔")
	private String imgUrls;

	@ApiModelProperty("商品视频")
	private String video;

	@ApiModelProperty("售价，整数方式保存")
	private Long priceFee;

	@ApiModelProperty("市场价，整数方式保存")
	private Long marketPriceFee;

	@ApiModelProperty("状态 1:enable, 0:disable, -1:deleted, 2:违规下架 3:等待审核")
	private Integer status;

	@ApiModelProperty("是否铺货: 0否 1是")
	private Integer iphStatus;

	@ApiModelProperty("sku是否含有图片 0无 1有")
	private Integer hasSkuImg;

	@ApiModelProperty("商品详情")
	private String detail;

	@ApiModelProperty("商品详情列表")
	private List<SpuDetailVO> detailList;


	@ApiModelProperty("总库存")
	private Integer totalStock;

	@ApiModelProperty("总库存")
	private Integer channelsStock;

	@ApiModelProperty("规格属性")
	private List<SpuAttrValueVO> spuAttrValues;

	@ApiModelProperty("sku列表")
	private List<SkuVO> skus;

	@ApiModelProperty("skc列表(款色列表)")
	private List<SkcVO> skcs;

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

	@ApiModelProperty("序号")
	private Integer seq;

	@ApiModelProperty("是否置顶")
	private Integer isTop;

	@ApiModelProperty("品牌信息")
	private BrandVO brand;

	@ApiModelProperty("商品销量")
	private Integer saleNum;

	@ApiModelProperty("评论数量")
	private Integer commentNum;

	@ApiModelProperty("物流信息")
	private DeliveryModeVO deliveryModeVO;

	@ApiModelProperty("店铺名称")
	private String shopName;

	@ApiModelProperty("分类信息")
	private CategoryVO category;

	@ApiModelProperty("店铺分类信息")
	private CategoryVO shopCategory;

	@ApiModelProperty("分组商品关联id")
	private Long referenceId;

	@ApiModelProperty("商品编码")
	private String spuCode;
	@ApiModelProperty("中台分类code")
	private String styleCode;

	@ApiModelProperty("商品简称")
	private String abbr;

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
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

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public List<SpuLangVO> getSpuLangList() {
		return spuLangList;
	}

	public void setSpuLangList(List<SpuLangVO> spuLangList) {
		this.spuLangList = spuLangList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSellingPoint() {
		return sellingPoint;
	}

	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
	}

	public String getMainImgUrl() {
		return mainImgUrl;
	}

	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getHasSkuImg() {
		return hasSkuImg;
	}

	public void setHasSkuImg(Integer hasSkuImg) {
		this.hasSkuImg = hasSkuImg;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public List<SpuDetailVO> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<SpuDetailVO> detailList) {
		this.detailList = detailList;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public List<SpuAttrValueVO> getSpuAttrValues() {
		return spuAttrValues;
	}

	public void setSpuAttrValues(List<SpuAttrValueVO> spuAttrValues) {
		this.spuAttrValues = spuAttrValues;
	}

	public List<SkuVO> getSkus() {
		return skus;
	}

	public void setSkus(List<SkuVO> skus) {
		this.skus = skus;
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

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public BrandVO getBrand() {
		return brand;
	}

	public void setBrand(BrandVO brand) {
		this.brand = brand;
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

	public DeliveryModeVO getDeliveryModeVO() {
		return deliveryModeVO;
	}

	public void setDeliveryModeVO(DeliveryModeVO deliveryModeVO) {
		this.deliveryModeVO = deliveryModeVO;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public CategoryVO getCategory() {
		return category;
	}

	public void setCategory(CategoryVO category) {
		this.category = category;
	}

	public CategoryVO getShopCategory() {
		return shopCategory;
	}

	public void setShopCategory(CategoryVO shopCategory) {
		this.shopCategory = shopCategory;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public Integer getIphStatus() {
		return iphStatus;
	}

	public void setIphStatus(Integer iphStatus) {
		this.iphStatus = iphStatus;
	}

	@Override
	public String toString() {
		return "SpuVO{" +
				"spuId=" + spuId +
				", brandId=" + brandId +
				", categoryId=" + categoryId +
				", shopCategoryId=" + shopCategoryId +
				", shopId=" + shopId +
				", spuLangList=" + spuLangList +
				", name='" + name + '\'' +
				", sellingPoint='" + sellingPoint + '\'' +
				", mainImgUrl='" + mainImgUrl + '\'' +
				", imgUrls='" + imgUrls + '\'' +
				", video='" + video + '\'' +
				", priceFee=" + priceFee +
				", marketPriceFee=" + marketPriceFee +
				", status=" + status +
				", hasSkuImg=" + hasSkuImg +
				", detail='" + detail + '\'' +
				", detailList=" + detailList +
				", totalStock=" + totalStock +
				", spuAttrValues=" + spuAttrValues +
				", skus=" + skus +
				", deliveryMode='" + deliveryMode + '\'' +
				", deliveryTemplateId=" + deliveryTemplateId +
				", spuType=" + spuType +
				", activityId=" + activityId +
				", isCompose=" + isCompose +
				", seq=" + seq +
				", toTop=" + isTop +
				", brand=" + brand +
				", saleNum=" + saleNum +
				", commentNum=" + commentNum +
				", deliveryModeVO=" + deliveryModeVO +
				", shopName='" + shopName + '\'' +
				", category=" + category +
				", shopCategory=" + shopCategory +
				", referenceId=" + referenceId +
				'}';
	}

	public static class DeliveryModeVO {
		/**
		 * 用户自提
		 */
		@ApiModelProperty(value = "用户自提", required = true)
		private Boolean hasUserPickUp;

		/**
		 * 店铺配送
		 */
		@ApiModelProperty(value = "店铺配送", required = true)
		private Boolean hasShopDelivery;

		/**
		 * 同城配送
		 */
		@ApiModelProperty(value = "同城配送", required = true)
		private Boolean hasCityDelivery;

		public Boolean getHasUserPickUp() {
			return hasUserPickUp;
		}

		public void setHasUserPickUp(Boolean hasUserPickUp) {
			this.hasUserPickUp = hasUserPickUp;
		}

		public Boolean getHasShopDelivery() {
			return hasShopDelivery;
		}

		public void setHasShopDelivery(Boolean hasShopDelivery) {
			this.hasShopDelivery = hasShopDelivery;
		}

		public Boolean getHasCityDelivery() {
			return hasCityDelivery;
		}

		public void setHasCityDelivery(Boolean hasCityDelivery) {
			this.hasCityDelivery = hasCityDelivery;
		}

		@Override
		public String toString() {
			return "DeliveryModeVO{" +
					"hasUserPickUp=" + hasUserPickUp +
					", hasShopDelivery=" + hasShopDelivery +
					", hasCityDelivery=" + hasCityDelivery +
					'}';
		}
	}
}
