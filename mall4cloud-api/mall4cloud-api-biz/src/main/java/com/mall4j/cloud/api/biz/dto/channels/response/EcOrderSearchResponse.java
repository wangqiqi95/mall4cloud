package com.mall4j.cloud.api.biz.dto.channels.response;

import lombok.Data;

import java.util.List;

@Data
public class EcOrderSearchResponse extends EcBaseResponse{
    //是否还有下一页, true:有下一页；false:已经结束，没有下一页。
    private Boolean has_more;
    //分页参数，下一个页请求回传
    private String next_key;
    //订单号列表
    private List<String> orders;
}
