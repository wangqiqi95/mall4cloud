package com.mall4j.cloud.api.docking.dto;

import com.mall4j.cloud.api.docking.enums.InventoryStatus;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ElectronicSignDto {

	@ApiModelProperty(value = "门店ID或商家自定义门店ID，不传使用默认门店", required = true)
	private String storeId;

	@ApiModelProperty(value = "默认值为false，如果配置为true则商品信息会更新门店下其它ItemId字段相同的商品信息；如果一次商品列表中包含多个ItemId相同的商品，则以排在最后那个内容做更新；", required = true)
	private Boolean syncByItemId = Boolean.FALSE;

	@ApiModelProperty(value = "商品信息集合", required = true)
	private List<ItemInfo> itemInfos;

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Boolean getSyncByItemId() {
		return syncByItemId;
	}

	public void setSyncByItemId(Boolean syncByItemId) {
		this.syncByItemId = syncByItemId;
	}

	public List<ItemInfo> getItemInfos() {
		return itemInfos;
	}

	public void setItemInfos(List<ItemInfo> itemInfos) {
		this.itemInfos = itemInfos;
	}

	public static class ItemInfo {

		@ApiModelProperty(value = "商品条码，字符不区分大小写，最长64", required = true)
		private String itemBarCode;

		@ApiModelProperty(value = "自定义商品条码，只允许输入构成整数的阿拉伯数字", required = true)
		private String itemId;

		@ApiModelProperty(value = "商品全称，最长100字符", required = true)
		private String itemTitle;

		@ApiModelProperty(value = "实际销售价格(单位:分)", required = true)
		private Integer actionPrice;

		@ApiModelProperty(value = "计价单位，最长64个字符；例如：箱", required = true)
		private String priceUnit;

		@ApiModelProperty("商品ID(SKU)，最长64个字符")
		private String skuId;

		@ApiModelProperty("商品简称，不输入则从商品全称中截取，最长64字符")
		private String itemShortTitle;

		@ApiModelProperty("品牌名称，最长64字符")
		private String brandName;

		@ApiModelProperty("型号，最长64个字符，330")
		private String modelNumber;

		@ApiModelProperty("材质，最长64个字符，例如：新鲜牛奶")
		private String material;

		@ApiModelProperty("品类，最长64个字符，例如：饮料")
		private String categoryName;

		@ApiModelProperty(value = "产地，最长64个字符", example = "中国")
		private String productionPlace;

		@ApiModelProperty(value = "能效，最长64个字符", example = "2焦/毫升")
		private String energyEfficiency;

		@ApiModelProperty(value = "等级，最长32个字符", example = "1级")
		private String rank;

		@ApiModelProperty(value = "规格，最长64个字符", example = "330毫升")
		private String saleSpec;

		@ApiModelProperty(value = "生产商，最长128个字符",example = "中国制造")
		private String manufacturer;

		@ApiModelProperty(value = "商品二维码地址，最长1024个字符", example = "http://m.taobao.com/xxx.html")
		private String itemQrCode;

		@ApiModelProperty(value = "经销商，最长128个字符", example = "天猫超市")
		private String supplierName;

		@ApiModelProperty(value = "一类商品类目ID，最长32个字符", example = "食品")
		private String forestFirstId;

		@ApiModelProperty(value = "二类商品类目ID，最长32个字符", example = "饮料")
		private String forestSecondId;

		@ApiModelProperty("销售价格(单位:分)")
		private Integer salesPrice;

		@ApiModelProperty("原价(单位:分)")
		private Integer originalPrice;

		@ApiModelProperty(value = "税费信息，最长32个字符", example = "增值税")
		private String taxFee;

		@ApiModelProperty("是否匹配促销模板显示，默认值为false")
		private Boolean bePromotion = Boolean.FALSE;

		@ApiModelProperty(value = "促销原因，最长64个字符", example = "儿童节活动")
		private String promotionReason;

		@ApiModelProperty("促销开始时间 UTC格式 \"yyyy-MM-dd'T'HH:mm:ss'Z'\"")
		private String promotionStart;

		@ApiModelProperty("促销结束时间 UTC格式 \"yyyy-MM-dd'T'HH:mm:ss'Z'\"。")
		private String promotionEnd;

		@ApiModelProperty(value = "促销文案，最长64个字符", example = "买一送一")
		private String promotionText;

		@ApiModelProperty("建议零售价(单位:分)")
		private Integer suggestPrice;

		@ApiModelProperty("是否匹配溯源模板显示，默认值为false")
		private Boolean beSourceCode = Boolean.FALSE;

		@ApiModelProperty("溯源码，最长128个字符")
		private String sourceCode;

		@ApiModelProperty("是否匹配缺货模板显示，可选值：\n" + "\n" + "OUT_OF_STOCK：缺货\n" + "NORMAL：正常。\n" + "默认值NORMAL，如果配置为OUT_OF_STOCK则会配置缺货模板进行显示")
		private String inventoryStatus = InventoryStatus.NORMAL.getValue();

		@ApiModelProperty("是否匹配会员模板显示，默认值为false")
		private Boolean beMember = Boolean.FALSE;

		@ApiModelProperty("会员价(单位:分)")
		private Integer memberPrice;

		@ApiModelProperty(value = "商品图片URL，最长128个字符", example = "http://m.taobao.com/xxx.html")
		private String itemPicUrl;

		@ApiModelProperty("客户自定义模板ID，如果有输入有效字符则匹配客户自定义模板进行商品展示，默认值为空字符“”")
		private String templateSceneId;

		@ApiModelProperty("自定义属性A，最长64个字符")
		private String customizeFeatureA;
		@ApiModelProperty("自定义属性B，最长64个字符")
		private String customizeFeatureB;
		@ApiModelProperty("自定义属性C，最长64个字符")
		private String customizeFeatureC;
		@ApiModelProperty("自定义属性D，最长64个字符")
		private String customizeFeatureD;
		@ApiModelProperty("自定义属性E，最长64个字符")
		private String customizeFeatureE;
		@ApiModelProperty("自定义属性F，最长64个字符")
		private String customizeFeatureF;
		@ApiModelProperty("自定义属性G，最长64个字符")
		private String customizeFeatureG;
		@ApiModelProperty("自定义属性H，最长64个字符")
		private String customizeFeatureH;
		@ApiModelProperty("自定义属性I，最长64个字符")
		private String customizeFeatureI;
		@ApiModelProperty("自定义属性J，最长64个字符")
		private String customizeFeatureJ;
		@ApiModelProperty("自定义属性K，最长128个字符")
		private String customizeFeatureK;
		@ApiModelProperty("自定义属性L，最长128个字符")
		private String customizeFeatureL;
		@ApiModelProperty("自定义属性M，最长128个字符")
		private String customizeFeatureM;
		@ApiModelProperty("自定义属性N，最长128个字符")
		private String customizeFeatureN;
		@ApiModelProperty("自定义属性O，最长128个字符")
		private String customizeFeatureO;


		public ItemInfo() {
		}

		public Integer getMemberPrice() {
			return memberPrice;
		}

		public void setMemberPrice(Integer memberPrice) {
			this.memberPrice = memberPrice;
		}

		public Integer getActionPrice() {
			return actionPrice;
		}

		public void setActionPrice(Integer actionPrice) {
			this.actionPrice = actionPrice;
		}

		public Boolean getBeSourceCode() {
			return beSourceCode;
		}

		public void setBeSourceCode(Boolean beSourceCode) {
			this.beSourceCode = beSourceCode;
		}

		public String getBrandName() {
			return brandName;
		}

		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}

		public String getPromotionStart() {
			return promotionStart;
		}

		public void setPromotionStart(String promotionStart) {
			this.promotionStart = promotionStart;
		}

		public String getPriceUnit() {
			return priceUnit;
		}

		public void setPriceUnit(String priceUnit) {
			this.priceUnit = priceUnit;
		}

		public String getRank() {
			return rank;
		}

		public void setRank(String rank) {
			this.rank = rank;
		}

		public String getItemBarCode() {
			return itemBarCode;
		}

		public void setItemBarCode(String itemBarCode) {
			this.itemBarCode = itemBarCode;
		}

		public String getCustomizeFeatureK() {
			return customizeFeatureK;
		}

		public void setCustomizeFeatureK(String customizeFeatureK) {
			this.customizeFeatureK = customizeFeatureK;
		}

		public String getCustomizeFeatureL() {
			return customizeFeatureL;
		}

		public void setCustomizeFeatureL(String customizeFeatureL) {
			this.customizeFeatureL = customizeFeatureL;
		}

		public String getCustomizeFeatureM() {
			return customizeFeatureM;
		}

		public void setCustomizeFeatureM(String customizeFeatureM) {
			this.customizeFeatureM = customizeFeatureM;
		}

		public Boolean getBePromotion() {
			return bePromotion;
		}

		public void setBePromotion(Boolean bePromotion) {
			this.bePromotion = bePromotion;
		}

		public String getCustomizeFeatureN() {
			return customizeFeatureN;
		}

		public void setCustomizeFeatureN(String customizeFeatureN) {
			this.customizeFeatureN = customizeFeatureN;
		}

		public String getCustomizeFeatureO() {
			return customizeFeatureO;
		}

		public void setCustomizeFeatureO(String customizeFeatureO) {
			this.customizeFeatureO = customizeFeatureO;
		}

		public String getPromotionEnd() {
			return promotionEnd;
		}

		public void setPromotionEnd(String promotionEnd) {
			this.promotionEnd = promotionEnd;
		}

		public String getItemTitle() {
			return itemTitle;
		}

		public void setItemTitle(String itemTitle) {
			this.itemTitle = itemTitle;
		}

		public String getCustomizeFeatureC() {
			return customizeFeatureC;
		}

		public void setCustomizeFeatureC(String customizeFeatureC) {
			this.customizeFeatureC = customizeFeatureC;
		}

		public String getCustomizeFeatureD() {
			return customizeFeatureD;
		}

		public void setCustomizeFeatureD(String customizeFeatureD) {
			this.customizeFeatureD = customizeFeatureD;
		}

		public String getItemQrCode() {
			return itemQrCode;
		}

		public void setItemQrCode(String itemQrCode) {
			this.itemQrCode = itemQrCode;
		}

		public String getCustomizeFeatureE() {
			return customizeFeatureE;
		}

		public void setCustomizeFeatureE(String customizeFeatureE) {
			this.customizeFeatureE = customizeFeatureE;
		}

		public String getInventoryStatus() {
			return inventoryStatus;
		}

		public void setInventoryStatus(String inventoryStatus) {
			this.inventoryStatus = inventoryStatus;
		}

		public String getPromotionReason() {
			return promotionReason;
		}

		public void setPromotionReason(String promotionReason) {
			this.promotionReason = promotionReason;
		}

		public String getCustomizeFeatureF() {
			return customizeFeatureF;
		}

		public void setCustomizeFeatureF(String customizeFeatureF) {
			this.customizeFeatureF = customizeFeatureF;
		}

		public String getCustomizeFeatureG() {
			return customizeFeatureG;
		}

		public void setCustomizeFeatureG(String customizeFeatureG) {
			this.customizeFeatureG = customizeFeatureG;
		}

		public String getCustomizeFeatureH() {
			return customizeFeatureH;
		}

		public void setCustomizeFeatureH(String customizeFeatureH) {
			this.customizeFeatureH = customizeFeatureH;
		}

		public String getCustomizeFeatureI() {
			return customizeFeatureI;
		}

		public void setCustomizeFeatureI(String customizeFeatureI) {
			this.customizeFeatureI = customizeFeatureI;
		}

		public String getCustomizeFeatureJ() {
			return customizeFeatureJ;
		}

		public void setCustomizeFeatureJ(String customizeFeatureJ) {
			this.customizeFeatureJ = customizeFeatureJ;
		}

		public String getCustomizeFeatureA() {
			return customizeFeatureA;
		}

		public void setCustomizeFeatureA(String customizeFeatureA) {
			this.customizeFeatureA = customizeFeatureA;
		}

		public String getCustomizeFeatureB() {
			return customizeFeatureB;
		}

		public void setCustomizeFeatureB(String customizeFeatureB) {
			this.customizeFeatureB = customizeFeatureB;
		}

		public Integer getSuggestPrice() {
			return suggestPrice;
		}

		public void setSuggestPrice(Integer suggestPrice) {
			this.suggestPrice = suggestPrice;
		}

		public String getForestFirstId() {
			return forestFirstId;
		}

		public void setForestFirstId(String forestFirstId) {
			this.forestFirstId = forestFirstId;
		}

		public String getProductionPlace() {
			return productionPlace;
		}

		public void setProductionPlace(String productionPlace) {
			this.productionPlace = productionPlace;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public String getSourceCode() {
			return sourceCode;
		}

		public void setSourceCode(String sourceCode) {
			this.sourceCode = sourceCode;
		}

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public Boolean getBeMember() {
			return beMember;
		}

		public void setBeMember(Boolean beMember) {
			this.beMember = beMember;
		}

		public String getTemplateSceneId() {
			return templateSceneId;
		}

		public void setTemplateSceneId(String templateSceneId) {
			this.templateSceneId = templateSceneId;
		}

		public Integer getSalesPrice() {
			return salesPrice;
		}

		public void setSalesPrice(Integer salesPrice) {
			this.salesPrice = salesPrice;
		}

		public Integer getOriginalPrice() {
			return originalPrice;
		}

		public void setOriginalPrice(Integer originalPrice) {
			this.originalPrice = originalPrice;
		}

		public String getItemShortTitle() {
			return itemShortTitle;
		}

		public void setItemShortTitle(String itemShortTitle) {
			this.itemShortTitle = itemShortTitle;
		}

		public String getForestSecondId() {
			return forestSecondId;
		}

		public void setForestSecondId(String forestSecondId) {
			this.forestSecondId = forestSecondId;
		}

		public String getItemPicUrl() {
			return itemPicUrl;
		}

		public void setItemPicUrl(String itemPicUrl) {
			this.itemPicUrl = itemPicUrl;
		}

		public String getSupplierName() {
			return supplierName;
		}

		public void setSupplierName(String supplierName) {
			this.supplierName = supplierName;
		}

		public String getMaterial() {
			return material;
		}

		public void setMaterial(String material) {
			this.material = material;
		}

		public String getModelNumber() {
			return modelNumber;
		}

		public void setModelNumber(String modelNumber) {
			this.modelNumber = modelNumber;
		}

		public String getSaleSpec() {
			return saleSpec;
		}

		public void setSaleSpec(String saleSpec) {
			this.saleSpec = saleSpec;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public String getTaxFee() {
			return taxFee;
		}

		public void setTaxFee(String taxFee) {
			this.taxFee = taxFee;
		}

		public String getEnergyEfficiency() {
			return energyEfficiency;
		}

		public void setEnergyEfficiency(String energyEfficiency) {
			this.energyEfficiency = energyEfficiency;
		}

		public String getPromotionText() {
			return promotionText;
		}

		public void setPromotionText(String promotionText) {
			this.promotionText = promotionText;
		}

		public String getSkuId() {
			return skuId;
		}

		public void setSkuId(String skuId) {
			this.skuId = skuId;
		}

		@Override
		public String toString() {
			return "ItemInfo{" + "itemBarCode='" + itemBarCode + '\'' + ", itemId='" + itemId + '\'' + ", itemTitle='" + itemTitle + '\'' + ", actionPrice="
					+ actionPrice + ", priceUnit='" + priceUnit + '\'' + ", memberPrice=" + memberPrice + ", beSourceCode=" + beSourceCode + ", brandName='"
					+ brandName + '\'' + ", promotionStart='" + promotionStart + '\'' + ", promotionEnd='" + promotionEnd + '\'' + ", promotionText='"
					+ promotionText + '\'' + ", rank='" + rank + '\'' + ", bePromotion=" + bePromotion + ", itemQrCode='" + itemQrCode + '\''
					+ ", inventoryStatus='" + inventoryStatus + '\'' + ", promotionReason='" + promotionReason + '\'' + ", suggestPrice=" + suggestPrice
					+ ", forestFirstId='" + forestFirstId + '\'' + ", forestSecondId='" + forestSecondId + '\'' + ", productionPlace='" + productionPlace + '\''
					+ ", material='" + material + '\'' + ", modelNumber='" + modelNumber + '\'' + ", saleSpec='" + saleSpec + '\'' + ", categoryName='"
					+ categoryName + '\'' + ", energyEfficiency='" + energyEfficiency + '\'' + ", manufacturer='" + manufacturer + '\'' + ", sourceCode='"
					+ sourceCode + '\'' + ", beMember=" + beMember + ", templateSceneId='" + templateSceneId + '\'' + ", salesPrice=" + salesPrice
					+ ", originalPrice=" + originalPrice + ", itemShortTitle='" + itemShortTitle + '\'' + ", itemPicUrl='" + itemPicUrl + '\''
					+ ", supplierName='" + supplierName + '\'' + ", taxFee='" + taxFee + '\'' + ", skuId='" + skuId + '\'' + ", customizeFeatureA='"
					+ customizeFeatureA + '\'' + ", customizeFeatureB='" + customizeFeatureB + '\'' + ", customizeFeatureC='" + customizeFeatureC + '\''
					+ ", customizeFeatureD='" + customizeFeatureD + '\'' + ", customizeFeatureE='" + customizeFeatureE + '\'' + ", customizeFeatureF='"
					+ customizeFeatureF + '\'' + ", customizeFeatureG='" + customizeFeatureG + '\'' + ", customizeFeatureH='" + customizeFeatureH + '\''
					+ ", customizeFeatureI='" + customizeFeatureI + '\'' + ", customizeFeatureJ='" + customizeFeatureJ + '\'' + ", customizeFeatureK='"
					+ customizeFeatureK + '\'' + ", customizeFeatureL='" + customizeFeatureL + '\'' + ", customizeFeatureM='" + customizeFeatureM + '\''
					+ ", customizeFeatureN='" + customizeFeatureN + '\'' + ", customizeFeatureO='" + customizeFeatureO + '\'' + '}';
		}
	}

	@Override
	public String toString() {
		return "ElectronicSignDto{" + "storeId='" + storeId + '\'' + ", syncByItemId=" + syncByItemId + ", itemInfos=" + itemInfos + '}';
	}
}
