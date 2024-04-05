package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * DTO
 *
 */
@Data
public class ReqStaffCodeLogDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工id")
    private List<Long> staffIds;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("过滤不包含错误码数据")
    private List<String> noErrorCodes;

}
