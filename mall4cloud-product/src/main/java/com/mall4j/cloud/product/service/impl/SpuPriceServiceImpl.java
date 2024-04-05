package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.product.enums.SkuPriceLogType;
import com.mall4j.cloud.api.product.dto.ESkuPriceLogDTO;
import com.mall4j.cloud.api.product.dto.ErpSkuPriceDTO;
import com.mall4j.cloud.api.product.dto.ErpSkuStockDTO;
import com.mall4j.cloud.api.product.dto.SkuPriceLogDTO;
import com.mall4j.cloud.api.product.enums.PriceTypeEnums;
import com.mall4j.cloud.api.product.enums.SpuChannelEnums;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.mapper.SkuStoreMapper;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.mapper.SpuStoreMapper;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.SkuPriceLogService;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SkuStoreService;
import com.mall4j.cloud.product.service.SpuPriceService;
import com.mall4j.cloud.product.vo.SpuSkuVo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RefreshScope
public class SpuPriceServiceImpl implements SpuPriceService {
    @Autowired
    private SpuServiceImpl spuService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SkuStoreService skuStoreService;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Resource
    private SpuMapper spuMapper;
    @Autowired
    private SpuStoreMapper spuStoreMapper;
    @Autowired
    private SkuPriceLogService skuPriceLogService;
    @Value("${mall4cloud.product.savePushPSLog:false}")
    @Setter
    private Boolean savePushPSLog=false;

    @Override
    public void skuStoreSync(List<ErpSkuPriceDTO> skuPriceDTOList,Date now) {
        //获取门店集合
        List<ErpSkuPriceDTO> skuPriceStore = skuPriceDTOList.stream()
                .filter(skuPriceDTO -> StrUtil.isNotBlank(skuPriceDTO.getStoreCode()))
                .collect(Collectors.toList());

        if(CollectionUtil.isEmpty(skuPriceStore)){
            log.info("----价格同步，门店库存更新--门店集合为空");
            return;
        }
        List<String> storeCodeList = skuPriceStore.stream().map(ErpSkuPriceDTO::getStoreCode).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(storeCodeList)){//去重复
            storeCodeList=new ArrayList<>(new HashSet<>(storeCodeList));
        }
        //获取门店信息
        List<StoreCodeVO> byStoreCodes = storeFeignClient.findByStoreCodes(storeCodeList);

        Map<String, Long> storeCodeMap = byStoreCodes.stream().collect(Collectors.toMap(StoreCodeVO::getStoreCode,StoreCodeVO::getStoreId,
                (storeCodeVO1,storeCodeVO2) -> storeCodeVO2));

