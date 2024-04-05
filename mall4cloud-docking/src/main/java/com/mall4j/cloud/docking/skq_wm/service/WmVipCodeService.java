package com.mall4j.cloud.docking.skq_wm.service;

import com.mall4j.cloud.api.docking.skq_erp.dto.PushOrderDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushRefundDto;
import com.mall4j.cloud.api.docking.skq_wm.dto.GetMemberCodeDTO;
import com.mall4j.cloud.api.docking.skq_wm.vo.MemberCodeVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface WmVipCodeService {

	/**
	 * 查询会员码信息
	 *
	 * @param getMemberCodeDTO 查询会员码信息
	 */
	ServerResponseEntity<MemberCodeVO> getMemberCode(GetMemberCodeDTO getMemberCodeDTO);

}
