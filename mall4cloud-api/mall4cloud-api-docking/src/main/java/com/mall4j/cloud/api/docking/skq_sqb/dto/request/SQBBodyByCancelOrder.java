package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBBody;
import lombok.Data;

/**
 * @Description 收钱吧取消订单操作请求入参主体
 * 只有待操作的订单可以取消。
 * @author Peter_Tan
 * @date 2023-05-06 16:30
 **/
@Data
public class SQBBodyByCancelOrder extends SQBBody {

    /**
     * 请求编号，每次请求必须唯一；
     * 表示每一次请求时不同的业务，如果第一次请求业务失败了，
     * 再次请求，可以用于区分是哪次请求的业务。
     */
    @JsonProperty("request_id")
    private String request_id;

    /**
     * 品牌编号，系统对接前由"收钱吧"分配并提供
     */
    @JsonProperty("brand_code")
    private String brand_code;

    /**
     * 商户内部使用的门店编号
     */
    @JsonProperty("original_store_sn")
    private String original_store_sn;

    /**
     * 原始门店收银机编号，如果没有请传入"0"
     */
    @JsonProperty("original_workstation_sn")
    private String original_workstation_sn;

    /**
     * 原始商户订单号
     */
    @JsonProperty("original_check_sn")
    private String original_check_sn;

    /**
     * 本系统为该订单生成的订单序列号
     */
    @JsonProperty("original_order_sn")
    private String original_order_sn;

    /**
     * 反射参数; 任何开发者希望原样返回的信息，
     * 可以用于关联商户ERP系统的订单或记录附加订单内容。
     * 可以在订单结果通知中返回
     */
    private String reflect;

}
