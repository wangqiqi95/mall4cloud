package com.mall4j.cloud.group.controller.ad.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AddPopUpAdDTO;
import com.mall4j.cloud.group.dto.QueryPopUpAdPageDTO;
import com.mall4j.cloud.group.dto.UpdatePopUpAdDTO;
import com.mall4j.cloud.group.service.PopUpAdService;
import com.mall4j.cloud.group.vo.PopUpAdDataPageVO;
import com.mall4j.cloud.group.vo.PopUpAdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/p/pop/up/ad")
@Api(tags = "管理后台弹窗广告相关接口")
@Slf4j
public class PlatformPopUpAdController {


    @Autowired
    private PopUpAdService popUpAdService;


    @PostMapping("/add")
    @ApiOperation("新增弹窗广告")
    public ServerResponseEntity add(@Valid @RequestBody  AddPopUpAdDTO addPopUpAdDTO){
        return popUpAdService.createPopUpAd(addPopUpAdDTO);
    }

    @PostMapping("/update")
    @ApiOperation("编辑弹窗广告")
    public ServerResponseEntity update(@Valid @RequestBody UpdatePopUpAdDTO updatePopUpAdDTO){
        return popUpAdService.updatePopUpAd(updatePopUpAdDTO);
    }


    @DeleteMapping("/{id}")
    @ApiOperation("删除弹窗广告")
    public ServerResponseEntity remove(@PathVariable Long id){
        return popUpAdService.remove(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("禁用弹窗广告")
    public ServerResponseEntity enableOrDisableAd(@PathVariable Long id){
        return popUpAdService.enableOrDisableAd(id);
    }

    @PostMapping("/page")
    @ApiOperation("弹窗广告管理后台分页查询接口")
    public ServerResponseEntity<PageVO<PopUpAdDataPageVO>> getPage(@RequestBody QueryPopUpAdPageDTO pageDTO){
        return popUpAdService.getPage(pageDTO);
    }

    @GetMapping("/detail")
    @ApiOperation("弹窗广告管理后台详情查询接口")
    ServerResponseEntity<PopUpAdVO> getPopUpAdById(Long adId){
        return popUpAdService.getPopUpAdById(adId);
    }

}
