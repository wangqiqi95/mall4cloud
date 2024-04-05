package com.mall4j.cloud.api.biz.dto.channels.request;

import com.mall4j.cloud.api.biz.dto.channels.EcSearchCondition;
import lombok.Data;

@Data
public class EcSearchOrderRequest {
    private EcSearchCondition search_condition;
    //不填该参数:全部订单 0:没有正在售后的订单, 1:正在售后单数量大于等于1的订单
    private Integer on_aftersale_order_exist;
    /**
     * 枚举值	描述
     * 10	待付款
     * 20	待发货（包含部分发货）
     * 21	部分发货
     * 30	待收货（包含部分发货）
     * 100	完成
     * 250	订单取消（包括未付款自动取消，售后取消等取消情况）
     */
    private Integer status;
    //分页参数，上一页请求返回
    private String next_key;
    //每页数量(不超过100)
    private Integer page_size;
}
