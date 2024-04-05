package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponDetailVO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.model.*;
import com.mall4j.cloud.group.service.*;
import com.mall4j.cloud.group.vo.PayActivityListVO;
import com.mall4j.cloud.group.vo.PayActivityVO;
import com.mall4j.cloud.group.vo.app.PayActivityInfoAppVO;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PayActivityBizServiceImpl implements PayActivityBizService {
    @Resource
    private PayActivityService payActivityService;
    @Resource
    private ActivityCommodityBizService activityCommodityBizService;
    @Resource
    private TCouponFeignClient tCouponFeignClient;
    @Resource
    private OrderFeignClient orderFeignClient;
    @Resource
    private PayActivityRecordService payActivityRecordService;
    @Resource
    private PayActivityShopService payActivityShopService;
    @Resource
    private CrmUserFeignClient crmUserFeignClient;

    @Override
    public ServerResponseEntity<Void> saveOrUpdatePayActivity(PayActivityDTO param) {
        PayActivity payActivity = BeanUtil.copyProperties(param, PayActivity.class);
        String applyCommodityIdsStr = param.getApplyCommodityIds();
        Integer id = param.getId();
        Date activityBeginTime = param.getActivityBeginTime();
        Date activityEndTime = param.getActivityEndTime();

//        if (null != id) {
//            PayActivity payInfo = payActivityService.getById(id);
//            Integer status = payInfo.getStatus();
//            if (1 == status) {
//                String applyCommodityIds = payInfo.getApplyCommodityIds();
//                List<String> commodityIds = Arrays.asList(applyCommodityIds.split(StringPool.COMMA));
//                activityCommodityBizService.removeActivityCommodity(commodityIds);
//
//                List<String> insertCommodityIds = Arrays.asList(applyCommodityIdsStr.split(StringPool.COMMA));
//                List<Long> insertCommodityIdsLong = Convert.toList(Long.class, insertCommodityIds);
//
//                String applyShopIds = param.getApplyShopIds();
//                List<String> shopIds = StrUtil.isNotBlank(applyShopIds)?Arrays.asList(applyShopIds.split(StringPool.COMMA)):null;
//                List<Long> storeIds = CollectionUtil.isNotEmpty(shopIds)?shopIds.stream().map(temp -> Long.parseLong(temp)).collect(Collectors.toList()):null;
//                ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdsLong,
//                        activityBeginTime,
//                        activityEndTime,
//                        ActivityChannelEnums.PAY_ACTIVITY.getCode(),
//                        Long.valueOf(id),
//                        storeIds);
//                log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
//                if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
//                    String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
//                    throw new LuckException(msg, activityCommodityAddDTO);
//                }
//            }
//        }
        payActivityService.saveOrUpdate(payActivity);

        Integer payActivityId = payActivity.getId();

        if (Objects.nonNull(param.getStatus()) && 1 == param.getStatus() && payActivityId!=null) {//保存并且启用需要校验商品池

            List<String> insertCommodityIds = Arrays.asList(applyCommodityIdsStr.split(StringPool.COMMA));
            List<Long> insertCommodityIdsLong = Convert.toList(Long.class, insertCommodityIds);

            String applyShopIds = param.getApplyShopIds();
            List<String> shopIds = StrUtil.isNotBlank(applyShopIds)?Arrays.asList(applyShopIds.split(StringPool.COMMA)):null;
            List<Long> storeIds = CollectionUtil.isNotEmpty(shopIds)?shopIds.stream().map(temp -> Long.parseLong(temp)).collect(Collectors.toList()):null;
            ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdsLong,
                    activityBeginTime,
                    activityEndTime,
                    ActivityChannelEnums.PAY_ACTIVITY.getCode(),
                    Long.valueOf(payActivityId),
                    storeIds);
            log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
            if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
                String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();

                payActivity.setStatus(0);
                payActivityService.updateById(payActivity);

                throw new LuckException(msg, activityCommodityAddDTO);
            }
        }

        String applyShopIds = param.getApplyShopIds();

        payActivityShopService.remove(new LambdaQueryWrapper<PayActivityShop>().eq(PayActivityShop::getActivityId,payActivityId));
        if (StringUtils.isNotEmpty(applyShopIds)){
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<PayActivityShop> payActivityShops = new ArrayList<>();
            shopIds.forEach(temp->{

                PayActivityShop payActivityShop = PayActivityShop.builder()
                        .activityId(payActivityId)
                        .shopId(Long.valueOf(temp)).build();
                payActivityShops.add(payActivityShop);

            });
            payActivityShopService.saveBatch(payActivityShops);
        }


        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PayActivityVO> detail(Integer id) {
        PayActivity payActivity = payActivityService.getById(id);
        PayActivityVO result = BeanUtil.copyProperties(payActivity, PayActivityVO.class);

        List<PayActivityShop> shops = payActivityShopService.list(new LambdaQueryWrapper<PayActivityShop>().eq(PayActivityShop::getActivityId, id));
        result.setShops(shops);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<PageVO<PayActivityListVO>> page(PayActivityPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();


        Page<PayActivityListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> payActivityService.payActivityList(param));

        List<PayActivityListVO> list = page.getResult();

        List<PayActivityListVO> resultList = list.stream().peek(temp -> {
                    Date activityBeginTime = temp.getActivityBeginTime();
                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getStatus();
                    // 优惠券名称获取
                    String activityCouponId = temp.getActivityCouponId();
                    if(StringUtils.isNotEmpty(activityCouponId)){
                        ServerResponseEntity<CouponDetailVO> couponDetail = tCouponFeignClient.getCouponDetail(Long.valueOf(activityCouponId));
                        CouponDetailVO data = couponDetail.getData();
                        temp.setActivityCouponName(data.getName());
                    }

                    temp.setParticipationAmount(null == temp.getParticipationAmount() ? new BigDecimal("0") :temp.getParticipationAmount());

                    if (!ActivityStatusEnums.NOT_ENABLED.getCode().equals(tempActivityStatus)) {
                        if (DateUtil.isIn(date, activityBeginTime, activityEndTime)) {
                            temp.setStatus(ActivityStatusEnums.IN_PROGRESS.getCode());
                        } else if (date.compareTo(activityBeginTime) < 0) {
                            temp.setStatus(ActivityStatusEnums.NOT_START.getCode());
                        } else if (date.compareTo(activityEndTime) > 0) {
                            temp.setStatus(ActivityStatusEnums.END.getCode());
                        }
                    }
                }
        ).collect(Collectors.toList());

        PageVO<PayActivityListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        PayActivity payActivity = payActivityService.getById(id);
        Date activityBeginTime = payActivity.getActivityBeginTime();
        Date activityEndTime = payActivity.getActivityEndTime();

        String applyCommodityIds = payActivity.getApplyCommodityIds();
        List<String> commodityIds = Arrays.asList(applyCommodityIds.split(StringPool.COMMA));
        List<Long> insertCommodityIdsLong = Convert.toList(Long.class, commodityIds);

        List<PayActivityShop> shops = payActivity.getIsAllShop()==0?payActivityShopService.list(new LambdaQueryWrapper<PayActivityShop>().eq(PayActivityShop::getActivityId, id)):null;
        List<Long> storeIds = CollectionUtil.isNotEmpty(shops)?shops.stream().map(temp -> temp.getShopId()).collect(Collectors.toList()):null;
        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdsLong,
                activityBeginTime,
                activityEndTime,
                ActivityChannelEnums.PAY_ACTIVITY.getCode(),
                Long.valueOf(id),
                storeIds);
        log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
            String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
            throw new LuckException(msg, activityCommodityAddDTO);
        }

