package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.NotifyTemplate;
import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.biz.vo.SmsCodeTemplateVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public interface NotifyTemplateMapper {

	/**
	 * 获取列表
	 *
	 * @return 列表
	 */
	List<NotifyTemplateVO> list();

	/**
	 * 根据id获取
	 *
	 * @param templateId id
	 * @return
	 */
	NotifyTemplateVO getByTemplateId(@Param("templateId") Long templateId);

	/**
	 * 保存
	 *
	 * @param notifyTemplate
	 */
	void save(@Param("notifyTemplate") NotifyTemplate notifyTemplate);

	/**
	 * 更新
	 *
	 * @param notifyTemplate
	 */
	void update(@Param("notifyTemplate") NotifyTemplate notifyTemplate);

	/**
	 * 根据id删除
	 *
	 * @param templateId
	 */
	void deleteById(@Param("templateId") Long templateId);

	/**
	 * 根据发送类型和店铺id，获取通知模板及店家发送类型
	 * @param sendType 发送类型
	 * @return 通知模板
	 */
	NotifyTemplateVO getBySendType( @Param("sendType") Integer sendType);

	/**
	 * 获取短信验证码模板信息
	 *
	 * @param sendType 模板类型
	 * @return 短信验证码模板信息
	 */
	SmsCodeTemplateVO getSmsCodeTemplateBySendType(@Param("sendType") Integer sendType);


	/**
	 * 根据发送类型及非当前模板id获取当前消息类型数量
	 * @param sendType 发送类型
	 * @param templateId 模板id
	 * @return 数量
	 */
	Integer countBySendType(@Param("sendType") Integer sendType, @Param("templateId") Long templateId);
}
