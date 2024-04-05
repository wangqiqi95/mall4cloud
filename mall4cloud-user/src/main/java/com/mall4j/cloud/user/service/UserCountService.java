package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.vo.UserCountVO;
import com.mall4j.cloud.api.user.vo.UserRelactionAddWayVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserCountDTO;
import com.mall4j.cloud.user.vo.UserRelCountDataVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 好友统计
 *
 */
public interface UserCountService {

	/**
	 * 分页获取好友统计列表
	 * @param dto 分页参数
	 * @return 好友统计列表分页数据
	 */
	PageVO<UserCountVO> page(UserCountDTO dto);

	List<UserCountVO> pageList(UserCountDTO dto);

	/**
	 * 获取性别分析
	 */
	List<UserCountVO> getSexCount(UserCountDTO dto);

	/**
	 * 获取图表统计
	 */
	List<UserCountVO> getChartCount(UserCountDTO dto);

	/**
	 * 好友统计-来源分析
	 * @param dto
	 * @return
	 */
	List<UserRelactionAddWayVO> getUserRelactionAddWays(UserCountDTO dto);

	/**
	 * 好友统计排行榜头部累计数据
	 * @param dto
	 * @return
	 */
	UserRelCountDataVO getUserRelCountDataVO(UserCountDTO dto);

}
