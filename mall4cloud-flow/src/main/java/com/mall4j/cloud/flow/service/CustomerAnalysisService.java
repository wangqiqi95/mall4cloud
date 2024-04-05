package com.mall4j.cloud.flow.service;


import com.mall4j.cloud.api.order.dto.CustomerRetainedDTO;
import com.mall4j.cloud.api.order.vo.CustomerRetainVO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.api.user.vo.MemberContributeRespVO;
import com.mall4j.cloud.api.user.vo.MemberDealRespVO;
import com.mall4j.cloud.flow.vo.MemberSurveyRespVO;

import java.util.List;

/**
 * @author lgh on 2018/10/26.
 */
public interface CustomerAnalysisService {

    /**
     * 会员分析，会员贡献价值分析
     * @param param 参数
     * @return 会员分析，会员贡献价值分析
     */
    MemberContributeRespVO getMemberContributeValue(MemberReqDTO param);
    /**
     * 会员分析，新老会员成交分析
     * @param param 参数
     * @return 新老会员成交分析
     */
    MemberDealRespVO getMemberDeal(MemberReqDTO param);

    /**
     * 统计交易留存数据
     * @param customerRetainedDTO 条件参数
     * @return 留存数据
     */
    List<CustomerRetainVO> getTradeRetained(CustomerRetainedDTO customerRetainedDTO);

    /**
     * 访问留存数据
     * @param customerRetainedDTO 条件参数
     * @return 留存数据
     */
    List<CustomerRetainVO> getVisitRetained(CustomerRetainedDTO customerRetainedDTO);

    /**
     * 清除用户访问留存数据缓存
     * @param customerRetainedDTO 条件参数
     */
    void removeCacheVisitRetained(CustomerRetainedDTO customerRetainedDTO);

    /**
     * 清除交易缓存
     * @param customerRetainedDTO 参数
     */
    void removeCacheTradeRetained(CustomerRetainedDTO customerRetainedDTO);
}
