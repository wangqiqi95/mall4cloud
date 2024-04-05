package com.mall4j.cloud.api.openapi.feign;

import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "mall4cloud-openapi",contextId = "ipuhuo-product")
public interface IPuHuoFeignClient {

	@PostMapping(value = "/iph/product")
	IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto);

}
