package com.mall4j.cloud.product.controller.app;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.model.SpuCollection;
import com.mall4j.cloud.product.service.SpuCollectionService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.product.vo.app.SpuAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 商品收藏信息
 *
 * @author FrozenWatermelon
 * @date 2020-11-21 14:43:16
 */
@RestController("appSpuCollectionController")
@RequestMapping("/spu_collection")
@Api(tags = "app-商品收藏信息")
public class SpuCollectionController {

    @Autowired
    private SpuCollectionService spuCollectionService;

    @Autowired
    private SpuService spuService;

    @GetMapping("/page")
    @ApiOperation(value = "分页返回收藏数据", notes = "根据用户id获取")
    public ServerResponseEntity<PageVO<SpuAppVO>> getUserCollectionDtoPageByUserId(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId,
                                                                                   PageDTO page, String prodName,Integer prodType) {
        return ServerResponseEntity.success(spuCollectionService.getUserCollectionDtoPageByUserId(page, AuthUserContext.get().getUserId(),prodName,prodType,storeId));
    }

    @GetMapping("/ma/is_collection")
    @ApiOperation(value = "根据商品id获取该商品是否在收藏夹中", notes = "传入收藏商品id")
    @ApiImplicitParam(name = "spuId", value = "商品id")
    public ServerResponseEntity<Boolean> isCollection(@RequestParam Long spuId) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO == null) {
            return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
        }
        int count = spuCollectionService.userCollectionCount(spuId, userInfoInTokenBO.getUserId());
        return ServerResponseEntity.success(count > 0);

    }

    @PostMapping("/add_or_cancel")
    @ApiOperation(value = "添加/取消收藏(返回false：取消收藏  true：添加收藏)", notes = "传入收藏商品id,如果商品未收藏则收藏商品，已收藏则取消收藏")
    public ServerResponseEntity<Boolean> addOrCancel(@RequestBody Long spuId) {
        if (Objects.isNull(spuService.getBySpuId(spuId))) {
            throw new LuckException("该商品不存在");
        }
        boolean isAdd = false;
        Long userId = AuthUserContext.get().getUserId();
        if (spuCollectionService.userCollectionCount(spuId, userId) > 0) {
            spuCollectionService.deleteBySpuIdAndUserId(spuId, userId);
        } else {
            SpuCollection userCollection = new SpuCollection();
            userCollection.setUserId(userId);
            userCollection.setSpuId(spuId);
            spuCollectionService.save(userCollection);
            isAdd = true;
        }
        return ServerResponseEntity.success(isAdd);
    }

    @PostMapping("/batch_cancel")
    @ApiOperation(value = "批量取消收藏", notes = "传入收藏商品id")
    public ServerResponseEntity<Void> batachCancel(@RequestBody List<Long> spuIds) {
        Long userId = AuthUserContext.get().getUserId();
        if (CollUtil.isEmpty(spuIds)){
            return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        }
        spuCollectionService.deleteBatchBySpuIdsAndUserId(spuIds, userId);
        return ServerResponseEntity.success();
    }


    /**
     * 查询用户收藏商品数量
     */
    @GetMapping("count")
    @ApiOperation(value = "查询用户收藏商品数量", notes = "查询用户收藏商品数量")
    public int findUserCollectionCount() {
        return spuCollectionService.userCollectionCount(null, AuthUserContext.get().getUserId());
    }

    @PostMapping("/order_prod_collection_all")
    @ApiOperation(value = "订单商品收藏", notes = "传入商品id拼接字符串")
    @ApiImplicitParam(name = "spuIdList", value = "spuId集合")
    public ServerResponseEntity<Boolean> orderProdCollectionAll(@RequestBody List<Long> spuIdList) {
        if (CollectionUtil.isEmpty(spuIdList)) {
            throw new LuckException("spuId列表不能为空");
        }
        // 去重
        HashSet<Long> spuIdSet = new HashSet<>(spuIdList);
        spuCollectionService.spuBatchCollection(new ArrayList<>(spuIdSet));
        return ServerResponseEntity.success();
    }
}
