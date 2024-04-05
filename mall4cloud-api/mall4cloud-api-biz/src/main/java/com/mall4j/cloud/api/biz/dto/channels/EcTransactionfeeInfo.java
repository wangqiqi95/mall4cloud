package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcTransactionfeeInfo {
    //类目实收的交易佣金比例，单位万分比
    private String basis_point;
    //类目原始佣金比例，单位万分比
    private Long original_basis_point;
    //佣金激励类型，0：无激励措施，1：新店佣金减免
    private Integer incentive_type;
}
