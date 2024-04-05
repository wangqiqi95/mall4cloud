package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcGetaftersaleListRequest {
    //订单创建启始时间
    private Long begin_create_time;
    //订单创建结束时间，end_create_time减去begin_create_time不得大于24小时
    private Long end_create_time;
    //翻页参数，从第二页开始传，来源于上一页的返回值
    private String next_key;
}
