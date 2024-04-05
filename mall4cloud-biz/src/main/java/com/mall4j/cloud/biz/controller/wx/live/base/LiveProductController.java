package com.mall4j.cloud.biz.controller.wx.live.base;

import com.mall4j.cloud.biz.dto.LiveProductDTO;
import com.mall4j.cloud.biz.service.WechatLiveProductService;
import com.mall4j.cloud.biz.vo.LiveProductVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author lt
 * @Date 2022-01-17
 */
@RestController("liveProductController")
@RequestMapping("/mp/livestore/product")
@Api(tags = "视频号商品管理")
public class LiveProductController {

    @Autowired
    private WechatLiveProductService wechatLiveProductService;

    @GetMapping("/list")
    @ApiOperation(value = "分页查询商品列表", notes = "分页查询商品列表")
    public ServerResponseEntity<PageVO<LiveProductVO>> list(@Valid PageDTO page, @RequestParam(value = "onsale", required = false) Integer onsale) {
        PageVO<LiveProductVO> pageVO = wechatLiveProductService.list(page, onsale);
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping()
    @ApiOperation(value = "查询单个商品", notes = "查询单个商品")
    public ServerResponseEntity<LiveProductDTO> one(@RequestParam(value = "productId") String productId) {
        LiveProductDTO liveProductVO = wechatLiveProductService.one(productId);
        return ServerResponseEntity.success(liveProductVO);
    }


    @PutMapping
    @ApiOperation(value = "添加商品", notes = "添加商品")
    public ServerResponseEntity<Void> addProduct(@Valid @RequestBody LiveProductDTO liveProductDTO) {
        wechatLiveProductService.add(liveProductDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping
    @ApiOperation(value = "更新商品", notes = "更新商品")
    public ServerResponseEntity<Void> updateProduct(@Valid @RequestBody LiveProductDTO liveProductDTO) {
        wechatLiveProductService.update(liveProductDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/onsale")
    @ApiOperation(value = "上架商品", notes = "上架商品")
    public ServerResponseEntity<Void> onsale(@Valid @RequestBody LiveProductDTO liveProductDTO) {
        wechatLiveProductService.onsale(liveProductDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/offsale")
    @ApiOperation(value = "下架商品", notes = "下架商品")
    public ServerResponseEntity<Void> offsale(@Valid @RequestBody LiveProductDTO liveProductDTO) {
        wechatLiveProductService.offsale(liveProductDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除商品", notes = "删除商品")
    public ServerResponseEntity<Void> delete(@Valid LiveProductDTO liveProductDTO) {
        wechatLiveProductService.delete(liveProductDTO);
        return ServerResponseEntity.success();
    }

}
