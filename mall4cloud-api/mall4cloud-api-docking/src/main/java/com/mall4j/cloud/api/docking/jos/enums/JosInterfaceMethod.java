package com.mall4j.cloud.api.docking.jos.enums;

/**
 * @description: jos接口名称枚举
 * @date 2021/12/23 23:46
 */
public enum JosInterfaceMethod {
	/**
	 * 会员以及协议基本信息接口
	 */
	MEMBER_AND_PROTOCOL_INFO_JSON("https://cf.jd.com/pages/viewpage.action?pageId=185592675", "jingdong.ysdk.MemberApplyJsfService.saveMemberAndProtocolInfo", "会员以及协议基本信息接口"),
	/**
	 * requestId查询会员信息审核状态
	 * TODO 待确认调用url
	 */
	QUERY_MEMBER_STATUS("https://api.jd.com/routerjson", "jingdong.ysdk.MemberApplyJsfService.queryMemberStatus", "requestId查询会员信息审核状态"),

	/**redis.clients.jedis.Connection
	 * 根据证件号码查询会员审核状态
	 */
	QUERY_MEMBER_AUDITSTATUS_BY_CERTNO("https://cf.jd.com/pages/viewpage.action?pageId=231486548", "jingdong.ysdk.MemberApplyJsfService.queryMemberAuditStatusByCertNo", "根据证件号码查询会员审核状态"),

	/**
	 * 益世企业综合服务发佣申请接口
	 * TODO 待确认调用url
	 */
	SETTLEMENT_APPLY("https://cf.jd.com/pages/viewpage.action?pageId=231486548", "jingdong.qyzhfw.payment.settlementApply", "益世企业综合服务发佣申请接口"),

	/**
	 * 企业综合服务发佣信息修改接口
	 * TODO 待确认调用url
	 */
	SETTLEMENT_UPDATE("https://cf.jd.com/pages/viewpage.action?pageId=231486548", "jingdong.qyzhfw.payment.settlementUpdate", "企业综合服务发佣信息修改接口"),

	/**
	 * 企业综合服务发佣状态查询接口
	 *  TODO 待确认调用url
	 */
	QUERY_SETTLEMENT_STATUS("https://cf.jd.com/pages/viewpage.action?pageId=231486548", "jingdong.qyzhfw.payment.querySettlementStatus", "企业综合服务发佣状态查询接口"),

	/**
	 * 企业综合服务查询应付金额接口
	 * TODO 待确认调用url
	 */
	QUERY_PAYAMOUNT("https://cf.jd.com/pages/viewpage.action?pageId=231486548", "jingdong.qyzhfw.payment.queryPayAmount", "企业综合服务查询应付金额接口");

	private String url;
	private String method;

	private String desc;

	JosInterfaceMethod(String url, String method, String desc) {
		this.url = url;
		this.method = method;
		this.desc = desc;
	}

	public String url() {
		return url;
	}

	public String method() {
		return method;
	}

	public String desc() {
		return desc;
	}
}
