package com.mall4j.cloud.api.biz.dto.channels.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EcuploadrefundcertificateRequest {

    @ApiModelProperty("外部 售后单号")
    private Long after_sale_order_id;
    @ApiModelProperty("退款凭证 media_id")
    private List<String> refund_certificates;
    @ApiModelProperty("描述")
    private String desc;

}
