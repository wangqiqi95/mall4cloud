package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
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
import com.mall4j.cloud.coupon.mapper.InventoryLogMapper;
import com.mall4j.cloud.coupon.mapper.PushActivityCouponMapper;
import com.mall4j.cloud.coupon.mapper.PushActivityShopMapper;
import com.mall4j.cloud.coupon.mapper.PushCouponActivityMapper;
import com.mall4j.cloud.coupon.mapper.TCouponUserMapper;
import com.mall4j.cloud.coupon.model.InventoryLog;
import com.mall4j.cloud.coupon.model.PushCouponActivity;
import com.mall4j.cloud.coupon.model.PushCouponActivityCoupon;
import com.mall4j.cloud.coupon.model.PushCouponActivityShop;
import com.mall4j.cloud.coupon.model.TCouponUser;
import com.mall4j.cloud.coupon.service.CouponPackService;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PushCouponActivityServiceImpl implements PushCouponActivityService {
    @Resource
    private PushCouponActivityMapper pushCouponActivityMapper;
    @Resource
    private PushActivityCouponMapper pushActivityCouponMapper;
    @Resource
    private PushActivityShopMapper shopMapper;
    @Resource
    private InventoryLogMapper inventoryLogMapper;
    @Resource
    private TCouponUserService tCouponUserService;
    @Resource
    private CouponPackService couponPackService;
    @Resource
    private TCouponUserMapper tCouponUserMapper;
    @Resource
    private UserFeignClient userFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;

    @Override
    public ServerResponseEntity<PageInfo<ActivityListVO>> list(ActivityListDTO param) {
        log.info("推券活动列表的搜索条件为 :{}", param);
            PageInfo<ActivityListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                pushCouponActivityMapper.list(param)
        );

        //处理返回值
        List<ActivityListVO> list = result.getList();
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
    public ServerResponseEntity<Void> save(ActivityDTO param) {
        log.info("新增推券活动的参数为 :{}", param);
        //插入基础信息 20221115新增了消费限制入参
        PushCouponActivity pushCouponActivity = BeanUtil.copyProperties(param, PushCouponActivity.class);
        pushCouponActivity.setCreateId(AuthUserContext.get().getUserId());
        pushCouponActivity.setCreateName(AuthUserContext.get().getUsername());
        pushCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        pushCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        pushCouponActivityMapper.insert(pushCouponActivity);

        //组装关联门店信息
        List<Long> shops = param.getShops();
        addShops(shops, pushCouponActivity.getId());

        //组装关联优惠券信息
        List<ActivityCouponDTO> codes = param.getCodes();
        addCoupons(codes, pushCouponActivity.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<ActivityDetailVO> detail(Long id) {
        PushCouponActivity pushCouponActivity = pushCouponActivityMapper.selectById(id);
        ActivityDetailVO result = BeanUtil.copyProperties(pushCouponActivity, ActivityDetailVO.class);
        if (!result.getIsAllShop()){
            //适用门店数量
            Integer shopNum = shopNum(result.getId());
            result.setShopNum(shopNum);
            //适用门店
            List<Long> shops = shops(result.getId());
            result.setShops(shops);
        }
        //关联优惠券信息
        List<ActivityCouponVO> activityCouponVOS = pushActivityCouponMapper.couponList(id);
        result.setCoupons(activityCouponVOS);

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> delete(Long id) {
        PushCouponActivity pushCouponActivity = new PushCouponActivity();
        pushCouponActivity.setId(id);
        pushCouponActivity.setDel(ActivityDelEnum.DEL.value());
        pushCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        pushCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        pushCouponActivityMapper.updateById(pushCouponActivity);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(ActivityDTO param) {
        log.info("修改推券活动的参数为 :{}", param);
        //修改基本信息
        PushCouponActivity pushCouponActivity = BeanUtil.copyProperties(param, PushCouponActivity.class);
        pushCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        pushCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        pushCouponActivityMapper.updateById(pushCouponActivity);

        //删除原有门店信息
        shopMapper.delete(new LambdaUpdateWrapper<PushCouponActivityShop>().eq(PushCouponActivityShop::getActivityId,param.getId()));
        //新增关联门店信息
        List<Long> shops = param.getShops();
        addShops(shops,param.getId());

        //删除原有关联优惠券信息
        pushActivityCouponMapper.delete(new LambdaUpdateWrapper<PushCouponActivityCoupon>().eq(PushCouponActivityCoupon::getActivityId,param.getId()));
        //组装关联优惠券信息
        List<ActivityCouponDTO> codes = param.getCodes();
        addCoupons(codes, pushCouponActivity.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateStatus(Long id, Short status) {
        PushCouponActivity pushCouponActivity = new PushCouponActivity();
        pushCouponActivity.setId(id);
        pushCouponActivity.setStatus(status);
        pushCouponActivity.setUpdateId(AuthUserContext.get().getUserId());
        pushCouponActivity.setUpdateName(AuthUserContext.get().getUsername());
        pushCouponActivityMapper.updateById(pushCouponActivity);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<ActivityCouponVO>> ActivityCouponList(ActivityCouponListVO param) {
        PageInfo<ActivityCouponVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                pushActivityCouponMapper.couponList(param.getActivityId())
        );
        //处理返回值
        List<ActivityCouponVO> list = result.getList();
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(temp ->{
                if (temp.getTimeType() == 1){
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
        PushCouponActivityCoupon activityCoupon = pushActivityCouponMapper.selectById(param.getId());
        activityCoupon.setStocks(activityCoupon.getStocks() + param.getNum());
        pushActivityCouponMapper.updateById(activityCoupon);

        //记录库存调整日志
        InventoryLog inventoryLog = BeanUtil.copyProperties(param, InventoryLog.class);
        inventoryLog.setCreateId(AuthUserContext.get().getUserId());
        inventoryLog.setCreateCode(AuthUserContext.get().getUserId().toString());
        inventoryLog.setCreateName(AuthUserContext.get().getUsername());
        inventoryLog.setActivityId(activityCoupon.getActivityId());
        inventoryLog.setCouponId(activityCoupon.getCouponId());
        inventoryLogMapper.insert(inventoryLog);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<InventoryListVO>> inventoryLog(ActivityCouponListVO param) {
        PageInfo<InventoryListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                inventoryLogMapper.list(param.getActivityId())
        );

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<PageVO<CouponForShoppersVO>> listForShoppers(PageDTO pageDTO, Integer type) {
        PageVO<CouponForShoppersVO> pageVO = new PageVO<>();
        List<Long> activityIds = pushCouponActivityMapper.shoppersActivity(AuthUserContext.get().getStoreId());
        pageVO.setTotal(0L);
        pageVO.setList(Collections.emptyList());
        if (!CollectionUtils.isEmpty(activityIds)) {
            pageVO = PageUtil.doPage(pageDTO, () -> pushCouponActivityMapper.listForShoppers(activityIds, type));
        }
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<CouponDetailForShoppersVO> detailForShoppers(Long id, Long userId) {
        CouponDetailForShoppersVO result = pushActivityCouponMapper.couponDetail(id);
        //优惠券已经领取数量
//        Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
//                .eq(TCouponUser :: getCouponId, result.getCouponId())
//                .eq(TCouponUser::getActivityId,result.getActivityId())
//                .eq(TCouponUser::getActivitySource,ActivitySourceEnum.PUSH.value()));
        result.setReceiveAmount(20000);

        //剩余数量
//        result.setLeaveAmount(result.getStocks());
//        if(Objects.nonNull(result.getStocks()) && Objects.nonNull(result.getReceiveAmount())){
//            if(result.getReceiveAmount()>result.getStocks()){
//                result.setLeaveAmount(0);
//            }else{
//                result.setLeaveAmount(result.getStocks()-result.getReceiveAmount());
//            }
//        }

        if (Objects.nonNull(userId)) {
            Integer tCouponUsersResult = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser :: getUserId, userId)
                    .eq(TCouponUser :: getCouponId, result.getCouponId())
                    .eq(TCouponUser::getActivityId,result.getActivityId())
                    .eq(TCouponUser::getActivitySource,ActivitySourceEnum.PUSH.value()));
            if (tCouponUsersResult > 0) {
                PushCouponActivityCoupon activityCoupon = pushActivityCouponMapper.selectOne(new LambdaQueryWrapper<PushCouponActivityCoupon>()
                        .eq(PushCouponActivityCoupon::getActivityId, result.getActivityId())
                        .eq(PushCouponActivityCoupon::getCouponId, result.getCouponId()));
                if (!(activityCoupon.getLimitNum() > tCouponUsersResult)){
                    result.setReceiveStatus(1);
                }
            }
        }
        return ServerResponseEntity.success(result);
    }

//    @HystrixCommand
    @Override
    public ServerResponseEntity<Void> sendCoupon(StaffSendCouponDTO staffSendCouponDTO) {
        Long couponId = staffSendCouponDTO.getCouponId();
        Long activityId = staffSendCouponDTO.getActivityId();
        Long shopId = staffSendCouponDTO.getShopId();
        Long userId = AuthUserContext.get().getUserId();

        PushCouponActivity pushCouponActivity = pushCouponActivityMapper.selectById(activityId);
        if (ObjectUtils.isEmpty(pushCouponActivity)){
            throw new LuckException("送券活动不存在");
        }
        if(pushCouponActivity.getStatus() == 1){
            throw new LuckException("送券活动已失效");
        }
        if (System.currentTimeMillis() < pushCouponActivity.getStartTime().getTime()) {
            throw new LuckException("送券活动未开始，无法领取优惠券！");
        }
        if (System.currentTimeMillis() > pushCouponActivity.getEndTime().getTime()) {
            throw new LuckException("送券活动已结束，无法领取优惠券！");
        }
        if (!pushCouponActivity.getIsAllShop()) {
            Integer selectCount = shopMapper.selectCount(new LambdaQueryWrapper<PushCouponActivityShop>().eq(PushCouponActivityShop::getActivityId, activityId).eq(PushCouponActivityShop::getShopId, shopId));
            if (selectCount == 0) {
                throw new LuckException("该门店不在活动范围内！");
            }
        }

        PushCouponActivityCoupon activityCoupon = pushActivityCouponMapper.selectOne(new LambdaQueryWrapper<PushCouponActivityCoupon>()
                .eq(PushCouponActivityCoupon::getActivityId, activityId)
                .eq(PushCouponActivityCoupon::getCouponId, couponId));
        //校验库存
        if (activityCoupon.getIsStocksLimit()){
            if (activityCoupon.getStocks() < 1){
                throw new LuckException("券被领完啦！下次记得早点领取哦！");
            }
        }
        //校验每人限制领取总数
        if (activityCoupon.getIsPersonLimit()){
            Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser::getCouponId, couponId)
                    .eq(TCouponUser::getUserId, userId)
                    .eq(TCouponUser::getActivityId, activityId)
                    .eq(TCouponUser::getActivitySource, ActivitySourceEnum.PUSH.value()));
            if (!(activityCoupon.getLimitNum() > count)){
                throw new LuckException("已到达领取上限，无法领取优惠券");
            }
        }

        //校验每天限额数量
        if (activityCoupon.getIsDailyLimit()){
            Integer count = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser::getCouponId, couponId)
                    .eq(TCouponUser::getUserId, AuthUserContext.get().getUserId())
                    .eq(TCouponUser::getActivityId, activityId)
                    .eq(TCouponUser::getActivitySource, ActivitySourceEnum.PUSH.value())
                    .between(TCouponUser::getReceiveTime, DateUtil.beginOfDay(new Date()),DateUtil.endOfDay(new Date())));
            if (!(activityCoupon.getDailyLimitNum() > count)){
                throw new LuckException("今日领取次数已用完，无法领取优惠券");
            }
        }

        // 消费限制校验 判断用户订单消费金额是否满足条件
        ExpendConditionDTO expendConditionDTO = BeanUtil.copyProperties(pushCouponActivity, ExpendConditionDTO.class);
        couponPackService.extracted(userId,expendConditionDTO);

        //如果staffId为空 ，根据触点号获取导购staffId
        log.info("领取优惠券1 staffId【{}】 tentacleNo【{}】",staffSendCouponDTO.getStaffId(),staffSendCouponDTO.getTentacleNo());
        if(Objects.isNull(staffSendCouponDTO.getStaffId())){
            if (StrUtil.isNotBlank(staffSendCouponDTO.getTentacleNo())) {
                ServerResponseEntity<TentacleContentVO> tentacleResp = tentacleContentFeignClient.findByTentacleNo(staffSendCouponDTO.getTentacleNo());
                if (tentacleResp.isSuccess() && tentacleResp.getData().getTentacle().getTentacleType() == 4) {
                    Long staffId = tentacleResp.getData().getTentacle().getBusinessId();
                    ServerResponseEntity<StaffVO> staffRep = staffFeignClient.getStaffById(staffId);
                    if (staffRep.isSuccess() && Objects.nonNull(staffRep.getData())) {
                        staffSendCouponDTO.setStaffId(staffId);
                        staffSendCouponDTO.setStaffName(staffRep.getData().getStaffName());
                        staffSendCouponDTO.setStaffPhone(staffRep.getData().getMobile());
                    }
                }
            }
        }
        log.info("领取优惠券2 staffId【{}】 tentacleNo【{}】",staffSendCouponDTO.getStaffId(),staffSendCouponDTO.getTentacleNo());

        //新增用户领取记录
        ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
        receiveCouponDTO.setCouponId(couponId);
        receiveCouponDTO.setUserId(AuthUserContext.get().getUserId());
        receiveCouponDTO.setActivityId(activityId);
        receiveCouponDTO.setActivitySource(ActivitySourceEnum.PUSH.value());
        receiveCouponDTO.setShopId(staffSendCouponDTO.getShopId());
        receiveCouponDTO.setStaffId(staffSendCouponDTO.getStaffId());
        receiveCouponDTO.setStaffName(staffSendCouponDTO.getStaffName());
        receiveCouponDTO.setStaffPhone(staffSendCouponDTO.getStaffPhone());
        tCouponUserService.receive(receiveCouponDTO);

        //更改库存
        if (pushActivityCouponMapper.updateCouponStocks(couponId,activityId,activityCoupon.getVersion()) < 1) {
            throw new LuckException("券被领完啦！!下次记得早点领取哦！");
        }

        return ServerResponseEntity.success();
    }


    @Override
    public ServerResponseEntity<Void> deleteShop(Long id,Long shopId,Boolean isAllShop) {
        if (isAllShop){
            PushCouponActivity pushCouponActivity = new PushCouponActivity();
            pushCouponActivity.setId(id);
            pushCouponActivity.setIsAllShop(true);
            pushCouponActivityMapper.updateById(pushCouponActivity);
            shopMapper.delete(new LambdaUpdateWrapper<PushCouponActivityShop>().eq(PushCouponActivityShop::getActivityId, id));
        }
        shopMapper.delete(new LambdaUpdateWrapper<PushCouponActivityShop>()
                .eq(PushCouponActivityShop::getActivityId, id)
                .eq(PushCouponActivityShop::getShopId, shopId));
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
    public ServerResponseEntity<ActivityReportVO> activityReport(ActivityReportDTO param) {
        //=============== 活动信息 ===============
        PushCouponActivity pushCouponActivity = pushCouponActivityMapper.selectById(param.getActivityId());
        ActivityReportVO result = BeanUtil.copyProperties(pushCouponActivity, ActivityReportVO.class);
        log.info("查询参数：{}", JSONObject.toJSONString(param));
        log.info("推券活动数据:{}",JSONObject.toJSONString(result));
        //=============== 券活动概况 ===============
        //统计指标：总领券数
        Integer receiveSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.PUSH.value(), param.getCouponInfo(), null);
        log.info("总领券数：{}",receiveSum);
        result.setReceiveSum(receiveSum);
        //统计指标：总核销数
        Integer writeOffSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.PUSH.value(), param.getCouponInfo(), "USED");
        log.info("总核销数：{}",writeOffSum);
        result.setWriteOffSum(writeOffSum);

        //统计指标：活动收入
        BigDecimal activityIncome = tCouponUserMapper.activityIncome(param.getActivityId(), ActivitySourceEnum.PUSH.value(), param.getCouponInfo(), "USED");
        log.info("活动收入:{}",activityIncome);
        result.setActivityIncome(activityIncome);


        //统计指标：券过期数
        Integer overdueSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.PUSH.value(), param.getCouponInfo(), "EXPIRED");
        result.setOverdueSum(overdueSum);
        log.info("券过期数：{}",overdueSum);

        //优惠券报表列表
        List<ActivityReportDetailVO> activityReportDetails = pushCouponActivityMapper.activityReportDetail(param.getActivityId(), param.getCouponInfo(), ActivitySourceEnum.PUSH.value());

        activityReportDetails.forEach(temp ->{
            if(temp.getWriteOffNum() == 0){
                temp.setWriteOffRatio(new BigDecimal("0"));
            }else {
                BigDecimal writeOffRatio = NumberUtil.div(String.valueOf(temp.getWriteOffNum()), String.valueOf(temp.getReceiveNum()));
                temp.setWriteOffRatio(writeOffRatio);
            }
//            if (temp.getActivityIncome() != null && temp.getActivityIncome() != 0) {
//                temp.setActivityIncome(temp.getActivityIncome()/100);
//            }
        });
        result.setActivityReportDetail(activityReportDetails);
        log.info("活动报表返回数据：{}",JSONObject.toJSONString(result));
        return ServerResponseEntity.success(result);
    }

    public Integer shopNum(Long id){
        //适用门店数量
        List<PushCouponActivityShop> shops = shopMapper.selectList(new LambdaQueryWrapper<PushCouponActivityShop>().eq(PushCouponActivityShop::getActivityId,id));
        return shops.size();
    }

    public void addShops(List<Long> shops, Long activityId){
        List<PushCouponActivityShop> shopList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shops)){
            shops.forEach(temp ->{
                PushCouponActivityShop shop = new PushCouponActivityShop();
                shop.setActivityId(activityId);
                shop.setShopId(temp);
                shopList.add(shop);
            });
            shopMapper.insertBatch(shopList);
        }

    }

    public void addCoupons(List<ActivityCouponDTO> codes, Long activityId){
        List<PushCouponActivityCoupon> codeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(codes)){
            codes.forEach(temp ->{
                PushCouponActivityCoupon coupon = new PushCouponActivityCoupon();
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
                if (temp.getDailyLimitNum()== 0){
                    coupon.setIsDailyLimit(false);
                }else {
                    coupon.setIsDailyLimit(true);
                }
                coupon.setDailyLimitNum(temp.getDailyLimitNum());
                codeList.add(coupon);
            });
            pushActivityCouponMapper.insertBatch(codeList);
        }

    }

    public List<Long> shops(Long activityId){
        List<PushCouponActivityShop> shops = shopMapper.selectList(new LambdaQueryWrapper<PushCouponActivityShop>().eq(PushCouponActivityShop::getActivityId,activityId));
        List<Long> result = shops.stream().map(PushCouponActivityShop::getShopId).collect(Collectors.toList());

        return result;
    }

}
