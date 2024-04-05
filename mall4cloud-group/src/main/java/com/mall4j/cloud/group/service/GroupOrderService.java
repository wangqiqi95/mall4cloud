package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.group.bo.GroupOrderBO;
import com.mall4j.cloud.group.model.GroupOrder;
import com.mall4j.cloud.group.vo.GroupOrderVO;
import com.mall4j.cloud.group.vo.app.AppGroupUserVO;

import java.util.List;

/**
 * 拼团订单表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public interface GroupOrderService {

    /**
     * 分页获取拼团订单表列表
     * @param pageDTO 分页参数
     * @return 拼团订单表列表分页数据
     */
    PageVO<GroupOrder> page(PageDTO pageDTO);

    /**
     * 根据拼团订单表id获取拼团订单表
     *
     * @param groupOrderId 拼团订单表id
     * @return 拼团订单表
     */
    GroupOrder getByGroupOrderId(Long groupOrderId);

    /**
     * 保存拼团订单表
     * @param groupOrder 拼团订单表
     */
    void save(GroupOrder groupOrder);

    /**
     * 更新拼团订单表
     * @param groupOrder 拼团订单表
     */
    void update(GroupOrder groupOrder);

    /**
     * 根据拼团订单表id删除拼团订单表
     * @param groupOrderId 拼团订单表id
     */
    void deleteById(Long groupOrderId);

    /**
     * 根据
     * @param orderId
     * @return
     */
    GroupOrderVO getByOrderId(Long orderId);

    /**
     * 获取参团的用户列表
     * @param groupTeamId
     * @return
     */
    List<AppGroupUserVO> listApiGroupUserDto(Long groupTeamId);

    /**
     * 获取用户团购订单信息
     * @param groupTeamId
     * @return
     */
    GroupOrderVO getUserGroupOrderByGroupTeamId(Long groupTeamId);

    /**
     * 用户在该活动中有的商品数量
     * @param userId
     * @param groupActivityId
     * @return
     */
    Integer getUserHadSpuCountByGroupActivityId(Long userId, Long groupActivityId);

    /**
     * 团购活动提交订单
     * @param groupOrderBO
     */
    void submit(GroupOrderBO groupOrderBO);

    /**
     * 取消团购订单
     * @param orderIds 团购订单ids
     */
    void cancelGroupOrder(List<Long> orderIds);

    /**
     * 订单支付成功通知
     * @param message
     */
    void payNotifyGroupOrder(PayNotifyBO message);

    /**
     * 拼团失败
     * @param groupTeamId
     */
    void unSuccess(Long groupTeamId);

    /**
     * 根据商品及用户id，获取用户订单数量
     * @param groupActivityId 团购id
     * @param spuId 商品id
     * @param userId 用户id
     * @return
     */
    Integer getOrderBySpuIdAndUserId(Long groupActivityId, Long spuId, Long userId);
}
