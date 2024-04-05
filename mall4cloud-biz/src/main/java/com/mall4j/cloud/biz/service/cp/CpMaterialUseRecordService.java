package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 素材 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 10:03:14
 */
public interface CpMaterialUseRecordService {

	/**
	 * 分页获取素材 使用记录列表
	 * @param pageDTO 分页参数
	 * @return 素材 使用记录列表分页数据
	 */
	PageVO<CpMaterialUseRecord> page(PageDTO pageDTO, MaterialUseRecordPageDTO request);

	/**
	 * 导出
	 * @param request
	 * @return
	 */
	List<CpMaterialUseRecord> exportPage(MaterialUseRecordPageDTO request);

	/**
	 * 根据素材 使用记录id获取素材 使用记录
	 *
	 * @param id 素材 使用记录id
	 * @return 素材 使用记录
	 */
	CpMaterialUseRecord getById(Long id);

	/**
	 * 保存素材 使用记录
	 * @param cpMaterialUseRecord 素材 使用记录
	 */
	void save(CpMaterialUseRecord cpMaterialUseRecord);

	/**
	 * 更新素材 使用记录
	 * @param cpMaterialUseRecord 素材 使用记录
	 */
	void update(CpMaterialUseRecord cpMaterialUseRecord);

	/**
	 * 根据素材 使用记录id删除素材 使用记录
	 * @param id 素材 使用记录id
	 */
	void deleteById(Long id);

	List<MaterialBrowseRecordByDayVO> useStatistics(MaterialUseRecordPageDTO request);

	void use(Long id);

	void use(Long matId,Long staffId);
}
