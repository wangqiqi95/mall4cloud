package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.AddressInfo;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0运费计算方法实体
 */
@Data
public class EcFreightCalcMethod {
	
	//支持的地址列表
	@JsonProperty("address_infos")
	private List<AddressInfo> addressInfos;
	
	//是否默认运费
	@JsonProperty("is_default")
	private Boolean isDefault;
	
	//快递公司
	@JsonProperty("delivery_id")
	private String deliveryId;
	
	//首段运费需要满足的数量
	@JsonProperty("first_val_amount")
	private Integer firstValAmount;
	
	//首段运费的金额
	@JsonProperty("first_price")
	private Integer firstPrice;
	
	//续费的数量
	@JsonProperty("second_val_amount")
	private Integer secondValAmount;
	
	//续费的金额
	@JsonProperty("second_price")
	private Integer secondPrice;
}
