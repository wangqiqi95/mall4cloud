package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.biz.feign.QrcodeFeignClient;
import com.mall4j.cloud.api.docking.dto.BatchResultsDto;
import com.mall4j.cloud.api.docking.dto.ElectronicSignDto;
import com.mall4j.cloud.api.docking.feign.ElectronicSignFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.dto.ErpSkuPriceDTO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.NumberTo64;
import com.mall4j.cloud.product.mapper.ElPriceProdMapper;
import com.mall4j.cloud.product.model.ElPriceProd;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuStore;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @luzhengxiang
 * @create 2022-04-02 11:19 AM
 **/
@Slf4j
@Service
public class ElectronicSignServiceImpl implements ElectronicSignService {
    @Autowired
    StoreFeignClient storeFeignClient;
    @Autowired
    ElPriceProdMapper elPriceProdMapper;
    @Autowired
    ElectronicSignFeignClient electronicSignFeignClient;
    @Autowired
    private SpuService spuService;
    @Autowired
    private SkuStoreService skuStoreService;
    @Autowired
    private ElPriceProdService elPriceProdService;
    @Autowired
    private QrcodeFeignClient qrcodeFeignClient;

    @Value("${wxconfg.malinkurl}")
    private String malinkurl;

    /**
     * 获取配置价签同步的商品
     * @param spuIds
     * @return
     */
    private List<Long> getElSpuList(List<Long> spuIds){
        List<ElPriceProd> el_spus=elPriceProdService.getElSpuList(spuIds);
        if(CollectionUtil.isNotEmpty(el_spus)){
            List<Long> spuIdList = el_spus.stream().map(ElPriceProd::getSpuId).collect(Collectors.toList());
            return spuIdList;
        }
        return null;
    }