//        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdsLong, activityBeginTime, activityEndTime,ActivityChannelEnums.PAY_ACTIVITY.getCode(),Long.valueOf(id));
//        List<String> failCommodityIds = activityCommodityAddDTO.getFailCommodityIds();
//        if (CollectionUtil.isNotEmpty(failCommodityIds)) {
//            throw new LuckException("当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息", activityCommodityAddDTO);
//        }

        payActivityService.update(new LambdaUpdateWrapper<PayActivity>()
                .set(PayActivity::getStatus, 1)
                .eq(PayActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        PayActivity payActivity = payActivityService.getById(id);

//        String applyCommodityIds = payActivity.getApplyCommodityIds();
//        List<String> commodityIds = Arrays.asList(applyCommodityIds.split(StringPool.COMMA));
//        activityCommodityBizService.removeActivityCommodity(commodityIds);

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(id),ActivityChannelEnums.PAY_ACTIVITY.getCode());

        payActivityService.update(new LambdaUpdateWrapper<PayActivity>()
                .set(PayActivity::getStatus, 0)
                .eq(PayActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        payActivityService.update(new LambdaUpdateWrapper<PayActivity>()
                .set(PayActivity::getDeleted, 1)
                .eq(PayActivity::getId, id));

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(id),ActivityChannelEnums.PAY_ACTIVITY.getCode());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<PayActivityShop>> getActivityShop(Integer activityId) {
        List<PayActivityShop> list = payActivityShopService.list(new LambdaQueryWrapper<PayActivityShop>().eq(PayActivityShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<PayActivityShop> payActivityShops = new ArrayList<>();
        shopIds.forEach(temp->{
            PayActivityShop payActivityShop = PayActivityShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            payActivityShops.add(payActivityShop);
        });

        payActivityShopService.saveBatch(payActivityShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId) {
        payActivityShopService.remove(new LambdaQueryWrapper<PayActivityShop>()
                .eq(PayActivityShop::getActivityId,activityId)
                .eq(PayActivityShop::getShopId,shopId));

        List<PayActivityShop> list = payActivityShopService.list(new LambdaQueryWrapper<PayActivityShop>().eq(PayActivityShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)){
            payActivityService.update(null,new LambdaUpdateWrapper<PayActivity>()
                    .set(PayActivity::getIsAllShop,1)
                    .eq(PayActivity::getId,activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        payActivityShopService.remove(new LambdaQueryWrapper<PayActivityShop>()
                .eq(PayActivityShop::getActivityId,activityId));
        payActivityService.update(null,new LambdaUpdateWrapper<PayActivity>()
                .set(PayActivity::getIsAllShop,1)
                .eq(PayActivity::getId,activityId));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PayActivityInfoAppVO> info(String shopId,Long orderId) {

        PayActivity payActivity = payActivityService.selectFirstActivity(shopId);

        if (null == payActivity){
            throw new LuckException("该门店无支付有礼活动");
        }
        PayActivityInfoAppVO payActivityInfoAppVO = BeanUtil.copyProperties(payActivity, PayActivityInfoAppVO.class);

        String activityCouponId = payActivityInfoAppVO.getActivityCouponId();

        List<PayActivityRecord> payActivityRecords = payActivityRecordService.list(new LambdaQueryWrapper<PayActivityRecord>().eq(PayActivityRecord::getOrderId, orderId));
        Integer activityCouponSwitch = payActivityInfoAppVO.getActivityCouponSwitch();
        Integer activityPointSwitch = payActivityInfoAppVO.getActivityPointSwitch();
        if (activityCouponSwitch == 1){
            List<PayActivityRecord> couponList = payActivityRecords.stream().filter(temp -> temp.getDrawType() == 1).collect(Collectors.toList());
            payActivityInfoAppVO.setCouponFlag(CollectionUtil.isEmpty(couponList));
        }
        if (activityPointSwitch == 1){
            List<PayActivityRecord> pointList = payActivityRecords.stream().filter(temp -> temp.getDrawType() == 2).collect(Collectors.toList());
            payActivityInfoAppVO.setPointFlag(CollectionUtil.isEmpty(pointList));
        }

        boolean couponFlag = payActivityInfoAppVO.isCouponFlag();
        boolean pointFlag = payActivityInfoAppVO.isPointFlag();
        if (!couponFlag && !pointFlag){
            throw new LuckException("该订单已领取过奖励!");
        }

        if (StringUtils.isNotEmpty(activityCouponId)){
            ServerResponseEntity<CouponDetailVO> couponDetail = tCouponFeignClient.getCouponDetail(Long.valueOf(activityCouponId));
            CouponDetailVO data = couponDetail.getData();

            payActivityInfoAppVO.setCouponDetail(data);
        }

        return ServerResponseEntity.success(payActivityInfoAppVO);
    }

    @Override
    public ServerResponseEntity<Void> draw(PayActivityDrawAppDTO param) {
        Date date = new Date();
        Long orderId = param.getOrderId();
        Integer drawType = param.getDrawType();
        Integer id = param.getId();
        Long userId = param.getUserId();
        DateTime beginOfDay = DateUtil.beginOfDay(date);
        DateTime endOfDay = DateUtil.endOfDay(date);

        PayActivity payActivity = payActivityService.getById(id);
        BigDecimal consumptionAmount = payActivity.getConsumptionAmount();
        Integer dayMaxOrderNum = payActivity.getDayMaxOrderNum();
        Integer minOrderTotalNum = payActivity.getMinOrderTotalNum();


        //校验订单金额
        ServerResponseEntity<EsOrderBO> esOrder = orderFeignClient.getEsOrder(orderId);
        EsOrderBO data = esOrder.getData();
        Long actualTotal = data.getActualTotal();

        long consumptionAmountLong = consumptionAmount.multiply(new BigDecimal("100")).longValue();

        if (actualTotal < consumptionAmountLong){
            throw new LuckException("订单金额未达标");
        }

        List<PayActivityRecord> orderRecords = payActivityRecordService.list(new LambdaQueryWrapper<PayActivityRecord>()
                .eq(PayActivityRecord::getUserId, userId)
                .eq(PayActivityRecord::getOrderId, orderId)
                .eq(PayActivityRecord::getDrawType, drawType));
        if (CollectionUtil.isNotEmpty(orderRecords)){
            throw new LuckException("该订单已领取过奖励");
        }

        List<PayActivityRecord> payActivityRecords = payActivityRecordService.list(new LambdaQueryWrapper<PayActivityRecord>()
                .eq(PayActivityRecord::getActivityId, id)
                .eq(PayActivityRecord::getUserId, userId)
                .eq(PayActivityRecord::getDrawType, drawType));

        List<PayActivityRecord> dayRecord = payActivityRecords.stream().filter(temp -> DateUtil.isIn(temp.getCreateTime(), beginOfDay, endOfDay)).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(dayRecord) && dayRecord.size() == dayMaxOrderNum){
            throw new LuckException("今日领取已达上限");
        }

        if (CollectionUtil.isNotEmpty(payActivityRecords) && payActivityRecords.size()>=minOrderTotalNum){
            throw new LuckException("已达累计领取上限");
        }

        if (1==drawType){
            String activityCouponId = payActivity.getActivityCouponId();
            ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
            receiveCouponDTO.setActivityId(Long.valueOf(id));
            receiveCouponDTO.setUserId(userId);
            receiveCouponDTO.setCouponId(Long.valueOf(activityCouponId));
            receiveCouponDTO.setActivitySource(ActivitySourceEnum.PAY_ACTIVITY.value());
            tCouponFeignClient.receive(receiveCouponDTO);
        }

        if (2 == drawType){
            Integer activityPointNumber = payActivity.getActivityPointNumber();
            // 赠送积分
            UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
            updateScoreDTO.setUserId(userId);
            updateScoreDTO.setPoint_value(activityPointNumber);
            updateScoreDTO.setSource("完善资料赠送积分");
            updateScoreDTO.setPoint_channel("wechat");
            updateScoreDTO.setPoint_type("SKX_JLJF");
            updateScoreDTO.setRemark("完善资料赠送积分");
            updateScoreDTO.setIoType(1);
            crmUserFeignClient.updateScore(updateScoreDTO);
            log.info("payActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
        }

        //记录订单金额
        BigDecimal bigDecimal = new BigDecimal(actualTotal);
        BigDecimal orderTotal = bigDecimal.divide(new BigDecimal(100));
        PayActivityRecord payActivityRecord = PayActivityRecord.builder()
                .activityId(id)
                .orderAmount(orderTotal)
                .drawType(drawType)
                .orderId(orderId)
                .userId(userId).build();

        payActivityRecordService.save(payActivityRecord);
        return ServerResponseEntity.success();
    }
}
