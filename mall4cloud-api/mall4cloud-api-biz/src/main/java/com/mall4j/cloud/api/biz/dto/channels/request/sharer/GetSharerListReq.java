package com.mall4j.cloud.api.biz.dto.channels.request.sharer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 获取绑定的分享员列表
 * @Author axin
 * @Date 2023-02-13 12:04
 **/
@Data
public class GetSharerListReq {
    /**
     * 分页参数，页数
     */
    private Integer page;

    /**
     * 分页参数，每页分享员数（不超过100）
     */
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
     * 分享员类型
     * 0	普通分享员
     * 1	企业分享员
     */
    @JsonProperty("sharer_type")
    private Integer sharerType;
}
