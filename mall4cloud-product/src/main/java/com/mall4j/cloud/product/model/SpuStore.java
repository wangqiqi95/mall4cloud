package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * spu信息
 *
 * @author zhangjie
 * @TableName spu_store
 */
@Data
public class SpuStore {
    /**
     * 门店商品id
     */
    @TableId(type = IdType.AUTO)
    private Long spuStoreId;

    /**
     * spu id
     */
    private Long spuId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 商品编码
     */
    private String spuCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 售价，整数方式保存
     */
    private Long priceFee;

    /**
     * 市场价，整数方式保存
     */
    private Long marketPriceFee;


    private static final long serialVersionUID = 1L;

}