        Map<String,SkuStore> skuStoresInsertMap=new HashMap<>();
        skuPriceDTOList.forEach(erpSkuStockDTO -> {
            //sku基础数据
            List<Sku> skuList=skuService.getSkuByPriceCode(erpSkuStockDTO.getPriceCode());
            //获取官店库存
            Long storeId = storeCodeMap.get(erpSkuStockDTO.getStoreCode());
            if (Objects.nonNull(storeId)) {
                if(CollectionUtil.isNotEmpty(skuList)){
                    //官店
                    List<SkuStore> skuStoresMainShop=getSkuStores(erpSkuStockDTO.getPriceCode(),Constant.MAIN_SHOP);
                    Map<String, SkuStore> skuStoresMainShopMap = skuStoresMainShop.stream()
                            .collect(Collectors.toMap(SkuStore::getSkuCode,
                                    storeCodeVO -> storeCodeVO,(k1, k2)->k1));
                    //门店
                    List<SkuStore> skuStores=getSkuStores(erpSkuStockDTO.getPriceCode(),storeId);
                    Map<String, SkuStore> skuStoresMap = skuStores.stream()
                            .collect(Collectors.toMap(SkuStore::getSkuCode,
                                    storeCodeVO -> storeCodeVO,(k1, k2)->k1));

                    skuList.stream().forEach(sku -> {
                        if(!skuStoresMainShopMap.containsKey(sku.getSkuCode())
                                && !skuStoresInsertMap.containsKey(sku.getSkuCode()+Constant.MAIN_SHOP)){
                            //官店
                            SkuStore skuStoreMainShop = new SkuStore();
                            BeanUtils.copyProperties(sku, skuStoreMainShop);
                            skuStoreMainShop.setStoreId(Constant.MAIN_SHOP);
                            skuStoreMainShop.setStock(0);
                            skuStoreMainShop.setCreateTime(now);
                            skuStoreMainShop.setUpdateTime(now);
                            skuStoreMainShop.setEm("ps");
                            skuStoresInsertMap.put(sku.getSkuCode()+Constant.MAIN_SHOP,skuStoreMainShop);
                        }
                        //门店
                        if(!skuStoresMap.containsKey(sku.getSkuCode())
                                && !skuStoresInsertMap.containsKey(sku.getSkuCode()+storeId)){
                            //门店
                            SkuStore skuStore = new SkuStore();
                            BeanUtils.copyProperties(sku, skuStore);
                            skuStore.setStoreId(storeId);
                            skuStore.setStock(0);
                            skuStore.setPriceFee(sku.getMarketPriceFee());
                            skuStore.setPhPrice(0L);
                            skuStore.setChannelPrice(0L);
                            skuStore.setActivityPrice(0L);
                            skuStore.setCreateTime(now);
                            skuStore.setUpdateTime(now);
                            skuStore.setEm("ps");
                            skuStoresInsertMap.put(sku.getSkuCode()+storeId,skuStore);
                        }
                    });
                }
            }
        });
        if(CollectionUtil.isNotEmpty(skuStoresInsertMap)){
            List<SkuStore> skuStoresInsert=new ArrayList<>(skuStoresInsertMap.values());
            skuStoreService.saveBatch(skuStoresInsert);
        }

    }


    /**
     * 市场价（吊牌价）
     * 修订日志：2022-08-05 13:54 吊牌价统一只推送官店(门店编号为空即官店)，同时需要更新门店吊牌价(吊牌价统一以官店为基准)
     */
    @Override
    public void marketPriceSync(List<ErpSkuPriceDTO> skuPriceDTOList,Date now) {
        log.info("erp 价格同步处理: 市场价（吊牌价）");
        //处理门店数据为空的情况（为空即为官店）
        List<ErpSkuPriceDTO> skuPrice = skuPriceDTOList.stream()
                .filter(skuPriceDTO -> StrUtil.isBlank(skuPriceDTO.getStoreCode()))
                .collect(Collectors.toList());

        Map<String,String> spuCodeMps=new HashMap<>();
        List<SkuPriceLogDTO> skuPriceLogs=new ArrayList();

        //更新sku 的市场价(吊牌价)
        skuPrice.forEach(skuPriceDTO -> {
            //商品货号
            String spuCode=skuPriceDTO.getPriceCode().split("-").length>1?skuPriceDTO.getPriceCode().substring(0, skuPriceDTO.getPriceCode().indexOf("-")):skuPriceDTO.getPriceCode();
            if(!spuCodeMps.containsKey(spuCode)){
                spuCodeMps.put(spuCode,spuCode);
            }
            //更新官店吊牌价 -------start----
            skuService.lambdaUpdate()
                    .setSql("past_market_price_fee=market_price_fee")//保留上一次历史吊牌价
                    .set(Objects.nonNull(skuPriceDTO.getPrice()), Sku::getMarketPriceFee, skuPriceDTO.getPrice())
                    .set(Sku::getUpdateTime, now)
                    .eq(Sku::getPriceCode, skuPriceDTO.getPriceCode())
                    .update();
            skuStoreService.lambdaUpdate()
                    .setSql("past_market_price_fee=market_price_fee")//保留上一次历史吊牌价
                    .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getMarketPriceFee, skuPriceDTO.getPrice())
                    .set(SkuStore::getUpdateTime, now)
//                    .eq(SkuStore::getStatus, 1)
                    .eq(SkuStore::getStoreId, Constant.MAIN_SHOP)
                    .eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode())
                    .update();
            //更新官店吊牌价 -------end----

            //更新门店吊牌价------start------
            skuStoreService.lambdaUpdate()
                    .setSql("past_market_price_fee=market_price_fee")//保留上一次历史吊牌价
                    .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getMarketPriceFee, skuPriceDTO.getPrice())
                    .set(SkuStore::getUpdateTime, now)
                    .eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode())
