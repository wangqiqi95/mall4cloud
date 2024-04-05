package com.mall4j.cloud.common.product.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
public class EsProductBO {

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 商品编码
     */
    private String spuCode;

    /**
     * 中文商品名称
     */
    private String spuNameZh;

    /**
     * 英文商品名称
     */
    private String spuNameEn;

    /**
     * 中文卖点
     */
    private String sellingPointZh;

    /**
     * 英文卖点
     */
    private String sellingPointEn;

    /**
     * 商品积分价
     */
    private Long scoreFee;

    /**
     * 商品售价
     */
    private Long priceFee;

    /**
     * 市场价，整数方式保存
     */
    private Long marketPriceFee;

    /**
     * 商品介绍主图
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String mainImgUrl;

    /**
     * 商品类型
     */
    private Integer spuType;

    /**
     * 商品类型
     */
    private Integer isCompose;

    /**
     * 店铺名称 搜索华为的时候，可以把华为的旗舰店搜索出来
     */
    private String shopName;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 店铺logo
     */
    private String shopImg;

    /**
     * 店铺类型1自营店 2普通店
     */
    private Integer shopType;

    /**
     * 商品状态
     */
    private Integer spuStatus;

    /**
     * 商品编码列表
     */
    private Set<String> partyCodes;

    /**
     * 商品条形码列表
     */
    private Set<String> modelIds;

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer saleNum;

    /**
     * 实际销量
     */
    private Integer actualSoldNum;

    /**
     * 注水销量
     */
    private Integer waterSoldNum;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 好评量
     */
    private Integer goodReviewNum;

    /**
     * 商品创建时间
     */
    private Date createTime;

    /**
     * 商品更新时间
     */
    private Date updateTime;

    /**
     * 活动开始时间
     */
    private Long activityStartTime;

    /**
     * 活动开始时间
     */
    private Integer selectedLot;

    /**
     * 秒杀商品分类
     */
    private Long seckillCategoryId;

    /**
     * 品牌信息
     */
    private EsBrandBO brand;

    /**
     * 商品序号
     */
    private Integer seq;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 商家一级分类id
     */
    private Long shopPrimaryCategoryId;

    /**
     * 商家二级分类信息
     */
    private EsCategoryBO shopCategory;

    /**
     * 平台一级分类id
     */
    private Long primaryCategoryId;

    /**
     * 平台二级分类id
     */
    private Long secondaryCategoryId;

    /**
     * 平台三级分类信息
     */
    private EsCategoryBO category;

    /**
     * 商品分组id列表
     */
    private List<Long> tagIds;

    /**
     * 商品用于搜索的规格属性
     */
    private List<EsAttrBO> attrs;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 平台分类id
     */
    private Long categoryId;

    /**
     * 店铺分类id
     */
    private Long shopCategoryId;

    /**
     * 用户端显示该商品(0:不显示， 1：显示)
     */
    private Boolean appDisplay;

    /**
     * 是否属于分销商品(true: 是，false: 否)
     */
    private Boolean distributionSpu;

    /**
     * 商品分销信息
     */
    private EsDistributionInfoBO distributionInfo;

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public Boolean getDistributionSpu() {
        return distributionSpu;
    }

    public void setDistributionSpu(Boolean distributionSpu) {
        this.distributionSpu = distributionSpu;
    }

    public EsDistributionInfoBO getDistributionInfo() {
        return distributionInfo;
    }

    public void setDistributionInfo(EsDistributionInfoBO distributionInfo) {
        this.distributionInfo = distributionInfo;
    }

    public Integer getActualSoldNum() {
        return actualSoldNum;
    }

