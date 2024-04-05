package com.mall4j.cloud.biz.vo.cp.analyze;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * VO
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
@Data
public class CpTagGroupCodeStaffVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("状态：0进行中/1已完成")
    private Integer status;

    @ApiModelProperty("执行次数")
    private Integer executeCount;

    @ApiModelProperty("未执行次数")
    private Integer noExecuteCount;

}
