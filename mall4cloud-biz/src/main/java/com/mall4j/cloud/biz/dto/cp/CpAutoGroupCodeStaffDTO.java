package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DTO
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
@Data
public class CpAutoGroupCodeStaffDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("企业微信用户id")
    private String userId;

}
