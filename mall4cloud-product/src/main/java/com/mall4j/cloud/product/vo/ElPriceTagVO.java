package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import com.mall4j.cloud.product.dto.ElPriceProdDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 电子价签管理VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
@Data
public class ElPriceTagVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("商品数量")
    private Integer prodCount;

    @ApiModelProperty("商品集合")
    private List<ElPriceProdVO> prodList;

}
