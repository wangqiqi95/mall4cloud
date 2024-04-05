package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 视频号4.0运费模板
 * 
 */
@Data
@TableName("channels_freight_template")
public class ChannelsFreightTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 模板id
	 */
	private Long transportId;
	/**
	 * 模板名称
	 */
	private String transName;
	/**
	 * 微信侧模板id
	 */
	private String wxTemplateId;
	/**
	 * 微信侧模板名称
	 */
	private String wxTemplateName;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**
	 * 是否删除 0 否 1是
	 */
	private Integer isDeleted;
}
