package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcProduct;
import lombok.Data;

@Data
public class EcAddProductResponse extends EcBaseResponse{

    private EcProduct data;

}
