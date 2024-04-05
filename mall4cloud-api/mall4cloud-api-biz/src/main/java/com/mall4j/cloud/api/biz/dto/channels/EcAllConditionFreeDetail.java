package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0条件包邮详情集合
 */
@Data
public class EcAllConditionFreeDetail {

	@JsonProperty("condition_free_detail_list")
	private List<EcConditionFreeDetail> ecConditionFreeDetailList;
}
