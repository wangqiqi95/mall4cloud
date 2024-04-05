package com.mall4j.cloud.api.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信小程序订阅模版消息类型 可选值列表VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
@Data
public class WeixinMaSubscriptTmessageValueVO extends BaseVO{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	private Long id;

	@ApiModelProperty("系统模板ID")
	private String templateId;

	@ApiModelProperty("模板值中文名称(如：签到名称)")
	private String templateKeyName;

	@ApiModelProperty("模板值名称 微信返回的名称(如：{{phrase1.DATA}} 中的phrase1)")
	private String templateValueName;

	@ApiModelProperty("模板值value")
	private String templateValue;

}