    public void setActualSoldNum(Integer actualSoldNum) {
        this.actualSoldNum = actualSoldNum;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getWaterSoldNum() {
        return waterSoldNum;
    }

    public void setWaterSoldNum(Integer waterSoldNum) {
        this.waterSoldNum = waterSoldNum;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getSpuNameZh() {
        return spuNameZh;
    }

    public void setSpuNameZh(String spuNameZh) {
        this.spuNameZh = spuNameZh;
    }

    public String getSpuNameEn() {
        return spuNameEn;
    }

    public void setSpuNameEn(String spuNameEn) {
        this.spuNameEn = spuNameEn;
    }

    public String getSellingPointZh() {
        return sellingPointZh;
    }

    public void setSellingPointZh(String sellingPointZh) {
        this.sellingPointZh = sellingPointZh;
    }

    public String getSellingPointEn() {
        return sellingPointEn;
    }

    public void setSellingPointEn(String sellingPointEn) {
        this.sellingPointEn = sellingPointEn;
    }

    public Long getPriceFee() {
        return priceFee;
    }

    public void setPriceFee(Long priceFee) {
        this.priceFee = priceFee;
    }

    public Long getScoreFee() {
        return scoreFee;
    }

    public void setScoreFee(Long scoreFee) {
        this.scoreFee = scoreFee;
    }

    public Long getMarketPriceFee() {
        return marketPriceFee;
    }

    public void setMarketPriceFee(Long marketPriceFee) {
        this.marketPriceFee = marketPriceFee;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    public Integer getSpuType() {
        return spuType;
    }

    public void setSpuType(Integer spuType) {
        this.spuType = spuType;
    }

    public Integer getIsCompose() {
        return isCompose;
    }

    public void setIsCompose(Integer isCompose) {
        this.isCompose = isCompose;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public Integer getSpuStatus() {
        return spuStatus;
    }

    public void setSpuStatus(Integer spuStatus) {
        this.spuStatus = spuStatus;
    }

    public Set<String> getPartyCodes() {
        return partyCodes;
    }

    public void setPartyCodes(Set<String> partyCodes) {
        this.partyCodes = partyCodes;
    }

    public Set<String> getModelIds() {
        return modelIds;
    }

    public void setModelIds(Set<String> modelIds) {
        this.modelIds = modelIds;
    }

    public Boolean getHasStock() {
        return hasStock;
    }

    public void setHasStock(Boolean hasStock) {
        this.hasStock = hasStock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    public Integer getGoodReviewNum() {
        return goodReviewNum;
    }

    public void setGoodReviewNum(Integer goodReviewNum) {
        this.goodReviewNum = goodReviewNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(Long activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public Integer getSelectedLot() {
        return selectedLot;
    }

    public void setSelectedLot(Integer selectedLot) {
        this.selectedLot = selectedLot;
    }

    public Long getSeckillCategoryId() {
        return seckillCategoryId;
    }

    public void setSeckillCategoryId(Long seckillCategoryId) {
        this.seckillCategoryId = seckillCategoryId;
    }

    public EsBrandBO getBrand() {
        return brand;
    }

    public void setBrand(EsBrandBO brand) {
        this.brand = brand;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getShopPrimaryCategoryId() {
        return shopPrimaryCategoryId;
    }

    public void setShopPrimaryCategoryId(Long shopPrimaryCategoryId) {
        this.shopPrimaryCategoryId = shopPrimaryCategoryId;
    }

    public EsCategoryBO getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(EsCategoryBO shopCategory) {
        this.shopCategory = shopCategory;
    }

    public Long getPrimaryCategoryId() {
        return primaryCategoryId;
    }

    public void setPrimaryCategoryId(Long primaryCategoryId) {
        this.primaryCategoryId = primaryCategoryId;
    }

    public Long getSecondaryCategoryId() {
        return secondaryCategoryId;
    }

    public void setSecondaryCategoryId(Long secondaryCategoryId) {
        this.secondaryCategoryId = secondaryCategoryId;
    }

    public EsCategoryBO getCategory() {
        return category;
    }

    public void setCategory(EsCategoryBO category) {
        this.category = category;
    }

    public List<EsAttrBO> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<EsAttrBO> attrs) {
        this.attrs = attrs;
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

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public Boolean getAppDisplay() {
        return appDisplay;
    }

    public void setAppDisplay(Boolean appDisplay) {
        this.appDisplay = appDisplay;
    }

    @Override
    public String toString() {
        return "EsProductBO{" +
                "spuId=" + spuId +
                ", spuNameZh='" + spuNameZh + '\'' +
                ", spuNameEn='" + spuNameEn + '\'' +
                ", sellingPointZh='" + sellingPointZh + '\'' +
                ", sellingPointEn='" + sellingPointEn + '\'' +
                ", priceFee=" + priceFee +
                ", scoreFee=" + scoreFee +
                ", marketPriceFee=" + marketPriceFee +
                ", mainImgUrl='" + mainImgUrl + '\'' +
                ", spuType=" + spuType +
                ", isCompose=" + isCompose +
                ", shopName='" + shopName + '\'' +
                ", shopId=" + shopId +
                ", shopImg='" + shopImg + '\'' +
                ", shopType=" + shopType +
                ", spuStatus=" + spuStatus +
                ", partyCodes=" + partyCodes +
                ", modelIds=" + modelIds +
                ", hasStock=" + hasStock +
                ", stock=" + stock +
                ", saleNum=" + saleNum +
                ", actualSoldNum=" + actualSoldNum +
                ", waterSoldNum=" + waterSoldNum +
                ", commentNum=" + commentNum +
                ", goodReviewNum=" + goodReviewNum +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", activityStartTime=" + activityStartTime +
                ", selectedLot=" + selectedLot +
                ", seckillCategoryId=" + seckillCategoryId +
                ", brand=" + brand +
                ", seq=" + seq +
                ", isTop=" + isTop +
                ", activityId=" + activityId +
                ", shopPrimaryCategoryId=" + shopPrimaryCategoryId +
                ", shopCategory=" + shopCategory +
                ", primaryCategoryId=" + primaryCategoryId +
                ", secondaryCategoryId=" + secondaryCategoryId +
                ", category=" + category +
                ", tagIds=" + tagIds +
                ", attrs=" + attrs +
                ", brandId=" + brandId +
                ", categoryId=" + categoryId +
                ", shopCategoryId=" + shopCategoryId +
                ", appDisplay=" + appDisplay +
                ", isDistribution=" + distributionSpu +
                ", distributionInfo=" + distributionInfo +
                '}';
    }
}
