package com.mall4j.cloud.platform.controller.app;

import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.dto.StoreRenovationDTO;
import com.mall4j.cloud.platform.model.StoreRenovation;
import com.mall4j.cloud.platform.service.StoreRenovationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 店铺装修信息
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@RestController("appStoreRenovationController")
@RequestMapping("/store/renovation")
@Api(tags = "app-店铺装修信息")
public class StoreRenovationController {

    @Autowired
    private StoreRenovationService storeRenovationService;

    @Autowired
    private MapperFacade mapperFacade;

    public static final String STORE_RENOVATION_PREVIEW = "STORE_RENOVATION_PREVIEW";


    @GetMapping("/ua/info")
    @ApiOperation(value = "获取店铺装修信息", notes = "根据renovationId获取店铺装修信息")
    public ServerResponseEntity<StoreRenovation> getByRenovationId(@RequestParam Long renovationId) {
        return ServerResponseEntity.success(storeRenovationService.getByRenovationId(renovationId));
    }

    @GetMapping("/ua/home")
    @ApiOperation(value = "获取首页", notes = "获取门店首页配置")
    public ServerResponseEntity<StoreRenovation> getHomepage(@RequestParam Long storeId) {
        return ServerResponseEntity.success(storeRenovationService.getHomepage(storeId));
    }

    @GetMapping("/ua/getStoreRPreview")
    @ApiOperation(value = "获取店铺装修预览信息", notes = "获取店铺装修预览信息")
    public ServerResponseEntity<String> getStoreRPreview() {
        String content=RedisUtil.get(STORE_RENOVATION_PREVIEW);
        return ServerResponseEntity.success(content);
    }

    @PostMapping
    @ApiOperation(value = "保存店铺装修信息", notes = "保存店铺装修信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody StoreRenovationDTO storeRenovationDTO) {
        StoreRenovation storeRenovation = mapperFacade.map(storeRenovationDTO, StoreRenovation.class);
        storeRenovationService.save(storeRenovation);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新店铺装修信息", notes = "更新店铺装修信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody StoreRenovationDTO storeRenovationDTO) {
        StoreRenovation storeRenovation = mapperFacade.map(storeRenovationDTO, StoreRenovation.class);
        storeRenovationService.update(storeRenovation);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除店铺装修信息", notes = "根据店铺装修信息id删除店铺装修信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long renovationId) {
        storeRenovationService.deleteById(renovationId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/ua/renovation")
    @ApiOperation(value = "获取装修页配置", notes = "获取装修页配置")
    public ServerResponseEntity<StoreRenovation> getRenovation(@RequestParam Long storeId,
                                                             @RequestParam(value = "homeStatus") Integer homeStatus) {
        return ServerResponseEntity.success(storeRenovationService.getRenovation(homeStatus));
    }

    @PostMapping("/saveStoreRenovationItem")
    @ApiOperation(value = "新增店铺装修详情记录", notes = "新增店铺装修详情记录")
    public ServerResponseEntity<String> saveStoreRenovationItem(@Valid @RequestBody Long renovationId) {
        return storeRenovationService.saveStoreRenovationItem(renovationId);
    }

}
