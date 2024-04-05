package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EcRegisterDetails {
	
	//商标注册人, R标时必填
	private String registrant;
	
	//商标注册号, R标时必填
	@JsonProperty("register_no")
	private String registerNo;
	
	//商标注册有效期, 开始时间, 长期有效可不填
	@JsonProperty("start_time")
	private Long startTime;
	
	//商标注册有效期, 结束时间, 长期有效可不填
	@JsonProperty("end_time")
	private Long endTime;
	
	//是否长期有效
	@JsonProperty("is_permanent")
	private Boolean isPermanent;
	
	//商标注册证的file_id, R标时必填, 限制最多传1张, 需要先调用“资质上传”接口上传资质图片
	@JsonProperty("register_certifications")
	private List<String> registerCertifications;
	
	//变更/续展证明的file_id, 限制最多传5张, 需要先调用“资质上传”接口上传资质图片
	@JsonProperty("renew_certifications")
	private List<String> renewCertifications;
}
