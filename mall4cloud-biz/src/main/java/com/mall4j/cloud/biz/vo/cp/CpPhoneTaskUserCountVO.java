package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 引流手机号任务关联客户VO
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Data
public class CpPhoneTaskUserCountVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer actualTotal;

    @ApiModelProperty("任务id")
    private Long taskId;

}
