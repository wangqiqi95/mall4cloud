package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.common.order.dto.OrderInvoiceDTO;
import com.mall4j.cloud.order.model.OrderInvoice;
import com.mall4j.cloud.order.vo.OrderInvoiceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Pineapple
 * @date 2021/8/2 8:54
 */
public interface OrderInvoiceMapper {
    /**
     * 获取列表
     *
     * @return 列表
     */
    List<OrderInvoice> list();

    /**
     * 根据参数查询店铺的订单发票列表
     *
     * @param orderInvoiceDTO 参数
     * @return 店铺的订单发票列表
     */
    List<OrderInvoice> listByShopId(@Param("orderInvoiceDTO") OrderInvoiceDTO orderInvoiceDTO);

    /**
     * 获取用户发票列表
     *
     * @param userId 用户id
     * @param lang   语言
     * @return 列表
     */
    List<OrderInvoiceVO> listUserInvoice(@Param("userId") Long userId, @Param("lang") Integer lang);

    /**
     * 根据id获取
     *
     * @param orderInvoiceId id
     * @return
     */
    OrderInvoice getByOrderInvoiceId(@Param("orderInvoiceId") Long orderInvoiceId);

    /**
     * 保存
     *
     * @param orderInvoice
     */
    void save(@Param("orderInvoice") OrderInvoice orderInvoice);

    /**
     * 更新
     *
     * @param orderInvoice
     */
    void update(@Param("orderInvoice") OrderInvoice orderInvoice);

    /**
     * 根据id删除
     *
     * @param orderInvoiceId
     */
    void deleteById(@Param("orderInvoiceId") Long orderInvoiceId);

    /**
     * 根据订单发票id获取，app端使用
     *
     * @param orderInvoiceId 订单发票id
     * @param lang 语言
     * @return 信息
     */
    OrderInvoiceVO getById(@Param("orderInvoiceId") Long orderInvoiceId, @Param("lang") Integer lang);

    /**
     * 该订单是否已经开具发票
     * @param orderId
     * @return
     */
    int isUpload(@Param("orderId") Long orderId);

    /**
     * 根据订单id获取发票id
     * @param orderId 订单id
     * @return
     */
    Long getByOrderId(@Param("orderId")Long orderId);

    /**
     * 批量删除
     * @param orderIds 被取消的订单id
     */
    void deleteBatch(@Param("orderIds")List<Long> orderIds);
}
