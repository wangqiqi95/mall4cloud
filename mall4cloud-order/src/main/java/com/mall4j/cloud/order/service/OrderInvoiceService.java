package com.mall4j.cloud.order.service;

import com.mall4j.cloud.common.order.dto.OrderInvoiceDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderInvoice;
import com.mall4j.cloud.order.vo.OrderInvoiceVO;

import java.util.List;

/**
 * @author Pineapple
 * @date 2021/8/2 8:57
 */

public interface OrderInvoiceService {
    /**
     * 分页获取列表
     * @param pageDTO 分页参数
     * @param orderInvoiceDTO 搜索参数
     * @return 列表分页数据
     */
    PageVO<OrderInvoice> page(PageDTO pageDTO, OrderInvoiceDTO orderInvoiceDTO);

    /**
     * 分页获取列表
     * @param pageDTO 分页参数
     * @return 列表分页数据
     */
    PageVO<OrderInvoiceVO> pageUserInvoice(PageDTO pageDTO);

    /**
     * 根据id获取
     *
     * @param orderInvoiceId id
     * @return
     */
    OrderInvoice getByOrderInvoiceId(Long orderInvoiceId);

    /**
     * 保存
     * @param orderInvoice
     */
    void save(OrderInvoice orderInvoice);

    /**
     * 更新
     * @param orderInvoice
     */
    void update(OrderInvoice orderInvoice);

    /**
     * 根据id删除
     * @param orderInvoiceId id
     */
    void deleteById(Long orderInvoiceId);

    /**
     * 根据订单发票id获取，app端使用
     * @param orderInvoiceId 订单发票id
     * @return 信息
     */
    OrderInvoiceVO getById(Long orderInvoiceId);

    /**
     * 根据订单id获取发票id
     * @param orderId 订单id
     * @return
     */
    Long getByOrderId(Long orderId);

    /**
     * 该订单是否已经开具发票
     * @param orderId
     * @return
     */
    boolean isUpload(Long orderId);

    /**
     * 批量删除
     * @param orderIds 被取消的订单id
     */
    void deleteBatch(List<Long> orderIds);
}
