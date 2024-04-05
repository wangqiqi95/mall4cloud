package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.vo.ScoreConfigApiVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.bo.UserScoreChangeLogBO;
import com.mall4j.cloud.user.constant.*;
import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.dto.UserLevelDTO;
import com.mall4j.cloud.user.mapper.UserExtensionMapper;
import com.mall4j.cloud.user.mapper.UserLevelMapper;
import com.mall4j.cloud.user.model.*;
import com.mall4j.cloud.user.service.*;
import com.mall4j.cloud.user.vo.UserLevelVO;
import com.mall4j.cloud.user.vo.UserRightsVO;
import com.mall4j.cloud.user.vo.UserVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员等级表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Service
public class UserLevelServiceImpl implements UserLevelService {

    @Autowired
    private UserLevelMapper userLevelMapper;
    @Autowired
    private ConfigFeignClient configFeignClient;
    @Autowired
    private UserScoreGetLogService userScoreGetLogService;
    @Autowired
    private UserScoreLogService userScoreLogService;
    @Autowired
    private UserLevelRightsService userLevelRightsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtensionService userExtensionService;
    @Autowired
    private UserLevelLogService userLevelLogService;
    @Autowired
    private UserLevelTermService userLevelTermService;
//    @Autowired
//    private OnsMQTemplate sendNotifyToUserTemplate;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private UserGrowthLogService userGrowthLogService;
//    @Autowired
//    private OnsMQTemplate levelUpCouponGiveTemplate;
//    @Autowired
//    private OnsMQTemplate levelDownCouponExpireTemplate;
    @Autowired
    private UserExtensionMapper userExtensionMapper;
    @Autowired
    private UserRightsService userRightsService;

    @Override
    @Cacheable(cacheNames = UserCacheNames.LEVEL_LIST_KEY, key = "#levelType")
    public List<UserLevelVO> list(Integer levelType) {
        List<UserLevelVO> list = userLevelMapper.list(levelType, null);
        for (UserLevelVO userLevelVO : list) {
            userLevelVO.setCouponsNum(userLevelMapper.countCouponsNumByUserLevelId(userLevelVO.getUserLevelId()));
        }
        return list;
    }