    /**
     * 同步配置价签库的商品至阿里云电子价签库
     * 商品包含的所有SKU
     * @param spuIds
     */
    @Override
    public void syncSpu(List<Long> spuIds) {
        try {
            log.info("-----价签同步，全部开启价签同步门店商品----");
            List<Long> spuIdList = getElSpuList(spuIds);
            if(CollectionUtil.isEmpty(spuIdList)){
                log.info("同步电子价签失败，未匹配到价签库商品");
                return;
            }
            log.info("同步电子价签 商品数据 {}",spuIdList.size());
            if(CollectionUtil.isNotEmpty(spuIdList)){
                List<Spu> spus=spuService.lambdaQuery().in(Spu::getSpuId,spuIdList).list();
                if(CollectionUtil.isEmpty(spus)){
                    log.info("同步电子价签  同步商品集合为空");
                    return;
                }
                ServerResponseEntity<List<StoreVO>> response= storeFeignClient.getStoreByPushElStatus(1);
                log.info("同步电子价签开启同步价签配置的门店 {},{}",response.isSuccess(),response.getMsg());
                if(response.isSuccess()){
                    if(CollectionUtil.isNotEmpty(response.getData())){
                        spus.forEach(spu -> {
                            response.getData().forEach(storeVO -> {
                                //获取门店sku_store
                                List<SkuStore> skuStores = skuStoreService.getSkuStoresByStoreId(storeVO.getStoreId());
                                if(CollectionUtil.isNotEmpty(skuStores)){
                                    skuStores = skuStores.stream()
                                            .filter(skuStore -> StrUtil.isNotBlank(skuStore.getIntscode()))
                                            .collect(Collectors.toList());
                                    log.info("同步电子价签 门店sku数据 {}",skuStores.size());
                                    ElectronicSignDto signDto=convert(spu,storeVO.getStoreCode(),skuStores,getItemQrCode(spu.getSpuId(),storeVO.getStoreId()));
//                                    ElectronicSignDto signDto=convert(spu,null,list,getItemQrCode(spu.getSpuId(),storeVO.getStoreId()));
                                    log.info("同步电子价签 请求入参：商品spuId:【{}】 , 商品spuCode: 【{}】 , 价签参数: {}",spu.getSpuId(),spu.getSpuCode(),JSON.toJSON(signDto));
                                    //调用电子加签接口。
                                    ServerResponseEntity<List<BatchResultsDto>> responseEntity=electronicSignFeignClient.batchInsertItems(signDto);
                                    log.info("同步电子价签 响应结果 {}", JSON.toJSON(responseEntity));
                                }
                            });
                        });
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 价签同步逻辑：
     * 比如商品配置800个，推送对应门店800个商品对应sku都要全部推送。
     * 如果门店价格为空取官店价格，否则取门店价格
     * @param storeIds
     */
    @Override
    public void syncStoreSpu(List<Long> storeIds) {
        log.info("-----价签同步，门店商品----");
        ServerResponseEntity<List<StoreVO>> response= storeFeignClient.listByStoreIdList(storeIds);
        if(response.isSuccess()){
            response.getData().forEach(storeVO -> {
                if(storeVO.getPushElStatus()==null || storeVO.getPushElStatus()==0){
                    log.info("价签同步，门店【{}】未开启价签同步",storeVO.getName());
                    return;
                }
                //获取电子价签配置的全部商品关联的全部sku数据（价格逻辑：当前门店价格为空取官店sku价格）
                List<SkuStore> skuStoreList = skuStoreService.getElSkuStoresByStoreId(storeVO.getStoreId());

                List<SkuStore> skuStores = skuStoreList.stream()
                        .filter(SkuStore -> StrUtil.isNotBlank(SkuStore.getForcode()))
                        .filter(SkuStore -> StrUtil.isNotBlank(SkuStore.getIntscode()))
                        .collect(Collectors.toList());

                if(CollectionUtil.isEmpty(skuStores)){
                    log.info("价签同步，暂无门店商品sku_store-->门店:【{}】",storeVO.getName());
                    return;
                }
                log.info("价签同步，skuStores 总数 【{}】",skuStores.size());
                //需要推送的sku_store数据
                Map<Long, List<SkuStore>> skuStoreMap = skuStores.stream().collect(Collectors.groupingBy(SkuStore::getSpuId));
                List<Long> spuIdList = new ArrayList<>(skuStoreMap.keySet());

                log.info("价签同步，spu 总数 【{}】",spuIdList.size());

                log.info("价签同步，实际价签库商品spu 总数 【{}】",getElSpuList(null).size());

                spuIdList.forEach(spuId -> {

                    //获取商品sku数据
                    List<SkuStore> list=skuStoreMap.get(spuId);

                    Spu spu=new Spu();
                    spu.setSpuId(list.get(0).getSpuId());
                    spu.setSpuCode(list.get(0).getSpuCode());
                    spu.setName(list.get(0).getSpuName());
                    spu.setStyleCode(list.get(0).getSpuStyleCode());

                    ElectronicSignDto signDto=convert(spu,storeVO.getStoreCode(),list,getItemQrCode(spu.getSpuId(),storeVO.getStoreId()));
                    log.info("价签同步 请求入参：商品spuId:【{}】商品spuCode:【{}】 sku总数:【{}】 价签参数:【{}】",spu.getSpuId(),spu.getSpuCode(),list.size(),JSON.toJSON(signDto));
                    //调用电子加签接口。
                    ServerResponseEntity<List<BatchResultsDto>> responseEntity=electronicSignFeignClient.batchInsertItems(signDto);
                    log.info("价签同步 响应结果 【{}】", JSON.toJSON(responseEntity));
                });

            });
        }
    }





//    @Override
//    public void syncStoreSpu(List<Long> storeIds) {
//        log.info("-----价签同步，门店商品----");
//        ServerResponseEntity<List<StoreVO>> response= storeFeignClient.listByStoreIdList(storeIds);
//        if(response.isSuccess()){
//            response.getData().forEach(storeVO -> {
//                if(storeVO.getPushElStatus()==null || storeVO.getPushElStatus()==0){
//                    log.info("价签同步，门店【{}】未开启价签同步",storeVO.getName());
//                    return;
//                }
//                //获取门店sku_store
//                List<SkuStore> skuStores = skuStoreService.getSkuStoresByStoreId(storeVO.getStoreId());
//                if(CollectionUtil.isEmpty(skuStores)){
//                    log.info("价签同步，暂无门店商品sku_store-->门店:【{}】",storeVO.getName());
//                    return;
//                }
//                skuStores = skuStores.stream()
//                        .filter(skuStore -> StrUtil.isNotBlank(skuStore.getIntscode()))
//                        .collect(Collectors.toList());
//                //spuId 集合
//                Map<Long, Long> spuIdsMap = skuStores.stream().collect(Collectors.toMap(SkuStore::getSpuId, skuStore -> skuStore.getSpuId(),(k1, k2)-> k1));
//                List<Long> spuIds = new ArrayList<>(spuIdsMap.values());
//                if(CollectionUtil.isEmpty(spuIds)){
//                    log.info("价签同步，根据门店sku_store 未找到商品spu信息-->门店:【{}】",storeVO.getName());
//                    return;
//                }
//                //根据门店商品匹配是否配置价签同步
//                List<Long> spuIdList = getElSpuList(spuIds);
//                if(CollectionUtil.isEmpty(spuIdList)){
//                    log.info("同步电子价签失败，未匹配到价签库商品");
//                    return;
//                }
//                if(CollectionUtil.isNotEmpty(spuIdList)){
//                    log.info("同步电子价签 商品数据 {}",spuIdList.size());
//                    //获取spu信息
//                    List<Spu> spus=spuService.lambdaQuery().in(Spu::getSpuId,spuIdList).list();
//                    if(CollectionUtil.isEmpty(spus)){
//                        log.info("同步电子价签  同步商品集合为空");
//                        return;
//                    }
//                    //需要推送的sku_store数据
//                    Map<Long, List<SkuStore>> skuStoreMap = skuStores.stream().collect(Collectors.groupingBy(SkuStore::getSpuId));
//                    spus.forEach(spu -> {
//                        List<SkuStore> list=skuStoreMap.get(spu.getSpuId());
//                        ElectronicSignDto signDto=convert(spu,storeVO.getStoreCode(),list,getItemQrCode(spu.getSpuId(),storeVO.getStoreId()));
////                        ElectronicSignDto signDto=convert(spu,null,list,getItemQrCode(spu.getSpuId(),storeVO.getStoreId()));
//                        log.info("同步电子价签 请求入参：商品spuId:【{}】 , 商品spuCode: 【{}】 , 价签参数: {}",spu.getSpuId(),spu.getSpuCode(),JSON.toJSON(signDto));
//                        //调用电子加签接口。
//                        ServerResponseEntity<List<BatchResultsDto>> responseEntity=electronicSignFeignClient.batchInsertItems(signDto);
//                        log.info("同步电子价签 响应结果 {}", JSON.toJSON(responseEntity));
//                    });
//                }
//            });
//        }
//    }

    /**
     * 小程序商品详情页太阳码
     * @param spuId
     * @param storeId
     * @return
     */
    private String getItemQrCode(Long spuId,Long storeId){
        //http://skqwebapp.tuoketech.com/dzjq?id=72826&s=80144

        String itemQrCode=malinkurl+"?id="+spuId+"&s="+storeId;
        
        //商品太阳码
//        String path="pages/detail/detail";
//        String scene="id="+spuId;
//        String itemQrCode=null;
//        ServerResponseEntity<String> responseItemQrcode=qrcodeFeignClient.getWxaCodeUrl(scene,storeId,path,430);
//        if(responseItemQrcode!=null && responseItemQrcode.isSuccess()){
//            itemQrCode=responseItemQrcode.getData();
//        }
        return itemQrCode;
    }

    /**
     * 转换价签同步数据
     * @param spu
     * @param storeCode
     * @param skuStores
     * @param itemQrCode
     * @return
     */
    private ElectronicSignDto convert(Spu spu,String storeCode,List<SkuStore> skuStores,String itemQrCode){
        ElectronicSignDto electronicSignDto=new ElectronicSignDto();
        electronicSignDto.setStoreId(storeCode);
//        electronicSignDto.setSyncByItemId(true);
        List<ElectronicSignDto.ItemInfo> itemInfos=new ArrayList<>();
        skuStores.forEach(skuStore -> {
            ElectronicSignDto.ItemInfo itemInfo=new ElectronicSignDto.ItemInfo();
//            itemInfo.setItemBarCode(skuStore.getSkuCode());
            //优先取19码，其次取69码
            String itemBarCode=StrUtil.isNotBlank(skuStore.getForcode())?skuStore.getForcode():skuStore.getIntscode();
            itemInfo.setItemBarCode(itemBarCode);
            itemInfo.setItemId(String.valueOf(skuStore.getSkuId()));
//            itemInfo.setItemId(skuStore.getPriceCode());
//            itemInfo.setItemId(spu.getSpuCode());
            itemInfo.setItemTitle(skuStore.getSkuCode() +" "+spu.getName());
            itemInfo.setPriceUnit("双");
            if(StrUtil.isNotBlank(spu.getStyleCode())){
                if(spu.getStyleCode().equals("FTW")){
                    itemInfo.setPriceUnit("双");
                }
            }
            itemInfo.setSkuId(skuStore.getSkuId().toString());
            //售价
            itemInfo.setActionPrice(skuStore.getPriceFee().intValue());
            //吊牌价
            itemInfo.setOriginalPrice(skuStore.getMarketPriceFee().intValue());

            //是否促销(活动价小于吊牌价：是  默认：否)
            if(skuStore.getActivityPrice()<skuStore.getMarketPriceFee()){
                itemInfo.setBePromotion(true);
            }else{
                itemInfo.setBePromotion(false);
            }
            //itemQrCode 商品二维码地址，最长1024个字符
            itemInfo.setItemQrCode(itemQrCode);
            itemInfos.add(itemInfo);

        });
        electronicSignDto.setItemInfos(itemInfos);
        return electronicSignDto;
    }
}
