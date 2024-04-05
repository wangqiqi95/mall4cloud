package com.mall4j.cloud.flow.controller.admin;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.order.constant.RetainedDateType;
import com.mall4j.cloud.api.order.constant.RetainedType;
import com.mall4j.cloud.api.order.dto.CustomerRetainedDTO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.CustomerRetainVO;
import com.mall4j.cloud.api.user.bo.UserOrderStatisticBO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.MemberContributeRespVO;
import com.mall4j.cloud.api.user.vo.MemberDealRespVO;
import com.mall4j.cloud.api.user.vo.MemberOverviewVO;
import com.mall4j.cloud.api.user.vo.MemberTrendRespVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.vo.DateVO;
import com.mall4j.cloud.flow.service.CustomerAnalysisService;
import com.mall4j.cloud.flow.vo.MemberSurveyRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 顾客分析接口
 * @author
 */
@Slf4j
@Api(tags = "顾客分析接口")
@RestController("adminCustomerAnalysisController")
@RequestMapping("/mp/customer_analysis")
public class CustomerAnalysisController {

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private CustomerAnalysisService customerAnalysisService;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private CouponFeignClient couponFeignClient;


    /**
     * 会员分析，会员概况
     */
    @ApiOperation(value = "会员分析，会员概况", notes = "会员分析，会员概况")
    @GetMapping("/get_member_survey")
    public ServerResponseEntity<MemberSurveyRespVO> getMemberSurvey(MemberReqDTO param) {
        param.setShopId(AuthUserContext.get().getTenantId());
        log.info("get_member_survey -> 入参信息: {}",JSON.toJSON(param));
        MemberSurveyRespVO memberSurveyRespVO = new MemberSurveyRespVO();
        memberSurveyRespVO.setMemberOverviewModelList(getMemberAnalysisSurvey(param));
        // 统计比例数据,获取到时间对比，新的会员数据截止时间为结束时间，旧会员数据截止时间为开始时间
        Date startTime = null;
        //时间类型 1今日实时 2 近7天 3 近30天 4自然日 5自然月
        switch (param.getDateType()){
            case 1:startTime = DateUtil.offsetDay(param.getStartTime(),-7); break;
            case 3:startTime = DateUtil.offsetDay(param.getStartTime(),-30); break;
            case 4:startTime = DateUtil.offsetDay(param.getStartTime(),-30); break;
            default:startTime = DateUtil.offsetDay(param.getStartTime(),-1);
        }
        param.setDateTime(null);
        // 获取新会员订单相关数据
        MemberOverviewVO newMemberOverviewVO = getMemberSurveyRespData(param);
        // 获取旧会员订单相关数据
        param.setEndTime(param.getStartTime());
        param.setStartTime(startTime);
        MemberOverviewVO oldMemberOverviewVO = getMemberSurveyRespData(param);

        getGeneralizeRate(newMemberOverviewVO,oldMemberOverviewVO);
        memberSurveyRespVO.setMemberOverviewModel(newMemberOverviewVO);
        return ServerResponseEntity.success(memberSurveyRespVO);
    }

    private MemberOverviewVO getMemberSurveyRespData(MemberReqDTO param) {
        try {
            List<Long> userIds = new ArrayList<>();
            // 获取会员订单相关数据
            List<UserOrderStatisticBO> userOrderList = orderFeignClient.getPaidMemberByParam(param).getData();
            // 获取到条件内每个用户的支付金额，支付订单数（同时运算下获取其他的数据）
            userOrderList.forEach(userOrderStatisticBO -> userIds.add(userOrderStatisticBO.getUserId()));
            param.setUserIds(userIds);
            MemberOverviewVO memberOverviewVO = userFeignClient.getMemberAnalysisByParam(param).getData();
            if(CollectionUtil.isEmpty(memberOverviewVO.getUserIds())){
                // 会员支付金额
                memberOverviewVO.setMemberPayAmount(0.00);
                // 会员支付订单数
                memberOverviewVO.setMemberPayOrder(0);
                // 会员客单价
                memberOverviewVO.setMemberSingleAmount(0.0);
                // 支付会员数
                memberOverviewVO.setPayMember(0);

            }else {
                // 如果不为0则筛选一下当前满足条件的用户
                if (!Objects.equals(param.getMemberType(), 0)) {
                    List<Long> userDbIds = memberOverviewVO.getUserIds();
                    // 筛选交集的用户
                    userOrderList = userOrderList.stream().filter(userOrder->userDbIds.contains(
                            userOrder.getUserId())).collect(Collectors.toList());
                }
                long sumAmount = userOrderList.stream().mapToLong(UserOrderStatisticBO::getActualAmount).sum();
                Integer sumPayOrderCount = userOrderList.stream().mapToInt(UserOrderStatisticBO::getConsTimes).sum();
                // 会员支付金额，分转换成元
                memberOverviewVO.setMemberPayAmount(handDouble(PriceUtil.toDecimalPrice(sumAmount).doubleValue()));
                // 支付会员数
                memberOverviewVO.setPayMember(userOrderList.size());
                // 会员支付订单数
                memberOverviewVO.setMemberPayOrder(sumPayOrderCount);
                // 会员客单价
                memberOverviewVO.setMemberSingleAmount(divAverage(memberOverviewVO.getMemberPayAmount(), userOrderList.size(), 2));
            }
            // 获取领取的所有优惠券的用户和优惠券数量集合
            List<Long> receiverUserIds = couponFeignClient.countMemberCouponByParam(param.getShopId(), param.getStartTime(), param.getEndTime()).getData();
            param.setUserIds(receiverUserIds);
            List<Long> userCouponIds = userFeignClient.getMeetConditionsUserIds(param).getData();
            if(CollectionUtil.isEmpty(userCouponIds)){
                // 领券会员数
                memberOverviewVO.setCouponMember(0);
            }else{
                // 如果不为0则筛选一下当前满足条件的用户
                if (!Objects.equals(param.getMemberType(), 0)) {
                    // 筛选交集的用户
                    receiverUserIds = userCouponIds;
                }
                // 领券会员数
                memberOverviewVO.setCouponMember(receiverUserIds.size());
            }
            return memberOverviewVO;
        }catch (Exception e){
            return new MemberOverviewVO();
        }

    }

