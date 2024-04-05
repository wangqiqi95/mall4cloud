package com.mall4j.cloud.api.payment.vo;

import lombok.Data;

@Data
public class PayInfoFeignVO {

    private Long payId;
    private String orderId;
    private String payBizNo;
    private String payType;

}
