package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 引流手机号任务关联员工DTO
 *
 * @author gmq
 * @date 2023-10-30 17:13:37
 */
@Data
public class CpPhoneTaskStaffDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("员工id")
    private Long staffId;

}
