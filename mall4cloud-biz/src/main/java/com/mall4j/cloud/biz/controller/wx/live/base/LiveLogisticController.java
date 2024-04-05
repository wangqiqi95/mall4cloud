package com.mall4j.cloud.biz.controller.wx.live.base;

import com.mall4j.cloud.biz.service.WechatLiveLogisticService;
import com.mall4j.cloud.biz.vo.LiveLogisticsVO;
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
@RestController("liveLogisticController")
@RequestMapping("/mp/livestore/logistic")
@Api(tags = "视频号物流映射管理")
public class LiveLogisticController {

    @Autowired
    private WechatLiveLogisticService wechatLiveLogisticService;

    @GetMapping("/baseList")
    @ApiOperation(value = "查询基本物流列表", notes = "查询基本物流列表")
    public ServerResponseEntity<List<LiveLogisticsVO>> baseList(@RequestParam(value = "query", required = false) String query) {
        List<LiveLogisticsVO> list = wechatLiveLogisticService.baseList(query);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询物流映射列表", notes = "查询物流映射列表")
    public ServerResponseEntity<PageVO<LiveLogisticsVO>> list(@Valid PageDTO page) {
        PageVO<LiveLogisticsVO> pageVO = wechatLiveLogisticService.list(page);
        return ServerResponseEntity.success(pageVO);
    }

    @PutMapping
    @ApiOperation(value = "添加物流映射", notes = "添加物流映射")
    public ServerResponseEntity<Void> addLogistic(@Valid @RequestBody LiveLogisticsVO liveLogisticsVO) {
        wechatLiveLogisticService.add(liveLogisticsVO);
        return null;
    }

    @DeleteMapping
    @ApiOperation(value = "删除物流映射", notes = "删除物流映射")
    public ServerResponseEntity<Void> deleteLogistic(@Valid LiveLogisticsVO liveLogisticsVO) {
        wechatLiveLogisticService.delete(liveLogisticsVO);
        return null;
    }
    
    
    @GetMapping("/detail")
    @ApiOperation(value = "获取物流映射详情", notes = "获取物流映射详情")
    public ServerResponseEntity<LiveLogisticsVO> detail(@RequestParam Long id) {
        return ServerResponseEntity.success(wechatLiveLogisticService.detail(id));
    }
    
    @PostMapping("/update")
    @ApiOperation(value = "更新物流映射", notes = "更新物流映射")
    public ServerResponseEntity<Void> update(@Valid @RequestBody LiveLogisticsVO liveLogisticsVO) {
        wechatLiveLogisticService.update(liveLogisticsVO);
        return ServerResponseEntity.success();
    }
}
