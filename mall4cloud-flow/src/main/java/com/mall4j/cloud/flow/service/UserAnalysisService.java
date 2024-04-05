package com.mall4j.cloud.flow.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.flow.dto.FlowLogDTO;
import com.mall4j.cloud.flow.model.UserAnalysis;

import java.util.List;

/**
 * 流量分析—用户数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface UserAnalysisService {

	/**
	 * 分页获取流量分析—用户数据列表
	 * @param pageDTO 分页参数
	 * @return 流量分析—用户数据列表分页数据
	 */
	PageVO<UserAnalysis> page(PageDTO pageDTO);

	/**
	 * 根据流量分析—用户数据id获取流量分析—用户数据
	 *
	 * @param userAnalysisId 流量分析—用户数据id
	 * @return 流量分析—用户数据
	 */
	UserAnalysis getByUserAnalysisId(Long userAnalysisId);

	/**
	 * 保存流量分析—用户数据
	 * @param userAnalysis 流量分析—用户数据
	 */
	void save(UserAnalysis userAnalysis);

	/**
	 * 更新流量分析—用户数据
	 * @param userAnalysis 流量分析—用户数据
	 */
	void update(UserAnalysis userAnalysis);

	/**
	 * 根据流量分析—用户数据id删除流量分析—用户数据
	 * @param userAnalysisId 流量分析—用户数据id
	 */
	void deleteById(Long userAnalysisId);

	/**
	 * 统计用户信息
	 * @param flowLogList
	 */
    void statisticalUser(List<FlowLogDTO> flowLogList);

}
