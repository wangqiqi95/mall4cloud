package com.mall4j.cloud.flow.mapper;


import com.mall4j.cloud.api.order.dto.CustomerRetainedDTO;
import com.mall4j.cloud.api.order.vo.CustomerRetainVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 */
@Repository
public interface CustomerAnalysisMapper {

    /**
     * 获取用户留存率, 统计用户第一次访问后，之后的几个月的留存用户数
     * @param customerRetainedDTO 条件查询参数
     * @return 访问留存信息列表
     */
    List<CustomerRetainVO> getVisitRetained(@Param("customerRetainedDTO") CustomerRetainedDTO customerRetainedDTO);


//    List<MemberOverviewListParam> generalizeList(@Param("memberReqParam") MemberReqParam memberReqParam);
}
