package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 客群分配明细表 DTO
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
@Data
public class CustGroupAssignDetailDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("接替员工")
    private Long replaceBy;

}
