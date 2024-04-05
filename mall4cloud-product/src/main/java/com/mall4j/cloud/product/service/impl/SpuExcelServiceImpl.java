package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.constant.CategoryLevel;
import com.mall4j.cloud.api.product.dto.SkuErpSyncDTO;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.delivery.vo.ShopTransportVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.*;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.*;
import com.mall4j.cloud.product.constant.SpuExportError;
import com.mall4j.cloud.product.dto.ActivitySkuExportDTO;
import com.mall4j.cloud.product.dto.SpuAbbrReqDto;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueLangDTO;
import com.mall4j.cloud.product.listener.SpuAbbrListener;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.*;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
public class SpuExcelServiceImpl implements SpuExcelService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuExtensionService spuExtensionService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryShopService categoryShopService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StoreFeignClient storeFeignClient;

    /**
     * 商品显示信息
     */
    public static final String ENABLE = "上架";
    public static final String DISABLE = "下架";
    public static final String OFFLINE = "违规下架";
    public static final String WAIT_AUDIT = "等待审核";
    public static final String[] DELIVERY_MODE = {DeliveryType.SAME_CITY.description(), DeliveryType.STATION.description(), "用户自提+同城配送"};
    public static final String[] STATUS_MODE = {ENABLE,DISABLE};
    /**
     * 平台分类
     */
    public static final int CATEGORY_INDEX = 6;
    /**
     * 店铺分类
     */
    public static final int SHOP_CATEGORY_INDEX = 7;
    /**
     * 配送方式
     */
    public static final int DELIVERY_MODE_INDEX = 8;
    /**
     * 运费模板
     */
    public static final int DELIVERY_TEMPLATE_INDEX = 9;
    /**
     * 状态
     */
    public static final int STATUS_INDEX = 10;

    private static final Logger log = LoggerFactory.getLogger(SpuExcelServiceImpl.class);

