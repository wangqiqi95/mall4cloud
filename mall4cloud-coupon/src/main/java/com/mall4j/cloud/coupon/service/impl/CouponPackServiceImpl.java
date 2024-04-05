package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.coupon.dto.BatchReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerGetDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.CustomerGetVo;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.api.order.dto.CheckOrderDTO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.feign.SysUserClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.ActivityStatusEnums;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.mapper.CouponPackActivityMapper;
import com.mall4j.cloud.coupon.mapper.CouponPackDrawRecordMapper;
import com.mall4j.cloud.coupon.mapper.CouponPackStockLogMapper;
import com.mall4j.cloud.coupon.mapper.TCouponUserMapper;
import com.mall4j.cloud.coupon.model.*;
import com.mall4j.cloud.coupon.service.CouponPackService;
import com.mall4j.cloud.coupon.service.CouponPackShopService;
import com.mall4j.cloud.coupon.vo.*;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class CouponPackServiceImpl extends ServiceImpl<CouponPackActivityMapper, CouponPackActivity> implements CouponPackService {
    @Resource
    private CouponPackActivityMapper couponPackActivityMapper;
    @Resource
    private TCouponFeignClient tCouponFeignClient;
    @Resource
    private CrmCustomerFeignClient crmCustomerFeignClient;
    @Resource
    private OrderFeignClient orderFeignClient;
    @Resource
    private StoreFeignClient storeFeignClient;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private CouponPackShopService couponPackShopService;
    @Resource
    private CouponPackDrawRecordMapper couponPackDrawRecordMapper;
    @Resource
    private SysUserClient sysUserClient;
    @Resource
    private CouponPackStockLogMapper couponPackStockLogMapper;
    @Resource
    private TCouponUserMapper tCouponUserMapper;

    @Override
    public ServerResponseEntity<Void> saveOrUpdateCouponPackActivity(CouponPackDTO param) {
        Integer id = param.getId();
        Integer initialCouponStock = param.getInitialCouponStock();
        String applyShopIds = param.getApplyShopIds();

        // 20221115新增了消费限制入参
        CouponPackActivity couponPackActivity = BeanUtil.copyProperties(param, CouponPackActivity.class);
        if (null == id){
            couponPackActivityMapper.insert(couponPackActivity);
        }else {
            couponPackShopService.remove(new LambdaQueryWrapper<CouponPackShop>().eq(CouponPackShop::getActivityId,id));
            couponPackActivityMapper.updateById(couponPackActivity);
        }

        id = couponPackActivity.getId();
        String redisKey = "couponPack::stock::"+id;
        if (StringUtils.isNotEmpty(applyShopIds)){
            Integer packActivityId = couponPackActivity.getId();
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<CouponPackShop> couponPackShops = new ArrayList<>();
            shopIds.forEach(temp->{
                CouponPackShop couponPackShop = CouponPackShop.builder()
                        .activityId(packActivityId)
                        .shopId(Long.valueOf(temp)).build();
                couponPackShops.add(couponPackShop);
            });
            couponPackShopService.saveBatch(couponPackShops);
        }
        RedisUtil.set(redisKey,initialCouponStock,-1);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<CouponPackVO> detail(Integer id) {
        CouponPackActivity couponPackActivity = couponPackActivityMapper.selectById(id);
        CouponPackVO couponPackVO = BeanUtil.copyProperties(couponPackActivity, CouponPackVO.class);
        List<CouponPackShop> list = couponPackShopService.list(new LambdaQueryWrapper<CouponPackShop>().eq(CouponPackShop::getActivityId, id));
        couponPackVO.setCouponPackShops(list);

        return ServerResponseEntity.success(couponPackVO);
    }

    @Override
    public ServerResponseEntity<PageVO<CouponPackListVO>> couponPackPage(CouponPackPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();


        Page<CouponPackListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> couponPackActivityMapper.couponPackList(param));

        List<CouponPackListVO> list = page.getResult();

        List<CouponPackListVO> resultList = list.stream().peek(temp -> {

                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getStatus();
                    Date activityBeginTime = temp.getActivityBeginTime();

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

        PageVO<CouponPackListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<PageVO<CouponPackListVO>> selectCouponPack(CouponPackSelectDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();


        Page<CouponPackListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> couponPackActivityMapper.couponPackActivityList(param));

        List<CouponPackListVO> list = page.getResult();

        List<CouponPackListVO> resultList = list.stream().peek(temp -> {
                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getStatus();
                    Date activityBeginTime = temp.getActivityBeginTime();

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

        PageVO<CouponPackListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        couponPackActivityMapper.update(null,new LambdaUpdateWrapper<CouponPackActivity>()
                .set(CouponPackActivity::getStatus,1)
                .eq(CouponPackActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        couponPackActivityMapper.update(null,new LambdaUpdateWrapper<CouponPackActivity>()
                .set(CouponPackActivity::getStatus,0)
                .eq(CouponPackActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        couponPackActivityMapper.update(null,new LambdaUpdateWrapper<CouponPackActivity>()
                .set(CouponPackActivity::getDeleted,1)
                .eq(CouponPackActivity::getId,id));
        return ServerResponseEntity.success();
    }



    @Override
    public ServerResponseEntity<Void> AddStock(AddStockDTO param) {
        String username = AuthUserContext.get().getUsername();
        Long userId = AuthUserContext.get().getUserId();
        ServerResponseEntity<SysUserVO> sysUserByUserId = sysUserClient.getSysUserByUserId(userId);
        SysUserVO data = sysUserByUserId.getData();
        String staffNo = data.getCode();

        Integer id = param.getId();
        String redisKey = "couponPack::stock::"+id;
        Integer stock = param.getStock();

        couponPackActivityMapper.addStock(id,stock);
        RedisUtil.incr(redisKey,stock);

        //记录调整日志
        CouponPackStockLog couponPackStockLog = CouponPackStockLog.builder()
                .activityId(id)
                .optStock(stock)
                .optUserName(username)
                .optUserNo(staffNo).build();
        couponPackStockLogMapper.insert(couponPackStockLog);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<CouponPackShop>> getActivityShop(Integer activityId) {
        List<CouponPackShop> list = couponPackShopService.list(new LambdaQueryWrapper<CouponPackShop>().eq(CouponPackShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<ActivityReportVO> activityReport(ActivityReportDTO param) {
        //=============== 活动信息 ===============
        CouponPackActivity couponPackActivity = couponPackActivityMapper.selectById(param.getActivityId());
        ActivityReportVO result = BeanUtil.copyProperties(couponPackActivity, ActivityReportVO.class);
        result.setTitle(couponPackActivity.getActivityName());
        result.setStartTime(couponPackActivity.getActivityBeginTime());
        result.setEndTime(couponPackActivity.getActivityEndTime());
        log.info("查询参数：{}", JSONObject.toJSONString(param));
        log.info("推券活动数据:{}",JSONObject.toJSONString(result));
        //=============== 券活动概况 ===============
        //统计指标：总领券数
        Integer receiveSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.COUPON_PACK.value(), param.getCouponInfo(), null);
        log.info("总领券数：{}",receiveSum);
        result.setReceiveSum(receiveSum);
        //统计指标：总核销数
        Integer writeOffSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.COUPON_PACK.value(), param.getCouponInfo(), "USED");
        log.info("总核销数：{}",writeOffSum);
        result.setWriteOffSum(writeOffSum);

        //统计指标：活动收入
        BigDecimal activityIncome = tCouponUserMapper.activityIncome(param.getActivityId(), ActivitySourceEnum.COUPON_PACK.value(), param.getCouponInfo(), "USED");
        log.info("活动收入:{}",activityIncome);
        result.setActivityIncome(activityIncome);

        //统计指标：券过期数
        Integer overdueSum = tCouponUserMapper.indexStatisticsCount(param.getActivityId(), ActivitySourceEnum.COUPON_PACK.value(), param.getCouponInfo(), "EXPIRED");
        result.setOverdueSum(overdueSum);
        log.info("券过期数：{}",overdueSum);

        if (StrUtil.isNotBlank(couponPackActivity.getCouponIds())) {
            String[] couponIds = couponPackActivity.getCouponIds().split(",");
            List<ActivityReportDetailVO> activityReportDetails = new ArrayList<>();
            for (int i = 0; i < couponIds.length; i++) {
                //优惠券报表列表
                ActivityReportDetailVO activityReportDetail = couponPackActivityMapper.activityReportDetail(param.getActivityId(), param.getCouponInfo(), ActivitySourceEnum.COUPON_PACK.value() ,couponIds[i]);
                if(activityReportDetail.getWriteOffNum() == 0){
                    activityReportDetail.setWriteOffRatio(new BigDecimal("0"));
                }else {
                    BigDecimal writeOffRatio = NumberUtil.div(String.valueOf(activityReportDetail.getWriteOffNum()), String.valueOf(activityReportDetail.getReceiveNum()));
                    activityReportDetail.setWriteOffRatio(writeOffRatio);
                }
                activityReportDetails.add(activityReportDetail);
            }
            result.setActivityReportDetail(activityReportDetails);
        }
        log.info("活动报表返回数据：{}",JSONObject.toJSONString(result));
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<CouponPackShop> couponPackShops = new ArrayList<>();
        shopIds.forEach(temp->{
            CouponPackShop couponPackShop = CouponPackShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            couponPackShops.add(couponPackShop);
        });

        couponPackShopService.saveBatch(couponPackShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId ,Integer shopId) {
        couponPackShopService.remove(new LambdaQueryWrapper<CouponPackShop>()
                .eq(CouponPackShop::getActivityId,activityId)
                .eq(CouponPackShop::getShopId,shopId));

        List<CouponPackShop> list = couponPackShopService.list(new LambdaQueryWrapper<CouponPackShop>().eq(CouponPackShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)){
            couponPackActivityMapper.update(null,new LambdaUpdateWrapper<CouponPackActivity>()
                    .set(CouponPackActivity::getIsAllShop,1)
                    .eq(CouponPackActivity::getId,activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        couponPackShopService.remove(new LambdaQueryWrapper<CouponPackShop>()
                .eq(CouponPackShop::getActivityId,activityId));
        couponPackActivityMapper.update(null,new LambdaUpdateWrapper<CouponPackActivity>()
                .set(CouponPackActivity::getIsAllShop,1)
                .eq(CouponPackActivity::getId,activityId));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<CouponPackInfoVO> info(Long activityId) {
        CouponPackActivity couponPackActivity = couponPackActivityMapper.selectById(activityId);

        if (null == couponPackActivity){
            throw new LuckException("无券包活动");
        }

        Date date = new Date();
        Date activityBeginTime = couponPackActivity.getActivityBeginTime();
        Date activityEndTime = couponPackActivity.getActivityEndTime();
        Integer status = couponPackActivity.getStatus();

        Integer id = couponPackActivity.getId();
        List<CouponPackShop> shops = couponPackShopService.list(new LambdaQueryWrapper<CouponPackShop>().eq(CouponPackShop::getActivityId, id));
        CouponPackInfoVO couponPackInfoVO = BeanUtil.copyProperties(couponPackActivity, CouponPackInfoVO.class);

        // 获取优惠券信息
        List<String> couponList = Arrays.asList(couponPackActivity.getCouponIds().split(StringPool.COMMA));
        List<Long> couponIds = Convert.toList(Long.class, couponList);
        ServerResponseEntity<List<CouponListVO>> listServerResponseEntity = tCouponFeignClient.selectCouponByIds(couponIds);
        List<CouponListVO> coupons = listServerResponseEntity.getData();

        couponPackInfoVO.setCoupons(coupons);
        couponPackInfoVO.setShops(shops);

        if (!ActivityStatusEnums.NOT_ENABLED.getCode().equals(status)) {
            if (DateUtil.isIn(date, activityBeginTime, activityEndTime)) {
                couponPackInfoVO.setStatus(ActivityStatusEnums.IN_PROGRESS.getCode());
            } else if (date.compareTo(activityBeginTime) < 0) {
                couponPackInfoVO.setStatus(ActivityStatusEnums.NOT_START.getCode());
            } else if (date.compareTo(activityEndTime) > 0) {
                couponPackInfoVO.setStatus(ActivityStatusEnums.END.getCode());
            }
        }
        return ServerResponseEntity.success(couponPackInfoVO);
    }

    @Override
    public ServerResponseEntity<Void> draw(CouponPackDrawDTO param) {
        Long storeId = param.getStoreId();
        Long userId = param.getUserId();//AuthUserContext.get().getUserId()
        Integer id = param.getId();
        Date date = new Date();
        String redisKey = "couponPack::stock::"+id;

        CouponPackActivity couponPackActivity = couponPackActivityMapper.selectById(id);
        if (ObjectUtils.isEmpty(couponPackActivity)){
            throw new LuckException("活动不存在");
        }
        if(couponPackActivity.getStatus() == 0){
            throw new LuckException("活动已失效");
        }
        if (System.currentTimeMillis() < couponPackActivity.getActivityBeginTime().getTime()) {
            throw new LuckException("活动未开始，无法领取优惠券！");
        }
        if (System.currentTimeMillis() > couponPackActivity.getActivityEndTime().getTime()) {
            throw new LuckException("活动已结束，无法领取优惠券！");
        }
        Integer isAllShop = couponPackActivity.getIsAllShop();
        CouponPackShop couponPackShop = couponPackShopService.getOne(new LambdaQueryWrapper<CouponPackShop>()
                .eq(CouponPackShop::getShopId, storeId)
                .eq(CouponPackShop::getActivityId, id));
        if (1 != isAllShop && null == couponPackShop){
            log.info("isAllShop:{},storeId:{},id:{}",isAllShop,storeId,id);
            throw new LuckException("您登录的门店未参与本次兑换，详情请见店宣。");
        }

        List<CouponPackDrawRecord> couponPackDrawRecords = couponPackDrawRecordMapper.selectList(new LambdaQueryWrapper<CouponPackDrawRecord>()
                .eq(CouponPackDrawRecord::getUserId, userId)
                .eq(CouponPackDrawRecord::getActivityId, id));
        Integer receiveTimes = couponPackActivity.getReceiveTimes();
        Integer dayLimit = couponPackActivity.getDayLimit();

        List<CouponPackDrawRecord> drawRecords = couponPackDrawRecords.stream().filter(temp -> DateUtil.isIn(temp.getReceiveTime(), DateUtil.beginOfDay(date), DateUtil.endOfDay(date))).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(drawRecords) && drawRecords.size() >= dayLimit){
            throw new LuckException("领券次数超出每日限制");
        }
        if (couponPackDrawRecords.size() == receiveTimes){
            throw new LuckException("领券次数已达上限");
        }

        // 消费限制校验 判断用户订单消费金额是否满足条件
        ExpendConditionDTO expendConditionDTO = BeanUtil.copyProperties(couponPackActivity, ExpendConditionDTO.class);
        extracted(userId, expendConditionDTO);

        String couponIds = couponPackActivity.getCouponIds();
        List<String> couponList = Arrays.asList(couponIds.split(StringPool.COMMA));
        //校验库存
        Long decr = RedisUtil.decr(redisKey, 1);
        if (decr < 0){
            RedisUtil.incr(redisKey,1);
            throw new LuckException("券已领完，欢迎下次参与。");
        }
        log.info("当前券包活动入参数据:{}",JSONObject.toJSONString(param));
        log.info("当前参与券包活动领取前库存数量:{}", couponPackActivity.getCurCouponStock());
        couponPackActivityMapper.reduceStock(id);
        CouponPackActivity packActivity = couponPackActivityMapper.selectById(id);
        log.info("当前参与券包活动领取后库存数量:{}", packActivity.getCurCouponStock());

        // 发放优惠券
        List<Long> couponLongIds = Convert.toList(Long.class, couponList);
        BatchReceiveCouponDTO batchReceiveCouponDTO = new BatchReceiveCouponDTO();
        batchReceiveCouponDTO.setCouponIds(couponLongIds);
        batchReceiveCouponDTO.setActivityId(Long.valueOf(id));
        batchReceiveCouponDTO.setActivitySource(ActivitySourceEnum.COUPON_PACK.value());
        batchReceiveCouponDTO.setUserId(userId);
        tCouponFeignClient.batchReceive(batchReceiveCouponDTO);

        CouponPackDrawRecord couponPackDrawRecord = CouponPackDrawRecord.builder()
                .activityId(id)
                .userId(userId)
                .shopId(storeId).build();

        couponPackDrawRecordMapper.insert(couponPackDrawRecord);

        return ServerResponseEntity.success();
    }

    @Override
    public void extracted(Long userId, ExpendConditionDTO expendConditionDTO) {
        ServerResponseEntity<UserApiVO> userApiVO = userFeignClient.getUserById(userId);
        UserApiVO user = userApiVO.getData();
        log.info("进入消费限制方法,当前用户为:{},用户详情信息为:{},方法入参为:{}", userId, user, expendConditionDTO);
        if (ObjectUtil.isEmpty(user)) {
            throw new LuckException("请您先注册成为斯凯奇会员");
        }
        CustomerGetDto customerGetDto = new CustomerGetDto();
        customerGetDto.setMobile(user.getPhone());
        // 调用crm接口获取用户信息
        ServerResponseEntity<CustomerGetVo> customerGetVoServerResponseEntity = crmCustomerFeignClient.customerGet(customerGetDto);
        if (customerGetVoServerResponseEntity != null && customerGetVoServerResponseEntity.isSuccess() && customerGetVoServerResponseEntity.getData() != null) {
            CustomerGetVo crmUserData = customerGetVoServerResponseEntity.getData();
            log.info("调用crm接口查询会员信息,{}", crmUserData);
            // 校验用户会员等级
            String fanLevels = expendConditionDTO.getFanLevels();
            if (StringUtils.isNotEmpty(fanLevels)) {
                List<String> userLevelList = Arrays.asList(fanLevels.split(StringPool.COMMA));
                //List<Integer> userLevelList = Convert.toList(Integer.class, strings);
                log.info("进入等级校验中,活动要求会员等级为:{},转成String类型List后:{}", fanLevels, userLevelList);
                if (!userLevelList.contains(crmUserData.getCurrent_grade_id())) {
                    if (StrUtil.isNotBlank(expendConditionDTO.getFanTips())) {
                        throw new LuckException(expendConditionDTO.getFanTips());
                    } else {
                        throw new LuckException("未达到参与活动的条件");
                    }
                }
            }
        }

        // 校验消费开关
        if (expendConditionDTO.getOrderSwitch() == 1) {
            Long orderNum = expendConditionDTO.getOrderNum();
            CheckOrderDTO checkOrderDTO = new CheckOrderDTO();
            checkOrderDTO.setUserId(userId);
            checkOrderDTO.setPhone(user.getPhone());
            checkOrderDTO.setStartTime(DateUtil.formatDateTime(expendConditionDTO.getOrderStartTime()));
            checkOrderDTO.setEndTime(DateUtil.formatDateTime(expendConditionDTO.getOrderEndTime()));
            checkOrderDTO.setOrderType(expendConditionDTO.getOrderType());
            if ((expendConditionDTO.getOrderExpendType() == null || expendConditionDTO.getOrderExpendType() == 0) && orderNum != null && orderNum >= 0) {
                orderNum = orderNum * 100;
                String startTime = DateUtil.formatDateTime(expendConditionDTO.getOrderStartTime());
                String endTime = DateUtil.formatDateTime(expendConditionDTO.getOrderEndTime());
                String orderType = expendConditionDTO.getOrderType();
                log.info("判断用户消金额查询条件:{},{},{},{}", userId, startTime, endTime, orderType);
                ServerResponseEntity<Long> responseEntity = orderFeignClient.userConsumeAmount(userId, startTime, endTime, orderType);
                if (responseEntity != null && responseEntity.isSuccess() && responseEntity.getData() != null) {
                    Long orderPay = responseEntity.getData();
                    log.info("判断用户消金额是否满足条件:{},{}", orderNum, orderPay);
                    if (orderPay <= orderNum) {
                        if (StrUtil.isNotBlank(expendConditionDTO.getOrderTips())) {
                            throw new LuckException(expendConditionDTO.getOrderTips());
                        } else {
                            throw new LuckException("未达到参与活动的条件");
                        }
                    }
                }

                if (StrUtil.isNotBlank(expendConditionDTO.getAppointShop())) {
                    String[] types = expendConditionDTO.getAppointShop().split(",");
                    List<Long> storeIds = Arrays.stream(types).map(type -> {
                        return Long.valueOf(type);
                    }).collect(Collectors.toList());
                    ServerResponseEntity<List<StoreVO>> storeList = storeFeignClient.listByStoreIdList(storeIds);
                    List<String> storeCodes = storeList.getData().stream().map(StoreVO::getStoreCode).collect(Collectors.toList());
                    checkOrderDTO.setStoreIdList(storeIds);
                    checkOrderDTO.setStoreCodeList(storeCodes);
                    ServerResponseEntity<Integer> isResult = orderFeignClient.checkIsOrderIntoShops(checkOrderDTO);
                    log.info("消费校验返回数据,{}", isResult.getData());
                    if (isResult.getData() == 0) {
                        throw new LuckException("尚未在指定消费门店中进行消费，无法进行兑换！");
                    }
                }
            } else {
                // 获取指定消费门店,然后查询订单.看在活动时间范围内是否在指定门店中进行了消费
                if(expendConditionDTO.getOrderExpendType() != null && expendConditionDTO.getOrderExpendType() == 1){
                    checkOrderDTO.setOrderNum(expendConditionDTO.getOrderNum());
                }
                if (StrUtil.isNotBlank(expendConditionDTO.getAppointShop())) {
                    String[] types = expendConditionDTO.getAppointShop().split(",");
                    List<Long> storeIds = Arrays.stream(types).map(type -> {
                        return Long.valueOf(type);
                    }).collect(Collectors.toList());
                    ServerResponseEntity<List<StoreVO>> storeList = storeFeignClient.listByStoreIdList(storeIds);
                    List<String> storeCodes = storeList.getData().stream().map(StoreVO::getStoreCode).collect(Collectors.toList());
                    checkOrderDTO.setStoreIdList(storeIds);
                    checkOrderDTO.setStoreCodeList(storeCodes);
                }
                ServerResponseEntity<Integer> isResult = orderFeignClient.checkIsOrderIntoShops(checkOrderDTO);
                log.info("消费校验返回数据,{}", isResult.getData());
                if (isResult.getData() == 0) {
                    if (StrUtil.isNotBlank(expendConditionDTO.getOrderTips())) {
                        throw new LuckException(expendConditionDTO.getOrderTips());
                    } else {
                        throw new LuckException("尚未在指定消费门店中进行消费，无法进行兑换！");
                    }
                }
            }
        }
    }

    @Override
    public ServerResponseEntity<PageVO<CouponPackStockLog>> stockLog(Integer activityId, PageDTO param) {
        PageVO<CouponPackStockLog> page = PageUtil.doPage(param, () -> couponPackStockLogMapper.selectList(new LambdaQueryWrapper<CouponPackStockLog>()
                .eq(CouponPackStockLog::getActivityId, activityId)
                .orderByDesc(CouponPackStockLog::getOptTime)
        ));

        return ServerResponseEntity.success(page);
    }
}
