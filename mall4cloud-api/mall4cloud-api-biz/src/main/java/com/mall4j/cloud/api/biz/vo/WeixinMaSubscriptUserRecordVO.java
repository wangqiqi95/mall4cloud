package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信小程序用户订阅记录VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-23 11:10:56
 */
@Data
public class WeixinMaSubscriptUserRecordVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("模版id")
    private String templateId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("小程序openid")
    private String openId;

    @ApiModelProperty("业务单据id")
    private String bussinessId;

    @ApiModelProperty("发送状态 0未发送，1已经发送")
    private Long sendStatus;

    @ApiModelProperty("发送类型")
    private Long sendType;

}