//    @Override
    public List<SoldSpuExcelVO> excelSpuList(SpuPageSearchDTO spuDTO) {
//        StoreVO storeVO=null;
//        String storeName=null,storeCode=null;
//        Long storeId=spuDTO.getStoreId();
////        spuDTO.setShopId(AuthUserContext.get().getTenantId());
////        if(spuDTO.getShopId()!=null){
////            storeVO=storeFeignClient.findByStoreId(spuDTO.getShopId());
////            storeId=spuDTO.getShopId();
////        }
//        if(spuDTO.getStoreId()!=null){
//            storeVO=storeFeignClient.findByStoreId(spuDTO.getStoreId());
//            storeId=spuDTO.getStoreId();
//        }
//        if(storeVO!=null){
//            storeId=storeVO.getStoreId();
//            storeName=storeVO.getName();
//            storeCode=storeVO.getStoreCode();
//        }
//        if(spuDTO.getIphStatus()==null){
//            spuDTO.setIphStatus(1);
//        }
//        List<SpuPageVO> spulist = this.listSpuByCodes(spuDTO);
//        List<SoldSpuExcelVO> list=new ArrayList<>();
//        if(CollUtil.isEmpty(spulist)) {
//            return list;
//        }
//         list = spulist.stream().map(item->{
//             SoldSpuExcelVO soldSpuExcelVO=mapperFacade.map(item,SoldSpuExcelVO.class);
//             return soldSpuExcelVO;
//         }).collect(Collectors.toList());
//        List<Long> spuIds = new ArrayList<>();
//        for (SoldSpuExcelVO spuExcelVO : list) {
//            spuIds.add(spuExcelVO.getSpuId());
//        }
//        // 获取sku信息列表
//        String spuId=StringUtils.join(spuIds.toArray(),",");
//        log.info("----spuId---->"+spuId);
//        List<SpuExcelVO> skuList = skuService.excelSkuList(spuIds,storeId,null);
//        Map<Long, List<SpuExcelVO>> skuMap = skuList.stream().collect(Collectors.groupingBy(SpuExcelVO::getSpuId));
//        List<SoldSpuExcelVO> spuExcelList = new ArrayList<>();
//        // 店铺运费模板
////        ServerResponseEntity<List<ShopTransportVO>> response = deliveryFeignClient.listTransportByShopIdInsider(storeId);
//        ServerResponseEntity<List<ShopTransportVO>> response = deliveryFeignClient.listTransportByShopIdInsider(null);
//        if (!Objects.equals(response.getCode(), ResponseEnum.OK.value())) {
//            throw new LuckException(response.getMsg());
//        }
//        Map<Long, String> transportMap = response.getData().stream().collect(Collectors.toMap(ShopTransportVO::getTransportId, ShopTransportVO::getTransName));
//        int index = 1;
//        for (SoldSpuExcelVO spuExcelVO : list) {
//            List<SpuExcelVO> skuData = skuMap.get(spuExcelVO.getSpuId());
//            if (CollUtil.isEmpty(skuData)) {
//                continue;
//            }
//            spuExcelVO.setSeq(String.valueOf(index));
//            spuExcelVO.setDeliveryTemplate(transportMap.get(spuExcelVO.getDeliveryTemplateId()));
//            // 状态
//            if (Objects.equals(spuExcelVO.getStatus(), StatusEnum.ENABLE.value().toString())) {
//                spuExcelVO.setStatus(ENABLE);
//            } else if (Objects.equals(spuExcelVO.getStatus(), StatusEnum.DISABLE.value().toString())) {
//                spuExcelVO.setStatus(DISABLE);
//            } else if (Objects.equals(spuExcelVO.getStatus(), StatusEnum.OFFLINE.value().toString())) {
//                spuExcelVO.setStatus(OFFLINE);
//            } else if (Objects.equals(spuExcelVO.getStatus(), StatusEnum.WAIT_AUDIT.value().toString())) {
//                spuExcelVO.setStatus(WAIT_AUDIT);
//            }
//            //所属门店信息
//            spuExcelVO.setStoreName(storeName);
//            spuExcelVO.setStoreCode(storeCode);
//            //小程序路径
//            String scene="?id="+spuExcelVO.getSpuId();
//            if(storeId!=null){
//                scene=scene+"&s="+ NumberTo64.to64(storeId);
//            }
//            spuExcelVO.setWxmapath("pages/detail/detail"+scene);
//
//            handleSpuExcelInfo(null, skuData, spuExcelList, spuExcelVO);
//            index++;
//        }
//        return spuExcelList;
        return null;
    }

    private void handleSpuExcelInfo(Map<Long, CategoryVO> categoryMap, List<SpuExcelVO> skuData, List<SoldSpuExcelVO> spuExcelList, SoldSpuExcelVO spuExcelVO) {

        for (SpuExcelVO spuExcel : skuData) {
            //spu信息
            SoldSpuExcelVO soldSpuExcelVO=mapperFacade.map(spuExcelVO,SoldSpuExcelVO.class);

            //sku信息
            soldSpuExcelVO.setProperties(spuExcel.getProperties());
            soldSpuExcelVO.setPropertiesValues(spuExcel.getPropertieValues());
            soldSpuExcelVO.setPriceFee(conversionPrices(spuExcel.getPriceFee()));
            soldSpuExcelVO.setMarketPriceFee(conversionPrices(spuExcel.getMarketPriceFee()));
            soldSpuExcelVO.setSkuCode(spuExcel.getSkuCode());
            soldSpuExcelVO.setModelId(spuExcel.getModelId());
            soldSpuExcelVO.setPriceCode(spuExcel.getPriceCode());
            //库存
            soldSpuExcelVO.setStock(spuExcel.getStock());
            //销量
            soldSpuExcelVO.setSaleNum(spuExcelVO.getSaleNum());

            spuExcelList.add(soldSpuExcelVO);
        }
    }

