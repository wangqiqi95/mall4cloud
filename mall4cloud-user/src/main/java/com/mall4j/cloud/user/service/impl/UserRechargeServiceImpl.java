package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.PrincipalUtil;
import com.mall4j.cloud.user.constant.RechargeIoTypeEnum;
import com.mall4j.cloud.user.constant.RechargeTypeEnum;
import com.mall4j.cloud.user.dto.RechargeCouponDTO;
import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.dto.UserRechargeDTO;
import com.mall4j.cloud.user.model.*;
import com.mall4j.cloud.user.mapper.UserRechargeMapper;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.service.UserRechargeCouponService;
import com.mall4j.cloud.user.service.UserBalanceLogService;
import com.mall4j.cloud.user.service.UserRechargeService;
import com.mall4j.cloud.user.vo.UserRechargeVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 余额充值级别表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@Service
public class UserRechargeServiceImpl implements UserRechargeService {

    @Autowired
    private UserRechargeMapper userRechargeMapper;
    @Autowired
    private UserExtensionService userExtensionService;
    @Autowired
    private UserBalanceLogService userBalanceLogService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private UserRechargeCouponService userRechargeCouponService;

    @Override
    public PageVO<UserRecharge> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userRechargeMapper.list());
    }

    @Override
    @Cacheable(cacheNames = CacheNames.USER_RECHARGE_INFO, key = "#rechargeId")
    public UserRechargeVO getRechargeInfo(Long rechargeId) {
        UserRechargeVO userRechargeVO = userRechargeMapper.getRechargeInfo(rechargeId);
        return userRechargeVO;
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.USER_RECHARGE_LIST, allEntries = true)
    public void save(UserRecharge userRecharge) {
        userRechargeMapper.save(userRecharge);
    }

    @Override
    public void update(UserRecharge userRecharge) {
        userRechargeMapper.update(userRecharge);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long rechargeId) {
        userRechargeMapper.deleteById(rechargeId);
        userRechargeCouponService.deleteById(rechargeId);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.USER_RECHARGE_LIST)
    public List<UserRechargeVO> list() {
        return userRechargeMapper.list();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.USER_RECHARGE_LIST, allEntries = true),
            @CacheEvict(cacheNames = CacheNames.USER_RECHARGE_INFO, key = "#rechargeId")})
    public void removeCacheByRechargeId(Long rechargeId) {
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchUpdateUserBalance(UserAdminDTO userAdminDTO) {
        BigDecimal balance= userAdminDTO.getBalanceValue();
        if (!PrincipalUtil.isMaximumOfTwoDecimal(balance.toString())) {
            throw new LuckException("最多是保留两位小数的数值");
        }
        // 将元转为分
        long balanceValue = PriceUtil.toLongCent(balance);
        List<Long> userIds = userAdminDTO.getUserIds();
        if (Objects.equals(0L,balanceValue) || CollUtil.isEmpty(userIds)){
            return false;
        }
        DateTime now = DateUtil.date();
        // 批量给用户充值
        List<UserExtension> extensions = userExtensionService.getByUserIdsAndLevelType(userIds);
        List<Long> userIdsUpdate =extensions.stream().map(UserExtension::getUserId).collect(Collectors.toList());
        List<UserBalanceLog> userRechargeLogs = new ArrayList<>();
        for (UserExtension extension : extensions) {
            if (Objects.isNull(extension)){
                continue;
            }
            // 用户改变的余额数值
            long changeBalance = balanceValue;
            long totalBalance = Objects.nonNull(extension.getActualBalance())? extension.getActualBalance(): 0L;
            int compare = Long.compare(PriceUtil.MAX_CENT - extension.getBalance(), balanceValue);
            // 用户余额的balance的最大值为Long.MAX_VALUE，当用户余额 + 新增余额达到这个值的时候，用户将不能在继续充值了
            if (compare < 0 && userIds.size() == 1) {
                throw new LuckException("当前用户余额已达到最大限额，剩余可充值余额为：" + PriceUtil.toDecimalPrice(PriceUtil.MAX_CENT - extension.getBalance()));
            } else if (compare < 0 && userIds.size() > 1)  {
                continue;
            }
            long userBalance = totalBalance + balanceValue;
            if(userBalance < 0){
                //修改后的金额 < 0 ，那么减少的金额就是用户原本的余额
                changeBalance = totalBalance;
            }
            else if (userBalance > Constant.MAX_USER_BALANCE) {
                changeBalance = Constant.MAX_USER_BALANCE - totalBalance;
            }
            // 添加日志
            UserBalanceLog userRechargeLog = new UserBalanceLog();
            userRechargeLog.setUserId(extension.getUserId());
            userRechargeLog.setChangeBalance(changeBalance);
            userRechargeLog.setIoType(balanceValue>0? RechargeIoTypeEnum.INCOME.value():RechargeIoTypeEnum.EXPENDITURE.value());
            userRechargeLog.setType(RechargeTypeEnum.SYSTEM.value());
            userRechargeLogs.add(userRechargeLog);
        }
        if (CollUtil.isEmpty(userIdsUpdate)) {
            return false;
        }
        userExtensionService.updateBatchUserBalanceByUserIds(userIdsUpdate,balanceValue,now);
        userBalanceLogService.saveBatch(userRechargeLogs);
        return true;
    }

    @Override
    public UserRechargeVO getRechargeByRechargeId(Long rechargeId, Integer putOnStatus) {
        if (Objects.isNull(userRechargeMapper.getByRechargeId(rechargeId))){
            throw new LuckException("数据状态错误，请刷新重试");
        }
        UserRechargeVO userRechargeVO = getRechargeInfo(rechargeId);
        return userRechargeVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNames.USER_RECHARGE_LIST, allEntries = true)
    public boolean saveRecharge(UserRechargeDTO userRechargeDTO) {
        UserRecharge userRecharge = mapperFacade.map(userRechargeDTO, UserRecharge.class);
        userRechargeMapper.save(userRecharge);
        if (CollUtil.isEmpty(userRechargeDTO.getCouponList())){
            return false;
        }
        userRechargeCouponService.insertBatch(userRecharge.getRechargeId(),userRechargeDTO.getCouponList());
        return true;
    }

    @Override
    public void updateByRechargeId(UserRechargeDTO userRechargeDTO) {
        if (Objects.isNull(userRechargeMapper.getByRechargeId(userRechargeDTO.getRechargeId()))){
            throw new LuckException("数据状态错误，请刷新重试");
        }
        UserRecharge userRecharge = mapperFacade.map(userRechargeDTO, UserRecharge.class);
        userRechargeMapper.update(userRecharge);
        UserRechargeVO userRechargeDb = getRechargeByRechargeId(userRecharge.getRechargeId(), null);
        List<RechargeCouponDTO> addCoupon = Lists.newArrayList();
    }
}
