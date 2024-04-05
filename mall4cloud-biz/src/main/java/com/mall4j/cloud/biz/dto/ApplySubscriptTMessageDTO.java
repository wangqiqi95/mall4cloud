package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户接受发送订阅消息请求对象
 *
 * @luzhengxiang
 * @create 2022-03-23 11:35 AM
 **/
@Data
public class ApplySubscriptTMessageDTO {

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;

    @ApiModelProperty("业务id")
    private String bussinessId;
    @ApiModelProperty("业务消息模版id")
    private String templateId;

}
