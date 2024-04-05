package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.dto.SpuSkuPriceDTO;
import com.mall4j.cloud.api.product.enums.SpuChannelEnums;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.service.SpuSkuPricingPriceService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品取价逻辑
 *
 * @author eury
 * @date 2022-07-29 15:27:24
 */
@Slf4j
@Service
public class SpuSkuPricingPriceServiceImpl  implements SpuSkuPricingPriceService {

    private static final  String TAG="取价逻辑->";

    @Autowired
    private TimeDiscountFeignClient timeDiscountFeignClient;

    @Autowired
    private SpuService spuService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SkuService skuService;
    @Autowired
    private StoreFeignClient storeFeignClient;

    /**
     * 小程序：商品列表、商品详情、猜你喜欢商品列表、分销商品列表、购物车、下单提交页 -> 商品取价逻辑
     * @param storeId
     * @param skuPriceDTOs
     * @return
     */
    @Override
    public List<SkuTimeDiscountActivityVO> getStoreSpuAndSkuPrice(Long storeId, List<SpuSkuPriceDTO> skuPriceDTOs) {
        StoreVO storeVO=storeFeignClient.findByStoreId(storeId);
        if(Objects.isNull(storeVO)){
            log.info("进入商品取价逻辑，未获取到门店信息取价失败:{}",storeId);
            return new ArrayList<>();
        }
        if(Objects.nonNull(storeVO.getStoreInviteType()) && storeVO.getStoreInviteType()==1){
            //虚拟门店取价
            log.info("进入虚拟门店取价：门店id:{} 门店code:{}",storeVO.getStoreId(),storeVO.getStoreCode());
            return getInviteStorePrice(storeVO,skuPriceDTOs);
        }else{
            //普通门店取价
            log.info("进入普通门店取价：门店id:{} 门店code:{}",storeVO.getStoreId(),storeVO.getStoreCode());
            return getStorePrice(storeId,skuPriceDTOs);
        }
    }

    /**
     * 虚拟门店取价逻辑
     * @return 取价顺序：取保护价 > 虚拟门店活动价 > 销售价(统一为吊牌价)
     */
    private List<SkuTimeDiscountActivityVO> getInviteStorePrice(StoreVO storeVO, List<SpuSkuPriceDTO> skuPriceDTOs){
        log.info(TAG+"开始执行虚拟门店取价逻辑 门店id【{}】 门店code【{}】 取价sku总数【{}】",storeVO.getStoreId(),storeVO.getStoreCode(),skuPriceDTOs.size());
        Long startTime=System.currentTimeMillis();
        //最终可取到活动价的数据
        Map<Long,TimeDiscountActivityVO> discountSkuMaps=new HashMap<>();

        //1.判断是否有保护价
        Map<Long, Long> skuIdMaps = skuPriceDTOs.stream().collect(Collectors.toMap(SpuSkuPriceDTO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO.getSkuId()));
        List<Long> skuIds=new ArrayList<>();//可以参与活动价的数据
        for(SpuSkuPriceDTO appVO:skuPriceDTOs){
            if(appVO.getSkuProtectPrice()>0){//有保护价，不参与活动价
                skuIdMaps.remove(appVO.getSkuId());
            }
            if(!discountSkuMaps.containsKey(appVO.getSkuId())){
                TimeDiscountActivityVO activityVO=new TimeDiscountActivityVO();
                activityVO.setSpuId(appVO.getSpuId());
                activityVO.setSkuId(appVO.getSkuId());
                if(appVO.getSkuProtectPrice()>0){
                    activityVO.setPrice(appVO.getSkuProtectPrice());//有保护价
                }else{
                    activityVO.setPrice(appVO.getMarketPriceFee());//销售价默认取吊牌价
                }
                discountSkuMaps.put(appVO.getSkuId(),activityVO);
            }
        }
        skuIds.addAll(skuIdMaps.values());

        log.info(TAG+"取价前置1-sku数 门店【{}】 sku总数【{}】 参与活动价sku总数【{}】 保护价sku数【{}】",
                storeVO.getStoreId(),
                skuPriceDTOs.size(),
                skuIds.size(),
                skuPriceDTOs.stream().filter(dTO -> dTO.getSkuProtectPrice()>0).collect(Collectors.toList()).size());

        //2.取虚拟门店活动价
        if(CollectionUtil.isNotEmpty(skuIds)){
            TimeDiscountActivityDTO timeDiscountActivityDTO = new TimeDiscountActivityDTO();//每次使用需要重新new，否则会造成数据取值重复问题
            timeDiscountActivityDTO.setShopId(storeVO.getStoreId());
            timeDiscountActivityDTO.setNoStoreStock(false);
            timeDiscountActivityDTO.setSkuIds(skuIds);
            timeDiscountActivityDTO.setType(3);
            ServerResponseEntity<List<TimeDiscountActivityVO>> inviteStoreResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
            if(inviteStoreResponses.isSuccess()){
                log.info(TAG+"取虚拟门店【{}】 可以参与虚拟门店价sku总数【{}】",storeVO.getStoreId(),inviteStoreResponses.getData().size());
                //可以参与活动价的数据
                inviteStoreResponses.getData().stream().forEach(activityVO -> {
                    if(discountSkuMaps.containsKey(activityVO.getSkuId()) && Objects.nonNull(activityVO.getPrice()) && activityVO.getPrice()>0){
                        discountSkuMaps.get(activityVO.getSkuId()).setPrice(activityVO.getPrice());
                        discountSkuMaps.get(activityVO.getSkuId()).setFriendlyCouponUseFlag(activityVO.getFriendlyCouponUseFlag());
                        discountSkuMaps.get(activityVO.getSkuId()).setFriendlyDiscountFlag(activityVO.getFriendlyDiscountFlag());
                        discountSkuMaps.get(activityVO.getSkuId()).setPriceType(5);
                        discountSkuMaps.get(activityVO.getSkuId()).setActivityId(activityVO.getActivityId());
                    }
                });
            }
        }

        //最终返回数据
        List<TimeDiscountActivityVO> discountSkus=new ArrayList<>(discountSkuMaps.values());
        List<SkuTimeDiscountActivityVO> backSkus = mapperFacade.mapAsList(discountSkus,SkuTimeDiscountActivityVO.class);

        //增加sku使用虚拟门店价标识
        backSkus.stream().forEach(skuVO -> {
            if(Objects.nonNull(skuVO.getPriceType()) && skuVO.getPriceType()==5){
                skuVO.setInvateStorePriceFlag(true);
            }
        });

        log.info(TAG+"结束执行虚拟门店取价逻辑->最终返回取价sku数【{}】 耗时：{}ms",backSkus.size(),System.currentTimeMillis() - startTime);
        return backSkus;
    }


