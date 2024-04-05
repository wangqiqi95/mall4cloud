package com.mall4j.cloud.biz.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@Data
public class WeixinShortlinkRecordVo{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long id;

	@ApiModelProperty("原链接路径")
	private String originalLink;

	@ApiModelProperty("携带参数")
	private String scene;

	@ApiModelProperty("短链路径")
	private String shortLink;

	@ApiModelProperty("打开次数")
	private Integer openNumber;

	@ApiModelProperty("打开人数")
	private Integer openPeople;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("创建人")
	private String createBy;

}
