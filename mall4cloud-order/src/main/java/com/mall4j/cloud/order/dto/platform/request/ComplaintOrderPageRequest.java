package com.mall4j.cloud.order.dto.platform.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ComplaintOrderPageRequest {
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("退单编号")
    private String refundNumber;

    @ApiModelProperty("结束时间 yyyy-MM-dd HH:mm:ss")
    private String creteTime;
    @ApiModelProperty("结束时间 yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty("纠纷单状态" +
            "230000 商家售后处理中\n" +
            "     * 230001 客服处理中\n" +
            "     * 230002 待用户上传凭证\n" +
            "     * 230003 待商家上传凭证\n" +
            "     * 230004 待双方上传凭证\n" +
            "     * 230005 商家已补充凭证，缺用户上传凭证\n" +
            "     * 230006 用户已补充凭证，缺商家上传凭证\n" +
            "     * 230007 待客服判责\n" +
            "     * 230009 售后处理中\n" +
            "     * 230011 待客服核实退款凭证\n" +
            "     * 230012 投诉完结\n" +
            "     * 230013 投诉关闭\n" +
            "     * 230014 客服退货纠纷处理中\n" +
            "     * 230015 待用户上传退货凭证\n" +
            "     * 230016 待商家上传退货凭证\n" +
            "     * 230017 待双方上传退货凭证\n" +
            "     * 230018 商家已补充凭证，用户上传退货凭证\n" +
            "     * 230019 用户已补充凭证，缺商家上传退货凭证\n" +
            "     * 230020 客服核定退货凭证")
    private Integer status;

    @ApiModelProperty("纠纷单类型" +
            "1   未按约定时效发货\n" +
            "     * 2   商家表示无法发货\n" +
            "     * 3   已发货但物流没有更新\n" +
            "     * 4   已发货但物流运转异常\n" +
            "     * 5   无需该商品需求退款\n" +
            "     * 6   取消订单退款\n" +
            "     * 7   商家拒绝处理售后\n" +
            "     * 8   售后处理方式未达成一致\n" +
            "     * 9   售后费用退还争议\n" +
            "     * 10  商品返回事宜未达成一致")
    private Integer type;

    @ApiModelProperty("页码")
    private Integer offset;
    @ApiModelProperty("每页条数")
    private Integer limit;


}
