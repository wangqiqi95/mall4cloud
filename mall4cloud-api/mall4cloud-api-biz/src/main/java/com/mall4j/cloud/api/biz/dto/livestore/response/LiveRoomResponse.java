

package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;


@Data
public class LiveRoomResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("shop_id")
    private Long shopId;

    @JsonProperty("room_id")
    private Integer roomId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("anchor_name")
    private String anchorName;

    @JsonProperty("cover_img")
    private String coverImg;

    @JsonProperty("share_img")
    private String shareImg;

    @JsonProperty("feeds_img")
    private String feedsImg;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("screen_type")
    private Integer screenType;

    @JsonProperty("room_top")
    private Integer roomTop;

    @JsonProperty("live_status")
    private Integer liveStatus;

    @JsonProperty("start_time")
    private Date startTime;

    @JsonProperty("end_time")
    private Date endTime;

    @JsonProperty("anchor_wechat")
    private String anchorWechat;

    @JsonProperty("feeds_img_id")
    private String feedsImgId;
    /**
     * 背景图微信mediaId
     */
    @JsonProperty("cover_img_id")
    private String coverImgId;
    /**
     * 分享码微信mediaId
     */
    @JsonProperty("share_img_id")
    private String shareImgId;
    /**
     * 是否开启官方收录
     */
    @JsonProperty("is_feeds_public")
    private Integer isFeedsPublic;

    /**
     * 申请时间
     */
    @JsonProperty("apply_time")
    private Date applyTime;
    /**
     * 更新时间
     */
    @JsonProperty("update_time")
    private Date updateTime;

}
