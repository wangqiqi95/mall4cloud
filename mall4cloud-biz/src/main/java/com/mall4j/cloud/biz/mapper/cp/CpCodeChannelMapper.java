package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.CpCodeChannel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活码渠道标识表
 *
 * @author gmq
 * @date 2023-11-01 10:33:33
 */
public interface CpCodeChannelMapper extends BaseMapper<CpCodeChannel> {

	/**
	 * 获取活码渠道标识表列表
	 * @return 活码渠道标识表列表
	 */
	List<CpCodeChannel> list();

	/**
	 * 根据活码渠道标识表id获取活码渠道标识表
	 *
	 * @param id 活码渠道标识表id
	 * @return 活码渠道标识表
	 */
	CpCodeChannel getById(@Param("id") Long id);

	CpCodeChannel getBySourceState(@Param("sourceState") String sourceState);

	List<CpCodeChannel> getBySourceStates(@Param("sourceStates") List<String> sourceState);

	/**
	 * 根据活码渠道标识表id删除活码渠道标识表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
