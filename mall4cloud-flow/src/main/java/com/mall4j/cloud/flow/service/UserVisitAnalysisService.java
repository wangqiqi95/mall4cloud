package com.mall4j.cloud.flow.service;


import com.mall4j.cloud.flow.dto.FlowAnalysisDTO;
import com.mall4j.cloud.flow.vo.FlowUserAnalysisVO;

/**
 * 流量分析—用户访问数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface UserVisitAnalysisService {

    /**
     * 获取用户分析数据
     * @param flowAnalysisDTO
     * @return
     */
    FlowUserAnalysisVO getUserAnalysisData(FlowAnalysisDTO flowAnalysisDTO);
}
