package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionSpecialSubjectDTO;
import com.mall4j.cloud.distribution.model.DistributionSpecialSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-分销专题
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:56
 */
public interface DistributionSpecialSubjectMapper {

	/**
	 * 获取分销推广-分销专题列表
	 * @return 分销推广-分销专题列表
	 */
	List<DistributionSpecialSubject> list(@Param("distributionSpecialSubject") DistributionSpecialSubjectDTO dto);

	/**
	 * 根据分销推广-分销专题id获取分销推广-分销专题
	 *
	 * @param id 分销推广-分销专题id
	 * @return 分销推广-分销专题
	 */
	DistributionSpecialSubject getById(@Param("id") Long id);

	/**
	 * 保存分销推广-分销专题
	 * @param distributionSpecialSubject 分销推广-分销专题
	 */
	void save(@Param("distributionSpecialSubject") DistributionSpecialSubject distributionSpecialSubject);

	/**
	 * 更新分销推广-分销专题
	 * @param distributionSpecialSubject 分销推广-分销专题
	 */
	void update(@Param("distributionSpecialSubject") DistributionSpecialSubject distributionSpecialSubject);

	void updateStatusBatch(@Param("ids") List<Long> ids, @Param("status") Integer status);

	/**
	 * 根据分销推广-分销专题id删除分销推广-分销专题
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	int countSpecialSubjectTopNum();

	void specialSubjectTop(@Param("id") Long id, @Param("top") Integer top);

	List<DistributionSpecialSubject> pageEffect(@Param("distributionSpecialSubject") DistributionSpecialSubjectDTO dto);
}
