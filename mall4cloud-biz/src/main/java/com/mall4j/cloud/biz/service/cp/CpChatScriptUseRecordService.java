package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.CpChatScriptUseRecord;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 话术 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 10:25:19
 */
public interface CpChatScriptUseRecordService {

	/**
	 * 分页获取话术 使用记录列表
	 * @param pageDTO 分页参数
	 * @return 话术 使用记录列表分页数据
	 */
	PageVO<CpChatScriptUseRecord> page(PageDTO pageDTO);

	/**
	 * 根据话术 使用记录id获取话术 使用记录
	 *
	 * @param id 话术 使用记录id
	 * @return 话术 使用记录
	 */
	CpChatScriptUseRecord getById(Long id);

	/**
	 * 保存话术 使用记录
	 * @param cpChatScriptUseRecord 话术 使用记录
	 */
	void save(CpChatScriptUseRecord cpChatScriptUseRecord);

	/**
	 * 更新话术 使用记录
	 * @param cpChatScriptUseRecord 话术 使用记录
	 */
	void update(CpChatScriptUseRecord cpChatScriptUseRecord);

	/**
	 * 根据话术 使用记录id删除话术 使用记录
	 * @param id 话术 使用记录id
	 */
	void deleteById(Long id);


	PageVO<CpChatScriptUseRecord> usePage(PageDTO pageDTO, MaterialUseRecordPageDTO request);

	List<CpChatScriptUseRecord> soldUsePage( MaterialUseRecordPageDTO request);

	List<MaterialBrowseRecordByDayVO> useStatistics(MaterialUseRecordPageDTO request);

    void use(Long id, Long staffId);
}
