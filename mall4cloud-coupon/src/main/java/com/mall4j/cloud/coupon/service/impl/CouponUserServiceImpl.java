package com.mall4j.cloud.coupon.service.impl;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.coupon.bo.CouponGiveBO;
import com.mall4j.cloud.api.coupon.bo.CouponIdAndCountBO;
import com.mall4j.cloud.api.coupon.constant.CouponPutOnStatus;
import com.mall4j.cloud.api.coupon.dto.BindCouponDTO;
import com.mall4j.cloud.api.coupon.vo.CouponUserCountDataVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.CouponStatus;
import com.mall4j.cloud.coupon.constant.ValidTimeType;
import com.mall4j.cloud.coupon.dto.CouponUserCountDTO;
import com.mall4j.cloud.coupon.dto.CouponUserDTO;
import com.mall4j.cloud.coupon.mapper.CouponGiveLogMapper;
import com.mall4j.cloud.coupon.mapper.CouponLockMapper;
import com.mall4j.cloud.coupon.mapper.CouponMapper;
import com.mall4j.cloud.coupon.mapper.CouponUserMapper;
import com.mall4j.cloud.coupon.model.Coupon;
import com.mall4j.cloud.coupon.model.CouponGiveLog;
import com.mall4j.cloud.coupon.model.CouponLock;
import com.mall4j.cloud.coupon.model.CouponUser;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.service.CouponUserService;
import com.mall4j.cloud.coupon.vo.CouponAppVO;
import com.mall4j.cloud.coupon.vo.CouponUserVO;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 优惠券用户关联信息
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
@Service
public class CouponUserServiceImpl implements CouponUserService {

    @Autowired
    private CouponUserMapper couponUserMapper;
    @Autowired
    private CouponGiveLogMapper couponGiveLogMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponLockMapper couponLockMapper;

