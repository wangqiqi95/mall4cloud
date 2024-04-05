package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.DistributionMomentsDTO;
import com.mall4j.cloud.biz.dto.cp.MomentsSendDTO;
import com.mall4j.cloud.biz.model.cp.DistributionMoments;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionMomentsService {

	/**
	 * 分页获取分销推广-朋友圈列表
	 * @return 分销推广-朋友圈列表分页数据
	 */
	PageVO<DistributionMomentsVO> page(PageDTO pageDTO,DistributionMomentsDTO dto);

	/**
	 * 根据分销推广-朋友圈id获取分销推广-朋友圈
	 *
	 * @param id 分销推广-朋友圈id
	 * @return 分销推广-朋友圈
	 */
	DistributionMomentsDTO getMomentsById(Long id);

	DistributionMoments getById(Long id);

	/**
	 * 保存分销推广-朋友圈
	 * @param dto 分销推广-朋友圈
	 */
	void save(DistributionMomentsDTO dto);

	/**
	 * 更新分销推广-朋友圈
	 * @param dto 分销推广-朋友圈
	 */
	void update(DistributionMomentsDTO dto);

	/**
	 * 根据分销推广-朋友圈id删除分销推广-朋友圈
	 * @param id 分销推广-朋友圈id
	 */
	void deleteById(Long id);

	void updateStatusBatch(List<Long> ids, Integer status);

	void momentsTop(Long id, Integer top);

    PageVO<DistributionMomentsVO> pageEffect(PageDTO pageDTO, DistributionMomentsDTO dto);

	void send(MomentsSendDTO request);

	void sendByMoment(Long id);

	/**
	 * 定时任务处理超时未发送的朋友圈
	 */
	void taskTimeOut();
}
