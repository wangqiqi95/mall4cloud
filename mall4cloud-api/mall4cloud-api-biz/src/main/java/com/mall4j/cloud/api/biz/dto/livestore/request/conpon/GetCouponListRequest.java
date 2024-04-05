package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class GetCouponListRequest {

    private Integer page_size;
    private Integer offset;

}
