package com.mall4j.cloud.api.biz.dto.channels.request.league.promoter;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 达人列表入参
 * @Author axin
 * @Date 2023-02-13 16:07
 **/
@Data
public class LeaguePromoterListGetReq {

    /**
     * 达人状态
     * 0	初始值
     * 1	邀请中
     * 2	达人已接受邀请
     * 3	达人已拒绝邀请
     * 4	已取消邀请
     * 5	已取消合作
     * 10	已删除
     */
    private Integer status;

    /**
     *
     */
    @JsonProperty("page_index")
    private Integer pageIndex;

    /**
     *
     */
    @JsonProperty("page_size")
    private Integer pageSize;
}
