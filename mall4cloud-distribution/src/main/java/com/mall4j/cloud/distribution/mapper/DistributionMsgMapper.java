package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销公告信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
public interface DistributionMsgMapper {

	/**
	 * 获取分销公告信息列表
	 * @param msgTitle 公告标题
	 * @return 分销公告信息列表
	 */
	List<DistributionMsg> list(@Param("msgTitle")String msgTitle);

	/**
	 * 获取APP分销公告信息列表
	 * @param isTop
	 * @return 分销公告信息列表
	 */
	List<DistributionMsg> listApp(Integer isTop);

	/**
	 * 根据分销公告信息id获取分销公告信息
	 *
	 * @param msgId 分销公告信息id
	 * @return 分销公告信息
	 */
	DistributionMsg getByMsgId(@Param("msgId") Long msgId);

	/**
	 * 保存分销公告信息
	 * @param distributionMsg 分销公告信息
	 */
	void save(@Param("distributionMsg") DistributionMsg distributionMsg);

	/**
	 * 更新分销公告信息
	 * @param distributionMsg 分销公告信息
	 */
	void update(@Param("distributionMsg") DistributionMsg distributionMsg);

	/**
	 * 根据分销公告信息id删除分销公告信息
	 * @param msgId
	 */
	void deleteById(@Param("msgId") Long msgId);

	/**
	 * 批量删除公告
	 * @param msgIds
	 */
	void deleteBatch(@Param("msgIds")List<Long> msgIds);
}
