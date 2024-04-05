package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

/**
 * 视频号4.0 橱窗已上架商品集合
 * @date 2023/3/8
 */
@Data
public class EcWindowProductListRequest {

    /**
     * <p>用于指定查询某个店铺来源的商品
     * <p>非必选
     */
    private String appid;

    /**
     * <p>用于指定查询属于某个分店 ID 下的商品
     * <p>非必选
     */
    private Long branch_id;

    /**
     * <p>单页商品数（不超过200）
     * <p>必选
     */
    private Integer page_size;

    /**
     * <p>页面下标，下标从1开始，默认为1
     * <p>非必选
     */
    private Integer page_index;

    /**
     * <p>由上次请求返回，顺序翻页时需要传入, 会从上次返回的结果往后翻一页（填了该值后page_index不生效）
     * <p>非必选
     */
    private String last_buffer;

    /**
     * <p>是否需要返回满足筛选条件的商品总数
     * <p>非必选
     */
    private Integer need_total_num;
}
