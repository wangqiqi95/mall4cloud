package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBBody;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 购买实体
 */
@Data
public class SQBBodyByPurchase extends SQBBody {
	
	@ApiModelProperty(value = "必传,请求编号，每次请求必须唯一；表示每一次请求时不同的业务，如果第一次请求业务失败了，再次请求，可以用于区分是哪次请求的业务。")
	private String request_id;
	
	@ApiModelProperty(value = "必传,品牌编号，系统对接前由收钱吧分配并提供")
	private String brand_code;
	
	@ApiModelProperty(value = "必传,商户内部使用的门店编号")
	private String store_sn;
	
	@ApiModelProperty(value = "商户门店名称")
	private String store_name;
	
	@ApiModelProperty(value = "必传,门店收银机编号，如果没有请传入0")
	private String workstation_sn;
	
	@ApiModelProperty(value = "必传,商户订单号，在商户系统中唯一")
	private String check_sn;
	
	@ApiModelProperty(value = "POS 或 电商等业务系统内的实际销售订单号，不同于check_sn。如果发起支付请求时该订单号已经生成，强烈建议传入，方便后续对账和运营流程使用。本字段不影响交易本身。")
	private String sales_sn;
	
	@ApiModelProperty(value = "必传,业务场景值：0-无场景，1-智能终端，2-H5，4-PC，5-微信小程序/插件，7-刷脸终端，8-立即付，10-APP，11-顾客扫码，12-顾客出码")
	private String scene;
	
	@ApiModelProperty(value = "必传,商户订单创建时间")
	private String sales_time;
	
	@ApiModelProperty(value = "订单有效时间，传整数。单位为分钟，最小值为1，最大值为43200。默认24小时（即1440）。expire_time和expired_at二选一，如果都传，优先使用expired_at")
	private String expire_time;
	
	@ApiModelProperty(value = "订单绝对超时时间。不能大于30天。2015-12-05T15:28:36+0800。expire_time和expired_at二选一，如果都传，优先使用expired_at")
	private String expired_at;
	
	@ApiModelProperty(value = "必传,订单总金额，精确到分。如果同时传入【订单总金额】，【商品总金额】，【运费】，必须满足【订单总金额】=【商品总金额】+【运费】）")
	private String amount;
	
	@ApiModelProperty(value = "商品总金额，精确到分")
	private String item_amount;
	
	@ApiModelProperty(value = "必传,运费，精确到分")
	private String freight;
	
	@ApiModelProperty(value = "必传,币种，ISO numeric currency code 如：'156'for CNY")
	private String currency;
	
	@ApiModelProperty(value = "必传,订单简短描述，建议传8个字内，手机账单支付凭证页“商品说明”会展示")
	private String subject;
	
	@ApiModelProperty(value = "订单描述")
	private String description;
	
	@ApiModelProperty(value = "必传,操作员，可以传入收款的收银员或导购员。例如'张三'")
	private String operator;
	
	@ApiModelProperty(value = "必传,可以传入需要备注顾客的信息")
	private String customer;
	
	@ApiModelProperty(value = "拓展字段1，可以用于做自定义标识，如座号，房间号；")
	private String extension_1;
	
	@ApiModelProperty(value = "拓展字段2，可以用于做自定义标识，如座号，房间号；")
	private String extension_2;
	
	@ApiModelProperty(value = "必传,行业代码, 0=零售;1:酒店; 2:餐饮; 3:文娱; 4:教育;")
	private String industry_code;
	
	@ApiModelProperty(value = "必传,传入商户系统的产品名称、系统编号等信息，便于帮助商户调查问题")
	private String pos_info;
	
	@ApiModelProperty(value = "通知接收地址。总共回调7次，回调时间间隔：4m,10m,10m,1h,2h,6h,15h")
	private String notify_url;
	
	@ApiModelProperty(value = "支付结果页，H5场景、PC场景和APP场景选填，其余场景不适用")
	private String return_url;
	
	@ApiModelProperty(value = "扩展对象，用于传入本接口所定义字段之外的参数，JSON格式。")
	private String extended;
	
	@ApiModelProperty(value = "反射参数; 任何开发者希望原样返回的信息，可以用于关联商户ERP系统的订单或记录附加订单内容。")
	private String reflect;
	
	@ApiModelProperty(value = "可用的二级支付方式sub_tender_type,多个sub_tender_type用“|”（竖线符号）隔开。")
	private String enable_sub_tender_types;
	
	@ApiModelProperty(value = "不可用的二级支付方式sub_tender_type,多个sub_tender_type用“|”（竖线符号）隔开。与enable_sub_tender_types互斥")
	private String disable_sub_tender_types;
	
	@ApiModelProperty(value = "指定支付方式使用规则，收银台界面展示生效。字段定义见下表specified_payment")
	private PurchaseSpecifiedPayment specified_payment;
	
	@ApiModelProperty(value = "付款码，scene=12时必填")
	private String dynamic_id;
	
	@ApiModelProperty(value = "会员账户集成参数")
	private PurchaseCrmAccountOption crm_account_option;
	
	@ApiModelProperty(value = "订单货物清单")
	private List<PurchaseItem> items;
	
	@ApiModelProperty(value = "指定本订单的流水信息,不指定支付方式时可不传该字段，由用户在相应场景的收银台界面内选择支付方式；指定支付方式时下发各支付方式设置；预授权完成必须下发该项目并指定授权时的流水号")
	private List<PurchaseTender> tenders;
	
	@ApiModelProperty(value = "特性标识：1-微信trail, 2-支付宝trail,传入1过滤支付宝花呗分期支付方式，传入2过滤微信方式。")
	private String trail_flag;

}
