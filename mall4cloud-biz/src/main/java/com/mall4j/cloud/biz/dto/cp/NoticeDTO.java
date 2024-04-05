package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 通知消息DTO
 *
 */
@Data
public class NoticeDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("内容")
    private String content;

	@ApiModelProperty("推送时间")
	private String pushTime;

    @ApiModelProperty("推送对象")
    private String pushObj;
    @ApiModelProperty("消息url")
    private String msgUrl;
    @ApiModelProperty("消息类型 1好友流失，2跟进提醒，3素材浏览提醒，4敏感词命中，5超时回复")
    private String msgType;

}
