package com.mall4j.cloud.api.docking.skq_sqb.dto.response;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 收钱吧购买出参
 * @author Peter_Tan
 * @date 2023-05-08 14:30
 **/
@Data
public class SQBPurchaseResp {

    /**
     * 品牌编号，系统对接前由"收钱吧"分配并提供，返回调用方传入的值
     */
    @JsonProperty("brand_code")
    private String brandCode;

    /**
     * 商户内部使用的门店编号，返回调用方传入的值
     */
    @JsonProperty("store_sn")
    private String storeSn;

    /**
     * 门店收银机编号，返回调用方传入的值
     */
    @JsonProperty("workstation_sn")
    private String workstationSn;

    /**
     * 商户订单号，返回调用方传入的值
     */
    @JsonProperty("check_sn")
    private String checkSn;

    /**
     * 本系统为该订单生成的订单序列号
     */
    @JsonProperty("order_sn")
    private String orderSn;

    /**
     * POS 或 电商等业务系统内的实际销售订单号，不同于check_sn。
     * 如果发起支付请求时该订单号已经生成，强烈建议传入，
     * 方便后续对账和运营流程使用。本字段不影响交易本身。
     */
    @JsonProperty("sales_sn")
    private String salesSn;

    /**
     * 订单凭证，H5、微信小程序、微信小程序插件,刷脸终端的场景
     */
    @JsonProperty("order_token")
    private String orderToken;

    /**
     * 订单来源：1=商户系统，3=智能终端，4=门店码牌，5=商户后台
     */
    @JsonProperty("order_source")
    private String orderSource;

    /**
     * 终端的场景轻POS收银台地址，H5场景下会返回收银台链接；
     * APP场景下返回跳转小程序链接
     */
    @JsonProperty("cashier_url")
    private String cashierUrl;

    /**
     * 轻 POS 订单图片地址，H5 场景下会返回该字段
     */
    @JsonProperty("order_image_url")
    private String orderImageUrl;

    /**
     * 反射参数; 任何开发者希望原样返回的信息，
     * 可以用于关联商户ERP系统的订单或记录附加订单内容。
     * 可以在订单结果通知中返回
     */
    private String reflect;

    /**
     * 以下是“12-顾客出码”场景下的特有返回参数
     */

    /**
     * 订单状态0：已取消，1：待操作，2：操作中，3：等待结果中，
     * 4：操作完成，5：部分完成，6：操作失败，7：已终止
     */
    @JsonProperty("order_status")
    private String orderStatus;

    /**
     * 订单创建时间
     * 采用ISO 8601 Format. YYYY-MM-DDThh:mm:ssTZD
     * Example:
     * 2015-12-05T15:28:36+08:00
     */
    @JsonProperty("sales_time")
    private String salesTime;

    /**
     * 订单总金额，精确到分。
     */
    private String amount;

    /**
     * 币种，ISO numeric currency code 如："156"for CNY
     */
    private String currency;

    /**
     * 订单简短描述
     */
    private String subject;

    /**
     * 订单描述
     */
    private String description;

    /**
     * 操作员，可以传入收款的收银员或导购员。例如“张三”
     */
    private String operator;

    /**
     * 可以传入需要备注顾客的信息
     */
    private String customer;

    /**
     * 拓展字段1，可以用于做自定义标识，如座号，房间号
     */
    @JsonProperty("extension_1")
    private String extension1;

    /**
     * 拓展字段2，可以用于做自定义标识，如座号，房间号
     */
    @JsonProperty("extension_2")
    private String extension2;

    /**
     * 行业代码, 0=零售;1:酒店; 2:餐饮; 3:文娱; 4:教育;
     */
    @JsonProperty("industry_code")
    private String industryCode	;

    /**
     * 传入商户系统的产品名称、系统编号等信息，便于帮助商户调查问题
     */
    @JsonProperty("pos_info")
    private String posInfo;

    /**
     * 通知接收地址。总共回调7次，回调时间间隔：4m,10m,10m,1h,2h,6h,15h
     */
    @JsonProperty("notify_url")
    private String notifyUrl;

    /**
     * 扩展对象，用于传入本接口所定义字段之外的参数，JSON格式
     */
    private String extended;

    /**
     * 订单货物清单，定义如下表
     */
    private List<PurchaseRespItem> items;

    /**
     * 指定本订单的流水信息，定义如下表
     */
    private List<PurchaseRespTender> tenders;

}
