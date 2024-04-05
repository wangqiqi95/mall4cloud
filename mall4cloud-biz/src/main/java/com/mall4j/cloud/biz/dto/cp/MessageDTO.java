package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用消息配置DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("消息名称")
    private String msgName;

    @ApiModelProperty("消息类型 1:每天  2：添加微信好友 3：好友/粉丝成功注册成为会员 4：被好友删除 5：服务关系变更")
    private Integer msgType;

    @ApiModelProperty("消息内容")
    private String msgContent;

    @ApiModelProperty("消息推送时间：当msgType为 1，2时 必须有值 ")
    private String pushTime;

    @ApiModelProperty("消息推送对象")
    private String toUser;

    @ApiModelProperty("变量名")
    private String paramName;

    @ApiModelProperty("消息url")
    private String msgUrl;

    @ApiModelProperty("创建人")
    private Integer createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("消息状态 1启用 0 禁用")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;

	@Override
	public String toString() {
		return "MessageDTO{" +
				"id=" + id +
				",msgName=" + msgName +
				",msgContent=" + msgContent+
				",pushTime=" + pushTime +
				",toUser=" + toUser +
				",paramName=" + paramName +
				",msgUrl=" + msgUrl +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
