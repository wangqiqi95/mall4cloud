package com.mall4j.cloud.api.biz.dto.channels.response.sharer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * @Description 分享员订单列表
 * @Author axin
 * @Date 2023-02-13 14:09
 **/
@Data
public class GetSharerOrderListResp extends BaseResponse {
    /**
     * 分享员订单
     */
    @JsonProperty("order_list")
    private List<SharerOrder> orderList;

    @Data
    private static class SharerOrder {
        /**
         * 订单号
         */
        @JsonProperty("order_id")
        private String orderId;

        /**
         * 分享场景
         * 1	直播间
         * 2	橱窗
         * 3	短视频
         * 4	视频号主页
         * 5	商品详情页
         */
        @JsonProperty("sharer_scene")
        private Integer sharerScene;
        /**
         * 分享员openid
         */
        @JsonProperty("sharer_openid")
        private String sharerOpenid;
        /**
         * 分享员类型
         * 0	普通分享员
         * 1	企业分享员
         */
        @JsonProperty("sharer_type")
        private Integer sharerType;

        /**
         * 视频号场景信息
         */
        @JsonProperty("finder_scene_info")
        private FinderScene finderSceneInfo;
    }

    @Data
    private static class FinderScene {
        /**
         * 视频号唯一标识
         */
        @JsonProperty("promoter_id")
        private String promoterId;

        /**
         * 视频号昵称
         */
        @JsonProperty("finder_nickname")
        private String finderNickname;

        /**
         * 直播间唯一标识
         */
        @JsonProperty("live_export_id")
        private String liveExportId;

        /**
         * 短视频唯一标识
         */
        @JsonProperty("video_export_id")
        private String videoExportId;

        /**
         * 短视频标题
         */
        @JsonProperty("video_title")
        private String videoTitle;
    }
}
