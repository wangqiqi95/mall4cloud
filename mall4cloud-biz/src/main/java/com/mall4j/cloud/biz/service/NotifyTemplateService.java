package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.biz.vo.SmsCodeTemplateVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.NotifyTemplate;

import java.util.List;

/**
 *
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public interface NotifyTemplateService {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<NotifyTemplateVO> page(PageDTO pageDTO);

	/**
	 * 根据id获取
	 *
	 * @param templateId id
	 * @return
	 */
	NotifyTemplateVO getByTemplateId(Long templateId);

	/**
	 * 保存
	 * @param notifyTemplate
	 */
	void save(NotifyTemplate notifyTemplate);

	/**
	 * 更新
	 * @param notifyTemplate
	 */
	void update(NotifyTemplate notifyTemplate);

	/**
	 * 根据id删除
	 * @param templateId id
	 */
	void deleteById(Long templateId);


	/**
	 * 根据发送类型和店铺id，获取通知模板及店家发送类型
	 * @param sendType 发送类型
	 * @return 通知模板
	 */
	NotifyTemplateVO getBySendType(Integer sendType);

	/**
	 * 获取短信验证码模板信息
	 * @param sendType 模板类型
	 * @return 短信验证码模板信息
	 */
    SmsCodeTemplateVO getSmsCodeTemplateBySendType(Integer sendType);

	/**
	 * 根据发送类型及非当前模板id获取当前消息类型数量
	 * @param sendType 发送类型
	 * @param templateId 模板id
	 * @return 数量
	 */
	Integer countBySendType(Integer sendType, Long templateId);
}
