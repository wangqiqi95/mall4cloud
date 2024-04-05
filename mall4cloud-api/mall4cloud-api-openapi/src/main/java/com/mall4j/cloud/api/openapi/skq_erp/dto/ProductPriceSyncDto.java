package com.mall4j.cloud.api.openapi.skq_erp.dto;

import com.mall4j.cloud.api.openapi.skq_erp.enums.PriceType;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * @description 商品价格同步（门店版）请求参数
 * @date 2021/12/29 16:40：50
 */
public class ProductPriceSyncDto implements Serializable, IStdDataCheck {

    private static final long serialVersionUID = 5751704760603289239L;
    @ApiModelProperty(value = "商品编码（用来标识商品）", required = true)
    private String productCode;

    @ApiModelProperty(value = "门店", required = true)
    private String storeCode;

    @ApiModelProperty(value = "sku编码", required = true)
    private String skuCode;

    @ApiModelProperty(value = "skc编码", required = true)
    private String skcCode;

    @ApiModelProperty(value = "单价(单位:分)", required = true)
    private Integer price;

    /**
     * {@link com.mall4j.cloud.api.openapi.skq_erp.enums.PriceType}
     */
    @ApiModelProperty(value = "价格类型 1-吊牌价 2-保护价 3-活动价", required = true)
    private Integer priceType;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getSkcCode() {
        return skcCode;
    }

    public void setSkcCode(String skcCode) {
        this.skcCode = skcCode;
    }

    @Override
    public String toString() {
        return "ProductPriceSyncDto{" + "productCode='" + productCode + '\'' + ", storeCode='" + storeCode + '\'' + ", skuCode='" + skuCode + '\'' + ", skcCode='" + skcCode + '\'' + ", price="
                + price + ", priceType=" + priceType + '}';
    }

    @Override
    public StdResult check() {
        if (StringUtils.isBlank(productCode)) {
            return StdResult.fail("商品编码不能为空");
        }
        if (StringUtils.isBlank(skcCode)) {
            return StdResult.fail("skc编码不能为空");
        }

        if (priceType == null) {
            return StdResult.fail("价格类型不能为空");
        }

        Optional<PriceType> optionalPriceType = PriceType.get(this.priceType);
        if (!optionalPriceType.isPresent()) {
            return StdResult.fail("价格类型不正确");
        }

        if (PriceType.ACTIVITY_PRICE.value() == optionalPriceType.get().value()) {
            if (StringUtils.isBlank(storeCode)) {
                return StdResult.fail("门店编码不能为空");
            }
        }

        if (price == null) {
            return StdResult.fail("价格不能为空");
        }
        return StdResult.success();
    }
}
