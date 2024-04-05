package com.mall4j.cloud.api.biz.dto.channels.response;

import lombok.Data;

import java.util.List;

@Data
public class EcGetaftersaleListResponse extends EcBaseResponse {
    //售后单号列表
    private List<String> after_sale_order_id_list;
    //是否还有数据
    private Boolean has_more;
    //翻页参数
    private String next_key;
}
