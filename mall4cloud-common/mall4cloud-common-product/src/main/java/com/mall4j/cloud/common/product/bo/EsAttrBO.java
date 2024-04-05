package com.mall4j.cloud.common.product.bo;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
public class EsAttrBO {

    /**
     * 规格id
     */
    private Long attrId;

    /**
     * 中文规格名
     */
    private String attrNameZh;

    /**
     * 英文规格名
     */
    private String attrNameEn;

    /**
     * 规格值id
     */
    private Long attrValueId;

    /**
     * 中文规格值名称
     */
    private String attrValueNameZh;

    /**
     * 英文规格值名称
     */
    private String attrValueNameEn;

    public Long getAttrId() {
        return attrId;
    }

    public void setAttrId(Long attrId) {
        this.attrId = attrId;
    }

    public String getAttrNameZh() {
        return attrNameZh;
    }

    public void setAttrNameZh(String attrNameZh) {
        this.attrNameZh = attrNameZh;
    }

    public String getAttrNameEn() {
        return attrNameEn;
    }

    public void setAttrNameEn(String attrNameEn) {
        this.attrNameEn = attrNameEn;
    }

    public Long getAttrValueId() {
        return attrValueId;
    }

    public void setAttrValueId(Long attrValueId) {
        this.attrValueId = attrValueId;
    }

    public String getAttrValueNameZh() {
        return attrValueNameZh;
    }

    public void setAttrValueNameZh(String attrValueNameZh) {
        this.attrValueNameZh = attrValueNameZh;
    }

    public String getAttrValueNameEn() {
        return attrValueNameEn;
    }

    public void setAttrValueNameEn(String attrValueNameEn) {
        this.attrValueNameEn = attrValueNameEn;
    }

    @Override
    public String toString() {
        return "EsAttrBO{" +
                "attrId=" + attrId +
                ", attrNameZh='" + attrNameZh + '\'' +
                ", attrNameEn='" + attrNameEn + '\'' +
                ", attrValueId=" + attrValueId +
                ", attrValueNameZh='" + attrValueNameZh + '\'' +
                ", attrValueNameEn='" + attrValueNameEn + '\'' +
                '}';
    }
}
