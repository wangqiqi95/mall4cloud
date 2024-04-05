package com.mall4j.cloud.product.dto;

import lombok.Data;

import java.util.*;

@Data
public class SpuSeq {

    private Long spuId;
    private String spuCode;
    private Integer seq;
    /**
     * 导航分类ID
     */
    private Long shopCategoryId;
    private Date updateTime;

    public SpuSeq(){}

    public SpuSeq(Long spuId,Integer seq,Long shopCategoryId){
        this.spuId=spuId;
        this.seq=seq;
        this.shopCategoryId=shopCategoryId;
        this.updateTime=new Date();
    }
}
