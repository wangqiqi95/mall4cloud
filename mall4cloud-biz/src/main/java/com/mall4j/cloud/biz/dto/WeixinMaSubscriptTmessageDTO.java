package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 微信小程序订阅模版消息DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:44
 */
@Data
public class WeixinMaSubscriptTmessageDTO{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	private String id;

	@ApiModelProperty("小程序模版code")
	private String templateCode;

	@ApiModelProperty("业务类型：1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核")
	private Integer businessType;

	@ApiModelProperty("100.会员生日到期提醒 200.优惠券到期提醒 201.优惠券到账提醒 300.积分变更提醒 301.等级到期提醒 " +
			" 400.订单发货提醒  401.订单签收提醒 500.退单审核结果提醒 501.退单完成提醒 " +
			"600.活动上新提醒 601.活动开始提醒 602.活动结束提醒")
	private Integer sendType;

	@ApiModelProperty("微信模板标题")
	private String templateTitle;

	@ApiModelProperty("模板示例")
	private String example;

	@ApiModelProperty("小程序appid")
	private String appId;

	@ApiModelProperty("要跳转的页面地址，可以拼接参数。")
	private String page;

	@ApiModelProperty("系统模板名称")
	private String title;

	@ApiModelProperty("模版类型，2 为一次性订阅，3 为长期订阅")
	private Integer type;

	@ApiModelProperty("模版内容")
	private String content;

	@ApiModelProperty("0禁用 1启用")
	private Integer status;

	@ApiModelProperty("备注")
	private String remarks;

	@ApiModelProperty("模版内容项")
	private List<WeixinMaSubscriptTmessageValueDTO> contentValues;

}
