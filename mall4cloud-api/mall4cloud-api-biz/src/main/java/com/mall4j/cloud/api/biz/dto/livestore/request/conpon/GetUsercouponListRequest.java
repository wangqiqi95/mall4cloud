package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class GetUsercouponListRequest {
    private Integer page_size;
    private Integer offset;
    private String openid;

}
