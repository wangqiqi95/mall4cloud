package com.mall4j.cloud.biz.controller.wx.chat;

import com.mall4j.cloud.biz.dto.chat.SessionTimeOutDTO;
import com.mall4j.cloud.biz.service.chat.SessionTimeOutLogService;
import com.mall4j.cloud.biz.service.chat.SessionTimeOutService;
import com.mall4j.cloud.biz.vo.chat.SessionTimeOutVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController("sessionTimeOutController")
@RequestMapping("/p/timeout")
@Api(tags = "会话超时")
public class SessionTimeOutController {

    @Autowired
    private SessionTimeOutService timeOutService;
    @Autowired
    private SessionTimeOutLogService sessionTimeOutLogService;
    @Autowired
    private MapperFacade mapperFacade;
    @GetMapping("/list")
    @ApiOperation(value = "获取会话超时规则列表", notes = "分页获取会话超时规则列表")
    public ServerResponseEntity<PageVO<SessionTimeOutVO>> page(@Valid PageDTO pageDTO, SessionTimeOutDTO timeOutDTO) {
        PageVO<SessionTimeOutVO> pageVO = timeOutService.page(pageDTO, timeOutDTO);
        return ServerResponseEntity.success(pageVO);
    }

    @PostMapping("add")
    @ApiOperation(value = "保存会话超时规则", notes = "保存会话超时规则")
    public ServerResponseEntity<Void> save(@RequestBody SessionTimeOutDTO timeOutDTO){
        try{
            timeOutService.save(timeOutDTO);
        }catch (Exception e){
            log.error("保存失败==="+e);
            return ServerResponseEntity.showFailMsg("保存失败");
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新会话超时规则", notes = "更新会话超时规则")
    public ServerResponseEntity<Void> update(@RequestBody SessionTimeOutDTO timeOutDTO){
        try{
            timeOutService.update(timeOutDTO);
        }catch (Exception e){
            log.error("更新失败==="+e);
            return ServerResponseEntity.showFailMsg("更新失败");
        }
        return ServerResponseEntity.success();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除会话超时规则", notes = "删除会话超时规则")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        try{
            if(Objects.nonNull(sessionTimeOutLogService.getByTimeOutId(id.toString()))){
                throw new LuckException("使用中，不可删除");
            }
            timeOutService.deleteById(id);
        }catch (Exception e){
            log.error("删除失败==="+e);
            return ServerResponseEntity.showFailMsg("删除失败,"+e.getMessage());
        }
        return ServerResponseEntity.success();
    }
    @GetMapping("/getDetail")
    @ApiOperation(value = "获取超时详情", notes = "获取超时详情")
    public ServerResponseEntity<SessionTimeOutVO> getDetail(@RequestParam Long id) {
        SessionTimeOutVO pageVO = timeOutService.getById(id);
        return ServerResponseEntity.success(pageVO);
    }
}