    private void getGeneralizeRate(MemberOverviewVO memberOverviewVO, MemberOverviewVO oldMemberOverviewVO){
        memberOverviewVO.setMemberSingleAmount(Arith.div(memberOverviewVO.getMemberPayAmount(),memberOverviewVO.getPayMember(),2));
        oldMemberOverviewVO.setMemberSingleAmount(Arith.div(oldMemberOverviewVO.getMemberPayAmount(),oldMemberOverviewVO.getPayMember(),2));
        memberOverviewVO.setTotalMemberRate(setRate(memberOverviewVO.getTotalMember().doubleValue(),oldMemberOverviewVO.getTotalMember().doubleValue()));
        memberOverviewVO.setNewMemberRate(setRate(memberOverviewVO.getNewMember().doubleValue(),oldMemberOverviewVO.getNewMember().doubleValue()));
        memberOverviewVO.setCouponMemberRate(setRate(memberOverviewVO.getCouponMember().doubleValue(),oldMemberOverviewVO.getCouponMember().doubleValue()));
        memberOverviewVO.setPayMemberRate(setRate(memberOverviewVO.getPayMember().doubleValue(),oldMemberOverviewVO.getPayMember().doubleValue()));
        memberOverviewVO.setMemberPayAmountRate(setRate(memberOverviewVO.getMemberPayAmount(),oldMemberOverviewVO.getMemberPayAmount()));
        memberOverviewVO.setMemberPayOrderRate(setRate(memberOverviewVO.getMemberPayOrder().doubleValue(),oldMemberOverviewVO.getMemberPayOrder().doubleValue()));
        memberOverviewVO.setMemberSingleAmountRate(setRate(memberOverviewVO.getMemberSingleAmount(),oldMemberOverviewVO.getMemberSingleAmount()));

    }

    private Double setRate(Double newNum,Double oldNum){
        if (newNum.equals(0.0) && oldNum.equals(0.0)){
            return 0.0;
        }else if (newNum > 0.0 && oldNum.equals(0.0)){
            return 0.0;
        }else if (newNum.equals(0.0) && oldNum > 0.0){
            return -100.0;
        }
        double sub = Arith.sub(newNum, oldNum);
        return Arith.mul(Arith.div(sub,oldNum,2),100);
    }

