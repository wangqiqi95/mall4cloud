package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0运费计算法实体集合
 */
@Data
public class EcAllFreightCalcMethod {
	
	@JsonProperty("freight_calc_method_list")
	private List<EcFreightCalcMethod> ecFreightCalcMethodList;
}
