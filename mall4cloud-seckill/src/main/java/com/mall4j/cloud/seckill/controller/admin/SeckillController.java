package com.mall4j.cloud.seckill.controller.admin;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.group.feign.MealCommodityPoolFeignClient;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.seckill.dto.SeckillDTO;
import com.mall4j.cloud.seckill.dto.SeckillSkuDTO;
import com.mall4j.cloud.seckill.service.SeckillService;
import com.mall4j.cloud.seckill.service.SeckillSkuService;
import com.mall4j.cloud.seckill.vo.SeckillAdminVO;
import com.mall4j.cloud.seckill.vo.SeckillSpuVO;
import com.mall4j.cloud.seckill.vo.SeckillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 秒杀信息
 *
 * @author FrozenWatermelon
 * @date 2019-08-28 09:36:59
 */
@RestController("adminSeckillController")
@RequestMapping("/mp/seckill")
@Api(tags = "秒杀管理")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Autowired
    private SeckillSkuService seckillSkuService;



    @GetMapping("/page")
    @ApiOperation(value = "获取秒杀信息列表", notes = "分页获取秒杀信息列表")
    public ServerResponseEntity<PageVO<SeckillVO>> page(@Valid PageDTO pageDTO, SeckillDTO seckillDTO) {
        seckillDTO.setShopId(AuthUserContext.get().getTenantId());
        PageVO<SeckillVO> seckillPage = seckillService.page(pageDTO,seckillDTO);
        return ServerResponseEntity.success(seckillPage);
    }

    @GetMapping("/list_seckill")
    @ApiOperation(value = "显示最近的秒杀信息列表", notes = "显示最近的秒杀信息列表，type字段，0:为即将开场，1：已经开场,specTime时间搜索")
    public ServerResponseEntity<PageVO<SeckillAdminVO>> listSeckill(@Valid PageDTO pageDTO,@RequestParam("type") Integer type,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")@RequestParam(value = "specTime",required = false) Long specTime) {
        Long shopId = AuthUserContext.get().getTenantId();
        PageVO<SeckillAdminVO> adminVOList = seckillService.listByShopId(pageDTO,shopId,type,specTime);
        return ServerResponseEntity.success(adminVOList);
    }

    @GetMapping("/list_end_seckill")
    @ApiOperation(value = "显示已经结束的秒杀信息列表", notes = "显示已经结束的秒杀信息列表,specTime时间搜索")
    public ServerResponseEntity<PageVO<SeckillAdminVO>> listEndSeckill(@Valid PageDTO pageDTO, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")@RequestParam(value = "specTime",required = false) Long specTime) {
        Long shopId = AuthUserContext.get().getTenantId();
        PageVO<SeckillAdminVO> adminVOList = seckillService.listEndSeckillByShopId(pageDTO,shopId,specTime);
        return ServerResponseEntity.success(adminVOList);
    }

    @GetMapping("/list_seckill_spu_by_time")
    @ApiOperation(value = "根据时间、商品名称返回秒杀商品信息列表", notes = "显示最近一个月秒杀信息列表")
    public ServerResponseEntity<PageVO<SeckillSpuVO> > listSeckillSpuByTime(@RequestParam("startTimestamp") Long startTimestamp,
                                                                            PageDTO pageDTO, ProductSearchDTO productSearch) {
        PageVO<SeckillSpuVO> adminVOList = seckillService.listSeckillSpuByTime(pageDTO,startTimestamp,productSearch);
        return ServerResponseEntity.success(adminVOList);
    }

    /**
     * 通过id查询秒杀信息
     *
     * @param seckillId id
     * @return 单个数据
     */
    @GetMapping
    @ApiOperation(value = "通过id查询秒杀信息", notes = "通过id查询秒杀信息")
    public ServerResponseEntity<SeckillVO> getById(@RequestParam("seckillId") Long seckillId) {
        SeckillVO seckillVO = seckillService.getBySeckillId(seckillId);

        SpuVO spuVO = spuFeignClient.getSpuAndSkuBySpuId(seckillVO.getSpuId()).getData();
        seckillVO.setSpuVO(spuVO);
        seckillVO.setSkuList(spuVO.getSkus());
        seckillVO.setSeckillSkuList(seckillSkuService.listSeckillSkuBySeckillId(seckillId));
        return ServerResponseEntity.success(seckillVO);
    }

    /**
     * 新增秒杀信息
     *
     * @return 是否新增成功
     */
    @PostMapping
    @ApiOperation(value = "新增秒杀信息", notes = "新增秒杀信息")
    public ServerResponseEntity<Void> save(@RequestBody @Valid SeckillDTO seckillDTO) {
        SeckillVO seckill = mapperFacade.map(seckillDTO, SeckillVO.class);
        // 处理时间
        Date startTime = new Date(seckillDTO.getStartTimestamps());
        Date endTime = DateUtil.offsetHour(startTime,2);
//        startTime = DateUtil.beginOfDay(startTime);
        seckill.setStartTime(startTime);
        seckill.setEndTime(endTime);
        Long shopId = AuthUserContext.get().getTenantId();
        List<SpuVO> spuList = spuFeignClient.listCanSeckillProd(seckillDTO.getSpuId(), shopId).getData();
        if (CollectionUtil.isEmpty(spuList)) {
            // 商品无法参与秒杀活动
            throw new LuckException("商品无法参与秒杀活动");
        }

        // 当前小时
        int nowHour = DateUtil.hour(new Date(), true);
        if(Objects.equals(DateUtil.beginOfDay(new Date()),startTime) && seckillDTO.getSelectedLot() <= nowHour + 1){
            // 商品无法参与秒杀活动
            throw new LuckException("不能参与离开场不足一个小时的秒杀活动！");
        }
        List<SeckillSkuDTO> seckillSkuList = seckillDTO.getSeckillSkuList();

        Integer seckillTotalStocks = 0;

        long seckillPrice = Long.MAX_VALUE;


        for (SeckillSkuDTO seckillSku : seckillSkuList) {

            seckillTotalStocks += seckillSku.getSeckillStocks();

            seckillPrice = Math.min(seckillPrice, seckillSku.getSeckillPrice());
        }

        // 库存
        seckill.setSeckillOriginStocks(seckillTotalStocks);
        seckill.setSeckillTotalStocks(seckillTotalStocks);
        // 配置店铺id
        seckill.setShopId(AuthUserContext.get().getTenantId());
        seckill.setSpuId(seckillDTO.getSpuId());
        seckill.setSeckillPrice(seckillPrice);
        seckillService.saveSeckillAndItems(seckill, seckillSkuList);

        seckillSkuService.removeSeckillSkuCacheBySeckillId(seckill.getSeckillId());
        seckillService.removeSeckillCacheById(seckill.getSeckillId());

        return ServerResponseEntity.success();
    }

    /**
     * 使秒杀商品失效
     * @param seckillId 秒杀信息
     * @return 是否失效成功
     */
    @PutMapping("/invalid/{seckillId}")
    @ApiOperation(value = "使秒杀商品失效", notes = "使秒杀商品失效")
    public ServerResponseEntity<Void> invalidById(@PathVariable Long seckillId) {
        SeckillVO seckill = seckillService.getBySeckillId(seckillId);
        Long shopId = AuthUserContext.get().getTenantId();
        if (seckill == null) {
            throw new LuckException("未找到此活动，请稍后重试");
        }
        if (!Objects.equals(shopId,seckill.getShopId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        if (!Objects.equals(seckill.getStatus(), StatusEnum.ENABLE.value())) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        seckillService.invalidById(seckill.getSeckillId(),seckill.getSpuId());

        seckillSkuService.removeSeckillSkuCacheBySeckillId(seckill.getSeckillId());
        seckillService.removeSeckillCacheById(seckill.getSeckillId());
        return ServerResponseEntity.success();
    }

    /**
     * 通过id删除秒杀信息
     *
     * @param seckillId id
     * @return 是否删除成功
     */
    @DeleteMapping
    @ApiOperation(value = "通过id删除秒杀信息", notes = "通过id删除秒杀信息")
    public ServerResponseEntity<Void> removeById(@RequestParam("seckillId") Long seckillId) {
        SeckillVO seckill = seckillService.getBySeckillId(seckillId);

        if (!Objects.equals(StatusEnum.DISABLE.value(), seckill.getStatus())) {
            // 秒杀活动未关闭，无法删除
            throw new LuckException("秒杀活动未关闭，无法删除");
        }
        seckill.setIsDelete(1);
        seckillService.updateById(seckill);
        seckillSkuService.removeSeckillSkuCacheBySeckillId(seckill.getSeckillId());
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_offline_handle_event/{seckillId}")
    @ApiOperation(value = "获取最新下线的事件", notes = "获取最新下线的事件")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEvent(@PathVariable Long seckillId) {
        return ServerResponseEntity.success(seckillService.getOfflineHandleEvent(seckillId));
    }

    @PostMapping("/offline")
    @ApiOperation(value = "下线秒杀活动", notes = "下线秒杀活动")
    public ServerResponseEntity<Void> offline(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        SeckillVO seckillVO = seckillService.getBySeckillId(offlineHandleEventDto.getHandleId());
        if (Objects.isNull(seckillVO)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        seckillService.offline(offlineHandleEventDto,seckillVO.getSpuId());
        seckillService.removeSeckillCacheById(offlineHandleEventDto.getHandleId());
        seckillSkuService.removeSeckillSkuCacheBySeckillId(offlineHandleEventDto.getHandleId());
        return ServerResponseEntity.success();
    }

    @PostMapping("/audit")
    @ApiOperation(value = "审核活动", notes = "审核活动")
    public ServerResponseEntity<Void> audit(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        seckillService.audit(offlineHandleEventDto);
        seckillService.removeSeckillCacheById(offlineHandleEventDto.getHandleId());
        seckillSkuService.removeSeckillSkuCacheBySeckillId(offlineHandleEventDto.getHandleId());
        return ServerResponseEntity.success();
    }

    @PostMapping("/audit_apply")
    @ApiOperation(value = "违规活动提交审核", notes = "违规活动提交审核")
    public ServerResponseEntity<Void> auditApply(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        seckillService.auditApply(offlineHandleEventDto);
        seckillService.removeSeckillCacheById(offlineHandleEventDto.getHandleId());
        seckillSkuService.removeSeckillSkuCacheBySeckillId(offlineHandleEventDto.getHandleId());
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_join_seckill_merchant_num")
    @ApiOperation(value = "获取正在参与秒杀活动的商家数量", notes = "获取正在参与秒杀活动的商家数量")
    public ServerResponseEntity<Integer> getJoinSeckillMerchantNum() {
        if(!Objects.equals(AuthUserContext.get().getTenantId(), Constant.PLATFORM_SHOP_ID)){
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        Integer num = seckillService.getJoinSeckillMerchantNum();
        return ServerResponseEntity.success(num);
    }
}
