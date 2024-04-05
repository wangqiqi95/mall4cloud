package com.mall4j.cloud.biz.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * VO
 *
 * @author chaoge
 * @date 2022-03-05
 */
@Data
@ApiModel("admin-直播间vo")
public class LiveRoomVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "微信直播间id")
    private Integer roomId;

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

    @ApiModelProperty(value = "直播间类型 1: 推流，0：手机直播")
    private Integer type;

    @ApiModelProperty(value = "0.竖屏 1.横屏")
    private Integer screenType;

    @ApiModelProperty(value = "是否置顶 1.是 2.不是")
    private Integer roomTop;

    @ApiModelProperty(value = "直播间状态。101：直播中，102：未开始，103已结束，104禁播，105：暂停，106：异常，107：已过期 -1：已删除")
    private Integer liveStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "直播开始时间")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "直播结束时间")
    private Date endTime;

    @ApiModelProperty(value = "主播微信号")
    private String anchorWechat;

    @ApiModelProperty(value = "是否开启官方收录")
    private Integer isFeedsPublic;

    @ApiModelProperty(value = "是否关闭点赞 【0：开启，1：关闭】（若关闭，观众端将隐藏点赞按钮，直播开始后不允许开启）")
    private Integer closeLike;

    @ApiModelProperty(value = "是否关闭货架 【0：开启，1：关闭】（若关闭，观众端将隐藏商品货架，直播开始后不允许开启）")
    private Integer closeGoods;

    @ApiModelProperty(value = "是否关闭评论 【0：开启，1：关闭】（若关闭，观众端将隐藏评论入口，直播开始后不允许开启）")
    private Integer closeComment;

    @ApiModelProperty(value = "是否关闭回放 【0：开启，1：关闭】默认关闭回放（直播开始后允许开启）")
    private Integer closeReplay;

    @ApiModelProperty(value = "是否关闭分享 【0：开启，1：关闭】默认开启分享（直播开始后不允许修改）")
    private Integer closeShare;

    @ApiModelProperty(value = "是否关闭客服 【0：开启，1：关闭】 默认关闭客服（直播开始后允许开启）")
    private Integer closeKf;

    @ApiModelProperty(value = "创建")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
