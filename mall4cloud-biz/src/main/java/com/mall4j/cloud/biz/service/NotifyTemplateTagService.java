package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.NotifyTemplateDTO;
import com.mall4j.cloud.biz.model.NotifyTemplate;
import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.NotifyTemplateTag;

import java.util.List;

/**
 *
 *
 * @author cl
 * @date 2021-05-20 11:09:53
 */
public interface NotifyTemplateTagService {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<NotifyTemplateTag> page(PageDTO pageDTO);

	/**
	 * 根据id获取
	 *
	 * @param notifyTagId id
	 * @return
	 */
	NotifyTemplateTag getByNotifyTagId(Long notifyTagId);

	/**
	 * 保存
	 * @param notifyTemplateTag
	 */
	void save(NotifyTemplateTag notifyTemplateTag);

	/**
	 * 更新
	 * @param notifyTemplateTag
	 */
	void update(NotifyTemplateTag notifyTemplateTag);

	/**
	 * 根据id删除
	 * @param notifyTagId id
	 */
	void deleteById(Long notifyTagId);

	/**
	 * 分页获取标签消息列表
	 * @param pageDTO 分页参数
	 * @return
	 */
	PageVO<NotifyTemplateVO> pageTagNotify(PageDTO pageDTO);

	/**
	 * 保存标签消息模板
	 * @param notifyTemplate 消息模板
	 * @param tagIds 标签id
	 */
    void saveTagNotify(NotifyTemplate notifyTemplate, List<Long> tagIds);

	/**
	 * 删除模板并且删除模板下的所有关联标签信息
	 * @param templateId 模板id
	 * @return 删除行数
	 */
	void deleteTemplateTagByTempLateId(Long templateId);

	/**
	 * 给模板消息的标签用户发送站内小心
	 * @param templateId
	 */
    void sendMsg(Long templateId);

	/**
	 * 根据模板id获取模板信息
	 * @param templateId 模板id
	 * @return 模板信息（包含模板下的标签信息）
	 */
	NotifyTemplateVO getByTemplateId(Long templateId);

	/**
	 * 修改消息模板（包含模板下的标签信息）
	 * @param notifyTemplateDTO 模板参数
	 */
	void updateTemplateAndTag(NotifyTemplateDTO notifyTemplateDTO);

	/**
	 * 根据标签id删除模板与标签关联关系
	 * @param tagId
	 */
    void deleteByTagId(Long tagId);
}
