

package com.mall4j.cloud.biz.model.live;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
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
@ApiModel("直播间")
public class LiveRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id-修改时必传")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "微信直播间id-修改时必传")
    private Integer roomId;

    @ApiModelProperty(value = "直播间名称", required = true)
    private String name;

    @ApiModelProperty(value = "主播昵称", required = true)
    private String anchorName;

    @ApiModelProperty(value = "背景图", required = true)
    private String coverImg;

    @ApiModelProperty(value = "主播分享图", required = true)
    private String shareImg;

    @ApiModelProperty(value = "购物直播频道封面图", required = true)
    private String feedsImg;

    @ApiModelProperty(value = "直播间类型 1: 推流，0：手机直播")
    private Integer type;

    @ApiModelProperty(value = "0.竖屏 1.横屏", required = true)
    private Integer screenType;

    @ApiModelProperty(value = "是否置顶 1.是 2.不是")
    private Integer roomTop;

    @ApiModelProperty(value = "直播间状态。101：直播中，102：未开始，103已结束，104禁播，105：暂停，106：异常，107：已过期 -1：已删除")
    private Integer liveStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "直播开始时间", required = true)
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "直播结束时间", required = true)
    private Date endTime;
    /**
     * 主播微信号
     */
    @ApiModelProperty(value = "主播微信号", required = true)
    private String anchorWechat;
    /**
     * 购物直播频道封面图微信mediaId
     */
    @ApiModelProperty(value = "购物直播频道封面图微信mediaId")
    private String feedsImgId;
    /**
     * 背景图微信mediaId
     */
    @ApiModelProperty(value = "背景图微信mediaId")
    private String coverImgId;
    /**
     * 分享码微信mediaId
     */
    @ApiModelProperty(value = "分享码微信mediaId")
    private String shareImgId;
    /**
     * 是否开启官方收录
     */
    @ApiModelProperty(value = "是否开启官方收录", required = true)
    private Integer isFeedsPublic;
    /**
     * 直播间功能
     */
    @ApiModelProperty(value = "直播间功能")
    private String roomTools;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private Date applyTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "直播间功能")
    @TableField(exist = false)
    private LiveRoom.RoomToolsVO roomToolsVo;

    @ApiModelProperty(value = "商品信息", required = true)
    @TableField(exist = false)
    private List<LiveProdStore> liveProdStores;

    @ApiModelProperty(value = "是否关联所有开播门店：0-否，1-是", required = true)
    private Integer isAllShop;

    @ApiModelProperty(value = "开播门店id数组-当isAllShop=1时 可不传")
    @TableField(exist = false)
    private List<Long> shopIds;

    @ApiModelProperty(value = "是否关联所有分销门店：0-否，1-是", required = true)
    private Integer isAllSaleShop;

    @ApiModelProperty(value = "分销门店id数组-isAllSaleShop=1时 可不传")
    @TableField(exist = false)
    private List<Long> saleShopIds;


    @Data
    public static class RoomToolsVO {

        @ApiModelProperty(value = "是否关闭客服 0 开启，1 关闭", required = true)
        private Integer closeKf;

        @ApiModelProperty(value = "是否关闭点赞  0 开启，1 关闭", required = true)
        private Integer closeLike;

        @ApiModelProperty(value = " 是否关闭货架  0 开启，1 关闭", required = true)
        private Integer closeGoods;

        @ApiModelProperty(value = "是否关闭评论  0 开启，1 关闭", required = true)
        private Integer closeComment;

        @ApiModelProperty(value = " 是否关闭回放  0 开启，1 关闭", required = true)
        private Integer closeReplay;

        @ApiModelProperty(value = "是否关闭分享  0 开启，1 关闭", required = true)
        private Integer closeShare;
    }
}
