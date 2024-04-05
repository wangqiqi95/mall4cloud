package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBBody;
import lombok.Data;

import java.util.List;

/**
 * @author ty
 * @ClassName SQBBodyByRefund
 * @description
 * @date 2023/5/6 10:28
 */
@Data
public class SQBBodyByRefund extends SQBBody {

    /**
     * 求编号，每次请求必须唯一；表示每一次请求时不同的业务，如果第一次请求业务失败了，再次请求，可以用于区分是哪次请求的业务。
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
    @JsonProperty("store_sn")
    private String store_sn;

    /**
     * 商户门店名称
     */
    @JsonProperty("store_name")
    private String store_name;

    /**
     * 门店收银机编号，如果没有请传入"0"
     */
    @JsonProperty("workstation_sn")
    private String workstation_sn;

    /**
     * 商户订单号，在商户系统中唯一
     */
    @JsonProperty("check_sn")
    private String check_sn;

    /**
     * POS 或 电商等业务系统内的实际销售订单号，不同于check_sn。如果发起支付请求时该订单号已经生成，强烈建议传入，方便后续对账和运营流程使用。本字段不影响交易本身。
     */
    @JsonProperty("sales_sn")
    private String sales_sn;

    /**
     * 商户订单创建时间, 格式详见 1.5时间数据元素定义
     */
    @JsonProperty("sales_time")
    private String sales_time;

    /**
     * 订单总金额，精确到分，金额应为负数
     */
    private String amount;

    /**
     * 币种，ISO numeric currency code 如："156"for CNY
     */
    private String currency;

    /**
     * 订单简短描述，建议传8个字内
     */
    private String subject;

    /**
     * 订单描述
     */
    private String description;

    /**
     * 操作员，可以传入收款的收银员或导购员。例如"张三"
     */
    private String operator;

    /**
     * 可以传入需要备注顾客的信息
     */
    private String customer;

    /**
     * 智能终端手动录单功能需要添加此字段请联系收钱吧技术支持
     */
    @JsonProperty("extension_1")
    private String extension_1;

    /**
     * 智能终端手动录单功能需要添加此字段请联系收钱吧技术支持
     */
    @JsonProperty("extension_2")
    private String extension_2;

    /**
     * 行业代码, 0=零售1:酒店; 2:餐饮; 3:文娱; 4:教育;
     */
    @JsonProperty("industry_code")
    private String industry_code;

    /**
     * 传入商户系统的产品名称、系统编号等信息，便于帮助商户调查问题
     */
    @JsonProperty("pos_info")
    private String pos_info;

    /**
     * 通知接收地址。总共回调7次，回调时间间隔：4m,10m,10m,1h,2h,6h,15h。
     */
    @JsonProperty("notify_url")
    private String notify_url;

    /**
     * 扩展对象，用于传入本接口所定义字段之外的参数，JSON格式。
     */
    private String extended;

    /**
     * 反射参数; 任何开发者希望原样返回的信息，可以用于关联商户ERP系统的订单或记录附加订单内容。
     */
    private String reflect;

    /**
     * item数组	订单货物清单
     */
    private List<RefundItem> items;

    /**
     *指定本订单的流水信息
     */
    private List<RefundTender> tenders;
}
