package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.enums.SkuPriceLogType;
import com.mall4j.cloud.api.product.dto.ESkuPriceLogDTO;
import com.mall4j.cloud.api.product.dto.SkuPriceLogDTO;
import com.mall4j.cloud.api.product.enums.PriceTypeEnums;
import com.mall4j.cloud.api.product.enums.SpuChannelEnums;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.PrincipalUtil;
import com.mall4j.cloud.product.mapper.ProtectActivitySpuMapper;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.mapper.SkuStoreMapper;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2022年4月11日, 0011 10:15
 * @Created by eury
 * 处理erp价格同步
 */
@Slf4j
@Service
public class SpuChannelPriceServiceImpl implements SpuChannelPriceService {

    private static final Logger logger = LoggerFactory.getLogger(SpuChannelPriceServiceImpl.class);

    private static final String LOG_TAG_CHANNEL_PRICE="定时任务设置渠道价";
    private static final String LOG_TAG_CANCEL_CHANNEL_PRICE="定时任务取消渠道价";

    @Autowired
    private SpuServiceImpl spuService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SkuStoreService skuStoreService;
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private SkuStoreMapper skuStoreMapper;
    @Autowired
    private SpuSkuPriceLogServiceImpl spuSkuPriceLogService;
    @Resource
    private ProtectActivitySpuMapper protectActivitySpuMapper;
    @Autowired
    private TimeDiscountFeignClient timeDiscountFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private SkuPriceLogService skuPriceLogService;



    @Async
    @Override
    public void asyncChannelPriceAndCancelTask(String spuCodes) {

        String updateBy=Optional.ofNullable(AuthUserContext.get()).orElse(new UserInfoInTokenBO()).getUsername();
        logger.info("手动重置渠道价 操作人【{}】 {}",updateBy,"-----开始设置渠道价任务------");
        Long startTime=System.currentTimeMillis();

        this.channelPriceTask(spuCodes,updateBy);

        logger.info("手动重置渠道价 -----结束设置渠道价任务------耗时：{}ms", System.currentTimeMillis() - startTime);

        logger.info("手动重置渠道价 操作人【{}】  {}",updateBy,"-----开始取消渠道价任务------");
        startTime=System.currentTimeMillis();

        this.cancelChannelPriceTask(spuCodes,updateBy);

        logger.info("手动重置渠道价 -----结束取消渠道价任务------耗时：{}ms", System.currentTimeMillis() - startTime);

        SpuSkuPriceLog spuSkuPriceLog=new SpuSkuPriceLog();
        spuSkuPriceLog.setPriceFee(0L);
        spuSkuPriceLog.setPriceFeeNew(0L);
        spuSkuPriceLog.setMarketPriceFee(0L);
        spuSkuPriceLog.setUpdateTime(new Date());
        spuSkuPriceLog.setRemarks("管理员:"+ AuthUserContext.get().getUsername()+"_id:"+AuthUserContext.get().getUserId()+" 后台操作重置渠道价");
        spuSkuPriceLogService.save(spuSkuPriceLog);
    }


