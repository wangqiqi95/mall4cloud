

package com.mall4j.cloud.biz.dto.live;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lhd
 * @date 2020-08-06 16:29:53
 */
@Data
public class LiveRoomParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "直播间名称", required = true)
    private String name;

    @ApiModelProperty(value = "主播昵称", required = true)
    private String anchorName;

    @ApiModelProperty(value = "主播微信号", required = true)
    private String anchorWechat;

    @ApiModelProperty(value = "是否置顶 1.是 0.不是")
    private Integer roomTop;

    @ApiModelProperty(value = "背景图", required = true)
    private String coverImg;

    @ApiModelProperty(value = "背景图微信的media_id", required = true)
    private String coverImgId;

    @ApiModelProperty(value = "分享图", required = true)
    private String shareImg;

    @ApiModelProperty(value = "分享图微信的media_id", required = true)
    private String shareImgId;

    @ApiModelProperty(value = "封面图", required = true)
    private String feedsImg;

    @ApiModelProperty(value = "封面图微信的media_id", required = true)
    private String feedsImgId;

    @ApiModelProperty(value = "是否开启官方收录 1: 是，0：否", required = true)
    private Integer isFeedsPublic;

    @ApiModelProperty(value = "直播间类型 1: 推流，0：手机直播", required = true)
    private Integer type;

    @ApiModelProperty(value = "1.竖屏 2.横屏", required = true)
    private Integer screenType;

    @ApiModelProperty(value = "是否关闭点赞 【0：开启，1：关闭】（若关闭，观众端将隐藏点赞按钮，直播开始后不允许开启）", required = true)
    private Integer closeLike;

    @ApiModelProperty(value = "是否关闭货架 【0：开启，1：关闭】（若关闭，观众端将隐藏商品货架，直播开始后不允许开启）", required = true)
    private Integer closeGoods;

    @ApiModelProperty(value = "是否关闭评论 【0：开启，1：关闭】（若关闭，观众端将隐藏评论入口，直播开始后不允许开启）", required = true)
    private Integer closeComment;

    @ApiModelProperty(value = "是否关闭回放 【0：开启，1：关闭】默认关闭回放（直播开始后允许开启）", required = true)
    private Integer closeReplay;

    @ApiModelProperty(value = "是否关闭分享 【0：开启，1：关闭】默认开启分享（直播开始后不允许修改）", required = true)
    private Integer closeShare;

    @ApiModelProperty(value = "是否关闭客服 【0：开启，1：关闭】 默认关闭客服（直播开始后允许开启）", required = true)
    private Integer closeKf;

    @ApiModelProperty(value = "直播开始时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "直播结束时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "开播门店", required = true)
    private List<Long> shops;

    @ApiModelProperty(value = "分销门店", required = true)
    private List<Long> saleShops;

    @ApiModelProperty(value = "直播商品")
    private List<Long> goods;
}
