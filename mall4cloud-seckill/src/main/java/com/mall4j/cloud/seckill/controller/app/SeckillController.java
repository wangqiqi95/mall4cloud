package com.mall4j.cloud.seckill.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.product.dto.SpuSkuRDTO;
import com.mall4j.cloud.api.product.enums.SpuChannelEnums;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.seckill.dto.AppSeckillActivityDTO;
import com.mall4j.cloud.seckill.dto.SeckillDTO;
import com.mall4j.cloud.seckill.model.SeckillStore;
import com.mall4j.cloud.seckill.service.SeckillService;
import com.mall4j.cloud.seckill.service.SeckillSkuService;
import com.mall4j.cloud.seckill.service.SeckillStoreService;
import com.mall4j.cloud.seckill.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 秒杀信息
 *
 * @author lhd
 * @date 2021-04-12 09:36:59
 */
@Slf4j
@RestController("appSeckillController")
@RequestMapping("/ua/seckill")
@Api(tags = "app-秒杀活动信息")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private ConfigFeignClient configFeignClient;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;

    @Autowired
    private SeckillSkuService seckillSkuService;

    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private SeckillStoreService seckillStoreService;

    @GetMapping("/page")
    @ApiOperation(value = "获取首页秒杀信息", notes = "获取首页秒杀信息")
    public ServerResponseEntity<AppSeckillVO> page(SeckillDTO seckillDTO) {
        seckillDTO.setStatus(StatusEnum.ENABLE.value());
        AppSeckillVO appSeckillVO = seckillService.pageBySelectLot(seckillDTO);
        return ServerResponseEntity.success(appSeckillVO);
    }

    @GetMapping("/prod")
    @ApiOperation(value = "获取秒杀商品信息", notes = "根据商品id，获取当前秒杀的商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seckillId", value = "秒杀id", required = true, dataType = "Long"),
    })
    public ServerResponseEntity<AppSeckillSpuVO> getSeckill(@RequestParam(value = "storeId",required = false) Long storeId, @RequestParam(value = "seckillId") Long seckillId) {

        // 秒杀活动信息（来自缓存）
        SeckillVO seckill = seckillService.getBySeckillIdAndStoreId(seckillId,storeId);

        // 前端看到这个状态码的时候，不用渲染活动页面了
        if (seckill == null || !Objects.equals(seckill.getStatus(), StatusEnum.ENABLE.value()) || seckill.getEndTime().getTime() < System.currentTimeMillis()) {
            return ServerResponseEntity.fail(ResponseEnum.ACTIVITY_END);
        }
        boolean isInviteStore=false;
        ServerResponseEntity<Boolean> inviteStoreResponse=storeFeignClient.isInviteStore(storeId);
        if(inviteStoreResponse.isSuccess() && inviteStoreResponse.getData()){
            isInviteStore=inviteStoreResponse.getData();
        }
        AppSeckillSpuVO seckillSpuVO = buildAppSeckillSpuVO(storeId, seckillId,isInviteStore);
        return ServerResponseEntity.success(seckillSpuVO);
    }


    @GetMapping("/list_seckill_times")
    @ApiOperation(value = "显示秒杀后台配置信息", notes = "显示秒杀后台配置信息")
    public ServerResponseEntity<SeckillConfigVO> listSeckillTimes() {
        String config = configFeignClient.getConfig(ConfigNameConstant.SECKILL_TIME_CONFIG).getData();
        if (Objects.isNull(config)) {
            return ServerResponseEntity.success();
        }
        SeckillConfigVO seckillConfigVO = Json.parseObject(config, SeckillConfigVO.class);
        List<Integer> seckillTimeList = seckillConfigVO.getSeckillTimeList();
//        if(CollectionUtil.isEmpty(seckillTimeList)){
//            return ServerResponseEntity.success(seckillConfigVO);
//        }
        return ServerResponseEntity.success(seckillConfigVO);
    }

    @PostMapping("/activity/list")
    @ApiOperation(value = "获取秒杀活动集合", notes = "根据秒杀活动ID查询")
    public ServerResponseEntity<List<AppSeckillSpuVO>> activityList(@RequestParam(value = "storeId", defaultValue = "1") Long storeId, @RequestBody AppSeckillActivityDTO appSeckillActivityDTO) {
        boolean isInviteStore=false;
        ServerResponseEntity<Boolean> inviteStoreResponse=storeFeignClient.isInviteStore(storeId);
        if(inviteStoreResponse.isSuccess() && inviteStoreResponse.getData()){
            isInviteStore=inviteStoreResponse.getData();
        }
        List<AppSeckillSpuVO> collect = new ArrayList<>();
        for (Long seckillId : appSeckillActivityDTO.getSeckillIdList()) {
            AppSeckillSpuVO appSeckillSpuVO = buildAppSeckillSpuVO(storeId, seckillId,isInviteStore);
            if (Objects.nonNull(appSeckillSpuVO)) {
                collect.add(appSeckillSpuVO);
            }
        }
        return ServerResponseEntity.success(collect);
    }

    private AppSeckillSpuVO buildAppSeckillSpuVO(Long storeId, Long seckillId,boolean isInviteStore) {
        // 秒杀活动信息（来自缓存）
        SeckillVO seckill = seckillService.getBySeckillId(seckillId);

        //过滤R渠道不参与拼团活动价
        seckill.setRSpu(false);
        seckill.setActivityPrice(true);
        log.info("前置--> R渠道商品不参与限时调价活动(仅限官店) storeId:{} {}",storeId,(storeId==1?"官店不参与执行剔除逻辑":"门店参与"));
        if(Objects.nonNull(storeId) && storeId==1){
            List<Long> spuIds=new ArrayList<>();
            spuIds.add(seckill.getSpuId());
            ServerResponseEntity<List<Long>> serverResponseEntity=spuFeignClient.isSpuSkuChannel(new SpuSkuRDTO(SpuChannelEnums.CHANNEL_R.getCode(),spuIds));
            if(serverResponseEntity.isSuccess()
                    && CollectionUtil.isNotEmpty(serverResponseEntity.getData())
                    && serverResponseEntity.getData().size()>0){
                log.info("秒杀活动id【{}】商品id【{}】为R渠道 不参与",seckill.getSeckillId(),seckill.getSpuId());
                seckill.setRSpu(true);
            }
        }


        // 秒杀活动sku信息（来自缓存）
        List<SeckillSkuVO> seckillSkus = seckillSkuService.listSeckillSkuBySeckillId(seckill.getSeckillId());
        if (seckill.getLimitStoreType() != null && seckill.getLimitStoreType() == 1) {
            SeckillStore seckillStore = seckillStoreService.findBySeckillIdAndStoreId(seckillId, storeId);
            if (Objects.isNull(seckillStore)) {
                return null;
            }
        }
        // 商品信息（来自缓存）
        SpuVO spuVO = spuFeignClient.getDetailById(seckill.getSpuId()).getData();

        // 商品sku信息（来自缓存）
//        List<SkuAppVO> skuAppVO = skuFeignClient.listBySpuId(seckill.getSpuId()).getData();
        List<SkuAppVO> skuAppVO = skuFeignClient.listBySpuIdAndStoreId(seckill.getSpuId(),storeId).getData();
        log.info("秒杀商品信息1--seckillSkus-> {}", JSON.toJSONString(seckillSkus));
        log.info("秒杀商品信息2--skuAppVO-> {}", JSON.toJSONString(skuAppVO));
        List<AppSeckillSkuVO> seckillSkusVO = seckillSkus.stream().map(seckillSku -> {

            AppSeckillSkuVO appSeckillSkuVO = new AppSeckillSkuVO();
            for (SkuAppVO sku : skuAppVO) {
                if (!Objects.equals(sku.getSkuId(), seckillSku.getSkuId())) {
                    continue;
                }
                appSeckillSkuVO.setSkuName(sku.getSkuName());
                appSeckillSkuVO.setImgUrl(sku.getImgUrl());
                appSeckillSkuVO.setProperties(sku.getProperties());
                appSeckillSkuVO.setPriceFee(sku.getPriceFee());
                // 如果没有市场价，就用售价当作市场价
                appSeckillSkuVO.setMarketPriceFee(Objects.equals(sku.getMarketPriceFee(), 0L) ? sku.getPriceFee() : sku.getMarketPriceFee());

                appSeckillSkuVO.setSkuId(sku.getSkuId());
                appSeckillSkuVO.setSeckillId(seckill.getSeckillId());
                appSeckillSkuVO.setStock(seckillSku.getSeckillStocks());
                appSeckillSkuVO.setSeckillSkuId(seckillSku.getSeckillSkuId());
                if(isInviteStore){//虚拟门店
                    if(sku.getSkuProtectPrice()>0){
                        appSeckillSkuVO.setSeckillPrice(sku.getPriceFee());
                        seckill.setActivityPrice(false);
                    }else{
                        appSeckillSkuVO.setSeckillPrice(seckillSku.getSeckillPrice());
                    }
                }else{
                    //R渠道商品不参与拼团活动价
                    if(seckill.isRSpu()) {
                        appSeckillSkuVO.setSeckillPrice(sku.getPriceFee());
                    }else if(sku.getStoreSkuStock()<=0 || (sku.getStoreProtectPrice()>0 && sku.getStoreSkuStock()>0)){
                        //剔除门店无库存(取官店价格)、门店有库存且有保护价(取保护价) -> sku,不参与活动价
                        appSeckillSkuVO.setSeckillPrice(sku.getPriceFee());
                        seckill.setActivityPrice(false);
                    }else{
                        appSeckillSkuVO.setSeckillPrice(seckillSku.getSeckillPrice());
                    }
                }

            }

            return appSeckillSkuVO;
        }).collect(Collectors.toList());
        log.info("秒杀商品信息3--seckillSkusVO-> {}", JSON.toJSONString(seckillSkusVO));

        AppSeckillSpuVO seckillSpuVO = mapperFacade.map(seckill, AppSeckillSpuVO.class);
        seckillSpuVO.setShopId(seckill.getShopId());
        seckillSpuVO.setSpuVO(spuVO);
        seckillSpuVO.setSeckillOriginStocks(seckill.getSeckillOriginStocks());
        seckillSpuVO.setMaxNum(seckill.getMaxNum());
        seckillSpuVO.setSeckillTotalStocks(seckill.getSeckillTotalStocks());
        seckillSpuVO.setSeckillSkuList(seckillSkusVO);
        //R渠道商品不参与拼团活动价
        if(seckill.isRSpu()) {
            seckillSpuVO.setSeckillPrice(seckillSkusVO.get(0).getPriceFee());
        }else{
            //获取sku最低价展示
            AppSeckillSkuVO skuPriceDTO = seckillSkusVO.stream().min(Comparator.comparing(AppSeckillSkuVO::getSeckillPrice)).get();
            seckillSpuVO.setSeckillPrice(skuPriceDTO.getSeckillPrice());
        }

        long now = System.currentTimeMillis();
        int activityStatus = 2;
        // 活动还没开始
        Date startTime = seckill.getStartTime();
        if (startTime.getTime() > now) {
            long residualTime = (startTime.getTime() - now) / 1000;
            // 距离活动开始还有
            seckillSpuVO.setStartIn(residualTime);
            activityStatus = 1;
        }
        // 活动还没结束
        if (seckill.getEndTime().getTime() > now) {
            // 距离活动结束还有
            long residualTime = (seckill.getEndTime().getTime() - now) / 1000;
            seckillSpuVO.setExpiresIn(residualTime);
        } else {
            activityStatus = 3;
        }
        seckillSpuVO.setActivityStatus(activityStatus);
        return seckillSpuVO;
    }
}
