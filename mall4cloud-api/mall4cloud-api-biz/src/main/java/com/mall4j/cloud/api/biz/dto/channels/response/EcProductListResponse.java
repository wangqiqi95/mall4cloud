package com.mall4j.cloud.api.biz.dto.channels.response;

import lombok.Data;

import java.util.List;

@Data
public class EcProductListResponse extends EcBaseResponse {

    //商品 id 列表
    private List<String> product_ids;
    //本次翻页的上下文，用于请求下一页
    private String next_key;
    //商品总数
    private Integer total_num;
}
