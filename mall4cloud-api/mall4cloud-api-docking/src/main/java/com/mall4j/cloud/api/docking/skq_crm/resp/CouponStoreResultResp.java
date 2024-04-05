package com.mall4j.cloud.api.docking.skq_crm.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CouponStoreResultResp {
	@ApiModelProperty(value = "门店编码")
	private String store_code;
	
	@ApiModelProperty(value = "门店名称")
	private String store_name;
	
	@ApiModelProperty(value = "门店地址")
	private String store_address;
}
