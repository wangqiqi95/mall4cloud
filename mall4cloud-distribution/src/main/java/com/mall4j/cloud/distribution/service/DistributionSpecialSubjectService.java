package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionSpecialSubjectDTO;
import com.mall4j.cloud.distribution.model.DistributionSpecialSubject;
import com.mall4j.cloud.distribution.vo.DistributionSpecialSubjectVO;

import java.util.List;

/**
 * 分销推广-分销专题
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:56
 */
public interface DistributionSpecialSubjectService {

	/**
	 * 分页获取分销推广-分销专题列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-分销专题列表分页数据
	 */
	PageVO<DistributionSpecialSubjectVO> page(PageDTO pageDTO, DistributionSpecialSubjectDTO dto);

	/**
	 * 根据分销推广-分销专题id获取分销推广-分销专题
	 *
	 * @param id 分销推广-分销专题id
	 * @return 分销推广-分销专题
	 */
	DistributionSpecialSubjectDTO getSpecialSubjectById(Long id);

	DistributionSpecialSubject getById(Long id);

	/**
	 * 保存分销推广-分销专题
	 * @param dto 分销推广-分销专题
	 */
	void save(DistributionSpecialSubjectDTO dto);

	/**
	 * 更新分销推广-分销专题
	 * @param dto 分销推广-分销专题
	 */
	void update(DistributionSpecialSubjectDTO dto);


	void updateStatusBatch(List<Long> ids, Integer status);

	/**
	 * 根据分销推广-分销专题id删除分销推广-分销专题
	 * @param id 分销推广-分销专题id
	 */
	void deleteById(Long id);

	void specialSubjectTop(Long id, Integer top);

    PageVO<DistributionSpecialSubjectVO> pageEffect(PageDTO pageDTO, DistributionSpecialSubjectDTO dto);
}
