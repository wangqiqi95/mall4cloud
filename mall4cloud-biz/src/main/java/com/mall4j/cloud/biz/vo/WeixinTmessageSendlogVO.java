package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信模板消息推送日志VO
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 17:13:37
 */
@Data
public class WeixinTmessageSendlogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("接收用户openId")
    private String openId;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("消息ID")
    private String msgId;

    @ApiModelProperty("消息模板id")
    private String templateId;

    @ApiModelProperty("消息内容")
    private String dataJson;

    @ApiModelProperty("状态，0：成功，1，失败")
    private Integer status;

    @ApiModelProperty("公众号appId")
    private String appId;

	@ApiModelProperty("小程序appId")
	private String maAppId;

	@ApiModelProperty("本系统微信消息模板id")
	private String tmessageId;

	@ApiModelProperty("跳小程序所需数据")
	private String miniprogram;

	@ApiModelProperty("跳转到小程序的具体页面路径")
	private String pagepath;

}
