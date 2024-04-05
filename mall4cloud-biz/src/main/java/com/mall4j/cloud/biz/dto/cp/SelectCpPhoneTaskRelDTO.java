package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 引流手机号任务关联员工DTO
 *
 * @author gmq
 * @date 2023-10-30 17:13:37
 */
@Data
public class SelectCpPhoneTaskRelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long taskId;

    @ApiModelProperty("员工id")
    private List<Long> staffs;

}
