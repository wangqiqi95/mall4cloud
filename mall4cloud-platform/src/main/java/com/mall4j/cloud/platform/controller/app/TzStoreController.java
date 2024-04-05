package com.mall4j.cloud.platform.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.dto.StorePageQueryDTO;
import com.mall4j.cloud.platform.dto.TzStoreDTO;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.service.TzStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Slf4j
@RestController("appTzStoreController")
@RequestMapping("/ua/store")
@Api(tags = "app-门店信息")
public class TzStoreController {

    @Autowired
    private TzStoreService tzStoreService;

    @Autowired
    private MapperFacade mapperFacade;

    public final static String REDISKEY = "scrm_platform:saveUrl:";

    @GetMapping("/page")
    @ApiOperation(value = "获取列表", notes = "分页获取列表")
    public ServerResponseEntity<PageVO<TzStore>> page(@Valid PageDTO pageDTO,StorePageQueryDTO request) {
        log.info("门店搜索入参1:{}", JSONObject.toJSONString(request));
        //过滤以下状态门店不展示
        if(CollectionUtil.isEmpty(request.getSlcNames())){
            List<String> slcNames=new ArrayList<>();
            slcNames.add("已关店");
            slcNames.add("已转店");
            slcNames.add("已删除");
            request.setSlcNames(slcNames);
        }
        log.info("门店搜索入参1:{}", JSONObject.toJSONString(request));
        PageVO<TzStore> tzStorePage = tzStoreService.page(pageDTO,request);
        return ServerResponseEntity.success(tzStorePage);
    }

    @GetMapping
    @ApiOperation(value = "获取", notes = "根据storeId获取")
    public ServerResponseEntity<TzStore> getByStoreId(@RequestParam Long storeId) {
        return ServerResponseEntity.success(tzStoreService.getByStoreId(storeId));
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TzStoreDTO tzStoreDTO) {
        TzStore tzStore = mapperFacade.map(tzStoreDTO, TzStore.class);
        tzStoreService.save(tzStore);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TzStoreDTO tzStoreDTO) {
//        TzStore tzStore = mapperFacade.map(tzStoreDTO, TzStore.class);
        tzStoreService.update(tzStoreDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除", notes = "根据id删除")
    public ServerResponseEntity<Void> delete(@RequestParam Long storeId) {
        tzStoreService.deleteById(storeId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/saveUrl")
    @ApiOperation(value = "保存触点链接返回key", notes = "保存触点链接返回key")
    public ServerResponseEntity<String> saveUrl(@RequestBody String url) {
        String key = SecureUtil.md5().digestHex16(url);
        if (!RedisUtil.hasKey(REDISKEY + key)) {
            RedisUtil.set(REDISKEY + key,url,-1);
        }
        return ServerResponseEntity.success(key);
    }

    @GetMapping("/getUrl")
    @ApiOperation(value = "根据Key获取触点链接", notes = "根据Key获取触点链接")
    public ServerResponseEntity<String> getUrl(String key) {
        return ServerResponseEntity.success(RedisUtil.get(REDISKEY + key).toString());
    }


//    @GetMapping("/getByStoreIdByCach")
//    @ApiOperation(value = "getByStoreIdByCach", notes = "getByStoreIdByCach")
//    public ServerResponseEntity<TzStore> getByStoreIdByCach(@RequestParam Long storeId) {
//        return ServerResponseEntity.success(tzStoreService.getByStoreId(storeId));
//    }
//
//    @GetMapping("/deleteCacheByStoreId")
//    @ApiOperation(value = "deleteCacheByStoreId", notes = "deleteCacheByStoreId")
//    public ServerResponseEntity<Void> deleteCacheByStoreId(@RequestParam Long storeId) {
//        tzStoreService.removeCacheByStoreId(storeId);
//        return ServerResponseEntity.success();
//    }


}
