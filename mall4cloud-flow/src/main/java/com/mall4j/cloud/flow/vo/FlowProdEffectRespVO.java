package com.mall4j.cloud.flow.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Pineapple
 * @date 2021/5/25 11:04
 */
public class FlowProdEffectRespVO {
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long spuId;
    /**
     * 曝光次数
     */
    @ApiModelProperty("曝光次数")
    private Integer expose;
    /**
     * 曝光人数
     */
    @ApiModelProperty("曝光人数")
    private Integer exposePersonNum;
    /**
     * 加购人数
     */
    @ApiModelProperty("加购人数")
    private Integer addCartPerson;
    /**
     * 加购件数
     */
    @ApiModelProperty("加购件数")
    private Integer addCart;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Integer getExpose() {
        return expose;
    }

    public void setExpose(Integer expose) {
        this.expose = expose;
    }

    public Integer getExposePersonNum() {
        return exposePersonNum;
    }

    public void setExposePersonNum(Integer exposePersonNum) {
        this.exposePersonNum = exposePersonNum;
    }

    public Integer getAddCartPerson() {
        return addCartPerson;
    }

    public void setAddCartPerson(Integer addCartPerson) {
        this.addCartPerson = addCartPerson;
    }

    public Integer getAddCart() {
        return addCart;
    }

    public void setAddCart(Integer addCart) {
        this.addCart = addCart;
    }

    @Override
    public String toString() {
        return "FlowProdEffectRespVO{" +
                "spuId=" + spuId +
                ", expose=" + expose +
                ", exposePersonNum=" + exposePersonNum +
                ", addCartPerson=" + addCartPerson +
                ", addCart=" + addCart +
                '}';
    }
}
