package com.mall4j.cloud.flow.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.order.constant.RetainedDateType;
import com.mall4j.cloud.api.order.dto.CustomerRetainedDTO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.CustomerRetainVO;
import com.mall4j.cloud.api.user.bo.UserOrderStatisticBO;
import com.mall4j.cloud.api.user.bo.UserOrderStatisticListBO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.*;
import com.mall4j.cloud.common.cache.constant.FlowCacheNames;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.vo.DateVO;
import com.mall4j.cloud.flow.mapper.CustomerAnalysisMapper;
import com.mall4j.cloud.flow.service.CustomerAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lgh on 2018/10/26.
 */
@Slf4j
@Service
public class CustomerAnalysisServiceImpl implements CustomerAnalysisService {

    @Autowired
    private CustomerAnalysisMapper customerAnalysisMapper;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private CouponFeignClient couponFeignClient;

    @Override
    public MemberDealRespVO getMemberDeal(MemberReqDTO param) {
        MemberDealRespVO respParam = new MemberDealRespVO();
        List<MemberDealTreadVO> trend = new ArrayList<>();
        Date startTime = param.getStartTime();
        Date endTime = param.getEndTime();
        Date beforeYear = DateUtils.getBeforeYear(startTime, -2);
        param.setBeforeYear(beforeYear);
        param.setDateTime(null);
        param.setDayStartTime(startTime);
        // 获取会员成交数据
        UserOrderStatisticListBO data = orderFeignClient.getMemberPayData(param).getData();
        List<UserOrderStatisticBO> oldUserOrderStatistic = data.getOldUserOrderStatisticList();
        List<UserOrderStatisticBO> newUserOrderStatistic = data.getNewUserOrderStatisticList();
        // 支付客户数
        MemberDealVO allMember = new MemberDealVO();
        MemberDealVO oldMember = getMemberPayInfo(oldUserOrderStatistic);
        MemberDealVO newMember = getMemberPayInfo(newUserOrderStatistic);

        allMember.setPayMemberNum(oldUserOrderStatistic.size() + newUserOrderStatistic.size());
        // 成交会员占比
        Integer payMemberNum = allMember.getPayMemberNum();
        allMember.setPayMemberNumRate(1.0);
        newMember.setPayMemberNumRate(divAverage(newMember.getPayMemberNum(),payMemberNum,4));
        oldMember.setPayMemberNumRate(divAverage(oldMember.getPayMemberNum(),payMemberNum,4));
        // 付款金额,占比
        allMember.setPayAmount(Arith.add(oldMember.getPayAmount(),newMember.getPayAmount()));
        allMember.setPayAmountRate(1.0);
        newMember.setPayAmountRate(divAverage(newMember.getPayAmount(),allMember.getPayAmount(),4));
        oldMember.setPayAmountRate(Arith.sub(allMember.getPayAmountRate(),newMember.getPayAmountRate()));
        // 客单价
        allMember.setPricePerMember(divAverage(allMember.getPayAmount(),allMember.getPayMemberNum(),2));
        // 支付订单数
        allMember.setPayOrderNum(newMember.getPayOrderNum() + oldMember.getPayOrderNum());
        respParam.setAllMember(allMember);
        respParam.setNewMember(newMember);
        respParam.setOldMember(oldMember);
        List<DateVO> everyDays = DateUtils.findEveryDays(startTime, endTime);
        for (DateVO everyDay : everyDays) {
            Date dayStartTime = everyDay.getStartTime();
            param.setStartTime(everyDay.getStartTime());
            param.setEndTime(everyDay.getEndTime());
            UserOrderStatisticListBO dataItem = orderFeignClient.getMemberPayData(param).getData();
            MemberDealTreadVO res = new MemberDealTreadVO();
            // 获取会员成交数据
            List<UserOrderStatisticBO> oldUserOrderStatisticItem = dataItem.getOldUserOrderStatisticList();
            List<UserOrderStatisticBO> newUserOrderStatisticItem = dataItem.getNewUserOrderStatisticList();
            MemberDealVO oldMemberItem = getMemberPayInfo(oldUserOrderStatisticItem);
            MemberDealVO newMemberItem = getMemberPayInfo(newUserOrderStatisticItem);
            res.setCurrentDay(DateUtils.dateToNumber(dayStartTime));
            // 支付会员数
            res.setOldPayMemberNum(oldMemberItem.getPayMemberNum());
            res.setNewPayMemberNum(newMemberItem.getPayMemberNum());
            // 支付订单数
            res.setNewPayOrderNum(newMemberItem.getPayOrderNum());
            res.setOldPayOrderNum(oldMemberItem.getPayOrderNum());
            // 支付金额
            res.setOldPayAmount(oldMemberItem.getPayAmount());
            res.setNewPayAmount(newMemberItem.getPayAmount());
            // 客单价
            res.setOldPricePerMember(divAverage(res.getOldPayAmount(),res.getOldPayMemberNum(),2));
            res.setNewPricePerMember(divAverage(res.getNewPayAmount(),res.getNewPayMemberNum(),2));
            trend.add(res);
        }
        respParam.setTrendParam(trend);
        return respParam;
    }

