package com.mall4j.cloud.api.product.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description 修改spu状态
 * @Author axin
 * @Date 2022-10-09 11:39
 **/
@Data
public class ChangeSpuStatusDto {
    private List<Long> spuIds;

    /**
     * -1:删除, 0:下架, 1:上架, 2:平台下架, 3: 等待审核
     */
    private Integer status;
}
