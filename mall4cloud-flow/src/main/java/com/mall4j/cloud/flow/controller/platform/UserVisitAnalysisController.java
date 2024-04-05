package com.mall4j.cloud.flow.controller.platform;


import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.flow.constant.FlowTimeTypeEnum;
import com.mall4j.cloud.flow.dto.FlowAnalysisDTO;
import com.mall4j.cloud.flow.service.UserVisitAnalysisService;
import com.mall4j.cloud.flow.vo.FlowUserAnalysisVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * 流量分析—用户访问数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@RestController("platformUserVisitAnalysisController")
@RequestMapping("/p/user_visit_analysis")
@Api(tags = "platform-用户访问统计")
public class UserVisitAnalysisController {

    @Autowired
    private UserVisitAnalysisService userVisitAnalysisService;

    @GetMapping("/get_user_analysis_data")
    @ApiOperation(value = "获取访客分析数据", notes = "获取访客分析数据")
    public ServerResponseEntity<FlowUserAnalysisVO> getUserAnalysisData(FlowAnalysisDTO flowAnalysisDTO) {
        // 获取时间范围
        this.setDateRange(flowAnalysisDTO);
        FlowUserAnalysisVO flowUserAnalysisVO = userVisitAnalysisService.getUserAnalysisData(flowAnalysisDTO);
        return ServerResponseEntity.success(flowUserAnalysisVO);
    }

    /**
     * 获取时间范围
     * @param flowAnalysisDTO
     */
    private void setDateRange(FlowAnalysisDTO flowAnalysisDTO) {
        Date now = new Date();
        switch (Objects.requireNonNull(FlowTimeTypeEnum.instance(flowAnalysisDTO.getTimeType()))) {
            case DAY:
            case CUSTOM:
                if (Objects.isNull(flowAnalysisDTO.getStartTime()) || Objects.isNull(flowAnalysisDTO.getEndTime())) {
                    throw new LuckException("起始时间与结束时间不能为空");
                }
                flowAnalysisDTO.setStartTime(DateUtil.beginOfDay(flowAnalysisDTO.getStartTime()));
                flowAnalysisDTO.setEndTime(DateUtil.endOfDay(flowAnalysisDTO.getEndTime()));
                break;
            case WEEK:
                if (Objects.isNull(flowAnalysisDTO.getStartTime())) {
                    throw new LuckException("起始时间不能为空");
                }
                flowAnalysisDTO.setStartTime(DateUtil.beginOfWeek(flowAnalysisDTO.getStartTime()));
                flowAnalysisDTO.setEndTime(DateUtil.endOfWeek(flowAnalysisDTO.getStartTime()));
                break;
            case MONTH:
                if (Objects.isNull(flowAnalysisDTO.getStartTime())) {
                    throw new LuckException("起始时间不能为空");
                }
                flowAnalysisDTO.setStartTime(DateUtil.beginOfMonth(flowAnalysisDTO.getStartTime()));
                flowAnalysisDTO.setEndTime(DateUtil.endOfMonth(flowAnalysisDTO.getStartTime()));
                break;
            case NEARLY_WEEK:
                now = DateUtil.endOfDay(now);
                flowAnalysisDTO.setEndTime(DateUtils.getBeforeDay(now, -1));
                flowAnalysisDTO.setStartTime(DateUtils.getBeforeDay(now, -7));
                break;
            case NEARLY_MONTH:
                now = DateUtil.endOfDay(now);
                flowAnalysisDTO.setEndTime(DateUtils.getBeforeDay(now, -1));
                flowAnalysisDTO.setStartTime(DateUtils.getBeforeDay(now, -30));
                break;
            default:
                flowAnalysisDTO.setStartTime(DateUtil.beginOfDay(now));
                flowAnalysisDTO.setEndTime(DateUtil.endOfDay(now));
                break;
        }
    }
}
