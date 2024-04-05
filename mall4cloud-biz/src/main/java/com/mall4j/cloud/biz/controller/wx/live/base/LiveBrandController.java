package com.mall4j.cloud.biz.controller.wx.live.base;

import com.mall4j.cloud.biz.dto.LiveBrandDTO;
import com.mall4j.cloud.biz.service.WechatLiveBrandService;
import com.mall4j.cloud.biz.vo.LiveBrandVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author lt
 * @Date 2022-01-17
 */
@RestController("liveBrandController")
@RequestMapping("/mp/livestore/brand")
@Api(tags = "视频号品牌管理")
public class LiveBrandController {

    @Autowired
    private WechatLiveBrandService liveBrandService;

    @GetMapping("/auditedList")
    @ApiOperation(value = "查询已审核的品牌列表", notes = "查询品牌列表")
    public ServerResponseEntity<List<LiveBrandVO>> auditedList() {
        List<LiveBrandVO> list = liveBrandService.auditedList();
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询品牌列表", notes = "查询品牌列表")
    public ServerResponseEntity<PageVO<LiveBrandVO>> list(@Valid PageDTO pageDTO) {
        PageVO<LiveBrandVO> list = liveBrandService.list(pageDTO);
        return ServerResponseEntity.success(list);
    }

    @PutMapping
    @ApiOperation(value = "添加品牌", notes = "添加品牌")
    public ServerResponseEntity<Void> add(@Valid @RequestBody LiveBrandDTO liveBrandDTO) {
        liveBrandService.add(liveBrandDTO);
        return ServerResponseEntity.success();
    }



}
