package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.model.cp.CpWelcomeUseRecord;
import com.mall4j.cloud.biz.vo.cp.CpMaterialUseRecordVO;
import com.mall4j.cloud.biz.vo.cp.CpWelcomeUseRecordVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 欢迎语 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 18:13:07
 */
public interface CpWelcomeUseRecordService {

	/**
	 * 分页获取欢迎语 使用记录列表
	 * @param pageDTO 分页参数
	 * @return 欢迎语 使用记录列表分页数据
	 */
	PageVO<CpWelcomeUseRecord> page(PageDTO pageDTO);

	/**
	 * 根据欢迎语 使用记录id获取欢迎语 使用记录
	 *
	 * @param id 欢迎语 使用记录id
	 * @return 欢迎语 使用记录
	 */
	CpWelcomeUseRecord getById(Long id);

	/**
	 * 保存欢迎语 使用记录
	 * @param cpWelcomeUseRecord 欢迎语 使用记录
	 */
	void save(CpWelcomeUseRecord cpWelcomeUseRecord);

	/**
	 * 更新欢迎语 使用记录
	 * @param cpWelcomeUseRecord 欢迎语 使用记录
	 */
	void update(CpWelcomeUseRecord cpWelcomeUseRecord);

	/**
	 * 根据欢迎语 使用记录id删除欢迎语 使用记录
	 * @param id 欢迎语 使用记录id
	 */
	void deleteById(Long id);

    PageVO<CpWelcomeUseRecordVO> page(PageDTO pageDTO, MaterialUseRecordPageDTO request);

	List<CpWelcomeUseRecordVO> soldUserRecord(MaterialUseRecordPageDTO request);

	List<MaterialBrowseRecordByDayVO> useStatistics(MaterialUseRecordPageDTO request);
}
