package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.CpWelcomeUseRecord;
import com.mall4j.cloud.biz.vo.cp.CpWelcomeUseRecordVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 欢迎语 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 18:13:07
 */
public interface CpWelcomeUseRecordMapper {

	/**
	 * 获取欢迎语 使用记录列表
	 * @return 欢迎语 使用记录列表
	 */
	List<CpWelcomeUseRecord> list();

	/**
	 * 根据欢迎语 使用记录id获取欢迎语 使用记录
	 *
	 * @param id 欢迎语 使用记录id
	 * @return 欢迎语 使用记录
	 */
	CpWelcomeUseRecord getById(@Param("id") Long id);

	/**
	 * 保存欢迎语 使用记录
	 * @param cpWelcomeUseRecord 欢迎语 使用记录
	 */
	void save(@Param("cpWelcomeUseRecord") CpWelcomeUseRecord cpWelcomeUseRecord);

	/**
	 * 更新欢迎语 使用记录
	 * @param cpWelcomeUseRecord 欢迎语 使用记录
	 */
	void update(@Param("cpWelcomeUseRecord") CpWelcomeUseRecord cpWelcomeUseRecord);

	/**
	 * 根据欢迎语 使用记录id删除欢迎语 使用记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);


	List<MaterialBrowseRecordByDayVO> useStatistics(@Param("request")MaterialUseRecordPageDTO request);

	List<CpWelcomeUseRecordVO> page(@Param("request") MaterialUseRecordPageDTO request);
}
