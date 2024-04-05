package com.mall4j.cloud.biz.mapper;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.biz.model.SmsLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 短信记录表
 *
 * @author lhd
 * @date 2021-01-04 13:36:52
 */
public interface SmsLogMapper {

	/**
	 * 获取短信记录表列表
	 *
	 * @return 短信记录表列表
	 */
	List<SmsLog> list();

	/**
	 * 根据短信记录表id获取短信记录表
	 *
	 * @param id 短信记录表id
	 * @return 短信记录表
	 */
	SmsLog getById(@Param("id") Long id);

	/**
	 * 保存短信记录表
	 *
	 * @param smsLog 短信记录表
	 */
	void save(@Param("smsLog") SmsLog smsLog);

	/**
	 * 更新短信记录表
	 *
	 * @param smsLog 短信记录表
	 */
	void update(@Param("smsLog") SmsLog smsLog);

	/**
	 * 根据短信记录表id删除短信记录表
	 *
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据手机号和发送类型，失效掉对应的验证码
	 *
	 * @param mobile   手机号
	 * @param sendType 发送类型
	 */
    void invalidSmsByMobileAndType(@Param("mobile") String mobile, @Param("sendType") Integer sendType);

	/**
	 * 根据用户id和发送类型，查询当天发送的验证码条数
	 *
	 * @param beginOfDay 当天开始时间
	 * @param endOfDay   当天结束时间
	 * @param mobile     手机号
	 * @param type       发送类型
	 * @return 发送条数
	 */
	List<SmsLog> listByMobileAndTypeAndToday(@Param("beginOfDay") DateTime beginOfDay, @Param("endOfDay") DateTime endOfDay,
											 @Param("mobile") String mobile, @Param("sendType") Integer type);

	/**
	 * 根据手机号、验证码、发送类型，查询用户有效的验证码
	 *
	 * @param mobile    手机号
	 * @param validCode 验证码
	 * @param sendType  发送类型
	 * @return 验证码信息
	 */
	SmsLog getByMobileAndCodeAndType(@Param("mobile") String mobile, @Param("validCode") String validCode, @Param("sendType") Integer sendType);
}
