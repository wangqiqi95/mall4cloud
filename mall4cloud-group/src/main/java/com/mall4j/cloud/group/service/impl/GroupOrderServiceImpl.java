package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.order.bo.SendNotifyBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.GroupCacheNames;
import com.mall4j.cloud.common.cache.util.CacheManagerUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.OrderIdWithRefundIdBO;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.bo.GroupOrderBO;
import com.mall4j.cloud.group.constant.GroupOrderStatusEnum;
import com.mall4j.cloud.group.constant.TeamStatusEnum;
import com.mall4j.cloud.group.mapper.GroupActivityMapper;
import com.mall4j.cloud.group.mapper.GroupOrderMapper;
import com.mall4j.cloud.group.mapper.GroupTeamMapper;
import com.mall4j.cloud.group.model.GroupOrder;
import com.mall4j.cloud.group.model.GroupTeam;
import com.mall4j.cloud.group.service.GroupOrderService;
import com.mall4j.cloud.group.vo.GroupActivityVO;
import com.mall4j.cloud.group.vo.GroupOrderVO;
import com.mall4j.cloud.group.vo.GroupTeamVO;
import com.mall4j.cloud.group.vo.app.AppGroupUserVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 拼团订单表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
@Service
public class GroupOrderServiceImpl implements GroupOrderService {

    @Autowired
    private GroupOrderMapper groupOrderMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private GroupTeamMapper groupTeamMapper;

    @Autowired
    private GroupActivityMapper groupActivityMapper;

    @Autowired
    private CacheManagerUtil cacheManagerUtil;