    @Override
    @Cacheable(cacheNames = UserCacheNames.LEVEL_GET_KEY, key = "#userLevelId")
    public UserLevelVO getByUserLevelId(Long userLevelId) {
        return userLevelMapper.getByUserLevelId(userLevelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserLevelDTO userLevelDTO) {
        Integer count = userLevelMapper.getUserMaxLevelByLevelType(userLevelDTO.getLevelType());
        if (Objects.isNull(count)) {
            count = 0;
        }
        count++;
        if (Objects.equals(userLevelDTO.getLevel(), count)) {
            userLevelDTO.setLevel(count);
        }
        userLevelDTO.setUpdateStatus(UpdateStatusEnum.UPDATE.value());
        userLevelMapper.save(userLevelDTO);
        userLevelRightsService.save(userLevelDTO.getUserLevelId(), userLevelDTO.getUserRightsIds());
        if (userLevelDTO.getLevelType() == 1) {
            userLevelTermService.updateBatch(userLevelDTO.getUserLevelId(), userLevelDTO.getUserLevelTerms());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserLevelDTO userLevelDTO) {
        UserLevelService userLevelService = (UserLevelService) AopContext.currentProxy();
        UserLevelVO dbUserLevel = userLevelService.getByUserLevelId(userLevelDTO.getUserLevelId());
        // 普通会员，修改时成长值有变化，更新状态设置为未更新
        if (Objects.equals(userLevelDTO.getLevelType(), LevelTypeEnum.ORDINARY_USER.value())
                && !Objects.equals(dbUserLevel.getNeedGrowth(), userLevelDTO.getNeedGrowth())) {
            userLevelDTO.setUpdateStatus(UpdateStatusEnum.WAIT_UPDATE.value());
        }
        userLevelMapper.update(userLevelDTO);
        if (userLevelDTO.getLevelType() == 1) {
            userLevelTermService.updateBatch(userLevelDTO.getUserLevelId(), userLevelDTO.getUserLevelTerms());
        }
        userLevelRightsService.update(userLevelDTO.getUserLevelId(), userLevelDTO.getUserRightsIds());
        if (Objects.equals(dbUserLevel.getLevelType(), LevelTypeEnum.ORDINARY_USER.value())) {
            userService.updateUserLevel(userLevelDTO.getLevel(), userLevelDTO.getNeedGrowth() - 1, null);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserLevelId(Long userLevelId) {
        UserLevelVO userLevelVO = userLevelMapper.getByUserLevelId(userLevelId);
        if (Objects.equals(userLevelVO.getLevelType(), LevelTypeEnum.PAY_USER.value())) {
            userLevelTermService.deleteBatch(userLevelId);
        }
        userLevelMapper.deleteByUserLevelId(userLevelId);
        userLevelRightsService.deleteByUserLevelId(userLevelId);
        if (Objects.equals(userLevelVO.getLevelType(), LevelTypeEnum.ORDINARY_USER.value())) {
            // 批量将普通会员的等级与成长值不匹配的筛选出来，更新等级
            batchUpdateUserLevel(LevelTypeEnum.ORDINARY_USER.value());
        }
        // 付费会员，在付费会员等级降低一级
        if (Objects.equals(userLevelVO.getLevelType(), LevelTypeEnum.PAY_USER.value())) {
            batchChangePayUserLevel(userLevelVO);
        }
        userRightsService.removeRightsByLevelTypeCache(userLevelVO.getLevelType());
    }

    @Override
    public void updateUserLevel() {
        List<UserLevelVO> dbUserLevels = list(LevelTypeEnum.ORDINARY_USER.value());
        List<Long> updateList = new ArrayList<>();
        boolean isUpdate = false;
        Integer level = 0;
        Integer minNeedGrowth = 0;
        Integer maxLevel = dbUserLevels.size();
        for (UserLevelVO userLevelVO : dbUserLevels) {
            // 成长值处于会员等级变化的区间，更新用户等级
            if (isUpdate) {
                userService.updateUserLevel(level, minNeedGrowth - 1, userLevelVO.getNeedGrowth());
                isUpdate = false;
            }
            // 最后一个等级--把大于所需积分的普通用户都设为该等级
            if (Objects.equals(userLevelVO.getUpdateStatus(), UpdateStatusEnum.WAIT_UPDATE.value())
                    && Objects.equals(userLevelVO.getLevel(), maxLevel)) {
                userService.updateUserLevel(userLevelVO.getLevel(), userLevelVO.getNeedGrowth() - 1, null);
                updateList.add(userLevelVO.getUserLevelId());
            } else if (Objects.equals(userLevelVO.getUpdateStatus(), UpdateStatusEnum.WAIT_UPDATE.value())) {
                isUpdate = true;
                updateList.add(userLevelVO.getUserLevelId());
            }
            level = userLevelVO.getLevel();
            minNeedGrowth = userLevelVO.getNeedGrowth();
        }
        userLevelMapper.updateStatusByUserLevelIds(updateList);
        // 批量将普通会员的等级与成长值不匹配的筛选出来，更新等级
        batchUpdateUserLevel(LevelTypeEnum.ORDINARY_USER.value());
        removeLevelListCache(LevelTypeEnum.ORDINARY_USER.value());
    }

    /**
     * 修改会员等级, 该方法会根据成长值更新用户等级
     * 所以该方法只适合更新普通会员的等级
     *
     * @param levelType 会员类型
     */
    private void batchUpdateUserLevel(Integer levelType) {
        // 获取等级与普通会员成长值不匹配的用户
        List<Long> userIds = userExtensionMapper.getGrowthLevelMismatchUserByLevelType(levelType);
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        // 获取未修改用户等级
        List<UserExtension> dbUserExtensions = userExtensionMapper.getByUserIdsAndLevelType(userIds);
        // 批量修改用户等级
        userExtensionMapper.batchUpdateLevelByUserIds(userIds, levelType);
        // 将用户等级从用户扩展表同步到用户表里面
        userService.updateUserLevelByUserExtensionAndUserIds(userIds);
        // 获取修改后用户等级
        List<UserExtension> userExtensions = userExtensionMapper.getByUserIdsAndLevelType(userIds);
        Map<Long, UserExtension> extensionMap = userExtensions.stream().collect(Collectors.toMap(UserExtension::getUserId, (k) -> k));
        List<UserLevelLog> userLevelLogs = new ArrayList<>();
        for (UserExtension dbUserExtension : dbUserExtensions) {
            Long userId = dbUserExtension.getUserId();
            Integer beforeLevel = dbUserExtension.getLevel();
            UserExtension userExtension = extensionMap.get(userId);
            Integer afterLevel = userExtension.getLevel();
            int changeLevel = afterLevel - beforeLevel;
            if (changeLevel == 0) {
                continue;
            }
            UserLevelLog userLevelLog = new UserLevelLog();
            userLevelLog.setUserId(userId);
            userLevelLog.setLevelIoType(afterLevel.compareTo(beforeLevel));
            userLevelLog.setLevelChangeReason(LevelChangeReasonEnum.USER_LEVEL_CHANGE.value());
            userLevelLog.setBeforeLevel(beforeLevel);
            userLevelLog.setBeforeLevelType(dbUserExtension.getLevelType());
            userLevelLog.setAfterLevel(afterLevel);
            userLevelLog.setAfterLevelType(userExtension.getLevelType());
            userLevelLogs.add(userLevelLog);
            // 清除缓存
            userService.removeUserCacheByUserId(userId);
        }
        // 保存等级变化日志
        userLevelLogService.batchSaveUserLevelLogs(userLevelLogs);

    }

    /**
     * 批量删除付费会员的等级
     * 当会员等级配置表删除一个付费会员等级配置，付费会员等级降低一级
     *
     * @param userLevelVO 将要删除的等级配置信息
     */
    private void batchChangePayUserLevel(UserLevelVO userLevelVO) {
        Integer level = userLevelVO.getLevel();
        if (level <= 1) {
            return;
        }
        // 获取等级对应的付费会员
        List<UserExtension> userExtensions = userExtensionMapper.getLevelAndLevelType(level, LevelTypeEnum.PAY_USER.value());
        if (CollUtil.isEmpty(userExtensions)) {
            return;
        }
        List<Long> userIds = userExtensions.stream().map(UserExtension::getUserId).collect(Collectors.toList());
        List<UserLevelLog> userLevelLogs = new ArrayList<>();
        for (UserExtension userExtension : userExtensions) {
            Long userId = userExtension.getUserId();
            UserLevelLog userLevelLog = new UserLevelLog();
            userLevelLog.setUserId(userId);
            userLevelLog.setLevelIoType(-1);
            userLevelLog.setLevelChangeReason(LevelChangeReasonEnum.USER_LEVEL_CHANGE.value());
            userLevelLog.setBeforeLevel(userExtension.getLevel());
            userLevelLog.setBeforeLevelType(userExtension.getLevelType());
            userLevelLog.setAfterLevel(level - 1);
            userLevelLog.setAfterLevelType(userExtension.getLevelType());
            userLevelLogs.add(userLevelLog);
        }
        // 批量将等级降低一级
        userExtensionMapper.batchChangeLevelByUserIdsAndLevel(userIds, -1);
        // 将用户等级从用户扩展表同步到用户表里面
        userService.updateUserLevelByUserExtensionAndUserIds(userIds);
        // 保存等级变化日志
        userLevelLogService.batchSaveUserLevelLogs(userLevelLogs);
        for (Long userId : userIds) {
            // 清除缓存
            userService.removeUserCacheByUserId(userId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchUpdateGrowth(UserAdminDTO userAdminDTO) {
        Integer growth = userAdminDTO.getGrowth();
        List<Long> userIds = userAdminDTO.getUserIds();
        boolean isUpdate = Objects.isNull(growth) || CollUtil.isEmpty(userIds) || Objects.equals(0, growth);
        if (isUpdate) {
            return;
        }
        // 修改前数据
        List<UserExtension> dbUserExtensions = userExtensionService.getByUserIdsAndLevelType(userIds);
        userIds = dbUserExtensions.stream().map(UserExtension::getUserId).collect(Collectors.toList());
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        for (UserExtension extension : dbUserExtensions) {
            int compare = Integer.compare(Integer.MAX_VALUE - extension.getGrowth(), growth);
            // 成长值的最大值为Integer.MAX_VALUE，当用户成长值 + 新增成长值达到Integer.MAX_VALUE这个值的时候，用户将不能在继续增加成长值了
            if (compare < 0) {
                UserApiVO userVO = userService.getByUserId(extension.getUserId());
                throw new LuckException(userVO.getNickName() + " 新增成长值+用户成长值大于阈值，剩余可增加成长值为：" + (Integer.MAX_VALUE - extension.getGrowth()));
            }
        }
        // 系统直接修改成长值
        userExtensionService.updateUserScoreOrGrowth(userIds, null, growth);
        // 成长值和积分日志
        batchAddScoreOrGrowthLogs(dbUserExtensions, null, growth);
        // 根据成长值提升/降低用户等级
        List<UserLevelLog> userLevelLogs = batchLevelUpByGrowthAndUserIds(userIds, LevelTypeEnum.ORDINARY_USER.value());
        // 等级减少的，回退奖励
        if (growth.compareTo(0) < 0) {
            // 根据用户变更成长值日志回退奖励
            // 需要发送奖励, 等级降低一次就回退一次奖励
            // 回退奖励
            batchRollbackLevelDownRewards(userLevelLogs, LevelTypeEnum.ORDINARY_USER.value());
            return;
        }
        // 根据用户变更成长值日志发放奖励
        // 需要发送奖励, 等级提升一次就发放一次奖励
        // 发送奖励
        batchIssuanceLevelUpRewards(userLevelLogs, LevelTypeEnum.ORDINARY_USER.value());
    }

    /**
     * 回退升级的奖励
     * 用户等级批量降低，回退奖励
     * 等级奖励降低的取消发送的奖励。如果等级4 降低到 等级1，等级2，3，4 的奖励回退。
     * 如果是付费会员和普通会员的相互转变，引起的等级变化，不要用此方法
     *
     * @param userLevelLogs 等级变更日志
     * @param levelType     会员类型
     */
    private void batchRollbackLevelDownRewards(List<UserLevelLog> userLevelLogs, Integer levelType) {
        // 筛选出用户等级是降低了的用户回退奖励
        userLevelLogs = userLevelLogs.stream().filter(item -> item.getLevelIoType() == -1).collect(Collectors.toList());
        if (CollUtil.isEmpty(userLevelLogs)) {
            return;
        }
        // 获取每个等级相关优惠券列表
        List<UserLevelVO> levelCouponIdList = userLevelMapper.listLevelCouponByLevelType(levelType, null);
        // 获取普通会员等级列表
        List<UserLevelVO> levelList = userLevelMapper.list(levelType, null);
        Map<Integer, UserLevelVO> levelMap = levelCouponIdList.stream().collect(Collectors.toMap(UserLevelVO::getLevel, (k) -> k));
        List<UserScoreLog> userScoreLogs = new ArrayList<>();
        List<UserScoreGetLog> userScoreGetLogs = new ArrayList<>();
        List<UserExtension> userExtensions = new ArrayList<>();
        DateTime now = DateUtil.date();
        for (UserLevelLog userLevelLog : userLevelLogs) {
            Long userId = userLevelLog.getUserId();
            // 变更前的等级
            Integer beforeLevel = userLevelLog.getBeforeLevel();
            // 降低后等级
            Integer afterLevel = userLevelLog.getAfterLevel();
            // 每个用户降低等级的列表
            List<UserLevelVO> userChangeLevelList = levelList.stream()
                    .filter(item -> item.getLevel() > afterLevel && item.getLevel() <= beforeLevel)
                    .collect(Collectors.toList());
            for (UserLevelVO levelVO : userChangeLevelList) {
                List<UserRightsVO> userRightsList = levelVO.getUserRightsList();
                if (CollUtil.isEmpty(userRightsList)) {
                    continue;
                }
                for (UserRightsVO userRightsVO : userRightsList) {
                    Integer rightsType = userRightsVO.getRightsType();
                    // 用户需要扣除的优惠券
                    if (Objects.equals(rightsType, RightsTypeEnum.COUPON.value())) {
                        List<Long> couponIds = levelMap.get(levelVO.getLevel()).getCouponIds();
                    }
                    // 用户增加积分
                    if (Objects.equals(rightsType, RightsTypeEnum.SCORE_PRESENTING.value())) {
                        Long presScore = userRightsVO.getPresScore();
                        // TODO 扣减积分
                        UserScoreGetLog scoreDetail = getInitReduceUserScoreGetLog(now, userId, presScore);
                        userScoreGetLogs.add(scoreDetail);
                        UserScoreLog userScoreLog = getInitReduceUserScoreLog(userId, presScore);
                        userScoreLogs.add(userScoreLog);
                        UserExtension userExtension = new UserExtension();
                        userExtension.setUserId(userId);
                        userExtension.setScore(-presScore);
                        userExtensions.add(userExtension);
                    }
                }
            }
            if (CollUtil.isNotEmpty(userExtensions)) {
                userExtensionService.batchUpdateScore(userExtensions);
                userScoreGetLogService.saveBatch(userScoreGetLogs);
                userScoreLogService.saveBatch(userScoreLogs);
            }
            // 扣除优惠券
            //暂时没有这个业务，注释这里的逻辑 mq对应的topic 做微信视频号订单同步
//            if (CollUtil.isNotEmpty(bindCouponDTOList)) {
//                // 发送消息，扣除优惠券
//                SendResult sendResult = levelDownCouponExpireTemplate.syncSend(bindCouponDTOList);
//                if (sendResult == null || sendResult.getMessageId() == null) {
//                        // 消息发不出去就抛异常，因为订单回调会有多次，几乎不可能每次都无法发送出去，发的出去无所谓因为接口是幂等的
//                    throw new LuckException(ResponseEnum.EXCEPTION);
//                }
//            }
        }
    }

    private UserScoreLog getInitReduceUserScoreLog(Long userId, Long presScore) {
        UserScoreLog userScoreLog = new UserScoreLog();
        userScoreLog.setUserId(userId);
        userScoreLog.setSource(ScoreLogTypeEnum.EXPIRE.value());
        userScoreLog.setScore(presScore);
        userScoreLog.setIoType(ScoreIoTypeEnum.EXPENDITURE.value());
        return userScoreLog;
    }

    private UserScoreGetLog getInitReduceUserScoreGetLog(DateTime now, Long userId, Long presScore) {
        UserScoreGetLog scoreDetail = new UserScoreGetLog();
        scoreDetail.setUserId(userId);
        scoreDetail.setUsableScore(presScore);
        scoreDetail.setStatus(ScoreGetLogStatusEnum.EXPIRED.value());
        scoreDetail.setExpireTime(now);
        return scoreDetail;
    }

    /**
     * 批量等级提升后，批量发放等级提升奖励.
     * 等级奖励降低的取消发送的奖励。如果等级1 提升到 等级 4，等级2，3，4 的奖励都发。
     *
     * @param userLevelLogs 等级变更日志
     * @param levelType     会员类型
     */
    private void batchIssuanceLevelUpRewards(List<UserLevelLog> userLevelLogs, Integer levelType) {
        // 筛选出用户等级是提升了的用户发放奖励
        userLevelLogs = userLevelLogs.stream().filter(item -> item.getLevelIoType() == 1).collect(Collectors.toList());
        if (CollUtil.isEmpty(userLevelLogs)) {
            return;
        }
        // 获取普通会员等级列表
        List<UserLevelVO> levelList = userLevelMapper.list(levelType, null);
        // 获取每个等级相关优惠券列表
        List<UserLevelVO> levelCouponIdList = userLevelMapper.listLevelCouponByLevelType(levelType, null);
        Map<Integer, UserLevelVO> levelMap = levelCouponIdList.stream().collect(Collectors.toMap(UserLevelVO::getLevel, (k) -> k));
        List<UserExtension> userExtensions = new ArrayList<>();
        List<UserScoreLog> userScoreLogs = new ArrayList<>();
        List<UserScoreGetLog> userScoreGetLogs = new ArrayList<>();
        for (UserLevelLog userLevelLog : userLevelLogs) {
            Long userId = userLevelLog.getUserId();
            // 变更前的等级
            Integer beforeLevel = userLevelLog.getBeforeLevel();
            // 提升后等级
            Integer afterLevel = userLevelLog.getAfterLevel();
            // 每个用户提升等级的列表
            List<UserLevelVO> userChangeLevelList = levelList.stream().filter(item -> item.getLevel() > beforeLevel && item.getLevel() <= afterLevel).collect(Collectors.toList());
            for (UserLevelVO levelVO : userChangeLevelList) {
                List<UserRightsVO> userRightsList = levelVO.getUserRightsList();
                if (CollUtil.isEmpty(userRightsList)) {
                    continue;
                }
                for (UserRightsVO userRightsVO : userRightsList) {
                    Integer rightsType = userRightsVO.getRightsType();
                    // 用户增加积分
                    if (Objects.equals(rightsType, RightsTypeEnum.SCORE_PRESENTING.value())) {
                        Long presScore = userRightsVO.getPresScore();
                        UserScoreGetLog scoreDetail = new UserScoreGetLog();
                        scoreDetail.setUserId(userId);
                        scoreDetail.setUsableScore(presScore);
                        scoreDetail.setStatus(ScoreGetLogStatusEnum.NORMAL.value());
                        userScoreGetLogs.add(scoreDetail);
                        UserScoreLog userScoreLog = new UserScoreLog();
                        userScoreLog.setUserId(userId);
                        userScoreLog.setSource(ScoreLogTypeEnum.LEVEL_UP.value());
                        userScoreLog.setScore(presScore);
                        userScoreLog.setIoType(ScoreIoTypeEnum.INCOME.value());
                        userScoreLogs.add(userScoreLog);
                        UserExtension userExtension = new UserExtension();
                        userExtension.setUserId(userId);
                        userExtension.setScore(presScore);
                        userExtensions.add(userExtension);
                    }
                    // 给用户发优惠券
                    if (Objects.equals(rightsType, RightsTypeEnum.COUPON.value())) {
                        List<Long> couponIds = levelMap.get(levelVO.getLevel()).getCouponIds();
                    }
                }
            }
        }
        if (CollUtil.isNotEmpty(userExtensions)) {
            userExtensionService.batchUpdateScore(userExtensions);
            userScoreGetLogService.saveBatch(userScoreGetLogs);
            userScoreLogService.saveBatch(userScoreLogs);
        }
        // 发优惠券

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UserLevelLog> batchLevelUpByGrowthAndUserIds(List<Long> userIds, Integer levelType) {
        // 获取普通会员等级列表
        List<UserLevelVO> levelList = userLevelMapper.list(levelType, null);
        // 会员等级列表为空，无法升到下一级，直接结束
        if (CollUtil.isEmpty(levelList)) {
            return Collections.emptyList();
        }
        // 用户扩展表信息
        List<UserExtension> userExtensions = userExtensionService.getByUserIdsAndLevelType(userIds);
        if (CollUtil.isEmpty(userExtensions)) {
            return Collections.emptyList();
        }
        List<UserLevelLog> userLevelLogs = new ArrayList<>();
        // 需要更新等级的用户
        List<Long> updateUserIds = userExtensions.stream()
                .filter(item -> {
                    // 成长值对应的等级是否和会员等级匹配，不匹配就需要更新用户等级
                    Integer level = getUserLevelByGrowth(levelList, item.getGrowth());
                    if (item.getLevel().compareTo(level) != 0) {
                        UserLevelLog userLevelLog = new UserLevelLog();
                        userLevelLog.setUserId(item.getUserId());
                        userLevelLog.setLevelIoType(level.compareTo(item.getLevel()));
                        int levelChangeReason = userLevelLog.getLevelIoType() > 0 ? LevelChangeReasonEnum.GROWTH_ENOUGH.value() : LevelChangeReasonEnum.GROWTH_NOT_ENOUGH.value();
                        userLevelLog.setLevelChangeReason(levelChangeReason);
                        userLevelLog.setBeforeLevel(item.getLevel());
                        userLevelLog.setBeforeLevelType(item.getLevelType());
                        userLevelLog.setAfterLevel(level);
                        userLevelLog.setAfterLevelType(item.getLevelType());
                        userLevelLogs.add(userLevelLog);
                        return true;
                    }
                    return false;
                })
                .map(UserExtension::getUserId)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(updateUserIds)) {
            return Collections.emptyList();
        }
        userExtensionService.batchUpdateLevelByUserIds(updateUserIds, levelType);
        // 将用户等级从用户扩展表同步到用户表里面
        userService.updateUserLevelByUserExtensionAndUserIds(updateUserIds);
        // 批量保存会员等级变化日志
        userLevelLogService.batchSaveUserLevelLogs(userLevelLogs);
        // 清除用户缓存
        for (Long updateUserId : updateUserIds) {
            userService.removeUserCacheByUserId(updateUserId);
        }
        return userLevelLogs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundGrowth(Long orderId) {
        UserGrowthLog growthLog = userGrowthLogService.getByBizId(orderId, 1);
        // 如果没有找到这条增加成长值的记录，就不用退成长值了
        if (Objects.isNull(growthLog)) {
            return;
        }
        // 需要减少成长值数值
        Integer growth = -growthLog.getChangeGrowth();
        Long userId = growthLog.getUserId();
        List<Long> userIds = Collections.singletonList(userId);
        if (Objects.equals(0, growth)) {
            return;
        }
        // 系统直接修改成长值
        userExtensionService.updateUserScoreOrGrowth(userIds, null, growth);
        // 成长值日志
        UserGrowthLog userGrowthLog = new UserGrowthLog();
        userGrowthLog.setUserId(userId);
        userGrowthLog.setSource(GrowthLogSourceEnum.ORDER.value());
        userGrowthLog.setBizId(orderId);
        userGrowthLog.setChangeGrowth(growth);
        userGrowthLog.setRemarks("订单退还成长值");
        userGrowthLogService.save(userGrowthLog);
        // 根据成长值提升/降低用户等级
        List<UserLevelLog> userLevelLogs = batchLevelUpByGrowthAndUserIds(userIds, LevelTypeEnum.ORDINARY_USER.value());
        // 回退奖励
        batchRollbackLevelDownRewards(userLevelLogs, LevelTypeEnum.ORDINARY_USER.value());
    }

    @Override
    public int getMaxLevel(Integer levelType) {
        return userLevelMapper.getMaxLevel(levelType);
    }

    @Override
    public int getUserNormalLevel(Integer growth) {
        return userLevelMapper.getUserNormalLevel(growth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void expireVipUsers(List<UserVO> users) {
        if (CollUtil.isEmpty(users)) {
            return;
        }
        List<Long> userIds = users.stream().map(UserVO::getUserId).collect(Collectors.toList());
        // 将付费会员变成普通会员，会员等级直接变成普通会员的成长值对应的等级
        // 获取修改之前的等级和会员类型
        List<UserExtension> dbUserExtensions = userExtensionService.getByUserIdsAndLevelType(userIds);

        // 批量修改用户等级和用户类型
        userExtensionMapper.batchUpdateTypeAndLevelByUserIds(userIds, LevelTypeEnum.PAY_USER.value(), LevelTypeEnum.ORDINARY_USER.value());
        // 将用户会员类型和等级从用户扩展表同步到用户表里面
        userService.updateUserTypeLevelByUserExtensionAndUserIds(userIds);

        // 获取修改后用户等级
        List<UserExtension> userExtensions = userExtensionMapper.getByUserIdsAndLevelType(userIds);
        Map<Long, UserExtension> extensionMap = userExtensions.stream().collect(Collectors.toMap(UserExtension::getUserId, (k) -> k));
        List<UserLevelLog> userLevelLogs = new ArrayList<>();
        for (UserExtension dbUserExtension : dbUserExtensions) {
            Long userId = dbUserExtension.getUserId();
            Integer beforeLevel = dbUserExtension.getVipLevel();
            UserExtension userExtension = extensionMap.get(userId);
            Integer afterLevel = userExtension.getLevel();
            // 等级变化记录，会员类型一定是改变了的
            UserLevelLog userLevelLog = new UserLevelLog();
            userLevelLog.setUserId(userId);
            userLevelLog.setLevelIoType(afterLevel.compareTo(beforeLevel));
            userLevelLog.setLevelChangeReason(LevelChangeReasonEnum.VIP_EXPIRE.value());
            userLevelLog.setBeforeLevel(beforeLevel);
            userLevelLog.setBeforeLevelType(dbUserExtension.getLevelType());
            userLevelLog.setAfterLevel(afterLevel);
            userLevelLog.setAfterLevelType(userExtension.getLevelType());
            userLevelLogs.add(userLevelLog);
            // 清除缓存
            userService.removeUserCacheByUserId(userId);
        }
        // 保存等级变化日志
        userLevelLogService.batchSaveUserLevelLogs(userLevelLogs);
    }

    @Override
    public void updateRecruitStatus(UserLevelDTO userLevelDTO) {
        UserLevelDTO userLevel = new UserLevelDTO();
        userLevel.setUserLevelId(userLevelDTO.getUserLevelId());
        Integer recruitStatus = userLevelDTO.getRecruitStatus();
        userLevel.setRecruitStatus(recruitStatus);
        userLevelMapper.update(userLevel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerUserScore(List<UserExtension> userExtensionList) {
        Long registerScore = 0L;
        String scoreConfig = configFeignClient.getConfig(Constant.SCORE_CONFIG).getData();
        if (Objects.nonNull(scoreConfig)) {
            ScoreConfigApiVO userScoreDetailVO = Json.parseObject(scoreConfig, ScoreConfigApiVO.class);
            registerScore = userScoreDetailVO.getRegisterScore();
        }
        List<Long> userIds = userExtensionList.stream().map(UserExtension::getUserId).collect(Collectors.toList());
        if (CollUtil.isEmpty(userIds) || registerScore <= 0L) {
            return;
        }
        // 系统直接修改积分
        userExtensionService.updateUserScoreOrGrowth(userIds, registerScore, null);
        List<UserScoreLog> userScoreLogs = new ArrayList<>();
        List<UserScoreGetLog> userScoreDetails = new ArrayList<>();
        for (UserExtension userExtension : userExtensionList) {
            Long userId = userExtension.getUserId();
            // 积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userId);
            userScoreLog.setScore(registerScore);
            userScoreLog.setIoType(ScoreIoTypeEnum.INCOME.value());
            userScoreLog.setSource(ScoreLogTypeEnum.REGISTER.value());
            userScoreLogs.add(userScoreLog);
            // 积分明细
            UserScoreGetLog userScoreGetLog = new UserScoreGetLog();
            userScoreGetLog.setUserId(userId);
            userScoreGetLog.setUsableScore(registerScore);
            userScoreGetLog.setStatus(ScoreGetLogStatusEnum.NORMAL.value());
            userScoreDetails.add(userScoreGetLog);
        }
        if (CollUtil.isNotEmpty(userScoreLogs)) {
            userScoreLogService.saveBatch(userScoreLogs);
            userScoreGetLogService.saveBatch(userScoreDetails);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerUserSendRights(List<UserExtension> userExtensionList) {
        if (CollUtil.isEmpty(userExtensionList)) {
            return;
        }
        // 付费会员等级设置，如果是存在在的，就发放对应等级的权益；如果不存在，就不给付费会员发放权益
        List<Long> userIds = userExtensionList.stream().map(UserExtension::getUserId).collect(Collectors.toList());

        List<UserExtension> updateUserExtensions = new ArrayList<>();
        List<UserScoreLog> userScoreLogs = new ArrayList<>();
        List<UserScoreGetLog> userScoreGetLogs = new ArrayList<>();

        if (CollUtil.isNotEmpty(updateUserExtensions)) {
            userExtensionService.batchUpdateScore(updateUserExtensions);
        }
        if (CollUtil.isNotEmpty(userScoreLogs)) {
            userScoreGetLogService.saveBatch(userScoreGetLogs);
            userScoreLogService.saveBatch(userScoreLogs);
        }
        // 发优惠券

    }

    /**
     * 只发放等级对应的权益
     *
     * @param updateUserExtensions 修改用户扩展信息数据
     * @param userScoreLogs        积分日志集合
     * @param userScoreGetLogs     积分明细集合
     * @param userExtensionList    用户扩展信息数据
     * @param levelType            会员类型
     */
    private void justIssuanceLevelRights(List<UserExtension> updateUserExtensions, List<UserScoreLog> userScoreLogs, List<UserScoreGetLog> userScoreGetLogs, List<UserExtension> userExtensionList, Integer levelType) {
        // 会员类型筛选
        List<UserExtension> userExtensions = userExtensionList.stream().filter(item -> Objects.equals(levelType, item.getLevelType())).collect(Collectors.toList());
        if (CollUtil.isEmpty(userExtensions)) {
            return;
        }
        // 会员等级
        List<UserLevelVO> levelList = userLevelMapper.list(levelType, null);
        // 付费会员等级设置，如果是存在在的，就发放对应等级的权益；如果不存在，就不给付费会员发放权益
        // 普通会员等级必然是存在一个最低等级
        if (CollUtil.isEmpty(levelList)) {
            return;
        }
        Map<Integer, UserLevelVO> userLevelMap = levelList.stream().collect(Collectors.toMap(UserLevelVO::getLevel, (k) -> k));
        // 获取会员每个等级相关优惠券列表
        List<UserLevelVO> levelCouponIds = userLevelMapper.listLevelCouponByLevelType(levelType, null);
        Map<Integer, UserLevelVO> levelMap = levelCouponIds.stream().collect(Collectors.toMap(UserLevelVO::getLevel, (k) -> k));
        for (UserExtension userExtension : userExtensions) {
            Long userId = userExtension.getUserId();
            Integer level = userExtension.getLevel();
            UserLevelVO userLevelVO = userLevelMap.get(level);
            if (Objects.isNull(userLevelVO)) {
                continue;
            }
            // 等级对应的权益
            List<UserRightsVO> userRights = userLevelVO.getUserRightsList();
            if (CollUtil.isEmpty(userRights)) {
                continue;
            }
            for (UserRightsVO userRightsVO : userRights) {
                Integer rightsType = userRightsVO.getRightsType();
                // 用户增加积分
                if (Objects.equals(rightsType, RightsTypeEnum.SCORE_PRESENTING.value())) {
                    // 赠送积分
                    Long presScore = userRightsVO.getPresScore();
                    // 扩展信息表，加积分
                    UserExtension updateUserExtension = new UserExtension();
                    updateUserExtension.setUserId(userId);
                    updateUserExtension.setScore(presScore);
                    updateUserExtensions.add(updateUserExtension);
                    // 积分日志
                    UserScoreLog userScoreLog = new UserScoreLog();
                    userScoreLog.setUserId(userId);
                    userScoreLog.setScore(presScore);
                    userScoreLog.setIoType(ScoreIoTypeEnum.INCOME.value());
                    userScoreLog.setSource(ScoreLogTypeEnum.LEVEL_UP.value());
                    userScoreLogs.add(userScoreLog);
                    // 积分明细
                    UserScoreGetLog scoreDetail = new UserScoreGetLog();
                    scoreDetail.setUserId(userId);
                    scoreDetail.setUsableScore(presScore);
                    scoreDetail.setStatus(ScoreGetLogStatusEnum.NORMAL.value());
                    userScoreGetLogs.add(scoreDetail);
                }
                // 给用户发优惠券

            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGrowthAndScore(double growthPrice, Long score, Long userId, Long bizId, UserExtension userExtension, Integer type) {
        UserApiVO userApiVO = userService.getByUserId(userId);
        User user = mapperFacade.map(userApiVO, User.class);
        if (user.getLevel() == null) {
            user.setLevel(Constant.USER_LEVEL_INIT);
        }
        Integer growth = (int) growthPrice;

        addScoreAndGrowth(bizId, userId, growth, score, type);
        // 判断是否升级
        // 修改用户信息
        // 用户＋增加成长值
        Integer nowGrowth = userExtension.getGrowth() == null ? growth : userExtension.getGrowth() + growth;
        userExtension.setScore(userExtension.getScore() == null ? score : userExtension.getScore() + score);
        userExtension.setGrowth(nowGrowth);
        userExtension.setUpdateTime(new Date());
        // 判断用户是否提升过等级,如果是修改用户等级并添加用户等级提升日志
        List<UserLevelVO> userLevels = userLevelMapper.selectList(nowGrowth, userExtension.getLevel());
        if (CollectionUtils.isEmpty(userLevels)) {
            userExtensionMapper.updateBalanceByVersion(userExtension);
        } else {
            Integer level = userLevels.get(0).getLevel();
            //修改用户等级
            user.setLevel(level);
            userService.updateUser(user);
            //判断用户是否提升过等级
            //确认收货
            levelUp(userLevels, null, userExtension, user.getPhone());
        }
    }

    /**
     * 添加积分明细、积分日志、成长值日志
     *
     * @param bizId  type = 1:订单号 type = 2:充值id
     * @param userId 用户id
     * @param growth 成长值
     * @param score  积分
     * @param type   type = 1:订单 type = 2:余额
     */
    private void addScoreAndGrowth(Long bizId, Long userId, Integer growth, Long score, Integer type) {
        if (growth > 0) {
            //添加成长值日志
            UserGrowthLog userGrowthLog = new UserGrowthLog();
            userGrowthLog.setChangeGrowth(growth);
            userGrowthLog.setSource(1);
            userGrowthLog.setUserId(userId);
            userGrowthLog.setBizId(bizId);
            if (type.equals(1)) {
                userGrowthLog.setRemarks("订单确认收货获取的成长值");
            } else {
                userGrowthLog.setRemarks("用户充值余额获取的成长值");
            }
            userGrowthLogService.save(userGrowthLog);
        }
        if (score == 0) {
            return;
        }
        //添加积分日志
        UserScoreLog userScoreLog = new UserScoreLog();
        userScoreLog.setUserId(userId);
        userScoreLog.setScore(score);
        userScoreLog.setBizId(bizId);
        if (type.equals(1)) {
            userScoreLog.setSource(ScoreLogTypeEnum.SHOP.value());
        } else {
            userScoreLog.setSource(ScoreLogTypeEnum.BALANCE.value());
        }
        userScoreLog.setCreateTime(new Date());
        userScoreLog.setIoType(ScoreIoTypeEnum.INCOME.value());
        userScoreLogService.save(userScoreLog);
        if (score < 0) {
            score = Math.abs(score);
            //查询积分详细表数据
            List<UserScoreGetLog> scoreDetailList = userScoreGetLogService.listByCreateTime(userId, 1);
            List<UserScoreGetLog> updateScoreDetails = new ArrayList<>();
            // 如果是负的则表示为减少积分
            // 修改积分明细，如果当前明细不够扣除在进行下一条
            // 如果够添加一条积分明细记录
            for (UserScoreGetLog scoreDetail : scoreDetailList) {
                if (scoreDetail.getUsableScore() <= score) {
                    scoreDetail.setStatus(0);
                    updateScoreDetails.add(scoreDetail);
                    score -= scoreDetail.getUsableScore();
                } else {
                    UserScoreGetLog addDetail = new UserScoreGetLog();
                    addDetail.setCreateTime(scoreDetail.getCreateTime());
                    addDetail.setStatus(0);
                    addDetail.setUserId(scoreDetail.getUserId());
                    addDetail.setUsableScore(score);
                    userScoreGetLogService.save(addDetail);

                    scoreDetail.setUsableScore(scoreDetail.getUsableScore() - score);
                    updateScoreDetails.add(scoreDetail);
                    break;
                }
                if (score <= 0) {
                    break;
                }
            }
            userScoreGetLogService.updateBatchById(updateScoreDetails);
        } else {
            //添加积分明细
            UserScoreGetLog addDetail = new UserScoreGetLog();
            addDetail.setCreateTime(new Date());
            addDetail.setStatus(1);
            addDetail.setUserId(userId);
            addDetail.setBizId(String.valueOf(bizId));
            addDetail.setUsableScore(score);
            userScoreGetLogService.save(addDetail);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchUserScore(UserAdminDTO userAdminDTO) {
        List<Long> userIds = userAdminDTO.getUserIds();
        Long score = userAdminDTO.getScore();
        if (CollUtil.isEmpty(userIds) || Objects.isNull(score) || Objects.equals(0L, score)) {
            return;
        }
        // 修改前数据
        List<UserExtension> dbUserExtensions = userExtensionService.getByUserIdsAndLevelType(userIds);
        userIds = dbUserExtensions.stream().map(UserExtension::getUserId).collect(Collectors.toList());
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        for (UserExtension extension : dbUserExtensions) {
            int compare = Long.compare(Long.MAX_VALUE - extension.getScore(), score);
            // 用积分的最大值为Long.MAX_VALUE，当用户积分 + 新增积分达到Long.MAX_VALUE这个值的时候，用户将不能在继续增加积分了
            if (compare < 0) {
                UserApiVO userVO = userService.getByUserId(extension.getUserId());
                throw new LuckException(userVO.getNickName() + " 新增积分+用户积分大于阈值，剩余可增加积分为：" + (Long.MAX_VALUE - extension.getScore()));
            }
        }
        // 系统直接修改积分
        userExtensionService.updateUserScoreOrGrowth(userIds, score, null);
        // 积分日志
        batchAddScoreOrGrowthLogs(dbUserExtensions, score, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initUserInfoAndLevelInfo(UserExtension userExtension, Integer level, Integer levelType, String phone) {
        UserLevelVO userLevel = userLevelMapper.getOneByTypeAndLevel(0, Constant.USER_LEVEL_INIT);
        ArrayList<UserLevelVO> userLevels = new ArrayList<>();
        /*String scoreConfig = configFeignClient.getConfig(Constant.SCORE_CONFIG).getData();
        Long registerScore = 0L;
        if (Objects.nonNull(scoreConfig)) {
            ScoreConfigApiVO userScoreDetailVO = Json.parseObject(scoreConfig, ScoreConfigApiVO.class);
            registerScore = userScoreDetailVO.getRegisterScore();
        }*/
        userLevels.add(userLevel);
        userExtension.setScore(0l);
        userExtensionService.update(userExtension);
        /*if (registerScore > 0) {
            // 添加积分明细
            UserScoreGetLog addDetail = new UserScoreGetLog();
            addDetail.setCreateTime(new Date());
            addDetail.setStatus(1);
            addDetail.setUserId(userExtension.getUserId());
            addDetail.setUsableScore(registerScore);
            userScoreGetLogService.save(addDetail);
            //添加积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userExtension.getUserId());
            userScoreLog.setScore(registerScore);
            userScoreLog.setSource(ScoreLogTypeEnum.REGISTER.value());
            userScoreLog.setCreateTime(new Date());
            userScoreLog.setIoType(1);
            userScoreLogService.save(userScoreLog);
        }*/
        levelUp(userLevels, null, userExtension, phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(UserLevelDTO userLevelDTO) {
        UserLevelVO dbUserLevel = new UserLevelVO();
        //根据成长值，更新已有用户的等级
        if (Objects.nonNull(userLevelDTO.getUserLevelId())) {
            dbUserLevel = userLevelMapper.getByUserLevelId(userLevelDTO.getUserLevelId());
        } else {
            int count = userLevelMapper.countByLevelType(userLevelDTO.getLevelType());
            userLevelDTO.setLevel(++count);
        }
        // 如果是普通会员，新增的等级必须是递增的
        Integer level = userLevelDTO.getLevel();
        if (Objects.equals(userLevelDTO.getLevelType(), 0) && level != 1) {
            List<UserLevelVO> list = userLevelMapper.list(0, level - 1);
            if (CollUtil.isNotEmpty(list)) {
                UserLevelVO levelVO = list.stream().max(Comparator.comparing(UserLevelVO::getNeedGrowth)).get();
                if (userLevelDTO.getNeedGrowth() <= levelVO.getNeedGrowth()) {
                    throw new LuckException("等级需要的成长值必须大于上一级需要的成长值");
                }
            }
        }
        // 若是普通会员，新增或修改时成长值有变化，状态设置为未更新用户等级
        if (Objects.equals(userLevelDTO.getLevelType(), 0) && !Objects.equals(dbUserLevel.getNeedGrowth(), userLevelDTO.getNeedGrowth())) {
            userLevelDTO.setUpdateStatus(0);
        } else {
            userLevelDTO.setUpdateStatus(1);
        }
        if (Objects.nonNull(userLevelDTO.getUserLevelId())) {
            update(userLevelDTO);
        } else {
            // 初始化允许招募会员
            userLevelDTO.setRecruitStatus(RecruitStatusEnum.ALLOW_RECRUIT.value());
            save(userLevelDTO);
            // 新增等级的时候自动更新用户等级
            if (Objects.equals(userLevelDTO.getLevelType(), LevelTypeEnum.ORDINARY_USER.value())) {
                // 批量将普通会员的等级与成长值不匹配的筛选出来，更新等级
                batchUpdateUserLevel(LevelTypeEnum.ORDINARY_USER.value());
            }
        }
        userRightsService.removeRightsByLevelTypeCache(userLevelDTO.getLevelType());

//        this.saveOrUpdate(userLevelDTO);
//        // 插入/更新等级分类数据
//        this.delOrAddCategory(userLevelDTO,dbUserLevel);
//        //插入/更新等级优惠券数据
//        this.delOrAddCoupon(userLevelDTO);
//        //插入/更新等级用户权益数据
//        this.delOrAddRights(userLevelDTO);
    }

    @Override
    @Cacheable(cacheNames = UserCacheNames.LEVEL_GET_LIST_KEY, key = "#levelType + '-' + #level")
    public UserLevelVO getOneByTypeAndLevel(Integer levelType, Integer level) {
        return userLevelMapper.getOneByTypeAndLevel(levelType, level);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = UserCacheNames.LEVEL_LIST_KEY, key = "#levelType"),
            @CacheEvict(cacheNames = UserCacheNames.LEVEL_GET_KEY, key = "#userLevelId"),
            @CacheEvict(cacheNames = UserCacheNames.LEVEL_GET_LIST_KEY, key = "#levelType + '-' + #level")
    })
    public void removeLevelCache(Long userLevelId, Integer levelType, Integer level) {

    }

    @Override
    @CacheEvict(cacheNames = UserCacheNames.LEVEL_LIST_KEY, key = "#levelType")
    public void removeLevelListCache(Integer levelType) {

    }

    @Override
    @CacheEvict(cacheNames = UserCacheNames.LEVEL_GET_KEY, key = "#userLevelId")
    public void removeLevelById(Long userLevelId) {

    }

    /**
     * 等级提升
     *
     * @param userLevels   多个等级，奖励发放时
     * @param userLevelLog 等级日志,在购买付费会员时存在
     * @param user         用户详细信息
     * @param phone        用户手机号码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void levelUp(List<UserLevelVO> userLevels, UserLevelLog userLevelLog, UserExtension user, String phone) {
        List<UserScoreLog> userScoreLogs = new ArrayList<>();
        List<UserScoreGetLog> userScoreGetLogs = new ArrayList<>();
        //用户可能升多级，批量插入升级日志
        List<UserLevelLog> levelLogs = new ArrayList<>();
        List<Long> couponIds = new ArrayList<>();
        Integer beforeLevel = user.getLevel();
        Integer levelType = user.getLevelType();
        for (int i = userLevels.size() - 1; i >= 0; i--) {
            UserLevelVO userLevel = userLevels.get(i);
            //修改用户扩展表等级
            user.setLevel(userLevel.getLevel());
            user.setLevelType(userLevel.getLevelType());
            if (Objects.nonNull(userLevel.getVipLevel())){
                user.setVipLevel(userLevel.getVipLevel());
            }
            //升级奖励计算(积分，优惠券，商品)
            boolean isVip = levelUpRewards(userLevelLog, user, userScoreLogs, userScoreGetLogs, levelLogs, couponIds, beforeLevel, levelType, userLevel);
            // 原本就是会员则退出循环
            if (isVip) {
                break;
            }
        }
        //修改扩展表信息
        UserExtension extension = userExtensionService.getByUserExtensionId(user.getUserExtensionId());
        user.setVersion(extension.getVersion());
        userExtensionService.update(user);
        // 清除用户等级信息
        userService.removeUserCacheByUserId(user.getUserId());

        //保存积分日志、积分明细日志
        if (CollUtil.isNotEmpty(userScoreLogs)) {
            userScoreLogService.saveBatch(userScoreLogs);
            userScoreGetLogService.saveBatch(userScoreGetLogs);
        }
        // 批量保存等级日志
        if (CollUtil.isNotEmpty(levelLogs)) {
            userLevelLogService.batchSaveUserLevelLogs(levelLogs);
        }
        // 消息推送-升级提醒，只有升级时进行提醒

        // 消息推送--升级提醒（未测试）
    }

    /**
     * 等级提升奖励
     *
     * @param userLevelLog     用户等级记录
     * @param user             用户信息
     * @param userScoreLogs    用户积分记录记录
     * @param userScoreGetLogs 用户积分获取记录
     * @param levelLogs        等级记录
     * @param couponIds        优惠券id列表
     * @param beforeLevel      原本的等级
     * @param levelType        等级类型
     * @param userLevel        用户等级
     * @return 该用户原本是否为会员
     */
    private boolean levelUpRewards(UserLevelLog userLevelLog, UserExtension user, List<UserScoreLog> userScoreLogs, List<UserScoreGetLog> userScoreGetLogs, List<UserLevelLog> levelLogs, List<Long> couponIds, Integer beforeLevel, Integer levelType, UserLevelVO userLevel) {
        //1.积分
        UserLevelVO level = userLevelMapper.getOneByTypeAndLevel(user.getLevelType(), user.getLevel());
        UserLevelVO userLevelVO = userLevelMapper.getByUserLevelId(level.getUserLevelId());
        List<UserRightsVO> userRights = userLevelVO.getUserRightsList();
        for (UserRightsVO userRight : userRights) {
            if (userRight.getRightsType() == 3 && userRight.getPresScore() != null && userRight.getPresScore() > 0) {
                UserScoreLog userScoreLog = new UserScoreLog();
                Long score = userRight.getPresScore();
                userScoreLog.setUserId(user.getUserId());
                userScoreLog.setScore(score);
                userScoreLog.setSource(ScoreLogTypeEnum.LEVEL_UP.value());
                userScoreLog.setCreateTime(DateUtil.date());
                userScoreLog.setIoType(1);
                userScoreLogs.add(userScoreLog);
                //添加积分明细
                UserScoreGetLog addDetail = new UserScoreGetLog();
                addDetail.setCreateTime(new Date());
                addDetail.setStatus(1);
                addDetail.setUserId(user.getUserId());
                addDetail.setUsableScore(score);
                userScoreGetLogs.add(addDetail);
                user.setScore(user.getScore() == null ? score : user.getScore() + score);
            }
        }
        //2.优惠券
        if (CollUtil.isNotEmpty(userLevelVO.getCouponIds())) {
            couponIds.addAll(userLevelVO.getCouponIds());
        }
        //3.等级日志,如果不为空则为会员购买提升的等级就直接退出，修改一行
        if (userLevelLog != null) {
            if (!Objects.equals(LevelChangeReasonEnum.RENEW_VIP.value(), userLevelLog.getLevelChangeReason()) && !Objects.equals(LevelChangeReasonEnum.BUY_VIP.value(), userLevelLog.getLevelChangeReason())) {
                // 等级提升日志
                userLevelLog.setIsPayed(1);
                userLevelLog.setLevelIoType(1);
                userLevelLog.setLevelChangeReason(LevelChangeReasonEnum.GROWTH_ENOUGH.value());
                userLevelLog.setBeforeLevel(beforeLevel);
                userLevelLog.setBeforeLevelType(levelType);
                userLevelLog.setAfterLevel(userLevel.getLevel());
                userLevelLog.setAfterLevelType(levelType);
                userLevelLogService.update(userLevelLog);
            }
            return true;
        }
        UserLevelLog levelLog = new UserLevelLog();
        levelLog.setUserId(user.getUserId());
        // 等级提升日志
        levelLog.setLevelIoType(1);
        levelLog.setLevelChangeReason(LevelChangeReasonEnum.GROWTH_ENOUGH.value());
        levelLog.setBeforeLevel(beforeLevel);
        levelLog.setBeforeLevelType(levelType);
        levelLog.setAfterLevel(userLevel.getLevel());
        levelLog.setAfterLevelType(levelType);
        levelLog.setIsPayed(1);
        levelLogs.add(levelLog);
        return false;
    }

    /**
     * 根据成长值获取所在等级
     *
     * @param levelList 等级列表
     * @param growth    成长值
     * @return 等级
     */
    private Integer getUserLevelByGrowth(List<UserLevelVO> levelList, Integer growth) {
        // 最低等级为 1级
        int level = 1;
        if (Objects.isNull(growth)) {
            return level;
        }
        for (UserLevelVO levelParam : levelList) {
            if (Objects.isNull(levelParam.getNeedGrowth())) {
                continue;
            }
            if (growth >= levelParam.getNeedGrowth()) {
                level = levelParam.getLevel();
            }
        }
        return level;
    }


    /**
     * 批量新增积分或者成长值日志
     *
     * @param dbUserExtensions 修改积分或者成长值前的数据
     * @param score            积分
     * @param growth           成长值
     */
    private void batchAddScoreOrGrowthLogs(List<UserExtension> dbUserExtensions, Long score, Integer growth) {
        // 积分日志
        List<UserScoreLog> scoreLogs = new ArrayList<>();
        // 积分明细
        List<UserScoreGetLog> userScoreGetLogs = new ArrayList<>();
        List<UserScoreGetLog> updateUserScoreGetLogs = new ArrayList<>();
        // 成长值
        List<UserGrowthLog> growthLogs = new ArrayList<>();
        // 可以被扣减的用户积分明细,存储的是以userId为key的map集合
        Map<Long, List<UserScoreGetLog>> scoreGetLogMap = new HashMap<>(16);
        if (Objects.nonNull(score) && score < 0L) {
            List<Long> userIds = dbUserExtensions.stream().map(UserExtension::getUserId).collect(Collectors.toList());
            List<UserScoreGetLog> userUsableScoreGetLogs = userScoreGetLogService.batchListByCreateTime(userIds, ScoreGetLogStatusEnum.NORMAL.value());
            scoreGetLogMap = userUsableScoreGetLogs.stream().collect(Collectors.groupingBy(UserScoreGetLog::getUserId));
        }
        DateTime now = DateUtil.date();
        for (UserExtension userExtension : dbUserExtensions) {
            Long userId = userExtension.getUserId();
            Long userScore = userExtension.getScore();
            Integer userGrowth = userExtension.getGrowth();
            // 成长值
            if (Objects.nonNull(growth) && growth.compareTo(0) != 0) {
                UserGrowthLog userGrowthLog = initGrowthLogByParam(growth, now, userId, userGrowth);
                if (Objects.nonNull(userGrowthLog)) {
                    growthLogs.add(userGrowthLog);
                }
            }
            // 积分
            if (Objects.nonNull(score) && score.compareTo(0L) != 0) {
                // 日志
                UserScoreLog scoreLog = initScoreByParam(score, userId, userScore);
                scoreLogs.add(scoreLog);
                // 明细
                UserScoreChangeLogBO userScoreChangeLogBO = handleScoreGetLogs(score, userScore, userId, scoreGetLogMap.get(userId), now);
                List<UserScoreGetLog> saveUserScoreGetLogs = userScoreChangeLogBO.getSaveUserScoreGetLogs();
                List<UserScoreGetLog> updateUserScoreDetailLogs = userScoreChangeLogBO.getUpdateUserScoreGetLogs();
                if (Objects.nonNull(saveUserScoreGetLogs)) {
                    userScoreGetLogs.addAll(saveUserScoreGetLogs);
                }
                if (Objects.nonNull(updateUserScoreDetailLogs)) {
                    updateUserScoreGetLogs.addAll(updateUserScoreDetailLogs);
                }
            }
        }
        if (CollUtil.isNotEmpty(scoreLogs)) {
            userScoreLogService.saveBatch(scoreLogs);
        }
        if (CollUtil.isNotEmpty(userScoreGetLogs)) {
            userScoreGetLogService.saveBatch(userScoreGetLogs);
        }
        if (CollUtil.isNotEmpty(updateUserScoreGetLogs)) {
            userScoreGetLogService.updateBatchById(updateUserScoreGetLogs);
        }
        if (CollUtil.isNotEmpty(growthLogs)) {
            userGrowthLogService.saveBatch(growthLogs);
        }
    }

    /**
     * @param score            扣减的积分
     * @param userScore        用户已有积分
     * @param userId           用户id
     * @param userScoreGetLogs 用户可以扣减的积分明细
     * @param now              当前时间
     * @return 新增的积分明细
     */
    private UserScoreChangeLogBO handleScoreGetLogs(Long score, Long userScore, Long userId, List<UserScoreGetLog> userScoreGetLogs, DateTime now) {
        UserScoreChangeLogBO userScoreChangeLogBO = new UserScoreChangeLogBO();
        if (score == 0L) {
            return userScoreChangeLogBO;
        }
        // 如果用户本身的积分是0 ，再减少用户积分，是不会减少用户积分的，积分日志也是没有的
        if (Objects.nonNull(userScore) && score < 0L && userScore <= 0L) {
            return userScoreChangeLogBO;
        }
        List<UserScoreGetLog> saveUserScoreGetLogs = new ArrayList<>();
        List<UserScoreGetLog> updateUserScoreGetLogs = new ArrayList<>();
        if (score < 0L) {
            // 实际减少的积分
            if (Objects.nonNull(userScore)) {
                score = userScore + score < 0 ? userScore : Math.abs(score);
            }
            // 如果是负的则表示为减少积分
            // 修改积分明细，如果当前明细不够扣除在进行下一条
            // 如果够添加一条积分明细记录
            for (UserScoreGetLog scoreDetail : userScoreGetLogs) {
                if (scoreDetail.getUsableScore() <= score) {
                    scoreDetail.setStatus(ScoreGetLogStatusEnum.OFFSET_CASH.value());
                    scoreDetail.setUpdateTime(now);
                    updateUserScoreGetLogs.add(scoreDetail);
                    score -= scoreDetail.getUsableScore();
                } else {
                    UserScoreGetLog addDetail = new UserScoreGetLog();
                    addDetail.setCreateTime(scoreDetail.getCreateTime());
                    addDetail.setStatus(ScoreGetLogStatusEnum.OFFSET_CASH.value());
                    addDetail.setUserId(scoreDetail.getUserId());
                    addDetail.setUsableScore(score);
                    saveUserScoreGetLogs.add(addDetail);
                    //
                    scoreDetail.setUsableScore(scoreDetail.getUsableScore() - score);
                    scoreDetail.setUpdateTime(now);
                    updateUserScoreGetLogs.add(scoreDetail);
                    break;
                }
                if (score <= 0) {
                    break;
                }
            }
        } else {
            //添加积分明细
            UserScoreGetLog addDetail = new UserScoreGetLog();
            addDetail.setStatus(ScoreGetLogStatusEnum.NORMAL.value());
            addDetail.setUserId(userId);
            addDetail.setUsableScore(score);
            saveUserScoreGetLogs.add(addDetail);
        }
        userScoreChangeLogBO.setSaveUserScoreGetLogs(saveUserScoreGetLogs);
        userScoreChangeLogBO.setUpdateUserScoreGetLogs(updateUserScoreGetLogs);
        return userScoreChangeLogBO;
    }

    /**
     * 根据参数初始化一个积分明细对象
     *
     * @param score     积分
     * @param now       时间
     * @param userId    用户id
     * @param userScore 用户积分
     * @return 积分明细
     */
    private UserScoreGetLog initScoreGetLogs(Long score, DateTime now, Long userId, Long userScore) {
        UserScoreGetLog scoreGetLog = new UserScoreGetLog();
        scoreGetLog.setUserId(userId);
        if (score.compareTo(0L) < 0) {
            BigDecimal add = new BigDecimal(userScore).add(new BigDecimal(score));
            scoreGetLog.setUsableScore(add.compareTo(new BigDecimal("0")) < 0 ? userScore : Math.abs(score));
            scoreGetLog.setStatus(ScoreGetLogStatusEnum.EXPIRED.value());
            scoreGetLog.setExpireTime(now);
        } else {
            scoreGetLog.setUsableScore(score);
            scoreGetLog.setStatus(ScoreGetLogStatusEnum.NORMAL.value());
        }
        return scoreGetLog;
    }

    /**
     * 根据参数初始化用户积分日志对象
     *
     * @param score     积分
     * @param userId    用户id
     * @param userScore 用户积分
     * @return 积分日志
     */
    private UserScoreLog initScoreByParam(Long score, Long userId, Long userScore) {
        UserScoreLog scoreLog = new UserScoreLog();
        scoreLog.setUserId(userId);
        scoreLog.setSource(ScoreLogTypeEnum.SYSTEM.value());
        long changeScore;
        if (userScore + score < 0) {
            changeScore = userScore;
        } else {
            changeScore = Math.abs(score);
        }
        scoreLog.setScore(changeScore);
        scoreLog.setIoType(score.compareTo(0L) < 0 ? ScoreIoTypeEnum.EXPENDITURE.value() : ScoreIoTypeEnum.INCOME.value());
        return scoreLog;
    }

    /**
     * 根据参数初始化一个成长值交易 对象
     *
     * @param growth     成长值
     * @param now        时间
     * @param userId     用户id
     * @param userGrowth 用户成长值
     * @return 成长值日志
     */
    private UserGrowthLog initGrowthLogByParam(Integer growth, DateTime now, Long userId, Integer userGrowth) {
        // 如果成长值是0，再去减少用户成长值是不可以的，应该没有成长值日志
        if (growth < 0 && userGrowth <= 0) {
            return null;
        }
        UserGrowthLog userGrowthLog = new UserGrowthLog();
        userGrowthLog.setUserId(userId);
        userGrowthLog.setSource(GrowthLogSourceEnum.SYSTEM.value());
        int changeGrowth;
        if (userGrowth + growth < 0) {
            // 有io类型字段
            // changeGrowth = userGrowth
            changeGrowth = -userGrowth;
        } else {
            // 有io类型字段
            // changeGrowth = Math.abs(growth)
            changeGrowth = growth;
        }
        userGrowthLog.setChangeGrowth(changeGrowth);
        userGrowthLog.setRemarks("系统修改用户成长值");
        userGrowthLog.setCreateTime(now);
        userGrowthLog.setUpdateTime(now);
        return userGrowthLog;
    }
}
