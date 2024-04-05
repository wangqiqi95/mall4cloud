package com.mall4j.cloud.user.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Peter_Tan
 * @date 2023.03.14
 */
@Data
public class GroupSonTaskSendRecordBO {

    @ApiModelProperty(value = "群发任务ID")
    private Long taskId;

    @ApiModelProperty(value = "群发子任务ID")
    private Long sonTaskId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "0非最终发送，1最终发送（完成）")
    private Integer final_send;

    @ApiModelProperty(value = "推送类型：1:1v1，2:批量群发")
    private String send_model;

}
