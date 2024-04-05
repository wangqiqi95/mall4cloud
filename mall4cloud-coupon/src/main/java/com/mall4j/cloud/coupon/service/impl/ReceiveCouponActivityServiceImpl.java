package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.dto.InsiderStorePageDTO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.ActivityDelEnum;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.constant.ActivityStatusEnum;
import com.mall4j.cloud.coupon.constant.TCouponStatus;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.mapper.*;
import com.mall4j.cloud.coupon.model.*;
import com.mall4j.cloud.coupon.service.CouponPackService;
import com.mall4j.cloud.coupon.service.ReceiveCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReceiveCouponActivityServiceImpl implements ReceiveCouponActivityService {
    @Resource
    private ReceiveCouponActivityMapper receiveCouponActivityMapper;
    @Resource
    private ReceiveActivityCouponMapper receiveActivityCouponMapper;
    @Resource
    private ReceiveActivityShopMapper receiveShopMapper;
    @Resource
    private ReceiveInventoryLogMapper receiveInventoryLogMapper;
    @Resource
    private TCouponUserService tCouponUserService;
    @Resource
    private CouponPackService couponPackService;
    @Resource
    private TCouponShopMapper tCouponShopMapper;
    @Resource
    private TCouponCommodityMapper tCouponCommodityMapper;
    @Resource
    private TCouponUserMapper tCouponUserMapper;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private StoreFeignClient storeFeignClient;


    @Override
    public ServerResponseEntity<PageInfo<ActivityListVO>> list(ActivityListDTO param) {
        log.info("领券活动列表的搜索条件为 :{}", param);
        PageInfo<ActivityListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                receiveCouponActivityMapper.list(param)
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
    public ServerResponseEntity<Void> save(ReceiveActivityDTO param) {
        log.info("新增领券活动的参数为 :{}", param);
        //插入基础信息 新增消费限制入参
        ReceiveCouponActivity receiveCouponActivity = BeanUtil.copyProperties(param, ReceiveCouponActivity.class);
        receiveCouponActivity.setCreateId(AuthUserContext.get().getUserId());
        receiveCouponActivity.setCreateName(AuthUserContext.get().getUsername());
        receiveCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        receiveCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        receiveCouponActivityMapper.insert(receiveCouponActivity);

        //组装关联门店信息
        List<Long> shops = param.getShops();
        addShops(shops, receiveCouponActivity.getId());

        //组装关联优惠券信息
        List<ReceiveActivityCouponDTO> coupons = param.getCoupons();
        addCoupons(coupons, receiveCouponActivity.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<ReceiveActivityDetailVO> detail(Long id) {
        ReceiveCouponActivity receiveCouponActivity = receiveCouponActivityMapper.selectById(id);
        ReceiveActivityDetailVO result = BeanUtil.copyProperties(receiveCouponActivity, ReceiveActivityDetailVO.class);
        if (!result.getIsAllShop()){
            //适用门店数量
            Integer shopNum = shopNum(result.getId());
            result.setShopNum(shopNum);
            //适用门店
            List<Long> shops = shops(result.getId());
            result.setShops(shops);
        }
        //关联优惠券信息
        List<ReceiveActivityCouponVO> receiveActivityCouponVOS = receiveActivityCouponMapper.couponList(id);
        result.setCoupons(receiveActivityCouponVOS);

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> delete(Long id) {
        ReceiveCouponActivity receiveCouponActivity = new ReceiveCouponActivity();
        receiveCouponActivity.setId(id);
        receiveCouponActivity.setDel(ActivityDelEnum.DEL.value());
        receiveCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        receiveCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        receiveCouponActivityMapper.updateById(receiveCouponActivity);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(ReceiveActivityDTO param) {
        log.info("修改领券活动的参数为 :{}", param);
        //修改基本信息
        ReceiveCouponActivity receiveCouponActivity = BeanUtil.copyProperties(param, ReceiveCouponActivity.class);
        receiveCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        receiveCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        receiveCouponActivityMapper.updateById(receiveCouponActivity);

        //删除原有门店信息
        receiveShopMapper.delete(new LambdaUpdateWrapper<ReceiveCouponActivityShop>().eq(ReceiveCouponActivityShop::getActivityId,param.getId()));
        //新增关联门店信息
        List<Long> shops = param.getShops();
        addShops(shops,param.getId());

        //删除原有关联优惠券信息
        receiveActivityCouponMapper.delete(new LambdaUpdateWrapper<ReceiveCouponActivityCoupon>().eq(ReceiveCouponActivityCoupon::getActivityId,param.getId()));
        //组装关联优惠券信息
        List<ReceiveActivityCouponDTO> coupons = param.getCoupons();
        addCoupons(coupons, param.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateStatus(Long id, Short status) {
        ReceiveCouponActivity receiveCouponActivity = new ReceiveCouponActivity();
        receiveCouponActivity.setId(id);
        receiveCouponActivity.setStatus(status);
        receiveCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        receiveCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        receiveCouponActivityMapper.updateById(receiveCouponActivity);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<ReceiveActivityCouponVO>> ActivityCouponList(ActivityCouponListVO param) {
        PageInfo<ReceiveActivityCouponVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                receiveActivityCouponMapper.couponList(param.getActivityId())
        );
        //处理返回值
        List<ReceiveActivityCouponVO> list = result.getList();
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
        ReceiveCouponActivityCoupon receiveActivityCoupon = receiveActivityCouponMapper.selectById(param.getId());
        receiveActivityCoupon.setStocksLimit(receiveActivityCoupon.getStocksLimit() + param.getNum());
        receiveActivityCoupon.setStocks(receiveActivityCoupon.getStocks() + param.getNum());
        receiveActivityCouponMapper.updateById(receiveActivityCoupon);

        //记录库存调整日志
        ReceiveInventoryLog receiveInventoryLog = BeanUtil.copyProperties(param, ReceiveInventoryLog.class);
        receiveInventoryLog.setActivityId(receiveActivityCoupon.getActivityId());
        receiveInventoryLog.setCouponId(receiveActivityCoupon.getCouponId());
        receiveInventoryLog.setCreateId(AuthUserContext.get().getUserId());
        receiveInventoryLog.setCreateCode(AuthUserContext.get().getUserId().toString());
        receiveInventoryLog.setCreateName(AuthUserContext.get().getUsername());
        receiveInventoryLogMapper.insert(receiveInventoryLog);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<InventoryListVO>> inventoryLog(ActivityCouponListVO param) {
        PageInfo<InventoryListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                receiveInventoryLogMapper.list(param.getActivityId())
        );

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> deleteShop(Long id,Long shopId,Boolean isAllShop) {
        if (isAllShop){
            ReceiveCouponActivity receiveCouponActivity = new ReceiveCouponActivity();
            receiveCouponActivity.setId(id);
            receiveCouponActivity.setIsAllShop(true);
            receiveCouponActivityMapper.updateById(receiveCouponActivity);
            receiveShopMapper.delete(new LambdaUpdateWrapper<ReceiveCouponActivityShop>().eq(ReceiveCouponActivityShop::getActivityId, id));
        }
        receiveShopMapper.delete(new LambdaUpdateWrapper<ReceiveCouponActivityShop>()
                .eq(ReceiveCouponActivityShop::getActivityId, id)
                .eq(ReceiveCouponActivityShop::getShopId, shopId));
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
    public ServerResponseEntity<AppReceiveActivityVO> appReceiveActivity(Long storeId) {
        if(storeId==null){
            storeId=AuthUserContext.get().getStoreId();
        }
        log.info("领券活动首页 storeId:【{}】",storeId);
        AppReceiveActivityVO result = receiveCouponActivityMapper.appActivity(storeId);
        if (ObjectUtil.isNotEmpty(result)){
            List<AppCouponVO> appCouponVOS = receiveActivityCouponMapper.appCouponList(result.getId());
            if (!CollectionUtils.isEmpty(appCouponVOS)){
                appCouponVOS.forEach(temp ->{
                    //适用门店
                    if (!temp.getIsAllShop()){
                        List<TCouponShop> couponShops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, temp.getCouponId()));
                        List<Long> shopIds = couponShops.stream().map(TCouponShop::getShopId).collect(Collectors.toList());
                        InsiderStorePageDTO insiderStorePageDTO = new InsiderStorePageDTO();
                        insiderStorePageDTO.setStoreIdList(shopIds);
                        log.info("调用门店信息查询参数为：{}", JSONObject.toJSONString(insiderStorePageDTO));
                        ServerResponseEntity<PageVO<SelectedStoreVO>> pageVOServerResponseEntity = storeFeignClient.storePage(insiderStorePageDTO);
                        if (pageVOServerResponseEntity.isFail()){
                            log.info("调用门店信息查询失败，返回值为：{}", JSONObject.toJSONString(pageVOServerResponseEntity));
                        }else {
                            temp.setShops(pageVOServerResponseEntity.getData().getList());
                        }
                    }
                    //当前领取进度
                    if (temp.getStocksLimit() == 0){
                        //优惠券为不限量时候，领取比例固定为85%
                        temp.setProportion(new BigDecimal("0.85"));
                    }else{
                        if (temp.getStocksLimit().equals(temp.getStocks())){
                            temp.setProportion(new BigDecimal("0"));
                        }else {
                            int sum = temp.getStocksLimit() - temp.getStocks();
                            BigDecimal div = NumberUtil.div(String.valueOf(sum), String.valueOf(temp.getStocksLimit()));
                            log.info("----------------------------------" + div);
                            temp.setProportion(div);
                        }
                    }
                    //适用商品
                    ProductSearchDTO productSearchDTO = new ProductSearchDTO();
                    if (temp.getCommodityScopeType() == 1){
                        //部分商品适用
                        List<TCouponCommodity> tCouponCommodities = tCouponCommodityMapper.selectList(new LambdaQueryWrapper<TCouponCommodity>().eq(TCouponCommodity::getCouponId, temp.getCouponId()));
                        List<Long> commodities = tCouponCommodities.stream().map(TCouponCommodity::getCommodityId).collect(Collectors.toList());
                        productSearchDTO.setPageNum(1);
                        productSearchDTO.setPageSize(appCouponVOS.size());
                        productSearchDTO.setSpuIds(commodities);
                    }else{
                        productSearchDTO.setPageNum(1);
                        productSearchDTO.setPageSize(3);
                    }
                    log.info("调用商品信息查询参数为：{}", JSONObject.toJSONString(productSearchDTO));
                    ServerResponseEntity<PageVO<SpuCommonVO>> response = productFeignClient.couponSearch(productSearchDTO);
                    if (response.isFail()){
                        log.info("调用商品信息查询失败，返回值为：{}", JSONObject.toJSONString(response));
                    }else {
                        temp.setCommodities(response.getData().getList());
                    }
                    //用户是否已经领取
                    if (temp.getIsPersonLimit()){
                        Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                                .eq(TCouponUser::getCouponId, temp.getCouponId())
                                .eq(TCouponUser::getUserId, AuthUserContext.get().getUserId())
                                .eq(TCouponUser::getActivityId, temp.getActivityId())
                                .eq(TCouponUser::getActivitySource, ActivitySourceEnum.RECEIVE.value()));
                        if (!(temp.getLimitNum() > count)){
                            temp.setReceiveStatus(false);
                        }
                    }
                });
            }
            result.setCoupons(appCouponVOS);
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> userReceive(Long id, Long couponId,Long storeId,Long userId) {
        //校验活动状态（是否存在，活动时间，状态）
        ReceiveCouponActivity receiveCouponActivity = receiveCouponActivityMapper.selectById(id);
        if (ObjectUtil.isEmpty(receiveCouponActivity)) {
            throw new LuckException("领券活动不存在，无法领取优惠券！");
        }
        if (receiveCouponActivity.getStatus() == ActivityStatusEnum.DISABLE.value()) {
            throw new LuckException("领券活动已停用，无法领取优惠券！");
        }
        if (System.currentTimeMillis() < receiveCouponActivity.getStartTime().getTime()) {
            throw new LuckException("领券活动未开始，无法领取优惠券！");
        }
        if (System.currentTimeMillis() > receiveCouponActivity.getEndTime().getTime()) {
            throw new LuckException("领券活动已结束，无法领取优惠券！");
        }
        if(storeId==null){
            storeId=AuthUserContext.get().getStoreId();
        }
        log.info("领券活动 storeId:【{}】",storeId);
        if (!receiveCouponActivity.getIsAllShop()) {
            Integer selectCount = receiveShopMapper.selectCount(new LambdaQueryWrapper<ReceiveCouponActivityShop>().eq(ReceiveCouponActivityShop::getActivityId, receiveCouponActivity.getId())
                    .eq(ReceiveCouponActivityShop::getShopId, storeId));
            if (selectCount == 0) {
                throw new LuckException("当前访问门店不在活动范围内！");
            }
        }


        //校验每人限制领取,库存
        ReceiveCouponActivityCoupon receiveCouponActivityCoupon = receiveActivityCouponMapper.selectOne(new LambdaQueryWrapper<ReceiveCouponActivityCoupon>()
                .eq(ReceiveCouponActivityCoupon::getActivityId, id)
                .eq(ReceiveCouponActivityCoupon::getCouponId, couponId));

        //校验每人限制领取
        if (receiveCouponActivityCoupon.getIsPersonLimit()){
            Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser::getCouponId, couponId)
                    .eq(TCouponUser::getUserId, userId)
                    .eq(TCouponUser::getActivityId, id)
                    .eq(TCouponUser::getActivitySource, ActivitySourceEnum.RECEIVE.value())
                    .between(TCouponUser::getReceiveTime, DateUtil.today(),DateUtil.offsetDay(new Date(),1).toDateStr()));
            if (!(receiveCouponActivityCoupon.getDailyLimitNum() > count)){
                throw new LuckException("今日次数已用完，无法领取优惠券！");
            }
        }

        //校验每人每天限制领取
        if (receiveCouponActivityCoupon.getIsPersonLimit()){
            Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser::getCouponId, couponId)
                    .eq(TCouponUser::getUserId, userId)
                    .eq(TCouponUser::getActivityId, id)
                    .eq(TCouponUser::getActivitySource, ActivitySourceEnum.RECEIVE.value()));
            if (!(receiveCouponActivityCoupon.getLimitNum() > count)){
                throw new LuckException("累计次数已用完，无法领取优惠券！");
            }
        }

        //校验库存
        if (receiveCouponActivityCoupon.getStocksLimit() != 0){
            if (receiveCouponActivityCoupon.getStocks() < 1){
                throw new LuckException("库存不足，无法领取优惠券！");
            }else {
                //扣减库存
                if (receiveActivityCouponMapper.updateCouponStocks(couponId,id,receiveCouponActivityCoupon.getVersion()) < 1 ){
                    throw new LuckException("优惠券领取失败！");
                }
            }
        }

        // 消费限制校验 判断用户订单消费金额是否满足条件
        ExpendConditionDTO expendConditionDTO = BeanUtil.copyProperties(receiveCouponActivity, ExpendConditionDTO.class);
        couponPackService.extracted(userId,expendConditionDTO);

        //核准优惠券状态，增加用户优惠券记录
        ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
        receiveCouponDTO.setCouponId(couponId);
        receiveCouponDTO.setUserId(userId);
        receiveCouponDTO.setActivityId(id);
        receiveCouponDTO.setShopId(storeId);
        receiveCouponDTO.setActivitySource(ActivitySourceEnum.RECEIVE.value());
        tCouponUserService.receive(receiveCouponDTO);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<ActivityReportVO> activityReport(ActivityReportDTO param) {
        //=============== 活动信息 ===============
        ReceiveCouponActivity receiveCouponActivity = receiveCouponActivityMapper.selectById(param.getActivityId());
        ActivityReportVO result = BeanUtil.copyProperties(receiveCouponActivity, ActivityReportVO.class);

        //=============== 券活动概况 ===============
        //统计指标：总领券数
        Integer receiveSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.RECEIVE.value(), param.getCouponInfo(), null);
        result.setReceiveSum(receiveSum);
        //统计指标：总核销数
        Integer writeOffSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.RECEIVE.value(), param.getCouponInfo(), "USED");
        result.setWriteOffSum(writeOffSum);
        //统计指标：活动收入
        BigDecimal activityIncome = tCouponUserMapper.activityIncome(param.getActivityId(), ActivitySourceEnum.RECEIVE.value(), param.getCouponInfo(), "USED");
        log.info("活动收入:{}",activityIncome);
        result.setActivityIncome(activityIncome);
        //统计指标：券过期数
        Integer overdueSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.RECEIVE.value(), param.getCouponInfo(), "EXPIRED");
        result.setOverdueSum(overdueSum);

        //优惠券报表列表
        List<ActivityReportDetailVO> activityReportDetails = receiveCouponActivityMapper.activityReportDetail(param.getActivityId(), param.getCouponInfo(), ActivitySourceEnum.RECEIVE.value());

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
        List<ReceiveCouponActivityShop> shops = receiveShopMapper.selectList(new LambdaQueryWrapper<ReceiveCouponActivityShop>().eq(ReceiveCouponActivityShop::getActivityId,id));
        return shops.size();
    }

    public void addShops(List<Long> shops, Long activityId){
        List<ReceiveCouponActivityShop> shopList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shops)){
            shops.forEach(temp ->{
                ReceiveCouponActivityShop shop = new ReceiveCouponActivityShop();
                shop.setActivityId(activityId);
                shop.setShopId(temp);
                shopList.add(shop);
            });
            receiveShopMapper.insertBatch(shopList);
        }
    }

    public void addCoupons(List<ReceiveActivityCouponDTO> coupons, Long activityId){
        List<ReceiveCouponActivityCoupon> couponList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(coupons)){
            coupons.forEach(temp ->{
                ReceiveCouponActivityCoupon coupon = new ReceiveCouponActivityCoupon();
                coupon.setActivityId(activityId);
                coupon.setCouponId(temp.getCouponId());
                coupon.setStocksLimit(temp.getStocks());
                coupon.setLimitNum(temp.getLimitNum());
                if (temp.getLimitNum() == 0){
                    coupon.setIsPersonLimit(false);
                }else {
                    coupon.setIsPersonLimit(true);
                }
                if (temp.getDailyLimitNum() == 0){
                    coupon.setIsDailyLimit(false);
                }else {
                    coupon.setIsDailyLimit(true);
                }
                coupon.setDailyLimitNum(temp.getDailyLimitNum());
                coupon.setStocks(temp.getStocks());
                coupon.setIsCommon(temp.getIsCommon());
                couponList.add(coupon);
            });
            receiveActivityCouponMapper.insertBatch(couponList);
        }
    }

    public List<Long> shops(Long activityId){
        List<ReceiveCouponActivityShop> shops = receiveShopMapper.selectList(new LambdaQueryWrapper<ReceiveCouponActivityShop>().eq(ReceiveCouponActivityShop::getActivityId,activityId));
        List<Long> result = shops.stream().map(ReceiveCouponActivityShop::getShopId).collect(Collectors.toList());
        return result;
    }
}
