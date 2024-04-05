package com.mall4j.cloud.biz.controller.wx.chat;


import com.mall4j.cloud.biz.dto.chat.KeywordDTO;
import com.mall4j.cloud.biz.model.chat.Keyword;
import com.mall4j.cloud.biz.model.chat.SessionTimeOut;
import com.mall4j.cloud.biz.service.chat.KeywordService;
import com.mall4j.cloud.biz.vo.chat.KeywordVO;
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
import java.util.Date;

/**
 * 会话关键词
 */
@Slf4j
@RestController("keywordController")
@RequestMapping("/p/keyword")
@Api(tags = "会话关键词")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;
    @Autowired
    private MapperFacade mapperFacade;


    @GetMapping("/list")
    @ApiOperation(value = "获取关键词列表", notes = "分页获取获取关键词列表")
    public ServerResponseEntity<PageVO<KeywordVO>> page(@Valid PageDTO pageDTO, KeywordDTO keywordDTO) {
        PageVO<KeywordVO> pageVO = keywordService.page(pageDTO, keywordDTO);
        return ServerResponseEntity.success(pageVO);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加关键词")
    public ServerResponseEntity<Void> saveKeyword(@RequestBody KeywordDTO dto){
        try{
            keywordService.saveKeyword(dto);
        }catch (Exception e){
            log.error("保存失败==="+e);
            return ServerResponseEntity.showFailMsg("保存失败");
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改关键词")
    public ServerResponseEntity<Void> updateKeyword(@RequestBody KeywordDTO dto){
        try {
            keywordService.update(dto);
        }catch (Exception e){
            log.error("修改失败==="+e);
            return ServerResponseEntity.showFailMsg("修改失败");
        }
        return ServerResponseEntity.success();
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除会话关键词", notes = "删除会话关键词")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        try{
            keywordService.deleteById(id);
        }catch (Exception e){
            log.error("删除失败==="+e);
            return ServerResponseEntity.showFailMsg("删除失败");
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("/getDetail")
    @ApiOperation(value = "获取关键词详情", notes = "获取关键词详情")
    public ServerResponseEntity<KeywordVO> getDetail(@RequestParam Long id) {
        KeywordVO pageVO = keywordService.getDetail(id);
        return ServerResponseEntity.success(pageVO);
    }

}