    private List<MemberOverviewVO> getMemberAnalysisSurvey(MemberReqDTO param) {
        Date startTime = param.getStartTime();
        Date endTime = param.getEndTime();
        List<DateVO> everyDays = DateUtils.findEveryDays(startTime,endTime);
        List<MemberOverviewVO> resList = new ArrayList<>();
        if (Objects.isNull(param.getMemberType())){
            param.setMemberType(0);
        }
        if (everyDays.size() == 1 ) {
            Date beforeDate = DateUtils.getBeforeDate(everyDays.get(0).getStartTime());
            everyDays = DateUtils.findEveryDays(beforeDate, param.getEndTime());
        }
        MemberOverviewVO memberOverviewVO;
        List<Long> userIds = new ArrayList<>();
        for (DateVO everyDay : everyDays) {
            // 支付会员数，再筛选时间内，购买商品的会员人数
            // 先查询用户（可能有几十万用户ids）再根据用户来查询订单不行，需要先查订单在进行筛选
            param.setDateTime(null);
            param.setStartTime(startTime);
            param.setEndTime(endTime);
            // 获取订单相关用户信息
            memberOverviewVO = getUserOrderBO(param, userIds, everyDay.getStartTime(),everyDay.getEndTime());
            if(Objects.isNull(memberOverviewVO)){
                continue;
            }
            // 获取领取的所有优惠券的用户和优惠券数量集合
            List<Long> receiverUserIds = couponFeignClient.countMemberCouponByParam(param.getShopId(), param.getStartTime(), param.getEndTime()).getData();
            param.setUserIds(receiverUserIds);
            List<Long> userCouponIds = userFeignClient.getMeetConditionsUserIds(param).getData();
            if(CollectionUtil.isEmpty(userCouponIds)){
                // 领券会员数
                memberOverviewVO.setCouponMember(0);
            }else{
                // 如果不为0则筛选一下当前满足条件的用户
                if (!Objects.equals(param.getMemberType(), 0)) {
                    // 筛选交集的用户
                    receiverUserIds = userCouponIds;
                }
                // 领券会员数
                memberOverviewVO.setCouponMember(receiverUserIds.size());
            }
            resList.add(memberOverviewVO);
        }
        return resList;
    }


