package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.CpChatScriptUseRecord;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 话术 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 10:25:19
 */
public interface CpChatScriptUseRecordMapper {

	/**
	 * 获取话术 使用记录列表
	 * @return 话术 使用记录列表
	 */
	List<CpChatScriptUseRecord> list(@Param("request")MaterialUseRecordPageDTO request);

	/**
	 * 根据话术 使用记录id获取话术 使用记录
	 *
	 * @param id 话术 使用记录id
	 * @return 话术 使用记录
	 */
	CpChatScriptUseRecord getById(@Param("id") Long id);

	/**
	 * 保存话术 使用记录
	 * @param cpChatScriptUseRecord 话术 使用记录
	 */
	void save(@Param("cpChatScriptUseRecord") CpChatScriptUseRecord cpChatScriptUseRecord);

	/**
	 * 更新话术 使用记录
	 * @param cpChatScriptUseRecord 话术 使用记录
	 */
	void update(@Param("cpChatScriptUseRecord") CpChatScriptUseRecord cpChatScriptUseRecord);

	/**
	 * 根据话术 使用记录id删除话术 使用记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<MaterialBrowseRecordByDayVO> useStatistics(@Param("request")MaterialUseRecordPageDTO request);
}
