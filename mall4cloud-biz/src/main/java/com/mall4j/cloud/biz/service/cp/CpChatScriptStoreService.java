package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.CpChatScriptStore;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 话术部门 表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
public interface CpChatScriptStoreService {

	/**
	 * 分页获取话术部门 表列表
	 * @param pageDTO 分页参数
	 * @return 话术部门 表列表分页数据
	 */
	PageVO<CpChatScriptStore> page(PageDTO pageDTO);

	/**
	 * 根据话术部门 表id获取话术部门 表
	 *
	 * @param id 话术部门 表id
	 * @return 话术部门 表
	 */
	CpChatScriptStore getById(Long id);

	/**
	 * 保存话术部门 表
	 * @param cpChatScriptStore 话术部门 表
	 */
	void save(CpChatScriptStore cpChatScriptStore);

	/**
	 * 更新话术部门 表
	 * @param cpChatScriptStore 话术部门 表
	 */
	void update(CpChatScriptStore cpChatScriptStore);

	/**
	 * 根据话术部门 表id删除话术部门 表
	 * @param id 话术部门 表id
	 */
	void deleteById(Long id);

    void deleteByScriptId(Long scriptId);
}
