package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.feign.SessionFileFeignClient;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.api.biz.vo.StaffSessionVO;
import com.mall4j.cloud.biz.service.chat.SessionSearchService;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import com.mall4j.cloud.biz.wx.wx.util.MsgAuditAccessToken;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class SessionFileFeignController implements SessionFileFeignClient {

    @Autowired
    private SessionSearchService searchService;
    @Autowired
    private ConfigService configService;

    @Override
    public ServerResponseEntity<PageVO<SessionFileVO>> getSessionFile(SessionFileDTO dto) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageSize(dto.getPageSize());
        pageDTO.setPageNum(dto.getPageNum());
        PageVO<SessionFileVO> filePage = searchService.getSessionFile(pageDTO, dto);
        return ServerResponseEntity.success(filePage);
    }

    @Override
    public ServerResponseEntity<PageVO<SessionFileVO>> sessionFileSearch(SessionFileDTO dto) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageSize(dto.getPageSize());
        pageDTO.setPageNum(dto.getPageNum());
        PageVO<SessionFileVO> filePage = searchService.page(pageDTO,dto);
        return ServerResponseEntity.success(filePage);
    }

    @Override
    public ServerResponseEntity<StaffSessionVO> getStaffCount(SessionFileDTO dto) {
        StaffSessionVO staffSessionVO = searchService.getStaffCount(dto);
        return ServerResponseEntity.success(staffSessionVO);
    }

    @Override
    public ServerResponseEntity<List<StaffSessionVO>> getUserAndStaffCount(SessionFileDTO dto) {
        List<StaffSessionVO> staffSessionVOList = searchService.getUserAndStaffCount(dto);
        return ServerResponseEntity.success(staffSessionVOList);
    }

    @Override
    public ServerResponseEntity<String> getMSGAUDITAccessToken() {
        String token=new MsgAuditAccessToken().getMSGAUDITAccessToken(configService);
        return ServerResponseEntity.success(token);
    }

    @Override
    public ServerResponseEntity<Date> getLastTime(SessionFileDTO dto) {
        Date lastTime = searchService.getLastTime(dto);
        return ServerResponseEntity.success(lastTime);
    }
    @Override
    public ServerResponseEntity<Date> getRoomLastTime(SessionFileDTO dto) {
        Date lastTime = searchService.getRoomLastTime(dto);
        return ServerResponseEntity.success(lastTime);
    }

}
