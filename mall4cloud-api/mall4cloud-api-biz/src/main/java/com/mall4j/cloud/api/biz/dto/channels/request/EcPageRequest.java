package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcPageRequest {
    //每页数量(默认10, 不超过50)
    private Integer page_size;
    //由上次请求返回, 记录翻页的上下文, 传入时会从上次返回的结果往后翻一页, 不传默认拉取第一页数据
    private String next_key;
}
