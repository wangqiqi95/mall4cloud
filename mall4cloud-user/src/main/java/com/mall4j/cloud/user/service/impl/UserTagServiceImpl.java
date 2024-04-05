package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.biz.feign.NotifyFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.user.constant.RecentDaysTypeEnum;
import com.mall4j.cloud.user.constant.UserStatusEnum;
import com.mall4j.cloud.user.dto.UserTagDTO;
import com.mall4j.cloud.user.mapper.UserBalanceLogMapper;
import com.mall4j.cloud.user.mapper.UserLevelLogMapper;
import com.mall4j.cloud.user.model.UserTag;
import com.mall4j.cloud.user.mapper.UserTagMapper;
import com.mall4j.cloud.user.model.UserTagUser;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserTagService;
import com.mall4j.cloud.user.service.UserTagUserService;
import com.mall4j.cloud.user.vo.UserTagVO;
import com.mall4j.cloud.user.vo.UserVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 客户标签
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Service
public class UserTagServiceImpl implements UserTagService {

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired
    private UserTagUserService userTagUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLevelLogMapper userLevelLogMapper;

    @Autowired
    private UserBalanceLogMapper userBalanceLogMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private NotifyFeignClient notifyFeignClient;

    @Override
    public PageVO<UserTagVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userTagMapper.list(new UserTag()));
    }

    @Override
    public UserTag getByUserTagId(Long userTagId) {
        return userTagMapper.getByUserTagId(userTagId);
    }

    @Override
    public Long save(UserTag userTag) {
        return userTagMapper.save(userTag);
    }

    @Override
    public void update(UserTag userTag) {
        userTagMapper.update(userTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long userTagId) {
        userTagMapper.deleteById(userTagId);
        // 删除用户与标签关联关系
        userTagUserService.deleteByTagId(userTagId);
        // 删除推送消息模板与标签关联关系
        ServerResponseEntity<Void> notifyDeleteRes = notifyFeignClient.deleteTagByTagId(userTagId);
        if (notifyDeleteRes.isFail()) {
            throw new LuckException(notifyDeleteRes.getMsg());
        }
    }

    @Override
    public List<UserTagVO> list() {
        return userTagMapper.list(new UserTag());
    }

    @Override
    public UserTagVO refreshConditionTag(Long userTagId) {
        // 检查标签类型
        UserTag userTag = getByUserTagId(userTagId);
        if (Objects.isNull(userTag)) {
            // 用户标签不存在
            throw new LuckException("用户标签不存在");
        }
        if (userTag.getTagType() != 1) {
            // 标签类型不对劲
            throw new LuckException("标签类型不对劲");
        }
        if (userTag.getStatisticUpdateTime() != null && DateUtil.date().compareTo(DateUtil.offsetMinute(userTag.getStatisticUpdateTime(), 1)) < 0) {
            // 刷新间隔不能小于一分钟
//            throw new LuckException("刷新间隔不能小于一分钟");
        }
        // 筛选条件
        List<List<UserVO>> listUserList = new ArrayList<>();
        // 基础条件
        // 成为客户时间
        if (Objects.nonNull(userTag.getRegisterMinTime()) && Objects.nonNull(userTag.getRegisterMaxTime())) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
            calendar.setTime(userTag.getRegisterMinTime());
            Date startDate = Date.from(calendar.toInstant());
            calendar.setTime(userTag.getRegisterMaxTime());
            Date endDate = Date.from(calendar.toInstant());
            List<UserVO> users = userService.getUserByCreateTimeRange(UserStatusEnum.ENABLE.value(), startDate, endDate);
            listUserList.add(users);
        }
        // 关注时间
        if (Objects.nonNull(userTag.getSubscribeMinTime()) && Objects.nonNull(userTag.getSubscribeMaxTime())) {
        }
        // 成为会员时间
        if (Objects.nonNull(userTag.getToBeMemberMinTime()) && Objects.nonNull(userTag.getToBeMemberMaxTime())) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
            calendar.setTime(userTag.getToBeMemberMinTime());
            Date startDate = Date.from(calendar.toInstant());
            calendar.setTime(userTag.getToBeMemberMaxTime());
            Date endDate = Date.from(calendar.toInstant());
            List<Long> userIds = userLevelLogMapper.listUserIdByEarliestRechargeTime(1, startDate, endDate);
            List<UserVO> users = getInitUserVOList(userIds);
            listUserList.add(users);
        }
        // 交易条件-最近消费时间、消费次数、消费金额
        buyTransactionConditionTag(userTag, listUserList);
        // 订单均价、充值金额、充值次数
        amountTransactionConditionTag(userTag, listUserList);

        // 抽取用户子集
        List<UserVO> users;
        if (listUserList.isEmpty()) {
            users = new ArrayList<>();
        } else {
            users = listUserList.get(0);
            for (int i = 1; i < listUserList.size(); i++) {
                users = userFilter(users, listUserList.get(i));
            }
        }
        // 清空关联表
        userTagUserService.removeByUserTagId(userTagId);
        // 写入关联表
        List<UserTagUser> userTagUsers = new ArrayList<>();
        for (UserVO user : users) {
            UserTagUser userTagUser = new UserTagUser();
            userTagUser.setUserId(user.getUserId());
            userTagUser.setUserTagId(userTagId);
            userTagUsers.add(userTagUser);
        }
        if (CollUtil.isNotEmpty(userTagUsers)) {
            userTagUserService.saveBatch(userTagUsers);
        }
        // 更新统计人数，时间
        userTag.setUserNum((long) userTagUsers.size());
        userTag.setStatisticUpdateTime(DateUtil.date());
        update(userTag);
        return mapperFacade.map(userTag, UserTagVO.class);
    }

    @Override
    public int count(UserTag userTag) {
        return userTagMapper.count(userTag);
    }

    @Override
    public boolean addUserTag(UserTagDTO userTagDTO) {
        UserTag userTag = new UserTag();
        userTag.setTagName(userTagDTO.getTagName());
        userTag.setTagType(userTagDTO.getTagType());
        userTag.setIsSysTurnOn(0);
        switch (userTagDTO.getTagType()) {
            // 手动标签
            case 0:
                //todo
                break;
            // 条件标签
            case 1:
                fillInCustomOptionalCondition(userTagDTO, userTag,false);
                break;
            default:
        }
        userTag.setUserNum(0L);
        this.save(userTag);
        return true;
    }

    @Override
    public boolean updateUserTag(UserTagDTO userTagDTO) {
        UserTag userTag = getByUserTagId(userTagDTO.getUserTagId());
        if (Objects.isNull(userTag)) {
            // 用户标签不存在
            throw new LuckException("用户标签不存在");
        }
        userTag.setTagName(userTagDTO.getTagName());
        userTag.setUpdateTime(DateUtil.date());
        switch (userTag.getTagType()) {
            // 手动标签
            case 0:
                //todo
                break;
            // 条件标签
            case 1:
                fillInCustomOptionalCondition(userTagDTO, userTag, true);
                break;
            default:
        }
        update(userTag);
        return false;
    }

    @Override
    public PageVO<UserTagVO> getPage(PageDTO pageDTO, UserTagDTO userTagDTO) {
        return PageUtil.doPage(pageDTO, () -> userTagMapper.list(mapperFacade.map(userTagDTO, UserTag.class)));
    }


    @Override
    public List<UserTagApiVO> getUserTagList(List<Long> tagIds) {
        return userTagMapper.getByUserTagIds(tagIds);
    }

    @Override
    public List<UserTagApiVO> getStaffUserTagList(String tagName, List<Long> tagIds) {
        return userTagMapper.getStaffUserTagList(tagName, tagIds);
    }

    @Override
    public List<UserTagApiVO> listUserTagByType(Integer tagType) {
        return userTagMapper.listUserTagByType(tagType);
    }

    /**
     * 填写自定义可选条件
     * @param userTagDTO 参数
     * @param userTag 用户标签
     * @param isUpdate true 是修改 false 新增
     */
    private void fillInCustomOptionalCondition(UserTagDTO userTagDTO, UserTag userTag, boolean isUpdate) {
        // 基础条件
        baseOptionalCondition(userTagDTO, userTag, isUpdate);
        // 交易条件
        transactionOptionalCondition(userTagDTO, userTag, isUpdate);
    }

    /**
     * 基础条件
     * @param userTagDTO  标签信息
     * @param userTag 客户标签
     * @param isUpdate true 是修改 false 新增
     */
    private void baseOptionalCondition(UserTagDTO userTagDTO, UserTag userTag, boolean isUpdate) {
        // 成为客户时间
        if (Objects.nonNull(userTagDTO.getRegisterMinTime()) && Objects.nonNull(userTagDTO.getRegisterMaxTime())) {
            if (userTagDTO.getRegisterMinTime().after(userTagDTO.getRegisterMaxTime())) {
                // 开始时间不能比结束时间晚
                throw new LuckException("开始时间不能比结束时间晚");
            }
            userTag.setRegisterMinTime(userTagDTO.getRegisterMinTime());
            userTag.setRegisterMaxTime(userTagDTO.getRegisterMaxTime());
        }
        if (isUpdate && Objects.nonNull(userTagDTO.getClearRegisterTime()) && userTagDTO.getClearRegisterTime()) {
            userTag.setRegisterMinTime(null);
            userTag.setRegisterMaxTime(null);
        }
        // 关注时间
        if (Objects.nonNull(userTagDTO.getSubscribeMinTime()) && Objects.nonNull(userTagDTO.getSubscribeMaxTime())) {
            if (userTagDTO.getSubscribeMinTime().after(userTagDTO.getSubscribeMaxTime())) {
                // 开始时间不能比结束时间晚
                throw new LuckException("开始时间不能比结束时间晚");
            }
            userTag.setSubscribeMinTime(userTagDTO.getSubscribeMinTime());
            userTag.setSubscribeMaxTime(userTagDTO.getSubscribeMaxTime());
        }
        if (isUpdate && Objects.nonNull(userTagDTO.getClearSubscribeTime()) && userTagDTO.getClearSubscribeTime()) {
            userTag.setSubscribeMinTime(null);
            userTag.setSubscribeMaxTime(null);
        }
        // 成为会员时间
        if (Objects.nonNull(userTagDTO.getToBeMemberMinTime()) && Objects.nonNull(userTagDTO.getToBeMemberMaxTime())) {
            if (userTagDTO.getToBeMemberMinTime().after(userTagDTO.getToBeMemberMaxTime())) {
                // 开始时间不能比结束时间晚
                throw new LuckException("开始时间不能比结束时间晚");
            }
            userTag.setToBeMemberMinTime(userTagDTO.getToBeMemberMinTime());
            userTag.setToBeMemberMaxTime(userTagDTO.getToBeMemberMaxTime());
        }
        if (isUpdate && Objects.nonNull(userTagDTO.getClearToBeMemberTime()) && userTagDTO.getClearToBeMemberTime()) {
            userTag.setToBeMemberMinTime(null);
            userTag.setToBeMemberMaxTime(null);
        }
    }
    /**
     * 交易条件
     * @param userTagDTO  标签信息
     * @param userTag 客户标签
     * @param isUpdate true 是修改 false 新增
     */
    private void transactionOptionalCondition(UserTagDTO userTagDTO, UserTag userTag, boolean isUpdate) {
        // 最近消费时间
        if (Objects.nonNull(userTagDTO.getRecentPurchaseTime())) {
            userTag.setRecentPurchaseTime(userTagDTO.getRecentPurchaseTime());
        } else {
            userTag.setRecentPurchaseTime(null);
        }
        if (Objects.nonNull(userTagDTO.getClearRecentPurchaseTime()) && userTagDTO.getClearRecentPurchaseTime()) {
            userTag.setRecentPurchaseTime(null);
        }
        // 消费次数
        if (Objects.nonNull(userTagDTO.getPurchaseNumTime()) && Objects.nonNull(userTagDTO.getPurchaseNumMinNum()) && Objects.nonNull(userTagDTO.getPurchaseNumMaxNum())) {
            if (userTagDTO.getPurchaseNumMinNum() > userTagDTO.getPurchaseNumMaxNum()) {
                // 最小次数不能比最大次数大
                throw new LuckException("最小次数不能比最大次数大");
            }
            userTag.setPurchaseNumTime(userTagDTO.getPurchaseNumTime());
            userTag.setPurchaseNumMinNum(userTagDTO.getPurchaseNumMinNum());
            userTag.setPurchaseNumMaxNum(userTagDTO.getPurchaseNumMaxNum());
        }
        if (isUpdate && Objects.nonNull(userTagDTO.getClearPurchaseNum()) && userTagDTO.getClearPurchaseNum()) {
            userTag.setPurchaseNumTime(null);
            userTag.setPurchaseNumMinNum(null);
            userTag.setPurchaseNumMaxNum(null);
        }
        // 消费金额
        if (Objects.nonNull(userTagDTO.getPurchaseAmountTime()) && Objects.nonNull(userTagDTO.getPurchaseAmountMinAmount()) && Objects.nonNull(userTagDTO.getPurchaseAmountMaxAmount())) {
            if (userTagDTO.getPurchaseAmountMinAmount().compareTo(userTagDTO.getPurchaseAmountMaxAmount()) > 0) {
                // 最小金额不能比最大金额大
                throw new LuckException("最小金额不能比最大金额大");
            }
            userTag.setPurchaseAmountTime(userTagDTO.getPurchaseAmountTime());
            // 元转分
            userTag.setPurchaseAmountMinAmount(PriceUtil.toLongCent(userTagDTO.getPurchaseAmountMinAmount()));
            userTag.setPurchaseAmountMaxAmount(PriceUtil.toLongCent(userTagDTO.getPurchaseAmountMaxAmount()));
        }
        if (isUpdate && Objects.nonNull(userTagDTO.getClearPurchaseAmount()) && userTagDTO.getClearPurchaseAmount()) {
            userTag.setPurchaseAmountTime(null);
            userTag.setPurchaseAmountMinAmount(null);
            userTag.setPurchaseAmountMaxAmount(null);
        }
        // 订单均价
        if (Objects.nonNull(userTagDTO.getOrderAveragePriceTime()) && Objects.nonNull(userTagDTO.getOrderAveragePriceMinAmount()) && Objects.nonNull(userTagDTO.getOrderAveragePriceMaxAmount())) {
            if (userTagDTO.getOrderAveragePriceMinAmount().compareTo(userTagDTO.getOrderAveragePriceMaxAmount()) > 0) {
                // 最小金额不能比最大金额大
                throw new LuckException("最小金额不能比最大金额大");
            }
            userTag.setOrderAveragePriceTime(userTagDTO.getOrderAveragePriceTime());
            // 元转分
            userTag.setOrderAveragePriceMinAmount(PriceUtil.toLongCent(userTagDTO.getOrderAveragePriceMinAmount()));
            userTag.setOrderAveragePriceMaxAmount(PriceUtil.toLongCent(userTagDTO.getOrderAveragePriceMaxAmount()));
        }
        if (isUpdate && Objects.nonNull(userTagDTO.getClearOrderAveragePrice()) && userTagDTO.getClearOrderAveragePrice()) {
            userTag.setOrderAveragePriceTime(null);
            userTag.setOrderAveragePriceMinAmount(null);
            userTag.setOrderAveragePriceMaxAmount(null);
        }
        // 充值金额
        if (Objects.nonNull(userTagDTO.getRechargeAmountTime()) && Objects.nonNull(userTagDTO.getRechargeAmountMinAmount()) && Objects.nonNull(userTagDTO.getRechargeAmountMaxAmount())) {
            if (userTagDTO.getRechargeAmountMinAmount().compareTo(userTagDTO.getRechargeAmountMaxAmount()) > 0) {
                // 最小金额不能比最大金额大
                throw new LuckException("最小金额不能比最大金额大");
            }
            userTag.setRechargeAmountTime(userTagDTO.getRechargeAmountTime());
            userTag.setRechargeAmountMinAmount(userTagDTO.getRechargeAmountMinAmount());
            userTag.setRechargeAmountMaxAmount(userTagDTO.getRechargeAmountMaxAmount());
        }
        if (isUpdate && Objects.nonNull(userTagDTO.getClearRechargeAmount()) && userTagDTO.getClearRechargeAmount()) {
            userTag.setRechargeAmountTime(null);
            userTag.setRechargeAmountMinAmount(null);
            userTag.setRechargeAmountMaxAmount(null);
        }
        // 充值次数
        if (Objects.nonNull(userTagDTO.getRechargeNumTime()) && Objects.nonNull(userTagDTO.getRechargeNumMinNum()) && Objects.nonNull(userTagDTO.getRechargeNumMaxNum())) {
            if (userTagDTO.getRechargeNumMinNum() > userTagDTO.getRechargeNumMaxNum()) {
                // 最小次数不能比最大次数大
                throw new LuckException("最小次数不能比最大次数大");
            }
            userTag.setRechargeNumTime(userTagDTO.getRechargeNumTime());
            userTag.setRechargeNumMinNum(userTagDTO.getRechargeNumMinNum());
            userTag.setRechargeNumMaxNum(userTagDTO.getRechargeNumMaxNum());
        }
        if (isUpdate && Objects.nonNull(userTagDTO.getClearRechargeNum()) && userTagDTO.getClearRechargeNum()) {
            userTag.setRechargeNumTime(null);
            userTag.setRechargeNumMinNum(null);
            userTag.setRechargeNumMaxNum(null);
        }
    }

    /**
     * 刷新条件标签统计人数 -交易条件（购买相关）
     * @param userTag 客户标签
     * @param listUserList 筛选条件
     */
    private void buyTransactionConditionTag(UserTag userTag, List<List<UserVO>> listUserList) {
        // 最近消费时间
        if (Objects.nonNull(userTag.getRecentPurchaseTime())) {
            OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
            orderSearchDTO.setIsPayed(1);
            orderSearchDTO.setDeleteStatus(0);
            // 当0时不限时间
            if (userTag.getRecentPurchaseTime() != 0) {
                LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                Date startDate = addDateTimeCondition(userTag.getRecentPurchaseTime(), localDateTime);
                orderSearchDTO.setStartTime(startDate);
                orderSearchDTO.setEndTime(DateUtil.endOfDay(new Date()));
            }
        }
    }

    /**
     * 刷新条件标签统计人数 -交易条件（金额相关）
     * @param userTag 客户标签
     * @param listUserList 筛选条件
     */
    private void amountTransactionConditionTag(UserTag userTag, List<List<UserVO>> listUserList) {
        // 订单均价
        if (Objects.nonNull(userTag.getOrderAveragePriceTime()) && Objects.nonNull(userTag.getOrderAveragePriceMinAmount()) && Objects.nonNull(userTag.getOrderAveragePriceMaxAmount())) {
        }
        // 充值金额
        if (Objects.nonNull(userTag.getRechargeAmountTime()) && Objects.nonNull(userTag.getRechargeAmountMinAmount()) && Objects.nonNull(userTag.getRechargeAmountMaxAmount())) {
            List<Long> userIds;
            if (userTag.getRechargeAmountTime() != 0) {
                LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                Date startDate = addDateTimeCondition(userTag.getRechargeAmountTime(), localDateTime);
                userIds = userBalanceLogMapper.listUserIdByRechargeAmount(1, startDate, date, userTag.getRechargeAmountMinAmount(), userTag.getRechargeAmountMaxAmount());
            } else {
                userIds = userBalanceLogMapper.listUserIdByRechargeAmount(1, null, null, userTag.getRechargeAmountMinAmount(), userTag.getRechargeAmountMaxAmount());
            }
            List<UserVO> users = getInitUserVOList(userIds);
            listUserList.add(users);
        }
        // 充值次数
        if (Objects.nonNull(userTag.getRechargeNumTime()) && Objects.nonNull(userTag.getRechargeNumMinNum()) && Objects.nonNull(userTag.getRechargeNumMaxNum())) {
            List<Long> userIds;
            if (userTag.getRechargeNumTime() != 0) {
                LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                Date startDate = addDateTimeCondition(userTag.getRechargeNumTime(), localDateTime);
                userIds = userBalanceLogMapper.listUserIdByRechargeNum(1, startDate, date, userTag.getRechargeNumMinNum(), userTag.getRechargeNumMaxNum());
            } else {
                userIds = userBalanceLogMapper.listUserIdByRechargeNum(1, null, null, userTag.getRechargeNumMinNum(), userTag.getRechargeNumMaxNum());
            }
            List<UserVO> users = getInitUserVOList(userIds);
            listUserList.add(users);
        }
    }


    /**
     * 初始化集合
     * @param userIds 用户id集合
     * @return 用户信息列表
     */
    private List<UserVO> getInitUserVOList(List<Long> userIds) {
        List<UserVO> users = new ArrayList<>();
        for (Long userId : userIds) {
            UserVO user = new UserVO();
            user.setUserId(userId);
            users.add(user);
        }
        return users;
    }

    /**
     * 添加时间条件
     * @param timeType 类型
     * @param localDateTime 本地时间
     * @return 时间
     */
    private Date addDateTimeCondition(Integer timeType, LocalDateTime localDateTime) {
        Date startDate = null;
        switch (timeType) {
            case 1:
                startDate = Date.from(localDateTime.minusDays(RecentDaysTypeEnum.TODAY.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 2:
                startDate = Date.from(localDateTime.minusDays(RecentDaysTypeEnum.RECENT_SEVEN_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 3:
                startDate = Date.from(localDateTime.minusDays(RecentDaysTypeEnum.RECENT_FIFTEEN_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 4:
                startDate = Date.from(localDateTime.minusDays(RecentDaysTypeEnum.RECENT_THIRTY_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 5:
                startDate = Date.from(localDateTime.minusDays(RecentDaysTypeEnum.RECENT_FORTY_FIVE_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 6:
                startDate = Date.from(localDateTime.minusDays(RecentDaysTypeEnum.RECENT_SIXTY_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 7:
                startDate = Date.from(localDateTime.minusDays(RecentDaysTypeEnum.RECENT_NINETY_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 8:
                startDate = Date.from(localDateTime.minusDays(RecentDaysTypeEnum.RECENT_ONE_HUNDRED_AND_EIGHTY_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            default:
        }
        return startDate;
    }

    private List<UserVO> userFilter(List<UserVO> users1, List<UserVO> users2) {
        return users1.stream().filter(users2::contains).collect(toList());
    }
}
