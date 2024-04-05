package com.mall4j.cloud.order.service.impl;
import com.google.common.collect.Lists;
import java.util.Date;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderPayRefundRespDTO;
import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderRefundRespDTO;
import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderRefundSearchDTO;
import com.mall4j.cloud.api.platform.feign.TzTagFeignClient;
import com.mall4j.cloud.api.platform.vo.TzTagDetailVO;
import com.mall4j.cloud.api.platform.vo.TzTagStoreDetailVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.order.dto.OrderDetailReportRequest;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.service.StaffOrderStatisticsService;
import com.mall4j.cloud.order.vo.OrderOverviewVO;
import com.mall4j.cloud.order.vo.OrderRefundStatisticsVO;
import com.mall4j.cloud.order.vo.StaffOrderOverviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StaffOrderStatisticsServiceImpl implements StaffOrderStatisticsService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    TzTagFeignClient tzTagFeignClient;

    @DS("slave")
    @Override
    public StaffOrderOverviewVO getStaffOrderOverviewVO(Long staffId) {
        StaffOrderOverviewVO staffOrderOverviewVO = new StaffOrderOverviewVO();

        ServerResponseEntity<List<TzTagDetailVO>> serverResponse = tzTagFeignClient.listTagByStaffId(staffId);
        log.info("StaffOrderStatisticsServiceImpl.getStaffOrderOverviewVO 。查询导购门店标签参数:{},返回结果：{}。",staffId, JSONObject.toJSONString(serverResponse));
        if(serverResponse==null || serverResponse.isFail()){
            Assert.faild("导购查询门店标签失败。");
        }
        List<TzTagDetailVO> tzTagDetailVOS = serverResponse.getData();

        List<Long> storeIds = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(tzTagDetailVOS)){
            for (TzTagDetailVO tzTagDetailVO : tzTagDetailVOS) {
                if (CollectionUtil.isNotEmpty(tzTagDetailVO.getStores())){
                    storeIds.addAll(tzTagDetailVO.getStores().stream().map(TzTagStoreDetailVO::getStoreId).collect(Collectors.toList()));
                }
            }
            storeIds.stream().distinct();
        }

        if(CollectionUtil.isEmpty(storeIds)){
            Assert.faild("当前导购标签没有指定门店，不支持查询门店概览业绩数据。");
        }

        //查询当前导购标签关联的门店
        List<Long> shopIds = storeIds;

        //当天统计数据
        String dayBeginTime = DateUtil.beginOfDay(DateUtil.date()).toString();
        String dayEndTime = DateUtil.offsetDay(DateUtil.beginOfDay(DateUtil.date()),1).toString();
        OrderOverviewVO dayOrderOverviewVO = orderMapper.getStaffOrderOverviewInfoByShopId(shopIds, dayBeginTime, dayEndTime);
        staffOrderOverviewVO.setPayActual(dayOrderOverviewVO.getPayActual());
        staffOrderOverviewVO.setPayOrderCount(dayOrderOverviewVO.getPayOrderCount());
        staffOrderOverviewVO.setPayUserCount(dayOrderOverviewVO.getPayUserCount());
        double dayOnePrice = (dayOrderOverviewVO.getPayUserCount() == 0) ?
                0 : (Arith.div(dayOrderOverviewVO.getPayActual(), dayOrderOverviewVO.getPayUserCount(), 2));
        staffOrderOverviewVO.setOnePrice(dayOnePrice);

        DistributionJointVentureOrderRefundSearchDTO dayDistributionJointVentureOrderRefundSearchDTO = new DistributionJointVentureOrderRefundSearchDTO();
        dayDistributionJointVentureOrderRefundSearchDTO.setRefundStartTime(DateUtil.beginOfDay(DateUtil.date()));
        dayDistributionJointVentureOrderRefundSearchDTO.setRefundEndTime(DateUtil.offsetDay(DateUtil.beginOfDay(DateUtil.date()),1));
        dayDistributionJointVentureOrderRefundSearchDTO.setStoreIdList(shopIds);
        DistributionJointVentureOrderPayRefundRespDTO dayOrderPayRefundRespDTO = orderMapper.totalDistributionJointVenturePayRefund(dayDistributionJointVentureOrderRefundSearchDTO);
        staffOrderOverviewVO.setNeedRefundAmount((double)dayOrderPayRefundRespDTO.getRefundAmountTotal());
        staffOrderOverviewVO.setNeedApplyAmount((double)(dayOrderPayRefundRespDTO.getPayActualTotal()-dayOrderPayRefundRespDTO.getFreightAmountTotal()-dayOrderPayRefundRespDTO.getRefundAmountTotal()));


        //月统计数据
        String monthBeginTime = DateUtil.beginOfMonth(DateUtil.date()).toString();
        String monthEndTime = DateUtil.endOfMonth(DateUtil.date()).toString();
        OrderOverviewVO monthOrderOverviewVO = orderMapper.getStaffOrderOverviewInfoByShopId(shopIds, monthBeginTime, monthEndTime);
        staffOrderOverviewVO.setCurrentMonthPayActual(monthOrderOverviewVO.getPayActual());
        staffOrderOverviewVO.setCurrentMonthPayOrderCount(monthOrderOverviewVO.getPayOrderCount());
        staffOrderOverviewVO.setCurrentMonthPayUserCount(monthOrderOverviewVO.getPayUserCount());
        double monthOnePrice = (monthOrderOverviewVO.getPayUserCount() == 0) ?
                0 : (Arith.div(monthOrderOverviewVO.getPayActual(), monthOrderOverviewVO.getPayUserCount(), 2));
        staffOrderOverviewVO.setCurrentMonthOnePrice(monthOnePrice);

        DistributionJointVentureOrderRefundSearchDTO monthDistributionJointVentureOrderRefundSearchDTO = new DistributionJointVentureOrderRefundSearchDTO();
        monthDistributionJointVentureOrderRefundSearchDTO.setRefundStartTime(DateUtil.beginOfMonth(DateUtil.date()));
        monthDistributionJointVentureOrderRefundSearchDTO.setRefundEndTime(DateUtil.endOfMonth(DateUtil.date()));
        monthDistributionJointVentureOrderRefundSearchDTO.setStoreIdList(shopIds);
        DistributionJointVentureOrderPayRefundRespDTO monthOrderPayRefundRespDTO = orderMapper.totalDistributionJointVenturePayRefund(monthDistributionJointVentureOrderRefundSearchDTO);
        staffOrderOverviewVO.setCurrentMonthNeedRefundAmount((double)monthOrderPayRefundRespDTO.getRefundAmountTotal());
        staffOrderOverviewVO.setCurrentMonthNeedApplyAmount((double)(monthOrderPayRefundRespDTO.getPayActualTotal()-monthOrderPayRefundRespDTO.getFreightAmountTotal()-monthOrderPayRefundRespDTO.getRefundAmountTotal()));



        return staffOrderOverviewVO;
    }

