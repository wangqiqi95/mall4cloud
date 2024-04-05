package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcAfterSaleOrder;
import lombok.Data;

@Data
public class EcGetaftersaleorderResponse extends EcBaseResponse {

    private EcAfterSaleOrder after_sale_order;
}
