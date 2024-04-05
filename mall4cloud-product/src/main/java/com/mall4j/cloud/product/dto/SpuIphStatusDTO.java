package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.product.dto.SpuDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品分组表DTO
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
@Data
public class SpuIphStatusDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品id")
    private Long id;

    @ApiModelProperty("铺货状态: 0未铺货 1已铺货")
    private Integer iphStatus;
}
