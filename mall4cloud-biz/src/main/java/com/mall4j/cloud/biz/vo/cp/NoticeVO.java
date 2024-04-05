package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知消息表VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

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
    @ApiModelProperty("消息类型")
    private String msgType;

}
