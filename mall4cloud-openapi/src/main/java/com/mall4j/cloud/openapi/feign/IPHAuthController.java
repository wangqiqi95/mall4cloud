package com.mall4j.cloud.openapi.feign;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.GetAccessTokenDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.GetCodeDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.RefreshTokenDto;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.impl.IPHAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/iph")
@Api(tags = "爱铺货授权接口")
public class IPHAuthController {

	@Autowired
	IPHAuthService iphAuthService;

	@PostMapping("/code")
	@ApiOperation(value = "爱铺货获取Code", notes = "爱铺货获取Code")
	public ServerResponseEntity<String> code(@Valid GetCodeDto getCodeDto) {
		return iphAuthService.code(getCodeDto);
	}

	@PostMapping("/accessToken")
	public JSONObject accessToken(@Valid GetAccessTokenDto getAccessTokenDto) {
		return iphAuthService.accessToken(getAccessTokenDto);
	}

	@PostMapping("/refreshToken")
	public JSONObject refreshToken(@Valid RefreshTokenDto refreshTokenDto) {
		return iphAuthService.refreshToken(refreshTokenDto);
	}
}
