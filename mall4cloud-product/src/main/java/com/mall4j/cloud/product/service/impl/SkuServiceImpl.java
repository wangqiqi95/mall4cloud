package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.dto.SkuLangDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueDTO;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SkuPriceDTO;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.mapper.SkuStoreMapper;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.SpuExcelVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Slf4j
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuStoreMapper skuStoreMapper;
    @Autowired
    private SpuSkuAttrValueService spuSkuAttrValueService;
    @Autowired
    private SkuStockService skuStockService;
    @Autowired
    private SkuLangService skuLangService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    SpuService spuService;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Override
    public void save(Long spuId, List<SkuDTO> skuList) {
        skuList.forEach(sku -> {
            sku.setSpuId(spuId);
            sku.setStatus(StatusEnum.ENABLE.value());
            if (StrUtil.isBlank(sku.getImgUrl())) {
                sku.setImgUrl(null);
            }
            if (Objects.isNull(sku.getScoreFee())) {
                sku.setScoreFee(Constant.ZERO_LONG);
            }
            if (StrUtil.isBlank(sku.getSkuName())) {
                if(CollectionUtil.isNotEmpty(sku.getSkuLangList()) && sku.getSkuLangList().size()>0){
                    SkuLangDTO skuLangDTO = sku.getSkuLangList().get(0);
                    String[] attrValueNames = skuLangDTO.getSkuName().trim().split("#");
                    String skuName=attrValueNames[0];
                    if(attrValueNames.length>1){
                        skuName=skuName+","+attrValueNames[1];
                    }
                    sku.setSkuName(skuName);
                    sku.setName(skuName);
                }
            }
        });
        // 处理数据，保存库存、属性
        skuMapper.saveSkuBatch(skuList);
        List<SkuStock> skuStocks = new ArrayList<>();
        List<SpuSkuAttrValueDTO> spuSkuAttrValues = new ArrayList<>();
        List<SkuLangDTO> skuLangList = new ArrayList<>();
        for (SkuDTO skuDTO : skuList) {
            SkuStock skuStock = new SkuStock();
            skuStock.setSkuId(skuDTO.getSkuId());
            skuStock.setStock(skuDTO.getStock());
            skuStock.setActualStock(skuDTO.getStock());
            skuStock.setLockStock(0);
            skuStock.setSpuId(spuId);
            skuStocks.add(skuStock);
            if (CollUtil.isEmpty(skuDTO.getSpuSkuAttrValues())) {
                continue;
            }
            for (SkuLangDTO skuLangDTO : skuDTO.getSkuLangList()) {
                skuLangDTO.setSkuId(skuDTO.getSkuId());
                skuLangList.add(skuLangDTO);
            }
            for (SpuSkuAttrValueDTO spuSkuAttrValue : skuDTO.getSpuSkuAttrValues()) {
                spuSkuAttrValue.setSpuId(spuId);
                spuSkuAttrValue.setSkuId(skuDTO.getSkuId());
                spuSkuAttrValue.setStatus(StatusEnum.ENABLE.value());
                spuSkuAttrValues.add(spuSkuAttrValue);
            }
        }
        skuLangService.batchSave(mapperFacade.mapAsList(skuLangList, SkuLang.class));
        skuStockService.batchSave(skuStocks);
        spuSkuAttrValueService.save(spuSkuAttrValues);
    }

    @Override
    public void update(Long spuId, List<SkuDTO> skuList) {
        // 从获取旧的sku的数据
        List<SkuVO> skuListDb = listSkuWithAttrBySpuId(spuId, Constant.MAIN_SHOP);
        // skuId
        List<Long> skuIdsDb = skuListDb.stream().map(SkuVO::getSkuId).collect(Collectors.toList());
        List<Long> skuIds = new ArrayList<>();
        List<SkuDTO> updateSkus = new ArrayList<>();
        List<SkuDTO> insertSkus = new ArrayList<>();
        for (SkuDTO sku : skuList) {
            if (StrUtil.isBlank(sku.getImgUrl())) {
                sku.setImgUrl(null);
            }
            if (StrUtil.isBlank(sku.getSkuName())) {
                if(CollectionUtil.isNotEmpty(sku.getSkuLangList()) && sku.getSkuLangList().size()>0){
                    SkuLangDTO skuLangDTO = sku.getSkuLangList().get(0);
                    String[] attrValueNames = skuLangDTO.getSkuName().trim().split("#");
                    String skuName=attrValueNames[0];
                    if(attrValueNames.length>1){
                        skuName=skuName+","+attrValueNames[1];
                    }
                    sku.setSkuName(skuName);
                    sku.setName(skuName);
                }
            }
            sku.setSpuId(spuId);
            if (skuIdsDb.contains(sku.getSkuId())) {
                updateSkus.add(sku);
                skuIds.add(sku.getSkuId());
            } else if (Objects.isNull(sku.getSkuId())) {
                insertSkus.add(sku);
            }
        }

        // 新增的sku--保存
//        if (CollUtil.isNotEmpty(insertSkus)) {
//            save(spuId, insertSkus);
//        }
        // 已有的sku--更新
        Date updateTime = new Date();
        if (CollUtil.isNotEmpty(updateSkus)) {
            List<Sku> skus = mapperFacade.mapAsList(updateSkus, Sku.class);

            skuMapper.updateSkuBatch(skus);
            //TODO sku_store数据批量更新(门店sku售价)
            log.info("商品详情发布-->sku_store数据批量更新 商品id【{}】 操作人【{}】 工号【{}】",spuId,AuthUserContext.get().getUsername(),AuthUserContext.get().getUserId());
//            skuStoreMapper.updateSkuBatch(skus, Constant.MAIN_SHOP);
            skuStockService.updateBatch(updateSkus);
            //更新规格列表
            updateSkus.forEach(skuDTO -> {
                SkuLangDTO skuLangDTO = skuDTO.getSkuLangList().get(0);
                String[] attrNames = skuLangDTO.getAttrs().trim().split(Constant.COMMA);
                String[] attrValueNames = skuLangDTO.getSkuName().trim().split("#");
                try {
                    for (int a = 0; a < attrNames.length - 1; a++) {
                        spuSkuAttrValueService.lambdaUpdate().set(SpuSkuAttrValue::getAttrValueName, attrValueNames[a])
                                .set(SpuSkuAttrValue::getUpdateTime, updateTime)
                                .eq(SpuSkuAttrValue::getAttrName, attrNames[a])
                                .eq(SpuSkuAttrValue::getSkuId, skuDTO.getSkuId())
                                .eq(SpuSkuAttrValue::getSpuId, skuDTO.getSpuId())
                                .update();
                    }
                } catch (Exception e) {
                    throw new LuckException("规格参数更新失败");
                }
            });

        }
        // 不存在的sku--删除
//        skuIdsDb.removeAll(skuIds);
//        if (CollUtil.isNotEmpty(skuIdsDb)) {
//            deleteSkuBatch(skuIdsDb);
//        }
    }

    @Override
    public void deleteById(Long skuId) {
        skuMapper.deleteById(skuId);
    }

    @Override
