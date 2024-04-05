package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.dto.NotifyTemplateDTO;
import com.mall4j.cloud.biz.model.NotifyTemplateTag;
import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author cl
 * @date 2021-05-20 11:09:53
 */
public interface NotifyTemplateTagMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<NotifyTemplateTag> list();

	/**
	 * 根据id获取
	 *
	 * @param notifyTagId id
	 * @return
	 */
	NotifyTemplateTag getByNotifyTagId(@Param("notifyTagId") Long notifyTagId);

	/**
	 * 保存
	 * @param notifyTemplateTag
	 */
	void save(@Param("notifyTemplateTag") NotifyTemplateTag notifyTemplateTag);

	/**
	 * 更新
	 * @param notifyTemplateTag
	 */
	void update(@Param("notifyTemplateTag") NotifyTemplateTag notifyTemplateTag);

	/**
	 * 根据id删除
	 * @param notifyTagId
	 */
	void deleteById(@Param("notifyTagId") Long notifyTagId);

	/**
	 * 获取标签消息模板
	 * @param pageAdapter 分页参数
	 * @return 消息模板列表
	 */
    List<NotifyTemplateVO> pageTagNotify(@Param("page") PageAdapter pageAdapter);

	/**
	 *  统计消息模板数量
	 * 分页方法 pageTagNotify
	 * @return 数量
	 */
	int countTagNotify();

	/**
	 * 批量保存标签信息模板
	 * @param notifyTemplateTags 消息标签关联列表
	 * @return 插入行数
	 */
    int saveBatch(@Param("notifyTemplateTags") List<NotifyTemplateTag> notifyTemplateTags);

	/**
	 * 删除模板下的所有关联标签信息
	 * @param templateId 模板id
	 */
	void deleteTemplateTagByTempLateId(@Param("templateId") Long templateId);

	/**
	 * 获取标签下的所有标签id
	 * @param templateId 消息模板id
	 * @return 标签集合
	 */
    List<Long> getTagIdsByTemplateId(@Param("templateId") Long templateId);

	/**
	 * 根据模板id获取模板信息
	 * @param templateId 模板id
	 * @return 模板信息（包含模板下的标签信息）
	 */
	NotifyTemplateVO getByTemplateId(@Param("templateId") Long templateId);

	/**
	 * 根据标签id删除模板与标签关联关系
	 * @param tagId
	 */
    void deleteTemplateTagByTagId(@Param("tagId") Long tagId);
}
