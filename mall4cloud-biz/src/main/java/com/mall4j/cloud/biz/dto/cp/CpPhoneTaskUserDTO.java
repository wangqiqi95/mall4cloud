package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 引流手机号任务关联客户DTO
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Data
public class CpPhoneTaskUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("手机号库id")
    private Long phoneUserId;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("执行员工id")
    private Long staffId;

}
