package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;

/**
 *销售类结果通知,收钱吧请求实体
 */
@Data
public class SQBBodyByResultNotice {
	
	/**
	 * 通知编号
	 */
	private String notification_sn;
	
	/**
	 * 品牌编号，系统对接前由"收钱吧"分配并提供，返回调用方传入的值
	 */
	private String brand_code;
	
	/**
	 * 商户内部使用的门店编号，返回调用方传入的值
	 */
	private String store_sn;
	
	/**
	 * 门店收银机编号，返回调用方传入的值
	 */
	private String workstation_sn;
	/**
	 * 商户订单号，返回调用方传入的值
	 */
	private String check_sn;
	
	/**
	 * 订单序列号
	 */
	private String order_sn;
	
	/**
	 * 订单类型，1 销售； 2 退款
	 */
	private String order_type;
	
	/**
	 * 	POS 或 电商等业务系统内的实际销售订单号，不同于check_sn。如果发起支付请求时该订单号已经生成，强烈建议传入，方便后续对账和运营流程使用。本字段不影响交易本身。
	 */
	private String sales_sn;
	
	/**
	 * 订单来源：1=商户系统，3=智能终端，4=门店码牌，5=商户后台
	 */
	private String order_source;
	
	/**
	 * 产生回调时订单状态可能有：0：已取消，4：操作完成，6：操作失败，7：已终止
	 */
	private String order_status;
	
	/**
	 * 业务场景值：0-无场景，1-智能终端，2-H5，4-PC，5-微信小程序/插件，7-刷脸终端，8-立即付，10-APP支付，11-顾客扫码支付，12-顾客出码
	 */
	private String scene;
	
	/**
	 * 字符串，20- 25位	订单创建时间,格式详见 1.5时间数据元素定义
	 */
	private String sales_time;
	
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
	 * 操作员，可以传入收款的收银员或导购员。例如"张三"
	 */
	private String operator;
	
	/**
	 * 可以传入需要备注顾客的信息
	 */
	private String customer;
	
	/**
	 * 拓展字段1，可以用于做自定义标识，如座号，房间号；智能终端手动录单功能需要添加此字段请联系收钱吧技术支持
	 */
	private String extension_1;
	
	/**
	 * 拓展字段2，可以用于做自定义标识，如座号，房间号；智能终端手动录单功能需要添加此字段请联系收钱吧技术支持
	 */
	private String extension_2;
	
	/**
	 * 行业代码, 0=零售;1:酒店; 2:餐饮; 3:文娱; 4:教育;
	 */
	private String industry_code;
	
	/**
	 * 商户系统的产品名称、系统编号等信息，便于帮助商户调查问题
	 */
	private String pos_info;
	
	/**
	 * 通知接收地址。总共回调7次，回调时间间隔：4m,10m,10m,1h,2h,6h,15h。
	 */
	private String notify_url;
	
	/**
	 * 扩展对象，用于传入本接口所定义字段之外的参数，JSON格式。
	 */
	private JSON extended;
	
	/**
	 * 反射参数; 任何开发者希望原样返回的信息，可以用于关联商户ERP系统的订单或记录附加订单内容。可以在订单结果通知中返回
	 */
	private String reflect;
	
	/**
	 * 订单货物清单
	 */
	private List<ResultNoticeItem> items;
	
	/**
	 * 指定本订单的流水信息
	 */
	private List<ResultNoticeTender> tenders;
}
