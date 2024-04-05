package com.mall4j.cloud.biz.dto.cp.analyze;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动拉群活码表DTO
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Data
public class CpTagGroupCodeAnalyzeStaffDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("状态：0进行中/1已完成")
    private Integer status;

    @ApiModelProperty(value = "接待人员",required = false)
    private List<Long> staffs=new ArrayList<>();

    @ApiModelProperty("活码id")
    private Long codeId;

    @ApiModelProperty("标签建群任务id")
    private Long taskId;

    private String staffName;
}