    /**
     * 定时任务设置渠道价
     */
    @Override
    public void channelPriceTask(String spuCodes,String updateBy) {

        log.info(LOG_TAG_CHANNEL_PRICE+" 开始执行商品批量设置渠道价");
        Long startTime=System.currentTimeMillis();

        //获取全部上架商品，且渠道为R
        LambdaQueryWrapper<Spu> lambdaQueryWrapper=new LambdaQueryWrapper<Spu>();
        lambdaQueryWrapper.eq(Spu::getStatus,1);
        lambdaQueryWrapper.eq(Spu::getChannelName,SpuChannelEnums.CHANNEL_R.getCode());
        if(StrUtil.isNotBlank(spuCodes)){
            lambdaQueryWrapper.in(Spu::getSpuCode,spuCodes);
        }
        List<Spu> spuList=spuService.list(lambdaQueryWrapper);

        if(CollectionUtil.isEmpty(spuList)){
            log.info(LOG_TAG_CHANNEL_PRICE+" 结束执行商品批量设置渠道价 未获取到上架商品，耗时：{}ms", System.currentTimeMillis() - startTime);
            return;
        }
        log.info(LOG_TAG_CHANNEL_PRICE+" 获取到上架商品条数【{}】",spuList.size());

        Map<Long,Spu> spuMaps=spuList.stream().collect(Collectors.toMap(Spu::getSpuId, a -> a,(k1,k2)->k1));
        List<Long> spuIds = spuList.stream().map(Spu::getSpuId).collect(Collectors.toList());

        //获取商品是否配置会员日活动价
        TimeDiscountActivityDTO activityDTO=new  TimeDiscountActivityDTO();
        activityDTO.setShopId(Constant.MAIN_SHOP);
        activityDTO.setSpuId(spuIds);
        ServerResponseEntity<List<TimeDiscountActivityVO>> response= timeDiscountFeignClient.currentActivityBySpuId(activityDTO);
        if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData()) && response.getData().size()>0){
            response.getData().stream().forEach(activitySpu->{
                spuMaps.remove(activitySpu.getSpuId());
            });
            spuList=new ArrayList<>(spuMaps.values());
            spuIds = spuList.stream().map(Spu::getSpuId).collect(Collectors.toList());
            log.info(LOG_TAG_CHANNEL_PRICE+" 获取到会员日商品spu数【{}】 剔除会员日商品不参与渠道价后spu数【{}】",response.getData().size(),spuList.size());
        }

        if(CollectionUtil.isEmpty(spuList)){
            log.info(LOG_TAG_CHANNEL_PRICE+" 结束执行商品批量设置渠道价 未获取到上架商品，耗时：{}ms", System.currentTimeMillis() - startTime);
            return;
        }

        //获取电商保护价商品(生效状态，进行中的商品)
        List<ProtectActivitySpu> protectActivitySpus=protectActivitySpuMapper.getListBySpus(spuIds);
        final Map<Long, ProtectActivitySpu> protectSpuMaps=new HashMap<>();
        if(CollectionUtil.isNotEmpty(protectActivitySpus)){
            protectSpuMaps.putAll( protectActivitySpus.stream().collect(Collectors.toMap(ProtectActivitySpu::getSpuId, a -> a,(k1,k2)->k1)));
        }

        //改价日志
        List<SpuSkuPriceLog> spuSkuPriceLogs=new ArrayList<>();
        List<SkuPriceLogDTO> skuPriceLogs=new ArrayList<>();
        StoreVO storeVO=storeFeignClient.findByStoreId(Constant.MAIN_SHOP);
        Date now=new Date();

        spuList.forEach(spu -> {
            String spuCode=spu.getSpuCode();
            //获取商品sku数据
            List<Sku> skuList=skuService.list(new LambdaQueryWrapper<Sku>()
                    .eq(Sku::getStatus,1)
                    .eq(Sku::getSpuId,spu.getSpuId()));
            //获取官店保护款商品
            List<SkuStore> skuStoreList=skuStoreService.list(new LambdaQueryWrapper<SkuStore>()
                    .gt(SkuStore::getProtectPrice,0)
                    .eq(SkuStore::getStatus,1)
                    .eq(SkuStore::getStoreId,Constant.MAIN_SHOP)
                    .eq(SkuStore::getSpuId,spu.getSpuId()));
            Map<String, SkuStore> skuStoreMaps=new HashMap<>();
            if(CollectionUtil.isNotEmpty(skuStoreList)){
                skuStoreMaps = skuStoreList.stream().collect(Collectors.toMap(SkuStore::getPriceCode, a -> a,(k1,k2)->k1));
            }

            log.info(LOG_TAG_CHANNEL_PRICE+" 商品id【{}】 商品货号【{}】 获取到sku条数【{}】",spu.getSpuId(),spu.getSpuCode(),skuList.size());
            if(CollectionUtil.isNotEmpty(skuList)){

                List<Sku> r_list = skuList.stream()
                        .filter(sku -> StrUtil.isNotBlank(sku.getChannelName()) && sku.getChannelName().equals(SpuChannelEnums.CHANNEL_R.getCode()))
                        .collect(Collectors.toList());
                List<Sku> nor_list = skuList.stream()
                        .filter(sku -> StrUtil.isNotBlank(sku.getChannelName()) && !sku.getChannelName().equals(SpuChannelEnums.CHANNEL_R.getCode()))
                        .collect(Collectors.toList());
                List<Sku> empty_list = skuList.stream()
                        .filter(sku -> StrUtil.isBlank(sku.getChannelName()))
                        .collect(Collectors.toList());
                int rCount=CollectionUtil.isNotEmpty(r_list)?r_list.size():0;//R渠道总数
                int norCount=CollectionUtil.isNotEmpty(r_list)?nor_list.size():0;//非R渠道总数
                int emptyCount=CollectionUtil.isNotEmpty(r_list)?empty_list.size():0;//空渠道总数
                log.info(LOG_TAG_CHANNEL_PRICE+" 商品id【{}】 商品货号【{}】 获取到sku总数【{}】 R渠道【{}】 非R渠道【{}】 空渠道【{}】",
                        spu.getSpuId(),spu.getSpuCode(),skuList.size(),rCount,norCount,emptyCount);

                //商品spu内全部sku都为R渠道才能进行渠道价设置
                if(rCount<skuList.size()){
                    log.info(LOG_TAG_CHANNEL_PRICE+"商品id【{}】 商品货号【{}】 获取到sku总数【{}】 R渠道【{}】 不满足全部sku为R渠道执行下一个商品校验",
                            spu.getSpuId(),spu.getSpuCode(),skuList.size(),rCount);
                    return;
                }

                //取spu内sku最高折扣等级(并不是款的最高等级)
                Sku maxDiscountSku = skuList.stream().filter(sku -> StrUtil.isNotBlank(sku.getChannelDiscount())).max(Comparator.comparing(Sku::getChannelDiscount)).get();
                String channelDiscount=maxDiscountSku.getChannelDiscount();
                Double discount= NumberUtil.parseDouble(channelDiscount);

                /**
                 * 满足spu内全部sku都为R渠道商品
                 * 1. 门店有库存，取三者最高价：门店库存最高价hp、 电商保护价pp、mp吊牌价*折扣等级(最高折扣)
                 * 2. 门店库无存价：1、取保护价 2、吊牌价折扣价
                 */

                //根据商品sku -> price_code 款分组
                Map<String, Sku> skuMaps = skuList.stream().collect(Collectors.toMap(Sku::getPriceCode, a -> a,(k1,k2)->k1));
                log.info(LOG_TAG_CHANNEL_PRICE+" 商品货号【{}】 原始款分组总数【{}】 款集合【{}】",spu.getSpuCode(),skuMaps.size(),JSON.toJSON(skuMaps.keySet()));
                //剔除保护款：不参与渠道价设置
                if(CollectionUtil.isNotEmpty(skuStoreMaps)){
                    for (Map.Entry<String, SkuStore> entry : skuStoreMaps.entrySet()) {
                        if(skuMaps.containsKey(entry.getKey())){
                            skuMaps.remove(entry.getKey());
                        }
                    }
                }

                log.info(LOG_TAG_CHANNEL_PRICE+" 商品货号【{}】 剔除逻辑后款分组总数【{}】 款集合【{}】",spu.getSpuCode(),skuMaps.size(),JSON.toJSON(skuMaps.keySet()));

                //最新可以设置渠道价sku
                List<Sku> skus=new ArrayList(skuMaps.values());

                /**
                 * 其中一款有库存，其他款统一按照有库存来计算
                 */
                //1.根据商品，获取门店有库存且售价不高于吊牌价（不为官店数据）
                List<SkuStore> skuStoresAll=skuStoreMapper.getChannelPirceSkuStores(spu.getSpuId(),null);
                log.info(LOG_TAG_CHANNEL_PRICE+" 商品关联门店sku数据【{}】",skuStoresAll.size());
                final List<SkuStore> skuStores=new ArrayList<>();
                if(CollectionUtil.isNotEmpty(skuStoresAll)){
                    //获取门店sku最高价行数据
                    SkuStore skuStore = skuStoresAll.stream().max(Comparator.comparing(SkuStore::getPriceFee)).get();
                    log.info(LOG_TAG_CHANNEL_PRICE+" 商品关联门店有库存 门店数据【{}】",(skuStore.getSkuStoreId()+"_"+skuStore.getStoreId()));
                    skuStores.add(skuStore);
                }

                for(Sku sku:skus){
                    Long channelPrice=null;//渠道价
                    String actualDiscount=channelDiscount;
                    Long marketPriceFee=sku.getMarketPriceFee();
                    SkuStore skuStore=null;

                    StringBuilder sb_priceList=new StringBuilder();

                    //根据商品，sku款获取门店sku数据(门店有库存且售价不高于吊牌价)
//                    List<SkuStore> skuStores=skuStoreMapper.getChannelPirceSkuStores(spu.getSpuId(),sku.getPriceCode());
                    log.info(LOG_TAG_CHANNEL_PRICE+" 门店是有库存 门店数据【{}】",skuStores.size());

                    if(CollectionUtil.isNotEmpty(skuStores) && skuStores.size()>0){ //门店有库存，且售价不高于吊牌价

                        log.info(LOG_TAG_CHANNEL_PRICE+" 进入门店有库存价格计算逻辑 门店数据【{}】",skuStores.size());
                        /**
                         * 取三者最高价：门店库存最高价hp、 电商保护价pp、mp吊牌价*折扣等级(最高折扣)
                         */
                        //1.门店库存最高价hp
                        skuStore = skuStores.stream().max(Comparator.comparing(SkuStore::getPriceFee)).get();
                        Long storeHighPrice=skuStore.getPriceFee();
                        //门店有库存最高价同时有pos活动价，如果pos活动价高于吊牌价取吊牌价否则取售价
                        if(Objects.nonNull(skuStore.getActivityPrice()) && skuStore.getActivityPrice()>skuStore.getMarketPriceFee()){
                            storeHighPrice=skuStore.getMarketPriceFee();
                        }
                        sb_priceList.append("storeHighPrice:").append(storeHighPrice);

                        //2.电商保护价pp
                        Long protectSpuPrieceFee=0L;
                        if(CollectionUtil.isNotEmpty(protectSpuMaps) && protectSpuMaps.containsKey(sku.getSpuId())){
                            protectSpuPrieceFee=protectSpuMaps.get(sku.getSpuId()).getProtectPrice();
                        }
                        sb_priceList.append("/").append("protectSpuPrieceFee:").append(protectSpuPrieceFee);

                        //3.mp吊牌价*折扣等级(最高折扣)
//                        Double marketDisPriceFee = marketPriceFee.doubleValue() * discount / 10;
                        Double marketDisPriceFee = marketPriceFee.doubleValue();
                        if(getDiscount(spuCode,channelDiscount)){
                            marketDisPriceFee = marketPriceFee.doubleValue() * discount / 10;
                            sb_priceList.append("/").append("marketDisPriceFee:").append(marketDisPriceFee);
                        }else{
                            sb_priceList.append("/").append("错误折扣取吊牌价marketPriceFee:").append(marketDisPriceFee);
                        }

                        //4. 三者最高价hhp
                        Long hhpPriceFee=Max(storeHighPrice,protectSpuPrieceFee,marketDisPriceFee.longValue());
                        sb_priceList.append("/").append("hhpPriceFee:").append(hhpPriceFee);

                        //5. 判断最高价是否大于吊牌价
                        if(hhpPriceFee>marketPriceFee){ //最高价大于吊牌价 -> 渠道价=吊牌价
                            channelPrice=marketPriceFee;
                        }else{
                            Long marketDis3Price = marketPriceFee * 3 / 10;
                            //5.1 最高价与吊牌价3折判断
                            if(hhpPriceFee<marketDis3Price){ //hhp < 吊牌价*3折 -> 渠道价=吊牌价*3折
                                channelPrice=marketDis3Price;
                                actualDiscount="3";//3折
                            }else{ //渠道价 = 三者最高价hhp
                                channelPrice=hhpPriceFee;
                            }
                        }
                    }else{
                        //门店没有库存（判断是否有电商保护价）
                        if(CollectionUtil.isNotEmpty(protectSpuMaps) && protectSpuMaps.containsKey(sku.getSpuId())){ //有电商保护价
                            log.info(LOG_TAG_CHANNEL_PRICE+" 门店没有库存价格计算逻辑 -> 有电商保护价",skuStores.size());
                            //1.电商保护价pp
                            Long protectSpuPrieceFee=protectSpuMaps.get(sku.getSpuId()).getProtectPrice();
                            sb_priceList.append("protectSpuPrieceFee:").append(protectSpuPrieceFee);

                            //2.mp吊牌价*折扣等级(最高折扣) 如果是0或者折扣等级不规范则不打折
                            Double marketDisPriceFee = marketPriceFee.doubleValue();
                            if(getDiscount(spuCode,channelDiscount)){
                                marketDisPriceFee = marketPriceFee.doubleValue() * discount / 10;
                                sb_priceList.append("/").append("marketDisPriceFee:").append(marketDisPriceFee);
                            }else{
                                sb_priceList.append("/").append("错误折扣取吊牌价marketPriceFee:").append(marketDisPriceFee);
                            }

                            //3. 二者最高价hhp
                            Long hhpPriceFee=Max(protectSpuPrieceFee,marketDisPriceFee.longValue());
                            sb_priceList.append("/").append("hhpPriceFee:").append(hhpPriceFee);

                            //4. 判断最高价是否大于吊牌价
                            if(hhpPriceFee>marketPriceFee){//最高价大于吊牌价 -> 渠道价=吊牌价
                                channelPrice=marketPriceFee;
                            }else{
                                Long marketDis3Price = marketPriceFee * 3 / 10;
                                //4.1 最高价与吊牌价3折判断
                                if(hhpPriceFee<marketDis3Price){ //hhp < 吊牌价*3折 -> 渠道价=吊牌价*3折
                                    channelPrice=marketDis3Price;
                                    actualDiscount="3";//3折
                                }else{ //渠道价 = 三者最高价hhp
                                    channelPrice=hhpPriceFee;
                                }
                            }
                        }else{
                            log.info(LOG_TAG_CHANNEL_PRICE+" 门店没有库存价格计算逻辑 -> 没有电商保护价",skuStores.size());
                            /**
                             * 没有电商保护价：
                             * 1. 无吊牌价 -> 渠道价 = 1099
                             * 2. 有吊牌价 -> 吊牌价与最高折扣等级计算
                             */
                            JSONObject jsonObject=getMarketDiscountPrice(spuCode,channelDiscount,marketPriceFee,channelPrice,actualDiscount);
                            channelPrice=jsonObject.getLong("channelPrice");
                            actualDiscount=jsonObject.getString("actualDiscount");
                            sb_priceList.append("无电商保护价及门店库存价->marketDisPriceFee:").append(channelPrice);
                        }
                    }

                    logger.info(LOG_TAG_CHANNEL_PRICE+" 渠道价为空则不更新 商品货号【{}】 priceCode【{}】 渠道【{}】 折扣等级【{}】 实际折扣等级【{}】 渠道价计算结果【{}】 "
                            ,spuMaps.get(sku.getSpuId()).getSpuCode(),
                            sku.getPriceCode(),
                            sku.getChannelName(),
                            sku.getChannelDiscount(),
                            actualDiscount,
                            channelPrice);

                    //根据 price_code 更新渠道价
                    if(Objects.nonNull(channelPrice)){//更新官店渠道价
                        //更新sku(更新条件: price_code)
                        LambdaUpdateChainWrapper<Sku> skuLambdaUpdateChainWrapper=skuService.lambdaUpdate();
                        skuLambdaUpdateChainWrapper.set(Sku::getPriceFee,channelPrice);
                        skuLambdaUpdateChainWrapper.set(Sku::getChannelPrice,channelPrice);
                        skuLambdaUpdateChainWrapper.set(Sku::getUpdateTime,now);
                        skuLambdaUpdateChainWrapper.eq(Sku::getPriceCode,sku.getPriceCode());
                        skuLambdaUpdateChainWrapper.update();

                        //更新sku_store(更新条件：官店，price_code)
                        LambdaUpdateChainWrapper<SkuStore> skuStoreLambdaUpdateChainWrapper=skuStoreService.lambdaUpdate();
                        skuStoreLambdaUpdateChainWrapper.set(SkuStore::getPriceFee,channelPrice);
                        skuStoreLambdaUpdateChainWrapper.set(SkuStore::getChannelPrice,channelPrice);
                        skuStoreLambdaUpdateChainWrapper.set(SkuStore::getUpdateTime,now);
                        skuStoreLambdaUpdateChainWrapper.eq(SkuStore::getStoreId,Constant.MAIN_SHOP);
                        skuStoreLambdaUpdateChainWrapper.eq(SkuStore::getPriceCode,sku.getPriceCode());
                        skuStoreLambdaUpdateChainWrapper.update();

                        //保存价格日志
                        SpuSkuPriceLog spuSkuPriceLog=new SpuSkuPriceLog();
                        spuSkuPriceLog.setSpuCode(spuMaps.get(sku.getSpuId()).getSpuCode());
                        spuSkuPriceLog.setPriceCode(sku.getPriceCode());
                        spuSkuPriceLog.setPriceFee(sku.getPriceFee());
                        spuSkuPriceLog.setPriceFeeNew(channelPrice);
                        spuSkuPriceLog.setChannelName(sku.getChannelName());
                        spuSkuPriceLog.setDiscount(sku.getChannelDiscount());
                        spuSkuPriceLog.setActualDiscount(actualDiscount);
                        spuSkuPriceLog.setMarketPriceFee(sku.getMarketPriceFee());
                        spuSkuPriceLog.setUpdateTime(now);
                        spuSkuPriceLog.setPriceType(PriceTypeEnums.CHANNEL_PRICE.getCode());
                        spuSkuPriceLog.setToStoreId(1L);
                        spuSkuPriceLog.setType(0);
                        spuSkuPriceLog.setBusinessId(""+System.currentTimeMillis()+ RandomUtil.randomString(IdUtil.objectId(),3));
                        spuSkuPriceLog.setRemarks("设置渠道价sku/官店sku_store-> "+sb_priceList.toString());
                        if(skuStore!=null){
                            spuSkuPriceLog.setStoreSkuId(skuStore.getSkuStoreId());
                            spuSkuPriceLog.setSkuStoreStock(skuStore.getStock());
                            spuSkuPriceLog.setFromStoreId(skuStore.getStoreId());
                            spuSkuPriceLog.setStockType(0);
                        }else{
                            spuSkuPriceLog.setStockType(1);
                        }
                        spuSkuPriceLogs.add(spuSkuPriceLog);

                        SkuPriceLogDTO skuPriceLogDTO=new SkuPriceLogDTO();
                        skuPriceLogDTO.setBusinessId(spuSkuPriceLog.getBusinessId());
                        skuPriceLogDTO.setBusinessType(SkuPriceLogType.APP_BATCH_CHANNEL_PRICE.value());
                        skuPriceLogDTO.setSpuCode(spuCode);
                        skuPriceLogDTO.setPriceCode(sku.getPriceCode());
                        skuPriceLogDTO.setLogType(SkuPriceLogType.APP_BATCH_CHANNEL_PRICE.value());
                        skuPriceLogDTO.setPrice(channelPrice);
                        skuPriceLogDTO.setStoreId(storeVO.getStoreId());
                        skuPriceLogDTO.setStoreCode(storeVO.getStoreCode());
                        skuPriceLogDTO.setUpdateBy(StrUtil.isNotBlank(updateBy)?updateBy:"");
                        skuPriceLogDTO.setUpdateTime(now);
                        skuPriceLogs.add(skuPriceLogDTO);
                    }
                }
            }
        });

        //TODO 改价日志
        if(CollectionUtil.isNotEmpty(spuSkuPriceLogs)){
            log.info(LOG_TAG_CANCEL_CHANNEL_PRICE+"  保存价格日志 【{}】", spuSkuPriceLogs.size());
            spuSkuPriceLogService.saveBatch(spuSkuPriceLogs);

            //TODO 保存价格日志
            skuPriceLogService.savePriceLogs(new ESkuPriceLogDTO(skuPriceLogs,
                    "定时任务设置渠道价"+ DateUtil.format(now, "yyyy-MM-dd HH:mm:ss")));
        }

        log.info(LOG_TAG_CHANNEL_PRICE+" 结束执行商品批量设置渠道价，耗时：{}ms", System.currentTimeMillis() - startTime);
    }


    private JSONObject getMarketDiscountPrice(String spuCode, String channelDiscount, Long marketPriceFee, Long channelPrice, String actualDiscount){
        if(Objects.isNull(marketPriceFee)){//渠道价=1099
            channelPrice=109900L;
            actualDiscount="吊牌价为空 1099";
        }else{

            if(getDiscount(spuCode,channelDiscount)){
                //正常折扣等级
                Double discount= NumberUtil.parseDouble(channelDiscount);
                actualDiscount=discount.toString();
                if(discount<=0 || discount<0.99){//折扣等级小于0不打折，渠道价=吊牌价
                    channelPrice=marketPriceFee;
                }else if(discount<3.0 && discount>0.0){//折扣等级小于3折，设置渠道价=商品吊牌价*默认折扣等级3折
                    channelPrice = marketPriceFee * 3 / 10;
                    actualDiscount="3";
                }else{//正常折扣
                    Double discountPrice = marketPriceFee.doubleValue() * discount / 10;
                    channelPrice=discountPrice.longValue();
                }
            }else{//错误折扣等级，渠道价=吊牌价
                actualDiscount="错误折扣等级不打折取吊牌价";
                channelPrice=marketPriceFee;
            }
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("channelPrice",channelPrice);
        jsonObject.put("actualDiscount",actualDiscount);
        return jsonObject;
    }


    @Override
    public void cancelChannelPriceTask(String spuCodes,String updateBy) {
        log.info(LOG_TAG_CANCEL_CHANNEL_PRICE+" 开始执行商品批量取消渠道价");
        Long startTime=System.currentTimeMillis();

        //价格日志
        Map<String,SpuSkuPriceLog> spuSkuPriceLogMaps=new HashMap<>();

        List<SkuPriceLogDTO> skuPriceLogs=new ArrayList<>();
        StoreVO storeVO=storeFeignClient.findByStoreId(Constant.MAIN_SHOP);
        Date now=new Date();

        //取消sku渠道价(恢复售价=铺货价)
        List<Sku> skuList=skuMapper.getCancelChannelPirceSkus(SpuChannelEnums.CHANNEL_R.getCode());
        log.info(LOG_TAG_CANCEL_CHANNEL_PRICE+" 开始执行商品批量取消渠道价 sku条数【{}】",skuList.size());
        if(CollectionUtil.isNotEmpty(skuList)){
            List<Sku> updateSkus=new ArrayList<>();
            skuList.forEach(sku -> {
                Sku updateSku=new Sku();
                updateSku.setSkuId(sku.getSkuId());
//                updateSku.setPriceFee(sku.getPhPrice());
                if(sku.getProtectPrice()>0){
                    updateSku.setPriceFee(sku.getProtectPrice());
                    updateSku.setPhPrice(sku.getProtectPrice());
                }else{
                    updateSku.setPriceFee(sku.getPhPrice()>0?sku.getPhPrice():sku.getMarketPriceFee());
                }
                updateSku.setChannelPrice(0L);
                updateSku.setUpdateTime(new Date());
                updateSkus.add(updateSku);

                //保存价格日志
                String key=sku.getSpuCode()+sku.getPriceCode();
                if(!spuSkuPriceLogMaps.containsKey(key)){
                    SpuSkuPriceLog spuSkuPriceLog=new SpuSkuPriceLog();
                    spuSkuPriceLog.setSpuCode(sku.getSpuCode());
                    spuSkuPriceLog.setPriceCode(sku.getPriceCode());
                    spuSkuPriceLog.setPriceFee(sku.getPriceFee());
                    spuSkuPriceLog.setPriceFeeNew(updateSku.getPriceFee());
                    spuSkuPriceLog.setUpdateTime(now);
                    spuSkuPriceLog.setPriceType(PriceTypeEnums.CHANNEL_PRICE.getCode());
                    spuSkuPriceLog.setToStoreId(1L);
                    spuSkuPriceLog.setType(1);
                    spuSkuPriceLog.setBusinessId(""+System.currentTimeMillis()+ RandomUtil.randomString(IdUtil.objectId(),3));
                    spuSkuPriceLog.setRemarks("取消渠道价sku");
                    spuSkuPriceLogMaps.put(key,spuSkuPriceLog);

                    //TODO 价格日志
                    SkuPriceLogDTO skuPriceLogDTO=new SkuPriceLogDTO();
                    skuPriceLogDTO.setBusinessId(spuSkuPriceLog.getBusinessId());
                    skuPriceLogDTO.setBusinessType(SkuPriceLogType.APP_CANCEL_CHANNEL_PRICE.value());
                    skuPriceLogDTO.setSpuCode(sku.getSpuCode());
                    skuPriceLogDTO.setPriceCode(sku.getPriceCode());
                    skuPriceLogDTO.setLogType(SkuPriceLogType.APP_CANCEL_CHANNEL_PRICE.value());
                    skuPriceLogDTO.setPrice(updateSku.getPriceFee());
                    skuPriceLogDTO.setStoreId(storeVO.getStoreId());
                    skuPriceLogDTO.setStoreCode(storeVO.getStoreCode());
                    skuPriceLogDTO.setUpdateBy(StrUtil.isNotBlank(updateBy)?updateBy:"");
                    skuPriceLogDTO.setUpdateTime(now);
                    skuPriceLogs.add(skuPriceLogDTO);
                }

            });

            skuService.updateBatchById(updateSkus);
        }
        //取消官店sku_store渠道价(恢复售价=铺货价)
        List<SkuStore> skuStores=skuStoreMapper.getCancelChannelPriceSkuStores(SpuChannelEnums.CHANNEL_R.getCode());
        log.info(LOG_TAG_CANCEL_CHANNEL_PRICE+" 开始执行商品批量取消渠道价 skuStore条数【{}】",skuStores.size());
        if(CollectionUtil.isNotEmpty(skuStores)){
            List<SkuStore> updateSkuStores=new ArrayList<>();
            skuStores.forEach(skuStore -> {
                SkuStore updateSkuStore=new SkuStore();
                updateSkuStore.setSkuStoreId(skuStore.getSkuStoreId());
//                updateSkuStore.setPriceFee(skuStore.getPhPrice());
                if(skuStore.getProtectPrice()>0){
                    updateSkuStore.setPriceFee(skuStore.getProtectPrice());
                    updateSkuStore.setPhPrice(skuStore.getProtectPrice());
                }else{
                    updateSkuStore.setPriceFee(skuStore.getPhPrice()>0?skuStore.getPhPrice():skuStore.getMarketPriceFee());
                }
                updateSkuStore.setChannelPrice(0L);
                updateSkuStore.setUpdateTime(new Date());
                updateSkuStores.add(updateSkuStore);

                //保存价格日志
                String key=skuStore.getSpuCode()+skuStore.getPriceCode();
                if(!spuSkuPriceLogMaps.containsKey(key)){
                    SpuSkuPriceLog spuSkuPriceLog=new SpuSkuPriceLog();
                    spuSkuPriceLog.setSpuCode(skuStore.getSpuCode());
                    spuSkuPriceLog.setPriceCode(skuStore.getPriceCode());
                    spuSkuPriceLog.setPriceFee(skuStore.getPriceFee());
                    spuSkuPriceLog.setPriceFeeNew(updateSkuStore.getPriceFee());
                    spuSkuPriceLog.setUpdateTime(now);
                    spuSkuPriceLog.setPriceType(PriceTypeEnums.CHANNEL_PRICE.getCode());
                    spuSkuPriceLog.setToStoreId(1L);
                    spuSkuPriceLog.setType(1);
                    spuSkuPriceLog.setRemarks("取消渠道价sku_store");
                    spuSkuPriceLogMaps.put(key,spuSkuPriceLog);
                }
            });

            skuStoreService.updateBatchById(updateSkuStores);
        }

        //TODO 改价日志
        if(CollectionUtil.isNotEmpty(spuSkuPriceLogMaps)){
            List<SpuSkuPriceLog> spuSkuPriceLogs=new ArrayList<>(spuSkuPriceLogMaps.values());
            log.info(LOG_TAG_CANCEL_CHANNEL_PRICE+"  保存价格日志 【{}】", spuSkuPriceLogs.size());
            spuSkuPriceLogService.saveBatch(spuSkuPriceLogs);

            //TODO 保存价格日志
            skuPriceLogService.savePriceLogs(new ESkuPriceLogDTO(skuPriceLogs,
                    "定时任务取消渠道价"+ DateUtil.format(now, "yyyy-MM-dd HH:mm:ss")));
        }

        log.info(LOG_TAG_CANCEL_CHANNEL_PRICE+"  结束执行商品批量取消渠道价，耗时：{}ms", System.currentTimeMillis() - startTime);
    }


    private boolean getDiscount(String spuCode,String discount){
        logger.info(LOG_TAG_CHANNEL_PRICE+" 校验折扣1---> 商品货号【{}】 渠道折扣等级【{}】",spuCode,discount);
        if(StrUtil.isBlank(discount)){
            logger.info(LOG_TAG_CHANNEL_PRICE+"{}","折扣等级为空");
            return false;
        }
        if (!PrincipalUtil.isMaximumOfTwoDecimal(discount) && !discount.equals("0")) {
            logger.error(LOG_TAG_CHANNEL_PRICE+"{}","最多是保留2位小数的数值");
            return false;
        }
        if(NumberUtil.parseDouble(discount)<0 || NumberUtil.parseDouble(discount)<0.99){
            logger.error(LOG_TAG_CHANNEL_PRICE+"{}","小于0，默认为0");
            return false;
        }
        logger.info(LOG_TAG_CHANNEL_PRICE+" 校验折扣2---> 商品货号【{}】 渠道折扣等级【{}】",spuCode,discount);
        return true;
    }

    public static Long Max(Long a,Long b,Long c){
        Long temp = (a>b) ? a : b;
        Long max = (temp>c) ? temp : c;
        logger.info(LOG_TAG_CHANNEL_PRICE+" 三个取最高价 价格1->【{}】 价格2->【{}】 价格3->【{}】 最高价->【{}】",a,b,c,max);
        return max;
    }

    public static Long Max(Long a,Long b){
        Long max = (a>b) ? a : b;
        logger.info(LOG_TAG_CHANNEL_PRICE+" 二个取最高价 价格1->【{}】 价格2->【{}】 最高价->【{}】",a,b,max);
        return max;
    }

    public static void main(String[] args){
        Long marketPriceFee = 39900L;
        Long priceFee = 9900L;
        Double discount=2.4;
        Double marketDisPriceFee = marketPriceFee.doubleValue() * discount / 10;
        System.out.println("marketPriceFee:"+marketPriceFee+"---priceFee:"+priceFee+"---marketDisPriceFee:"+marketDisPriceFee.longValue());
        if(priceFee<marketDisPriceFee){
            System.out.println("当前订单异常，无法提交订单请联系客服处理！");
        }
    }


}
