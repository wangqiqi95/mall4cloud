package com.mall4j.cloud.api.biz.dto.channels.response.sharer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * @Description 获取绑定的分享员列表
 * @Author axin
 * @Date 2023-02-14 12:03
 **/
@Data
public class GetSharerListResp extends BaseResponse {
    /**
     * 分享员信息
     */
    @JsonProperty("sharer_info_list")
    private List<SharerInfo> sharerInfoList;

    @Data
    private static class SharerInfo {
        /**
         * 分享员openid
         */
        private String openid;

        /**
         * 分享员昵称
         */
        private String nickname;

        /**
         * 绑定时间
         */
        @JsonProperty("bind_time")
        private Long bindTime;

        /**
         * 分享员类型
         */
        @JsonProperty("sharer_type")
        private String sharerType;
    }
}
