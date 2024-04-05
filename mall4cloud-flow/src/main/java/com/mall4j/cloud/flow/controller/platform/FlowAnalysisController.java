package com.mall4j.cloud.flow.controller.platform;


import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.flow.constant.FlowTimeTypeEnum;
import com.mall4j.cloud.flow.dto.FlowAnalysisDTO;
import com.mall4j.cloud.flow.service.FlowService;
import com.mall4j.cloud.flow.service.FlowUserAnalysisService;
import com.mall4j.cloud.flow.vo.FlowAnalysisVO;
import com.mall4j.cloud.flow.vo.SystemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 流量概括
 *
 * @author YXF
 * @date 2021-06-02
 */
@RestController
@RequestMapping("/p/flow_analysis")
@Api(tags = "platform-流量概括")
public class FlowAnalysisController {

    @Autowired
    private FlowUserAnalysisService flowUserAnalysisService;


    @Autowired
    private FlowService flowService;

    /**
     * 流量总览
     */
    @GetMapping("/flow_all")
    @ApiOperation(value = "流量总览", notes = "流量总览")
    public ServerResponseEntity<FlowAnalysisVO> page(FlowAnalysisDTO flowAnalysisDTO) {
        handleRequest(flowAnalysisDTO);
        // 实时数据，需要先进行统计（由于这里四个接口都是一起请求的，所以只在这个接口进行处理）
        flowService.statisticalUser();
        //获取开始和结束时间
        FlowAnalysisVO flowAnalysisDto = flowUserAnalysisService.getFlowAnalysisData(flowAnalysisDTO);
        return ServerResponseEntity.success(flowAnalysisDto);
    }

    /**
     * 流量趋势
     */
    @GetMapping("/flow_trend")
    @ApiOperation(value = "流量趋势", notes = "流量趋势")
    public ServerResponseEntity<List<FlowAnalysisVO>> flowTrend(FlowAnalysisDTO flowAnalysisDTO) {
        handleRequest(flowAnalysisDTO);
        handleTime(flowAnalysisDTO);
        // 获取开始和结束时间
        List<FlowAnalysisVO> flowAnalysisDtoList = flowUserAnalysisService.flowTrend(flowAnalysisDTO);
        return ServerResponseEntity.success(flowAnalysisDtoList);
    }

    /**
     * 流量来源构成
     */
    @GetMapping("/flow_sour")
    @ApiOperation(value = "流量来源构成", notes = "流量来源构成")
    public ServerResponseEntity<List<FlowAnalysisVO>> flowSour(FlowAnalysisDTO flowAnalysisDTO) {
        handleRequest(flowAnalysisDTO);
        List<FlowAnalysisVO> flowAnalysisDtoList = flowUserAnalysisService.flowSour(flowAnalysisDTO);
        return ServerResponseEntity.success(flowAnalysisDtoList);
    }

    /**
     * 系统访客数量
     */
    @GetMapping("/flow_system")
    @ApiOperation(value = "系统访客数量", notes = "系统访客数量")
    public ServerResponseEntity<SystemVO> systemTypeNums(FlowAnalysisDTO flowAnalysisDTO) {
        handleRequest(flowAnalysisDTO);
        SystemVO systemVO = flowUserAnalysisService.systemTypeNums(flowAnalysisDTO);
        return ServerResponseEntity.success(systemVO);
    }

    /**
     * 处理统计时间
     * @param flowAnalysisDTO
     */
    private void handleTime(FlowAnalysisDTO flowAnalysisDTO) {
        Date startTime = flowAnalysisDTO.getStartTime();
        Date endTime = flowAnalysisDTO.getEndTime();
        if (Objects.equals(flowAnalysisDTO.getTimeType(), FlowTimeTypeEnum.MONTH.value())) {
            endTime = DateUtil.endOfMonth(flowAnalysisDTO.getEndTime());
            startTime = DateUtil.beginOfMonth(DateUtil.offsetMonth(flowAnalysisDTO.getEndTime(), -11));
        } else if (Objects.equals(flowAnalysisDTO.getTimeType(), FlowTimeTypeEnum.WEEK.value())) {
            endTime = DateUtil.endOfWeek(flowAnalysisDTO.getEndTime());
            startTime = DateUtil.offsetWeek(DateUtil.beginOfWeek(flowAnalysisDTO.getEndTime()), -11);
        } else if (Objects.equals(flowAnalysisDTO.getTimeType(), FlowTimeTypeEnum.DAY.value())
                || Objects.equals(flowAnalysisDTO.getTimeType(), FlowTimeTypeEnum.REAL_TIME.value())) {
            endTime = DateUtil.endOfDay(flowAnalysisDTO.getEndTime());
            startTime = DateUtil.offsetDay(DateUtil.beginOfDay(flowAnalysisDTO.getEndTime()), -29);
        } else {
            endTime = DateUtil.endOfDay(endTime);
            startTime = DateUtil.beginOfDay(startTime);
        }
        flowAnalysisDTO.setEndTime(endTime);
        flowAnalysisDTO.setStartTime(startTime);
    }

    /**
     * 处理请求参数
     * @param flowAnalysisDTO
     */
    private void handleRequest(FlowAnalysisDTO flowAnalysisDTO) {
        if (Objects.equals(flowAnalysisDTO.getSystemType(), 0)) {
            flowAnalysisDTO.setSystemType(null);
        }
        if (Objects.isNull(flowAnalysisDTO.getStartTime())) {
            throw new LuckException("开始时间不能为空");
        }
        flowAnalysisDTO.setStartTime(DateUtil.beginOfDay(flowAnalysisDTO.getStartTime()));
        flowAnalysisDTO.setEndTime(DateUtil.endOfDay(flowAnalysisDTO.getEndTime()));
    }
}
