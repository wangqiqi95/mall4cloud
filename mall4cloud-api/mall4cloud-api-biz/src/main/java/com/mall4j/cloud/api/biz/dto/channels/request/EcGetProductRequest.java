package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcGetProductRequest {
    //商品ID
    private String product_id;
    //默认取1。1:获取线上数据, 2:获取草稿数据, 3:同时获取线上和草稿数据（注意：需成功上架后才有线上数据）
    private Integer data_type;
}
