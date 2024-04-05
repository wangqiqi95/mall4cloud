package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.CpCodeChannel;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 活码渠道标识表
 *
 * @author gmq
 * @date 2023-11-01 10:33:33
 */
public interface CpCodeChannelService extends IService<CpCodeChannel> {

	/**
	 * 分页获取活码渠道标识表列表
	 * @param pageDTO 分页参数
	 * @return 活码渠道标识表列表分页数据
	 */
	PageVO<CpCodeChannel> page(PageDTO pageDTO);

	/**
	 * 根据活码渠道标识表id获取活码渠道标识表
	 *
	 * @param id 活码渠道标识表id
	 * @return 活码渠道标识表
	 */
	CpCodeChannel getById(Long id);

	CpCodeChannel getBySourceState(String sourceState);

	List<CpCodeChannel> getBySourceStates(List<String> sourceState);

	void saveCpCodeChannel(Integer sourceFrom,String sourceId,String baseId,String sourceState);
	void saveCpCodeChannel(List<CpCodeChannel> cpCodeChannels);

	/**
	 * 根据活码渠道标识表id删除活码渠道标识表
	 * @param id 活码渠道标识表id
	 */
	void deleteById(Long id);
}
