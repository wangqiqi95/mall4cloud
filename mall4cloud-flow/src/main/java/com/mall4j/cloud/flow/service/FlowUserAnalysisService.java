package com.mall4j.cloud.flow.service;

import com.mall4j.cloud.flow.dto.FlowAnalysisDTO;
import com.mall4j.cloud.flow.vo.FlowAnalysisVO;
import com.mall4j.cloud.flow.vo.SystemVO;

import java.util.List;
/**
 * 流量分析—用户访问数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface FlowUserAnalysisService {
    /**
     * 流量总览
     *
     * @param flowAnalysisDTO
     * @return
     */
    FlowAnalysisVO getFlowAnalysisData(FlowAnalysisDTO flowAnalysisDTO);

    /**
     * 流量趋势
     *
     * @param flowAnalysisDTO
     * @return
     */
    List<FlowAnalysisVO> flowTrend(FlowAnalysisDTO flowAnalysisDTO);

    /**
     * 流量来源构成
     *
     * @param flowAnalysisDTO
     * @return
     */
    List<FlowAnalysisVO> flowSour(FlowAnalysisDTO flowAnalysisDTO);

    /**
     * 系统访客数量
     *
     * @param flowAnalysisDTO
     * @return
     */
    SystemVO systemTypeNums(FlowAnalysisDTO flowAnalysisDTO);
}
