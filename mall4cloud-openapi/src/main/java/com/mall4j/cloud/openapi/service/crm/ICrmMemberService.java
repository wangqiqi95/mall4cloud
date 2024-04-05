package com.mall4j.cloud.openapi.service.crm;

import com.mall4j.cloud.api.openapi.skq_crm.dto.CrmUpdateUserDto;
import com.mall4j.cloud.api.user.dto.CrmUserSyncDTO;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;

public interface ICrmMemberService {

	/**
	 * 方法描述：会员信息更新（crm端）
	 * 			crm端会员基础信息（包含等级）更新时推送给小程序
	 * @param crmUpdateUserDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 */
	CrmResponse updateSync(CrmUpdateUserDto crmUpdateUserDto);

	CrmResponse userSync(CrmUserSyncDTO crmUserSyncDTO);

}
