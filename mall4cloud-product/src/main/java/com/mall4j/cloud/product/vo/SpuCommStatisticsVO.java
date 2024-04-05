package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品评论统计VO
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
public class SpuCommStatisticsVO {
    @ApiModelProperty(value = "好评率")
    private Double positiveRating;

    @ApiModelProperty(value = "评论数量")
    private Integer number;

    @ApiModelProperty(value = "好评数")
    private Integer praiseNumber;

    @ApiModelProperty(value = "中评数")
    private Integer secondaryNumber;

    @ApiModelProperty(value = "差评数")
    private Integer negativeNumber;

    @ApiModelProperty(value = "有图数")
    private Integer picNumber;

    public Double getPositiveRating() {
        return positiveRating;
    }

    public void setPositiveRating(Double positiveRating) {
        this.positiveRating = positiveRating;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(Integer praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public Integer getSecondaryNumber() {
        return secondaryNumber;
    }

    public void setSecondaryNumber(Integer secondaryNumber) {
        this.secondaryNumber = secondaryNumber;
    }

    public Integer getNegativeNumber() {
        return negativeNumber;
    }

    public void setNegativeNumber(Integer negativeNumber) {
        this.negativeNumber = negativeNumber;
    }

    public Integer getPicNumber() {
        return picNumber;
    }

    public void setPicNumber(Integer picNumber) {
        this.picNumber = picNumber;
    }

    @Override
    public String toString() {
        return "ProdCommEvaluateVO{" +
                "positiveRating=" + positiveRating +
                ", number=" + number +
                ", praiseNumber=" + praiseNumber +
                ", secondaryNumber=" + secondaryNumber +
                ", negativeNumber=" + negativeNumber +
                ", picNumber=" + picNumber +
                '}';
    }
}
