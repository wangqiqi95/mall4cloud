package com.mall4j.cloud.api.biz.dto.livestore.request.complaint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ComplaintListRequest {

    /**
     * 纠纷单状态，见ComplaintOrderState
     * 230000 商家售后处理中
     * 230001 客服处理中
     * 230002 待用户上传凭证
     * 230003 待商家上传凭证
     * 230004 待双方上传凭证
     * 230005 商家已补充凭证，缺用户上传凭证
     * 230006 用户已补充凭证，缺商家上传凭证
     * 230007 待客服判责
     * 230009 售后处理中
     * 230011 待客服核实退款凭证
     * 230012 投诉完结
     * 230013 投诉关闭
     * 230014 客服退货纠纷处理中
     * 230015 待用户上传退货凭证
     * 230016 待商家上传退货凭证
     * 230017 待双方上传退货凭证
     * 230018 商家已补充凭证，用户上传退货凭证
     * 230019 用户已补充凭证，缺商家上传退货凭证
     * 230020 客服核定退货凭证
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * 纠纷单类型，见ComplaintOrderType
     *
     * 1   未按约定时效发货
     * 2   商家表示无法发货
     * 3   已发货但物流没有更新
     * 4   已发货但物流运转异常
     * 5   无需该商品需求退款
     * 6   取消订单退款
     * 7   商家拒绝处理售后
     * 8   售后处理方式未达成一致
     * 9   售后费用退还争议
     * 10  商品返回事宜未达成一致
     */
    @JsonProperty("type")
    private Integer type;

    //申请时间（单位秒）起始
    @JsonProperty("begin_create_time")
    private Long begin_create_time;

    //申请时间（单位秒）结束
    @JsonProperty("end_create_time")
    private Long end_create_time;

    //本次拉取的起始位置（从0开始）
    @JsonProperty("offset")
    private Integer offset;

    //本次拉取的大小（最大50）
    @JsonProperty("limit")
    private Integer limit;

    //关联售后单号
    @JsonProperty("after_sale_order_id")
    private Long after_sale_order_id;

    //关联订单号
    @JsonProperty("order_id")
    private Long order_id;


}
