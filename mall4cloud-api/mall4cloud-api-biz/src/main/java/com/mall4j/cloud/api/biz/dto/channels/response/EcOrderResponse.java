package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcOrder;
import lombok.Data;

@Data
public class EcOrderResponse extends EcBaseResponse {
    //订单结构
    private EcOrder order;
}
