package com.mall4j.cloud.product.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.product.vo.BrandLangVO;
import com.mall4j.cloud.common.product.vo.SpuLangVO;
import com.mall4j.cloud.common.product.vo.SpuSkuAttrValueVO;

import java.util.List;

/**
 * spu信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class SpuExcelVO extends ExcelModel {
	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "商品信息";
	public static final String SHEET_NAME = "商品";
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;
	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {0,1,2,3,4,5,6,7,8,9,10};

	@ExcelProperty(value = {"商品信息", "编号*"}, index = 0)
	private String seq;

	@ExcelProperty(value = {"商品信息", "商品中文名称*"}, index = 1)
	private String nameZh;

	@ExcelProperty(value = {"商品信息", "商品英文名称"}, index = 2)
	private String nameEn;

	@ExcelProperty(value = {"商品信息", "商品中文卖点"}, index = 3)
	private String sellingPointZh;

	@ExcelProperty(value = {"商品信息", "商品英文卖点"}, index = 4)
	private String sellingPointEn;

	@ExcelProperty(value = {"商品信息", "品牌"}, index = 5)
	private String brandName;

	@ExcelProperty(value = {"商品信息", "平台分类*"}, index = 6)
	private String categoryName;

	@ExcelProperty(value = {"商品信息", "店铺分类*"}, index = 7)
	private String shopCategoryName;

	@ExcelProperty(value = {"商品信息", "配送方式*"}, index = 8)
	private String deliveryMode;

	@ExcelProperty(value = {"商品信息", "运费模板*"}, index = 9)
	private String deliveryTemplate;

	@ExcelProperty(value = {"商品信息", "状态*"}, index = 10)
	private String status;

	@ExcelProperty(value = {"商品sku信息", "中文sku名称"})
	private String skuNameZh;

	@ExcelProperty(value = {"商品sku信息", "英文sku名称"})
	private String skuNameEn;

	@ExcelProperty(value = {"商品sku信息", "中文销售属性"})
	private String properties;

	@ExcelProperty(value = {"商品sku信息", "中文销售属性"})
	private String propertiesZh;

	@ExcelProperty(value = {"商品sku信息", "英文销售属性"})
	private String propertiesEn;

	@ExcelProperty(value = {"商品sku信息", "售价"})
	private String priceFee;

	@ExcelProperty(value = {"商品sku信息", "市场价"})
	private String marketPriceFee;

	@ExcelProperty(value = {"商品sku信息", "库存"})
	private Integer stock;

	@ExcelProperty(value = {"商品sku信息", "商品编码"})
	private String partyCode;

	@ExcelProperty(value = {"商品sku信息", "商品条形码"})
	private String modelId;

	@ExcelProperty(value = {"商品sku信息", "商品重量"})
	private Double weight;

	@ExcelProperty(value = {"商品sku信息", "商品体积"})
	private Double volume;

	/**
	 * 商品id
	 */
	@ExcelIgnore
	private Long spuId;
	@ExcelIgnore
	private String propertieValues;


	/**
	 * 物流模板id
	 */
	@ExcelIgnore
	private Long deliveryTemplateId;

	/**
	 * 是否收藏
	 */
	@ExcelIgnore
	private Integer isCompose;

	@ExcelIgnore
	private List<SpuSkuAttrValueVO> spuSkuAttrValueList;

	@ExcelIgnore
	private List<BrandLangVO> brandLangList;

	@ExcelIgnore
	private List<SpuLangVO> spuLangList;

	@ExcelIgnore
	private Long categoryId;

	@ExcelIgnore
	private Long shopCategoryId;

	@ExcelIgnore
	private String skuCode;
	@ExcelIgnore
	private String skuName;
	@ExcelIgnore
	private String priceCode;

	public String getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}

	public String getPropertieValues() {
		return propertieValues;
	}

	public void setPropertieValues(String propertieValues) {
		this.propertieValues = propertieValues;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getShopCategoryName() {
		return shopCategoryName;
	}

	public void setShopCategoryName(String shopCategoryName) {
		this.shopCategoryName = shopCategoryName;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getDeliveryTemplate() {
		return deliveryTemplate;
	}

	public void setDeliveryTemplate(String deliveryTemplate) {
		this.deliveryTemplate = deliveryTemplate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSkuNameZh() {
		return skuNameZh;
	}

	public void setSkuNameZh(String skuNameZh) {
		this.skuNameZh = skuNameZh;
	}

	public String getSkuNameEn() {
		return skuNameEn;
	}

	public void setSkuNameEn(String skuNameEn) {
		this.skuNameEn = skuNameEn;
	}

	public String getPropertiesZh() {
		return propertiesZh;
	}

	public void setPropertiesZh(String propertiesZh) {
		this.propertiesZh = propertiesZh;
	}

	public String getPropertiesEn() {
		return propertiesEn;
	}

	public void setPropertiesEn(String propertiesEn) {
		this.propertiesEn = propertiesEn;
	}

	public String getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(String priceFee) {
		this.priceFee = priceFee;
	}

	public String getMarketPriceFee() {
		return marketPriceFee;
	}

	public void setMarketPriceFee(String marketPriceFee) {
		this.marketPriceFee = marketPriceFee;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getDeliveryTemplateId() {
		return deliveryTemplateId;
	}

	public void setDeliveryTemplateId(Long deliveryTemplateId) {
		this.deliveryTemplateId = deliveryTemplateId;
	}

	public Integer getIsCompose() {
		return isCompose;
	}

	public void setIsCompose(Integer isCompose) {
		this.isCompose = isCompose;
	}

	public List<SpuSkuAttrValueVO> getSpuSkuAttrValueList() {
		return spuSkuAttrValueList;
	}

	public void setSpuSkuAttrValueList(List<SpuSkuAttrValueVO> spuSkuAttrValueList) {
		this.spuSkuAttrValueList = spuSkuAttrValueList;
	}

	public List<BrandLangVO> getBrandLangList() {
		return brandLangList;
	}

	public void setBrandLangList(List<BrandLangVO> brandLangList) {
		this.brandLangList = brandLangList;
	}

	public List<SpuLangVO> getSpuLangList() {
		return spuLangList;
	}

	public void setSpuLangList(List<SpuLangVO> spuLangList) {
		this.spuLangList = spuLangList;
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

	@Override
	public String toString() {
		return "SpuExcelVO{" +
				"seq='" + seq + '\'' +
				", nameZh='" + nameZh + '\'' +
				", nameEn='" + nameEn + '\'' +
				", sellingPointZh='" + sellingPointZh + '\'' +
				", sellingPointEn='" + sellingPointEn + '\'' +
				", brandName='" + brandName + '\'' +
				", categoryName='" + categoryName + '\'' +
				", shopCategoryName='" + shopCategoryName + '\'' +
				", deliveryMode='" + deliveryMode + '\'' +
				", deliveryTemplate='" + deliveryTemplate + '\'' +
				", status='" + status + '\'' +
				", skuNameZh='" + skuNameZh + '\'' +
				", skuNameEn='" + skuNameEn + '\'' +
				", propertiesZh='" + propertiesZh + '\'' +
				", propertiesEn='" + propertiesEn + '\'' +
				", priceFee='" + priceFee + '\'' +
				", marketPriceFee='" + marketPriceFee + '\'' +
				", stock=" + stock +
				", partyCode='" + partyCode + '\'' +
				", modelId='" + modelId + '\'' +
				", weight=" + weight +
				", volume=" + volume +
				", spuId=" + spuId +
				", deliveryTemplateId=" + deliveryTemplateId +
				", isCompose=" + isCompose +
				", spuSkuAttrValueList=" + spuSkuAttrValueList +
				", brandLangList=" + brandLangList +
				", spuLangList=" + spuLangList +
				", categoryId=" + categoryId +
				", shopCategoryId=" + shopCategoryId +
				'}';
	}
}
