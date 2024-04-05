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
public class WeixinShortlinkRecordItemVo{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long id;

	@ApiModelProperty("短链ID")
	private String shortLinkId;

	@ApiModelProperty("短链链接路径")
	private String shortLinkUrl;

	@ApiModelProperty("用户uniId")
	private String uniId;

	@ApiModelProperty("会员ID/会员编号")
	private String vipCode;

	@ApiModelProperty("会员名称/用户昵称")
	private String nickName;

	@ApiModelProperty(value = "服务门店id")
	private Long staffStoreId;

	@ApiModelProperty(value = "服务门店")
	private String staffStoreName;

	@ApiModelProperty(value = "服务门店Code")
	private String staffStoreCode;

	@ApiModelProperty("查看时间")
	private Date checkTime;

}
