package com.mall4j.cloud.product.controller.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.service.SpuTagService;
import com.mall4j.cloud.product.vo.SpuTagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 商品分组表
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
@RestController("appSpuTagController")
@RequestMapping("/ua/spu_tag")
@Api(tags = "app-商品分组")
public class SpuTagController {

    @Autowired
    private SpuTagService spuTagService;

    @GetMapping("/list")
    @ApiOperation(value = "获取商品分组列表", notes = "获取商品分组列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "shopId", value = "店铺id", dataType = "Long")
    })
    public ServerResponseEntity<List<SpuTagVO>> list(@RequestParam(value = "shopId", defaultValue = "0") Long shopId) {
        List<SpuTagVO> spuTagList = spuTagService.listByShopId(shopId);
        return ServerResponseEntity.success(spuTagList);
    }
}