    @Autowired
    private OnsMQTemplate groupOrderSuccessTemplate;
    @Autowired
    private OnsMQTemplate groupOrderUnSuccessTemplate;
    @Autowired
    private OnsMQTemplate sendGroupNotifyToUserTemplate;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OnsMQTransactionTemplate groupOrderUnSuccessRefundTemplate;
    @Autowired
    private SegmentFeignClient segmentFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    public PageVO<GroupOrder> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> groupOrderMapper.list());
    }

    @Override
    public GroupOrder getByGroupOrderId(Long groupOrderId) {
        return groupOrderMapper.getByGroupOrderId(groupOrderId);
    }

    @Override
    public void save(GroupOrder groupOrder) {
        groupOrderMapper.save(groupOrder);
    }

    @Override
    public void update(GroupOrder groupOrder) {
        groupOrderMapper.update(groupOrder);
    }

    @Override
    public void deleteById(Long groupOrderId) {
        groupOrderMapper.deleteById(groupOrderId);
    }

    @Override
    public GroupOrderVO getByOrderId(Long orderId) {
        return groupOrderMapper.getByOrderId(orderId);
    }

    @Override
    public List<AppGroupUserVO> listApiGroupUserDto(Long groupTeamId) {
        List<AppGroupUserVO> appGroupUserList = groupOrderMapper.listApiGroupUserDto(groupTeamId);
        if (CollUtil.isEmpty(appGroupUserList)) {
            return null;
        }
        Set<Long> userIds = appGroupUserList.stream().map(AppGroupUserVO::getUserId).collect(Collectors.toSet());
        ServerResponseEntity<List<UserApiVO>> userResponse = userFeignClient.getUserByUserIds(new ArrayList<>(userIds));
        Map<Long, UserApiVO> userMap = userResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, u -> u));
        for (AppGroupUserVO groupUserVO : appGroupUserList) {
            // 机器人参团
            if (Objects.isNull(groupUserVO.getUserId()) || Objects.equals(groupUserVO.getUserId(), 0L)) {
                groupUserVO.setNickName(Constant.GROUP_SYSTEM_USER);
            }else {
                UserApiVO userApiVO = userMap.get(groupUserVO.getUserId());
                groupUserVO.setNickName(userApiVO.getNickName());
                groupUserVO.setPic(userApiVO.getPic());
            }
        }
        return appGroupUserList;
    }

    @Override
    public GroupOrderVO getUserGroupOrderByGroupTeamId(Long groupTeamId) {
        return groupOrderMapper.getUserGroupOrderByGroupTeamId(groupTeamId, AuthUserContext.get().getUserId());
    }

    @Override
    public Integer getUserHadSpuCountByGroupActivityId(Long userId, Long groupActivityId) {
        List<Long> orderIds = groupOrderMapper.getUserHadSpuCountByGroupActivityId(userId, groupActivityId);
        ServerResponseEntity<Integer> orderResponse = orderFeignClient.countNormalOrderByOrderIds(orderIds);
        if (!orderResponse.isSuccess()) {
            throw new LuckException(orderResponse.getMsg());
        }
        return orderResponse.getData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(GroupOrderBO groupOrderBO) {
        // 插入拼团订单
        GroupOrder groupOrder = new GroupOrder();
        // 如果为团长，则需新建拼团团队
        if (Objects.equals(groupOrderBO.getGroupTeamId(), 0L)) {
            GroupTeam groupTeam = new GroupTeam();
            groupTeam.setShareUserId(groupOrderBO.getShareUserId());
            groupTeam.setGroupSpuId(groupOrderBO.getSpuId());
            groupTeam.setGroupActivityId(groupOrderBO.getGroupActivityId());
            groupTeam.setJoinNum(1);
            groupTeam.setShopId(groupOrderBO.getShopId());
            groupTeam.setStatus(TeamStatusEnum.WAITING_GROUP.value());
            groupTeam.setTotalPrice(groupOrder.getPayPrice());
            groupTeamMapper.save(groupTeam);
            groupOrder.setGroupTeamId(groupTeam.getGroupTeamId());
        } else {
            groupOrder.setGroupTeamId(groupOrderBO.getGroupTeamId());
        }

        groupOrder.setGroupActivityId(groupOrderBO.getGroupActivityId());
        groupOrder.setUserId(groupOrderBO.getUserId());
        groupOrder.setOrderId(groupOrderBO.getOrderId());
        groupOrder.setActivityProdPrice(groupOrderBO.getActivityProdPrice());
        groupOrder.setPayPrice(groupOrderBO.getPayPrice());
        groupOrder.setShopId(groupOrderBO.getShopId());
        groupOrder.setIdentityType(Objects.equals(groupOrderBO.getGroupTeamId(), 0L) ? 1 : 0);
        groupOrder.setStatus(GroupOrderStatusEnum.WAITING_PAY.value());
        groupOrder.setCount(groupOrderBO.getCount());
        groupOrderMapper.save(groupOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelGroupOrder(List<Long> orderIds) {
        Long orderId = orderIds.get(0);
        GroupOrderVO groupOrder = groupOrderMapper.getByOrderId(orderId);
        if (!Objects.equals(groupOrder.getStatus(), GroupOrderStatusEnum.WAITING_PAY.value())) {
            return;
        }
        groupOrderMapper.cancelGroupOrder(orderIds);
        groupTeamMapper.cancelGroupTeam(groupOrder.getGroupTeamId());
        // 取消团购订单算取消拼团？
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payNotifyGroupOrder(PayNotifyBO message) {
        Long orderId = message.getOrderIds().get(0);
        // 获取用户拼团订单信息
        GroupOrderVO groupOrder = groupOrderMapper.getByOrderId(orderId);
        if (Objects.equals(groupOrder.getStatus(), GroupOrderStatusEnum.SUCCESS.value())) {
            return;
        }
        // 如果为团长则直接开团，否则判断是否拼团已满
        if (Objects.equals(groupOrder.getIdentityType(), 1)) {
            // 开团
            openGroup(orderId, groupOrder);
        } else {
            // 获取拼团团队订单
            GroupTeamVO groupTeam = groupTeamMapper.getByGroupTeamId(groupOrder.getGroupTeamId());
            // 如果团已满，或回调的时候团已经失败了（之前的团退款了）则开新团
            // 否则加入该团
            if (Objects.equals(groupTeam.getStatus(), TeamStatusEnum.SUCCESS.value())
                    || Objects.equals(groupTeam.getStatus(), TeamStatusEnum.FAIL.value())) {
                // 开新团
                openNewGroup(orderId, groupOrder, groupTeam);
            } else if (Objects.equals(groupTeam.getStatus(), TeamStatusEnum.IN_GROUP.value())) {
                // 参团
                joinGroup(orderId, groupOrder, groupTeam);
            }
        }
    }

    @Override
    public void unSuccess(Long groupTeamId) {
        Date nowDate = new Date();
        // 机器人列表
        List<GroupOrder> robot = new ArrayList<>();
        GroupTeamVO groupTeam = groupTeamMapper.getByGroupTeamId(groupTeamId);
        if (!Objects.equals(groupTeam.getStatus(),TeamStatusEnum.IN_GROUP.value())) {
            return;
        }
        GroupActivityVO groupActivity = groupActivityMapper.getByGroupActivityId(groupTeam.getGroupActivityId());

        List<Long> orderIds = groupOrderMapper.listSuccessOrderIdByTeamId(groupTeam.getGroupTeamId());
        if (CollectionUtil.isEmpty(orderIds)) {
            return;
        }

        // 如果未成团，有机器人可以成团，不需要进行退款的操作
        if (groupActivity.getHasRobot() == 1) {
            addRobot(nowDate, robot, groupTeam, groupActivity, orderIds);
        }

        List<OrderIdWithRefundIdBO> orderIdWithRefundIds = new ArrayList<>();
        for (Long orderId : orderIds) {
            ServerResponseEntity<Long> segmentId = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);

            OrderIdWithRefundIdBO orderIdWithRefundIdBO = new OrderIdWithRefundIdBO();
            orderIdWithRefundIdBO.setOrderId(orderId);
            orderIdWithRefundIdBO.setRefundId(segmentId.getData());
            orderIdWithRefundIdBO.setRefundNumber(getRefundNumber());
            orderIdWithRefundIds.add(orderIdWithRefundIdBO);
        }


        // 发送退款服务进行退款的通知

        SendResult sendResult = groupOrderUnSuccessRefundTemplate.sendMessageInTransaction(orderIdWithRefundIds, groupTeamId);

        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        // 消息推送-拼团失败
        for (Long orderId : orderIds) {
            SendNotifyBO sendNotifyBO = new SendNotifyBO();
            sendNotifyBO.setSendType(SendTypeEnum.GROUP_FAIL.getValue());
            sendNotifyBO.setBizId(orderId);
            sendGroupNotifyToUserTemplate.syncSend(sendNotifyBO);
        }

    }

    private String getRefundNumber() {
        String refundNumber = "TD";
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //打乱顺序
//        time=cn.hutool.core.util.RandomUtil.randomString(time,14);
        refundNumber = refundNumber + time + cn.hutool.core.util.RandomUtil.randomNumbers(6);
        return refundNumber;
    }

    /**
     * 如果未成团，有机器人可以成团，则添加机器人
     * @param nowDate 当前时间
     * @param robot 机器人列表
     * @param groupTeam 拼团队伍
     * @param groupActivity 团购活动
     * @param orderIds 订单id列表
     */
    private void addRobot(Date nowDate, List<GroupOrder> robot, GroupTeamVO groupTeam, GroupActivityVO groupActivity, List<Long> orderIds) {

        // 模拟参团操作（添加机器人）
        for (int i = 0; i < groupActivity.getGroupNumber() - groupTeam.getJoinNum(); i++) {
            GroupOrder groupOrder = new GroupOrder();
            groupOrder.setShopId(groupTeam.getShopId());
            groupOrder.setGroupTeamId(groupTeam.getGroupTeamId());
            groupOrder.setUserId(0L);
            groupOrder.setIdentityType(0);
            groupOrder.setStatus(GroupOrderStatusEnum.SUCCESS.value());
            groupOrder.setCreateTime(nowDate);
            robot.add(groupOrder);
        }
        if (CollectionUtil.isNotEmpty(robot)) {
            groupOrderMapper.saveBatch(robot);
        }

        int updateStatus = groupTeamMapper.updateToSuccess(mapperFacade.map(groupTeam, GroupTeam.class));
        if (updateStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        // 更新已成团人数
        groupActivityMapper.updateGroupOrderInfo(groupActivity.getGroupActivityId());

        // 发送通知给订单，将订单状态变为待发货
        SendResult sendResult = groupOrderSuccessTemplate.syncSend(orderIds);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，发的出去无所谓
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        for (Long orderId : orderIds) {
            // 消息推送-拼团成功
            SendNotifyBO sendNotifyBO = new SendNotifyBO();
            sendNotifyBO.setSendType(SendTypeEnum.GROUP_SUCCESS.getValue());
            sendNotifyBO.setBizId(orderId);
            sendGroupNotifyToUserTemplate.syncSend(orderId);
        }
    }

    @Override
    public Integer getOrderBySpuIdAndUserId(Long groupActivityId, Long spuId, Long userId) {
        return groupOrderMapper.getOrderBySpuIdAndUserId(groupActivityId, spuId, userId);
    }


    private void openGroup(Long orderId, GroupOrderVO groupOrder) {
        Date now = new Date();
        GroupTeam newGroupTeam = new GroupTeam();
        newGroupTeam.setTotalPrice(groupOrder.getPayPrice());
        newGroupTeam.setGroupTeamId(groupOrder.getGroupTeamId());
        newGroupTeam.setStatus(TeamStatusEnum.IN_GROUP.value());
        newGroupTeam.setStartTime(now);
        newGroupTeam.setEndTime(DateUtil.offsetMillisecond(now, RocketMqConstant.CANCEL_TIME_INTERVAL));
        groupTeamMapper.update(newGroupTeam);
        int updateStatus = groupOrderMapper.updateToPaySuccess(orderId, null, null);
        if (updateStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        groupActivityMapper.updateGroupOrderInfo(groupOrder.getGroupActivityId());
        // 发送延迟消息
        // 发送消息，如果三十分钟后没有成团，则取消订单进行退款
        SendResult sendResult = groupOrderUnSuccessTemplate.syncSend(groupOrder.getGroupTeamId(), RocketMqConstant.CANCEL_ORDER_DELAY_LEVEL);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，发的出去无所谓
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        // 消息推送-开团成功
        SendNotifyBO sendNotifyBO = new SendNotifyBO();
        sendNotifyBO.setSendType(SendTypeEnum.GROUP_START.getValue());
        sendNotifyBO.setBizId(orderId);
        sendGroupNotifyToUserTemplate.syncSend(sendNotifyBO);

//        notifyTemplateService.sendNotifyOfGroupStart(order,SendType.GROUP_START);
    }

    private void openNewGroup(Long orderId, GroupOrderVO groupOrder, GroupTeamVO groupTeam) {
        Date now = new Date();
        // 满员开团
        GroupTeam newGroupTeam = new GroupTeam();
        newGroupTeam.setJoinNum(1);
        newGroupTeam.setCreateTime(now);
        newGroupTeam.setUpdateTime(now);
        newGroupTeam.setStartTime(now);
        newGroupTeam.setShopId(groupTeam.getShopId());
        newGroupTeam.setTotalPrice(groupOrder.getPayPrice());
        newGroupTeam.setShareUserId(groupOrder.getUserId());
        newGroupTeam.setStatus(TeamStatusEnum.IN_GROUP.value());
        newGroupTeam.setGroupSpuId(groupTeam.getGroupSpuId());
        newGroupTeam.setGroupActivityId(groupTeam.getGroupActivityId());
        newGroupTeam.setEndTime(DateUtil.offsetMillisecond(now, RocketMqConstant.CANCEL_TIME_INTERVAL));
        groupTeamMapper.save(newGroupTeam);

        int updateStatus = groupOrderMapper.updateToPaySuccess(orderId,1, newGroupTeam.getGroupTeamId());
        if (updateStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        groupActivityMapper.updateGroupOrderInfo(groupOrder.getGroupActivityId());

        // 发送延迟消息
        // 发送消息，如果三十分钟后没有成团，则取消订单进行退款
        SendResult sendResult = groupOrderUnSuccessTemplate.syncSend(groupOrder.getGroupTeamId(), RocketMqConstant.CANCEL_ORDER_DELAY_LEVEL);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，发的出去无所谓
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        // 消息推送-开团成功
        SendNotifyBO sendNotifyBO = new SendNotifyBO();
        sendNotifyBO.setSendType(SendTypeEnum.GROUP_START.getValue());
        sendNotifyBO.setBizId(orderId);
        sendGroupNotifyToUserTemplate.syncSend(sendNotifyBO);

//        notifyTemplateService.sendNotifyOfGroupStart(order, SendType.GROUP_START);
    }

    private void joinGroup(Long orderId, GroupOrderVO groupOrder, GroupTeamVO groupTeam) {
        // 拼团活动
        GroupActivityVO groupActivity = groupActivityMapper.getByGroupActivityId(groupTeam.getGroupActivityId());
        GroupTeam newGroupTeam = new GroupTeam();
        newGroupTeam.setGroupTeamId(groupOrder.getGroupTeamId());
        // 参团
        newGroupTeam.setJoinNum(groupTeam.getJoinNum() + 1);
        newGroupTeam.setTotalPrice(groupTeam.getTotalPrice() + groupOrder.getPayPrice());

        int updateStatus = groupOrderMapper.updateToPaySuccess(orderId,null, null);
        if (updateStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        // 参团人数未满，更新人数，金额
        if (newGroupTeam.getJoinNum() < groupActivity.getGroupNumber()) {
            groupTeamMapper.update(newGroupTeam);
            return;
        }

        int updateTeamStatus = groupTeamMapper.updateToSuccess(newGroupTeam);


        if (updateTeamStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        groupActivityMapper.updateGroupOrderInfo(groupActivity.getGroupActivityId());
        // 清除缓存-更新了已成团订单数及人数
//        groupActivityService.removeCache(groupActivity.getSpuId());
        cacheManagerUtil.evictCache(GroupCacheNames.GROUP_BY_SPU_KEY, groupActivity.toString());
        // 发送通知给订单，将所有同一个团并且已支付订单的状态变为待发货
        if (newGroupTeam.getJoinNum() >= groupActivity.getGroupNumber()) {
            List<Long> orderIds = groupOrderMapper.listSuccessOrderIdByTeamId(groupTeam.getGroupTeamId());
            SendResult sendResult = groupOrderSuccessTemplate.syncSend(orderIds);
            if (sendResult == null || sendResult.getMessageId() == null) {
                // 消息发不出去就抛异常，发的出去无所谓
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            for (Long id : orderIds) {
                // 消息推送-拼团成功
                SendNotifyBO sendNotifyBO = new SendNotifyBO();
                sendNotifyBO.setSendType(SendTypeEnum.GROUP_SUCCESS.getValue());
                sendNotifyBO.setBizId(id);
                sendGroupNotifyToUserTemplate.syncSend(sendNotifyBO);
            }
        }
    }

}