//    @Cacheable(cacheNames = CacheNames.SKU_WITH_ATTR_LIST_KEY, key = "#spuId",sync = true)
    public List<SkuVO> listSkuWithAttrBySpuId(Long spuId, Long storeId) {
        return skuMapper.listSkuWithAttrBySpuId(spuId, storeId);
    }

    @Override
    public List<SkcVO> listSkcWithAttrBySpuId(Long spuId, Long storeId) {
        return skuMapper.listSkcWithAttrBySpuId(spuId, storeId);
    }

    @Override
    public void deleteBySpuId(Long spuId) {
        skuMapper.updateBySpuId(spuId);
    }

    @Override
//    @Cacheable(cacheNames = CacheNames.SKU_KEY, key = "#skuId")
    public SkuVO getSkuBySkuId(Long skuId) {
        SkuVO skuVO = skuMapper.getSkuBySkuId(skuId);
        if(skuVO==null){
            return null;
        }
        SpuVO spuVO = spuService.getBySpuId(skuVO.getSpuId());
        skuVO.setSpuName(spuVO.getName());
        return skuVO;
    }

    @Override
    public SkuVO getSkuBySkuCode(String skuCode) {
        return null;
    }

    @Override
    public void updateAmountOrStock(SpuDTO spuDTO) {
        List<SkuDTO> skuList = spuDTO.getSkuList();
        List<Sku> skus = new ArrayList<>();
        boolean isUpdateStock = false;
        for (SkuDTO skuDTO : skuList) {
            if (Objects.nonNull(skuDTO.getChangeStock()) && !Objects.equals(skuDTO.getChangeStock(), 0)) {
                isUpdateStock = true;
                break;
            } else if (Objects.nonNull(skuDTO.getPriceFee())) {
                Sku sku = new Sku();
                sku.setSkuId(skuDTO.getSkuId());
                sku.setPriceFee(skuDTO.getPriceFee());
                skus.add(sku);
            }
        }
        if (isUpdateStock) {
            skuStockService.updateBatch(skuList);
        }
        if (CollUtil.isNotEmpty(skus)) {
            skuMapper.updateSkuBatch(skus);
        }
    }

    @Override
    public List<SkuAppVO> getSpuDetailSkuInfo(Long spuId, Long storeId) {
        // 从缓存获取sku的数据
        List<SkuVO> skuData = listSkuAllInfoBySpuId(spuId, true, storeId);

        List<SkuAppVO> skuAppList = new ArrayList<>();
        for (SkuVO sku : skuData) {
            SkuAppVO skuAppVO = mapperFacade.map(sku, SkuAppVO.class);
            skuAppVO.setProperties(spliceProperties(sku.getSpuSkuAttrValues()));
            skuAppList.add(skuAppVO);
        }
        return skuAppList;
    }

    public String spliceAttrNames(List<SpuSkuAttrValueVO> spuSkuAttrValueList) {
        StringBuilder properties = new StringBuilder();
        for (SpuSkuAttrValueVO spuSkuAttrValue : spuSkuAttrValueList) {
            properties.append(spuSkuAttrValue.getAttrName()).append(Constant.COLON);
        }
        if (StrUtil.isBlank(properties.toString())) {
            return properties.toString();
        }
        return properties.substring(0, properties.length() - 1);
    }

    public String spliceAttrValueNames(List<SpuSkuAttrValueVO> spuSkuAttrValueList) {
        StringBuilder properties = new StringBuilder();
        for (SpuSkuAttrValueVO spuSkuAttrValue : spuSkuAttrValueList) {
            properties.append(spuSkuAttrValue.getAttrValueName()).append(Constant.CN_COMMA);
        }
        if (StrUtil.isBlank(properties.toString())) {
            return properties.toString();
        }
        return properties.substring(0, properties.length() - 1);
    }


    @Override
    public String spliceProperties(List<SpuSkuAttrValueVO> spuSkuAttrValueList) {
        StringBuilder properties = new StringBuilder();
        for (SpuSkuAttrValueVO spuSkuAttrValue : spuSkuAttrValueList) {
//            if (CollUtil.isEmpty(spuSkuAttrValue.getSpuSkuAttrValueLangList())) {
//                continue;
//            }
            properties.append(spuSkuAttrValue.getAttrName()).append(Constant.COLON).append(spuSkuAttrValue.getAttrValueName()).append(Constant.SEMICOLON);
        }
        if (StrUtil.isBlank(properties.toString())) {
            return properties.toString();
        }
        return properties.substring(0, properties.length() - 1);
    }


    private void deleteSkuBatch(List<Long> skuIds) {
        List<Sku> skus = new ArrayList<>();
        for (Long skuId : skuIds) {
            Sku sku = new Sku();
            sku.setSkuId(skuId);
            sku.setStatus(StatusEnum.DELETE.value());
            skus.add(sku);
        }
        skuMapper.updateSkuBatch(skus);
        spuSkuAttrValueService.changeStatusBySkuId(skuIds, StatusEnum.DELETE.value());
    }


    @Override
    public List<SkuVO> listSkuAllInfoBySpuId(Long spuId, boolean enable, Long storeId) {
        SkuServiceImpl skuService = (SkuServiceImpl) AopContext.currentProxy();

        // 从缓存获取sku的数据
        List<SkuVO> skus = skuService.listSkuWithAttrBySpuId(spuId, storeId);

        if (enable) {
            // 启用的sku，app查询到的商品是启用sku的商品
            skus = skus.stream().filter(skuVO -> Objects.equals(skuVO.getStatus(), 1)).collect(Collectors.toList());
        }

//        // 因为库存的缓存是经常变的，而规格信息的缓存是几乎不变的，所以库存的缓存要独立拿出来
//        List<SkuStockVO> skuStocks = skuStockService.listStockBySpuId(spuId);
//
//        for (SkuVO skuVO : skus) {
//            for (SkuStockVO skuStock : skuStocks) {
//                if (Objects.equals(skuStock.getSkuId(), skuVO.getSkuId())) {
//                    skuVO.setStock(skuStock.getStock());
//                }
//            }
//        }
////        ProductLangUtil.skuList(skus);
        return skus;
    }

    @Override
    public List<SkcVO> listSkcAllInfoBySpuId(Long spuId, boolean enable, Long storeId) {
        SkuServiceImpl skuService = (SkuServiceImpl) AopContext.currentProxy();
        // 从缓存获取sku的数据
        List<SkcVO> skcs = skuService.listSkcWithAttrBySpuId(spuId, storeId);
        if (enable) {
            // 启用的skc，app查询到的商品是启用skc的商品
            skcs = skcs.stream().filter(skcVO -> Objects.equals(skcVO.getStatus(), 1)).collect(Collectors.toList());
        }
        return skcs;
    }

    @Override
    public List<SoldSpuExcelVO> excelSkuList(List<Long> spuIds, Long storeId, PageAdapter pageAdapter,boolean isAllMainShop) {
        if (CollUtil.isEmpty(spuIds)) {
            return new ArrayList<>();
        }
        List<SoldSpuExcelVO> spuExcelList = new ArrayList<>();
        List<SkuVO> skuList = !isAllMainShop?skuMapper.excelSkuList(spuIds, storeId,pageAdapter):skuMapper.excelMainShopAllSkuList(spuIds, storeId,pageAdapter);
        log.info("excelSkuList--->size {}",skuList.size());
        for (SkuVO skuVO : skuList) {
            List<SpuSkuAttrValue> spuSkuAttrValues=spuSkuAttrValueService.getSpuSkuAttrValueBySkuId(skuVO.getSpuId());
            if(CollectionUtil.isNotEmpty(spuSkuAttrValues)){
                List<SpuSkuAttrValueVO> spuSkuAttrValueVOS=mapperFacade.mapAsList(spuSkuAttrValues, SpuSkuAttrValueVO.class);

                Map<Long, List<SpuSkuAttrValueVO>> spuSkuAttrValueMap = spuSkuAttrValueVOS.stream().collect(Collectors.groupingBy(SpuSkuAttrValueVO::getSkuId));

                if(spuSkuAttrValueMap.containsKey(skuVO.getSkuId())){
                    skuVO.setSpuSkuAttrValues(spuSkuAttrValueMap.get(skuVO.getSkuId()));
                }
            }

            SoldSpuExcelVO spuExcelVO = mapperFacade.map(skuVO, SoldSpuExcelVO.class);
            spuExcelVO.setPriceFee(conversionPrices(skuVO.getPriceFee().toString()));
            spuExcelVO.setMarketPriceFee(conversionPrices(skuVO.getMarketPriceFee().toString()));
            ProductLangUtil.spuSkuAttrValueList(skuVO.getSpuSkuAttrValues(), null);
            spuExcelVO.setProperties(spliceAttrNames(skuVO.getSpuSkuAttrValues()));
            spuExcelVO.setPropertiesValues(spliceAttrValueNames(skuVO.getSpuSkuAttrValues()));
            spuExcelVO.setSkucodeStatus(Objects.nonNull(skuVO.getStatus())?String.valueOf(skuVO.getStatus()):"0");
//            spuExcelVO.setPropertiesZh(spliceProperties(skuVO.getSpuSkuAttrValues()));
//            spuExcelVO.setProperties(spliceProperties(skuVO.getSpuSkuAttrValues()));

            spuExcelList.add(spuExcelVO);
        }
        return spuExcelList;
    }

