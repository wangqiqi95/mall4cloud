package com.mall4j.cloud.user.controller.platform.ayalyze;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationDetailVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationExportVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.user.dto.analyze.AnalyzeUserStaffCpRelationDTO;
import com.mall4j.cloud.user.manager.QiWeiFriendsChangeManager;
import com.mall4j.cloud.user.service.AnalyzeUserStaffCpRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
@RestController("AnalyzeUserStaffCpRelationController")
@RequestMapping("/p/cp/analayze")
@Api(tags = "客户数据分析")
@Slf4j
public class AnalyzeUserStaffCpRelationController {

    @Autowired
    private AnalyzeUserStaffCpRelationService analyzeUserStaffCpRelationService;
    @Autowired
    private QiWeiFriendsChangeManager changeManager;


    @GetMapping("/staff/code/analyzeUSRFPage")
    @ApiOperation(value = "渠道活码详情-好友统计分页列表", notes = "渠道活码详情-好友统计分页列表")
    public ServerResponseEntity<PageVO<AnalyzeUserStaffCpRelationListVO>> getAnalyzeUSRFPage(@Valid PageDTO pageDTO, AnalyzeUserStaffCpRelationDTO dto) {
        return ServerResponseEntity.success(analyzeUserStaffCpRelationService.getAnalyzeUSRFPage(pageDTO,dto));
    }

    @GetMapping("/staff/code/exportAnalyzeUSRF")
    @ApiOperation(value = "渠道活码详情-导出好友统计分页列表", notes = "渠道活码详情-导出好友统计分页列表")
    public void exportAnalyzeUSRF(AnalyzeUserStaffCpRelationDTO dto, HttpServletResponse response) {
        List<AnalyzeUserStaffCpRelationExportVO> dataList=analyzeUserStaffCpRelationService.exportAnalyzeUSRFPage(dto);
        String fileName="coderecord-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            ExcelUtil.exportExcel(AnalyzeUserStaffCpRelationExportVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("导出好友统计失败，请重试！");
        }
    }

    @GetMapping("/staff/code/analyzeNewUSRFList")
    @ApiOperation(value = "渠道活码详情-新增客户折线图", notes = "渠道活码详情-新增客户折线图")
    public ServerResponseEntity<AnalyzeUserStaffCpRelationDetailVO> getAnalyzeNewUSRFList(@Valid AnalyzeUserStaffCpRelationDTO dto) {
        return ServerResponseEntity.success(analyzeUserStaffCpRelationService.getAnalyzeNewUSRFList(dto));
    }

    @GetMapping("/addtest")
    @ApiOperation(value = "addtest", notes = "addtest")
    public ServerResponseEntity<List<AnalyzeUserStaffCpRelationVO>> getAnalyzeNewUSRFList(@RequestParam String json) {
        UserStaffCpRelationDTO dto= JSONObject.parseObject(json,UserStaffCpRelationDTO.class);
        changeManager.doMessage(dto);
        return ServerResponseEntity.success();
    }

//    @GetMapping("/staff/code/analyzeAllUSRFList")
//    @ApiOperation(value = "渠道活码详情-所有客户折线图", notes = "渠道活码详情-所有客户折线图")
//    public ServerResponseEntity<List<AnalyzeUserStaffCpRelationVO>> getAnalyzeAllUSRFList(@Valid AnalyzeUserStaffCpRelationDTO dto) {
//        return ServerResponseEntity.success(analyzeUserStaffCpRelationService.getAnalyzeAllUSRFList(dto));
//    }
//
//    @GetMapping("/staff/code/数据明细")
//    @ApiOperation(value = "渠道活码详情-所有客户折线图", notes = "渠道活码详情-所有客户折线图")
//    public ServerResponseEntity<PageVO<AnalyzeUserStaffCpRelationVO>> getAnalyzeUSRFDetailPage(@Valid PageDTO pageDTO,  AnalyzeUserStaffCpRelationDTO dto) {
//        return ServerResponseEntity.success(analyzeUserStaffCpRelationService.getAnalyzeUSRFDetailPage(pageDTO,dto));
//    }


}
