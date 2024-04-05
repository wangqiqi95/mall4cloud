package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.CpChatScriptMenu;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * 话术分类表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
public interface CpChatScriptMenuService {

	/**
	 * 分页获取话术分类表列表
	 * @param pageDTO 分页参数
	 * @return 话术分类表列表分页数据
	 */
	PageVO<CpChatScriptMenu> page(PageDTO pageDTO);

	/**
	 * 根据话术分类表id获取话术分类表
	 *
	 * @param id 话术分类表id
	 * @return 话术分类表
	 */
	CpChatScriptMenu getById(Long id);

	/**
	 * 保存话术分类表
	 * @param cpChatScriptMenu 话术分类表
	 */
	void save(CpChatScriptMenu cpChatScriptMenu);

	/**
	 * 更新话术分类表
	 * @param cpChatScriptMenu 话术分类表
	 */
	void update(CpChatScriptMenu cpChatScriptMenu);

	/**
	 * 根据话术分类表id删除话术分类表
	 * @param id 话术分类表id
	 */
	void deleteById(Long id);

	List<CpChatScriptMenu> listParent();

	List<CpChatScriptMenu> listChildren(Long id);

	List<CpChatScriptMenu> listParentContainChildren(Integer type, Integer isShow);

	void logicDeleteById(Long id);
}
