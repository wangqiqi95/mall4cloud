package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.CpChatScriptMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 话术分类表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
public interface CpChatScriptMenuMapper {

	/**
	 * 获取话术分类表列表
	 * @return 话术分类表列表
	 */
	List<CpChatScriptMenu> list();

	/**
	 * 根据话术分类表id获取话术分类表
	 *
	 * @param id 话术分类表id
	 * @return 话术分类表
	 */
	CpChatScriptMenu getById(@Param("id") Long id);

	/**
	 * 保存话术分类表
	 * @param cpChatScriptMenu 话术分类表
	 */
	void save(@Param("cpChatScriptMenu") CpChatScriptMenu cpChatScriptMenu);

	/**
	 * 更新话术分类表
	 * @param cpChatScriptMenu 话术分类表
	 */
	void update(@Param("cpChatScriptMenu") CpChatScriptMenu cpChatScriptMenu);

	/**
	 * 根据话术分类表id删除话术分类表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<CpChatScriptMenu> listParent();

	List<CpChatScriptMenu> listChildren(@Param("parentId")Long parentId);

	List<CpChatScriptMenu> listParentContainChildren(@Param("type")Integer type, @Param("isShow")Integer isShow);

	void logicDeleteById(@Param("id") Long id);

    List<Integer> checkMaterial(@Param("parentId")Long parentId);

	/**
	 * 根据素材类型ID查询素材数量
	 * @return 素材类型ID
	 */
	Integer getMaterialCountByMatTypeId(@Param("matTypeIdList") List<Integer> matTypeIdList);
}
