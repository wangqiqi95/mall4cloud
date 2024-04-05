package com.mall4j.cloud.biz.controller.wx.live.base;

import com.mall4j.cloud.biz.dto.LiveCategoryDTO;
import com.mall4j.cloud.biz.service.WechatLiveCategoryService;
import com.mall4j.cloud.biz.vo.LiveCategoryVO;
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
@RestController("liveCategoryController")
@RequestMapping("/mp/livestore/category")
@Api(tags = "视频号类目管理")
public class LiveCategoryController {

    @Autowired
    private WechatLiveCategoryService categoryService;

    @GetMapping("/baseList")
    @ApiOperation(value = "查询基本类目列表", notes = "查询基本类目列表")
    public ServerResponseEntity<List<LiveCategoryVO>> baseList(@RequestParam(value = "query", required = false) String query, @RequestParam(value = "all", required = false) String all) {
        List<LiveCategoryVO> list = categoryService.baseList(query, all);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/applyList")
    @ApiOperation(value = "查询申请通过的类目列表", notes = "查询申请通过的类目列表")
    public ServerResponseEntity<List<LiveCategoryVO>> applyList(@RequestParam(value = "query", required = false) String query, @RequestParam(value = "all", required = false) String all) {
        List<LiveCategoryVO> list = categoryService.applyList(query);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询类目列表", notes = "查询类目列表")
    public ServerResponseEntity<List<LiveCategoryVO>> list() {
        List<LiveCategoryVO> list = categoryService.list();
        return ServerResponseEntity.success(list);
    }

    @PutMapping
    @ApiOperation(value = "添加类目", notes = "添加类目")
    public ServerResponseEntity<Void> addProduct(@Valid @RequestBody LiveCategoryDTO liveCategoryDTO) {
        categoryService.add(liveCategoryDTO);
        return ServerResponseEntity.success();
    }



}
