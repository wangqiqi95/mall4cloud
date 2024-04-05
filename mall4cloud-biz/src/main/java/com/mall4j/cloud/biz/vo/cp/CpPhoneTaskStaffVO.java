package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 引流手机号任务关联员工VO
 *
 * @author gmq
 * @date 2023-10-30 17:13:37
 */
@Data
public class CpPhoneTaskStaffVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("员工名称")
    private String staffName;

}
