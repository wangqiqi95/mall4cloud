package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionAuditingDTO;
import com.mall4j.cloud.distribution.model.DistributionAuditing;
import com.mall4j.cloud.distribution.vo.DistributionAuditingVO;

/**
 * 分销员申请信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
public interface DistributionAuditingService {

	/**
	 * 分页获取分销员申请信息列表
	 * @param pageDTO 分页参数
	 * @param distributionAuditingDTO
	 * @return 分销员申请信息列表分页数据
	 */
	PageVO<DistributionAuditingVO> pageDistributionAuditing(PageDTO pageDTO, DistributionAuditingDTO distributionAuditingDTO);

	/**
	 * 根据分销员申请信息id获取分销员申请信息
	 *
	 * @param auditingId 分销员申请信息id
	 * @return 分销员申请信息
	 */
	DistributionAuditing getByAuditingId(Long auditingId);

	/**
	 * 保存分销员申请信息
	 * @param distributionAuditing 分销员申请信息
	 */
	void save(DistributionAuditing distributionAuditing);

	/**
	 * 更新分销员申请信息
	 * @param distributionAuditing 分销员申请信息
	 */
	void update(DistributionAuditing distributionAuditing);

	/**
	 * 根据分销员申请信息id删除分销员申请信息
	 * @param auditingId 分销员申请信息id
	 */
	void deleteById(Long auditingId);

	/**
	 * 分销员审核操作
	 * @param distributionAuditing 审核信息
	 */
	void examine(DistributionAuditing distributionAuditing);
}
