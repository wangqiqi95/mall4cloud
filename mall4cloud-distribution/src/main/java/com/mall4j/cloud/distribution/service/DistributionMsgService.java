package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionMsg;

import java.util.List;

/**
 * 分销公告信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
public interface DistributionMsgService {

	/**
	 * 分页获取分销公告信息列表
	 * @param pageDTO 分页参数
	 * @param msgTitle 公告标题
	 * @return 分销公告信息列表分页数据
	 */
	PageVO<DistributionMsg> page(PageDTO pageDTO,String msgTitle);

	/**
	 * 分页获取app端的公告信息列表
	 * @param pageDTO 分页参数
	 * @param isTop
	 * @return 分销公告信息列表分页数据
	 */
	PageVO<DistributionMsg> pageApp(PageDTO pageDTO,Integer isTop);

	/**
	 * 根据分销公告信息id获取分销公告信息
	 *
	 * @param msgId 分销公告信息id
	 * @return 分销公告信息
	 */
	DistributionMsg getByMsgId(Long msgId);

	/**
	 * 保存分销公告信息
	 * @param distributionMsg 分销公告信息
	 */
	void save(DistributionMsg distributionMsg);

	/**
	 * 更新分销公告信息
	 * @param distributionMsg 分销公告信息
	 */
	void update(DistributionMsg distributionMsg);

	/**
	 * 根据分销公告信息id删除分销公告信息
	 * @param msgId 分销公告信息id
	 */
	void deleteById(Long msgId);

	/**
	 * 批量删除公告
	 * @param msgIds
	 */
	void deleteBatch(List<Long> msgIds);
}
