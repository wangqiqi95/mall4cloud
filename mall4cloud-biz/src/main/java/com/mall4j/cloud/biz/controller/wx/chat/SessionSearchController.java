package com.mall4j.cloud.biz.controller.wx.chat;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.api.biz.vo.SoldSessionFileVO;
import com.mall4j.cloud.api.biz.vo.SoldStaffSessionVO;
import com.mall4j.cloud.api.biz.vo.StaffSessionVO;
import com.mall4j.cloud.biz.dto.chat.SessionAgreeDTO;
import com.mall4j.cloud.biz.service.chat.SessionSearchService;
import com.mall4j.cloud.biz.vo.chat.SessionAgreeVO;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController("sessionSearchController")
@RequestMapping("/p/search")
@Api(tags = "会话存档检索")
@Slf4j
public class SessionSearchController {

    @Autowired
    SessionSearchService sessionSearchService;
    @Autowired
    private MapperFacade mapperFacade;
    @GetMapping("/list")
    @ApiOperation(value = "获取会话存档列表", notes = "分页获取会话存档列表")
    public ServerResponseEntity<PageVO<SessionFileVO>> page(@Valid PageDTO pageDTO, SessionFileDTO fileDTO) {
        PageVO<SessionFileVO> filePage = sessionSearchService.page(pageDTO, fileDTO);
        return ServerResponseEntity.success(filePage);
    }

    @GetMapping("getSessionFile")
    @ApiOperation(value = "查询会话存档上下文", notes = "查询会话存档上下文")
    public ServerResponseEntity<PageVO<SessionFileVO>> getSessionFile(@Valid PageDTO pageDTO, SessionFileDTO fileDTO) {
        PageVO<SessionFileVO> filePage = sessionSearchService.getSessionFile(pageDTO, fileDTO);
        return ServerResponseEntity.success(filePage);
    }

    @GetMapping("getSessionCount")
    @ApiOperation(value = "查询员工和客户会话数量", notes = "查询员工和客户会话数量")
    public ServerResponseEntity<StaffSessionVO> getSessionCount(SessionFileDTO fileDTO) {
        StaffSessionVO filePage = sessionSearchService.getSessionCount(fileDTO);
        return ServerResponseEntity.success(filePage);
    }

    @GetMapping("/getUserSession")
    @ApiOperation(value = "查询员工收发消息", notes = "查询员工收发消息")
    public ServerResponseEntity<PageVO<StaffSessionVO>> getUserSession(@Valid PageDTO pageDTO, SessionFileDTO fileDTO) {
        PageVO<StaffSessionVO> filePage = sessionSearchService.getUserSession(pageDTO, fileDTO);
        return ServerResponseEntity.success(filePage);
    }

    @GetMapping("/soldExcelUserSession")
    @ApiOperation(value = "查询员工收发消息-导出", notes = "查询员工收发消息-导出")
    public void soldExcel(SessionFileDTO dto, HttpServletResponse response) {
        List<StaffSessionVO> list=sessionSearchService.getUserSessionList(dto);
        String fileName="userSession-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<SoldStaffSessionVO> dataList= mapperFacade.mapAsList(list,SoldStaffSessionVO.class);
            ExcelUtil.exportExcel(SoldStaffSessionVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("/soldSessionFileExcel")
    @ApiOperation(value = "获取会话存档列表-导出", notes = "获取会话存档列表-导出")
    public void soldSessionFileExcel(SessionFileDTO dto, HttpServletResponse response) {
        List<SoldSessionFileVO> dataList=sessionSearchService.soldSessionFileExcel(dto);
        try {
            String fileName="会话记录导出"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
            ExcelUtil.exportExcel(SoldSessionFileVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @PostMapping("/getSessionFileByMsgId")
    @ApiOperation(value = "根据msgId查询消息", notes = "根据msgId查询消息")
    public ServerResponseEntity<List<SessionFileVO>> getSessionFileByMsgId(@RequestBody SessionFileDTO fileDTO) {
        List<SessionFileVO> files = sessionSearchService.getSessionFileByMsgId(fileDTO.getMsgIds());
        return ServerResponseEntity.success(files);
    }
    @GetMapping("getStaffCount")
    @ApiOperation(value = "查询员工和客户会话数量", notes = "查询员工和客户会话数量")
    public ServerResponseEntity<StaffSessionVO> getStaffCount(SessionFileDTO fileDTO) {
        StaffSessionVO filePage = sessionSearchService.getStaffCount(fileDTO);
        return ServerResponseEntity.success(filePage);
    }
    @GetMapping("getLastTime")
    @ApiOperation(value = "查询员工和客户最近联系时间", notes = "查询员工和客户最近联系时间")
    public ServerResponseEntity<Date> getLastTime(SessionFileDTO fileDTO) {
        Date filePage = sessionSearchService.getLastTime(fileDTO);
        return ServerResponseEntity.success(filePage);
    }

    @GetMapping("getRoomLastTime")
    @ApiOperation(value = "查询群聊最近联系时间", notes = "查询群聊最近联系时间")
    public ServerResponseEntity<Date> getRoomLastTime(SessionFileDTO fileDTO) {
        Date filePage = sessionSearchService.getRoomLastTime(fileDTO);
        return ServerResponseEntity.success(filePage);
    }
}
