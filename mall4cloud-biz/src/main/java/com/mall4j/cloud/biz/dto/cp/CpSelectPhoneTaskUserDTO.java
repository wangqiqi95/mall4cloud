package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 引流手机号任务关联客户DTO
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Data
public class CpSelectPhoneTaskUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务id",required = true)
    private Long taskId;

    @ApiModelProperty(value = "手机号",required = false)
    private String phone;

    @ApiModelProperty(value = "昵称",required = false)
    private String nickName;

    @ApiModelProperty(value = "搜索关键字",required = false)
    private String searchKey;

    @ApiModelProperty(value = "导入来源：0表格导入/1系统用户",required = false)
    private Integer importFrom;

    @ApiModelProperty(value = "执行员工id",required = false)
    private List<Long> staffs;

    @ApiModelProperty(value = "开始时间",required = false)
    private String startTime;

    @ApiModelProperty(value = "结束时间",required = false)
    private String endTime;

}