    /**
     * 普通门店取价逻辑
     * @param storeId
     * @param skuPriceDTOs
     * @return
     */
    public List<SkuTimeDiscountActivityVO> getStorePrice(Long storeId, List<SpuSkuPriceDTO> skuPriceDTOs) {

        log.info(TAG+"开始执行取价逻辑 门店【{}】 取价sku总数【{}】",storeId,skuPriceDTOs.size());
        Long startTime=System.currentTimeMillis();

        /**
         * 前置取价：区分官店、门店取价逻辑
         * 1. 官店取价：官店保护价 > 官店会员价 > 官店渠道价(R渠道) > 官店限时调价 > 官店售价(erp推送价格)
         * 2. 门店取价：门店无库存取官店价格 > 门店有库存有保护价 > 门店有库存无保护价有会员价 > 门店有库存无保护价有限时调价 > 门店售价(erp推送价格)
         * 2.1 门店取价(无库存取官店价格)：官店保护价 > 官店会员价 > 官店渠道价(R渠道) > 官店限时调价 > 官店售价(erp推送价格)
         */
        Map<Long, Long> skuIdMaps = skuPriceDTOs.stream().collect(Collectors.toMap(SpuSkuPriceDTO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO.getSkuId()));
        List<Long> skuStoreNoStockIds=new ArrayList<>();//剔除门店无库存不参与活动价
        List<Long> skuIds=new ArrayList<>();//可以参与活动价的数据
        for(SpuSkuPriceDTO appVO:skuPriceDTOs){
            if(storeId == Constant.MAIN_SHOP){ //官店取价
                if(appVO.getSkuProtectPrice()>0){//有保护价，不参与活动价(会员、限时调价)
                    skuIdMaps.remove(appVO.getSkuId());
                }
            }else{ //门店取价
                if(appVO.getStoreSkuStock()<=0 && appVO.getSkuProtectPrice()<=0){//门店无库存取官店价格，不参与活动价->条件:官店无保护价，否则取官店保护价(price_fee erp推送保护价已处理直接取)
                    skuStoreNoStockIds.add(appVO.getSkuId());
                }
                if(appVO.getStoreSkuStock()<=0 || (appVO.getStoreProtectPrice()>0 && appVO.getStoreSkuStock()>0)){//有库存有保护价，不参与活动价(会员、限时调价)
                    skuIdMaps.remove(appVO.getSkuId());
                }
            }
        }
        skuIds.addAll(skuIdMaps.values());
        Map<Long, Long> skuStoreNoStockIdMaps = skuStoreNoStockIds.stream().collect(Collectors.toMap(s->s,s->s));

        log.info(TAG+"取价前置1-sku数 门店【{}】 sku总数【{}】 参与活动价sku总数【{}】 门店无库存sku总数【{}】 门店保护价sku数【{}】 官店保护价sku数【{}】 R渠道sku数【{}】",
                storeId,
                skuPriceDTOs.size(),
                skuIds.size(),
                skuStoreNoStockIds.size(),
                skuPriceDTOs.stream().filter(dTO -> dTO.getSkuProtectPrice()>0).collect(Collectors.toList()).size(),
                skuPriceDTOs.stream().filter(dTO -> dTO.getStoreProtectPrice()>0).collect(Collectors.toList()).size(),
                skuPriceDTOs.stream()
                        .filter(sku -> StrUtil.isNotBlank(sku.getChannelName()) && sku.getChannelName().equals(SpuChannelEnums.CHANNEL_R.getCode()))
                        .collect(Collectors.toList()).size());

        //最终可取到活动价的数据
        List<TimeDiscountActivityVO> discountSkus=new ArrayList<>();

        //门店无库存sku(门店无库存取官店逻辑：R渠道价(不参与限时调价) > 小程序配置活动价 > 官店铺货价)
        boolean checkShopR=false;

        /**
         * 以下价格取价逻辑：1、会员活动价 2、限时调价
         * 两者活动添加商品数据已做限制，同一个门店同一个商品同一个时间段不可添加同一个商品
         * 小程序活动价取价优先级官店：R渠道价 > 会员活动价 > 限时调价
         * 小程序活动价取价优先级门店：R渠道价 > 会员活动价 > 限时调价
         */
        TimeDiscountActivityDTO timeDiscountActivityDTO = new TimeDiscountActivityDTO();//每次使用需要重新new，否则会造成数据取值重复问题
        timeDiscountActivityDTO.setShopId(storeId);
        timeDiscountActivityDTO.setNoStoreStock(false);
        timeDiscountActivityDTO.setSkuIds(skuIds);

        //---1.增加会员逻辑 start---------------------------------------->>>>>
        timeDiscountActivityDTO.setType(2);
        ServerResponseEntity<List<TimeDiscountActivityVO>> memberResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
        if(memberResponses.isSuccess()){
            log.info(TAG+"取会员价 门店【{}】 可以参与会员价sku总数【{}】",storeId,memberResponses.getData().size());
            //可以参与活动价的数据
            memberResponses.getData().stream().forEach(activityVO -> {
                activityVO.setPriceType(1);
            });
            discountSkus.addAll(memberResponses.getData());
            //已经参加会员活动价，不再参与限时调价
            memberResponses.getData().stream().forEach(activityVO -> {
                if(skuIdMaps.containsKey(activityVO.getSkuId())){
                    skuIdMaps.remove(activityVO.getSkuId());
                }
            });
        }
        if(storeId!=Constant.MAIN_SHOP && skuStoreNoStockIds.size()>0){ //门店无库存取官店活动价(条件：官店有活动价)
            timeDiscountActivityDTO = new TimeDiscountActivityDTO();
            timeDiscountActivityDTO.setShopId(Constant.MAIN_SHOP);
            timeDiscountActivityDTO.setType(2);
            timeDiscountActivityDTO.setNoStoreStock(true);
            timeDiscountActivityDTO.setSkuStoreNoStockIds(skuStoreNoStockIds);
            ServerResponseEntity<List<TimeDiscountActivityVO>> memberNoStoreStockResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
            if(memberNoStoreStockResponses.isSuccess() && memberNoStoreStockResponses.getData().size()>0){
                checkShopR=true;
                log.info(TAG+"取会员价-门店无库存取官店 门店【{}】 可以参与会员价sku总数【{}】",storeId,memberNoStoreStockResponses.getData().size());
                //门店无库存可以参与活动价的数据
                memberNoStoreStockResponses.getData().stream().forEach(activityVO -> {
                    activityVO.setPriceType(2);
                });
                discountSkus.addAll(memberNoStoreStockResponses.getData());
                //已经参加会员活动价，不再参与限时调价
                memberNoStoreStockResponses.getData().stream().forEach(activityVO -> {
                    if(skuStoreNoStockIdMaps.containsKey(activityVO.getSkuId())){
                        skuStoreNoStockIdMaps.remove(activityVO.getSkuId());
                    }
                });
            }
        }
        //---1.增加会员逻辑 end---------------------------------------->>>>>


        //---2.增加限时调价逻辑 start---------------------------------------->>>>>
        skuIds=new ArrayList<>(skuIdMaps.values());
        skuStoreNoStockIds=new ArrayList<>(skuStoreNoStockIdMaps.values());
        log.info(TAG+"取价前置2-sku数 门店【{}】 参与sku总数【{}】 门店无库存sku总数【{}】",storeId,skuIds.size(),skuStoreNoStockIds.size());

        if(skuIds.size()>0 || skuStoreNoStockIds.size()>0){
            timeDiscountActivityDTO = new TimeDiscountActivityDTO();
            timeDiscountActivityDTO.setShopId(storeId);
            timeDiscountActivityDTO.setSkuIds(skuIds);
            timeDiscountActivityDTO.setNoStoreStock(false);
            timeDiscountActivityDTO.setType(1);
            ServerResponseEntity<List<TimeDiscountActivityVO>> timesResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
            if(timesResponses.isSuccess()){
                log.info(TAG+"取限时调价 门店【{}】 可以参与限时调价sku总数【{}】",storeId,timesResponses.getData().size());
                //可以参与活动价的数据
                timesResponses.getData().stream().forEach(activityVO -> { activityVO.setPriceType(3); });
                discountSkus.addAll(timesResponses.getData());
                //官店取到限时调价sku需要检查商品R渠道不参与限时调价
                if(storeId==Constant.MAIN_SHOP && CollectionUtil.isNotEmpty(timesResponses.getData())){
                    checkShopR=true;
                }
            }

            if(storeId!=Constant.MAIN_SHOP && skuStoreNoStockIds.size()>0){ //门店无库存取官店活动价(条件：官店有活动价)
                timeDiscountActivityDTO = new TimeDiscountActivityDTO();
                timeDiscountActivityDTO.setShopId(Constant.MAIN_SHOP);
                timeDiscountActivityDTO.setType(1);
                timeDiscountActivityDTO.setNoStoreStock(true);
                timeDiscountActivityDTO.setSkuStoreNoStockIds(skuStoreNoStockIds);
                ServerResponseEntity<List<TimeDiscountActivityVO>> timeNoStoreStockResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
                if(timeNoStoreStockResponses.isSuccess() && timeNoStoreStockResponses.getData().size()>0){
                    log.info(TAG+"取限时调价-门店无库存取官店 门店【{}】 可以参与限时调价sku总数【{}】",storeId,timeNoStoreStockResponses.getData().size());
                    checkShopR=true;
                    //门店无库存可以参与活动价的数据
                    timeNoStoreStockResponses.getData().stream().forEach(activityVO -> { activityVO.setPriceType(4); });
                    discountSkus.addAll(timeNoStoreStockResponses.getData());
                }
            }
        }
        //---2.增加限时调价逻辑 end---------------------------------------->>>>>


        //---3.R渠道商品不参与限时调价活动（门店无库存取到官店限时调价、官店取到限时调价价格需要剔除R渠道sku） start---------------------------------------->>>>>
        List<TimeDiscountActivityVO> mainShopTimeDisSku=new ArrayList<>();
        //门店取到官店限时调价sku
        mainShopTimeDisSku.addAll(discountSkus.stream().filter(discountSku-> discountSku.getPriceType()==4).collect(Collectors.toList()));
        if(storeId==Constant.MAIN_SHOP){//官店限时调价sku
            mainShopTimeDisSku.addAll(discountSkus.stream().filter(discountSku-> discountSku.getPriceType()==3).collect(Collectors.toList()));
        }
        log.info("前置--> R渠道商品不参与限时调价活动(仅限官店) storeId:【{}】 是否参与:【{}】 checkShopR:【{}】 需要校验R渠道限时调价sku数:{}",
                storeId,
                (storeId==Constant.MAIN_SHOP?"官店不参与执行剔除逻辑":"门店参与"),
                checkShopR,
                mainShopTimeDisSku.size());

        if(checkShopR && (CollectionUtil.isNotEmpty(mainShopTimeDisSku))){

            Map<Long, List<TimeDiscountActivityVO>> spuMap = mainShopTimeDisSku.stream().collect(Collectors.groupingBy(TimeDiscountActivityVO::getSpuId));
            List<Long> spuIds=new ArrayList<>(spuMap.keySet());

            //获取商品sku数据(R渠道)
            List<Sku> skus=skuService.list(new LambdaQueryWrapper<Sku>()
                    .eq(Sku::getStatus,1)
//                    .eq(Sku::getChannelName,SpuChannelEnums.CHANNEL_R.getCode())
                    .in(Sku::getSpuId,spuIds));

//            List<Long> rSpuList=spuService.isSpuSkuChannel(new SpuSkuRDTO(SpuChannelEnums.CHANNEL_R.getCode(),spuIds));

            if(CollectionUtil.isNotEmpty(skus)){
                //商品分组sku
                Map<Long, List<Sku>> spuSkuMap = skus.stream().collect(Collectors.groupingBy(Sku::getSpuId));
                for(Map.Entry<Long,List<Sku>> entry : spuSkuMap.entrySet()){
                    List<Sku> r_list = entry.getValue().stream()
                            .filter(sku -> StrUtil.isNotBlank(sku.getChannelName()) && sku.getChannelName().equals(SpuChannelEnums.CHANNEL_R.getCode()))
                            .collect(Collectors.toList());
                    int rCount=CollectionUtil.isNotEmpty(r_list)?r_list.size():0;//R渠道总数

                    log.info(TAG+"剔除R渠道-门店无库存取官店 门店【{}】 商品id【{}】 sku总数【{}】 R渠道sku总数【{}】 保护价sku数【{}】 限时调价sku数【{}】",
                            storeId,
                            entry.getKey(),
                            entry.getValue().size(),
                            rCount,
                            entry.getValue().stream().filter(dTO -> dTO.getProtectPrice()>0).collect(Collectors.toList()).size(),
                            mainShopTimeDisSku.size());

                    //R渠道商品条件：全部sku为R渠道
                    if(rCount>=entry.getValue().size()){
                        Map<Long, TimeDiscountActivityVO> activityVOAllMaps = discountSkus.stream()
                                .collect(Collectors.toMap(TimeDiscountActivityVO::getSkuId, activityVO -> activityVO,(k1, k2)->k1));
                        Map<Long, TimeDiscountActivityVO> mainShopTimeDisSkuMaps = mainShopTimeDisSku.stream()
                                .collect(Collectors.toMap(TimeDiscountActivityVO::getSkuId, activityVO -> activityVO,(k1, k2)->k1));
                        entry.getValue().forEach(item->{
                            if(mainShopTimeDisSkuMaps.containsKey(item.getSkuId())){//剔除R渠道不参与活动价sku
                                activityVOAllMaps.remove(item.getSkuId());
                            }
                        });
                        discountSkus=new ArrayList<>(activityVOAllMaps.values());
                    }
                }
            }
        }
        //---3.R渠道商品不参与限时调价活动 end---------------------------------------->>>>>

        //最终返回数据
        List<SkuTimeDiscountActivityVO> backSkus = mapperFacade.mapAsList(discountSkus,SkuTimeDiscountActivityVO.class);

        //增加sku使用会员日价标识
        backSkus.stream().forEach(skuVO -> {
            if(Objects.nonNull(skuVO.getPriceType()) && (skuVO.getPriceType()==1 || skuVO.getPriceType()==2)){
                skuVO.setMemberPriceFlag(true);
            }
        });

        log.info(TAG+"结束执行取价逻辑->最终返回取价sku数【{}】 耗时：{}ms",backSkus.size(),System.currentTimeMillis() - startTime);
        return backSkus;
    }






//    @Override
//    public List<SkuTimeDiscountActivityVO> getStoreSpuAndSkuPrice(Long storeId, List<SpuSkuPriceDTO> skuPriceDTOs) {
//
//        log.info(TAG+"开始执行取价逻辑 门店【{}】 取价sku总数【{}】",storeId,skuPriceDTOs.size());
//        Long startTime=System.currentTimeMillis();
//
//        /**
//         * 前置取价：区分官店、门店取价逻辑
//         * 1. 官店取价：官店保护价 > 官店会员价 > 官店渠道价(R渠道) > 官店限时调价 > 官店售价(erp推送价格)
//         * 2. 门店取价：门店无库存取官店价格 > 门店有库存有保护价 > 门店有库存无保护价有会员价 > 门店有库存无保护价有限时调价 > 门店售价(erp推送价格)
//         * 2.1 门店取价(无库存取官店价格)：官店保护价 > 官店会员价 > 官店渠道价(R渠道) > 官店限时调价 > 官店售价(erp推送价格)
//         */
//        Map<Long, Long> skuIdMaps = skuPriceDTOs.stream().collect(Collectors.toMap(SpuSkuPriceDTO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO.getSkuId()));
//        List<Long> skuStoreNoStockIds=new ArrayList<>();//剔除门店无库存不参与活动价
//        List<Long> skuIds=new ArrayList<>();//可以参与活动价的数据
//        for(SpuSkuPriceDTO appVO:skuPriceDTOs){
//            if(storeId == Constant.MAIN_SHOP){ //官店取价
//                if(appVO.getStoreProtectPrice()>0){//有保护价，不参与活动价(会员、限时调价)
//                    skuIdMaps.remove(appVO.getSkuId());
//                }
//            }else{ //门店取价
//                if(appVO.getStoreSkuStock()<=0 && appVO.getSkuProtectPrice()<=0){//门店无库存取官店价格，不参与活动价->条件:官店无保护价，否则取官店保护价(price_fee erp推送保护价已处理直接取)
//                    skuStoreNoStockIds.add(appVO.getSkuId());
//                }
//                if(appVO.getStoreSkuStock()<=0 || (appVO.getStoreProtectPrice()>0 && appVO.getStoreSkuStock()>0)){//有库存有保护价，不参与活动价(会员、限时调价)
//                    skuIdMaps.remove(appVO.getSkuId());
//                }
//            }
//        }
//        skuIds.addAll(skuIdMaps.values());
//        Map<Long, Long> skuStoreNoStockIdMaps = skuStoreNoStockIds.stream().collect(Collectors.toMap(s->s,s->s));
//
//        log.info(TAG+"取价前置1-sku数 门店【{}】 sku总数【{}】 参与活动价sku总数【{}】 门店无库存sku总数【{}】 门店保护价sku数【{}】 官店保护价sku数【{}】 R渠道sku数【{}】",
//                storeId,
//                skuPriceDTOs.size(),
//                skuIds.size(),
//                skuStoreNoStockIds.size(),
//                skuPriceDTOs.stream().filter(dTO -> dTO.getSkuProtectPrice()>0).collect(Collectors.toList()).size(),
//                skuPriceDTOs.stream().filter(dTO -> dTO.getStoreProtectPrice()>0).collect(Collectors.toList()).size(),
//                skuPriceDTOs.stream()
//                        .filter(sku -> StrUtil.isNotBlank(sku.getChannelName()) && sku.getChannelName().equals(SpuChannelEnums.CHANNEL_R.getCode()))
//                        .collect(Collectors.toList()).size());
//
//        //最终可取到活动价的数据
//        List<TimeDiscountActivityVO> discountSkus=new ArrayList<>();
//
//        //门店无库存sku(门店无库存取官店逻辑：R渠道价(不参与限时调价) > 小程序配置活动价 > 官店铺货价)
//        boolean checkShopR=false;
//
//        /**
//         * 以下价格取价逻辑：1、会员活动价 2、限时调价
//         * 两者活动添加商品数据已做限制，同一个门店同一个商品同一个时间段不可添加同一个商品
//         * 小程序活动价取价优先级官店：R渠道价 > 会员活动价 > 限时调价
//         * 小程序活动价取价优先级门店：R渠道价 > 会员活动价 > 限时调价
//         */
//        TimeDiscountActivityDTO timeDiscountActivityDTO = new TimeDiscountActivityDTO();//每次使用需要重新new，否则会造成数据取值重复问题
//        timeDiscountActivityDTO.setShopId(storeId);
//        timeDiscountActivityDTO.setNoStoreStock(false);
//        timeDiscountActivityDTO.setSkuIds(skuIds);
//
//        //---1.增加会员逻辑 start---------------------------------------->>>>>
//        timeDiscountActivityDTO.setType(2);
//        ServerResponseEntity<List<TimeDiscountActivityVO>> memberResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
//        if(memberResponses.isSuccess()){
//            log.info(TAG+"取会员价 门店【{}】 可以参与会员价sku总数【{}】",storeId,memberResponses.getData().size());
//            //可以参与活动价的数据
//            memberResponses.getData().stream().forEach(activityVO -> {
//                activityVO.setPriceType(1);
//            });
//            discountSkus.addAll(memberResponses.getData());
//            //已经参加会员活动价，不再参与限时调价
//            memberResponses.getData().stream().forEach(activityVO -> {
//                if(skuIdMaps.containsKey(activityVO.getSkuId())){
//                    skuIdMaps.remove(activityVO.getSkuId());
//                }
//            });
//        }
//        if(storeId!=Constant.MAIN_SHOP && skuStoreNoStockIds.size()>0){ //门店无库存取官店活动价(条件：官店有活动价)
//            timeDiscountActivityDTO = new TimeDiscountActivityDTO();
//            timeDiscountActivityDTO.setShopId(Constant.MAIN_SHOP);
//            timeDiscountActivityDTO.setType(2);
//            timeDiscountActivityDTO.setNoStoreStock(true);
//            timeDiscountActivityDTO.setSkuStoreNoStockIds(skuStoreNoStockIds);
//            ServerResponseEntity<List<TimeDiscountActivityVO>> memberNoStoreStockResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
//            if(memberNoStoreStockResponses.isSuccess() && memberNoStoreStockResponses.getData().size()>0){
//                checkShopR=true;
//                log.info(TAG+"取会员价-门店无库存取官店 门店【{}】 可以参与会员价sku总数【{}】",storeId,memberNoStoreStockResponses.getData().size());
//                //门店无库存可以参与活动价的数据
//                memberNoStoreStockResponses.getData().stream().forEach(activityVO -> {
//                    activityVO.setPriceType(2);
//                });
//                discountSkus.addAll(memberNoStoreStockResponses.getData());
//                //已经参加会员活动价，不再参与限时调价
//                memberNoStoreStockResponses.getData().stream().forEach(activityVO -> {
//                    if(skuStoreNoStockIdMaps.containsKey(activityVO.getSkuId())){
//                        skuStoreNoStockIdMaps.remove(activityVO.getSkuId());
//                    }
//                });
//            }
//        }
//        //---1.增加会员逻辑 end---------------------------------------->>>>>
//
//
//        //---2.增加限时调价逻辑 start---------------------------------------->>>>>
//        skuIds=new ArrayList<>(skuIdMaps.values());
//        skuStoreNoStockIds=new ArrayList<>(skuStoreNoStockIdMaps.values());
//        log.info(TAG+"取价前置2-sku数 门店【{}】 参与sku总数【{}】 门店无库存sku总数【{}】",storeId,skuIds.size(),skuStoreNoStockIds.size());
//
//        if(skuIds.size()>0 || skuStoreNoStockIds.size()>0){
//            timeDiscountActivityDTO = new TimeDiscountActivityDTO();
//            timeDiscountActivityDTO.setShopId(storeId);
//            timeDiscountActivityDTO.setSkuIds(skuIds);
//            timeDiscountActivityDTO.setNoStoreStock(false);
//            timeDiscountActivityDTO.setType(1);
//            ServerResponseEntity<List<TimeDiscountActivityVO>> timesResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
//            if(timesResponses.isSuccess()){
//                log.info(TAG+"取限时调价 门店【{}】 可以参与限时调价sku总数【{}】",storeId,timesResponses.getData().size());
//                //可以参与活动价的数据
//                timesResponses.getData().stream().forEach(activityVO -> { activityVO.setPriceType(3); });
//                discountSkus.addAll(timesResponses.getData());
//                //官店取到限时调价sku需要检查商品R渠道不参与限时调价
//                if(storeId==Constant.MAIN_SHOP && CollectionUtil.isNotEmpty(timesResponses.getData())){
//                    checkShopR=true;
//                }
//            }
//
//            if(storeId!=Constant.MAIN_SHOP && skuStoreNoStockIds.size()>0){ //门店无库存取官店活动价(条件：官店有活动价)
//                timeDiscountActivityDTO = new TimeDiscountActivityDTO();
//                timeDiscountActivityDTO.setShopId(Constant.MAIN_SHOP);
//                timeDiscountActivityDTO.setType(1);
//                timeDiscountActivityDTO.setNoStoreStock(true);
//                timeDiscountActivityDTO.setSkuStoreNoStockIds(skuStoreNoStockIds);
//                ServerResponseEntity<List<TimeDiscountActivityVO>> timeNoStoreStockResponses = timeDiscountFeignClient.convertActivityPricesNoFilter(timeDiscountActivityDTO);
//                if(timeNoStoreStockResponses.isSuccess() && timeNoStoreStockResponses.getData().size()>0){
//                    log.info(TAG+"取限时调价-门店无库存取官店 门店【{}】 可以参与限时调价sku总数【{}】",storeId,timeNoStoreStockResponses.getData().size());
//                    checkShopR=true;
//                    //门店无库存可以参与活动价的数据
//                    timeNoStoreStockResponses.getData().stream().forEach(activityVO -> { activityVO.setPriceType(4); });
//                    discountSkus.addAll(timeNoStoreStockResponses.getData());
//                }
//            }
//        }
//        //---2.增加限时调价逻辑 end---------------------------------------->>>>>
//
//
//        //---3.R渠道商品不参与限时调价活动（门店无库存取到官店限时调价、官店取到限时调价价格需要剔除R渠道sku） start---------------------------------------->>>>>
//        List<TimeDiscountActivityVO> mainShopTimeDisSku=new ArrayList<>();
//        //门店取到官店限时调价sku
//        mainShopTimeDisSku.addAll(discountSkus.stream().filter(discountSku-> discountSku.getPriceType()==4).collect(Collectors.toList()));
//        if(storeId==Constant.MAIN_SHOP){//官店限时调价sku
//            mainShopTimeDisSku.addAll(discountSkus.stream().filter(discountSku-> discountSku.getPriceType()==3).collect(Collectors.toList()));
//        }
//        log.info("前置--> R渠道商品不参与限时调价活动(仅限官店) storeId:【{}】 是否参与:【{}】 checkShopR:【{}】 需要校验R渠道限时调价sku数:{}",
//                storeId,
//                (storeId==Constant.MAIN_SHOP?"官店不参与执行剔除逻辑":"门店参与"),
//                checkShopR,
//                mainShopTimeDisSku.size());
//
//        if(checkShopR && (CollectionUtil.isNotEmpty(mainShopTimeDisSku))){
//
//            Map<Long, List<TimeDiscountActivityVO>> spuMap = mainShopTimeDisSku.stream().collect(Collectors.groupingBy(TimeDiscountActivityVO::getSpuId));
//            List<Long> spuIds=new ArrayList<>(spuMap.keySet());
//
//            //获取商品sku数据(R渠道)
//            List<Sku> skus=skuService.list(new LambdaQueryWrapper<Sku>()
//                    .eq(Sku::getStatus,1)
////                    .eq(Sku::getChannelName,SpuChannelEnums.CHANNEL_R.getCode())
//                    .in(Sku::getSpuId,spuIds));
//
////            List<Long> rSpuList=spuService.isSpuSkuChannel(new SpuSkuRDTO(SpuChannelEnums.CHANNEL_R.getCode(),spuIds));
//
//            if(CollectionUtil.isNotEmpty(skus)){
//                //商品分组sku
//                Map<Long, List<Sku>> spuSkuMap = skus.stream().collect(Collectors.groupingBy(Sku::getSpuId));
//                for(Map.Entry<Long,List<Sku>> entry : spuSkuMap.entrySet()){
//                    List<Sku> r_list = entry.getValue().stream()
//                            .filter(sku -> StrUtil.isNotBlank(sku.getChannelName()) && sku.getChannelName().equals(SpuChannelEnums.CHANNEL_R.getCode()))
//                            .collect(Collectors.toList());
//                    int rCount=CollectionUtil.isNotEmpty(r_list)?r_list.size():0;//R渠道总数
//
//                    log.info(TAG+"剔除R渠道-门店无库存取官店 门店【{}】 商品id【{}】 sku总数【{}】 R渠道sku总数【{}】 保护价sku数【{}】 限时调价sku数【{}】",
//                            storeId,
//                            entry.getKey(),
//                            entry.getValue().size(),
//                            rCount,
//                            entry.getValue().stream().filter(dTO -> dTO.getProtectPrice()>0).collect(Collectors.toList()).size(),
//                            mainShopTimeDisSku.size());
//
//                    //R渠道商品条件：全部sku为R渠道
//                    if(rCount>=entry.getValue().size()){
//                        Map<Long, TimeDiscountActivityVO> activityVOAllMaps = discountSkus.stream()
//                                .collect(Collectors.toMap(TimeDiscountActivityVO::getSkuId, activityVO -> activityVO,(k1, k2)->k1));
//                        Map<Long, TimeDiscountActivityVO> mainShopTimeDisSkuMaps = mainShopTimeDisSku.stream()
//                                .collect(Collectors.toMap(TimeDiscountActivityVO::getSkuId, activityVO -> activityVO,(k1, k2)->k1));
//                        entry.getValue().forEach(item->{
//                            if(mainShopTimeDisSkuMaps.containsKey(item.getSkuId())){//剔除R渠道不参与活动价sku
//                                activityVOAllMaps.remove(item.getSkuId());
//                            }
//                        });
//                        discountSkus=new ArrayList<>(activityVOAllMaps.values());
//                    }
//                }
//            }
//        }
//        //---3.R渠道商品不参与限时调价活动 end---------------------------------------->>>>>
//
//        //最终返回数据
//        List<SkuTimeDiscountActivityVO> backSkus = mapperFacade.mapAsList(discountSkus,SkuTimeDiscountActivityVO.class);
//
//        //增加sku使用会员日价标识
//        backSkus.stream().forEach(skuVO -> {
//            if(Objects.nonNull(skuVO.getPriceType()) && (skuVO.getPriceType()==1 || skuVO.getPriceType()==2)){
//                skuVO.setMemberPriceFlag(true);
//            }
//        });
//
//        log.info(TAG+"结束执行取价逻辑->最终返回取价sku数【{}】 耗时：{}ms",backSkus.size(),System.currentTimeMillis() - startTime);
//        return backSkus;
//    }

}
