package com.mall4j.cloud.api.group.feign.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * VO
 *
 * @author gmq
 * @date 2022-06-07 11:42:41
 */
@Data
public class ProtectSpuVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("商品货号")
    private String spuCode;

    @ApiModelProperty("电商保护价")
    private Long protectPrice;

}
