package com.mall4j.cloud.api.biz.dto.channels.response;

import lombok.Data;

import java.util.List;

@Data
public class EcOrderListResponse extends EcBaseResponse {
    //订单号列表
    private List<String> order_id_list;
    //分页参数，下一页请求回传
    private String next_key;
    //是否还有下一页，true:有下一页；false:已经结束，没有下一页。
    private Boolean has_more;
}
