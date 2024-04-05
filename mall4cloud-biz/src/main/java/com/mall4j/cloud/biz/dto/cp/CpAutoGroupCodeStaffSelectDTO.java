package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * DTO
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
@Data
public class CpAutoGroupCodeStaffSelectDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活码id")
    private Long codeId;

    @ApiModelProperty("员工id")
    private List<Long> staffs;
}
