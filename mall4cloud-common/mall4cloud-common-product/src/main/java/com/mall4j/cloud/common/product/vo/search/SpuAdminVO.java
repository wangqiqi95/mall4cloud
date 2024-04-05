package com.mall4j.cloud.common.product.vo.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author YXF
 * @date 2021/03/16
 */
@Data
public class SpuAdminVO {

    @ApiModelProperty(value = "商品id")
    private Long spuId;

    @ApiModelProperty(value = "商品名称")
    private String spuName;

    @ApiModelProperty(value = "商品编码")
    private String spuCode;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty(value = "商品售价")
    private Long priceFee;

    @ApiModelProperty(value = "市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty(value = "销量")
    private Integer saleNum;

    @ApiModelProperty(value = "实际销量")
    private Integer actualSoldNum;

    @ApiModelProperty(value = "注水销量")
    private Integer waterSoldNum;

    @ApiModelProperty(value = "状态")
    private Integer spuStatus;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "序号")
    private Integer seq;

    @ApiModelProperty(value = "是否置顶")
    private Integer isTop;

    @ApiModelProperty("积分价格")
    private Long scoreFee;

    @ApiModelProperty("分销信息")
    private DistributionInfoVO distributionInfo;

    @ApiModelProperty("分组")
    private List<SearchSpuTagVO> tags;

    @ApiModelProperty("erpCategoryName")
    private String erpCategoryName;

    @ApiModelProperty("店铺分类")
    private String shopCategory;

    @ApiModelProperty("平台分类")
    private String platformCategory;

    @ApiModelProperty("门店节点")
    private String storeId;

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public DistributionInfoVO getDistributionInfo() {
        return distributionInfo;
    }

    public void setDistributionInfo(DistributionInfoVO distributionInfo) {
        this.distributionInfo = distributionInfo;
    }

    public Integer getActualSoldNum() {
        return actualSoldNum;
    }

    public void setActualSoldNum(Integer actualSoldNum) {
        this.actualSoldNum = actualSoldNum;
    }

    public Integer getWaterSoldNum() {
        return waterSoldNum;
    }

    public void setWaterSoldNum(Integer waterSoldNum) {
        this.waterSoldNum = waterSoldNum;
    }

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

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getSpuStatus() {
        return spuStatus;
    }

    public void setSpuStatus(Integer spuStatus) {
        this.spuStatus = spuStatus;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Long getScoreFee() {
        return scoreFee;
    }

    public void setScoreFee(Long scoreFee) {
        this.scoreFee = scoreFee;
    }

    public List<SearchSpuTagVO> getTags() {
        return tags;
    }

    public void setTags(List<SearchSpuTagVO> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "SpuAdminVO{" +
                "spuId=" + spuId +
                ", spuName='" + spuName + '\'' +
                ", mainImgUrl='" + mainImgUrl + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", priceFee=" + priceFee +
                ", marketPriceFee=" + marketPriceFee +
                ", saleNum=" + saleNum +
                ", actualSoldNum=" + actualSoldNum +
                ", waterSoldNum=" + waterSoldNum +
                ", spuStatus=" + spuStatus +
                ", stock=" + stock +
                ", seq=" + seq +
                ", isTop=" + isTop +
                ", scoreFee=" + scoreFee +
                ", distributionInfoVO=" + distributionInfo +
                ", tags=" + tags +
                '}';
    }
}