//    @Override
//    public List<SpuExcelVO> excelSpuList(SpuPageSearchDTO spuDTO) {
//        spuDTO.setShopId(AuthUserContext.get().getTenantId());
//        List<SpuExcelVO> list = spuMapper.excelSpuList(spuDTO);
//        if(CollUtil.isEmpty(list)) {
//            return list;
//        }
//        Set<Long> categoryIds = new HashSet<>();
//        List<Long> spuIds = new ArrayList<>();
//        for (SpuExcelVO spuExcelVO : list) {
//            spuIds.add(spuExcelVO.getSpuId());
//            categoryIds.add(spuExcelVO.getCategoryId());
//            categoryIds.add(spuExcelVO.getShopCategoryId());
//        }
//        // 获取分类列表
//        List<CategoryVO> categoryList = categoryService.listByCategoryIds(categoryIds);
//        ProductLangUtil.categoryList(categoryList);
//        Map<Long, CategoryVO> categoryMap = categoryList.stream().collect(Collectors.toMap(CategoryVO::getCategoryId, c->c));
//        // 获取sku信息列表
//        List<SpuExcelVO> skuList = skuService.excelSkuList(spuIds);
//        Map<Long, List<SpuExcelVO>> skuMap = skuList.stream().collect(Collectors.groupingBy(SpuExcelVO::getSpuId));
//        List<SpuExcelVO> spuExcelList = new ArrayList<>();
//        // 店铺运费模板
//        ServerResponseEntity<List<ShopTransportVO>> response = deliveryFeignClient.listTransportByShopId(spuDTO.getShopId());
//        if (!Objects.equals(response.getCode(), ResponseEnum.OK.value())) {
//            throw new LuckException(response.getMsg());
//        }
//        Map<Long, String> transportMap = response.getData().stream().collect(Collectors.toMap(ShopTransportVO::getTransportId, ShopTransportVO::getTransName));
//        int index = 1;
//        for (SpuExcelVO spuExcelVO : list) {
//            List<SpuExcelVO> skuData = skuMap.get(spuExcelVO.getSpuId());
//            if (CollUtil.isEmpty(skuData)) {
//                continue;
//            }
//            spuExcelVO.setSeq(String.valueOf(index));
//            spuExcelVO.setDeliveryTemplate(transportMap.get(spuExcelVO.getDeliveryTemplateId()));
//            // 状态
//            if (Objects.equals(spuExcelVO.getStatus(), StatusEnum.ENABLE.value().toString())) {
//                spuExcelVO.setStatus(ENABLE);
//            } else if (Objects.equals(spuExcelVO.getStatus(), StatusEnum.DISABLE.value().toString())) {
//                spuExcelVO.setStatus(DISABLE);
//            } else if (Objects.equals(spuExcelVO.getStatus(), StatusEnum.OFFLINE.value().toString())) {
//                spuExcelVO.setStatus(OFFLINE);
//            } else if (Objects.equals(spuExcelVO.getStatus(), StatusEnum.WAIT_AUDIT.value().toString())) {
//                spuExcelVO.setStatus(WAIT_AUDIT);
//            }
//            // 配送方式
//            String delivery = "";
//            SpuVO.DeliveryModeVO deliveryModeVO = Json.parseObject(spuExcelVO.getDeliveryMode(), SpuVO.DeliveryModeVO.class);
//            if(deliveryModeVO!=null){
//                if (deliveryModeVO.getHasUserPickUp()!=null && deliveryModeVO.getHasUserPickUp()) {
//                    delivery = DeliveryType.STATION.description() + Constant.COMMA;
//                }
//                if (deliveryModeVO.getHasCityDelivery()!=null && deliveryModeVO.getHasCityDelivery()) {
//                    delivery = delivery + DeliveryType.SAME_CITY.description() + Constant.COMMA;
//                }
//                if (deliveryModeVO.getHasShopDelivery()!=null && deliveryModeVO.getHasShopDelivery()) {
//                    delivery = delivery + DeliveryType.DELIVERY.description() + Constant.COMMA;
//                }
//            }
//            spuExcelVO.setDeliveryMode(delivery);
//            handleSpuExcelInfo(categoryMap, skuData, spuExcelList, spuExcelVO);
//            index++;
//        }
//        return spuExcelList;
//    }
//
//    private void handleSpuExcelInfo(Map<Long, CategoryVO> categoryMap, List<SpuExcelVO> skuData, List<SpuExcelVO> spuExcelList, SpuExcelVO spuExcelVO) {
//
//        String brandName = null;
//        if (CollUtil.isNotEmpty(spuExcelVO.getBrandLangList())) {
//            BrandLangVO brandLang = ProductLangUtil.handelBrandLang(spuExcelVO.getBrandLangList());
//            brandName = brandLang.getName();
//        }
////         名称、卖点
//        for (SpuLangVO spuLangVO : spuExcelVO.getSpuLangList()) {
//            if (Objects.equals(spuLangVO.getLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
//                spuExcelVO.setNameZh(spuLangVO.getSpuName());
//                spuExcelVO.setSellingPointZh(spuLangVO.getSellingPoint());
//            } else if (Objects.equals(spuLangVO.getLang(), LanguageEnum.LANGUAGE_EN.getLang())) {
//                spuExcelVO.setNameEn(spuLangVO.getSpuName());
//                spuExcelVO.setSellingPointEn(spuLangVO.getSellingPoint());
//            }
//        }
//        String categoryName = null;
//        String shopCategoryName = null;
//        // 平台分类
//        CategoryVO categoryVO = categoryMap.get(spuExcelVO.getCategoryId());
//        if (Objects.nonNull(categoryVO)) {
//            categoryName = categoryVO.getName();
//        }
//        // 店铺分类
//        CategoryVO shopCategoryVO = categoryMap.get(spuExcelVO.getShopCategoryId());
//        if (Objects.nonNull(shopCategoryVO)) {
//            shopCategoryName = shopCategoryVO.getName();
//        }
//        String delivery = spuExcelVO.getDeliveryMode();
//        if(StrUtil.isNotBlank(delivery)){
//            delivery = delivery.substring(0, delivery.length() -1);
//        }
//        for (SpuExcelVO spuExcel : skuData) {
//            spuExcel.setPriceFee(conversionPrices(spuExcel.getPriceFee()));
//            spuExcel.setMarketPriceFee(conversionPrices(spuExcel.getMarketPriceFee()));
//            spuExcel.setSeq(spuExcelVO.getSeq());
//            spuExcel.setDeliveryTemplate(spuExcelVO.getDeliveryTemplate());
//            spuExcel.setBrandName(brandName);
//            spuExcel.setNameZh(spuExcelVO.getNameZh());
//            spuExcel.setNameEn(spuExcelVO.getNameEn());
//            spuExcel.setSellingPointZh(spuExcelVO.getSellingPointZh());
//            spuExcel.setSellingPointEn(spuExcelVO.getSellingPointEn());
//            spuExcel.setCategoryName(categoryName);
//            spuExcel.setShopCategoryName(shopCategoryName);
//            spuExcel.setDeliveryMode(delivery);
//            spuExcel.setStatus(spuExcelVO.getStatus());
//            spuExcelList.add(spuExcel);
//        }
//    }

    @Override
    public void exportExcel(List<SpuExcelVO> spuList, Map<Integer, List<String>> errorMap) {
        Long shopId = AuthUserContext.get().getTenantId();
        Set<Long> brandIds = new HashSet<>();
        // 平台分类
        Map<String, Long> categoryMap = getCategoryList(Constant.PLATFORM_SHOP_ID, spuList);
        // 店铺分类
        Map<String, Long> shopCategoryMap = getCategoryList(shopId, spuList);
        if (MapUtil.isEmpty(shopCategoryMap)) {
            addSpuExcelError(errorMap, SpuExportError.OTHER, "请先创建店铺分类");
            return;
        }
        // 运费模板
        ServerResponseEntity<List<ShopTransportVO>> response = deliveryFeignClient.listTransportByShopId(shopId);
        Map<String, Long> transportMap = response.getData().stream().collect(Collectors.toMap(ShopTransportVO::getTransName, ShopTransportVO::getTransportId));
        if (MapUtil.isEmpty(transportMap)) {
            addSpuExcelError(errorMap, SpuExportError.OTHER, "请先创建运费模板");
            return;
        }
        // 品牌
        Set<String> brandNames = new HashSet<>();
        for (SpuExcelVO spuExcel : spuList) {
            brandNames.add(spuExcel.getBrandName());
        }
        List<BrandLang> brandList = brandService.listBrandLangByBrandNames(brandNames, shopId);
        Map<String, List<BrandLang>> brandMap = brandList.stream().collect(Collectors.groupingBy(BrandLang::getName));
        Map<String, List<SpuExcelVO>> spuMap = spuList.stream().collect(Collectors.groupingBy(SpuExcelVO::getSeq));
        for (String seq : spuMap.keySet()) {
            List<SpuExcelVO> spuExcelList = spuMap.get(seq);
            SpuDTO spu = new SpuDTO();
            spu.setShopId(shopId);
            try {
                loadSpuData(spuExcelList, spu, categoryMap, shopCategoryMap, transportMap, brandMap, errorMap);
            } catch (Exception e) {
                addSpuExcelError(errorMap, SpuExportError.PRODUCT_DATA, e.getMessage());
                continue;
            }
            if (Objects.nonNull(spu.getBrandId())) {
                brandIds.add(spu.getBrandId());
            }
        }
        brandService.updateSpuCountByBrandIds(brandIds);
    }

    private void addSpuExcelError(Map<Integer, List<String>> errorMap, SpuExportError spuExportError, String error) {
        List list = errorMap.get(spuExportError.value());
        if (CollUtil.isEmpty(list)) {
            list = new ArrayList();
            errorMap.put(spuExportError.value(), list);
        }
        list.add(error);
    }

    private Map<String, Long> getCategoryList(Long shopId, List<SpuExcelVO> spuList) {
        List<CategoryVO> categoryList;
        if (Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
            categoryList = categoryService.getShopSigningCategoryAndLangInfo(AuthUserContext.get().getTenantId());
        } else {
            categoryList = categoryService.listAndLangInfoByShopId(shopId);
        }
        categoryList.sort(Comparator.comparing(CategoryVO::getSeq).reversed().thenComparing(CategoryVO::getCategoryId));
        Map<String, Long> categoryNameMap = new HashMap<>(categoryList.size());
        Map<String, Long> categoryNameEnMap = new HashMap<>(categoryList.size());
        for (CategoryVO categoryVO : categoryList) {
            boolean isParentCategory = Objects.equals(categoryVO.getLevel(), CategoryLevel.First.value()) ||
                    (Objects.equals(shopId, Constant.PLATFORM_SHOP_ID) && Objects.equals(categoryVO.getLevel(), CategoryLevel.SECOND.value()));
            if (isParentCategory) {
                continue;
            }
            for (CategoryLangVO categoryLangVO : categoryVO.getCategoryLangList()) {
                String name = categoryLangVO.getName();
                if(Objects.equals(categoryLangVO.getLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang()) && !categoryNameMap.containsKey(name)) {
                    categoryNameMap.put(name, categoryVO.getCategoryId());
                } else if(Objects.equals(categoryLangVO.getLang(), LanguageEnum.LANGUAGE_EN.getLang()) && !categoryNameMap.containsKey(name)) {
                    categoryNameEnMap.put(name, categoryVO.getCategoryId());
                }
            }
        }
        for (String key : categoryNameEnMap.keySet()) {
            if (!categoryNameMap.containsKey(key)) {
                categoryNameMap.put(key, categoryNameEnMap.get(key));
            }
        }
        return categoryNameMap;
    }

    @Override
    public String spuExportError(Map<Integer, List<String>> errorMap) {
        StringBuilder info = new StringBuilder();
        for (Integer key : errorMap.keySet()) {
            List<String> list = errorMap.get(key);
            SpuExportError spuExportError = SpuExportError.instance(key);
            if (Objects.equals(spuExportError, SpuExportError.OTHER)) {
                info.append(list.toString() + "\n");
                continue;
            }
            info.append(spuExportError.errorInfo() + list.toString() + "\n");
        }
        return info.toString();
    }

    @Override
    public void downloadModel(HttpServletResponse response) {
        Long shopId = AuthUserContext.get().getTenantId();
        List<SpuExcelVO> list = new ArrayList<>();
        list.add(new SpuExcelVO());
        Map<Integer, String[]> map = new HashMap<>(8);
        map.put(DELIVERY_MODE_INDEX, DELIVERY_MODE);
        map.put(STATUS_INDEX, STATUS_MODE);
        ServerResponseEntity<List<ShopTransportVO>> responseEntity = deliveryFeignClient.listTransportByShopId(shopId);
        List<ShopTransportVO> data = responseEntity.getData();
        if (CollUtil.isNotEmpty(data)) {
            String[] transNames = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                ShopTransportVO transport = data.get(i);
                transNames[i] = transport.getTransName();
            }
            map.put(DELIVERY_TEMPLATE_INDEX, transNames);
        }
        // 平台分类
        List<CategoryShopVO> categoryShopList = categoryShopService.listByShopId(shopId, I18nMessage.getLang());
        List<String> categoryNames = categoryShopList.stream().map(CategoryShopVO::getName).collect(Collectors.toList());
        map.put(CATEGORY_INDEX, categoryNames.toArray(new String[categoryNames.size()]));
        //店铺分类
        List<CategoryVO> shopCategoryList = categoryService.listAndLangInfoByShopId(shopId);
        ProductLangUtil.categoryList(shopCategoryList);
        List<String> shopCategoryNames = shopCategoryList.stream().map(CategoryVO::getName).collect(Collectors.toList());
        map.put(SHOP_CATEGORY_INDEX, shopCategoryNames.toArray(new String[shopCategoryNames.size()]));
        ExcelWriter excelWriter = null;
        try {
            // 先执行合并策略
            ExcelWriterBuilder excelWriterMerge = ExcelUtil.getExcelWriterMerge(response, SpuExcelVO.EXCEL_NAME, SpuExcelVO.MERGE_ROW_INDEX, SpuExcelVO.MERGE_COLUMN_INDEX);
            excelWriter = ExcelUtil.getExcelModel(excelWriterMerge, map, Constant.START_ROW).build();
            // 业务代码
            if (CollUtil.isNotEmpty(list)) {
                // 进行写入操作
                WriteSheet sheetWriter = EasyExcel.writerSheet(SpuExcelVO.SHEET_NAME).head(SpuExcelVO.class).build();
                excelWriter.write(list,sheetWriter);
            }
        } catch (Exception e) {
            log.error("导出excel失败", e);
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    @Override
    public List<SpuPageVO> listSpuByCodes(SpuPageSearchDTO searchDTO) {
//        return spuMapper.listPageVO(searchDTO);
        return spuService.listSpu(searchDTO);
    }

    @DS("slave")
    @Override
    public List<SimpleSpuExcelVO> listSimpleSpuExcelVO() {
        return spuMapper.listSimpleSpuExcelVO();
    }

    @Override
    public ServerResponseEntity<List<SpuPageVO>> readExcelSpuCode(MultipartFile multipartFile) {
        try {
            File file = FileUtil.transferFile(multipartFile);
            InputStream inputStream = null;
            inputStream = new FileInputStream(file);
            ExcelUtils<SpuCodeReadExcelVO> excelUtil = new ExcelUtils(SpuCodeReadExcelVO.class);
            List<SpuCodeReadExcelVO> list = excelUtil.importExcel("批量导入数据", inputStream);

            List<SpuCodeReadExcelVO> readList = list.stream()
                    .filter(item -> StrUtil.isNotBlank(item.getSpuCode()))
                    .collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(
                                    SpuCodeReadExcelVO::getSpuCode))), ArrayList::new));

            readList.stream().forEach(spuCodeReadExcelVO -> {
                if(Objects.isNull(spuCodeReadExcelVO.getSeq())){
                    throw new LuckException("商品货号:"+spuCodeReadExcelVO.getSpuCode()+"序号不能为空");
                }
            });

            List<String> spuCodes = readList.stream().map(SpuCodeReadExcelVO::getSpuCode).collect(Collectors.toList());
            SpuPageSearchDTO spuPageSearchDTO=new SpuPageSearchDTO();
            spuPageSearchDTO.setSpuCodeExcelList(spuCodes);
            List<SpuPageVO> spuPageVOS=this.listSpuByCodes(spuPageSearchDTO);

            if(CollectionUtil.isNotEmpty(spuPageVOS)){
                Map<String, SpuCodeReadExcelVO> spuNameMap = readList.stream().collect(Collectors.toMap(SpuCodeReadExcelVO::getSpuCode,s->s));

                spuPageVOS.forEach(spuPageVO -> {
                    if(spuNameMap.containsKey(spuPageVO.getSpuCode())){
                        spuPageVO.setSeq(spuNameMap.get(spuPageVO.getSpuCode()).getSeq());
                    }
                });
                Collections.sort(spuPageVOS, new Comparator<SpuPageVO>() {
                    public int compare(SpuPageVO o1, SpuPageVO o2) {
                        //升序
                        if(Objects.nonNull(o1.getSeq()) && Objects.nonNull(o2.getSeq())){
                            return o1.getSeq().compareTo(o2.getSeq());
                        }
                        return 0;
                    }
                });
            }

            return ServerResponseEntity.success(spuPageVOS);
        } catch (LuckException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导入商品组件模版文件 操作失败--->",e,e.getMessage());
            throw new LuckException("操作失败，请检查模板及内容是否正确");
        }
