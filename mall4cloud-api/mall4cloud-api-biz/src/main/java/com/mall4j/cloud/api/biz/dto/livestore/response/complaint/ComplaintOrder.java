package com.mall4j.cloud.api.biz.dto.livestore.response.complaint;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class ComplaintOrder {

    @ApiModelProperty("纠纷单ID")
    @JsonProperty("complaint_order_id")
    private Long complaint_order_id;

    @ApiModelProperty("售后单ID")
    @JsonProperty("after_sale_order_id")
    private Long after_sale_order_id;

    @ApiModelProperty("订单ID")
    @JsonProperty("order_id")
    private String order_id;

    @ApiModelProperty("投诉类型" +
            " 1   未按约定时效发货\n" +
            " 2   商家表示无法发货\n" +
            " 3   已发货但物流没有更新\n" +
            " 4   已发货但物流运转异常\n" +
            " 5   无需该商品需求退款\n" +
            " 6   取消订单退款\n" +
            " 7   商家拒绝处理售后\n" +
            " 8   售后处理方式未达成一致\n" +
            " 9   售后费用退还争议\n" +
            " 10  商品返回事宜未达成一致")
    @JsonProperty("type")
    private Integer type;

    @ApiModelProperty("当前纠纷状态" +
            "* 230000 商家售后处理中\n" +
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
    @JsonProperty("state")
    private Integer state;

    @ApiModelProperty("当前状态超时unix时间戳")
    @JsonProperty("expired_time")
    private Integer expired_time;

    @ApiModelProperty("用户openid")
    @JsonProperty("openid")
    private String openid;

    @ApiModelProperty("判责结果" +
            "0\t尚未判责\n" +
            "1\t平台判定商家责任，需退款\n" +
            "2\t平台判定用户责任，需退款\n" +
            "3\t平台判定双方责任，需退款\n" +
            "4\t平台判定双方无责，需退款\n" +
            "5\t平台判定商家责任，需退货退款\n" +
            "6\t平台判定用户责任，需退货退款\n" +
            "7\t平台判定双方责任，需退货退款\n" +
            "8\t平台判定双方无责，需退货退款\n" +
            "9\t平台判定商家责任，无需退款/退货退款\n" +
            "10\t平台判定用户责任，无需退款/退货退款\n" +
            "11\t平台判定双方责任，无需退款/退货退款\n" +
            "12\t平台判定双方无责，无需退款/退货退款\n")
    @JsonProperty("judge_result")
    private Integer judge_result;

    @ApiModelProperty("申请时间")
    @JsonProperty("create_time")
    private Integer create_time;

    @ApiModelProperty("更新时间")
    @JsonProperty("update_time")
    private Integer update_time;

    @JsonProperty("phone_number")
    @ApiModelProperty("用户手机号码")
    private String phone_number;

}
