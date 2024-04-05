package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.ActivityDelEnum;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.constant.ActivityStatusEnum;
import com.mall4j.cloud.coupon.constant.TCouponStatus;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.mapper.*;
import com.mall4j.cloud.coupon.model.*;
import com.mall4j.cloud.coupon.service.GoodsCouponActivityService;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class GoodsCouponActivityServiceImpl implements GoodsCouponActivityService {
    @Resource
    private GoodsCouponActivityMapper goodsCouponActivityMapper;
    @Resource
    private GoodsActivityShopMapper goodsActivityShopMapper;
    @Resource
    private GoodsActivityCommodityMapper goodsActivityCommodityMapper;
    @Resource
    private TCouponUserService tCouponUserService;
    @Resource
    private TCouponUserMapper tCouponUserMapper;


    @Override
    public ServerResponseEntity<PageInfo<GoodsActivityListVO>> list(ActivityListDTO param) {
        log.info("商详领券活动列表的搜索条件为 :{}", param);
        PageInfo<GoodsActivityListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                goodsCouponActivityMapper.list(param)
        );

        //处理返回值
        List<GoodsActivityListVO> list = result.getList();
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(temp ->{
                //活动状态
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

                if (!temp.getIsAllShop()){
                    //适用门店
                    List<Long> shops = shops(temp.getId());
                    temp.setShops(shops);
                    //适用门店数量
                    temp.setShopNum(shops.size());
                }

                //适用商品数量
                List<GoodsCouponActivityCommodity> commodities = goodsActivityCommodityMapper.selectList(new LambdaQueryWrapper<GoodsCouponActivityCommodity>().eq(GoodsCouponActivityCommodity::getActivityId, temp.getId()));
                List<Long> commodityIds = commodities.stream().map(GoodsCouponActivityCommodity::getCommodityId).collect(Collectors.toList());
                temp.setCommodityNum(commodityIds.size());
            });
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> save(GoodsActivityDTO param) {
        //插入基础信息
        GoodsCouponActivity goodsCouponActivity = BeanUtil.copyProperties(param, GoodsCouponActivity.class);
        goodsCouponActivity.setCreateId(AuthUserContext.get().getUserId());
        goodsCouponActivity.setCreateName(AuthUserContext.get().getUsername());
        goodsCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        goodsCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        goodsCouponActivityMapper.insert(goodsCouponActivity);

        //组装关联门店信息
        if (!param.getIsAllShop()){
            addShops(param.getShops(),goodsCouponActivity.getId());
        }

        //组装关联商品信息
        addCommodity(param.getCommodityIds(),goodsCouponActivity.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<GoodsActivityVO> detail(Long id) {
        GoodsCouponActivity goodsCouponActivity = goodsCouponActivityMapper.selectById(id);
        GoodsActivityVO result = BeanUtil.copyProperties(goodsCouponActivity, GoodsActivityVO.class);
        if (!result.getIsAllShop()){
            //适用门店
            List<Long> shops = shops(result.getId());
            result.setShops(shops);
            result.setShopNum(shops.size());
        }
        //关联商品信息
        List<GoodsCouponActivityCommodity> commodities = goodsActivityCommodityMapper.selectList(new LambdaQueryWrapper<GoodsCouponActivityCommodity>().eq(GoodsCouponActivityCommodity::getActivityId, id));
        List<Long> commodityIds = commodities.stream().map(GoodsCouponActivityCommodity::getCommodityId).collect(Collectors.toList());
        result.setCommodityIds(commodityIds);
        result.setCommodityNum(commodityIds.size());

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> delete(Long id) {
        GoodsCouponActivity goodsCouponActivity = new GoodsCouponActivity();
        goodsCouponActivity.setId(id);
        goodsCouponActivity.setDel(ActivityDelEnum.DEL.value());
        goodsCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        goodsCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        goodsCouponActivityMapper.updateById(goodsCouponActivity);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(GoodsActivityDTO param) {
        //修改基本信息
        GoodsCouponActivity goodsCouponActivity = BeanUtil.copyProperties(param, GoodsCouponActivity.class);
        goodsCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        goodsCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        goodsCouponActivityMapper.updateById(goodsCouponActivity);

        //删除原有门店信息
        goodsActivityShopMapper.delete(new LambdaUpdateWrapper<GoodsCouponActivityShop>().eq(GoodsCouponActivityShop::getActivityId,param.getId()));
        //新增关联门店信息
        addShops(param.getShops(),param.getId());

        //删除原有关联优惠券信息
        goodsActivityCommodityMapper.delete(new LambdaUpdateWrapper<GoodsCouponActivityCommodity>().eq(GoodsCouponActivityCommodity::getActivityId,param.getId()));
        //组装关联优惠券信息
        addCommodity(param.getCommodityIds(), param.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateStatus(Long id, Short status) {
        GoodsCouponActivity goodsCouponActivity = new GoodsCouponActivity();
        goodsCouponActivity.setId(id);
        goodsCouponActivity.setStatus(status);
        goodsCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        goodsCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        goodsCouponActivityMapper.updateById(goodsCouponActivity);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteShop(Long id, Long shopId, Boolean isAllShop) {
        if (isAllShop){
            GoodsCouponActivity goodsCouponActivity = new GoodsCouponActivity();
            goodsCouponActivity.setId(id);
            goodsCouponActivity.setIsAllShop(true);
            goodsCouponActivityMapper.updateById(goodsCouponActivity);
            goodsActivityShopMapper.delete(new LambdaUpdateWrapper<GoodsCouponActivityShop>().eq(GoodsCouponActivityShop::getActivityId, id));
        }
        goodsActivityShopMapper.delete(new LambdaUpdateWrapper<GoodsCouponActivityShop>()
                .eq(GoodsCouponActivityShop::getActivityId, id)
                .eq(GoodsCouponActivityShop::getShopId, shopId));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> addShops(AddShopsDTO param) {
        if (!CollectionUtils.isEmpty(param.getShops())){
            //现有的门店id
            List<Long> oldShops = shops(param.getId());
            param.getShops().removeAll(oldShops);
            addShops(param.getShops(),param.getId());
        }else {
            throw new LuckException("添加门店不能为空");
        }
        return ServerResponseEntity.success();
    }

//    @HystrixCommand
    @Override
    public ServerResponseEntity<List<AppGoodsActivityVO>> couponsForGoods(Long commodityId,Long storeId) {
        log.info("当前登陆态信息为：{}", JSONObject.toJSONString(AuthUserContext.get()));
        List<AppGoodsActivityVO> result = goodsCouponActivityMapper.couponsForGoods(commodityId, storeId);

        result.forEach(temp ->{
            //用户是否已经领取
            if (temp.getPersonMaxAmount() > 0){
                Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                        .eq(TCouponUser::getCouponId, temp.getCouponId())
                        .eq(TCouponUser::getUserId, AuthUserContext.get().getUserId())
                        .eq(TCouponUser::getActivityId, temp.getId())
                        .eq(TCouponUser::getActivitySource, ActivitySourceEnum.GOODS_COUPON.value()));
                if (!(temp.getPersonMaxAmount() > count)){
                    temp.setReceiveStatus(false);
                }
            }
        });

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> userReceive(Long id, Long couponId) {
        //校验活动状态（是否存在，活动时间，状态）
        GoodsCouponActivity activity = goodsCouponActivityMapper.selectById(id);
        if (ObjectUtil.isEmpty(activity)) {
            throw new LuckException("商详领券活动不存在，无法领取优惠券！");
        }
        if (activity.getStatus() == ActivityStatusEnum.DISABLE.value()) {
            throw new LuckException("商详领券活动已停用，无法领取优惠券！");
        }
        if (System.currentTimeMillis() < activity.getStartTime().getTime()) {
            throw new LuckException("商详领券活动未开始，无法领取优惠券！");
        }
        if (System.currentTimeMillis() > activity.getEndTime().getTime()) {
            throw new LuckException("商详领券活动已结束，无法领取优惠券！");
        }

        //校验每人限制领取
        if (activity.getPersonMaxAmount() != 0){
            Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser::getCouponId, couponId)
                    .eq(TCouponUser::getUserId, AuthUserContext.get().getUserId())
                    .eq(TCouponUser::getActivityId, id)
                    .eq(TCouponUser::getActivitySource, ActivitySourceEnum.GOODS_COUPON.value()));
            if (!(activity.getPersonMaxAmount() > count)){
                throw new LuckException("累计次数已用完，无法领取优惠券！");
            }
        }

        //校验每人每天限制领取
        if (activity.getPersonDayAmount() != 0){
            //一天的开始
            Date beginOfDay = DateUtil.beginOfDay(DateUtil.date());
            //一天的结束
            Date endOfDay = DateUtil.endOfDay(DateUtil.date());
            Integer dayCount = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser::getCouponId, couponId)
                    .eq(TCouponUser::getUserId, AuthUserContext.get().getUserId())
                    .eq(TCouponUser::getActivityId, id)
                    .eq(TCouponUser::getActivitySource, ActivitySourceEnum.GOODS_COUPON.value())
                    .between(TCouponUser::getReceiveTime,beginOfDay,endOfDay));
            if (!(activity.getPersonDayAmount() > dayCount)){
                throw new LuckException("今日领券次数已用完，无法领取优惠券！");
            }
        }

        //增加用户优惠券记录
        ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
        receiveCouponDTO.setCouponId(couponId);
        receiveCouponDTO.setUserId(AuthUserContext.get().getUserId());
        receiveCouponDTO.setActivityId(id);
        receiveCouponDTO.setActivitySource(ActivitySourceEnum.GOODS_COUPON.value());
        ServerResponseEntity<Void> receive = tCouponUserService.receive(receiveCouponDTO);

        return ServerResponseEntity.success();
    }

    public void addShops(List<Long> shops, Long activityId){
        List<GoodsCouponActivityShop> shopList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shops)){
            shops.forEach(temp ->{
                GoodsCouponActivityShop shop = new GoodsCouponActivityShop();
                shop.setActivityId(activityId);
                shop.setShopId(temp);
                shopList.add(shop);
            });
            goodsActivityShopMapper.insertBatch(shopList);
        }

    }

    public void addCommodity(List<Long> commodities, Long activityId){
        List<GoodsCouponActivityCommodity> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(commodities)){
            commodities.forEach(temp ->{
                GoodsCouponActivityCommodity commodity = new GoodsCouponActivityCommodity();
                commodity.setActivityId(activityId);
                commodity.setCommodityId(temp);
                list.add(commodity);
            });
            goodsActivityCommodityMapper.insertBatch(list);
        }

    }

    public List<Long> shops(Long activityId){
        List<GoodsCouponActivityShop> shops = goodsActivityShopMapper.selectList(new LambdaQueryWrapper<GoodsCouponActivityShop>().eq(GoodsCouponActivityShop::getActivityId, activityId));
        List<Long> result = shops.stream().map(GoodsCouponActivityShop::getShopId).collect(Collectors.toList());

        return result;
    }
}
