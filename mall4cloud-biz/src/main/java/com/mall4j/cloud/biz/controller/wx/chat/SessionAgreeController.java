package com.mall4j.cloud.biz.controller.wx.chat;


import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.biz.dto.chat.KeywordHitDTO;
import com.mall4j.cloud.biz.dto.chat.SessionAgreeDTO;
import com.mall4j.cloud.biz.service.chat.KeywordHitService;
import com.mall4j.cloud.biz.service.chat.SessionAgreeService;
import com.mall4j.cloud.biz.vo.chat.KeywordHitVO;
import com.mall4j.cloud.biz.vo.chat.SessionAgreeVO;
import com.mall4j.cloud.biz.vo.chat.SoldKeywordHitVO;
import com.mall4j.cloud.biz.vo.chat.SoldSessionAgreeVO;
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
 * 会话同意情况
 */
@Slf4j
@RestController("sessionAgreeController")
@RequestMapping("/p/agree")
@Api(tags = "会话同意情况")
public class SessionAgreeController {

    @Autowired
    private SessionAgreeService agreeService;
    @Autowired
    private MapperFacade mapperFacade;


    @GetMapping("/list")
    @ApiOperation(value = "获取会话同意列表", notes = "分页获取会话同意列表")
    public ServerResponseEntity<PageVO<SessionAgreeVO>> page(@Valid PageDTO pageDTO, SessionAgreeDTO dto) {
        PageVO<SessionAgreeVO> pageVO = agreeService.page(pageDTO, dto);
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping("/soldExcel")
    @ApiOperation(value = "获取会话同意列表-导出", notes = "获取会话同意列表-导出")
    public void soldExcel(SessionAgreeDTO dto, HttpServletResponse response) {
        List<SessionAgreeVO> list=agreeService.soldExcel(dto);
//        if(CollUtil.isEmpty(list)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="keyword-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<SoldSessionAgreeVO> dataList= mapperFacade.mapAsList(list,SoldSessionAgreeVO.class);
            ExcelUtil.exportExcel(SoldSessionAgreeVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("/agreeProportion")
    @ApiOperation(value = "获取会话同意占比", notes = "获取会话同意占比")
    public ServerResponseEntity<List<SessionAgreeVO>> agreeProportion(SessionAgreeDTO dto) {
        List<SessionAgreeVO> VO = agreeService.agreeProportion(dto);
        return ServerResponseEntity.success(VO);
    }

    @GetMapping("/agreeMonCount")
    @ApiOperation(value = "获取会话同意月份趋势", notes = "获取会话同意月份趋势")
    public ServerResponseEntity<List<SessionAgreeVO>> agreeMonCount(SessionAgreeDTO dto) {
        List<SessionAgreeVO> hitVOS = agreeService.agreeMonCount(dto);
        return ServerResponseEntity.success(hitVOS);
    }

    @GetMapping("/agreeSum")
    @ApiOperation(value = "获取会话同意总量", notes = "获取会话同意总量")
    public ServerResponseEntity<SessionAgreeVO> agreeSum(SessionAgreeDTO dto) {
        SessionAgreeVO hitVOS = agreeService.agreeSum(dto);
        return ServerResponseEntity.success(hitVOS);
    }

}
