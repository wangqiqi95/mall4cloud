package com.mall4j.cloud.biz.controller.wx.live.base;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.SellerInfo;
import com.mall4j.cloud.biz.service.live.LiveStoreService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController("liveSellerController")
@RequestMapping("/mp/livestore/seller")
@Api(tags = "视频号商户信息管理")
public class LiveSellerController {
    @Resource
    private LiveStoreService liveStoreService;
    @GetMapping("info")
    @ApiOperation(value = "商户信息", notes = "商户信息")
    public ServerResponseEntity<SellerInfo> info() {
        SellerInfo sellerInfo = liveStoreService.getSellerInfo();
        return ServerResponseEntity.success(sellerInfo);
    }

    @PostMapping("update")
    @ApiOperation(value = "更新商户信息", notes = "更新商户信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SellerInfo sellerInfo) {
        BaseResponse baseResponse = liveStoreService.updateSellerInfo(sellerInfo);
        return ServerResponseEntity.success();
    }
}
