package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.AddressInfo;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0条件包邮详情实体
 */
@Data
public class EcConditionFreeDetail {

	//支持的地址列表
	@JsonProperty("address_infos")
	private List<AddressInfo> addressInfos;
	
	//最低件数
	@JsonProperty("min_piece")
	private Integer minPiece;
	
	//最低重量
	@JsonProperty("min_weight")
	private Integer minWeight;
	
	//最低金额
	@JsonProperty("min_amount")
	private Integer minAmount;
	
	//计费方式对应的选项是否已设置
	@JsonProperty("valuation_flag")
	private Integer valuationFlag;
	
	//金额是否设置
	@JsonProperty("amount_flag")
	private Integer amountFlag;
	
}
