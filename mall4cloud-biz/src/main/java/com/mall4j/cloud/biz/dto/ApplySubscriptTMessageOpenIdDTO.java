package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户接受发送订阅消息请求对象
 *
 * @luzhengxiang
 * @create 2022-03-23 11:35 AM
 **/
@Data
public class ApplySubscriptTMessageOpenIdDTO {

    @ApiModelProperty(value = "临时用户ID 不能成功登录传临时用户id")
    private String tempUid;

    @ApiModelProperty(value = "用户id 能成功登录传用户id")
    private Long userid;

    @ApiModelProperty("业务id")
    private String bussinessId;

    @ApiModelProperty("业务消息模版id")
    @NotBlank(message = "模板id不能为空")
    private String templateId;

    @ApiModelProperty("是否订阅成功")
    private boolean subscriptSucc=true;

}
