package com.mall4j.cloud.biz.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * VO
 *
 * @author FrozenWatermelon
 * @date 2021-01-25 09:32:38
 */
public class SmsCodeTemplateVO{

    @ApiModelProperty("通知内容")
    private String message;

    @ApiModelProperty("短信模板code")
    private String templateCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    @Override
    public String toString() {
        return "SmsCodeTemplateVO{" +
                "message='" + message + '\'' +
                ", templateCode='" + templateCode + '\'' +
                '}';
    }
}
