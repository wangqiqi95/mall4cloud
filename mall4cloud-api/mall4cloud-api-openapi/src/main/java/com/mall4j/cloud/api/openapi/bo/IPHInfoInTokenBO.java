package com.mall4j.cloud.api.openapi.bo;

import com.mall4j.cloud.api.openapi.constant.SysTypeEnum;

public class IPHInfoInTokenBO implements ITokenInfoBo {

	@Override
	public SysTypeEnum getSysType() {
		return SysTypeEnum.IPH;
	}
}
