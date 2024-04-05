package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

import java.util.List;

/**
 * 注册返回值
 * @author LGH
 */
@Data
public class RoomDetailResponse {
    /**
     * 直播间id
     */
    private Long roomId;
    /**
     * 直播间名称
     */
    private String name;
    /**
     * 主播昵称
     */
    private String anchorName;
    /**
     * 背景图
     */
    private String coverImg;
    /**
     * 主播分享图
     */
    private String shareImg;
    /**
     * 购物直播频道封面图
     */
    private String feedsImg;
    /**
     * 是否开启官方收录
     */
    private Integer isFeedsPublic;

    /**
     * 直播间类型 1.推流 2.手机直播
     */
    private Integer type;

    /**
     * 1.竖屏 2.横屏
     */
    private Integer screenType;
    /**
     * 直播间功能
     */
    private String roomTools;
    /**
     * 购物直播频道封面图微信mediaId
     */
    private String feedsImgId;
    /**
     * 购物直播频道封面图微信mediaId
     */
    private String coverImgId;
    /**
     * 购物直播频道封面图微信mediaId
     */
    private String shareImgId;
    /**
     * 直播开始时间
     */
    private Long wxStartTime;
    /**
     * 直播结束时间
     */
    private Long wxEndTime;
    /**
     * 直播间状态
     */
    private Integer liveStatus;
    /**
     * 直播间商品
     */
    private List<GoodsInfoRespParam> goods;
    /**
     * 直播间商品
     */
    private Integer total;

}
