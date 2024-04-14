package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskExecuteDetailInfoSearchParamDTO {
    @ApiModelProperty("调度id。一个任务配置会生成多次任务，多次任务的id即为调度id")
    private String executeId;
    @ApiModelProperty("客户昵称/客户群名称")
    private String nickName;
    @ApiModelProperty("状态 1完成 0未完成")
    private Integer status;
    @ApiModelProperty("任务类型为加企微好友时特有字段。0未添加 1已添加 2添加失败")
    private Integer addStatus;
}
