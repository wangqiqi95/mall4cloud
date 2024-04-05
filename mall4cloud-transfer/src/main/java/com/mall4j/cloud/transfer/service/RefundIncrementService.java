package com.mall4j.cloud.transfer.service;

import org.springframework.stereotype.Service;

/**
 * 退单增量处理
 *
 * @luzhengxiang
 * @create 2022-05-07 10:06 AM
 **/
@Service
public class RefundIncrementService {


    /**
     * 暂时没有想到什么办法全量更新，这里会做拆单。
     * 如果增量同步来的退款单状态有更新，我找不到对应的关联字段来更新我们的退款单状态。
     *
     *
     * 这里如果要做全量更新，又涉及到关联的订单表会因为增量的原因订单数据不全。
     * 比如这时，订单就可能同时存在 mall_sales_order 和增量的 mall_sales_order_increment表中 ？
     *
     *
     *
     *
     */
    public void transfer(){

    }
}
