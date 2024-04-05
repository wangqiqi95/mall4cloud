package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 电子价签管理VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
@Data
public class BatchSkuStoreVO{
    private static final long serialVersionUID = 1L;

    private Long skuId;

    private String skuCode;

    private Integer status;

    private Integer skuStatus;

    private Long skuStoreId;

    private Long storeId;


}
