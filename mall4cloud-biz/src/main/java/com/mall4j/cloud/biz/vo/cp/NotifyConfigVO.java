package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 应用消息配置VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NotifyConfigVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("消息名称")
    private String msgName;

    @ApiModelProperty("消息类型")
    private Integer msgType;

    @ApiModelProperty("消息内容")
    private String msgContent;

    @ApiModelProperty("消息推送时间")
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

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;


	@Override
	public String toString() {
		return "MessageVO{" +
				"id=" + id +
				",msgName=" + msgName +
				",msgContent=" + msgContent +
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
