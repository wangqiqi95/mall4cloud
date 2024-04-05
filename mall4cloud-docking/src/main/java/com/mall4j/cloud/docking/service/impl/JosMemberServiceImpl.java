package com.mall4j.cloud.docking.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoDto;
import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusResp;
import com.mall4j.cloud.api.docking.jos.enums.JosInterfaceMethod;
import com.mall4j.cloud.docking.service.ICallJosInterfaceService;
import com.mall4j.cloud.docking.service.IJosMemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service("josMemberService")
public class JosMemberServiceImpl implements IJosMemberService {
	/**
	 * 一、	会员以及协议基本信息接口
	 * * 同步自然人信息至益世，便于后续发佣金
	 *
	 * @param memberAndProtocolInfoDto
	 * @return
	 */
	@Override
	public MemberAndProtocolInfoResp memberAndProtocoInfo(MemberAndProtocolInfoDto memberAndProtocolInfoDto) {
		return new ICallJosInterfaceService<MemberAndProtocolInfoResp>() {
			@Override
			public MemberAndProtocolInfoResp convert(String s) {
				if (StringUtils.isNotBlank(s)) {
					return JSONObject.parseObject(s, MemberAndProtocolInfoResp.class);
				}
				return new MemberAndProtocolInfoResp(0, "调用失败");
			}
		}.callJosInterface(JosInterfaceMethod.MEMBER_AND_PROTOCOL_INFO_JSON, memberAndProtocolInfoDto);
	}

	/**
	 * 二、	requestId查询会员信息审核状态
	 * 查询已同步自然人信息的审核状态
	 *
	 * @param queryMemberStatusDto
	 * @return
	 */
	@Override
	public QueryMemberStatusResp queryMemberStatus(QueryMemberStatusDto queryMemberStatusDto) {
		return new ICallJosInterfaceService<QueryMemberStatusResp>() {
			@Override
			public QueryMemberStatusResp convert(String s) {
				if (StringUtils.isNotBlank(s)) {
					return JSONObject.parseObject(s, QueryMemberStatusResp.class);
				}
				return new QueryMemberStatusResp(0, "调用失败");
			}
		}.callJosInterface(JosInterfaceMethod.QUERY_MEMBER_STATUS, queryMemberStatusDto);
	}

	/**
	 * 三、	根据证件号码查询会员审核状态
	 * 查询已同步自然人信息的审核状态
	 *
	 * @param queryMemberAuditStatusByCertNoDto
	 * @return
	 */
	@Override
	public QueryMemberAuditStatusByCertNoResp queryMemberAuditStatusByCertNo(QueryMemberAuditStatusByCertNoDto queryMemberAuditStatusByCertNoDto) {
		return new ICallJosInterfaceService<QueryMemberAuditStatusByCertNoResp>() {
			@Override
			public QueryMemberAuditStatusByCertNoResp convert(String s) {
				if (StringUtils.isNotBlank(s)) {
					return JSONObject.parseObject(s, QueryMemberAuditStatusByCertNoResp.class);
				}
				return new QueryMemberAuditStatusByCertNoResp(0, "调用失败");
			}
		}.callJosInterface(JosInterfaceMethod.QUERY_MEMBER_AUDITSTATUS_BY_CERTNO, queryMemberAuditStatusByCertNoDto);
	}
}
