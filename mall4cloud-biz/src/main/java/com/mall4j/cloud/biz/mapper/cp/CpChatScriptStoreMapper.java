package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.CpChatScriptStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 话术部门 表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
public interface CpChatScriptStoreMapper {

	/**
	 * 获取话术部门 表列表
	 * @return 话术部门 表列表
	 */
	List<CpChatScriptStore> list();

	/**
	 * 根据话术部门 表id获取话术部门 表
	 *
	 * @param id 话术部门 表id
	 * @return 话术部门 表
	 */
	CpChatScriptStore getById(@Param("id") Long id);

	/**
	 * 保存话术部门 表
	 * @param cpChatScriptStore 话术部门 表
	 */
	void save(@Param("cpChatScriptStore") CpChatScriptStore cpChatScriptStore);

	/**
	 * 更新话术部门 表
	 * @param cpChatScriptStore 话术部门 表
	 */
	void update(@Param("cpChatScriptStore") CpChatScriptStore cpChatScriptStore);

	/**
	 * 根据话术部门 表id删除话术部门 表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    void deleteByScriptId(@Param("scriptId")Long scriptId);

	List<CpChatScriptStore> getByScriptId(@Param("scriptId")Long scriptId);
}
