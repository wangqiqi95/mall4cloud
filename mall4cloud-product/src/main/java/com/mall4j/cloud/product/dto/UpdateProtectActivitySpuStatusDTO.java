package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * DTO
 *
 * @author gmq
 * @date 2022-06-14 15:17:31
 */
@Data
public class UpdateProtectActivitySpuStatusDTO {
    private static final long serialVersionUID = 1L;

    private List<ProtectActivitySpuStatusDTO> updateIds;

}
