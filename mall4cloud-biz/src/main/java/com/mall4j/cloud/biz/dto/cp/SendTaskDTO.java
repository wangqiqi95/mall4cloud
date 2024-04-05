package com.mall4j.cloud.biz.dto.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 群发任务表DTO
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Data
public class SendTaskDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("标签组")
    private List<StaffCodeTagDTO> tagList;

    @ApiModelProperty("门店员工列表")
    private List<Long> staffList;

    @ApiModelProperty("执行对象是否修改 0 未修改  1已修改")
    private Integer isChange;

    @ApiModelProperty("推送列表")
    private List<TaskPushDTO> pushList;
}