//        return ServerResponseEntity.success();
    }

    @Override
    public String readExcelSpuAbbr(MultipartFile file) {
        SpuAbbrListener spuAbbrListener = new SpuAbbrListener(spuMapper);
        try {
            EasyExcel.read(file.getInputStream(), SpuAbbrReqDto.class, spuAbbrListener).sheet().doRead();
        } catch (IOException e) {
            throw new LuckException("操作失败：{}",e);
        }
        return null;
    }

    /**
     * 获取spu信息
     * @param spuList
     * @param spu
     * @param categoryMap 平台分类
     * @param shopCategoryMap 店铺分类
     * @param transportMap 运费模板
     * @param brandMap 品牌
     */
    private void loadSpuData(List<SpuExcelVO> spuList, SpuDTO spu, Map<String, Long> categoryMap,Map<String, Long> shopCategoryMap,
                             Map<String, Long> transportMap, Map<String, List<BrandLang>> brandMap, Map<Integer, List<String>> errorMap) {
        boolean init = false;
        List<SkuDTO> skuList = new ArrayList<>();
        for (SpuExcelVO spuExcelVO : spuList) {
            if (!init) {
                if (StrUtil.isBlank(spuExcelVO.getNameZh())) {
                    addSpuExcelError(errorMap, SpuExportError.SPU_NAME, spuExcelVO.getSeq());
                    return;
                }
                spu.setSpuLangList(new ArrayList<>());
                spu.getSpuLangList().add(new SpuLangDTO(LanguageEnum.LANGUAGE_ZH_CN.getLang(), spuExcelVO.getNameZh(), spuExcelVO.getSellingPointZh()));
                if (StrUtil.isNotBlank(spuExcelVO.getNameEn()) || StrUtil.isNotBlank(spuExcelVO.getSellingPointEn())) {
                    spu.getSpuLangList().add(new SpuLangDTO(LanguageEnum.LANGUAGE_EN.getLang(), spuExcelVO.getNameEn(), spuExcelVO.getSellingPointEn()));
                }
                spu.setBrandId(handleBrand(brandMap.get(spuExcelVO.getBrandName())));
                // 平台分类
                Long categoryId = categoryMap.get(spuExcelVO.getCategoryName());
                if (Objects.isNull(categoryId)) {
                    addSpuExcelError(errorMap, SpuExportError.PLATFORM_CATEGORY, spuExcelVO.getSeq());
                    return;
                }
                spu.setCategoryId(categoryId);
                // 店铺分类
                Long shopCategoryId = shopCategoryMap.get(spuExcelVO.getShopCategoryName());
                if (Objects.isNull(shopCategoryId)) {
                    addSpuExcelError(errorMap, SpuExportError.SHOP_CATEGORY, spuExcelVO.getSeq());
                    return;
                }
                spu.setShopCategoryId(shopCategoryId);
                // 运费模板
                Long deliveryTemplateId = transportMap.get(spuExcelVO.getDeliveryTemplate());
                if (Objects.isNull(deliveryTemplateId)) {
                    addSpuExcelError(errorMap, SpuExportError.DELIVER, spuExcelVO.getSeq());
                    return;
                }
                spu.setDeliveryTemplateId(deliveryTemplateId);
                spu.setDeliveryMode(getDeliveryMode(spuExcelVO.getDeliveryMode()));
                spu.setPriceFee(Constant.MIN_SPU_AMOUNT);
                spu.setMarketPriceFee(Constant.MIN_SPU_AMOUNT);
                if (Objects.equals(spuExcelVO.getStatus(), ENABLE)) {
                    spu.setStatus(StatusEnum.ENABLE.value());
                } else {
                    spu.setStatus(StatusEnum.DISABLE.value());
                }
                spu.setHasSkuImg(0);
                spu.setSpuType(SpuType.NORMAL.value());
                spu.setIsCompose(0);
                spu.setSeq(0);
                spu.setScoreFee(Constant.ZERO_LONG);
                init = true;
            }
            SkuDTO sku;
            try{
                sku = loadSkuData(spuExcelVO, spu, errorMap);
            } catch (Exception e) {
                if (!Objects.equals(e.getMessage(), ResponseEnum.DATA_ERROR.value())) {
                    addSpuExcelError(errorMap, SpuExportError.PRODUCT_DATA, spuExcelVO.getSeq());
                }
                return;
            }
            skuList.add(sku);
        }
        spu.setSkuList(skuList);
        spuService.save(spu);
    }

    private Long handleBrand(List<BrandLang> brandLangList) {
        if (CollUtil.isEmpty(brandLangList)) {
            return null;
        }
        if (brandLangList.size() > 1) {
            brandLangList.sort(Comparator.comparing(BrandLang::getLang).thenComparing(BrandLang::getBrandId).reversed());
        }
        return brandLangList.get(0).getBrandId();
    }


    /**
     * 获取spu配送类型信息
     * @param data
     * @return
     */
    private String getDeliveryMode(String data) {
        SpuVO.DeliveryModeVO deliveryModeVO = new SpuVO.DeliveryModeVO();
        // 默认支持快递
        deliveryModeVO.setHasShopDelivery(Boolean.TRUE);
        deliveryModeVO.setHasCityDelivery(StrUtil.contains(data, DeliveryType.SAME_CITY.description()));
        deliveryModeVO.setHasUserPickUp(StrUtil.contains(data, DeliveryType.STATION.description()));
        return Json.toJsonString(deliveryModeVO);
    }

    /**
     * 获取sku数据
     * @param spuExcelVO
     * @param spu
     * @param errorMap
     * @return
     */
    private SkuDTO loadSkuData(SpuExcelVO spuExcelVO, SpuDTO spu, Map<Integer, List<String>> errorMap) {
        spu.setTotalStock(0);
        SkuDTO sku = new SkuDTO();
        sku.setSkuLangList(new ArrayList<>());
        StrUtil.isNotBlank(sku.getSkuName());
        sku.setPriceFee(conversionPricesToLong(spuExcelVO.getPriceFee()));
        sku.setMarketPriceFee(conversionPricesToLong(spuExcelVO.getMarketPriceFee()));
        sku.setStatus(StatusEnum.ENABLE.value());
        sku.setStock(getStock(spuExcelVO.getStock()));
        spu.setTotalStock(spu.getTotalStock() + sku.getStock());
        sku.setPartyCode(spuExcelVO.getPartyCode());
        sku.setModelId(spuExcelVO.getModelId());
        sku.setWeight(spuExcelVO.getWeight());
        sku.setVolume(spuExcelVO.getVolume());
        sku.setScoreFee(Constant.ZERO_LONG);
        if (sku.getPriceFee() > spu.getPriceFee()) {
            spu.setPriceFee(sku.getPriceFee());
        }
        if (sku.getMarketPriceFee() > spu.getMarketPriceFee()) {
            spu.setMarketPriceFee(sku.getMarketPriceFee());
        }
        boolean blankSkuNameZh = StrUtil.isBlank(spuExcelVO.getSkuNameZh());
        boolean blankPropertiesZh = StrUtil.isBlank(spuExcelVO.getPropertiesZh());
        boolean blankSkuNameEn = StrUtil.isBlank(spuExcelVO.getSkuNameEn());
        boolean blankPropertiesEn = StrUtil.isBlank(spuExcelVO.getPropertiesEn());
        if (blankSkuNameZh && blankPropertiesZh && blankSkuNameEn && blankPropertiesEn) {
            return sku;
        }
        // 属性值及sku名称状态必须一致（都为空，或都有值）
        else if (!Objects.equals(blankPropertiesZh, blankSkuNameZh) || !Objects.equals(blankPropertiesEn, blankSkuNameEn)) {
            addSpuExcelError(errorMap, blankPropertiesZh ? SpuExportError.SKU_NAME : SpuExportError.PROPERTIES, spuExcelVO.getSeq());
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        // 英文属性有值时，中文属性不能为空
        else if (!blankPropertiesEn && blankPropertiesZh) {
            addSpuExcelError(errorMap, SpuExportError.PROPERTIES, spuExcelVO.getSeq());
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        // 英文sku有值时，中文sku不能为空
        else if (!blankSkuNameEn && blankSkuNameZh) {
            addSpuExcelError(errorMap, SpuExportError.SPU_NAME, spuExcelVO.getSeq());
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }

        sku.setSpuSkuAttrValues(new ArrayList<>());
        String[] propertiesZh = spuExcelVO.getPropertiesZh().split(Constant.SEMICOLON);
        String[] propertiesEn = spuExcelVO.getPropertiesEn().split(Constant.SEMICOLON);
        if (!Objects.equals(propertiesZh.length, propertiesEn.length)) {
            addSpuExcelError(errorMap, SpuExportError.SKU_NAME, spuExcelVO.getSeq());
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        StringBuilder attrsZh = new StringBuilder();
        StringBuilder attrsEn = new StringBuilder();
        for (int i = 0;i < propertiesZh.length;i++) {
            SpuSkuAttrValueDTO spuSkuAttrValue = new SpuSkuAttrValueDTO();
            spuSkuAttrValue.setSpuSkuAttrValueLangList(new ArrayList<>());
            setSpuSkuAttrValueLang(spuSkuAttrValue, propertiesZh[i], attrsZh, LanguageEnum.LANGUAGE_ZH_CN.getLang());
            if (propertiesEn.length > i) {
                setSpuSkuAttrValueLang(spuSkuAttrValue, propertiesEn[i], attrsEn, LanguageEnum.LANGUAGE_EN.getLang());
            }
            sku.getSpuSkuAttrValues().add(spuSkuAttrValue);
        }
        attrsZh.deleteCharAt(attrsZh.lastIndexOf(Constant.COMMA));
        // sku国际化信息
        sku.getSkuLangList().add(new SkuLangDTO(LanguageEnum.LANGUAGE_ZH_CN.getLang(), spuExcelVO.getSkuNameZh(), attrsZh.toString()));
        if (!blankSkuNameEn) {
            attrsEn.deleteCharAt(attrsEn.lastIndexOf(Constant.COMMA));
            sku.getSkuLangList().add(new SkuLangDTO(LanguageEnum.LANGUAGE_EN.getLang(), spuExcelVO.getSkuNameEn(), attrsEn.toString()));
        }

        return sku;
    }
    /**
     * 插入属性数据
     * @param spuSkuAttrValue
     * @param property
     * @param attrs
     * @param lang
     */
    private void setSpuSkuAttrValueLang(SpuSkuAttrValueDTO spuSkuAttrValue, String property, StringBuilder attrs, Integer lang) {
        String[] attr = property.split(Constant.COLON);
        attrs.append(attr[0] + Constant.COMMA);
        SpuSkuAttrValueLangDTO spuSkuAttrValueLangDTO = new SpuSkuAttrValueLangDTO();
        spuSkuAttrValueLangDTO.setLang(lang);
        spuSkuAttrValueLangDTO.setAttrName(attr[0]);
        spuSkuAttrValueLangDTO.setAttrValueName(attr[1]);
        spuSkuAttrValue.getSpuSkuAttrValueLangList().add(spuSkuAttrValueLangDTO);
    }

    /**
     * 获取sku库存
     * @param stock
     * @return
     */
    private Integer getStock(Integer stock) {
        Integer minStock = 0;
        if (Objects.isNull(stock) || stock < minStock) {
            return minStock;
        }
        return stock;
    }
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
    /**
     * 金额Double格式转换Long
     * @param num
     * @return
     */
    private Long conversionPricesToLong (String num) {
        long amount;
        try {
            amount = Math.round(Arith.mul(Double.parseDouble(num), Constant.PRICE_MAGNIFICATION));
            if (amount < Constant.MIN_SPU_AMOUNT) {
                amount = Constant.MIN_SPU_AMOUNT;
            }
            if (amount > Constant.MAX_SPU_AMOUNT) {
                amount = Constant.MAX_SPU_AMOUNT;
            }
        } catch (Exception e) {
            amount = Constant.MIN_SPU_AMOUNT;
        }
        return amount;
    }
}
