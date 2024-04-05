package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponDetailVO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.model.SignActivity;
import com.mall4j.cloud.group.model.SignActivityReward;
import com.mall4j.cloud.group.model.UserSignNotice;
import com.mall4j.cloud.group.model.UserSignReward;
import com.mall4j.cloud.group.service.*;
import com.mall4j.cloud.group.vo.*;
import com.mall4j.cloud.group.vo.app.SignActivityAppVO;
import com.mall4j.cloud.group.vo.app.SignRewardVO;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SignActivityBizServiceImpl implements SignActivityBizService {
    @Resource
    private SignActivityService signActivityService;
    @Resource
    private SignActivityRewardService signActivityRewardService;
    @Resource
    private UserSignRewardService userSignRewardService;
    @Resource
    private UserSignNoticeService userSignNoticeService;
    @Resource
    private TCouponFeignClient tCouponFeignClient;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private StoreFeignClient storeFeignClient;
    @Resource
    private CrmUserFeignClient crmUserFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    StaffFeignClient staffFeignClient;

    @Override
    public ServerResponseEntity<Void> saveOrUpdateSignActivity(SignActivityDTO param) {
        SignActivity signActivity = BeanUtil.copyProperties(param, SignActivity.class);
        List<SignActivityRewardDTO> rewards = param.getRewards();

        signActivityService.saveOrUpdate(signActivity);

        Integer id = signActivity.getId();
        if (CollectionUtil.isNotEmpty(rewards)) {
            List<SignActivityReward> signActivityRewards = Convert.toList(SignActivityReward.class, rewards);

            signActivityRewardService.remove(new LambdaQueryWrapper<SignActivityReward>().eq(SignActivityReward::getSignActivityId,id));

            List<SignActivityReward> rewardList = signActivityRewards.stream().peek(temp -> {
                temp.setSignActivityId(id);
                temp.setId(null);
            }).collect(Collectors.toList());

            signActivityRewardService.saveOrUpdateBatch(rewardList);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<SignActivityVO> detail(Integer id) {
        SignActivity signActivity = signActivityService.getById(id);

        List<SignActivityReward> activityRewards = signActivityRewardService.list(new LambdaQueryWrapper<SignActivityReward>().eq(SignActivityReward::getSignActivityId, id));

        SignActivityVO signActivityVO = BeanUtil.copyProperties(signActivity, SignActivityVO.class);

        List<SignActivityRewardVO> rewards = Convert.toList(SignActivityRewardVO.class, activityRewards);

        signActivityVO.setRewards(rewards);

        return ServerResponseEntity.success(signActivityVO);
    }

    @Override
    public ServerResponseEntity<PageVO<SignActivityListVO>> page(SignActivityPageDTO param) {
        String activityName = param.getActivityName();
        Integer activityStatus = param.getActivityStatus();
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();

        IPage<SignActivity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SignActivity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(activityName)){
            wrapper.like(SignActivity::getActivityName, activityName);
        }

        if (null != activityStatus){
            if (ActivityStatusEnums.NOT_ENABLED.getCode().equals(activityStatus)) {
                wrapper.eq(SignActivity::getActivityStatus, activityStatus);
            } else if (ActivityStatusEnums.IN_PROGRESS.getCode().equals(activityStatus)) {
                wrapper.eq(SignActivity::getActivityStatus, 1).and(wrapper1->wrapper1.eq(SignActivity::getActivityType, 0).and(wrapper3->wrapper3.le(SignActivity::getActivityBeginTime,date)).or(wrapper2-> wrapper2.gt(SignActivity::getActivityEndTime, date).le(SignActivity::getActivityBeginTime, date)));
            } else if (ActivityStatusEnums.NOT_START.getCode().equals(activityStatus)) {
                wrapper.eq(SignActivity::getActivityStatus, 1).gt(SignActivity::getActivityBeginTime, date);
            } else if (ActivityStatusEnums.END.getCode().equals(activityStatus)) {
                wrapper.eq(SignActivity::getActivityStatus, 1).lt(SignActivity::getActivityEndTime, date);
            }
        }
        wrapper.eq(SignActivity::getDeleted,0);
        wrapper.orderByDesc(SignActivity::getCreateTime);

        IPage<SignActivity> activityIPage = signActivityService.page(page, wrapper);

        List<SignActivity> records = activityIPage.getRecords();

        List<SignActivityListVO> list = Convert.toList(SignActivityListVO.class, records);

        List<SignActivityListVO> resultList = list.stream().peek(temp -> {
                    int activityType = temp.getActivityType();
                    Date activityBeginTime = temp.getActivityBeginTime();
                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getActivityStatus();

                    if (!ActivityStatusEnums.NOT_ENABLED.getCode().equals(tempActivityStatus)) {
                        if ((0 == activityType && date.compareTo(activityBeginTime) >= 0) || (null == activityEndTime && date.compareTo(activityBeginTime) >= 0) || (null!=activityEndTime && DateUtil.isIn(date, activityBeginTime, activityEndTime))){
                            temp.setActivityStatus(ActivityStatusEnums.IN_PROGRESS.getCode());
                        } else if (date.compareTo(activityBeginTime) < 0) {
                            temp.setActivityStatus(ActivityStatusEnums.NOT_START.getCode());
                        } else if (null != activityEndTime && date.compareTo(activityEndTime) > 0) {
                            temp.setActivityStatus(ActivityStatusEnums.END.getCode());
                        }
                    }
                }
        ).collect(Collectors.toList());

        PageVO<SignActivityListVO> pageVO = new PageVO<>();
        pageVO.setPages((int)activityIPage.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        Date date = new Date();
        List<SignActivity> signActivities = signActivityService.list(new LambdaQueryWrapper<SignActivity>()
                .and(wrapper1->wrapper1.le(SignActivity::getActivityBeginTime, date)
                        .gt(SignActivity::getActivityEndTime, date).or(wrapper2->wrapper2.le(SignActivity::getActivityBeginTime, date).eq(SignActivity::getActivityType,0))).eq(SignActivity::getDeleted,0)
                .eq(SignActivity::getActivityStatus, 1));
        if (CollectionUtil.isNotEmpty(signActivities)){
            throw new LuckException("已有正在进行中的活动!");
        }
        signActivityService.update(new LambdaUpdateWrapper<SignActivity>()
                .set(SignActivity::getActivityStatus,1)
                .eq(SignActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        signActivityService.update(new LambdaUpdateWrapper<SignActivity>()
                .set(SignActivity::getActivityStatus,0)
                .eq(SignActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        signActivityService.update(new LambdaUpdateWrapper<SignActivity>()
                .set(SignActivity::getDeleted,1)
                .eq(SignActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<SignGatherVO> signGather(SignGatherDTO param) {
        Date activityBeginTime = param.getActivityBeginTime();
        Date activityEndTime = param.getActivityEndTime();
        Integer activityId = param.getActivityId();

        SignActivity signActivity = signActivityService.getById(activityId);
        String activityName = signActivity.getActivityName();
        Date date = new Date();
        if (null == activityBeginTime && null == activityEndTime){
            Integer activityType = signActivity.getActivityType();
            Integer roundType = signActivity.getRoundType();
            //计算周期开始与截止时间
            if (0 == activityType){
                if (0 == roundType){
                    activityBeginTime = DateUtil.beginOfWeek(date);
                    activityEndTime = DateUtil.endOfWeek(date);
                }
                if (1 == roundType){
                    activityBeginTime = DateUtil.beginOfMonth(date);
                    activityEndTime = DateUtil.endOfMonth(date);
                }
                if (2 == roundType){
                    activityBeginTime = DateUtil.beginOfYear(date);
                    activityEndTime = DateUtil.endOfYear(date);
                }
            }else {
                activityBeginTime = signActivity.getActivityBeginTime();
                activityEndTime = signActivity.getActivityEndTime();
            }
        }

        long signRound = DateUtil.between(activityBeginTime, activityEndTime, DateUnit.DAY);

        List<UserSignReward> userSignRewards = userSignRewardService.list(new LambdaQueryWrapper<UserSignReward>()
                .eq(UserSignReward::getActivityId, activityId)
                .between(UserSignReward::getSignTime, activityBeginTime, activityEndTime));

        //获取常规签到与连续签到奖励优惠券集合
        Map<Integer, List<UserSignReward>> couponMap = userSignRewards.stream().filter(temp -> null != temp.getCouponId()).collect(Collectors.groupingBy(UserSignReward::getSignType));
        //获取常规签到与连续签到奖励积分集合
        Map<Integer, List<UserSignReward>> pointMap = userSignRewards.stream().filter(temp -> null != temp.getPointNum()).collect(Collectors.groupingBy(UserSignReward::getSignType));
        //获取签到用户集合
        List<UserSignReward> userList = userSignRewards.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(UserSignReward::getUserId))), ArrayList::new)
        );
        //获取连签奖励数集合
        Map<Integer, List<UserSignReward>> seriesDayNumMap = userSignRewards.stream().filter(temp -> temp.getSignType() == 2).collect(Collectors.groupingBy(UserSignReward::getSeriesSignDay));


        List<UserSignReward> normalCouponList = couponMap.get(1);
        List<UserSignReward> seriesCouponList = couponMap.get(2);

        List<UserSignReward> normalPointList = pointMap.get(1);
        List<UserSignReward> seriesPointList = pointMap.get(2);

        Integer normalCouponNum = CollectionUtil.isNotEmpty(normalCouponList)?normalCouponList.size():0;
        Integer seriesCouponNum = CollectionUtil.isNotEmpty(seriesCouponList)?seriesCouponList.size():0;

        Integer normalPointNum = CollectionUtil.isNotEmpty(normalPointList)?normalPointList.size():0;
        Integer seriesPointNum = CollectionUtil.isNotEmpty(seriesPointList)?seriesPointList.size():0;

        Integer signTimes = 0;
        if (CollectionUtil.isNotEmpty(userSignRewards)){
            signTimes = userSignRewards.stream().filter(u -> u.getSignType() == 1).collect(Collectors.toList()).size();
        }

        Integer signUserNum = CollectionUtil.isNotEmpty(userList)?userList.size():0;

        List<SignGatherSeriesVO> signGatherSeriesVOS = new ArrayList<>();
        seriesDayNumMap.forEach((k,v)->{
            SignGatherSeriesVO seriesVO = SignGatherSeriesVO.builder()
                    .seriesDay(k)
                    .seriesUserNum(v.size()).build();
            signGatherSeriesVOS.add(seriesVO);
        });

        SignGatherVO result = SignGatherVO.builder()
                .activityName(activityName)
                .allCouponNum(normalCouponNum + seriesCouponNum)
                .allPointNum(normalPointNum + seriesPointNum)
                .signRound((int) signRound)
                .signUserNum(signUserNum)
                .signTimes(signTimes)
                .normalSignCouponNum(normalCouponNum)
                .normalSignPointNum(normalPointNum)
                .seriesSignCouponNum(seriesCouponNum)
                .seriesSignPointNum(seriesPointNum)
                .series(signGatherSeriesVOS).build();
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<IPage<SignDetailVO>> signDetail(SignDetailDTO param) {
        Date activityBeginTime = param.getActivityBeginTime();
        Date activityEndTime = param.getActivityEndTime();
        Integer activityId = param.getActivityId();
        String mobile = param.getMobile();
        Integer signType = param.getSignType();

        Date date = new Date();

        if (null == activityBeginTime && null == activityEndTime){
            SignActivity signActivity = signActivityService.getById(activityId);
            Integer activityType = signActivity.getActivityType();
            Integer roundType = signActivity.getRoundType();
            //计算周期开始与截止时间
            if (0 == activityType){
                if (0 == roundType){
                    activityBeginTime = DateUtil.beginOfWeek(date);
                    activityEndTime = DateUtil.endOfWeek(date);
                }
                if (1 == roundType){
                    activityBeginTime = DateUtil.beginOfMonth(date);
                    activityEndTime = DateUtil.endOfMonth(date);
                }
                if (2 == roundType){
                    activityBeginTime = DateUtil.beginOfYear(date);
                    activityEndTime = DateUtil.endOfYear(date);
                }
            }else {
                activityBeginTime = signActivity.getActivityBeginTime();
                activityEndTime = signActivity.getActivityEndTime();
            }
        }
        LambdaQueryWrapper<UserSignReward> wrapper = new LambdaQueryWrapper<UserSignReward>()
                .between(UserSignReward::getSignTime,activityBeginTime,activityEndTime)
                .eq(UserSignReward::getActivityId,activityId)
                .eq(UserSignReward::getSignType,signType);
        if (StringUtils.isNotEmpty(mobile)){
            wrapper.eq(UserSignReward::getMobile,mobile);
        }
        wrapper.orderByDesc(UserSignReward::getCreateTime);

        IPage<UserSignReward> page = new Page<>(param.getPageNum(), param.getPageSize());
        IPage<UserSignReward> rewardIPage = userSignRewardService.page(page, wrapper);
        List<UserSignReward> records = rewardIPage.getRecords();
        List<SignDetailVO> detailVOS = Convert.toList(SignDetailVO.class, records);

        IPage<SignDetailVO> signDetailVOIPage = new Page<>();
        BeanUtil.copyProperties(rewardIPage,signDetailVOIPage);
        signDetailVOIPage.setRecords(detailVOS);
        return ServerResponseEntity.success(signDetailVOIPage);
    }

    @Override
    public ServerResponseEntity<SignActivityAppVO> signInfo(Long userId) {
        Date date = new Date();
        Date activityBeginTime = null;
        Date activityEndTime = null;

        SignActivity signActivity = signActivityService.getOne(new LambdaQueryWrapper<SignActivity>()
                .and(wrapper1->wrapper1.le(SignActivity::getActivityBeginTime, date)
                        .gt(SignActivity::getActivityEndTime, date)).eq(SignActivity::getDeleted,0)
                .eq(SignActivity::getActivityStatus, 1));

        if (null == signActivity){
            signActivity = signActivityService.getOne(new LambdaQueryWrapper<SignActivity>().and(wrapper2->wrapper2.le(SignActivity::getActivityBeginTime, date).eq(SignActivity::getActivityType,0)).eq(SignActivity::getDeleted,0)
                    .eq(SignActivity::getActivityStatus, 1));
            if (signActivity == null) {
                return ServerResponseEntity.success(null);
            }
        }

        Integer id = signActivity.getId();
        Integer activityType = signActivity.getActivityType();
        String activityName = signActivity.getActivityName();
        Integer roundType = signActivity.getRoundType();
        String activityRule = signActivity.getActivityRule();
        Integer seriesSignRewardTimes = signActivity.getSeriesSignRewardTimes();

        //计算周期开始与截止时间
        if (0 == activityType){
            if (0 == roundType){
                activityBeginTime = DateUtil.beginOfWeek(date);
                activityEndTime = DateUtil.endOfWeek(date);
            }
            if (1 == roundType){
                activityBeginTime = DateUtil.beginOfMonth(date);
                activityEndTime = DateUtil.endOfMonth(date);
            }
            if (2 == roundType){
                activityBeginTime = DateUtil.beginOfYear(date);
                activityEndTime = DateUtil.endOfYear(date);
            }
        }else {
            activityBeginTime = signActivity.getActivityBeginTime();
            activityEndTime = signActivity.getActivityEndTime();
        }

        //获取活动连签奖励列表
        List<SignActivityReward> activityRewards = signActivityRewardService.list(new LambdaQueryWrapper<SignActivityReward>()
                .eq(SignActivityReward::getSignActivityId, id));

        //获取用户当前周期签到信息
        List<UserSignReward> userSignRewards = userSignRewardService.list(new LambdaQueryWrapper<UserSignReward>()
                .eq(UserSignReward::getUserId, userId)
                .eq(UserSignReward::getActivityId, id)
                .between(UserSignReward::getSignTime, activityBeginTime, activityEndTime)
                .orderByAsc(UserSignReward::getCreateTime));

        SignActivityAppVO result = SignActivityAppVO.builder()
                .id(id)
                .seriesSignSwitch(signActivity.getSeriesSignSwitch())
                .maxGetTimes(seriesSignRewardTimes)
                .activityBeginTime(activityBeginTime)
                .activityEndTime(activityEndTime)
                .activityName(activityName)
                .activityRule(activityRule).build();

        //计算用户当前周期签到日期
        if (CollectionUtil.isNotEmpty(userSignRewards)){
            List<UserSignReward> tempRewards = userSignRewards.stream().filter(temp -> temp.getSignType() == 1).collect(Collectors.toList());
            List<Date> dates = tempRewards.stream().map(UserSignReward::getSignTime).collect(Collectors.toList());

            UserSignReward userSignReward = tempRewards.get(tempRewards.size() - 1);
            result.setSignDates(dates);
            result.setSignDay(userSignReward.getSeriesSignDay());
        }

        //获取用户签到提醒开启状态
        UserSignNotice userSignNotice = userSignNoticeService.getOne(new LambdaQueryWrapper<UserSignNotice>().eq(UserSignNotice::getActivityId, id).eq(UserSignNotice::getUserId, userId));
        if (null == userSignNotice){
            result.setSignNoticeSwitch(0);
        }else {
            result.setSignNoticeSwitch(userSignNotice.getNoticeSwitch());
        }


        //判断连签奖励次数及是否可领取
        List<SignRewardVO> signRewardsResult = activityRewards.stream().map(temp -> {
            Integer seriesSignDay = temp.getSeriesSignDay();
            List<UserSignReward> signRewards = userSignRewards.stream().filter(reward -> reward.getSignType() == 2 && reward.getSeriesSignDay().equals(seriesSignDay)).collect(Collectors.toList());
            List<UserSignReward> normalSign = userSignRewards.stream().filter(reward -> reward.getSignType() == 1 && reward.getSeriesSignDay() % seriesSignDay == 0).collect(Collectors.toList());
            int seriesTimes = CollectionUtil.isNotEmpty(signRewards) ? signRewards.size() : 0;
            int normalTimes = CollectionUtil.isNotEmpty(normalSign) ? normalSign.size() : 0;

            log.info("连签次数:{},签到轮数{},最大签到次数{},是否可领取:{}",seriesTimes,normalTimes,seriesSignRewardTimes,(seriesTimes < normalTimes) && (seriesTimes < seriesSignRewardTimes));
            return SignRewardVO.builder()
                    .id(temp.getId())
                    .rewardName(temp.getRewardName())
                    .seriesSignDay(seriesSignDay)
                    .rewardPicUrl(temp.getRewardPicUrl())
                    .curGetTimes(seriesTimes)
                    .flag((seriesTimes < normalTimes) && (seriesTimes < seriesSignRewardTimes)).build();
        }).collect(Collectors.toList());

        result.setRewards(signRewardsResult);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> sign(UserSignDTO param) {

        Integer activityId = param.getActivityId();
        Long userId = param.getUserId();
        Long shopId = param.getShopId();

        Integer roundDay;
        Date date = new Date();

        ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserData(userId);
        UserApiVO userApiVO = userData.getData();
        String userMobile = userApiVO.getUserMobile();
        param.setMobile(userMobile);

//        StoreVO storeVO = storeFeignClient.findByStoreId(userApiVO.getStoreId());
//        String shopName = storeVO.getName();
//        String storeCode = storeVO.getStoreCode();
//        param.setShopName(shopName);
//        param.setShopCode(storeCode);







//        Map<Long, StaffVO> staffVOMap = new HashMap<>();
//        List<Long> staffIdList = userList.stream().filter(u -> u.getStaffId() != null && u.getStaffId() > 0).map(UserManagerVO:: getStaffId)
//                .collect(Collectors.toList());
//        if (CollectionUtils.isNotEmpty(staffIdList)) {
//            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
//            staffQueryDTO.setStaffIdList(staffIdList);
//            ServerResponseEntity<List<StaffVO>> staffData = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
//            if (!staffData.isSuccess()){
//                return new ArrayList<>();
//            }
//            staffVOMap = staffData.getData().stream().filter(s -> s.getStatus() == 0).collect(Collectors.toMap(StaffVO :: getId, Function.identity()));
//        }

        UserSignReward userSignReward = BeanUtil.copyProperties(param, UserSignReward.class);
        userSignReward.setSignType(1);
        userSignReward.setSignTime(date);


        ServerResponseEntity<StaffVO> staffvoResponse = staffFeignClient.getStaffById(userApiVO.getStaffId());
        if(staffvoResponse!=null && staffvoResponse.getData()!=null){
            StaffVO staffVO = staffvoResponse.getData();
            userSignReward.setShopName(staffVO.getStoreName());
            userSignReward.setShopCode(staffVO.getStoreCode());
        }



        //获取活动信息
        SignActivity signActivity = signActivityService.getById(activityId);
        Integer daySignRewardType = signActivity.getDaySignRewardType();
        String daySignCouponId = signActivity.getDaySignCouponId();
        if (0 == daySignRewardType){
            Integer daySignPoint = signActivity.getDaySignPoint();
            userSignReward.setPointNum(daySignPoint);
            // 赠送积分
            UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
            updateScoreDTO.setUserId(userId);
            updateScoreDTO.setPoint_value(daySignPoint);
            updateScoreDTO.setSource("签到赠送积分");
            updateScoreDTO.setPoint_channel("wechat");
            updateScoreDTO.setPoint_type("SKX_JLJF");
            updateScoreDTO.setRemark("签到赠送积分");
            updateScoreDTO.setIoType(1);

            //todo 更新新的添加积分逻辑
//            crmUserFeignClient.updateScore(updateScoreDTO);
            log.info("SignActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
        }else if (1 == daySignRewardType){
            userSignReward.setCouponId(Long.valueOf(daySignCouponId));
            // 优惠券名称
            ServerResponseEntity<CouponDetailVO> couponDetail = tCouponFeignClient.getCouponDetail(Long.valueOf(daySignCouponId));
            CouponDetailVO data = couponDetail.getData();
            userSignReward.setCouponName(data.getName());
            //赠送券
            ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
            receiveCouponDTO.setCouponId(Long.valueOf(daySignCouponId));
            receiveCouponDTO.setActivityId(Long.valueOf(activityId));
            receiveCouponDTO.setActivitySource(ActivitySourceEnum.SIGN_ACTIVITY.value());
            receiveCouponDTO.setUserId(userId);
            tCouponFeignClient.receive(receiveCouponDTO);
        }

        //判断循环周期
        Integer activityType = signActivity.getActivityType();
        Integer roundType = signActivity.getRoundType();
        if (0 == activityType){
            if (0 == roundType){
                roundDay = 7;
            }else if (1 == roundType){
                roundDay = DateUtil.lengthOfMonth(DateUtil.monthEnum(date).getValue(), DateUtil.isLeapYear(DateUtil.year(date)));
            }else {
                roundDay = DateUtil.lengthOfYear(DateUtil.year(date));
            }
        }else {
            roundDay = signActivity.getRoundDayTime();
        }

        //获取用户普通签到列表
        List<UserSignReward> userSignRewards = userSignRewardService.list(new LambdaQueryWrapper<UserSignReward>()
                .eq(UserSignReward::getActivityId,activityId)
                .eq(UserSignReward::getUserId, userId)
                .eq(UserSignReward::getSignType,1)
                .orderByDesc(UserSignReward::getCreateTime));
        //根据签到信息计算当前连续签到天数
        if (CollectionUtil.isNotEmpty(userSignRewards)){
            UserSignReward reward = userSignRewards.get(0);
            Integer seriesSignDay = reward.getSeriesSignDay();
            if (seriesSignDay.equals(roundDay)){
                userSignReward.setSeriesSignDay(1);
            }else {
                DateTime yesterday = DateUtil.yesterday();
                boolean sameDay = DateUtil.isSameDay(yesterday, reward.getSignTime());
                if (sameDay){
//                    DateTime week = DateUtil.endOfWeek(yesterday);
//                    DateTime month = DateUtil.endOfMonth(yesterday);
//                    DateTime year = DateUtil.endOfYear(yesterday);
//                    if (0 == roundType && DateUtil.isSameDay(yesterday,week)){
//                        userSignReward.setSeriesSignDay(1);
//                    }else if (1 == roundType && DateUtil.isSameDay(yesterday,month)){
//                        userSignReward.setSeriesSignDay(1);
//                    }else if (2 == roundType && DateUtil.isSameDay(yesterday,year)){
//                        userSignReward.setSeriesSignDay(1);
//                    }else {
                    userSignReward.setSeriesSignDay(seriesSignDay+1);
//                    }
                }else {
                    userSignReward.setSeriesSignDay(1);
                }
            }
        }else {
            userSignReward.setSeriesSignDay(1);
        }
        userSignRewardService.save(userSignReward);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> series(UserSignDTO param) {
        Integer activityId = param.getActivityId();
        Long userId = param.getUserId();
        Integer rewardId = param.getRewardId();
        Long shopId = param.getShopId();
        Date date = new Date();


        ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserData(userId);
        UserApiVO userApiVO = userData.getData();
        String userMobile = userApiVO.getUserMobile();
        param.setMobile(userMobile);

        StoreVO storeVO = storeFeignClient.findByStoreId(userApiVO.getStoreId());
        String shopName = storeVO.getName();
        String storeCode = storeVO.getStoreCode();
        param.setShopName(shopName);
        param.setShopCode(storeCode);

        UserSignReward userSignReward = BeanUtil.copyProperties(param, UserSignReward.class);
        userSignReward.setSignTime(date);
        userSignReward.setSignType(2);
        userSignReward.setShopName(shopName);
        userSignReward.setShopCode(storeCode);

        Date activityBeginTime = null;
        Date activityEndTime = null;
        SignActivity signActivity = signActivityService.getById(activityId);
        Integer activityType = signActivity.getActivityType();
        Integer roundType = signActivity.getRoundType();
        Integer seriesSignRewardTimes = signActivity.getSeriesSignRewardTimes();

//        //计算周期开始与截止时间
//        if (0 == activityType){
//            if (0 == roundType){
//                activityBeginTime = DateUtil.beginOfWeek(date);
//                activityEndTime = DateUtil.endOfWeek(date);
//            }
//            if (1 == roundType){
//                activityBeginTime = DateUtil.beginOfMonth(date);
//                activityEndTime = DateUtil.endOfMonth(date);
//            }
//            if (2 == roundType){
//                activityBeginTime = DateUtil.beginOfYear(date);
//                activityEndTime = DateUtil.endOfYear(date);
//            }
//        }else {
            activityBeginTime = signActivity.getActivityBeginTime();
            activityEndTime = signActivity.getActivityEndTime();
//        }

        //获取连签奖励详情
        SignActivityReward signActivityReward = signActivityRewardService.getById(rewardId);
        Integer seriesSignDay = signActivityReward.getSeriesSignDay();
        Integer couponSwitch = signActivityReward.getCouponSwitch();
        Integer pointSwitch = signActivityReward.getPointSwitch();

        //若用户该周期未签到则返回
        List<UserSignReward> userSignRewards = userSignRewardService.list(new LambdaQueryWrapper<UserSignReward>()
                .eq(UserSignReward::getActivityId, activityId)
                .eq(UserSignReward::getUserId, userId)
                .eq(UserSignReward::getSignType,1)
                .between(UserSignReward::getSignTime, activityBeginTime, activityEndTime));
        if (CollectionUtil.isEmpty(userSignRewards)){
            throw new LuckException("该用户未达到领取该奖励条件");
        }

        //若用户已达可领取奖励上限则返回
        List<UserSignReward> seriesRewards = userSignRewardService.list(new LambdaQueryWrapper<UserSignReward>()
                .eq(UserSignReward::getActivityId, activityId)
                .eq(UserSignReward::getUserId, userId)
                .eq(UserSignReward::getSignType,2)
                .between(UserSignReward::getSignTime, activityBeginTime, activityEndTime));
        List<UserSignReward> collect = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(seriesRewards)){
            collect = seriesRewards.stream().filter(temp -> temp.getSeriesSignDay().equals(seriesSignDay)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect) && (collect.size() >= seriesSignRewardTimes)){
                throw new LuckException("您达到最大领取次数");
            }
        }

        //若用户未达签到天数则
        List<UserSignReward> rewards = userSignRewards.stream().filter(temp -> temp.getSeriesSignDay() % seriesSignDay == 0).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(rewards) && rewards.size()<=collect.size()){
            throw new LuckException("未达到领取所需连续签到天数");
        }

        //获取赠送优惠券和积分数
        if (1==couponSwitch){
            String couponId = signActivityReward.getCouponId();
            // 获取优惠券名称
            userSignReward.setCouponId(Long.valueOf(couponId));
            ServerResponseEntity<CouponDetailVO> couponDetail = tCouponFeignClient.getCouponDetail(Long.valueOf(couponId));
            CouponDetailVO data = couponDetail.getData();
            userSignReward.setCouponName(data.getName());

            //赠送优惠券
            ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
            receiveCouponDTO.setCouponId(Long.valueOf(couponId));
            receiveCouponDTO.setActivityId(Long.valueOf(activityId));
            receiveCouponDTO.setActivitySource(ActivitySourceEnum.SIGN_ACTIVITY.value());
            receiveCouponDTO.setUserId(userId);
            tCouponFeignClient.receive(receiveCouponDTO);
        }
        if (1==pointSwitch){
            Integer pointNum = signActivityReward.getPointNum();
            userSignReward.setPointNum(pointNum);

            // 赠送积分
            UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
            updateScoreDTO.setUserId(userId);
            updateScoreDTO.setPoint_value(pointNum);
            updateScoreDTO.setSource("连续签到赠送积分");
            updateScoreDTO.setPoint_channel("wechat");
            updateScoreDTO.setPoint_type("SKX_JLJF");
            updateScoreDTO.setRemark("连续签到赠送积分");
            updateScoreDTO.setIoType(1);
            //更新新的赠送积分逻辑
//            crmUserFeignClient.updateScore(updateScoreDTO);
            log.info("SignActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
        }

        userSignReward.setSeriesSignDay(seriesSignDay);



        userSignRewardService.save(userSignReward);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disableUserNotice(UserNoticeDTO param) {
        Long userId = param.getUserId();
        Integer activityId = param.getActivityId();
        userSignNoticeService.update(new LambdaUpdateWrapper<UserSignNotice>()
                .set(UserSignNotice::getNoticeSwitch,0)
                .eq(UserSignNotice::getUserId,userId)
                .eq(UserSignNotice::getActivityId,activityId));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> enableUserNotice(UserNoticeDTO param) {
        Integer id = param.getActivityId();
        Long userId = param.getUserId();
        String formId = param.getFormId();
        String openId = param.getOpenId();

        UserSignNotice userSignNotice = userSignNoticeService.getOne(new LambdaQueryWrapper<UserSignNotice>().eq(UserSignNotice::getActivityId, id).eq(UserSignNotice::getUserId, userId));

        if (null == userSignNotice){
            UserSignNotice signNotice = UserSignNotice.builder()
                    .activityId(id)
                    .userId(userId)
                    .openId(openId)
                    .noticeSwitch(1)
                    .formId(formId).build();
            userSignNoticeService.save(signNotice);
        }else {
            userSignNoticeService.update(new LambdaUpdateWrapper<UserSignNotice>()
                    .set(UserSignNotice::getNoticeSwitch,1)
                    .eq(UserSignNotice::getUserId,userId)
                    .eq(UserSignNotice::getActivityId,id));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public void signNormalDetailExport(HttpServletResponse response, SignDetailDTO param,Long downLoadHisId) {
        Date activityBeginTime = param.getActivityBeginTime();
        Date activityEndTime = param.getActivityEndTime();
        Integer activityId = param.getActivityId();
        String mobile = param.getMobile();
        Integer signType = param.getSignType();

        Date date = new Date();

        if (null == activityBeginTime && null == activityEndTime){
            SignActivity signActivity = signActivityService.getById(activityId);
            Integer activityType = signActivity.getActivityType();
            Integer roundType = signActivity.getRoundType();
            //计算周期开始与截止时间
            if (0 == activityType){
                if (0 == roundType){
                    activityBeginTime = DateUtil.beginOfWeek(date);
                    activityEndTime = DateUtil.endOfWeek(date);
                }
                if (1 == roundType){
                    activityBeginTime = DateUtil.beginOfMonth(date);
                    activityEndTime = DateUtil.endOfMonth(date);
                }
                if (2 == roundType){
                    activityBeginTime = DateUtil.beginOfYear(date);
                    activityEndTime = DateUtil.endOfYear(date);
                }
            }else {
                activityBeginTime = signActivity.getActivityBeginTime();
                activityEndTime = signActivity.getActivityEndTime();
            }
        }
        LambdaQueryWrapper<UserSignReward> wrapper = new LambdaQueryWrapper<UserSignReward>()
                .between(UserSignReward::getSignTime,activityBeginTime,activityEndTime)
                .eq(UserSignReward::getActivityId,activityId)
                .eq(UserSignReward::getSignType,signType);
        if (StringUtils.isNotEmpty(mobile)){
            wrapper.eq(UserSignReward::getMobile,mobile);
        }
        wrapper.orderByDesc(UserSignReward::getCreateTime);

        List<UserSignReward> records = userSignRewardService.list(wrapper);

        List<SignDetailExcelVO> excelVOS = records.stream().map(temp -> {
            SignDetailExcelVO signDetailExcelVO = new SignDetailExcelVO();
            signDetailExcelVO.setSignTime(temp.getSignTime());
            signDetailExcelVO.setCouponName(temp.getCouponName());
            signDetailExcelVO.setMobile(temp.getMobile());
            signDetailExcelVO.setPointNum(temp.getPointNum());
            signDetailExcelVO.setShopName(temp.getShopName());
            signDetailExcelVO.setShopCode(temp.getShopCode());
            String signTypeTemp = StringPool.EMPTY;
            if (1 == temp.getSignType()) {
                signTypeTemp = "常规签到";
            } else if (2 == temp.getSignType()) {
                signTypeTemp = "连续签到";
            }
            signDetailExcelVO.setSignType(signTypeTemp);
            return signDetailExcelVO;
        }).collect(Collectors.toList());

        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);

        if(CollUtil.isEmpty(excelVOS)) {
            finishDownLoadDTO.setRemarks("没有可导出的数据");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("没有可导出的数据");
            return;
        }
        File file=null;
        try {
            int calCount=excelVOS.size();
            log.info("导出数据行数 【{}】",calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行签到活动生成excel 总条数【{}】",calCount);
            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, SignDetailExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(excelVOS);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行签到活动生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,excelVOS.size(),pathExport);

            startTime = System.currentTimeMillis();
            log.info("导出数据到本地excel，开始执行上传excel.....");
            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("上传文件地址："+responseEntity.getData());
                //下载中心记录
                finishDownLoadDTO.setCalCount(calCount);
                String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+SignDetailExcelVO.EXCEL_NAME;
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                finishDownLoadDTO.setRemarks("导出成功");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            log.info("导出数据到本地excel，结束执行上传excel，耗时：{}ms", System.currentTimeMillis() - startTime);
        }catch (Exception e){
            log.error("导出签到活动信息错误: "+e.getMessage(),e);
            finishDownLoadDTO.setRemarks("导出签到活动日志失败，信息错误："+e.getMessage());
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
    }
}
