package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.api.order.dto.CustomerRetainedDTO;
import com.mall4j.cloud.api.order.vo.CustomerRetainVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单分析
 *
 * @author cl
 * @date 2021-05-25 09:33:50
 */
public interface OrderAnalysisMapper {


	/**
	 * 获取用户的成交留存率, 统计用户第一次成交后，之后的1到6个月的留存用户数
	 * @param customerRetainedDTO 条件查询参数
	 * @return 成交留存信息列表
	 */
    List<CustomerRetainVO> getTradeRetained(@Param("customerRetainedDTO") CustomerRetainedDTO customerRetainedDTO);

}
