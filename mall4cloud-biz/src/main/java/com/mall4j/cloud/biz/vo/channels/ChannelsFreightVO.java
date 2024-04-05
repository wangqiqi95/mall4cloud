package com.mall4j.cloud.biz.vo.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 视频号运费模板VO
 */
@Data
public class ChannelsFreightVO {
	
	@ApiModelProperty("主键id")
	private Long id;

	@ApiModelProperty("模板id")
	private Long transportId;

	@ApiModelProperty("模板名称")
	private String transName;

	@ApiModelProperty("微信侧模板id")
	private String wxTemplateId;

	@ApiModelProperty("微信侧模板名称")
	private String wxTemplateName;

	@ApiModelProperty("创建人")
	private String createBy;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("更新人")
	private String updateBy;
	
	@ApiModelProperty("更新时间")
	private Date updateTime;
}
