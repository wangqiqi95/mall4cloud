package com.mall4j.cloud.product.controller.platform;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.service.SpuExtensionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author lth
 * @Date 2021/7/19 15:32
 */
@RestController("platformSpuExtensionController")
@RequestMapping("/p/spuExtension")
@Api(tags = "platform-spuExtension信息")
public class SpuExtensionController {

    @Autowired
    private SpuExtensionService spuExtensionService;

    @PutMapping("/water_sold_num")
    @ApiOperation(value = "更新商品注水销量", notes = "更新商品注水销量")
    public ServerResponseEntity<Void> updateWaterSoldNum(@RequestParam(value = "waterSoldNum") Integer waterSoldNum, @RequestParam(value = "spuId") Long spuId) {
        if (Objects.isNull(waterSoldNum)) {
            waterSoldNum = 0;
        }
        spuExtensionService.updateWaterSoldNumBySpuId(spuId, waterSoldNum);
        return ServerResponseEntity.success();
    }
}
