package com.mall4j.cloud.api.biz.dto.livestore.response.complaint;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FlowInfo {

    @ApiModelProperty("操作对象，根据对象查看下面字段详细的操作记录，" +
            "1\t用户操作\n" +
            "2\t平台操作\n" +
            "3\t商家操作\n")
    private Integer object;

    @ApiModelProperty("操作对象，根据对象查看下面字段详细的操作记录，定义见 HandleObject")
    private OPHandleRecord op_record;
    @ApiModelProperty("操作对象，根据对象查看下面字段详细的操作记录，定义见 HandleObject")
    private UserHandleRecord user_record;
    @ApiModelProperty("操作对象，根据对象查看下面字段详细的操作记录，定义见 HandleObject")
    private MerchantHandleRecord merchant_record;

}
