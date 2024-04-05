package com.mall4j.cloud.api.openapi.bo;

import com.mall4j.cloud.api.openapi.constant.SysTypeEnum;

public interface ITokenInfoBo {

	SysTypeEnum getSysType();

	default Long getUid() {
		return null;
	}

	default Integer getSysTypeValue() {
		if (getSysType() != null) {
			return getSysType().value();
		}
		return null;
	}

}
