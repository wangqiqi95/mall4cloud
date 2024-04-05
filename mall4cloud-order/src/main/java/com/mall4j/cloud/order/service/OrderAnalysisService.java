package com.mall4j.cloud.order.service;


import com.mall4j.cloud.api.order.dto.CustomerRetainedDTO;
import com.mall4j.cloud.api.order.vo.CustomerRetainVO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.api.user.vo.MemberDealRespVO;

import java.util.List;

/**
 * 订单分析
 *
 * @author cl
 * @date 2021-05-25 09:13:02
 */
public interface OrderAnalysisService {

    /**
     * 获取用户的成交留存率
     * @param customerRetainedDTO 条件查询参数
     * @return 成交留存信息列表
     */
    List<CustomerRetainVO> getTradeRetained(CustomerRetainedDTO customerRetainedDTO);


    /**
     * 清除成交留存率换成
     * @param customerRetainedDTO 参数
     */
    void removeCacheTradeRetained(CustomerRetainedDTO customerRetainedDTO);
}
