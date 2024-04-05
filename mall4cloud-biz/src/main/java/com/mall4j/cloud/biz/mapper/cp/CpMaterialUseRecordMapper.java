package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 10:03:14
 */
public interface CpMaterialUseRecordMapper {

	/**
	 * 获取素材 使用记录列表
	 * @return 素材 使用记录列表
	 */
	List<CpMaterialUseRecord> list(@Param("request")MaterialUseRecordPageDTO request);

	/**
	 * 根据素材 使用记录id获取素材 使用记录
	 *
	 * @param id 素材 使用记录id
	 * @return 素材 使用记录
	 */
	CpMaterialUseRecord getById(@Param("id") Long id);

	/**
	 * 保存素材 使用记录
	 * @param cpMaterialUseRecord 素材 使用记录
	 */
	void save(@Param("cpMaterialUseRecord") CpMaterialUseRecord cpMaterialUseRecord);

	/**
	 * 更新素材 使用记录
	 * @param cpMaterialUseRecord 素材 使用记录
	 */
	void update(@Param("cpMaterialUseRecord") CpMaterialUseRecord cpMaterialUseRecord);

	/**
	 * 根据素材 使用记录id删除素材 使用记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<MaterialBrowseRecordByDayVO> useStatistics(@Param("request") MaterialUseRecordPageDTO request);
}
