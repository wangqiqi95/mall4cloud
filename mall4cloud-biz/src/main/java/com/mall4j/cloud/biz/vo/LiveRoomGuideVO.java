package com.mall4j.cloud.biz.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VO
 *
 * @author chaoge
 * @date 2022-03-05
 */
@Data
@ApiModel("导购-直播间")
public class LiveRoomGuideVO {

    @ApiModelProperty("微信直播间id")
    private Long roomId;

    @ApiModelProperty("背景图")
    private String coverImg;

    @ApiModelProperty("主播分享图")
    private String shareImg;

    @ApiModelProperty("购物直播频道封面图")
    private String feedsImg;

    @ApiModelProperty("直播间名称")
    private String name;

    @ApiModelProperty("直播开始时间")
    private Date startTime;

    @ApiModelProperty("直播结束时间")
    private Date endTime;

    @ApiModelProperty("直播间状态。101：直播中，102：未开始，103已结束，104禁播，105：暂停，106：异常，107：已过期")
    private Integer liveStatus;

}
