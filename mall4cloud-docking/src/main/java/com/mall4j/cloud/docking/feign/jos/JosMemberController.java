package com.mall4j.cloud.docking.feign.jos;

import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoDto;
import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusResp;
import com.mall4j.cloud.api.docking.jos.feign.MemberFeignClient;
import com.mall4j.cloud.docking.service.IJosMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "京东益世-自然人相关接口")
public class JosMemberController implements MemberFeignClient {

	@Autowired
	IJosMemberService josMemberService;

	/**
	 * 一、	会员以及协议基本信息接口
	 * 同步自然人信息至益世，便于后续发佣金
	 *
	 * @param memberAndProtocolInfoDto
	 * @return
	 */
	@Override
	@ApiOperation(value = "会员以及协议基本信息接口", notes = "同步自然人信息至益世，便于后续发佣金")
	public MemberAndProtocolInfoResp memberAndProtocoInfo(MemberAndProtocolInfoDto memberAndProtocolInfoDto) {
		return josMemberService.memberAndProtocoInfo(memberAndProtocolInfoDto);
	}

	/**
	 * 二、	requestId查询会员信息审核状态
	 * 查询已同步自然人信息的审核状态
	 *
	 * @param queryMemberStatusDto
	 * @return
	 */
	@Override
	@ApiOperation(value = "requestId查询会员信息审核状态", notes = "查询已同步自然人信息的审核状态")
	public QueryMemberStatusResp queryMemberStatus(QueryMemberStatusDto queryMemberStatusDto) {
		return josMemberService.queryMemberStatus(queryMemberStatusDto);
	}

	/**
	 * 三、	根据证件号码查询会员审核状态
	 * 查询已同步自然人信息的审核状态
	 *
	 * @param queryMemberAuditStatusByCertNoDto
	 * @return
	 */
	@Override
	@ApiOperation(value = "根据证件号码查询会员审核状态", notes = "查询已同步自然人信息的审核状态")
	public QueryMemberAuditStatusByCertNoResp queryMemberAuditStatusByCertNo(QueryMemberAuditStatusByCertNoDto queryMemberAuditStatusByCertNoDto) {
		return josMemberService.queryMemberAuditStatusByCertNo(queryMemberAuditStatusByCertNoDto);
	}
}
