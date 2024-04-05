package com.mall4j.cloud.docking.service;

import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoDto;
import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusResp;

/**
 * @description: jos 会员信息接口对接实现
 * @date 2021/12/23 23:22
 */
public interface IJosMemberService {

	/**
	 * 一、	会员以及协议基本信息接口
	 * 	 * 同步自然人信息至益世，便于后续发佣金
	 * @param memberAndProtocolInfoDto
	 * @return
	 */
	MemberAndProtocolInfoResp memberAndProtocoInfo(MemberAndProtocolInfoDto memberAndProtocolInfoDto);

	/**
	 * 二、	requestId查询会员信息审核状态
	 * 查询已同步自然人信息的审核状态
	 *
	 * @param queryMemberStatusDto
	 * @return
	 */
	QueryMemberStatusResp queryMemberStatus(QueryMemberStatusDto queryMemberStatusDto);

	/**
	 * 三、	根据证件号码查询会员审核状态
	 * 查询已同步自然人信息的审核状态
	 * @param queryMemberAuditStatusByCertNoDto
	 * @return
	 */
	QueryMemberAuditStatusByCertNoResp queryMemberAuditStatusByCertNo(QueryMemberAuditStatusByCertNoDto queryMemberAuditStatusByCertNoDto);

}
