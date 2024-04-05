package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * DTO
 *
 */
@Data
public class CpStaffCodeLogDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("员工编号")
    private String staffNo;

    @ApiModelProperty("员工手机号")
    private String staffPhone;

    @ApiModelProperty("失败原因")
    private String logs;

    @ApiModelProperty("微信返回失败日志")
    private String wxerror;

    @ApiModelProperty("微信返回失败状态码")
    private String errorCode;

    @ApiModelProperty("0失败 1成功")
    private Integer status;

    @ApiModelProperty("欢迎语内容")
    private String attachmentExt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("0正常 1删除")
    private Integer delFlag;

    protected Date createTime;

}
