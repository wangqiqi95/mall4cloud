
package com.mall4j.cloud.user.vo;


/**
 * 积分分类关联信息
 * @author lhd
 */
public class ScoreCategoryConfigVO {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 使用上限比例
     */
    private Double useScoreLimit;
    /**
     * 获取上限比例
     */
    private Double getScoreLimit;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getUseScoreLimit() {
        return useScoreLimit;
    }

    public void setUseScoreLimit(Double useScoreLimit) {
        this.useScoreLimit = useScoreLimit;
    }

    public Double getGetScoreLimit() {
        return getScoreLimit;
    }

    public void setGetScoreLimit(Double getScoreLimit) {
        this.getScoreLimit = getScoreLimit;
    }

    @Override
    public String toString() {
        return "ScoreCategoryConfigVO{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", useScoreLimit=" + useScoreLimit +
                ", getScoreLimit=" + getScoreLimit +
                '}';
    }
}
