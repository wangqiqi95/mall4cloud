package com.mall4j.cloud.order.service;

import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.order.vo.OrderDistributionExcelVO;
import com.mall4j.cloud.api.order.vo.OrderExcelVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.order.vo.UnDeliveryOrderExcelVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 订单excel
 * @author Pineapple
 * @date 2021/7/20 8:48
 */
public interface OrderExcelService {

    /**
     * 获取订单信息列表（excel导出）
     * @param orderSearchDTO 搜索参数
     * @return 订单信息列表
     */
    void excelOrderList(OrderSearchDTO orderSearchDTO);

    /**
     * 获取待发货的订单信息列表（excel）
     * @param response
     * @param orderSearchDTO 搜索参数
     */
    void excelUnDeliveryOrderList(HttpServletResponse response,OrderSearchDTO orderSearchDTO);

    /**
     * 导入待发货订单
     * @param list
     * @param errorMap
     */
    void exportOrderExcel(List<UnDeliveryOrderExcelVO> list, Map<Integer,List<String>> errorMap);

    /**
     * 待发货订单导入的错误信息
     * @param errorMap
     * @return
     */
    String orderExportError(Map<Integer,List<String>> errorMap);

    /**
     * 获取订单信息列表（excel导出）
     * @param queryDTO
     * @return
     */
    List<OrderDistributionExcelVO> excelDistributionOrderList(DistributionQueryDTO queryDTO);
}
