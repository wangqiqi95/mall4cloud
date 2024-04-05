package com.mall4j.cloud.flow.service;

import com.mall4j.cloud.flow.dto.FlowLogDTO;
/**
 * 流量分析
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface FlowService {
    /**
     * 用户流量记录
     * @param flowLogDTO
     */
    void log(FlowLogDTO flowLogDTO);

    /**
     * 统计记录数据，储存到对应的数据表中
     */
    void statisticalUser();

    /**
     * 统计记录数据，储存到对应的数据表中
     */
    void statisticalProduct();

}
