package com.mall4j.cloud.biz.controller.notice;

import com.mall4j.cloud.biz.dto.cp.NoticeDTO;
import com.mall4j.cloud.biz.model.cp.Notice;
import com.mall4j.cloud.biz.service.cp.NoticeService;
import com.mall4j.cloud.biz.vo.cp.NoticeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController("noticeController")
@RequestMapping("/p/notice")
@Api(tags = "通知")
public class NoticeController {

    @Autowired
    NoticeService noticeService;
    @Autowired
    private MapperFacade mapperFacade;
    @GetMapping("/list")
    @ApiOperation(value = "获取通知列表", notes = "分页获取通知列表")
    public ServerResponseEntity<PageVO<NoticeVO>> page(@Valid PageDTO pageDTO, NoticeDTO dto) {
        PageVO<NoticeVO> pageVO = noticeService.page(pageDTO, dto);
        return ServerResponseEntity.success(pageVO);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加通知")
    public ServerResponseEntity<Void> saveNotice(@RequestBody NoticeDTO dto){
        try{
            Notice notice = mapperFacade.map(dto, Notice.class);
            noticeService.saveNotice(notice);
        }catch (Exception e){
            log.error("保存失败==="+e);
            return ServerResponseEntity.showFailMsg("保存失败");
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改通知")
    public ServerResponseEntity<Void> updateNotice(@RequestBody NoticeDTO dto){
        try {
            Notice notice = mapperFacade.map(dto, Notice.class);
            noticeService.update(notice);
        }catch (Exception e){
            log.error("修改失败==="+e);
            return ServerResponseEntity.showFailMsg("修改失败");
        }
        return ServerResponseEntity.success();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除通知", notes = "删除通知")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        try{
            noticeService.deleteById(id);
        }catch (Exception e){
            log.error("删除失败==="+e);
            return ServerResponseEntity.showFailMsg("删除失败");
        }
        return ServerResponseEntity.success();
    }
}
