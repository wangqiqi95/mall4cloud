package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.DistributionMomentsCommentsDTO;
import com.mall4j.cloud.biz.dto.cp.DistributionMomentsCommentsPageDTO;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsComments;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsCommentsVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 朋友圈互动明细数据
 *
 * @author gmq
 * @date 2024-03-04 16:47:40
 */
public interface DistributionMomentsCommentsService extends IService<DistributionMomentsComments> {

	/**
	 * 分页获取朋友圈互动明细数据列表
	 * @param pageDTO 分页参数
	 * @return 朋友圈互动明细数据列表分页数据
	 */
	PageVO<DistributionMomentsCommentsVO> page(PageDTO pageDTO, DistributionMomentsCommentsPageDTO dto);

	/**
	 * 根据朋友圈互动明细数据id获取朋友圈互动明细数据
	 *
	 * @param id 朋友圈互动明细数据id
	 * @return 朋友圈互动明细数据
	 */
	DistributionMomentsComments getById(Long id);

	/**
	 *  保存明细数据
	 * @param commentsDTO
	 */
	void saveTo(DistributionMomentsCommentsDTO commentsDTO);
}
