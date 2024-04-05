package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.common.constant.SendTypeEnum;

import java.util.Map;

/**
 * 短信记录表
 *
 * @author lhd
 * @date 2021-01-04 13:36:52
 */
public interface SmsLogService {


	/**
	 * 送注册、登录、修改密码、校验等短信
	 * @param smsType 通知短信参数
	 * @param mobile 手机号
	 * @param params 短信参数
	 */
	void sendSmsCode(SendTypeEnum smsType, String mobile, Map<String,String> params);

	/**
	 * 发送通知短信
	 * @param templateCode 通知短信参数
	 * @param userMobile 通知短信手机号
	 * @param smsParam 短信参数
	 * @return 返回发送结果
	 */
    Boolean sendMsgSms(String templateCode, String userMobile, Map<String, String> smsParam);

	/**
	 * 校验验证码
	 * @param mobile 手机号
	 * @param validCode 验证码
	 * @param type 类型
	 * @return 成功or失败
	 */
	Boolean checkValidCode(String mobile, String validCode, SendTypeEnum type);
}
