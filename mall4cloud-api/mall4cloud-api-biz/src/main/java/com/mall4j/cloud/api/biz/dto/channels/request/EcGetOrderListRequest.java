package com.mall4j.cloud.api.biz.dto.channels.request;

import com.mall4j.cloud.api.biz.dto.channels.EcTimeRange;
import lombok.Data;

@Data
public class EcGetOrderListRequest {
    //订单创建时间范围
    private EcTimeRange create_time_range;
    //订单更新时间范围
    private EcTimeRange update_time_range;
    /**
     * 枚举值	描述
     * 10	待付款
     * 20	待发货（包括部分发货）
     * 21	部分发货
     * 30	待收货（包括部分发货）
     * 100	完成
     * 250	订单取消（包括未付款取消，售后取消等）
     */
    private Integer status;
    //买家身份标识
    private String openid;
    //分页参数，上一页请求返回
    private String next_key;
    //每页数量(不超过100)
    private Integer page_size;
}