//    @Override
//    public List<SpuExcelVO> excelSkuList(List<Long> spuIds, Long storeId, PageDTO pageDTO) {
//        if (CollUtil.isEmpty(spuIds)) {
//            return new ArrayList<>();
//        }
//        List<SpuExcelVO> spuExcelList = new ArrayList<>();
//        List<SkuVO> skuList = skuMapper.excelSkuList(spuIds, storeId,new PageAdapter(pageDTO));
//        for (SkuVO skuVO : skuList) {
//            SpuExcelVO spuExcelVO = mapperFacade.map(skuVO, SpuExcelVO.class);
//            spuExcelVO.setPriceFee(skuVO.getPriceFee().toString());
//            spuExcelVO.setMarketPriceFee(skuVO.getMarketPriceFee().toString());
//            spuExcelVO.setMarketPriceFee(conversionPrices(skuVO.getMarketPriceFee().toString()));
//            ProductLangUtil.spuSkuAttrValueList(skuVO.getSpuSkuAttrValues(), null);
////            spuExcelVO.setPropertiesZh(spliceProperties(skuVO.getSpuSkuAttrValues()));
////            spuExcelVO.setProperties(spliceProperties(skuVO.getSpuSkuAttrValues()));
//            spuExcelVO.setProperties(spliceAttrNames(skuVO.getSpuSkuAttrValues()));
//            spuExcelVO.setPropertieValues(spliceAttrValueNames(skuVO.getSpuSkuAttrValues()));
//
//            spuExcelList.add(spuExcelVO);
//        }
//        return spuExcelList;
//    }

    /**
     * 金额Long格式转换Double
     * @param num
     * @return 金额字符串
     */
    private String conversionPrices (String num) {
        if (StrUtil.isBlank(num)) {
            return num;
        }
        BigDecimal b1 = new BigDecimal(num);
        BigDecimal b2 = new BigDecimal(Constant.PRICE_MAGNIFICATION);
        double price = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(price);
    }

