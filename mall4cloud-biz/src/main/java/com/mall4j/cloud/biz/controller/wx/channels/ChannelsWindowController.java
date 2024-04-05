package com.mall4j.cloud.biz.controller.wx.channels;

import com.mall4j.cloud.biz.service.channels.ChannelsWindowService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 视频号4.0 橱窗
 * @date 2023/3/9
 */
@RestController("multishopChannelsWindowController")
@RequestMapping("/ua/channels/window")
@Api(tags = "视频号4.0橱窗")
public class ChannelsWindowController {
    @Autowired
    private ChannelsWindowService channelsWindowService;

    /**
     * 商品上架橱窗
     * @param spuId 内部商品ID
     */
    @PostMapping("/product/add")
    @ApiOperation(value = "视频号4.0商品上架橱窗", notes = "视频号4.0商品上架橱窗")
    public ServerResponseEntity<Void> addProduct(@RequestParam Long spuId){
        channelsWindowService.addProduct(spuId, false);
        return ServerResponseEntity.success();
    }

    @PostMapping("/product/add/batch")
    @ApiOperation(value = "视频号4.0批量商品上架橱窗", notes = "视频号4.0批量商品上架橱窗")
    public ServerResponseEntity<Void> addProductBatch(@RequestBody List<Long> spuIds){
        for (Long spuId : spuIds) {
            channelsWindowService.addProduct(spuId, false);
        }
        return ServerResponseEntity.success();
    }

    /**
     * 商品下架
     * @param spuId 内部商品ID
     */
    @PostMapping("/product/off")
    @ApiOperation(value = "视频号4.0商品下架橱窗", notes = "视频号4.0商品下架橱窗")
    public ServerResponseEntity<Void> offProduct(@RequestParam Long spuId){
        channelsWindowService.offProduct(spuId);
        return ServerResponseEntity.success();
    }

    /**
     * 批量商品下架
     * @param spuIds 内部商品ID
     */
    @PostMapping("/product/off/batch")
    @ApiOperation(value = "视频号4.0批量商品下架橱窗", notes = "视频号4.0商批量品下架橱窗")
    public ServerResponseEntity<Void> offProductBatch(@RequestBody List<Long> spuIds){
        for (Long spuId : spuIds) {
            channelsWindowService.offProduct(spuId);
        }
        return ServerResponseEntity.success();
    }
}