//                    .eq(SkuStore::getStatus, 1)
                    .ne(SkuStore::getStoreId, Constant.MAIN_SHOP)//过滤官店
                    .update();
            //更新门店吊牌价------end------

            //推送吊牌价重算门店有pos价3折兜底价（用最新推送的吊牌价计算3折兜底）--------start--------------

            recalculationStorePosPrice(skuPriceDTO,now,null,skuPriceLogs);

            //推送吊牌价重算门店有pos价3折兜底价（用最新推送的吊牌价计算3折兜底）--------end--------------

        });


        //吊牌价--门店
        List<ErpSkuPriceDTO> skuPrice_store = skuPriceDTOList.stream()
                .filter(skuPriceDTO -> StrUtil.isNotBlank(skuPriceDTO.getStoreCode()))
                .collect(Collectors.toList());

        //获取门店数据
        List<String> storeCodeList = skuPriceDTOList.stream().filter(erpSkuPriceDTO -> StrUtil.isNotBlank(erpSkuPriceDTO.getStoreCode())).map(ErpSkuPriceDTO::getStoreCode).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(storeCodeList)){
            storeCodeList=new ArrayList<>(new HashSet<>(storeCodeList));//去重复
            List<StoreCodeVO> byStoreCodes = storeFeignClient.findByStoreCodes(storeCodeList);

            Map<String, Long> storeCodeMap = byStoreCodes.stream().collect(Collectors.toMap(StoreCodeVO::getStoreCode,StoreCodeVO::getStoreId,
                    (storeCodeVO1,storeCodeVO2) -> storeCodeVO2));

            for (ErpSkuPriceDTO skuPriceDTO :
                    skuPrice_store) {
                Long storeId = storeCodeMap.get(skuPriceDTO.getStoreCode());
                if (Objects.isNull(storeId)) {
                    skuPriceDTO.setRemark("门店不存在");
                    log.error("门店不存在");
                    continue;
                }
                skuStoreService.lambdaUpdate()
                        .setSql("past_market_price_fee=market_price_fee")//保留上一次历史吊牌价
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getMarketPriceFee, skuPriceDTO.getPrice())
                        .set(SkuStore::getUpdateTime, now)
                        .eq(SkuStore::getStoreId, storeId)
//                        .eq(SkuStore::getStatus, 1)
                        .eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode())
                        .update();

                //推送吊牌价重算门店有pos价3折兜底价（用最新推送的吊牌价计算3折兜底）--------start--------------

                recalculationStorePosPrice(skuPriceDTO,now,storeId,skuPriceLogs);

                //推送吊牌价重算门店有pos价3折兜底价（用最新推送的吊牌价计算3折兜底）--------end--------------
            }
        }

        //更新商品主数据吊牌价
        try {
            if(CollectionUtil.isNotEmpty(spuCodeMps)){
                List<String> spuCodes=new ArrayList<>(spuCodeMps.values());
                List<SpuSkuVo> list=spuMapper.listSpuMarketPrice(spuCodes);
                if(CollectionUtil.isNotEmpty(list)){

                    List<Spu> updateSpus=new ArrayList<>();
                    List<SpuStore> updateSpuStore=new ArrayList<>();
                    list.forEach(spuSkuVo -> {
                        Spu spu=new Spu();
                        spu.setSpuId(spuSkuVo.getSpuId());
                        spu.setMarketPriceFee(spuSkuVo.getSkuMarketPriceFee());
                        updateSpus.add(spu);

                        SpuStore spuStore=new SpuStore();
                        spuStore.setSpuId(spuSkuVo.getSpuId());
                        spuStore.setStoreId(Constant.MAIN_SHOP);
                        spuStore.setMarketPriceFee(spuSkuVo.getSkuMarketPriceFee());
                        spuStore.setUpdateTime(new Date());
                        updateSpuStore.add(spuStore);
                    });

                    spuService.updateBatchById(updateSpus);

                    spuStoreMapper.updateBatchPriceFee(updateSpuStore);
                }
            }
        }catch (Exception e){
            log.info("erp 价格同步处理:吊牌价 商品spu吊牌价处理异常 {} {}",e,e.getMessage());
        }

        //TODO 价格日志触发
        if(CollectionUtil.isNotEmpty(skuPriceLogs)){
            //TODO 保存价格日志
            skuPriceLogService.savePriceLogs(new ESkuPriceLogDTO(skuPriceLogs,
                    "同步吊牌价重算门店pos价"+ DateUtil.format(now, "yyyy-MM-dd HH:mm:ss")));
        }
    }

    /**
     * 推送吊牌价重算门店有pos价3折兜底价（用最新推送的吊牌价计算3折兜底）
     * @param skuPriceDTO
     * @param now
     */
    private void recalculationStorePosPrice(ErpSkuPriceDTO skuPriceDTO,Date now,Long storeId,List<SkuPriceLogDTO> skuPriceLogs){
        LambdaQueryWrapper<SkuStore> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(SkuStore::getPriceCode,skuPriceDTO.getPriceCode());//
        if(Objects.nonNull(storeId)){
            lambdaQueryWrapper.eq(SkuStore::getStoreId, storeId);//门店
        }else{
            lambdaQueryWrapper.ne(SkuStore::getStoreId, Constant.MAIN_SHOP);//过滤官店
        }
        lambdaQueryWrapper.gt(SkuStore::getActivityPrice,0);//有pos价
        lambdaQueryWrapper.le(SkuStore::getProtectPrice,0);//没有保护价
        List<SkuStore> skuStores=skuStoreService.list(lambdaQueryWrapper);

        if(CollectionUtil.isNotEmpty(skuStores)){
            skuStores.forEach(skuStore -> {
                if(skuStore.getProtectPrice()>0){
                    return;
                }
                //当前价格
                Long priceFee=skuStore.getPriceFee();
                //用最新推送的吊牌价计算3折兜底
                skuStore.setMarketPriceFee(skuPriceDTO.getPrice().longValue());
                if(skuStore.getActivityPrice()>skuStore.getMarketPriceFee()){//pos价高于吊牌价，修改售价为吊牌价
                    priceFee=skuStore.getMarketPriceFee();
                    skuPriceDTO.setRemark("pos价高于吊牌价更新为吊牌价:"+skuStores.get(0).getMarketPriceFee());
                }else{
                    priceFee = getStPriceFee(skuStore,skuStore.getActivityPrice().intValue());
                    skuPriceDTO.setRemark("更新为pos价");
                }
                if(!skuStore.getPriceFee().toString().equals(priceFee.toString())){
                    skuStoreService.lambdaUpdate()
                            .eq(SkuStore::getSkuStoreId,skuStore.getSkuStoreId())
//                            .eq(SkuStore::getStatus,1)
                            .set(SkuStore::getPriceFee, priceFee)
                            .set(SkuStore::getUpdateTime, now)
                            .update();

                    //TODO 保存价格日志
                    String spuCode=skuPriceDTO.getPriceCode().split("-").length>1?skuPriceDTO.getPriceCode().substring(0, skuPriceDTO.getPriceCode().indexOf("-")):skuPriceDTO.getPriceCode();
                    SkuPriceLogDTO skuPriceLogDTO=new SkuPriceLogDTO();
                    skuPriceLogDTO.setSpuCode(spuCode);
                    skuPriceLogDTO.setPriceCode(skuPriceDTO.getPriceCode());
                    skuPriceLogDTO.setSkuCode(skuStore.getSkuCode());
                    skuPriceLogDTO.setLogType(SkuPriceLogType.ERP_PUSH_MARKET_PRICE_RELSTORE_POS.value());
                    skuPriceLogDTO.setPrice(priceFee);
                    skuPriceLogDTO.setOldPrice(skuStore.getPriceFee());
                    skuPriceLogDTO.setStoreId(skuStore.getStoreId());
                    skuPriceLogDTO.setRemarks(skuPriceDTO.getRemark());
                    skuPriceLogDTO.setUpdateTime(now);
                    skuPriceLogDTO.setUpdateBy("同步吊牌价重算门店pos价");
                    skuPriceLogs.add(skuPriceLogDTO);
                }
            });
        }
    }

    /**
     * 活动价
     */
    @Override
    public void activityPriceSync(List<ErpSkuPriceDTO> skuPriceDTOList,Date now) {
        log.info("erp 价格同步处理: 活动价");

        //处理门店数据为空的情况（为空即为官店）
//        List<ErpSkuPriceDTO> skuPrice = skuPriceDTOList.stream()
//                .filter(skuPriceDTO -> StrUtil.isBlank(skuPriceDTO.getStoreCode()))
//                .collect(Collectors.toList());

        //更新sku的活动价
//        skuPrice.forEach(skuPriceDTO -> {
//            skuService.lambdaUpdate()
//                    .set(Objects.nonNull(skuPriceDTO.getPrice()), Sku::getPriceFee, skuPriceDTO.getPrice())
//                    .set(Sku::getUpdateTime, now)
//                    .eq(Sku::getPriceCode, skuPriceDTO.getPriceCode())
//                    .update();
//            LambdaUpdateChainWrapper<SkuStore> lambdaUpdateChainWrapper=skuStoreService.lambdaUpdate();
//            //需要根据storeId,priceCode 查sku_store表，这个应该会查出来多条数据()。判断为null为o更新售价为活动价
//            if(getProtectPrice(Constant.MAIN_SHOP,skuPriceDTO.getPriceCode())<=0){
//                lambdaUpdateChainWrapper.set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getPriceFee, skuPriceDTO.getPrice());
//            }
//            lambdaUpdateChainWrapper.set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getActivityPrice, skuPriceDTO.getPrice());
//            lambdaUpdateChainWrapper.set(SkuStore::getUpdateTime, now);
//            lambdaUpdateChainWrapper.eq(SkuStore::getStoreId, Constant.MAIN_SHOP);
//            lambdaUpdateChainWrapper.eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode());
//            lambdaUpdateChainWrapper.update();
//
//        });

        //活动价只限门店
        List<ErpSkuPriceDTO> skuPrice_store = skuPriceDTOList.stream()
                .filter(skuPriceDTO -> StrUtil.isNotBlank(skuPriceDTO.getStoreCode()))
                .collect(Collectors.toList());

        //获取门店数据
        List<String> storeCodeList = skuPriceDTOList.stream().map(ErpSkuPriceDTO::getStoreCode).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(storeCodeList)){
            storeCodeList=new ArrayList<>(new HashSet<>(storeCodeList));
        }
        List<StoreCodeVO> byStoreCodes = storeFeignClient.findByStoreCodes(storeCodeList);

        Map<String, Long> storeCodeMap = byStoreCodes.stream().collect(Collectors.toMap(StoreCodeVO::getStoreCode,StoreCodeVO::getStoreId,
                (storeCodeVO1,storeCodeVO2) -> storeCodeVO2));

        //更新门店sku活动价及售价(售价更新需要判断是否有活动价，存在不更新呢)
        for (ErpSkuPriceDTO skuPriceDTO :
                skuPrice_store) {
            Long storeId = storeCodeMap.get(skuPriceDTO.getStoreCode());
            if (Objects.isNull(storeId)) {
                skuPriceDTO.setRemark("门店不存在");
                continue;
            }
            if(storeId==Constant.MAIN_SHOP){
                skuPriceDTO.setRemark("官店不处理活动价");
                continue;
            }
            skuPriceDTO.setStoreId(storeId);
            List<SkuStore> skuStores=getSkuStores(skuPriceDTO.getPriceCode(),storeId);
            //设置活动价
            if(skuPriceDTO.getPrice()>0 && CollectionUtil.isNotEmpty(skuStores)){
                //当前推送的价格是否为吊牌价的30%: 大于30%取活动价 小于30%取30%价格
                Long storePriceFee = getStorePriceFee(skuStores,skuPriceDTO.getPrice());
                LambdaUpdateChainWrapper<SkuStore> lambdaUpdateChainWrapper=skuStoreService.lambdaUpdate();
                lambdaUpdateChainWrapper.set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getActivityPrice, skuPriceDTO.getPrice());

                //没有保护价，推送pos活动价不高于吊牌价更新售价
                Long protectPrice=getProtectPrice(storeId,skuPriceDTO.getPriceCode());
                if(protectPrice<=0 && skuPriceDTO.getPrice()<=skuStores.get(0).getMarketPriceFee()){
                    skuPriceDTO.setRemark("更新为pos价");
                    lambdaUpdateChainWrapper.set(Objects.nonNull(storePriceFee), SkuStore::getPriceFee, storePriceFee);
                }else if(skuPriceDTO.getPrice()>skuStores.get(0).getMarketPriceFee()){ //pos价高于吊牌价，修改售价为吊牌价
                    skuPriceDTO.setRemark("pos价高于吊牌价更新为吊牌价:"+skuStores.get(0).getMarketPriceFee());
                    lambdaUpdateChainWrapper.set(Objects.nonNull(storePriceFee), SkuStore::getPriceFee, skuStores.get(0).getMarketPriceFee());
                }else{
                    skuPriceDTO.setRemark("有保护价或pos价高于吊牌价不更新pos价->保护价:"+protectPrice+"/吊牌价:"+skuStores.get(0).getMarketPriceFee());
                }
                lambdaUpdateChainWrapper.set(SkuStore::getUpdateTime, now);
                lambdaUpdateChainWrapper.eq(SkuStore::getStoreId, storeId);
//                lambdaUpdateChainWrapper.eq(SkuStore::getStatus, 1);
                lambdaUpdateChainWrapper.eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode());
                lambdaUpdateChainWrapper.update();
            }

            //取消活动（优先取保护价，保护价没有取吊牌价，吊牌价没有999，同时sku_store表活动价=0）
            if(skuPriceDTO.getPrice()==0 && CollectionUtil.isNotEmpty(skuStores)){
                SkuStore skuStore=skuStores.get(0);
                Long priceFee=skuStore.getProtectPrice();//优先取保护价
                if(priceFee==null || priceFee<=0){
                    priceFee=skuStore.getMarketPriceFee();//保护价为空或者为0取吊牌价
                }
                if(priceFee==null || priceFee<=0){
                    priceFee=999L;//吊牌价为空或者为0取吊牌价
                }
                LambdaUpdateChainWrapper<SkuStore> lambdaUpdateChainWrapper=skuStoreService.lambdaUpdate();
                lambdaUpdateChainWrapper.set(SkuStore::getActivityPrice, 0);//活动价
                lambdaUpdateChainWrapper.set(SkuStore::getPriceFee, priceFee);//售价
                lambdaUpdateChainWrapper.set(SkuStore::getPhPrice, priceFee);//售价
                lambdaUpdateChainWrapper.set(SkuStore::getUpdateTime, now);
//                lambdaUpdateChainWrapper.eq(SkuStore::getStatus, 1);
                lambdaUpdateChainWrapper.eq(SkuStore::getStoreId, storeId);
                lambdaUpdateChainWrapper.eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode());
                lambdaUpdateChainWrapper.update();
            }
        }
    }

    /**
     * 保护价
     */
    @Override
    public void protectPriceSync(List<ErpSkuPriceDTO> skuPriceDTOList,Date now) {
        log.info("erp 价格同步处理: 保护价");

        //设置保护价
        List<ErpSkuPriceDTO> addProtectPriceDTOList = skuPriceDTOList.stream()
                .filter(skuPriceDTO -> skuPriceDTO.getPriceType()==2 && skuPriceDTO.getPrice()>0)
                .collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(addProtectPriceDTOList)){

            log.info("erp 价格同步处理: 设置保护价");

            addProtectPriceDTOList.forEach(skuPriceDTO -> {

                //更新主表sku price_fee(售价)为保护价
                skuService.lambdaUpdate()
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), Sku::getPriceFee, skuPriceDTO.getPrice())
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), Sku::getProtectPrice, skuPriceDTO.getPrice())
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), Sku::getPhPrice, skuPriceDTO.getPrice())
                        .set(Sku::getUpdateTime, now)
                        .eq(Sku::getPriceCode, skuPriceDTO.getPriceCode())
                        .update();
                //更新官店sku_store price_fee售价 protect_fee保护价
                skuStoreService.lambdaUpdate()
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getPriceFee, skuPriceDTO.getPrice())
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getProtectPrice, skuPriceDTO.getPrice())
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getPhPrice, skuPriceDTO.getPrice())
                        .set(SkuStore::getUpdateTime, now)
