package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * DTO
 *
 * @author gmq
 * @date 2022-06-14 15:17:31
 */
@Data
public class ProtectActivitySpuPageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品货号")
    private String spuCode;

    @ApiModelProperty("状态：0生效 1失效")
    private Integer status;

    @ApiModelProperty("执行状态：0待审核 1待执行 2进行中 3已结束 4驳回")
    private Integer exStatus;

}
