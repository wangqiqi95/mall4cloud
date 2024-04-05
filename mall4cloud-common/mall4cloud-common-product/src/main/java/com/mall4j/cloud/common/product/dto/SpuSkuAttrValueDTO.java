package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品sku销售属性关联信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SpuSkuAttrValueDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品sku销售属性关联信息id")
    private Long spuSkuAttrId;

    @ApiModelProperty("SPU ID")
    private Long spuId;

    @ApiModelProperty("SKU ID")
    private Long skuId;

    @ApiModelProperty("语言 0.通用 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("销售属性ID")
    private Long attrId;

    @ApiModelProperty("销售属性名称")
    private String attrName;

    @ApiModelProperty("销售属性值ID")
    private Long attrValueId;

    @ApiModelProperty("销售属性值")
    private String attrValueName;

    @ApiModelProperty("状态 1:enable, 0:disable")
    private Integer status;

    @ApiModelProperty("规格图片")
    private String imgUrl;

    private List<SpuSkuAttrValueLangDTO> spuSkuAttrValueLangList;

    public Long getSpuSkuAttrId() {
        return spuSkuAttrId;
    }

    public void setSpuSkuAttrId(Long spuSkuAttrId) {
        this.spuSkuAttrId = spuSkuAttrId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getLang() {
        return lang;
    }

    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public Long getAttrId() {
        return attrId;
    }

    public void setAttrId(Long attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public Long getAttrValueId() {
        return attrValueId;
    }

    public void setAttrValueId(Long attrValueId) {
        this.attrValueId = attrValueId;
    }

    public String getAttrValueName() {
        return attrValueName;
    }

    public void setAttrValueName(String attrValueName) {
        this.attrValueName = attrValueName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SpuSkuAttrValueLangDTO> getSpuSkuAttrValueLangList() {
        return spuSkuAttrValueLangList;
    }

    public void setSpuSkuAttrValueLangList(List<SpuSkuAttrValueLangDTO> spuSkuAttrValueLangList) {
        this.spuSkuAttrValueLangList = spuSkuAttrValueLangList;
    }

    @Override
    public String toString() {
        return "SpuSkuAttrValueDTO{" +
                "spuSkuAttrId=" + spuSkuAttrId +
                ", spuId=" + spuId +
                ", skuId=" + skuId +
                ", lang=" + lang +
                ", attrId=" + attrId +
                ", attrName='" + attrName + '\'' +
                ", attrValueId=" + attrValueId +
                ", attrValueName='" + attrValueName + '\'' +
                ", status=" + status +
                ", spuSkuAttrValueLangList=" + spuSkuAttrValueLangList +
                '}';
    }
}
