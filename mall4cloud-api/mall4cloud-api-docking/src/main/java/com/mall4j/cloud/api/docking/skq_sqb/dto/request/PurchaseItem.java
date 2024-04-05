package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收钱吧购买操作,订单货物清单实体
 */
@Data
public class PurchaseItem {
	
	@ApiModelProperty("必传,商户系统中的商品编号")
	private String item_code;
	
	@ApiModelProperty("必传,商品描述信息，例'白色短袖'")
	private String item_desc;
	
	@ApiModelProperty("商品所属大类，例'短袖'")
	private String category;
	
	@ApiModelProperty("商品单位，例'件'")
	private String unit;
	
	@ApiModelProperty("必传,商品数量，例'2'；当退货时数量为负数，例：'-2'")
	private String item_qty;
	
	@ApiModelProperty("必传,商品单价，精确到分")
	private String item_price;
	
	@ApiModelProperty("必传,商品成交价格,一般为数量*单价，如有折扣再进行扣减，精确到分；当退货时成交价为负数；目前不校验'数量*单价'结果是否与此字段值相等")
	private String sales_price;
	
	@ApiModelProperty("必传,0-销售，1-退货")
	private String type;
	
	@ApiModelProperty("原商品销售门店号，退货时必填")
	private String return_store_sn;
	
	@ApiModelProperty("原商品销售收音机号，退货时必填")
	private String return_workstation_sn;
	
	@ApiModelProperty("原商品销售订单号，退货时必填")
	private String return_check_sn;
}
