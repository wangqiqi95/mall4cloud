package com.mall4j.cloud.api.openapi.skq_erp.dto;

import com.mall4j.cloud.api.openapi.skq_erp.enums.StockType;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 类描述：中台标准库存接口
 *
 * @date 2022/1/13 8:16：36
 */
@Data
public class StdStockDto implements Serializable, IStdDataCheck {


	@ApiModelProperty(value = "店铺编码或者逻辑仓编码")
	private String channelCode;

	@ApiModelProperty(value = "店铺名称或者逻辑仓名称")
	private String channelName;

	@ApiModelProperty(value = "表示线上或者线下,1：线上 0 线下")
	private String channelType;

	@ApiModelProperty(value = "可用库存量")
	private Integer availableStockNum;

	@ApiModelProperty(value = "平台商品id")
	private String platformProductId;

	@ApiModelProperty(value = "平台skuid")
	private String platformSkuId;

	@ApiModelProperty(value = "商家商品id")
	private String productId;

	@ApiModelProperty(value = "商家商品编码")
	private String productCode;

	@ApiModelProperty(value = "商家skuid")
	private String skuId;

	@ApiModelProperty(value = "商家sku编码")
	private String skuCode;

	@ApiModelProperty(value = "方便接收方update数据【相应记录的唯一表示】")
	private String updateKey;

	@ApiModelProperty(value = "扩展字段熟悉,放在该列,根据module_name取对应模板")
	private String stockExtend;

	@ApiModelProperty(value = "比如：向心云,")
	private String moduleName;

	@ApiModelProperty(value = "门店;店仓编码，当库存类型为2时必传")
	private String storeCode;

	/**
	 * {@link com.mall4j.cloud.api.openapi.skq_erp.enums.StockType}
	 */
	@ApiModelProperty(value = "库存类型(1-共享库存 2-门店库存)")
	private Integer stockType;

	@ApiModelProperty(value = "同步类型（1：增量同步，2：全量同步）")
	private Integer syncType;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public Integer getAvailableStockNum() {
		return availableStockNum;
	}

	public void setAvailableStockNum(Integer availableStockNum) {
		this.availableStockNum = availableStockNum;
	}

	public String getPlatformProductId() {
		return platformProductId;
	}

	public void setPlatformProductId(String platformProductId) {
		this.platformProductId = platformProductId;
	}

	public String getPlatformSkuId() {
		return platformSkuId;
	}

	public void setPlatformSkuId(String platformSkuId) {
		this.platformSkuId = platformSkuId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getUpdateKey() {
		return updateKey;
	}

	public void setUpdateKey(String updateKey) {
		this.updateKey = updateKey;
	}

	public String getStockExtend() {
		return stockExtend;
	}

	public void setStockExtend(String stockExtend) {
		this.stockExtend = stockExtend;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public Integer getStockType() {
		return stockType;
	}

	public void setStockType(Integer stockType) {
		this.stockType = stockType;
	}

	@Override
	public String toString() {
		return "StdStockDto{" + "channelCode='" + channelCode + '\'' + ", channelName='" + channelName + '\'' + ", channelType='" + channelType + '\''
				+ ", availableStockNum=" + availableStockNum + ", platformProductId='" + platformProductId + '\'' + ", platformSkuId='" + platformSkuId + '\''
				+ ", productId='" + productId + '\'' + ", productCode='" + productCode + '\'' + ", skuId='" + skuId + '\'' + ", skuCode='" + skuCode + '\''
				+ ", updateKey='" + updateKey + '\'' + ", stockExtend='" + stockExtend + '\'' + ", moduleName='" + moduleName + '\'' + ", storeCode='"
				+ storeCode + '\'' + ", stockType=" + stockType + '}';
	}

	@Override
	public StdResult check() {
		if (availableStockNum == null) {
			return StdResult.fail("availableStockNum不能为空");
		}

		if (StringUtils.isBlank(productCode)) {
			return StdResult.fail("productCode不能为空");
		}

		if (StringUtils.isBlank(skuCode)) {
			return StdResult.fail("skuCode不能为空");
		}

		if (StringUtils.isBlank(storeCode)) {
			return StdResult.fail("storeCode不能为空");
		}

		if (stockType == null) {
			return StdResult.fail("stockType不能为空");
		}

		if (!StockType.get(stockType).isPresent()) {
			return StdResult.fail("stockType不正确");
		}

		return StdResult.success();
	}
}
