package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.DistributionMomentsCommentsPageDTO;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsComments;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsCommentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 朋友圈互动明细数据
 *
 * @author gmq
 * @date 2024-03-04 16:47:40
 */
public interface DistributionMomentsCommentsMapper extends BaseMapper<DistributionMomentsComments> {

	/**
	 * 获取朋友圈互动明细数据列表
	 * @return 朋友圈互动明细数据列表
	 */
	List<DistributionMomentsCommentsVO> list(@Param("dto") DistributionMomentsCommentsPageDTO dto);

	/**
	 * 根据朋友圈互动明细数据id获取朋友圈互动明细数据
	 *
	 * @param id 朋友圈互动明细数据id
	 * @return 朋友圈互动明细数据
	 */
	DistributionMomentsComments getById(@Param("id") Long id);


}
