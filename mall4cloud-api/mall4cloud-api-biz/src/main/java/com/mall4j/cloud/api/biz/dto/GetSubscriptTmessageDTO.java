package com.mall4j.cloud.api.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询小程序订阅消息模版和待发送用户列表参数对象
 *
 * @luzhengxiang
 * @create 2022-03-23 2:02 PM
 **/
@Data
public class GetSubscriptTmessageDTO {

    @ApiModelProperty(value = "发送类型",required = true)
    private Integer sendType;

    @ApiModelProperty("业务单据id")
    private String bussinessId;
}
