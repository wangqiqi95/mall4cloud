package com.mall4j.cloud.flow.mapper;

import com.mall4j.cloud.flow.dto.FlowAnalysisDTO;
import com.mall4j.cloud.flow.model.UserAnalysis;
import com.mall4j.cloud.flow.vo.FlowAnalysisVO;
import com.mall4j.cloud.flow.vo.SystemVO;
import com.mall4j.cloud.flow.vo.UserAnalysisVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 流量分析—用户数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface UserAnalysisMapper {

	/**
	 * 获取流量分析—用户数据列表
	 *
	 * @return 流量分析—用户数据列表
	 */
	List<UserAnalysis> list();

	/**
	 * 根据流量分析—用户数据id获取流量分析—用户数据
	 *
	 * @param userAnalysisId 流量分析—用户数据id
	 * @return 流量分析—用户数据
	 */
	UserAnalysis getByUserAnalysisId(@Param("userAnalysisId") Long userAnalysisId);

	/**
	 * 保存流量分析—用户数据
	 *
	 * @param userAnalysis 流量分析—用户数据
	 */
	void save(@Param("userAnalysis") UserAnalysis userAnalysis);

	/**
	 * 更新流量分析—用户数据
	 *
	 * @param userAnalysis 流量分析—用户数据
	 */
	void update(@Param("userAnalysis") UserAnalysis userAnalysis);

	/**
	 * 根据流量分析—用户数据id删除流量分析—用户数据
	 *
	 * @param userAnalysisId
	 */
	void deleteById(@Param("userAnalysisId") Long userAnalysisId);

	/**
	 * 根据用户id列表，获取用户统计信息
	 *
	 * @param date
	 * @param userIds
	 * @return
	 */
    List<UserAnalysis> listByDate(@Param("date") Date date, @Param("userIds") Set<String> userIds);

	/**
	 * 批量保存流量分析—用户数据
	 *
	 * @param list
	 */
	void saveBatch(@Param("list") List<UserAnalysis> list);

	/**
	 * 批量更新流量分析—用户数据
	 *
	 * @param list
	 */
	void updateBatch(@Param("list") List<UserAnalysis> list);

	/**
	 * 根据用户id删除已合并的用户统计数据
	 * @param userAnalysisIds
	 */
    void deleteBatch(@Param("userAnalysisIds") List<Long> userAnalysisIds);

	/**
	 * 根据时间范围获取获取用户分析数据
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 * @return 用户流量分析列表
	 */
    List<UserAnalysisVO> listUserAnalysisByData(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 获取流量总览数据列表
	 *
	 * @param flowAnalysisDTO
	 * @param oldStartTime
	 * @return
	 */
    List<FlowAnalysisVO> getFlowAnalysisData(@Param("flowAnalysisDTO") FlowAnalysisDTO flowAnalysisDTO, @Param("oldStartTime") Date oldStartTime);

	/**
	 * 流量趋势
	 *
	 * @param flowAnalysisDTO
	 * @return
	 */
	List<FlowAnalysisVO> flowTrend(@Param("flowAnalysisDTO") FlowAnalysisDTO flowAnalysisDTO);

	/**
	 * 流量来源构成
	 *
	 * @param flowAnalysisDTO
	 * @return
	 */
	List<FlowAnalysisVO> flowSour(@Param("flowAnalysisDTO") FlowAnalysisDTO flowAnalysisDTO);

	/**
	 * 系统访客数量
	 *
	 * @param flowAnalysisDTO
	 * @return
	 */
	List<SystemVO> systemTypeNums(@Param("flowAnalysisDTO") FlowAnalysisDTO flowAnalysisDTO);

	/**
	 * 根据省份分组获取用户访问情况
	 * @param flowAnalysisDTO
	 * @return
	 */
	List<UserAnalysisVO> listUserAccessDataByProvinceAndDate(@Param("flowAnalysisDTO") FlowAnalysisDTO flowAnalysisDTO);

	List<com.mall4j.cloud.api.flow.vo.UserAnalysisVO> listUserAnalysisByUserId(@Param("userId") Long userId);
}
