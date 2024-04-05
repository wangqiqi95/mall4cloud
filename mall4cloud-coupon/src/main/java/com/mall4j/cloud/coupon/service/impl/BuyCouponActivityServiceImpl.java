package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.coupon.constant.ActivityDelEnum;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.constant.ActivityStatusEnum;
import com.mall4j.cloud.coupon.constant.TCouponStatus;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.mapper.*;
import com.mall4j.cloud.coupon.model.*;
import com.mall4j.cloud.coupon.service.BuyCouponActivityService;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class BuyCouponActivityServiceImpl implements BuyCouponActivityService {
    @Resource
    private BuyCouponActivityMapper buyCouponActivityMapper;
    @Resource
    private BuyActivityCouponMapper buyActivityCouponMapper;
    @Resource
    private BuyActivityShopMapper buyActivityShopMapper;
    @Resource
    private BuyInventoryLogMapper buyInventoryLogMapper;
    @Resource
    private TCouponUserService tCouponUserService;
    @Resource
    private BuyCouponLogMapper buyCouponLogMapper;
    @Resource
    private TCouponShopMapper tCouponShopMapper;
    @Resource
    private TCouponUserMapper tCouponUserMapper;


    @Override
    public ServerResponseEntity<PageInfo<ActivityListVO>> list(ActivityListDTO param) {
        log.info("现金买券活动列表的搜索条件为 :{}", param);
        PageInfo<ActivityListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                buyCouponActivityMapper.list(param)
        );

        //处理返回值
        List<ActivityListVO> list = result.getList();
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(temp ->{
                if (temp.getStatus().equals(0)){
                    //活动状态
                    if (System.currentTimeMillis() < temp.getStartTime().getTime()){
                        temp.setActivityStatus(ActivityStatusEnum.NOT_START.desc());
                        temp.setActivityStatusKey(ActivityStatusEnum.NOT_START.value());
                    }else if (System.currentTimeMillis() > temp.getStartTime().getTime() && System.currentTimeMillis() < temp.getEndTime().getTime()){
                        temp.setActivityStatus(ActivityStatusEnum.START.desc());
                        temp.setActivityStatusKey(ActivityStatusEnum.START.value());
                    }else if (System.currentTimeMillis() > temp.getEndTime().getTime()){
                        temp.setActivityStatus(ActivityStatusEnum.OVER.desc());
                        temp.setActivityStatusKey(ActivityStatusEnum.OVER.value());
                    }
                }else {
                    temp.setActivityStatus(ActivityStatusEnum.DISABLE.desc());
                    temp.setActivityStatusKey(ActivityStatusEnum.DISABLE.value());
                }
                //适用门店数量
                if (!temp.getIsAllShop()){
                    Integer shopNum = shopNum(temp.getId());
                    temp.setShopNum(shopNum);
                    //适用门店
                    List<Long> shops = shops(temp.getId());
                    temp.setShops(shops);
                }
            });
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> save(BuyActivityDTO param) {
        log.info("新增现金买券活动的参数为 :{}", param);
        //插入基础信息
        BuyCouponActivity buyCouponActivity = BeanUtil.copyProperties(param, BuyCouponActivity.class);
        buyCouponActivity.setCreateId(AuthUserContext.get().getUserId());
        buyCouponActivity.setCreateName(AuthUserContext.get().getUsername());
        buyCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        buyCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        buyCouponActivityMapper.insert(buyCouponActivity);

        //组装关联门店信息
        List<Long> shops = param.getShops();
        addShops(shops, buyCouponActivity.getId());

        //组装关联优惠券信息
        List<BuyActivityCouponDTO> coupons = param.getCoupons();
        addCoupons(coupons, buyCouponActivity.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<BuyActivityDetailVO> detail(Long id) {
        BuyCouponActivity buyCouponActivity = buyCouponActivityMapper.selectById(id);
        BuyActivityDetailVO result = BeanUtil.copyProperties(buyCouponActivity, BuyActivityDetailVO.class);
        if (!result.getIsAllShop()){
            //适用门店数量
            Integer shopNum = shopNum(result.getId());
            result.setShopNum(shopNum);
            //适用门店
            List<Long> shops = shops(result.getId());
            result.setShops(shops);
        }
        //关联优惠券信息
        List<BuyActivityCouponVO> buyActivityCouponVOS = buyActivityCouponMapper.couponList(id);
        result.setCoupons(buyActivityCouponVOS);

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> delete(Long id) {
        BuyCouponActivity buyCouponActivity = new BuyCouponActivity();
        buyCouponActivity.setId(id);
        buyCouponActivity.setDel(ActivityDelEnum.DEL.value());
        buyCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        buyCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        buyCouponActivityMapper.updateById(buyCouponActivity);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(BuyActivityDTO param) {
        log.info("修改现金买券活动的参数为 :{}", param);
        //修改基本信息
        BuyCouponActivity buyCouponActivity = BeanUtil.copyProperties(param, BuyCouponActivity.class);
        buyCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        buyCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        buyCouponActivityMapper.updateById(buyCouponActivity);

        //删除原有门店信息
        buyActivityShopMapper.delete(new LambdaUpdateWrapper<BuyCouponActivityShop>().eq(BuyCouponActivityShop::getActivityId,param.getId()));
        //新增关联门店信息
        List<Long> shops = param.getShops();
        addShops(shops,param.getId());

        //删除原有关联优惠券信息
        buyActivityCouponMapper.delete(new LambdaUpdateWrapper<BuyCouponActivityCoupon>().eq(BuyCouponActivityCoupon::getActivityId,param.getId()));
        //组装关联优惠券信息
        List<BuyActivityCouponDTO> coupons = param.getCoupons();
        addCoupons(coupons, param.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateStatus(Long id, Short status) {
        BuyCouponActivity buyCouponActivity = new BuyCouponActivity();
        buyCouponActivity.setId(id);
        buyCouponActivity.setStatus(status);
        buyCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        buyCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        buyCouponActivityMapper.updateById(buyCouponActivity);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<BuyActivityCouponVO>> ActivityCouponList(ActivityCouponListVO param) {
        PageInfo<BuyActivityCouponVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                buyActivityCouponMapper.couponList(param.getActivityId())
        );
        //处理返回值
        List<BuyActivityCouponVO> list = result.getList();
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(temp ->{
                if (temp.getTimeType() == 0){
                    if (temp.getStatus().equals(TCouponStatus.OVERDUE.value().toString())){
                        //活动状态
                        if (System.currentTimeMillis() < temp.getStartTime().getTime()){
                            temp.setStatus(TCouponStatus.NO_OVERDUE.desc());
                        }else if (System.currentTimeMillis() > temp.getStartTime().getTime() && System.currentTimeMillis() < temp.getEndTime().getTime()){
                            temp.setStatus(TCouponStatus.OVERDUE.desc());
                        }else if (System.currentTimeMillis() > temp.getEndTime().getTime()){
                            temp.setStatus(TCouponStatus.DEL.desc());
                        }
                    }
                }
            });
        }

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> addInventory(AddInventoryDTO param) {
        BuyCouponActivityCoupon buyCouponActivityCoupon = buyActivityCouponMapper.selectById(param.getId());
        buyCouponActivityCoupon.setStocks(buyCouponActivityCoupon.getStocks() + param.getNum());
        buyActivityCouponMapper.updateById(buyCouponActivityCoupon);

        //记录库存调整日志
        BuyInventoryLog buyInventoryLog = BeanUtil.copyProperties(param, BuyInventoryLog.class);
        buyInventoryLog.setActivityId(buyCouponActivityCoupon.getActivityId());
        buyInventoryLog.setCouponId(buyCouponActivityCoupon.getCouponId());
        buyInventoryLog.setCreateId(AuthUserContext.get().getUserId());
        buyInventoryLog.setCreateCode(AuthUserContext.get().getUserId().toString());
        buyInventoryLog.setCreateName(AuthUserContext.get().getUsername());
        buyInventoryLogMapper.insert(buyInventoryLog);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<InventoryListVO>> inventoryLog(ActivityCouponListVO param) {
        PageInfo<InventoryListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                buyInventoryLogMapper.list(param.getActivityId())
        );

        return ServerResponseEntity.success(result);
    }


    @Override
    public ServerResponseEntity<Void> deleteShop(Long id,Long shopId,Boolean isAllShop) {
        if (isAllShop){
            BuyCouponActivity buyCouponActivity = new BuyCouponActivity();
            buyCouponActivity.setId(id);
            buyCouponActivity.setIsAllShop(true);
            buyCouponActivityMapper.updateById(buyCouponActivity);
            buyActivityShopMapper.delete(new LambdaUpdateWrapper<BuyCouponActivityShop>().eq(BuyCouponActivityShop::getActivityId, id));
        }
        buyActivityShopMapper.delete(new LambdaUpdateWrapper<BuyCouponActivityShop>()
                .eq(BuyCouponActivityShop::getActivityId, id)
                .eq(BuyCouponActivityShop::getShopId, shopId));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> addShops(AddShopsDTO param) {
        List<Long> addShops = param.getShops();
        if (!CollectionUtils.isEmpty(addShops)){
            //现有的门店id
            List<Long> oldShops = shops(param.getId());
            addShops.removeAll(oldShops);
            addShops(addShops,param.getId());
        }else {
            throw new LuckException("添加门店不能为空");
        }
        return ServerResponseEntity.success();
    }


    @Override
    public ServerResponseEntity<List<AppBuyActivityVO>> appBuyActivity() {
        List<AppBuyActivityVO> appBuyActivityVOS = buyCouponActivityMapper.appList(AuthUserContext.get().getStoreId());
        return ServerResponseEntity.success(appBuyActivityVOS);
    }

    @Override
    public ServerResponseEntity<AppBuyActivityDetailVO> appBuyActivityDetail(Long id) {
        BuyCouponActivity buyCouponActivity = buyCouponActivityMapper.selectById(id);
        AppBuyActivityDetailVO result = BeanUtil.copyProperties(buyCouponActivity, AppBuyActivityDetailVO.class);
        //优惠券详情
        List<AppBuyCouponVO> appBuyCouponVOS = buyActivityCouponMapper.appCouponList(id);
        result.setCoupons(appBuyCouponVOS);
        //适用门店
        List<BuyCouponActivityShop> buyCouponActivityShops = buyActivityShopMapper.selectList(new LambdaQueryWrapper<BuyCouponActivityShop>().eq(BuyCouponActivityShop::getActivityId, id));
        List<Long> shops = buyCouponActivityShops.stream().map(BuyCouponActivityShop::getShopId).collect(Collectors.toList());
        result.setShops(shops);

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<AppBuyCouponDetailVO> appCouponDetail(Long id) {
        AppBuyCouponDetailVO result = buyActivityCouponMapper.appCouponDetail(id);
        List<TCouponShop> couponShops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, result.getCouponId()));
        result.setShops(couponShops.stream().map(TCouponShop::getShopId).collect(Collectors.toList()));
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Long> buyCoupon(Long id,Long couponId,Long storeId) {
        //校验活动状态（是否存在，活动时间，状态）
        BuyCouponActivity activity = buyCouponActivityMapper.selectById(id);
        if (ObjectUtil.isEmpty(activity)) {
            throw new LuckException("现金买券活动不存在，无法购买优惠券！");
        }
        if (activity.getStatus() == ActivityStatusEnum.DISABLE.value()) {
            throw new LuckException("现金买券活动已停用，无法购买优惠券！");
        }
        if (System.currentTimeMillis() < activity.getStartTime().getTime()) {
            throw new LuckException("现金买券活动未开始，无法购买优惠券！");
        }
        if (System.currentTimeMillis() > activity.getEndTime().getTime()) {
            throw new LuckException("现金买券活动已结束，无法购买优惠券！");
        }

        BuyCouponActivityCoupon buyCouponActivityCoupon = buyActivityCouponMapper.selectOne(new LambdaQueryWrapper<BuyCouponActivityCoupon>()
                .eq(BuyCouponActivityCoupon::getActivityId, id)
                .eq(BuyCouponActivityCoupon::getCouponId, couponId));
        //校验库存
        if (buyCouponActivityCoupon.getIsStocksLimit()){
            if (buyCouponActivityCoupon.getStocks() < 1){
                throw new LuckException("库存不足，无法购买优惠券！");
            }
        }
        //校验每人限制领取
        if (buyCouponActivityCoupon.getIsPersonLimit()){
            Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser::getCouponId, couponId)
                    .eq(TCouponUser::getUserId, AuthUserContext.get().getUserId())
                    .eq(TCouponUser::getActivityId, id)
                    .eq(TCouponUser::getActivitySource, ActivitySourceEnum.BUY.value()));
            if (!(buyCouponActivityCoupon.getLimitNum() > count)){
                throw new LuckException("累计次数已用完，无法购买优惠券！");
            }
        }

        //锁定库存
        if (buyActivityCouponMapper.updateCouponStocks(couponId,id,buyCouponActivityCoupon.getVersion()) < 1 ){
            throw new LuckException("优惠券购买失败！");
        }

        //新增购买记录
        BuyCouponLog buyCouponLog = new BuyCouponLog();
        buyCouponLog.setActivityId(id);
        buyCouponLog.setCouponId(couponId);
        buyCouponLog.setShopId(storeId);
        buyCouponLog.setOrderNo(RandomUtil.getUniqueNum());
        buyCouponLog.setPrice(buyCouponActivityCoupon.getPrice());
        buyCouponLog.setPayStatus(1);
        buyCouponLog.setCreateId(AuthUserContext.get().getUserId());
        buyCouponLog.setCreateName(AuthUserContext.get().getUsername());
        buyCouponLogMapper.insert(buyCouponLog);

        return ServerResponseEntity.success(buyCouponLog.getOrderNo());
    }

    @Override
    public ServerResponseEntity<Void> payCoupon(PayCouponDTO param) {

        //修改购买记录
        BuyCouponLog buyCouponLog = buyCouponLogMapper.selectOne(new LambdaQueryWrapper<BuyCouponLog>().eq(BuyCouponLog::getOrderNo, param.getOrderNo()));
        buyCouponLog.setWechatPayNo(param.getWechatPayNo());
        buyCouponLog.setPayStatus(0);
        buyCouponLogMapper.updateById(buyCouponLog);

        //分发优惠券
        ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
        receiveCouponDTO.setCouponId(buyCouponLog.getCouponId());
        receiveCouponDTO.setUserId(buyCouponLog.getCreateId());
        receiveCouponDTO.setActivityId(buyCouponLog.getActivityId());
        receiveCouponDTO.setActivitySource(ActivitySourceEnum.BUY.value());
        receiveCouponDTO.setShopId(buyCouponLog.getShopId());
        receiveCouponDTO.setWechatPayNo(param.getWechatPayNo());
        tCouponUserService.receive(receiveCouponDTO);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<BuyCouponLog>> buyCouponLog(Integer pageNo,Integer pageSize) {
        PageInfo<BuyCouponLog> result = PageHelper.startPage(pageNo, pageSize).doSelectPageInfo(() ->
                buyCouponLogMapper.list(AuthUserContext.get().getUserId())
        );

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<ActivityReportVO> activityReport(ActivityReportDTO param) {
        //=============== 活动信息 ===============
        BuyCouponActivity buyCouponActivity = buyCouponActivityMapper.selectById(param.getActivityId());
        ActivityReportVO result = BeanUtil.copyProperties(buyCouponActivity, ActivityReportVO.class);

        //=============== 券活动概况 ===============
        //统计指标：总领券数
        List<TCouponUser> receiveSum = tCouponUserMapper.indexStatistics(param.getActivityId(), ActivitySourceEnum.BUY.value(), param.getCouponInfo(), null);
        result.setReceiveSum(receiveSum.size());
        //统计指标：总核销数
        List<TCouponUser> writeOffSum = tCouponUserMapper.indexStatistics(param.getActivityId(), ActivitySourceEnum.BUY.value(), param.getCouponInfo(), "USED");
        result.setWriteOffSum(writeOffSum.size());
        //统计指标：活动收入
        if (!CollectionUtils.isEmpty(writeOffSum)){
            long activityIncome = writeOffSum.stream().mapToLong(TCouponUser::getOrderAmount).sum()/100;
//            result.setActivityIncome(activityIncome);
        }else {
//            result.setActivityIncome(0L);
        }
        //统计指标：券过期数
        List<TCouponUser> overdueSum = tCouponUserMapper.indexStatistics(param.getActivityId(), ActivitySourceEnum.BUY.value(), param.getCouponInfo(), "EXPIRED");
        result.setOverdueSum(overdueSum.size());

        //优惠券报表列表
        List<ActivityReportDetailVO> activityReportDetails = buyCouponActivityMapper.activityReportDetail(param.getActivityId(), param.getCouponInfo(), ActivitySourceEnum.BUY.value());

        activityReportDetails.forEach(temp ->{
            if(temp.getWriteOffNum() == 0){
                temp.setWriteOffRatio(new BigDecimal("0"));
            }else {
                BigDecimal writeOffRatio = NumberUtil.div(String.valueOf(temp.getWriteOffNum()), String.valueOf(temp.getReceiveNum()));
                temp.setWriteOffRatio(writeOffRatio);
            }
//            if (temp.getActivityIncome() != null && temp.getActivityIncome() != 0) {
//                temp.setActivityIncome(temp.getActivityIncome() / 100);
//            }
        });
        result.setActivityReportDetail(activityReportDetails);

        return ServerResponseEntity.success(result);
    }

    public Integer shopNum(Long id){
        //适用门店数量
        List<BuyCouponActivityShop> shops = buyActivityShopMapper.selectList(new LambdaQueryWrapper<BuyCouponActivityShop>().eq(BuyCouponActivityShop::getActivityId,id));
        return shops.size();
    }

    public void addShops(List<Long> shops, Long activityId){
        List<BuyCouponActivityShop> shopList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shops)){
            shops.forEach(temp ->{
                BuyCouponActivityShop shop = new BuyCouponActivityShop();
                shop.setActivityId(activityId);
                shop.setShopId(temp);
                shopList.add(shop);
            });
            buyActivityShopMapper.insertBatch(shopList);
        }

    }

    public void addCoupons(List<BuyActivityCouponDTO> coupons, Long activityId){
        List<BuyCouponActivityCoupon> couponList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(coupons)){
            coupons.forEach(temp ->{
                BuyCouponActivityCoupon coupon = new BuyCouponActivityCoupon();
                coupon.setActivityId(activityId);
                coupon.setCouponId(temp.getCouponId());
                if (temp.getLimitNum() == 0){
                    coupon.setIsPersonLimit(false);
                }else {
                    coupon.setIsPersonLimit(true);
                }
                coupon.setLimitNum(temp.getLimitNum());
                if (temp.getStocks() == 0){
                    coupon.setIsStocksLimit(false);
                }else {
                    coupon.setIsStocksLimit(true);
                }
                coupon.setStocks(temp.getStocks());
                if (temp.getDailyLimitNum() == 0){
                    coupon.setIsDailyLimit(false);
                }else{
                    coupon.setIsDailyLimit(true);
                }
                coupon.setDailyLimitNum(temp.getDailyLimitNum());
                coupon.setPrice(temp.getPrice());
                couponList.add(coupon);
            });
            buyActivityCouponMapper.insertBatch(couponList);
        }
    }

    public List<Long> shops(Long activityId){
        List<BuyCouponActivityShop> shops = buyActivityShopMapper.selectList(new LambdaQueryWrapper<BuyCouponActivityShop>().eq(BuyCouponActivityShop::getActivityId,activityId));
        List<Long> result = shops.stream().map(BuyCouponActivityShop::getShopId).collect(Collectors.toList());

        return result;
    }
}
