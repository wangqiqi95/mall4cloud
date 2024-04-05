package com.mall4j.cloud.biz.controller.wx.chat;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.biz.dto.chat.KeywordHitDTO;
import com.mall4j.cloud.biz.dto.cp.MaterialBrowsePageDTO;
import com.mall4j.cloud.biz.service.chat.KeywordHitService;
import com.mall4j.cloud.biz.vo.chat.KeywordHitVO;
import com.mall4j.cloud.biz.vo.chat.SoldKeywordHitVO;
import com.mall4j.cloud.biz.vo.cp.CpMaterialBrowseRecordVO;
import com.mall4j.cloud.biz.vo.cp.CpMaterialBrowseSoldVO;
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
 * 命中关键词
 */
@Slf4j
@RestController("keywordHitController")
@RequestMapping("/p/keywordHit")
@Api(tags = "命中关键词")
public class KeywordHitController {

    @Autowired
    private KeywordHitService hitService;
    @Autowired
    private MapperFacade mapperFacade;


    @GetMapping("/list")
    @ApiOperation(value = "获取命中关键词列表", notes = "分页获取命中关键词列表")
    public ServerResponseEntity<PageVO<KeywordHitVO>> page(@Valid PageDTO pageDTO, KeywordHitDTO dto) {
        PageVO<KeywordHitVO> pageVO = hitService.page(pageDTO, dto);
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping("/keyword_top")
    @ApiOperation(value = "获取命中关键词top", notes = "获取命中关键词top")
    public ServerResponseEntity<List<KeywordHitVO>> getTop(KeywordHitDTO dto) {
        List<KeywordHitVO> hitVOS = hitService.getTop(dto);
        return ServerResponseEntity.success(hitVOS);
    }

    @GetMapping("/soldExcel")
    @ApiOperation(value = "获取命中关键词列表-导出", notes = "获取命中关键词列表-导出")
    public void soldExcel(KeywordHitDTO dto, HttpServletResponse response) {
        List<KeywordHitVO> list=hitService.soldExcel(dto);
//        if(CollUtil.isEmpty(list)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="keyword-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<SoldKeywordHitVO> dataList= mapperFacade.mapAsList(list,SoldKeywordHitVO.class);
            ExcelUtil.exportExcel(SoldKeywordHitVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

}
