package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcProductDetail;
import lombok.Data;

@Data
public class EcProductResponse extends EcBaseResponse {

    private EcProductDetail product;

    private EcProductDetail edit_product;
}
