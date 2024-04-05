package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DTO
 *
 * @author gmq
 * @date 2023-12-06 16:14:28
 */
@Data
public class CpMaterialMsgImgSelectDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("素材id")
    private Long matId;

    @ApiModelProperty("素材消息id")
    private Long matMsgId;

}