//                        .eq(SkuStore::getStatus, 1)
                        .eq(SkuStore::getStoreId, Constant.MAIN_SHOP)
                        .eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode())
                        .update();

                //更新商品主表spu price_fee售价为保护价
                String name = skuPriceDTO.getProductCode();
                String spuCode = name.split("-").length>1?name.substring(0, name.indexOf("-")):name;
                spuService.lambdaUpdate()
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), Spu::getPriceFee, skuPriceDTO.getPrice())
                        .set(Spu::getUpdateTime, now)
                        .eq(Spu::getSpuCode, spuCode)
                        .update();

                //更新所有门店sku_store price_fee售价 protect_fee保护价
                skuStoreService.lambdaUpdate()
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getPriceFee, skuPriceDTO.getPrice())
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getProtectPrice, skuPriceDTO.getPrice())
                        .set(Objects.nonNull(skuPriceDTO.getPrice()), SkuStore::getPhPrice, skuPriceDTO.getPrice())
                        .set(SkuStore::getUpdateTime, now)
//                        .eq(SkuStore::getStatus, 1)
                        .eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode())
                        .ne(SkuStore::getStoreId, Constant.MAIN_SHOP)//过滤官店
                        .update();


            });
        }


        //取消保护价
        List<ErpSkuPriceDTO> cancelProtectPriceDTOList = skuPriceDTOList.stream()
                .filter(skuPriceDTO -> skuPriceDTO.getPriceType()==2 && skuPriceDTO.getPrice()==0)
                .collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(cancelProtectPriceDTOList)){

            log.info("erp 价格同步处理: 取消保护价");

            //处理官店
            cancelProtectPriceDTOList.forEach(skuPriceDTO -> {

                Long priceFree=null;

                rebackSkuPiceFee(skuPriceDTO,priceFree);
                //
                rebackSkuStoreMainShopPiceFee(skuPriceDTO);
                //
                if(Objects.nonNull(priceFree)){
                    String name = skuPriceDTO.getProductCode();
                    String spuCode = name.split("-").length>1?name.substring(0, name.indexOf("-")):name;
                    spuService.lambdaUpdate()
                            .set(Objects.nonNull(priceFree), Spu::getPriceFee, priceFree)
                            .set(Spu::getUpdateTime, now)
                            .eq(Spu::getSpuCode, spuCode)
                            .update();
                }

            });

            //处理门店
            cancelProtectPriceDTOList.forEach(skuPriceDTO -> {
                //根据推送的priceCode获取所有sku_store数据
                List<SkuStore> skuStores=skuStoreService.lambdaQuery()
                        .eq(SkuStore::getPriceCode,skuPriceDTO.getPriceCode())
                        .ne(SkuStore::getStoreId, Constant.MAIN_SHOP)//过滤官店
                        .list();

                List<SkuStore> updateSkuStores=skuStores.stream().filter(item->item.getProtectPrice()>0).collect(Collectors.toList());
                if(CollectionUtil.isEmpty(updateSkuStores)){
                    return;
                }

                //根据门店分组
                Map<Long, SkuStore> storeCodeMap = skuStores.stream().collect(Collectors.toMap(SkuStore::getStoreId, storeCodeVO -> storeCodeVO,(k1, k2)->k1));
                skuStores=new ArrayList<>(storeCodeMap.values());

                if(CollectionUtil.isNotEmpty(skuStores)){
                    skuStores.forEach(skuStore -> {
                        LambdaUpdateChainWrapper<SkuStore> lambdaUpdateChainWrapper=skuStoreService.lambdaUpdate();
                        //判断是否具有活动价
                        if(skuStore.getActivityPrice()<=0){
                            //没有活动价将marketPrice  更新 至priceFee
                            lambdaUpdateChainWrapper.set(SkuStore::getPriceFee, skuStore.getMarketPriceFee());
                            lambdaUpdateChainWrapper.set(SkuStore::getPhPrice, skuStore.getMarketPriceFee());
                        }else{
                            //活动价是否为吊牌价的30%: 大于30%取活动价 小于30%取30%价格
                            Long storePriceFee = getStPriceFee(skuStore,null);
                            lambdaUpdateChainWrapper.set(Objects.nonNull(storePriceFee),SkuStore::getPriceFee, storePriceFee);
                            lambdaUpdateChainWrapper.set(Objects.nonNull(storePriceFee),SkuStore::getPhPrice, storePriceFee);
                        }
                        lambdaUpdateChainWrapper.set(SkuStore::getProtectPrice, 0);
                        lambdaUpdateChainWrapper.set(SkuStore::getUpdateTime, now);
                        lambdaUpdateChainWrapper.gt(SkuStore::getProtectPrice, 0);//过滤没有保护价不修改
//                        lambdaUpdateChainWrapper.eq(SkuStore::getStatus, 1);
                        lambdaUpdateChainWrapper.eq(SkuStore::getStoreId, skuStore.getStoreId());
                        lambdaUpdateChainWrapper.eq(SkuStore::getPriceCode, skuPriceDTO.getPriceCode());
                        lambdaUpdateChainWrapper.update();
                    });
                }
            });
        }
    }

    @Override
    public void savePirceLog(List<ErpSkuPriceDTO> skuPriceDTOList,Date now) {
        log.info("savePirceLog--> savePushPSLog:{}",savePushPSLog);
        if(!savePushPSLog){
            return;
        }
        if(CollectionUtil.isNotEmpty(skuPriceDTOList)){
            List<SkuPriceLogDTO> skuPriceLogs=new ArrayList<>();
            skuPriceDTOList.forEach(skuPriceDTO -> {
                String spuCode=skuPriceDTO.getPriceCode().split("-").length>1?skuPriceDTO.getPriceCode().substring(0, skuPriceDTO.getPriceCode().indexOf("-")):skuPriceDTO.getPriceCode();
                SkuPriceLogDTO skuPriceLogDTO=new SkuPriceLogDTO();
                skuPriceLogDTO.setSpuCode(spuCode);
                skuPriceLogDTO.setPriceCode(skuPriceDTO.getPriceCode());
                skuPriceLogDTO.setLogType(skuPriceDTO.getPriceType());
                skuPriceLogDTO.setPrice(skuPriceDTO.getPrice().longValue());
                skuPriceLogDTO.setStoreId(skuPriceDTO.getStoreId());
                skuPriceLogDTO.setStoreCode(skuPriceDTO.getStoreCode());
                skuPriceLogDTO.setRemarks(skuPriceDTO.getRemark());
                skuPriceLogDTO.setUpdateTime(now);
                skuPriceLogDTO.setUpdateBy("中台");
                skuPriceLogs.add(skuPriceLogDTO);
            });
            //TODO 保存价格日志
            skuPriceLogService.savePriceLogs(new ESkuPriceLogDTO(skuPriceLogs,
                    "中台推送价格"+ DateUtil.format(now, "yyyy-MM-dd HH:mm:ss")));
        }
    }

    @Override
    public void saveStockLog(List<ErpSkuStockDTO> skuStockDTOS, Date now) {
        log.info("saveStockLog--> savePushPSLog:{}",savePushPSLog);
        if(!savePushPSLog){
            return;
        }
        if(CollectionUtil.isNotEmpty(skuStockDTOS)){
            List<SkuPriceLogDTO> skuPriceLogs=new ArrayList<>();
            skuStockDTOS.forEach(skuStockDTO -> {
                String spuCode=skuStockDTO.getSkuCode().split("-").length>1?skuStockDTO.getSkuCode().substring(0, skuStockDTO.getSkuCode().indexOf("-")):skuStockDTO.getSkuCode();
                SkuPriceLogDTO skuPriceLogDTO=new SkuPriceLogDTO();
                skuPriceLogDTO.setSpuCode(spuCode);
                skuPriceLogDTO.setPriceCode(skuStockDTO.getProductCode());
                skuPriceLogDTO.setSkuCode(skuStockDTO.getSkuCode());
                skuPriceLogDTO.setLogType(SkuPriceLogType.ERP_PUSH_STOCK.value());
                skuPriceLogDTO.setStock(skuStockDTO.getAvailableStockNum());
                skuPriceLogDTO.setStockType(skuStockDTO.getStockType());
                skuPriceLogDTO.setStoreId(skuStockDTO.getStoreId());
                skuPriceLogDTO.setStoreCode(skuStockDTO.getStoreCode());
                skuPriceLogDTO.setRemarks(skuStockDTO.getRemark());
                skuPriceLogDTO.setUpdateTime(now);
                skuPriceLogDTO.setUpdateBy("中台");
                skuPriceLogs.add(skuPriceLogDTO);
            });
            //TODO 保存库存日志
            skuPriceLogService.savePriceLogs(new ESkuPriceLogDTO(skuPriceLogs,
                    "中台推送库存"+ DateUtil.format(now, "yyyy-MM-dd HH:mm:ss")));
        }
    }


    private List<SkuStore> getSkuStores(String priceCode,Long storeId) {
        return skuStoreService.lambdaQuery()
                .eq(SkuStore::getPriceCode,priceCode)
//                .eq(SkuStore::getStatus,1)
                .eq(SkuStore::getStoreId,storeId).list();
    }

    //获取保护价
    private Long getProtectPrice(Long storeId,String priceCode){
        Long protectPrice=0L;
        List<SkuStore> skuStores=skuStoreService.lambdaQuery()
                .eq(SkuStore::getPriceCode,priceCode)
                .eq(SkuStore::getStoreId,storeId)
                .eq(SkuStore::getStatus,1)
                .list();
        if(CollectionUtil.isNotEmpty(skuStores)){
            SkuStore skuStore = skuStores.stream().min(Comparator.comparing(SkuStore::getProtectPrice)).get();
            protectPrice=skuStore!=null?skuStore.getProtectPrice():null;
        }
        return protectPrice;
    }

    private Long getActivityPrice(List<SkuStore> skuStores){
        Long activityPrice=0L;
        if(CollectionUtil.isNotEmpty(skuStores)){
            SkuStore skuStore = skuStores.stream().min(Comparator.comparing(SkuStore::getActivityPrice)).get();
            activityPrice=skuStore!=null?skuStore.getActivityPrice():null;
        }
        return activityPrice;
    }

    private Long getMarketPrice(Long storeId,String priceCode){
        Long marketPrice=0L;
        List<SkuStore> skuStores=skuStoreService.lambdaQuery()
                .eq(SkuStore::getPriceCode,priceCode)
                .eq(SkuStore::getStoreId,storeId)
                .eq(SkuStore::getStatus,1)
                .list();
        if(CollectionUtil.isNotEmpty(skuStores)){
            SkuStore skuStore = skuStores.stream().min(Comparator.comparing(SkuStore::getMarketPriceFee)).get();
            marketPrice=skuStore!=null?skuStore.getMarketPriceFee():null;
        }
        return marketPrice;
    }

    private Long getStorePriceFee(List<SkuStore> skuStores,Integer activityPrice) {
        if(CollectionUtil.isNotEmpty(skuStores)){
            SkuStore skuStore = skuStores.stream().min(Comparator.comparing(SkuStore::getMarketPriceFee)).get();
            return getStPriceFee(skuStore,activityPrice);
        }
        return null;
    }

    private Long getStPriceFee(SkuStore skuStore,Integer activityPrice){
        Long marketPrice = skuStore.getMarketPriceFee();
        if (Objects.nonNull(skuStore.getMarketPriceFee())) {
            marketPrice = NumberUtil.min(skuStore.getMarketPriceFee(), skuStore.getMarketPriceFee());
        }
        Long priceFee = marketPrice;
        if (Objects.nonNull(activityPrice)) {
            priceFee = marketPrice * getPricePocketBtDis() / 10;
            priceFee = NumberUtil.max(activityPrice, priceFee);
        }else if (Objects.nonNull(skuStore.getActivityPrice()) && skuStore.getActivityPrice()>0) {
            priceFee = marketPrice * getPricePocketBtDis() / 10;
            priceFee = NumberUtil.max(skuStore.getActivityPrice(), priceFee);
        }
        return priceFee;
    }

    private Integer getPricePocketBtDis(){
        return 3;
    }

    /**
     * 取消保护价
     * 商品sku
     * @param skuPriceDTO
     */
    private void rebackSkuPiceFee(ErpSkuPriceDTO skuPriceDTO,Long priceFee) {
        List<Sku> list = skuService.lambdaQuery()
                .eq(Sku::getPriceCode, skuPriceDTO.getPriceCode())
                .gt(Sku::getProtectPrice,0)//过滤没有保护价不修改
//                .eq(Sku::getStatus, 1)
                .list();

        List<Sku> updateSkuStores=list.stream().filter(item->item.getProtectPrice()>0).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(updateSkuStores)){
            return;
        }


        if(CollectionUtil.isNotEmpty(updateSkuStores)){
            list.forEach(item->{
                item.setPriceFee(Objects.nonNull(item.getActivityPrice())&&item.getActivityPrice()>0?item.getActivityPrice():item.getMarketPriceFee());//官店没有活动价，此处用到的活动价为原有价格(批量改价此价格会同步为售价)
                item.setPhPrice(Objects.nonNull(item.getActivityPrice())&&item.getActivityPrice()>0?item.getActivityPrice():item.getMarketPriceFee());//官店没有活动价，此处用到的活动价为原有价格(批量改价此价格会同步为售价)
                item.setProtectPrice(0L);
                item.setUpdateTime(new Date());
            });
            skuService.updateBatchById(list);
            Sku sku = list.stream().min(Comparator.comparing(Sku::getPriceFee)).get();
            priceFee=sku.getPriceFee();
        }
    }

    private void rebackSkuStoreMainShopPiceFee(ErpSkuPriceDTO skuPriceDTO) {
        List<SkuStore> skuStores=getSkuStores(skuPriceDTO.getPriceCode(),Constant.MAIN_SHOP);
        List<SkuStore> updateSkuStores=skuStores.stream().filter(item->item.getProtectPrice()>0).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(updateSkuStores)){
            return;
        }

        if(CollectionUtil.isNotEmpty(updateSkuStores)){
            skuStores.forEach(item->{
                item.setPriceFee(Objects.nonNull(item.getActivityPrice())&&item.getActivityPrice()>0?item.getActivityPrice():item.getMarketPriceFee());//官店没有活动价，此处用到的活动价为原有价格(批量改价此价格会同步为售价)
                item.setPhPrice(Objects.nonNull(item.getActivityPrice())&&item.getActivityPrice()>0?item.getActivityPrice():item.getMarketPriceFee());//官店没有活动价，此处用到的活动价为原有价格(批量改价此价格会同步为售价)
                item.setProtectPrice(0L);
                item.setUpdateTime(new Date());
            });
            skuStoreService.updateBatchById(skuStores);
        }
    }

}
