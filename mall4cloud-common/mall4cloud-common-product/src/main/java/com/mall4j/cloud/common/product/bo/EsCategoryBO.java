package com.mall4j.cloud.common.product.bo;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
public class EsCategoryBO {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 中文分类名称
     */
    private String categoryNameZh;

    /**
     * 英文分类名称
     */
    private String categoryNameEn;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryNameZh() {
        return categoryNameZh;
    }

    public void setCategoryNameZh(String categoryNameZh) {
        this.categoryNameZh = categoryNameZh;
    }

    public String getCategoryNameEn() {
        return categoryNameEn;
    }

    public void setCategoryNameEn(String categoryNameEn) {
        this.categoryNameEn = categoryNameEn;
    }

    @Override
    public String toString() {
        return "EsCategoryBO{" +
                "categoryId=" + categoryId +
                ", categoryNameZh='" + categoryNameZh + '\'' +
                ", categoryNameEn='" + categoryNameEn + '\'' +
                '}';
    }
}
