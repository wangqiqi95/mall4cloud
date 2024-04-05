package com.mall4j.cloud.common.product.bo;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
public class EsAttrLangBO {

    /**
     * 规格id
     */
    private Integer lang;

    /**
     * 规格名
     */
    private String attrName;

    /**
     * 规格值名称
     */
    private String attrValueName;

    public Integer getLang() {
        return lang;
    }

    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValueName() {
        return attrValueName;
    }

    public void setAttrValueName(String attrValueName) {
        this.attrValueName = attrValueName;
    }

    @Override
    public String toString() {
        return "EsAttrLangBO{" +
                "lang=" + lang +
                ", attrName='" + attrName + '\'' +
                ", attrValueName='" + attrValueName + '\'' +
                '}';
    }
}
