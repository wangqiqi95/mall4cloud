package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaitMatterCountVO {

    @ApiModelProperty("员工标签建群待办统计")
    private Integer tagGroupCount;

    @ApiModelProperty("员工手机号待办统计")
    private Integer taskPhoneCount;

    @ApiModelProperty("客户群发待办统计")
    private Integer customSendCount;

    @ApiModelProperty("群群发待办统计")
    private Integer groupSendCount;

}
