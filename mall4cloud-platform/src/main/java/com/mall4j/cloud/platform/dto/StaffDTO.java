package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 员工信息DTO
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Data
public class StaffDTO{

    @ApiModelProperty("员工id")
    private Long id;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String mobile;

    @NotBlank(message = "员工工号不能为空")
    @ApiModelProperty("员工工号")
    private String staffNo;

    @ApiModelProperty("员工编码")
    private String staffCode;

    @NotBlank(message = "员工名称不能为空")
    @ApiModelProperty("员工名称")
    private String staffName;

    @NotNull(message = "门店ID不能为空")
    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("邮件")
    private String email;

    @ApiModelProperty("职位")
    private String position;

    @NotNull(message = "角色不能为空")
    @ApiModelProperty("角色 1-导购 2-店长 3-店务")
    private Integer roleType;

    @ApiModelProperty("头像")
    private String avatar;

}