    @Override
    public PageVO<CouponUserVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> couponUserMapper.list());
    }

    @Override
    public CouponUserVO getByCouponUserId(Long couponUserId) {
        return couponUserMapper.getByCouponUserId(couponUserId);
    }

    @Override
    public void save(CouponUser couponUser) {
        couponUserMapper.save(couponUser);
    }

    @Override
    public void update(CouponUser couponUser) {
        couponUserMapper.update(couponUser);
    }

    @Override
    public void deleteById(Long couponUserId) {
        couponUserMapper.deleteById(couponUserId);
    }

    @Override
    public int getUserHasCouponCount(Long couponId, Long userId) {
        return couponUserMapper.getUserHasCouponCount(couponId, userId);
    }

    @Override
    public void saveBatch(List<CouponUser> couponUsers) {
        couponUserMapper.saveBatch(couponUsers);
    }

    @Override
    public int getCouponCountForUser(Long couponId, Long userId) {
        return couponUserMapper.getCouponCountForUser(couponId, userId);
    }

    @Override
    public List<CouponUserCountDTO> getCouponsCountForUsers(List<Long> couponIds, List<Long> userIds) {
        return couponUserMapper.getCouponsCountForUsers(couponIds, userIds);
    }

    @Override
    public void deleteUserCouponByCouponId(Long couponUserId) {
        couponUserMapper.deleteUserCouponByCouponId(AuthUserContext.get().getUserId(), couponUserId);
    }

    @Override
    public Map<String, Long> getCouponCountByStatus(Long userId) {
        return couponUserMapper.getCouponCountByStatus(userId);
    }

    @Override
    public void listByAndShopIdOrSpuId(List<CouponAppVO> coupons, List<Long> couponIds) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (CollectionUtil.isEmpty(coupons) || Objects.isNull(userInfoInTokenBO)) {
            return ;
        }
        List<CouponUserCountDTO> couponAppList = couponUserMapper.listByAndShopIdOrSpuId(userInfoInTokenBO.getUserId(), couponIds);
        Map<Long, CouponUserCountDTO> couponUserMap = couponAppList.stream().collect(Collectors.toMap(CouponUserCountDTO::getCouponId, c -> c));
        Iterator<CouponAppVO> iterator = coupons.iterator();
        while (iterator.hasNext()) {
            CouponAppVO coupon = iterator.next();
            coupon.setSpus(null);
            if (!couponUserMap.containsKey(coupon.getCouponId())) {
                continue;
            }
            CouponUserCountDTO couponUserCount = couponUserMap.get(coupon.getCouponId());
            if (couponUserCount.getReceiveNum() >= coupon.getLimitNum()) {
                iterator.remove();
                continue;
            }
            if (Objects.nonNull(couponUserCount.getNum()) && couponUserCount.getNum() > 0) {
                coupon.setHasReceive(couponUserCount.getNum());
            }
        }
        coupons.sort(Comparator.comparing(CouponAppVO::getHasReceive));
    }

    @Override
    public Integer countCanUseCoupon(Long userId) {
        return couponUserMapper.countCanUseCoupon(userId);
    }

    @Override
    public CouponUserCountDataVO countCouponUserByUserId(Long userId) {
        return couponUserMapper.countCouponUserByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void giveCoupon(CouponGiveBO couponGiveBO) {
        Integer bizType = couponGiveBO.getBizType();
        Long bizId = couponGiveBO.getBizId();
        Integer count = couponGiveLogMapper.count(bizType, bizId);
        if (count > 0) {
            return;
        }
        Long userId = couponGiveBO.getUserId();
        List<CouponIdAndCountBO> couponInfos = couponGiveBO.getCouponInfos();
        // 优惠券id
        List<Long> couponIds = couponInfos.stream().map(CouponIdAndCountBO::getCouponId).collect(Collectors.toList());
        Map<Long, Integer> couponIdWithCountMap = couponInfos.stream().collect(Collectors.toMap(CouponIdAndCountBO::getCouponId, CouponIdAndCountBO::getCount));
        // 优惠券
        List<Coupon> coupons = couponMapper.getListByCouponIds(couponIds);

        List<CouponUser> couponUsers = new ArrayList<>();

        for (Coupon coupon : coupons) {
            if (!Objects.equals(coupon.getStatus(), CouponStatus.NO_OVERDUE.value()) || !Objects.equals(coupon.getPutonStatus(), CouponPutOnStatus.PLACED.value())) {
                return;
            }
            // 判断优惠券当前数量，给用户发放优惠券
            Integer couponNum = couponIdWithCountMap.get(coupon.getCouponId());
            // 优惠券数量有限，送完即止
            if (coupon.getStocks()< couponNum) {
                couponNum = coupon.getStocks();
            }
            // 根据赠送的数量给用户发优惠券
            for (int i = 0; i < couponNum; i++) {
                CouponUser couponUser = new CouponUser();
                couponUser.setCouponId(coupon.getCouponId());
                couponUser.setUserId(userId);
                couponUser.setReceiveTime(new Date());
                couponUser.setStatus(1);
                if (Objects.equals(coupon.getValidTimeType(), ValidTimeType.FIXED.value())) {
                    // 优惠券已过期
                    if (coupon.getEndTime().getTime() < System.currentTimeMillis()) {
                        couponUser.setStatus(0);
                    }
                    couponUser.setUserStartTime(coupon.getStartTime());
                    couponUser.setUserEndTime(coupon.getEndTime());

                }
                // 生效时间类型为领取后生效
                if (Objects.equals(coupon.getValidTimeType(), ValidTimeType.RECEIVE.value())) {
                    if (coupon.getAfterReceiveDays() == null) {
                        coupon.setAfterReceiveDays(0);
                    }
                    if (coupon.getValidDays() == null) {
                        coupon.setValidDays(0);
                    }
                    couponUser.setUserStartTime(new Date());
                    couponUser.setUserEndTime(DateUtils.addDays(DateUtil.endOfDay(new Date()), coupon.getAfterReceiveDays()));
                }
                couponUser.setIsDelete(0);
                couponUsers.add(couponUser);
            }
        }

        couponUserMapper.saveBatch(couponUsers);
        // 这里有唯一索引，所以不用怕重复
        CouponGiveLog couponGiveLog = new CouponGiveLog();
        couponGiveLog.setBizId(bizId);
        couponGiveLog.setBizType(bizType);
        couponGiveLogMapper.save(couponGiveLog);
    }

    @Override
    public void deleteUnValidTimeCoupons(Date date) {
        couponUserMapper.deleteUnValidTimeCoupons(date);
    }

    @Override
    public void updateStatusByTime() {
        couponUserMapper.updateStatusByTime();
    }

    @Override
    public PageVO<CouponUserVO> getPageByUserId(PageDTO pageDTO, CouponUserDTO couponUserDTO) {
        return PageUtil.doPage(pageDTO, () -> couponUserMapper.getPageByUserId(couponUserDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchBindCoupon(List<BindCouponDTO> bindCouponDTOList) {
        if (CollUtil.isEmpty(bindCouponDTOList)) {
            return false;
        }
        Set<Long> couponIds = new HashSet<>();
        for (BindCouponDTO bindCouponDTO : bindCouponDTOList) {
            couponIds.addAll(bindCouponDTO.getCouponIds());
        }
        // 优惠券
        List<Coupon> coupons = couponMapper.getListByCouponIds(new ArrayList<>(couponIds));
        Map<Long, Coupon> couponMap = coupons.stream().collect(Collectors.toMap(Coupon::getCouponId, (k) -> k));
        List<CouponUser> couponUsers = new ArrayList<>();
        Date now = new Date();
        // 统计用户已经领取了多少个优惠券（领券有限制）
        List<Long> userIds = bindCouponDTOList.stream().map(BindCouponDTO::getUserId).collect(Collectors.toList());
        List<CouponUserCountDTO> couponsCountForUsers = getCouponsCountForUsers(new ArrayList<>(couponIds), userIds);
        // 优惠券发了多少个
        Map<Long, Integer> couponSendMap = new HashMap<>(16);
        for (BindCouponDTO bindCouponDTO : bindCouponDTOList) {
            Long userId = bindCouponDTO.getUserId();
            List<Long> userCouponIds = bindCouponDTO.getCouponIds();
            for (Long couponId : userCouponIds) {
                Coupon coupon = couponMap.get(couponId);
                // 超出领券数量限制，就不要继续发给用户
                boolean overLimit = judgeOverLimit(couponsCountForUsers, couponSendMap, coupon, userId, couponId);
                if (overLimit) {
                    continue;
                }
                CouponUser couponUser = new CouponUser();
                couponUser.setCouponId(couponId);
                couponUser.setUserId(userId);
                couponUser.setReceiveTime(now);
                couponUser.setStatus(1);
                couponUser.setIsDelete(0);
                if (Objects.equals(ValidTimeType.FIXED.value(), coupon.getValidTimeType())) {
                    // 优惠券已过期
                    if (coupon.getEndTime().getTime() < System.currentTimeMillis()) {
                        couponUser.setStatus(0);
                    }
                    couponUser.setUserStartTime(coupon.getStartTime());
                    couponUser.setUserEndTime(coupon.getEndTime());

                }
                // 生效时间类型为领取后生效
                if (Objects.equals(ValidTimeType.RECEIVE.value(), coupon.getValidTimeType())) {
                    if (Objects.isNull(coupon.getAfterReceiveDays())) {
                        coupon.setAfterReceiveDays(0);
                    }
                    if (coupon.getValidDays() == null) {
                        coupon.setValidDays(0);
                    }
                    couponUser.setUserStartTime(new Date());
                    couponUser.setUserEndTime(DateUtils.addDays(DateUtil.endOfDay(new Date()), coupon.getAfterReceiveDays()));
                }
                if (couponSendMap.containsKey(couponId)) {
                    couponSendMap.put(couponId,couponSendMap.get(couponId) + 1);
                } else {
                    couponSendMap.put(couponId,1);
                }
                couponUsers.add(couponUser);
            }
        }
        if (CollUtil.isNotEmpty(couponUsers)) {
            couponUserMapper.saveBatch(couponUsers);
            List<Coupon> couponsUpdate = getUpdateCouponStock(couponSendMap, true);
            couponMapper.batchUpdateCouponStocks(couponsUpdate);
            // 清除优惠券部分缓存
            for (Long couponId : couponSendMap.keySet()) {
                couponService.removeCacheByShopId(null,couponId);
            }
            return true;
        }
        return false;
    }

    private boolean judgeOverLimit(List<CouponUserCountDTO> couponsCountForUsers, Map<Long, Integer> couponSendMap, Coupon coupon, Long userId, Long couponId) {
        if (Objects.isNull(coupon)) {
            return true;
        }
        if (!(Objects.equals(coupon.getStatus(), CouponStatus.NO_OVERDUE.value()) && Objects.equals(coupon.getPutonStatus(), CouponPutOnStatus.PLACED.value()))) {
            return true;
        }
        if (couponSendMap.containsKey(couponId) && couponSendMap.get(couponId) >= coupon.getStocks()) {
            return true;
        }
        // 超出领券数量限制
        int num = 0;
        if (CollUtil.isNotEmpty(couponsCountForUsers)) {
            CouponUserCountDTO couponUserCountDTO = couponsCountForUsers.stream().filter(item -> Objects.equals(userId, item.getUserId()) && Objects.equals(couponId, item.getCouponId())).findAny().orElse(null);
            if (Objects.nonNull(couponUserCountDTO)) {
                num = couponUserCountDTO.getNum();
            }
        }
        if (num >= coupon.getLimitNum()) {
            return true;
        }
        if (couponSendMap.containsKey(couponId) && (couponSendMap.get(couponId) + num) >= coupon.getLimitNum()) {
            return true;
        }
        return false;
    }

    /**
     * 将减少优惠券的map转为数组集合
     * @param couponSendMap 发送优惠券的数量
     * @param isReduce 是否减少 true 减少 false增加
     * @return 修改的优惠券数量
     */
    private List<Coupon> getUpdateCouponStock(Map<Long, Integer> couponSendMap, boolean isReduce) {
        List<Coupon> couponsUpdate = Lists.newArrayList();
        for (Long couponId : couponSendMap.keySet()) {
            Integer count = couponSendMap.get(couponId);
            Coupon update = new Coupon();
            update.setCouponId(couponId);
            update.setStocks(isReduce ? -count : count);
            couponsUpdate.add(update);
        }
        return couponsUpdate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUserCoupon(List<BindCouponDTO> bindCouponDTOList) {
        // 删除用户优惠券
        // 可能有很多的优惠券，把用户有效的未使用的优惠券删除了
        if (CollUtil.isEmpty(bindCouponDTOList)) {
            return false;
        }
        Set<Long> couponIds = new HashSet<>();
        for (BindCouponDTO bindCouponDTO : bindCouponDTOList) {
            couponIds.addAll(bindCouponDTO.getCouponIds());
        }
        List<Long> userIds = bindCouponDTOList.stream().map(BindCouponDTO::getUserId).distinct().collect(Collectors.toList());
        // 将用户有效的优惠券筛选出来
        List<CouponUser> list = couponUserMapper.getByCouponIdsAndUserIds(userIds,new ArrayList<>(couponIds));
        if (CollUtil.isEmpty(list)) {
            return true;
        }
        Map<Long, List<CouponUser>> couponUserMap = list.stream().collect(Collectors.groupingBy(CouponUser::getUserId));
        // 筛选出待确认，已锁定的优惠券是哪些
        List<Long> couponUserIdList = list.stream().map(CouponUser::getCouponUserId).collect(Collectors.toList());
        List<CouponLock> couponLocks = couponLockMapper.getUnConfirmAndLockCoupon(couponUserIdList);
        List<Long> unCouponUserIds = couponLocks.stream().map(CouponLock::getCouponUserId).distinct().collect(Collectors.toList());
        // 筛选出可以被删除的优惠券
        List<Long> couponUserIds = couponUserIdList.stream().filter(item ->!unCouponUserIds.contains(item)).collect(Collectors.toList());
        if (CollUtil.isEmpty(couponUserIds)) {
            return true;
        }
        // 优惠券退还数量
        Map<Long, Integer> couponReturnStockMap = new HashMap<>(16);
        // 处理需要删除用户的优惠券是那些
        Set<Long> couponUserIdDeleteList = new HashSet<>();
        for (BindCouponDTO bindCouponDTO : bindCouponDTOList) {
            Long userId = bindCouponDTO.getUserId();
            List<Long> couponIdList = bindCouponDTO.getCouponIds();
            List<CouponUser> couponUsers = couponUserMap.get(userId);
            for (CouponUser couponUser : couponUsers) {
                Long couponId = couponUser.getCouponId();
                Long couponUserId = couponUser.getCouponUserId();
                // 优惠券不在已解锁状态，就不能删除，直接跳过
                if (!couponUserIds.contains(couponUserId)) {
                    continue;
                }
                if (couponIdList.contains(couponId)) {
                    couponUserIdDeleteList.add(couponUserId);
                    // 移除要被删除的优惠券的id列表中的其中一个
                    couponIdList.remove(couponId);
                    if (couponReturnStockMap.containsKey(couponId)) {
                        couponReturnStockMap.put(couponId,couponReturnStockMap.get(couponId) + 1);
                    } else {
                        couponReturnStockMap.put(couponId,1);
                    }
                }
            }
            List<CouponUser> collect = couponUsers.stream().filter(item -> !couponUserIdDeleteList.contains(item.getCouponUserId())).collect(Collectors.toList());
            couponUserMap.put(userId,collect);
        }
        if (CollUtil.isEmpty(couponUserIdDeleteList)) {
            return true;
        }
        DateTime now = DateUtil.date();
        couponUserMapper.batchDeleteUserCoupon(new ArrayList<>(couponUserIdDeleteList), now);
        // 还原库存
        List<Coupon> updateCouponStock = getUpdateCouponStock(couponReturnStockMap, false);
        couponMapper.batchUpdateCouponStocks(updateCouponStock);
        // 清除优惠券部分缓存
        for (Long couponId : couponReturnStockMap.keySet()) {
            couponService.removeCacheByShopId(null,couponId);
        }
        return true;
    }

}
