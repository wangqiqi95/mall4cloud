package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 群发任务人工关联表DTO
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Data
public class TaskStaffRefDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "员工id",required = true)
    private Long staffId;

    @ApiModelProperty(value = "员工企业微信id",required = true)
    private String userId;

    @ApiModelProperty("员工姓名")
    private String staffName;

//    @ApiModelProperty("类型")
//    private Integer type;
//
//    @ApiModelProperty("任务id")
//    private Long taskId;

}
