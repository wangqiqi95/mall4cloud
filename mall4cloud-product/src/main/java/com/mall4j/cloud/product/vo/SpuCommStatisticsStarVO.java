package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品评论统计(按星数）
 *
 * @Author lth
 * @Date 2021/6/1 9:41
 */
public class SpuCommStatisticsStarVO {

    @ApiModelProperty(value = "五星")
    private Integer fiveStarsNumber;

    @ApiModelProperty(value = "四星")
    private Integer fourStarsNumber;

    @ApiModelProperty(value = "三星")
    private Integer threeStarsNumber;

    @ApiModelProperty(value = "二星")
    private Integer twoStarsNumber;

    @ApiModelProperty(value = "一星")
    private Integer oneStarsNumber;

    public Integer getFiveStarsNumber() {
        return fiveStarsNumber;
    }

    public void setFiveStarsNumber(Integer fiveStarsNumber) {
        this.fiveStarsNumber = fiveStarsNumber;
    }

    public Integer getFourStarsNumber() {
        return fourStarsNumber;
    }

    public void setFourStarsNumber(Integer fourStarsNumber) {
        this.fourStarsNumber = fourStarsNumber;
    }

    public Integer getThreeStarsNumber() {
        return threeStarsNumber;
    }

    public void setThreeStarsNumber(Integer threeStarsNumber) {
        this.threeStarsNumber = threeStarsNumber;
    }

    public Integer getTwoStarsNumber() {
        return twoStarsNumber;
    }

    public void setTwoStarsNumber(Integer twoStarsNumber) {
        this.twoStarsNumber = twoStarsNumber;
    }

    public Integer getOneStarsNumber() {
        return oneStarsNumber;
    }

    public void setOneStarsNumber(Integer oneStarsNumber) {
        this.oneStarsNumber = oneStarsNumber;
    }

    @Override
    public String toString() {
        return "SpuCommStatisticsStarVO{" +
                "fiveStarsNumber=" + fiveStarsNumber +
                ", fourStarsNumber=" + fourStarsNumber +
                ", threeStarsNumber=" + threeStarsNumber +
                ", twoStarsNumber=" + twoStarsNumber +
                ", oneStarsNumber=" + oneStarsNumber +
                '}';
    }
}
