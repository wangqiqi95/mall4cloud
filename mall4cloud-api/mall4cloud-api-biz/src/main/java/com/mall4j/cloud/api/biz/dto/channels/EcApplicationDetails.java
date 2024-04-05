package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EcApplicationDetails {
	//商标申请受理时间, TM标时必填
	@JsonProperty("acceptance_time")
	private Long acceptanceTime;
	
	//商标注册申请受理书file_id, TM标时必填, 限制最多传1张, 需要先调用“资质上传”接口上传资质图片
	@JsonProperty("acceptance_certification")
	private List<String> acceptanceCertification;
	
	//商标申请号, TM标时必填
	@JsonProperty("acceptance_no")
	private String acceptanceNo;
}
