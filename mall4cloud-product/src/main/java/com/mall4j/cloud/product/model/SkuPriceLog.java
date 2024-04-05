package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 
 *
 * @author gmq
 * @date 2022-09-20 10:58:25
 */
@Data
public class SkuPriceLog implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId(type = IdType.AUTO)
    private Long id;

    /**
     * spu货号(123)
     */
    private String spuCode;

    /**
     * sku色码(123-BBK)
     */
    private String priceCode;

    /**
     * sku条码(123-BBK65)
     */
    private String skuCode;

    /**
     * 门店Id
     */
    private Long storeId;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 日志类型：0-同步商品基础数据 1-同步吊牌价 2-同步保护价 3-同步pos价 4-同步库存 5-商品批量改价 6-批量设置渠道价 
     */
    private Integer logType;

    /**
     * 变更价格
     */
    private Long price;

    private Long oldPrice;

    /**
     * 变更库存
     */
    private Integer stock;

    /**
     * 同步库存类型: 1共享库存(可售库存) 2门店库存
     */
    private Integer stockType;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务类型：1渠道价
     */
    private Integer businessType;

	/**
	 * 更新时间
	 */
	protected Date updateTime;

    /**
     * 操作人
     */
    private String updateBy;

    /**
     * 操作备注
     */
    private String remarks;
}
