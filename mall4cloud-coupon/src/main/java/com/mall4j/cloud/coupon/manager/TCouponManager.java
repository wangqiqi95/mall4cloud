package com.mall4j.cloud.coupon.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.docking.skq_erp.dto.GetStoresByShopsDto;
import com.mall4j.cloud.api.docking.skq_erp.feign.StdShopFeignClient;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopSpuStockVO;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.user.feign.ScoreActivityClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.constant.ToShopInventoryEnum;
import com.mall4j.cloud.coupon.mapper.TCouponCommodityMapper;
import com.mall4j.cloud.coupon.mapper.TCouponMapper;
import com.mall4j.cloud.coupon.mapper.TCouponSkuMapper;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.model.TCouponCommodity;
import com.mall4j.cloud.coupon.model.TCouponSku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TCouponManager {

    @Autowired
    private TCouponCommodityMapper tCouponCommodityMapper;

    @Autowired
    private ScoreActivityClient scoreActivityClient;

    @Autowired
    private StdShopFeignClient stdShopFeignClient;

    @Autowired
    private SkuFeignClient skuFeignClient;
    
    @Autowired
    private TCouponMapper tCouponMapper;
    
    @Autowired
    private TCouponSkuMapper tCouponSkuMapper;


    private static final Integer FEW_NUM = 2;

    public List<SelectedStoreVO> wrapperInventory(List<SelectedStoreVO> storeVOList,Long couponId,Long convertId){
    
        List<SelectedStoreVO> selectedStoreVOList = new ArrayList<>();
        List<Long> couponIdList = new ArrayList<>();
        if (Objects.nonNull(convertId)){
            //查询活动相关的优惠券ID集合
            ServerResponseEntity<List<Long>> couponIdListByConvertId = scoreActivityClient
                    .getCouponIdListByConvertId(convertId);
            //校验请求是否成功并获取响应数据
            if (couponIdListByConvertId.isFail() || CollectionUtil.isEmpty(couponIdList = couponIdListByConvertId.getData())){
                throw new LuckException("请联系客服，或以门店实际库存为准");
            }
        }
        if (Objects.nonNull(couponId)){
            couponIdList.add(couponId);
        }
    
        List<String> AllSkuCodeList = new ArrayList<>();
        List<Long> AllSpuIdList = new ArrayList<>();
        List<Long> spuIdList = new ArrayList<>();
        List<String> skuCodeList = new ArrayList<>();
        
        for (Long activityCouponId : couponIdList) {
            //查询优惠券信息
            TCoupon tCoupon = tCouponMapper.selectById(activityCouponId);
            
            if(Objects.nonNull(tCoupon)){
    
                //券码导入类型,直接返回
                if(tCoupon.getKind() == 2){
                    return selectedStoreVOList;
                }
                
                //指定商品spu
                if(tCoupon.getCommodityScopeType() == 1){
                    List<TCouponCommodity> tCouponCommodityList = tCouponCommodityMapper.selectList(
                            new LambdaQueryWrapper<TCouponCommodity>()
                                    .eq(TCouponCommodity::getCouponId, activityCouponId)
                    );
                    log.info("COUPON COMMODITY LIST DATA IS : {}",JSON.toJSONString(tCouponCommodityList));
                    if (CollectionUtil.isEmpty(tCouponCommodityList)){
                        throw new LuckException("请联系客服，或以门店实际库存为准");
                    }
                    //聚合优惠券关联的所有商品的SpuId
                    spuIdList = tCouponCommodityList.stream()
                            .map(TCouponCommodity::getCommodityId)
                            .collect(Collectors.toList());
                    AllSpuIdList.addAll(spuIdList);
                    
                    //查询与spuId集合中所有spu相关的sku
                    ServerResponseEntity<List<SkuVO>> responseSku=skuFeignClient.listSkusBySpuId(spuIdList);
    
                    if(responseSku.isFail() || CollectionUtil.isEmpty(responseSku.getData())){
                        throw new LuckException(responseSku.getMsg());
                    }
    
                    skuCodeList = responseSku.getData().stream()
                            .map(SkuVO::getSkuCode)
                            .collect(Collectors.toList());
                    AllSkuCodeList.addAll(skuCodeList);
                    
                }
    
                //指定商品sku
                if(tCoupon.getCommodityScopeType() == 3){
                    List<TCouponSku> skus = tCouponSkuMapper.selectList(new LambdaQueryWrapper<TCouponSku>().eq(TCouponSku::getCouponId, activityCouponId));
    
                    log.info("COUPON TCouponSku LIST DATA IS : {}",JSON.toJSONString(skus));
                    if (CollectionUtil.isEmpty(skus)){
                        throw new LuckException("请联系客服，或以门店实际库存为准");
                    }
                    
                    List<String> priceCodeList = skus.stream().map(TCouponSku::getPriceCode).distinct().collect(Collectors.toList());
    
                    //聚合优惠券关联的所有商品的SpuId
                    spuIdList = skus.stream().map(TCouponSku::getSpuId).distinct().collect(Collectors.toList());
                    AllSpuIdList.addAll(spuIdList);
                    
                    //根据priceCodeList获取SkuCodeList
                    ServerResponseEntity<List<SkuVO>> skusByPriceCodeList = skuFeignClient.getSkusByPriceCodeList(priceCodeList);
    
                    if(skusByPriceCodeList.isFail() || CollectionUtil.isEmpty(skusByPriceCodeList.getData())){
                        throw new LuckException(skusByPriceCodeList.getMsg());
                    }
                    
                    //聚合skuCode
                    skuCodeList = skusByPriceCodeList.getData()
                            .stream().map(SkuVO::getSkuCode)
                            .collect(Collectors.toList());
                    AllSkuCodeList.addAll(skuCodeList);
                }
                
            }
        }
    
        log.info("COUPON AllSkuCodeList OR AllSpuIdList LIST DATA IS  : {} , {}",JSON.toJSONString(AllSkuCodeList) ,JSON.toJSONString(AllSpuIdList));
        if (CollectionUtil.isEmpty(AllSkuCodeList) || CollectionUtil.isEmpty(AllSpuIdList)){
            return selectedStoreVOList;
        }

        //聚合所有门店的code
        List<String> storageCodeList = storeVOList.stream()
                .map(SelectedStoreVO::getStoreCode)
                .collect(Collectors.toList());

        //按storeCode聚合一个map
        Map<String, SelectedStoreVO> storeVOMap = storeVOList.stream()
                .collect(Collectors.toMap(SelectedStoreVO::getStoreCode, Function.identity()));

        List<ShopSpuStockVO> stockVOList = this.getShopSpuStockVOList(storageCodeList,AllSkuCodeList);

        //组装门店信息
         if (CollectionUtil.isNotEmpty(stockVOList)){
             Map<String, List<ShopSpuStockVO>> stockMap = stockVOList.stream()
                     .collect(Collectors.groupingBy(ShopSpuStockVO::getStoreCode));
             storeVOMap.forEach((storeCode,selectedStoreVO) -> {
                 List<ShopSpuStockVO> shopSpuStockVOList;
                 if (Objects.nonNull(shopSpuStockVOList = stockMap.get(storeCode))) {
                     //聚合门店所有sku的库存数量
                     int sum = shopSpuStockVOList.stream().mapToInt(ShopSpuStockVO::getAvailableStockNum).sum();
//                     SelectedStoreVO selectedStoreVO = storeVOMap.get(storeCode);
//                     selectedStoreVO.setSpuIdList(spuIdList);
                     selectedStoreVO.setSpuIdList(AllSpuIdList);
                     //判断库存情况并赋值
                     if (sum < FEW_NUM && sum > 0) {
                         selectedStoreVO.setInventory(ToShopInventoryEnum.FEW.getKey());
                         selectedStoreVO.setSort(ToShopInventoryEnum.FEW.getSort());
                     } else if (sum >= FEW_NUM) {
                         selectedStoreVO.setInventory(ToShopInventoryEnum.ENOUGH.getKey());
                         selectedStoreVO.setSort(ToShopInventoryEnum.ENOUGH.getSort());
                     } else {
                         selectedStoreVO.setInventory(ToShopInventoryEnum.SHORT.getKey());
                         selectedStoreVO.setSort(ToShopInventoryEnum.SHORT.getSort());
                     }

                     selectedStoreVOList.add(selectedStoreVO);
                 }else {
                     selectedStoreVO.setSpuIdList(AllSpuIdList);
                     selectedStoreVO.setInventory(ToShopInventoryEnum.SHORT.getKey());
                     selectedStoreVO.setSort(ToShopInventoryEnum.SHORT.getSort());
                     selectedStoreVOList.add(selectedStoreVO);
                 }
             });
         }else {
             //如果没查到对应的库存数据，则全部设置为缺货
             storeVOMap.forEach((storeCode,selectedStoreVO) -> {
                 selectedStoreVO.setSpuIdList(AllSpuIdList);
                 selectedStoreVO.setInventory(ToShopInventoryEnum.SHORT.getKey());
                 selectedStoreVO.setSort(ToShopInventoryEnum.SHORT.getSort());
                 selectedStoreVOList.add(selectedStoreVO);
             });
         }

        //返回结果并根据对应的库存结果排序
        return selectedStoreVOList.stream()
                .sorted(Comparator.comparing(SelectedStoreVO::getSort).thenComparing(SelectedStoreVO::getStoreDistance))
                .collect(Collectors.toList());

    }



    List<ShopSpuStockVO> getShopSpuStockVOList(List<String> storageCodeList, List<String> skuCodeList){
        try{
            GetStoresByShopsDto getStoresByShopsDto = new GetStoresByShopsDto();
            getStoresByShopsDto.setStoreEcodes(storageCodeList);
            getStoresByShopsDto.setSkuEcodes(skuCodeList);
            getStoresByShopsDto.setPage(0);
            getStoresByShopsDto.setPageSize(1000);

            //效用中台接口进行库存查询并组装数据
            List<ShopSpuStockVO> stockVOList;
            ServerResponseEntity<List<ShopSpuStockVO>> stockResponse = stdShopFeignClient.getStoresByShops(getStoresByShopsDto);
            if(stockResponse.isFail()){
                log.error("GET THE SKUVO LIST IS FAIL,MESSAGE IS {}",stockResponse.getMsg());
                throw new LuckException("请联系客服，或以门店实际库存为准");
            }

            stockVOList = stockResponse.getData();
            return stockVOList;
        }catch (Exception e){
            throw new LuckException("请联系客服，或以门店实际库存为准");
        }
    }
}