//    @Override
//    public List<SpuExcelVO> excelSkuList(List<Long> spuIds) {
//        if (CollUtil.isEmpty(spuIds)) {
//            return new ArrayList<>();
//        }
//        List<SpuExcelVO> spuExcelList = new ArrayList<>();
//        List<SkuVO> skuList = skuMapper.excelSkuList(spuIds);
//        for (SkuVO skuVO : skuList) {
//            SpuExcelVO spuExcelVO = mapperFacade.map(skuVO, SpuExcelVO.class);
//            spuExcelVO.setPriceFee(skuVO.getPriceFee().toString());
//            spuExcelVO.setMarketPriceFee(skuVO.getMarketPriceFee().toString());
//            if(CollectionUtil.isNotEmpty(skuVO.getSkuLangList())){
//                for (SkuLangVO skuLangVO : skuVO.getSkuLangList()) {
//                    if (Objects.equals(skuLangVO.getLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
//                        ProductLangUtil.spuSkuAttrValueList(skuVO.getSpuSkuAttrValues(), skuLangVO.getLang());
//                        spuExcelVO.setPropertiesZh(spliceProperties(skuVO.getSpuSkuAttrValues()));
//                        spuExcelVO.setSkuNameZh(skuLangVO.getSkuName());
//                    } else if (Objects.equals(skuLangVO.getLang(), LanguageEnum.LANGUAGE_EN.getLang())) {
//                        ProductLangUtil.spuSkuAttrValueList(skuVO.getSpuSkuAttrValues(), skuLangVO.getLang());
//                        spuExcelVO.setPropertiesEn(spliceProperties(skuVO.getSpuSkuAttrValues()));
//                        spuExcelVO.setSkuNameEn(skuLangVO.getSkuName());
//                    }
//                }
//            }
//            spuExcelList.add(spuExcelVO);
//        }
//        return spuExcelList;
//    }

    @Override
    public List<SkuVO> listSkuPriceByIds(List<Long> skuIds) {
        if (CollUtil.isEmpty(skuIds)) {
            return new ArrayList<>();
        }
        return skuMapper.listSkuPriceByIds(skuIds);
    }

    @Override
    public List<SkuVO> listSkuCodeByIds(List<Long> skuIds) {
        if (CollUtil.isEmpty(skuIds)) {
            return new ArrayList<>();
        }
        return skuMapper.listSkuCodeByIds(skuIds);
    }

    @Override
    public List<SkuAddrVO> listSpuDetailByIds(List<Long> skuIds) {
        return skuMapper.listSpuDetailByIds(skuIds);
    }

    @Override
    public SkuVO getSkuBySkuIdAndStoreId(Long spuId,Long skuId, Long storeId) {
        SkuVO skuVO=skuMapper.getSkuBySkuIdAndStoreId(spuId,skuId, storeId);
        if(Objects.nonNull(skuVO)){
            ServerResponseEntity<Boolean> inviteStoreResponse=storeFeignClient.isInviteStore(storeId);
            if(inviteStoreResponse.isSuccess() && Objects.nonNull(inviteStoreResponse.getData()) && inviteStoreResponse.getData()){
                if(skuVO.getSkuProtectPrice()<=0){
                    skuVO.setPriceFee(skuVO.getMarketPriceFee());//虚拟门店没有保护价默认取吊牌价
                }
            }
        }

        return skuVO;
    }

    @Override
    public SkuCodeVO getCodeBySkuId(Long skuId) {
        return skuMapper.getCodeBySkuId(skuId);
    }

    @Override
    public List<SkuAppVO> getSpuSkuInfo(Long spuId, Long storeId) {
        List<SkuVO> skuData = skuMapper.getSpuSkuInfo(spuId, storeId);
        List<SkuAppVO> skuAppList = new ArrayList<>();
        for (SkuVO sku : skuData) {
            SkuAppVO skuAppVO = mapperFacade.map(sku, SkuAppVO.class);
            skuAppVO.setProperties(spliceProperties(sku.getSpuSkuAttrValues()));
            skuAppList.add(skuAppVO);
        }
        return skuAppList;
    }

    @Override
    public SkuVO getSkuByCode(String skuCode) {
        SkuVO skuVO = skuMapper.getSkuByCode(skuCode);
        if(skuVO==null){
            return null;
        }
        SpuVO spuVO = spuService.getBySpuId(skuVO.getSpuId());
        skuVO.setSpuName(spuVO.getName());
        return skuVO;
    }

    @Override
    public List<SkuVO> getSkcByCode(String skcCode) {
        List<SkuVO> skcList = skuMapper.getSkcByCode(skcCode);
        if(CollectionUtil.isEmpty(skcList)){
            return null;
        }
        return skcList;
    }

    @Override
    public List<SkuVO> getSkuBySpuId(Long spuId) {
        return skuMapper.getSkuBySpuId(spuId);
    }

    @Override
    public List<Long> getAppSkuBySkuIdList(List<Long> spuIdList) {
        return skuMapper.getAppSkuBySkuIdList(spuIdList);
    }

    @Override
    public List<SkuPriceDTO> getAppSkuPriceBySkuIdList(List<Long> spuIdList, Long storeId) {
        return skuMapper.getAppSkuPriceBySkuIdList(spuIdList, storeId);
    }

    /**
     * 实际价格展示逻辑
     * @param spuId
     * @param storeId
     * @return
     */
    @Override
    public List<SkuAppVO> getSpuSkuCaseStock(Long spuId, Long storeId) {
        return null;
    }

    @Override
    public List<Long> checkSkuStorePrice(Long spuId, Long storeId, List<SkuProtectVO> skuProtectVOS) {
        if(CollectionUtil.isEmpty(skuProtectVOS) && skuProtectVOS.size()<=0){
            return null;
        }
//        Map<String, SkuProtectVO> stringSkuProtectVOMap = skuProtectVOS.stream().collect(Collectors.toMap(SkuProtectVO::getPriceCode, skuProtectVO -> skuProtectVO));
//        Long totalStoreProtectPrice=skuProtectVOS.stream().mapToLong(SkuProtectVO::getStoreProtectPrice).sum();

        skuProtectVOS=skuProtectVOS.stream().filter(skuProtectVO -> skuProtectVO.getStoreProtectPrice()<=0).collect(Collectors.toList());


        return skuProtectVOS.stream().map(SkuProtectVO::getSkuId).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = CacheNames.SKU_KEY_PRICECODE, key = "#priceCode")
    @Override
    public List<Sku> getSkuByPriceCode(String priceCode) {
        return skuMapper.getSkuByPriceCode(priceCode);
    }

    @Override
    public List<SkuVO> listSkusBySpuId(List<Long> spuIds) {
        List<SkuVO> skuVOS=new ArrayList<>();
        List<Sku> skus=this.list(new LambdaQueryWrapper<Sku>().in(Sku::getSpuId,spuIds));
        if(CollectionUtil.isNotEmpty(skus)){
            skuVOS=mapperFacade.mapAsList(skus,SkuVO.class);
        }
        return skuVOS;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SKU_KEY, key = "#skuCode")
    public Sku getSkuBySkuCodeCach(String skuCode) {
        Sku sku = this.getOne(new LambdaQueryWrapper<Sku>()
                        .eq(Sku::getSkuCode, skuCode)
                , false);
        return sku;
    }
    
    @Override
    public List<SkuVO> getSkusByPriceCodeList(List<String> priceCodes) {
        List<SkuVO> skuVOS=new ArrayList<>();
        List<Sku> skus=this.list(new LambdaQueryWrapper<Sku>().in(Sku::getPriceCode,priceCodes));
        if(CollectionUtil.isNotEmpty(skus)){
            skuVOS=mapperFacade.mapAsList(skus,SkuVO.class);
        }
        return skuVOS;
    }
}
