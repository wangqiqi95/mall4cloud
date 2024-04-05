package com.mall4j.cloud.biz.controller.wx.chat;


import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.biz.dto.chat.TimeOutLogDTO;
import com.mall4j.cloud.biz.service.chat.SessionTimeOutLogService;
import com.mall4j.cloud.biz.vo.chat.SoldTimeOutLogVO;
import com.mall4j.cloud.biz.vo.chat.TimeOutLogVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 超时回复统计
 */
@Slf4j
@RestController("sessionTimeOutLogController")
@RequestMapping("/p/timeOutLog")
@Api(tags = "超时回复统计")
public class SessionTimeOutLogController {

    @Autowired
    private SessionTimeOutLogService logService;
    @Autowired
    private MapperFacade mapperFacade;


    @GetMapping("/list")
    @ApiOperation(value = "获取会话超时回复列表", notes = "分页获取会话超时回复列表")
    public ServerResponseEntity<PageVO<TimeOutLogVO>> page(@Valid PageDTO pageDTO, TimeOutLogDTO dto) {
        PageVO<TimeOutLogVO> pageVO = logService.page(pageDTO, dto);
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping("/outLogCount")
    @ApiOperation(value = "获取超时回复统计", notes = "获取超时回复统计")
    public ServerResponseEntity<TimeOutLogVO> outLogCount(TimeOutLogDTO dto) {
        TimeOutLogVO VO = logService.outLogCount(dto);
        return ServerResponseEntity.success(VO);
    }

    @GetMapping("/outLogChart")
    @ApiOperation(value = "获取会话超时图表", notes = "获取会话超时图表")
    public ServerResponseEntity<List<TimeOutLogVO>> outLogChart(TimeOutLogDTO dto) {
        List<TimeOutLogVO> hitVOS = logService.outLogChart(dto);
        return ServerResponseEntity.success(hitVOS);
    }

    @GetMapping("/soldExcel")
    @ApiOperation(value = "获取会话超时回复列表-导出", notes = "获取会话超时回复列表-导出")
    public void soldExcel(TimeOutLogDTO dto, HttpServletResponse response) {
        List<TimeOutLogVO> list=logService.soldExcel(dto);
//        if(CollUtil.isEmpty(list)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="timeOutLog-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<SoldTimeOutLogVO> dataList= mapperFacade.mapAsList(list, SoldTimeOutLogVO.class);
            ExcelUtil.exportExcel(SoldTimeOutLogVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("/sendMessage")
    public void sendMessage(){
        logService.sendMessage();
    }
}