    private MemberDealVO getMemberPayInfo(List<UserOrderStatisticBO> userOrderStatistic) {
        MemberDealVO memberDealVO = new MemberDealVO();
        if(CollectionUtil.isEmpty(userOrderStatistic)){
            memberDealVO.setPayMemberNum(0);
            memberDealVO.setPayAmount(0.00);
            memberDealVO.setPricePerMember(0.00);
            memberDealVO.setPayOrderNum(0);
        }else {
            memberDealVO.setPayMemberNum(userOrderStatistic.size());
            long newSumAmount = userOrderStatistic.stream().mapToLong(UserOrderStatisticBO::getActualAmount).sum();
            memberDealVO.setPayAmount(handleLong(newSumAmount));
            memberDealVO.setPricePerMember(divAverage(memberDealVO.getPayAmount(), memberDealVO.getPayMemberNum(), 2));
            Integer newSumPayOrderCount = userOrderStatistic.stream().mapToInt(UserOrderStatisticBO::getConsTimes).sum();
            memberDealVO.setPayOrderNum(newSumPayOrderCount);
        }
        return memberDealVO;
    }

    @Override
    public List<CustomerRetainVO> getTradeRetained(CustomerRetainedDTO customerRetainedDTO) {
        // 交易留存/成交留存
        // 成交时间，是每一个月的时间 (例如： 2021-05, 2021-06)
        // 新成交客户数：以前从没有支付过，第一次下单支付了的用户。(余额充值，也是要生成订单信息的，所以都只需要统计订单表的信息就可以了)
        // 因为要计算用户第一次的成交时间所以一定有全表扫描的
        // 第一月留存：新成交的客户，在第一次交易后的下一个月，是否有再次交易成功的人数
        // eg：假设2021年1月份，A B C D 四位客户首次成交；2月份 A B 成交；3月份 D成交，4月份B C D有成交。求 1月份客户在后续2，3，4月份的留存率分别是多少？
        //根据前面的算法公式，则：
        //2月份的留存数是 2，留存率 = 2 /4 =50%
        //3月份的留存数是 1，留存率 = 1/4 =25%
        //4月份的留存数是 3，留存率 = 3/4 =75%
        ServerResponseEntity<List<CustomerRetainVO>> orderResponse = orderFeignClient.getTradeRetained(customerRetainedDTO);
        if (!Objects.equals(orderResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(orderResponse.getMsg());
        }
        List<CustomerRetainVO> list = orderResponse.getData();
        retainedAnalysis(list, customerRetainedDTO);
        return list;
    }

    @Override
    @Cacheable(cacheNames = FlowCacheNames.FLOW_VISIT_RETAINED_KEY, key = "#customerRetainedDTO.retainType +':'+ #customerRetainedDTO.dateType")
    public List<CustomerRetainVO> getVisitRetained(CustomerRetainedDTO customerRetainedDTO) {
        // 访问留存
        // 访问时间，是每一个月的时间 (例如： 2021-05, 2021-06)
        // 新访问客户数：之前没有访问过，第一次登录访问后，下个月再次登录访问的用户。（包含未注册登录的访客）
        // 因为要计算用户第一次的访问时间所以一定有全表扫描的
        // 第一月留存：新访问的客户，在第一次访问后的下一个月，是否有再次访问成功的人数
        // eg：假设2021年1月份，A B C D 四位客户登录访问；2月份 A B 访问；3月份 D访问，4月份B C D有访问。求 1月份客户在后续2，3，4月份的留存率分别是多少？
        //根据前面的算法公式，则：
        //2月份的留存数是 2，留存率 = 2 /4 = 50%
        //3月份的留存数是 1，留存率 = 1/4 = 25%
        //4月份的留存数是 3，留存率 = 3/4 = 75%
        List<CustomerRetainVO> list = customerAnalysisMapper.getVisitRetained(customerRetainedDTO);
        retainedAnalysis(list, customerRetainedDTO);
        return list;
    }

    @Override
    @CacheEvict(cacheNames = FlowCacheNames.FLOW_VISIT_RETAINED_KEY, key = "#customerRetainedDTO.retainType +':'+ #customerRetainedDTO.dateType")
    public void removeCacheVisitRetained(CustomerRetainedDTO customerRetainedDTO) {
    }

    @Override
    public void removeCacheTradeRetained(CustomerRetainedDTO customerRetainedDTO) {
        ServerResponseEntity<Void> orderResponse = orderFeignClient.removeCacheTradeRetained(customerRetainedDTO);
        if (!Objects.equals(orderResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(orderResponse.getMsg());
        }
    }


    private void retainedAnalysis(List<CustomerRetainVO> list, CustomerRetainedDTO customerRetainedDTO) {
        // 不在sql中统计了，在代码中来计算留存率
        // 因为最多就统计一年的留存数据,最多12行数据
        // 保留位数
        int scale = 2;
        // 放大倍数
        Integer percentage = 100;
        int size = list.size();
        int avgNewCustomers = 0;
        int avgFirstMonthRemain = 0;
        int avgSecondMonthRemain = 0;
        int avgThirdMonthRemain = 0;
        int avgFourthMonthRemain = 0;
        int avgFifthMonthRemain = 0;
        int avgSixthMonthRemain = 0;
        // 如果没有有月份不存在的，就需要收到初始化一行月份的数据了
        // 获取开始时间到结束时间的区间范围的数据
        // 可能有一些不存在的数据
        List<String> rangeDate = getRangeDate(customerRetainedDTO.getStartTime(), customerRetainedDTO.getEndTime());
        List<String> collect = list.stream().map(CustomerRetainVO::getCurrentMonth).collect(Collectors.toList());
        rangeDate.removeAll(collect);
        if (CollUtil.isNotEmpty(rangeDate)) {
            // 初始化未有新增月份的情况
            List<CustomerRetainVO> customerRetainRespParams = initCustomerRetain(rangeDate);
            list.addAll(customerRetainRespParams);
            list = list.stream().sorted(Comparator.comparing(CustomerRetainVO::getCurrentMonth)).collect(Collectors.toList());
        }
        for (CustomerRetainVO customerRetainVO : list) {
            Integer newCustomers = customerRetainVO.getNewCustomers();
            avgNewCustomers += newCustomers;
            Integer firstMonthRemain = customerRetainVO.getFirstMonthRemain();
            avgFirstMonthRemain += firstMonthRemain;
            customerRetainVO.setFirstMonthRemainRate(calculatePercentage(firstMonthRemain,newCustomers,scale, percentage));
            Integer secondMonthRemain = customerRetainVO.getSecondMonthRemain();
            avgSecondMonthRemain += secondMonthRemain;
            customerRetainVO.setSecondMonthRemainRate(calculatePercentage(secondMonthRemain,newCustomers,scale, percentage));
            Integer thirdMonthRemain = customerRetainVO.getThirdMonthRemain();
            avgThirdMonthRemain += thirdMonthRemain;
            customerRetainVO.setThirdMonthRemainRate(calculatePercentage(thirdMonthRemain,newCustomers,scale, percentage));
            Integer fourthMonthRemain = customerRetainVO.getFourthMonthRemain();
            avgFourthMonthRemain += fourthMonthRemain;
            customerRetainVO.setFourthMonthRemainRate(calculatePercentage(fourthMonthRemain,newCustomers,scale, percentage));
            Integer fifthMonthRemain = customerRetainVO.getFifthMonthRemain();
            avgFifthMonthRemain += fifthMonthRemain;
            customerRetainVO.setFifthMonthRemainRate(calculatePercentage(fifthMonthRemain,newCustomers,scale, percentage));
            Integer sixthMonthRemain = customerRetainVO.getSixthMonthRemain();
            avgSixthMonthRemain += sixthMonthRemain;
            customerRetainVO.setSixthMonthRemainRate(calculatePercentage(sixthMonthRemain,newCustomers,scale, percentage));

        }
        // 平均留存率
        CustomerRetainVO avgCustomerRetainVO = new CustomerRetainVO();
        avgNewCustomers = ceil(avgNewCustomers,size);
        avgCustomerRetainVO.setCurrentMonth("平均留存率");
        avgCustomerRetainVO.setNewCustomers(avgNewCustomers);
        avgCustomerRetainVO.setFirstMonthRemain(ceil(avgFirstMonthRemain,size));
        avgCustomerRetainVO.setFirstMonthRemainRate(calculatePercentage(avgCustomerRetainVO.getFirstMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetainVO.setSecondMonthRemain(ceil(avgSecondMonthRemain,size));
        avgCustomerRetainVO.setSecondMonthRemainRate(calculatePercentage(avgCustomerRetainVO.getSecondMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetainVO.setThirdMonthRemain(ceil(avgThirdMonthRemain,size));
        avgCustomerRetainVO.setThirdMonthRemainRate(calculatePercentage(avgCustomerRetainVO.getThirdMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetainVO.setFourthMonthRemain(ceil(avgFourthMonthRemain,size));
        avgCustomerRetainVO.setFourthMonthRemainRate(calculatePercentage(avgCustomerRetainVO.getFourthMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetainVO.setFifthMonthRemain(ceil(avgFifthMonthRemain,size));
        avgCustomerRetainVO.setFifthMonthRemainRate(calculatePercentage(avgCustomerRetainVO.getFifthMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetainVO.setSixthMonthRemain(ceil(avgSixthMonthRemain,size));
        avgCustomerRetainVO.setSixthMonthRemainRate(calculatePercentage(avgCustomerRetainVO.getSixthMonthRemain(),avgNewCustomers,scale, percentage));
        list.add(avgCustomerRetainVO);
    }

    private List<CustomerRetainVO> initCustomerRetain(List<String> rangeDate) {
        if (CollUtil.isEmpty(rangeDate)) {
            return Collections.emptyList();
        }
        List<CustomerRetainVO> list = new ArrayList<>();
        BigDecimal zero = new BigDecimal("0");
        for (String date : rangeDate) {
            CustomerRetainVO customerRetainVO = new CustomerRetainVO();
            customerRetainVO.setCurrentMonth(date);
            customerRetainVO.setNewCustomers(0);
            customerRetainVO.setFirstMonthRemain(0);
            customerRetainVO.setFirstMonthRemainRate(zero);
            customerRetainVO.setSecondMonthRemain(0);
            customerRetainVO.setSecondMonthRemainRate(zero);
            customerRetainVO.setThirdMonthRemain(0);
            customerRetainVO.setThirdMonthRemainRate(zero);
            customerRetainVO.setFourthMonthRemain(0);
            customerRetainVO.setFourthMonthRemainRate(zero);
            customerRetainVO.setFifthMonthRemain(0);
            customerRetainVO.setFifthMonthRemainRate(zero);
            customerRetainVO.setSixthMonthRemain(0);
            customerRetainVO.setSixthMonthRemainRate(zero);
            list.add(customerRetainVO);
        }
        return list;
    }

    /**
     * @param a dividend
     * @param b divisor
     * @return  向上取整
     */
    private int ceil(int a, int b) {
        if (a==0 || b==0) {
            return 0;
        }
        BigDecimal dividend = new BigDecimal(Integer.toString(a));
        BigDecimal divisor = new BigDecimal(Integer.toString(b));
        BigDecimal divide = dividend.divide(divisor,0,BigDecimal.ROUND_UP);
        return divide.intValue();
    }

    /**
     * 计算百分比
     * @param a 被除数
     * @param b 除数
     * @param scale 保留小数位
     * @param percentage 放大倍数
     * @return 放大倍数后的数值
     */
    private BigDecimal calculatePercentage(Integer a, Integer b, int scale, Integer percentage) {
        boolean isCalculate = Objects.isNull(a) || Objects.isNull(b) || Objects.isNull(percentage) ||
                a.compareTo(0)==0 || b<0 || percentage.compareTo(0)==0 ;
        if (isCalculate) {
            return new BigDecimal("0");
        }
        BigDecimal bigDecimal = new BigDecimal(a.toString());
        BigDecimal divisor = new BigDecimal(b.toString());
        BigDecimal multiplicand = new BigDecimal(percentage.toString());
        return bigDecimal.multiply(multiplicand).divide(divisor,scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取时间范围内的所有时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return yyyy-MM 的时间字符串列表
     */
    private List<String> getRangeDate(Date startTime, Date endTime) {
        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.beginOfMonth(startTime), endTime, DateField.MONTH);
        List<String> list = new ArrayList<>();
        for (DateTime dateTime : dateTimes) {
            list.add(DateUtil.format(dateTime,"yyyy-MM"));
        }
        // 控制月在最近 3 、6、 12个月
        int size = list.size();
        boolean isReduce = size == RetainedDateType.THREE_MONTH.getMonthNum() + 1 ||
                size == RetainedDateType.SIX_MONTH.getMonthNum() + 1 ||
                size == RetainedDateType.ONE_YEAR.getMonthNum() + 1;
        if (isReduce) {
            list.remove(list.get(0));
        }
        return list;
    }


    @Override
    public MemberContributeRespVO getMemberContributeValue(MemberReqDTO param) {
        MemberContributeRespVO memberContributeRespVO = new MemberContributeRespVO();
        try {
            // 当前筛选条件下的所有订单信息
            List<UserOrderStatisticBO> userOrderInfoList = orderFeignClient.getPaidMemberByParam(param).getData();
            List<Long> userIds = new ArrayList<>();
            List<UserOrderStatisticBO> userOrderList = new ArrayList<>();
            userOrderInfoList.forEach(userOrderStatisticBO -> userIds.add(userOrderStatisticBO.getUserId()));
            param.setUserIds(userIds);
            MemberContributeValueVO publicMember = new MemberContributeValueVO();
            MemberContributeValueVO paidMember = new MemberContributeValueVO();

            memberContributeRespVO.setPublicMember(publicMember);
            memberContributeRespVO.setPaidMember(paidMember);
            if(CollectionUtil.isEmpty(userIds)){
                return memberContributeRespVO;
            }
            memberContributeRespVO = userFeignClient.getMemberContributeByParam(param).getData();
            if(Objects.isNull(memberContributeRespVO)) {
                return memberContributeRespVO;
            }
            publicMember = memberContributeRespVO.getPublicMember();
            paidMember = memberContributeRespVO.getPaidMember();
            param.setDateTime(null);
            // 设置普通会员和付费会员数据的订单相关数据
            setUserOrderData(userOrderInfoList, memberContributeRespVO.getUserIds(), publicMember);
            setUserOrderData(userOrderInfoList, memberContributeRespVO.getPaidUserIds(), paidMember);
            // 占比信息
            publicMember.setTotalMemberRate(divAverage(publicMember.getTotalMember(),publicMember.getTotalMember()+paidMember.getTotalMember(),4));
            paidMember.setTotalMemberRate(Arith.sub(1,publicMember.getTotalMemberRate()));
            publicMember.setPayMemberNumRate(divAverage(publicMember.getPayMemberNum(),publicMember.getPayMemberNum()+ paidMember.getPayMemberNum(),4));
            paidMember.setPayMemberNumRate(Arith.sub(1,publicMember.getPayMemberNumRate()));
            publicMember.setPayAmountRate(divAverage(publicMember.getPayAmount(),Arith.add(publicMember.getPayAmount(),paidMember.getPayAmount()),4));
            paidMember.setPayAmountRate(Arith.sub(1,publicMember.getPayAmountRate()));

            memberContributeRespVO.setPublicMember(publicMember);
            memberContributeRespVO.setPaidMember(paidMember);
            return memberContributeRespVO;
        }catch (Exception e){
            log.info("getMemberContributeValue 异常：{} {}",e,e.getMessage());
            return memberContributeRespVO;
        }

    }

    /**
     * 设置普通会员or付费会员数据的订单相关数据（支付会员数、支付金额、支付订单数、人均消费频次、客单价、人均消费频次）
     * @param userOrderInfoList 会员订单相关数据
     * @param userIds 筛选出的符合条件userIds
     * @param memberContributeValueVO 普通会员or付费会员数据
     */
    private void setUserOrderData(List<UserOrderStatisticBO> userOrderInfoList, List<Long> userIds, MemberContributeValueVO memberContributeValueVO) {
        List<UserOrderStatisticBO> userOrderList;
        // TODO 支付频次暂不统计 时间段内：支付订单数 / 消费人数
        if(CollectionUtil.isEmpty(userIds)){
            // 支付会员数
            memberContributeValueVO.setPayMemberNum(0);
            // 支付金额
            memberContributeValueVO.setPayAmount(0.00);
            // 会员支付订单数
            memberContributeValueVO.setPayOrderNum(0);
            // 会员客单价
            memberContributeValueVO.setPricePerMember(0.00);
            // 人均消费频次
            memberContributeValueVO.setFrequencyOfConsume(0.0);
        }else {
            // 筛选交集的用户
            userOrderList = userOrderInfoList.stream().filter(userOrder->userIds.contains(
                    userOrder.getUserId())).collect(Collectors.toList());
            long sumAmount = userOrderList.stream().mapToLong(UserOrderStatisticBO::getActualAmount).sum();
            Integer sumPayOrderCount = userOrderList.stream().mapToInt(UserOrderStatisticBO::getConsTimes).sum();
            // 会员支付人数
            memberContributeValueVO.setPayMemberNum(userOrderList.size());
            // 会员支付金额
            memberContributeValueVO.setPayAmount(handleLong(sumAmount));
            // 会员支付订单数
            memberContributeValueVO.setPayOrderNum(sumPayOrderCount);
            // 会员客单价
            memberContributeValueVO.setPricePerMember(divAverage(memberContributeValueVO.getPayAmount(), memberContributeValueVO.getPayMemberNum(), 4));
            // 人均消费频次
            memberContributeValueVO.setFrequencyOfConsume(divAverage(memberContributeValueVO.getPayOrderNum(), memberContributeValueVO.getPayMemberNum(),2));
        }
    }

    private Double handleLong(Long value){
        if (Objects.isNull(value)){
            return 0.00;
        }
        return PriceUtil.toDecimalPrice(value).doubleValue();
    }

    private Double getDouble(Double value) {
        if (Objects.isNull(value)) {
            return 0.0;
        }
        return value;
    }

    private Double divAverage(Integer a, Integer b, Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a, b, scale);
    }

    private Double divAverage(Double a, Integer b, Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a, b, scale);
    }
    private Double divAverage(Double a, Double b, Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a, b, scale);
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
}
