package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.CpChatScriptPageDTO;
import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.CpChatScript;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.vo.cp.CpChatScriptpageVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 话术表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
public interface CpChatScriptService {

	/**
	 * 分页获取话术表列表
	 * @param pageDTO 分页参数
	 * @param request
	 * @return 话术表列表分页数据
	 */
	PageVO<CpChatScriptpageVO> page(PageDTO pageDTO, CpChatScriptPageDTO request);

	List<CpChatScript> getByIds(CpChatScriptPageDTO request);

	PageVO<CpChatScriptpageVO> appPage(PageDTO pageDTO, CpChatScriptPageDTO request);

	/**
	 * 根据话术表id获取话术表
	 *
	 * @param id 话术表id
	 * @return 话术表
	 */
	CpChatScript getById(Long id);

	CpChatScriptpageVO getDetailById(Long id);

	/**
	 * 保存话术表
	 * @param cpChatScript 话术表
	 */
	void save(CpChatScript cpChatScript);

	/**
	 * 更新话术表
	 * @param cpChatScript 话术表
	 */
	void update(CpChatScript cpChatScript);

	void disableMat(CpChatScript cpChatScript);

	/**
	 * 根据话术表id删除话术表
	 * @param id 话术表id
	 */
	void deleteById(Long id);

	void logicDeleteById(Long id);

	void updateUseNum(CpChatScript cpChatScript);

}
