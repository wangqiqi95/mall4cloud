package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionAuditingDTO;
import com.mall4j.cloud.distribution.model.DistributionAuditing;
import com.mall4j.cloud.distribution.vo.DistributionAuditingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销员申请信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
public interface DistributionAuditingMapper {

	/**
	 * 获取分销员申请信息列表
	 * @param distributionAuditing
	 * @return 分销员申请信息列表
	 */
	List<DistributionAuditingVO> listDistributionAuditing(@Param("distributionAuditing") DistributionAuditingDTO distributionAuditing);

	/**
	 * 根据分销员申请信息id获取分销员申请信息
	 *
	 * @param auditingId 分销员申请信息id
	 * @return 分销员申请信息
	 */
	DistributionAuditing getByAuditingId(@Param("auditingId") Long auditingId);

	/**
	 * 保存分销员申请信息
	 * @param distributionAuditing 分销员申请信息
	 */
	void save(@Param("distributionAuditing") DistributionAuditing distributionAuditing);

	/**
	 * 更新分销员申请信息
	 * @param distributionAuditing 分销员申请信息
	 */
	void update(@Param("distributionAuditing") DistributionAuditing distributionAuditing);

	/**
	 * 根据分销员申请信息id删除分销员申请信息
	 * @param auditingId
	 */
	void deleteById(@Param("auditingId") Long auditingId);

	/**
	 * 获取分销员申请信息
	 * @param distributionUserId 分销员id
	 * @return 分销员申请信息
	 */
    DistributionAuditingVO getByDistributionUserId(@Param("distributionUserId") Long distributionUserId);
}
