package com.mall4j.cloud.common.product.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
public class EsBrandBO {

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 品牌图片
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String brandImg;

    /**
     * 中文品牌名称
     */
    private String brandNameZh;

    /**
     * 英文品牌名称
     */
    private String brandNameEn;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandImg() {
        return brandImg;
    }

    public void setBrandImg(String brandImg) {
        this.brandImg = brandImg;
    }

    public String getBrandNameZh() {
        return brandNameZh;
    }

    public void setBrandNameZh(String brandNameZh) {
        this.brandNameZh = brandNameZh;
    }

    public String getBrandNameEn() {
        return brandNameEn;
    }

    public void setBrandNameEn(String brandNameEn) {
        this.brandNameEn = brandNameEn;
    }

    @Override
    public String toString() {
        return "EsBrandBO{" +
                "brandId=" + brandId +
                ", brandImg='" + brandImg + '\'' +
                ", brandNameZh='" + brandNameZh + '\'' +
                ", brandNameEn='" + brandNameEn + '\'' +
                '}';
    }
}
