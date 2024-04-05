package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工信息DTO
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Data
public class ImportStoreDTO {

    @ApiModelProperty("节点id")
    private Long orgIds;

}
