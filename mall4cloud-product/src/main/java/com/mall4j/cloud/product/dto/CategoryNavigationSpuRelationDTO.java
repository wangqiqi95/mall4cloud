package com.mall4j.cloud.product.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @date 2023/6/14
 */
@Data
public class CategoryNavigationSpuRelationDTO {

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 商品SpuId
     */
    @ApiModelProperty("商品SPUID集合")
    private List<Long> spuIds;
}
