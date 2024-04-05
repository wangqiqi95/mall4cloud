package com.mall4j.cloud.discount.controller.admin;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.discount.constant.DiscountStatusEnum;
import com.mall4j.cloud.discount.dto.DiscountDTO;
import com.mall4j.cloud.discount.service.DiscountService;
import com.mall4j.cloud.discount.service.DiscountSpuService;
import com.mall4j.cloud.discount.vo.DiscountVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


/**
 * @author lhd
 * @date 2020-12-17 15:10:00
 */
@RestController("adminDiscountController")
@RequestMapping("/mp/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;
    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private DiscountSpuService discountSpuService;

    @GetMapping("/page")
    @ApiOperation(value = "获取满减活动列表", notes = "获取满减活动列表")
    public ServerResponseEntity<PageVO<DiscountVO>> page(@Valid PageDTO pageDTO, DiscountDTO transportDTO) {
        Long tenantId = AuthUserContext.get().getTenantId();
        transportDTO.setShopId(tenantId);
        PageVO<DiscountVO> transports = discountService.page(pageDTO,transportDTO);
        return ServerResponseEntity.success(transports);
    }

    @GetMapping("/platform_page")
    @ApiOperation(value = "平台满减活动管理列表", notes = "平台满减活动管理列表")
    public ServerResponseEntity<PageVO<DiscountVO>> platformPage(@Valid PageDTO pageDTO, DiscountDTO transportDTO) {
        PageVO<DiscountVO> transports = discountService.platformPage(pageDTO,transportDTO);
        return ServerResponseEntity.success(transports);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{discountId}")
    public ServerResponseEntity<DiscountVO> info(@PathVariable("discountId") Long discountId) {
        Long shopId = AuthUserContext.get().getTenantId();
        DiscountVO discount = discountService.getDiscountAndSpu(discountId);
        if (Objects.isNull(discount)) {
            throw new LuckException("该活动不存在或者已删除");
        } else if (!Objects.equals(shopId, discount.getShopId()) && !Objects.equals(Constant.PLATFORM_SHOP_ID, shopId)) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        // 指定商品
        if (CollUtil.isNotEmpty(discount.getSpuIds())) {
            ServerResponseEntity<List<SpuSearchVO>> spuResponse = searchSpuFeignClient.listSpuBySpuIds(discount.getSpuIds());
            discount.setSpuList(spuResponse.getData());
        }
        return ServerResponseEntity.success(discount);
    }

    /**
     * 保存
     */
    @PostMapping
    public ServerResponseEntity<String> save(@RequestBody @Valid DiscountDTO discountDTO) {
        Long shopId = AuthUserContext.get().getTenantId();
        discountDTO.setShopId(shopId);
        discountService.insertDiscountAndItemAndSpu(discountDTO);
        discountService.removeDiscountCache(discountDTO.getDiscountId(), shopId);
        spuFeignClient.removeSpuActivityCache(discountDTO.getShopId(), discountDTO.getSpuIds());
        return ServerResponseEntity.success();
    }


    /**
     * 修改
     */
    @PutMapping
    public ServerResponseEntity<Void> update(@RequestBody @Valid DiscountDTO discountDTO) {
        DiscountVO discountDb = discountService.getDiscountAndSpu(discountDTO.getDiscountId());
        Long shopId = AuthUserContext.get().getTenantId();
        if (!Objects.equals(discountDb.getShopId(), shopId)) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        List<Long> spuIds = discountSpuService.listSpuIdByDiscountId(discountDb.getDiscountId());
        if (discountDb.getStatus() > DiscountStatusEnum.RUN.getValue() && discountDb.getStatus() < DiscountStatusEnum.OFFLINE.getValue()) {
            // 该活动已被平台下线，不能再更改状态
            throw new LuckException("该活动已被平台下线，不能再更改状态");
        }
        discountService.updateDiscountAndItemAndSpu(discountDTO);
        discountService.removeDiscountCache(discountDb.getDiscountId(), discountDb.getShopId());

        // 适用商品类型发生改变，清除店铺所有商品的缓存（全部商品转指定商品， 指定商品转全部商品）
        if (!Objects.equals(discountDb.getSuitableSpuType(), discountDTO.getSuitableSpuType())) {
            spuIds.clear();
        }
        if (CollUtil.isNotEmpty(spuIds)) {
            CollUtil.addAllIfNotContains(spuIds, discountDTO.getSpuIds());
        }
        spuFeignClient.removeSpuActivityCache(discountDTO.getShopId(), spuIds);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{discountId}")
    public ServerResponseEntity<Void> delete(@PathVariable Long discountId) {
        DiscountVO discount = discountService.getDiscountAndSpu(discountId);
        discountService.deleteDiscountsAndItemsAndSpuList(discountId, discount.getShopId());
        discountService.removeDiscountCache(discountId, discount.getShopId());
        spuFeignClient.removeSpuActivityCache(discount.getShopId(), discount.getSpuIds());
        return ServerResponseEntity.success();
    }


    @PostMapping("/offline")
    @ApiOperation(value = "下线活动", notes = "下线活动")
    public ServerResponseEntity<Void> offline(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        DiscountVO discount = discountService.getDiscountAndSpu(offlineHandleEventDto.getHandleId());
        if (Objects.isNull(discount)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        discountService.offline(offlineHandleEventDto);
        // 清除缓存
        discountService.removeDiscountCache(discount.getDiscountId(), discount.getShopId());
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_offline_handle_event/{discountId}")
    @ApiOperation(value = "通过discountId获取最新下线的事件", notes = "通过discountId获取最新下线的事件")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEvent(@PathVariable Long discountId) {
        return ServerResponseEntity.success(discountService.getOfflineHandleEvent(discountId));
    }

    @PostMapping("/audit")
    @ApiOperation(value = "审核活动", notes = "审核活动")
    public ServerResponseEntity<Void> audit(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        DiscountVO discount = discountService.getDiscountAndSpu(offlineHandleEventDto.getHandleId());
        discountService.audit(offlineHandleEventDto);
        // 清除缓存
        discountService.removeDiscountCache(discount.getDiscountId(), discount.getShopId());
        return ServerResponseEntity.success();
    }

    @PostMapping("/audit_apply")
    @ApiOperation(value = "违规活动提交审核", notes = "违规活动提交审核")
    public ServerResponseEntity<Void> auditApply(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        discountService.auditApply(offlineHandleEventDto);
        return ServerResponseEntity.success();
    }
}
