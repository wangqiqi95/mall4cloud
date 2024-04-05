package com.mall4j.cloud.user.service;


import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.MembershipRules;

public interface MembershipRulesService {

	/**
	 * 保存会员规则
	 */
	ServerResponseEntity<Void> saveOrUpdate(MembershipRules param);

	/**
	 * 保存会员规则
	 */
	ServerResponseEntity<MembershipRules> detail();

}
