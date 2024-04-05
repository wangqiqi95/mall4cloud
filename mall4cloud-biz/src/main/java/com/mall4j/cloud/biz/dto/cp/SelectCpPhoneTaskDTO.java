package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 引流手机号任务DTO
 *
 * @author gmqimport com.mall4j.cloud.common.database.dto.PageDTO;
 * @date 2023-10-30 17:13:30
 */
@Data
public class SelectCpPhoneTaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty(value = "任务名称",required = false)
    private String taskName;

    @ApiModelProperty(value = "执行开始时间",required = false)
    private Date startTime;

    @ApiModelProperty(value = "执行结束时间",required = false)
    private Date endTime;

    @ApiModelProperty("开启状态：0未开启/1已开启")
    private Integer openStatus;

    @ApiModelProperty(value = "执行员工",required = false)
    private List<Long> staffs;

    @ApiModelProperty("状态：0未开始/1已开始/2进行中/3已结束")
    private Integer status;


}
