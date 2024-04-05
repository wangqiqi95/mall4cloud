package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工活码表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class StaffCodeRefDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long relId;

    private Long codeTimeId;

    @ApiModelProperty(value = "员工id",required = true)
    private Long staffId;

    @ApiModelProperty(value = "员工名称",required = true)
    private String staffName;

    @ApiModelProperty(value = "企微用户id",required = true)
    private String userId;

    private Integer type;

    @ApiModelProperty("员工门店id")
    private Long storeId;

    @ApiModelProperty("员工编号")
    private String staffNo;

    @ApiModelProperty("员工手机号")
    private String staffPhone;
}
