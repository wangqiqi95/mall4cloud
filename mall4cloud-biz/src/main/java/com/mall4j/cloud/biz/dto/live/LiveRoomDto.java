

package com.mall4j.cloud.biz.dto.live;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lhd
 * @date 2020-08-06 16:29:53
 */
@Data
@ApiModel("app-直播间信息")
public class LiveRoomDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播间信息
     */
    @ApiModelProperty(value = "直播间id")
    private Long id;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "微信直播间id")
    private Long roomId;

    @ApiModelProperty(value = "直播间名称")
    private String name;

    @ApiModelProperty(value = "主播昵称")
    private String anchorName;

    @ApiModelProperty(value = "背景图")
    private String coverImg;

    @ApiModelProperty(value = "主播分享图")
    private String shareImg;

    @ApiModelProperty(value = "购物直播频道封面图")
    private String feedsImg;

    @ApiModelProperty(value = "直播间类型 1.推流 2.手机直播")
    private Integer type;

    @ApiModelProperty(value = "1.竖屏 2.横屏")
    private Integer screenType;

    @ApiModelProperty(value = "是否置顶 1.是 2.不是")
    private Integer roomTop;

    @ApiModelProperty(value = "直播间状态。101：直播中，102：未开始，103已结束，104禁播，105：暂停，106：异常，107：已过期")
    private Integer liveStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "直播开始时间")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "直播结束时间")
    private Date endTime;
}
