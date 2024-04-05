package com.mall4j.cloud.openapi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.GetAccessTokenDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.GetCodeDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.RefreshTokenDto;
import com.mall4j.cloud.common.response.ServerResponseEntity;

public interface IPHAuthService {
	ServerResponseEntity<String> code(GetCodeDto getCodeDto);

	JSONObject accessToken(GetAccessTokenDto getAccessTokenDto);

	JSONObject refreshToken(RefreshTokenDto refreshTokenDto);
}