    private MemberOverviewVO getUserOrderBO(MemberReqDTO param, List<Long> userIds, Date startTime,Date endTime) {
        log.info("getUserOrderBO 入参-> param:{} startTime:{} endTime:{}",JSON.toJSON(param),startTime,endTime);
        try {
            List<UserOrderStatisticBO> userOrderList;
            // 获取到条件内每个用户的支付金额，支付订单数（同时运算下获取其他的数据）
            userOrderList = orderFeignClient.getPaidMemberByParam(param).getData();
            userOrderList.forEach(userOrderStatisticBO -> userIds.add(userOrderStatisticBO.getUserId()));
            param.setUserIds(userIds);
            MemberOverviewVO res = userFeignClient.getUserAnalysis(param, startTime, endTime).getData();
            if(CollectionUtil.isEmpty(res.getUserIds())){
                // 会员支付金额
                res.setMemberPayAmount(0.00);
                // 会员支付订单数
                res.setMemberPayOrder(0);
                // 会员客单价
                res.setMemberSingleAmount(0.0);
                // 支付会员数
                res.setPayMember(0);

            }else {
                // 如果不为0则筛选一下当前满足条件的用户
                if (!Objects.equals(param.getMemberType(), 0)) {
                    List<Long> userDbIds = res.getUserIds();
                    // 筛选交集的用户
                    userOrderList = userOrderList.stream().filter(userOrder->userDbIds.contains(
                            userOrder.getUserId())).collect(Collectors.toList());
                }
                long sumAmount = userOrderList.stream().mapToLong(UserOrderStatisticBO::getActualAmount).sum();
                Integer sumPayOrderCount = userOrderList.stream().mapToInt(UserOrderStatisticBO::getConsTimes).sum();
                // 会员支付金额
                res.setMemberPayAmount(handDouble(PriceUtil.toDecimalPrice(sumAmount).doubleValue()));
                // 支付会员数
                res.setPayMember(userOrderList.size());
                // 会员支付订单数
                res.setMemberPayOrder(sumPayOrderCount);
                // 会员客单价
                res.setMemberSingleAmount(divAverage(res.getMemberPayAmount(), userOrderList.size(), 2));
            }
            return res;
        }catch (Exception e){
            log.info("getUserOrderBO 异常信息: {} {}",e,e.getMessage());
            return null;
        }

    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，会员人数趋势/ 会员占比趋势
     */
    @ApiOperation(value = "会员分析，会员人数趋势/ 会员占比趋势", notes = "会员分析，会员人数趋势/ 会员占比趋势")
    @GetMapping("/get_member_trend")
    public ServerResponseEntity<List<MemberTrendRespVO>> getMemberTrend(MemberReqDTO param) {

        List<MemberTrendRespVO> resList = new ArrayList<>();
        Date startTime = param.getStartTime();
        Date endTime = param.getEndTime();
        List<DateVO> everyDays = DateUtils.findEveryDays(startTime, endTime);
        int total = 0;
        for (DateVO everyDay : everyDays) {
            MemberTrendRespVO res = new MemberTrendRespVO();
            param.setDateTime(null);
            param.setStartTime(everyDay.getStartTime());
            param.setEndTime(everyDay.getEndTime());
            res.setCurrentDay(DateUtils.dateToNumber(everyDay.getStartTime()));
            // 筛选时间类的每一天注册的会员数数据，不是每一天平台的总会员数
            Integer memberNum = userFeignClient.getMemberTrend(param).getData();
            res.setMemberNum(Objects.isNull(memberNum) ? 0 : memberNum);
            total = total + res.getMemberNum();
            resList.add(res);
        }
        for (MemberTrendRespVO respParam : resList) {
            respParam.setMemberNumRate(divAverage(respParam.getMemberNum(),total,4));
        }
        return ServerResponseEntity.success(resList);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，会员贡献价值分析
     */
    @ApiOperation(value = "会员分析，会员贡献价值分析", notes = "会员分析，会员贡献价值分析")
    @GetMapping("/get_member_vontribute_value")
    public ServerResponseEntity<MemberContributeRespVO> getMemberContributeValue(MemberReqDTO param) {
        log.info("get_member_vontribute_value 入参: {}", JSON.toJSON(param));
        MemberContributeRespVO contributeRespVO = customerAnalysisService.getMemberContributeValue(param);
        return ServerResponseEntity.success(contributeRespVO);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，新老会员成交分析
     */
    @GetMapping("/get_member_deal")
    @ApiOperation(value = "会员分析，新老会员成交分析", notes = "会员分析，新老会员成交分析")
    public ServerResponseEntity<MemberDealRespVO> getMemberDeal(MemberReqDTO param) {
        MemberDealRespVO respParam = customerAnalysisService.getMemberDeal(param);
        return ServerResponseEntity.success(respParam);
    }

    /**
     * 客户分析，客户留存分析
     */
    @ApiOperation(value = "客户分析-客户留存分析",notes = "客户分析，客户留存分析,不做周留存数据")
    @GetMapping("/get_customer_retained")
    public ServerResponseEntity<List<CustomerRetainVO>> getCustomerRetained(CustomerRetainedDTO customerRetainedDTO) {
        //
        Integer dateType = customerRetainedDTO.getDateType();
        Integer dateRetainType = customerRetainedDTO.getDateRetainType();
        List<CustomerRetainVO> respList = new ArrayList<>();
        if (Objects.equals(1,dateType) && !Objects.equals(1,dateRetainType)) {
            // 最近一月，月留存。此时不显示数据
            return ServerResponseEntity.success(respList);
        }
        Integer retainType = customerRetainedDTO.getRetainType();
        if (Objects.equals(1,retainType)) {
            // 访问留存
            respList = customerAnalysisService.getVisitRetained(customerRetainedDTO);
        } else {
            // 成交留存
            respList = customerAnalysisService.getTradeRetained(customerRetainedDTO);
        }
        return ServerResponseEntity.success(respList);
    }

    @ApiOperation(value = "客户留存分析-刷新数据缓存",notes = "刷新数据缓存，重新拉取数据")
    @GetMapping("/refresh_retained_data")
    public ServerResponseEntity<Void> refreshRetainedData() {
        DateTime now = DateUtil.date();
        Date endOfDay =  DateUtil.endOfDay(now);
        List<CustomerRetainedDTO> list = new ArrayList<>();
        RetainedType[] values = RetainedType.values();
        for (RetainedType type : values) {
            Integer typeValue = type.value();
            RetainedDateType[] retainedDateTypes = RetainedDateType.values();
            for (RetainedDateType retainedDateType : retainedDateTypes) {
                if (Objects.equals(1,retainedDateType.value())) {
                    continue;
                }
                CustomerRetainedDTO param = new CustomerRetainedDTO();
                param.setDateRetainType(1);
                param.setDateType(retainedDateType.value());
                param.setRetainType(typeValue);
                int setMonth = 0;
                if (Objects.equals(param.getDateType(),RetainedDateType.THREE_MONTH.value())) {
                    setMonth = Constant.THREE_MONTH;

                } else if (Objects.equals(param.getDateType(),RetainedDateType.SIX_MONTH.value())) {
                    setMonth = Constant.SIX_MONTH;
                } else if (Objects.equals(param.getDateType(),RetainedDateType.ONE_YEAR.value())) {
                    setMonth = Constant.TWELVE_MONTH;
                }
                Date start =  DateUtil.beginOfDay(DateUtil.offsetMonth(now,-setMonth));
                param.setStartTime(start);
                param.setEndTime(endOfDay);
                list.add(param);
            }
        }
        // 清除缓存
        for (CustomerRetainedDTO param : list) {
            customerAnalysisService.removeCacheVisitRetained(param);
            customerAnalysisService.removeCacheTradeRetained(param);
        }
        // 重新拉取数据
        for (CustomerRetainedDTO param : list) {
            customerAnalysisService.getVisitRetained(param);
            customerAnalysisService.getTradeRetained(param);
        }
        return ServerResponseEntity.success();
    }



    private Double handDouble(Double value){
        if (Objects.isNull(value)){
            return 0.00;
        }
        return value;
    }

    private Double divAverage(Double a, Integer b, Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a, b, scale);
    }

    private Double divAverage(Integer a, Integer b,Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a,b,scale);
    }


}
