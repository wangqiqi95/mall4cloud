package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.CpChatScriptPageDTO;
import com.mall4j.cloud.biz.model.cp.CpChatScript;
import com.mall4j.cloud.biz.vo.cp.CpChatScriptpageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 话术表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
public interface CpChatScriptMapper {

	/**
	 * 获取话术表列表
	 * @return 话术表列表
	 */
	List<CpChatScript> list(@Param("request") CpChatScriptPageDTO request);



	List<CpChatScriptpageVO> page(@Param("request") CpChatScriptPageDTO request);

	List<CpChatScriptpageVO> appPage(@Param("request") CpChatScriptPageDTO request);

	List<CpChatScriptpageVO> appRecomdPage(@Param("request") CpChatScriptPageDTO request);

	/**
	 * 根据话术表id获取话术表
	 *
	 * @param id 话术表id
	 * @return 话术表
	 */
	CpChatScript getById(@Param("id") Long id);

	/**
	 * 保存话术表
	 * @param cpChatScript 话术表
	 */
	void save(@Param("cpChatScript") CpChatScript cpChatScript);

	/**
	 * 更新话术表
	 * @param cpChatScript 话术表
	 */
	void update(@Param("cpChatScript") CpChatScript cpChatScript);

	void updateStatus(@Param("cpChatScript") CpChatScript cpChatScript);

	void updateUseNum(@Param("cpChatScript") CpChatScript cpChatScript);

	/**
	 * 根据话术表id删除话术表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void logicDeleteById(@Param("id")Long id);

}
