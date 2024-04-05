package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 群发任务客户关联表VO
 *
 * @author gmq
 * @date 2023-10-30 14:23:04
 */
@Data
public class CpTaskUserRefVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("客户id")
    private Long userId;

    @ApiModelProperty("客户姓名")
    private String userName;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("任务id")
    private Long taskId;

}
