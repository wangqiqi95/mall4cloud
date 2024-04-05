package com.mall4j.cloud.flow.service.impl;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.flow.constant.FlowTimeTypeEnum;
import com.mall4j.cloud.flow.dto.FlowAnalysisDTO;
import com.mall4j.cloud.flow.mapper.UserAnalysisMapper;
import com.mall4j.cloud.flow.service.FlowUserAnalysisService;
import com.mall4j.cloud.flow.util.FlowArithUtil;
import com.mall4j.cloud.flow.vo.FlowAnalysisRatioVO;
import com.mall4j.cloud.flow.vo.FlowAnalysisVO;
import com.mall4j.cloud.flow.vo.SystemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
/**
 * 流量分析—用户访问数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@Service
public class FlowUserAnalysisServiceImpl implements FlowUserAnalysisService {

    @Autowired
    private UserAnalysisMapper userAnalysisMapper;

    @Override
    public FlowAnalysisVO getFlowAnalysisData(FlowAnalysisDTO flowAnalysisDTO) {
        //获取用于跟新数据比较的旧数据的开始时间
        Date oldStartTime = getOldStartTime(flowAnalysisDTO.getTimeType(),flowAnalysisDTO.getStartTime());
        //获取数据列表--时间端：（startTime(旧数据开始时间)--->旧数据区间<---flowAnalysisDTO.startTime(新数据开始时间)--->新数据区间<---flowAnalysisDTO.endTime(新数据结束时间)）
        List<FlowAnalysisVO> flowAnalysisList = userAnalysisMapper.getFlowAnalysisData(flowAnalysisDTO, oldStartTime);
        // 统计数据库中无法获取的数据
        handleFlowAnalysisData(flowAnalysisList);
        // 统计数据增减的比例
        return calculateProportion(flowAnalysisList);
    }

    @Override
    public List<FlowAnalysisVO> flowTrend(FlowAnalysisDTO flowAnalysisDTO) {
        List<FlowAnalysisVO> flowAnalysisList = Lists.newArrayList();
        //获取数据
        List<FlowAnalysisVO> flowAnalysisListDb = userAnalysisMapper.flowTrend(flowAnalysisDTO);
        //制作表格显示的数据（补充上数据库中没有获取到的时间日期）
        Date startTime= flowAnalysisDTO.getStartTime();
        Date endTime = flowAnalysisDTO.getEndTime();
        //时间类型 2:自然周 3:自然月
        Map<?, FlowAnalysisVO> flowAnalysisAsDate = null;
        if (flowAnalysisDTO.getTimeType().equals(FlowTimeTypeEnum.WEEK.value()) || flowAnalysisDTO.getTimeType().equals(FlowTimeTypeEnum.MONTH.value())) {
            //自然日 Map<Date, FlowAnalysisVO>  flowAnalysisAsDate
            flowAnalysisAsDate = flowAnalysisListDb.stream().collect(Collectors.toMap(FlowAnalysisVO::getCreateDate, f -> f));
        } else {
            //自然周、自然月 Map<String, FlowAnalysisVO>  flowAnalysisAsDate
            flowAnalysisAsDate = flowAnalysisListDb.stream().collect(Collectors.toMap(FlowAnalysisVO::getDay, f -> f));
        }
        //自然日、自然周、自然月 可以写在一个循环里，但每次循环都需要进行一次时间类型的判断，所以分开为多个循环
        Integer timeType = flowAnalysisDTO.getTimeType();
        if (timeType.equals(FlowTimeTypeEnum.MONTH.value())){
            //自然月
            while (startTime.getTime()<endTime.getTime()){
                Integer key = DateUtil.month(startTime) + 1;
                FlowAnalysisVO flowAnalysisVO = new FlowAnalysisVO();
                if (flowAnalysisAsDate.containsKey(key)){
                    flowAnalysisVO = flowAnalysisAsDate.get(key);
                }
                startTime = getNextTime(flowAnalysisVO, startTime,timeType);
                flowAnalysisList.add(flowAnalysisVO);
            }
        } else if (timeType.equals(FlowTimeTypeEnum.WEEK.value())){
            //自然周
            while (startTime.getTime()<endTime.getTime()){
                // java中计算的周从0开始，数据库从1开始
                Integer key = DateUtil.weekOfYear(startTime) - 1;
                FlowAnalysisVO flowAnalysisVO = new FlowAnalysisVO();
                if (flowAnalysisAsDate.containsKey(key)){
                    flowAnalysisVO = flowAnalysisAsDate.get(key);
                }
                startTime = getNextTime(flowAnalysisVO, startTime,timeType);
                flowAnalysisList.add(flowAnalysisVO);
            }
        } else {
            //自然日
            while (startTime.getTime()<endTime.getTime()){
                FlowAnalysisVO flowAnalysisVO = new FlowAnalysisVO();
                if (flowAnalysisAsDate.containsKey(startTime)){
                    flowAnalysisVO = flowAnalysisAsDate.get(startTime);
                }
                startTime = getNextTime(flowAnalysisVO, startTime,timeType);
                flowAnalysisList.add(flowAnalysisVO);
            }
        }
        handleFlowAnalysisData(flowAnalysisList);
        return flowAnalysisList;
    }

    @Override
    public List<FlowAnalysisVO> flowSour(FlowAnalysisDTO flowAnalysisDTO) {
        //获取数据
        List<FlowAnalysisVO> flowAnalysisList = userAnalysisMapper.flowSour(flowAnalysisDTO);
        handleFlowAnalysisData(flowAnalysisList);
        return flowAnalysisList;
    }

    @Override
    public SystemVO systemTypeNums(FlowAnalysisDTO flowAnalysisDTO) {
        SystemVO systemDto = new SystemVO();
        //获取数据（数据库）
        List<SystemVO> systemDtoList = userAnalysisMapper.systemTypeNums(flowAnalysisDTO);
        for (SystemVO system: systemDtoList){
            switch (system.getSystemType()){
                case 0: systemDto.setAll(system.getUserNums());break;
                case 1: systemDto.setPc(system.getUserNums());break;
                case 2: systemDto.setH5(system.getUserNums());break;
                case 3: systemDto.setApplets(system.getUserNums());break;
                case 4: systemDto.setAndroid(system.getUserNums());break;
                case 5: systemDto.setIos(system.getUserNums());break;
                default: break;
            }
        }
        return systemDto;
    }

    /**
     * 处理数据库中无法获取的数据
     * @param flowAnalysisList
     */
    private void handleFlowAnalysisData(List<FlowAnalysisVO> flowAnalysisList) {
        for (FlowAnalysisVO flowAnalysis : flowAnalysisList) {
            if (Objects.isNull(flowAnalysis.getUserNums())) {
                flowAnalysis.setUserNums(0);
            }
            if (Objects.isNull(flowAnalysis.getNewUserNums())) {
                flowAnalysis.setNewUserNums(0);
            }
            if (Objects.isNull(flowAnalysis.getVisitNums())) {
                flowAnalysis.setVisitNums(0);
            }
            if (Objects.isNull(flowAnalysis.getAverageVisitNums())) {
                flowAnalysis.setAverageVisitNums(0D);
            }
            if (Objects.isNull(flowAnalysis.getLossUser())) {
                flowAnalysis.setLossUser(0);
            }
            if (Objects.isNull(flowAnalysis.getPlusShopCart())) {
                flowAnalysis.setPlusShopCart(0);
            }
            if (Objects.isNull(flowAnalysis.getPlusShopCartUser())) {
                flowAnalysis.setPlusShopCartUser(0);
            }
            if (Objects.isNull(flowAnalysis.getPlaceOrderUser())) {
                flowAnalysis.setPlaceOrderUser(0);
            }
            if (Objects.isNull(flowAnalysis.getPayUser())) {
                flowAnalysis.setPayUser(0);
            }
            if (Objects.isNull(flowAnalysis.getPlaceOrderAmount())) {
                flowAnalysis.setPlaceOrderAmount(Constant.ZERO_LONG);
            }
            if (Objects.isNull(flowAnalysis.getPayAmount())) {
                flowAnalysis.setPayAmount(Constant.ZERO_LONG);
            }
            flowAnalysis.setAverageVisitNums(FlowArithUtil.getAverage(flowAnalysis.getVisitNums(),flowAnalysis.getUserNums()));
            flowAnalysis.setLossUserRate(FlowArithUtil.getRatio(flowAnalysis.getLossUser(), flowAnalysis.getUserNums()));
            flowAnalysis.setPayRate(FlowArithUtil.getRatio(flowAnalysis.getPayUser(),flowAnalysis.getUserNums()));
            flowAnalysis.setPlaceOrderRate(FlowArithUtil.getRatio(flowAnalysis.getPlaceOrderUser(),flowAnalysis.getUserNums()));
            flowAnalysis.setUvAmount(FlowArithUtil.getAnalysisAmount(flowAnalysis.getPayAmount(),flowAnalysis.getUserNums()));
            flowAnalysis.setCustomerAmount(FlowArithUtil.getAnalysisAmount(flowAnalysis.getPayAmount(),flowAnalysis.getPayUser()));
        }
    }

    /**
     * 获取下一次循环的开始时间，以及设置后台显示的时间字符串
     * @param flowAnalysisVO
     * @param startTime 开始时间
     * @param type 时间类型
     * @return
     */
    private Date getNextTime(FlowAnalysisVO flowAnalysisVO,Date startTime,Integer type){
        if (type.equals(FlowTimeTypeEnum.WEEK.value())) {
            //时间类型 2:自然周
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            //设置周一为每周的开始日期
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            flowAnalysisVO.setDateTime(DateUtil.format(startTime,"yyyy第ww周"));
            startTime = DateUtil.offsetWeek(startTime,1);
        }else if (type.equals(FlowTimeTypeEnum.MONTH.value())){
            //时间类型 3:自然月
            flowAnalysisVO.setDateTime(DateUtil.format(startTime,"yyyy-MM"));
            startTime = DateUtil.offsetMonth(startTime,1);
        }else {
            flowAnalysisVO.setDateTime(DateUtil.format(startTime,"yyyy-MM-dd"));
            startTime = DateUtil.offsetDay(startTime, 1);
        }
        return startTime;
    }

    /**
     * 统计数据的增减比例
     * @param flowAnalysisVOList
     */
    private FlowAnalysisVO calculateProportion(List<FlowAnalysisVO> flowAnalysisVOList) {
        Map<Boolean, FlowAnalysisVO> map = flowAnalysisVOList.stream().collect(Collectors.toMap(FlowAnalysisVO::getNewData, s -> s));
        FlowAnalysisVO flowAnalysisVO = map.get(Boolean.TRUE);
        FlowAnalysisVO oldFlowAnalysisVO = map.get(Boolean.FALSE);
        FlowAnalysisRatioVO ratio = new FlowAnalysisRatioVO();
        flowAnalysisVO.setRatio(ratio);
        //获取比例大小
        ratio.setUserNumsRatio(FlowArithUtil.getIncreaseRatio(flowAnalysisVO.getUserNums(), oldFlowAnalysisVO.getUserNums()));
        ratio.setNewUserNumsRatio(FlowArithUtil.getIncreaseRatio(flowAnalysisVO.getNewUserNums(), oldFlowAnalysisVO.getNewUserNums()));
        ratio.setLossUserRatio(FlowArithUtil.getIncreaseRatio(flowAnalysisVO.getLossUserRate(), oldFlowAnalysisVO.getLossUserRate()));
        ratio.setVisitNumsRatio(FlowArithUtil.getIncreaseRatio(flowAnalysisVO.getVisitNums(), oldFlowAnalysisVO.getVisitNums()));
        ratio.setAverageVisitNumsRatio(FlowArithUtil.getIncreaseRatio(flowAnalysisVO.getAverageVisitNums(), oldFlowAnalysisVO.getAverageVisitNums()));
        ratio.setPlusShopCartUserRatio(FlowArithUtil.getIncreaseRatio(flowAnalysisVO.getPlusShopCartUser(), oldFlowAnalysisVO.getPlusShopCartUser()));
        ratio.setPlaceOrderRatio(Arith.sub(flowAnalysisVO.getPlaceOrderRate(), oldFlowAnalysisVO.getPlaceOrderRate()));
        ratio.setPlaceOrderUserRatio(FlowArithUtil.getIncreaseRatio(flowAnalysisVO.getPlaceOrderUser(), oldFlowAnalysisVO.getPayUser()));
        ratio.setPayRatio(Arith.sub(flowAnalysisVO.getPayRate(), oldFlowAnalysisVO.getPayRate()));
        ratio.setPayUserRatio(FlowArithUtil.getIncreaseRatio(flowAnalysisVO.getPayUser(), oldFlowAnalysisVO.getPayUser()));
        return flowAnalysisVO;
    }

    /**
     * 获取用户比较上一段统计时间的开始时间
     * @param timeType 时间类型
     * @param startTime 当前统计数据的开始时间
     * @return 上一段统计时间的开始时间
     */
    public Date getOldStartTime(Integer timeType, Date startTime){
        FlowTimeTypeEnum flowTimeTypeEnum = FlowTimeTypeEnum.instance(timeType);
        return DateUtil.offsetDay(startTime,-flowTimeTypeEnum.getNum());
    }
}
