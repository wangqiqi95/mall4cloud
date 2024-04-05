package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawProcessDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcess;
import org.springframework.web.multipart.MultipartFile;

/**
 * 佣金处理批次
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:48
 */
public interface DistributionWithdrawProcessService {

	/**
	 * 分页获取佣金处理批次列表
	 * @param pageDTO 分页参数
	 * @return 佣金处理批次列表分页数据
	 */
	PageVO<DistributionWithdrawProcess> page(PageDTO pageDTO, DistributionWithdrawProcessDTO distributionWithdrawProcessDTO);

	/**
	 * 根据佣金处理批次id获取佣金处理批次
	 *
	 * @param id 佣金处理批次id
	 * @return 佣金处理批次
	 */
	DistributionWithdrawProcess getById(Long id);

	/**
	 * 保存佣金处理批次
	 * @param distributionWithdrawProcess 佣金处理批次
	 */
	void save(DistributionWithdrawProcess distributionWithdrawProcess);

	/**
	 * 更新佣金处理批次
	 * @param distributionWithdrawProcess 佣金处理批次
	 */
	void update(DistributionWithdrawProcess distributionWithdrawProcess);

	/**
	 * 根据佣金处理批次id删除佣金处理批次
	 * @param id 佣金处理批次id
	 */
	void deleteById(Long id);

    String importUserExcel(MultipartFile file, String name);

	void execute(Long id);
}
