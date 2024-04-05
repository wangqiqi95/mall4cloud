package com.mall4j.cloud.api.docking.jos.feign;

import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoDto;
import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusResp;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: 益世会员信息接口
 * @date 2021/12/23 23:14
 */
@FeignClient(value = "mall4cloud-docking",contextId = "jos-member")
public interface MemberFeignClient {

	/**
	 * 一、	会员以及协议基本信息接口
	 * 同步自然人信息至益世，便于后续发佣金
	 * @param memberAndProtocolInfoDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/josmember/memberAndProtocoInfo")
	MemberAndProtocolInfoResp memberAndProtocoInfo(@RequestBody MemberAndProtocolInfoDto memberAndProtocolInfoDto);

	/**
	 * 二、	requestId查询会员信息审核状态
	 * 查询已同步自然人信息的审核状态
	 * @param queryMemberStatusDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/josmember/queryMemberStatus")
	QueryMemberStatusResp queryMemberStatus(@RequestBody QueryMemberStatusDto queryMemberStatusDto);

	/**
	 * 三、	根据证件号码查询会员审核状态
	 * 查询已同步自然人信息的审核状态
	 * @param queryMemberAuditStatusByCertNoDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/josmember/queryMemberAuditStatusByCertNo")
	QueryMemberAuditStatusByCertNoResp queryMemberAuditStatusByCertNo(@RequestBody QueryMemberAuditStatusByCertNoDto queryMemberAuditStatusByCertNoDto);
}
