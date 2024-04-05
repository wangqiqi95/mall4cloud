package com.mall4j.cloud.api.product.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @luzhengxiang
 * @create 2022-03-15 10:35 AM
 **/
@Data
public class SpuListDTO extends PageDTO {

    /**
     * 参考EsProductSortEnum
     */
    @ApiModelProperty(value = "排序：1新品,2销量倒序,3销量正序,4商品价格倒序,5商品价格正序,6评论")
    private Integer sort;

    @ApiModelProperty(value = "spuIds")
    private List<Long> spuIds;

    /**
     * 状态 -1:删除, 0:下架, 1:上架, 2:平台下架, 3: 等待审核
     */
    @ApiModelProperty(value = "状态",hidden = true)
    private Integer status;

}
