package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcAftersaleDetail {
    //正在售后流程的售后单数
    private Integer on_aftersale_order_cnt;
    //售后单列表
    private List<EcAfterSaleOrderInfo> aftersale_order_list;

}