//    public static void main(String[] args) {
//        System.out.println(DateUtil.beginOfDay(DateUtil.date()).toString());
//        System.out.println(DateUtil.offsetDay(DateUtil.beginOfDay(DateUtil.date()),1).toString());
//
//
//        System.out.println(DateUtil.beginOfMonth(DateUtil.date()).toString());
//        System.out.println(DateUtil.endOfMonth(DateUtil.date()).toString());
//    }


    @DS("slave")
    @Override
    public StaffOrderOverviewVO getOrderDetailReport(OrderDetailReportRequest orderDetailReportRequest) {

        ServerResponseEntity<List<TzTagDetailVO>> serverResponse = tzTagFeignClient.listTagByStaffId(orderDetailReportRequest.getStaffId());
        log.info("StaffOrderStatisticsServiceImpl.getOrderDetailReport 。查询导购门店标签参数:{},返回结果：{}。",orderDetailReportRequest.getStaffId(), JSONObject.toJSONString(serverResponse));
        if(serverResponse==null || serverResponse.isFail()){
            Assert.faild("导购查询门店标签失败。");
        }
        List<TzTagDetailVO> tzTagDetailVOS = serverResponse.getData();

        if(CollectionUtil.isEmpty(tzTagDetailVOS)){
            Assert.faild("当前导购没有区域运营权限，不支持查询门店概览业绩数据。");
        }


        StaffOrderOverviewVO staffOrderOverviewVO = new StaffOrderOverviewVO();

        List<Long> storeIds = orderDetailReportRequest.getStoreIds();
        String beginTime = DateUtil.beginOfDay(DateUtil.parse(orderDetailReportRequest.getBeginTime()).toJdkDate()).toString();
        String endTime = DateUtil.endOfDay(DateUtil.parse(orderDetailReportRequest.getEndTime()).toJdkDate()).toString();

        OrderOverviewVO dayOrderOverviewVO = orderMapper.getStaffOrderOverviewInfoByShopId(storeIds,beginTime, endTime);

        DistributionJointVentureOrderRefundSearchDTO distributionJointVentureOrderRefundSearchDTO = new DistributionJointVentureOrderRefundSearchDTO();
        distributionJointVentureOrderRefundSearchDTO.setRefundStartTime(DateUtil.beginOfDay(DateUtil.parse(orderDetailReportRequest.getBeginTime())));
        distributionJointVentureOrderRefundSearchDTO.setRefundEndTime(DateUtil.endOfDay(DateUtil.parse(orderDetailReportRequest.getEndTime())));
        distributionJointVentureOrderRefundSearchDTO.setStoreIdList(orderDetailReportRequest.getStoreIds());
        DistributionJointVentureOrderPayRefundRespDTO orderPayRefundRespDTO = orderMapper.totalDistributionJointVenturePayRefund(distributionJointVentureOrderRefundSearchDTO);

        log.info("StaffOrderStatisticsServiceImpl.getOrderDetailReport: {},实付退款信息:{}",JSONObject.toJSONString(dayOrderOverviewVO),JSONObject.toJSONString(orderPayRefundRespDTO));
        //支付总金额
        staffOrderOverviewVO.setPayActual(dayOrderOverviewVO.getPayActual());
        //订单数
        staffOrderOverviewVO.setPayOrderCount(dayOrderOverviewVO.getPayOrderCount());
        //支付人数
        staffOrderOverviewVO.setPayUserCount(dayOrderOverviewVO.getPayUserCount());
        //客单价
        double dayOnePrice = (dayOrderOverviewVO.getPayUserCount() == 0) ?
                0 : (Arith.div(dayOrderOverviewVO.getPayActual(), dayOrderOverviewVO.getPayUserCount(), 2));
        staffOrderOverviewVO.setOnePrice(dayOnePrice);
        //退款金额
        staffOrderOverviewVO.setNeedRefundAmount((double)orderPayRefundRespDTO.getRefundAmountTotal());
        //实付金额
        staffOrderOverviewVO.setNeedApplyAmount((double)(orderPayRefundRespDTO.getPayActualTotal()-orderPayRefundRespDTO.getFreightAmountTotal()-orderPayRefundRespDTO.getRefundAmountTotal()));

        //支付总件数
        staffOrderOverviewVO.setSpuCount(dayOrderOverviewVO.getSpuCount());
        //件单价 = 支付金额 / 支付总件数
        double spuPrice = (dayOrderOverviewVO.getSpuCount() == 0) ?
                0 : (Arith.div(dayOrderOverviewVO.getPayActual(), dayOrderOverviewVO.getSpuCount(), 2));
        staffOrderOverviewVO.setSpuPrice(spuPrice);

        //连带率 = 支付件数/订单数
        double liandai = (dayOrderOverviewVO.getSpuCount() == 0) ?
                0 : (Arith.div(dayOrderOverviewVO.getSpuCount(), dayOrderOverviewVO.getPayOrderCount(), 1));
        staffOrderOverviewVO.setLiandai(liandai);

        // 按小时 获取的支付金额列表
        if(orderDetailReportRequest.getDateType()==0){

            List<Double> actualList = this.getActualByHour(storeIds, beginTime, endTime);

            staffOrderOverviewVO.setPayActualList(actualList);
        }

        // 按天 获取支付金额列表
        if(orderDetailReportRequest.getDateType()==1){
            int dayCount = orderDetailReportRequest.getDayCount();
            String startTime = orderDetailReportRequest.getBeginTime();

            List<OrderOverviewVO> orderOverviewVOList = orderMapper.staffListOrderOverviewInfoByShopIdAndDateRange(storeIds, beginTime, endTime);
            log.info("导购门店业绩概览参数： {},{},{},按天查询结果返回：{}",storeIds,beginTime,endTime,JSONObject.toJSONString(orderOverviewVOList));
            Map<?, OrderOverviewVO> orderOverviewMap = orderOverviewVOList.stream().collect(Collectors.toMap(OrderOverviewVO::getTimeData, o -> o));

            List<Double> actualList = new ArrayList<>();
            List<String> dateToStringList = new ArrayList<>();

            for (int i = 0; i < dayCount; i++) {
                if (orderOverviewMap.containsKey(startTime)) {
                    OrderOverviewVO overviewVO = orderOverviewMap.get(startTime);
                    actualList.add(Arith.div(overviewVO.getPayActual(), 100, 2));
                } else {
                    actualList.add(0.0);
                }
                dateToStringList.add(startTime);
                startTime = getNextTime(startTime);
            }
            staffOrderOverviewVO.setPayActualList(actualList);
            staffOrderOverviewVO.setDateToStringList(dateToStringList);
        }


        return staffOrderOverviewVO;
    }

    /**
     * 根据店铺Ids 与时间范围按小时分段获取数据支付金额列表
     * @param startTime
     * @param endTime
     */
    private List<Double> getActualByHour(List<Long> storeIds, String startTime, String endTime) {
        List<OrderOverviewVO> orderOverviewVOList = orderMapper.stafflistActualByHour(storeIds, startTime, endTime);
        log.info("导购门店业绩看板 按小时查询：参数：{},{},{}，查询结果：{}，",storeIds,startTime,endTime,JSONObject.toJSONString(orderOverviewVOList));
        Map<Integer, Double> payMap = new HashMap<>(30);
        for (OrderOverviewVO temp : orderOverviewVOList) {
            payMap.put(Integer.parseInt(temp.getTimeData()), temp.getPayActual());
        }
        List<Double> nowActual = new ArrayList<>();
        for (int i = 0; i < Constant.MAX_HOUR_NUM_BY_DAY; i++) {
            if (payMap.get(i) != null) {
                nowActual.add( Arith.div(payMap.get(i), 100, 2));
            }else{
                nowActual.add(0.0);
            }

        }
        return nowActual;
    }

    /**
     * 获取下一次循环的开始时间，以及设置后台显示的时间字符串
     * @param startTime 开始时间
     * @return
     */
    private String getNextTime(String startTime){
        startTime = DateUtil.offsetDay(DateUtil.parseDate(startTime).toJdkDate(), 1).toString("yyyy-MM-dd");
        return startTime;
    }
}
