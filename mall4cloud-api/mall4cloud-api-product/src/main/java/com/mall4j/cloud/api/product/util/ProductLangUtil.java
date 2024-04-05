package com.mall4j.cloud.api.product.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.order.vo.OrderSkuLangVO;
import com.mall4j.cloud.common.order.vo.OrderSpuLangVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.product.vo.app.BrandAppVO;
import com.mall4j.cloud.common.util.LangUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单语言工具类
 * @author YXF
 * @date 2021/05/19
 */
public class ProductLangUtil {

    /**
     * ===========================spu===================================
     */

    /**
     * spu国际化信息
     * @param spuVO sku信息
     */
    public static void spu(SpuVO spuVO) {
        handleSpu(spuVO);
    }

    /**
     * spu列表国际化信息
     * @param spuList spu列表
     */
    public static void spuList(List<SpuVO> spuList) {
        for (SpuVO spuVO : spuList) {
            handleSpu(spuVO);
        }
    }

    /**
     * 商品详情
     *
     * @param spu
     */
    public static void spuDetail(SpuVO spu) {
        if (CollUtil.isEmpty(spu.getDetailList())) {
            return;
        }
        Map<Integer, String> map = spu.getDetailList().stream().collect(Collectors.toMap(SpuDetailVO::getLang, SpuDetailVO::getDetail));
        spu.setDetail(LangUtil.getLangValue(map));
    }

    /**
     * 获取商品名称
     * @param spuVO 商品信息
     */
    private static void handleSpu(SpuVO spuVO) {
        if (CollUtil.isEmpty(spuVO.getSpuLangList())) {
            return;
        }
        Map<Integer, String> spuNameMap = spuVO.getSpuLangList().stream().filter(spuLangVO -> StrUtil.isNotBlank(spuLangVO.getSpuName())).collect(Collectors.toMap(SpuLangVO::getLang, SpuLangVO::getSpuName));
        Map<Integer, String> sellingPointMap = spuVO.getSpuLangList().stream().filter(spuLangVO -> StrUtil.isNotBlank(spuLangVO.getSellingPoint())).collect(Collectors.toMap(SpuLangVO::getLang, SpuLangVO::getSellingPoint));
        spuVO.setName(LangUtil.getLangValue(spuNameMap));
        spuVO.setSellingPoint(LangUtil.getLangValue(sellingPointMap));
        spuVO.setSpuLangList(null);
    }

    /**
     * ===========================sku===================================
     */

    /**
     * sku国际化信息
     * @param skuVO sku信息
     */
    public static void sku(SkuVO skuVO) {
        handleSku(skuVO);
    }

    /**
     * sku国际化信息
     * @param skuList sku列表
     */
    public static void skuList(List<SkuVO> skuList) {
        for (SkuVO skuVO : skuList) {
            handleSku(skuVO);
        }
    }

    /**
     * 获取sku名称
     * @param skuVO sku信息
     */
    public static void handleSku(SkuVO skuVO) {
        Map<Integer, String> skuMap = skuVO.getSkuLangList().stream().filter(skuLangVO -> StrUtil.isNotBlank(skuLangVO.getSkuName())).collect(Collectors.toMap(SkuLangVO::getLang, SkuLangVO::getSkuName));
        skuVO.setSkuName(LangUtil.getLangValue(skuMap));
        skuVO.setSkuLangList(null);
        if (CollUtil.isNotEmpty(skuVO.getSpuSkuAttrValues())) {
            spuSkuAttrValueList(skuVO.getSpuSkuAttrValues());
        }
    }


    /**
     * ===========================sku属性信息===================================
     */

    /**
     * sku属性国际化信息
     * @param spuSkuAttrValueList sku属性列表
     */
    public static void spuSkuAttrValueList(List<SpuSkuAttrValueVO> spuSkuAttrValueList) {
        Integer lang = I18nMessage.getLang();
        for (SpuSkuAttrValueVO spuSkuAttrValueVO : spuSkuAttrValueList) {
            handleSpuSkuAttrValue(spuSkuAttrValueVO, lang);
        }
    }
    /**
     * sku属性国际化信息
     * @param spuSkuAttrValueList sku属性列表
     */
    public static void spuSkuAttrValueList(List<SpuSkuAttrValueVO> spuSkuAttrValueList, Integer lang) {
        for (SpuSkuAttrValueVO spuSkuAttrValueVO : spuSkuAttrValueList) {
            handleSpuSkuAttrValue(spuSkuAttrValueVO, lang);
        }
    }

    /**
     * 获取sku属性名称
     * @param spuSkuAttrValueVO sku属性信息
     */
    public static void handleSpuSkuAttrValue(SpuSkuAttrValueVO spuSkuAttrValueVO, Integer lang) {
        if (CollUtil.isEmpty(spuSkuAttrValueVO.getSpuSkuAttrValueLangList())) {
            return;
        }
        Map<Integer, SpuSkuAttrValueLangVO> map = spuSkuAttrValueVO.getSpuSkuAttrValueLangList().stream().collect(Collectors.toMap(SpuSkuAttrValueLangVO::getLang, s -> s));
        if (MapUtil.isEmpty(map)) {
            return;
        }
        SpuSkuAttrValueLangVO spuSkuAttrValueLangVO;
        if (Objects.nonNull(map.get(lang))) {
            spuSkuAttrValueLangVO = map.get(lang);
        } else {
            spuSkuAttrValueLangVO = map.get(Constant.DEFAULT_LANG);
        }
        spuSkuAttrValueVO.setAttrName(spuSkuAttrValueLangVO.getAttrName());
        spuSkuAttrValueVO.setAttrValueName(spuSkuAttrValueLangVO.getAttrValueName());
    }

    /**
     * ===========================属性===================================
     */

    /**
     * 属性国际化信息
     * @param attrVO 属性信息
     */
    public static void attr(AttrVO attrVO) {
        handleAttr(attrVO);
    }

    /**
     * 属性国际化信息
     * @param attrList 属性列表
     */
    public static void attrList(List<AttrVO> attrList) {
        for (AttrVO attrVO : attrList) {
            handleAttr(attrVO);
        }
    }

    /**
     * 获取分类名称
     * @param attrVO 属性国际化信息列表
     */
    public static void handleAttr(AttrVO attrVO) {
        Map<Integer, String> attrNameMap = attrVO.getAttrLangList().stream().collect(Collectors.toMap(AttrLangVO::getLang, AttrLangVO::getName));
        Map<Integer, String> attrDescMap = attrVO.getAttrLangList().stream().collect(Collectors.toMap(AttrLangVO::getLang, AttrLangVO::getName));
        attrVO.setName(LangUtil.getLangValue(attrNameMap));
        attrVO.setDesc(LangUtil.getLangValue(attrDescMap));
        for (AttrValueVO attrValue : attrVO.getAttrValues()) {
            Map<Integer, String> valueMap = attrValue.getValues().stream().collect(Collectors.toMap(AttrValueLangVO::getLang, AttrValueLangVO::getValue));
            attrValue.setValue(LangUtil.getLangValue(valueMap));
        }
    }

    /**
     * ===========================分类===================================
     */

    /**
     * 分类国际化信息
     * @param categoryVO 分类信息
     */
    public static void category(CategoryVO categoryVO) {
        handleCategory(categoryVO);
    }

    /**
     * 分类国际化信息
     * @param categoryList 分类列表
     */
    public static void categoryList(List<CategoryVO> categoryList) {
        for (CategoryVO categoryVO : categoryList) {
            category(categoryVO);
        }
    }

    /**
     * 处理分类国际化信息
     * @param categoryVO 分类国际化信息列表
     */
    public static void handleCategory(CategoryVO categoryVO) {
        if (CollUtil.isEmpty(categoryVO.getCategoryLangList())) {
            return;
        }
        Map<Integer, String> map = categoryVO.getCategoryLangList().stream().collect(Collectors.toMap(CategoryLangVO::getLang, CategoryLangVO::getName));
        categoryVO.setName(LangUtil.getLangValue(map));
        categoryVO.setCategoryLangList(null);
    }

    /**
     * ===========================品牌===================================
     */

    /**
     * 品牌国际化信息
     * @param brandVO 品牌信息
     */
    public static void brand(BrandVO brandVO) {
        if (Objects.isNull(brandVO)) {
            return;
        }
        handelBrand(brandVO);
    }


    /**
     * 品牌国际化信息
     * @param brandList 品牌信息
     */
    public static void brandList(List<BrandVO> brandList) {
        if (CollUtil.isEmpty(brandList)) {
            return;
        }
        for (BrandVO brandVO : brandList) {
            handelBrand(brandVO);
        }
    }


    /**
     * 品牌国际化信息
     * @param brandList 品牌信息
     */
    public static void brandAppList(List<BrandAppVO> brandList) {
        if (CollUtil.isEmpty(brandList)) {
            return;
        }
        for (BrandAppVO brandVO : brandList) {
            handelBrand(brandVO);
        }
    }



    /**
     * 处理分类国际化信息
     * @param brandVO 品牌信息
     */
    public static void handelBrand(BrandVO brandVO) {
        BrandLangVO brandLangVO = handelBrandLang(brandVO.getBrandLangList());
        brandVO.setName(brandLangVO.getName());
        brandVO.setDesc(brandLangVO.getDesc());
        brandVO.setBrandLangList(null);
    }

    /**
     * 处理分类国际化信息
     * @param brandVO 品牌信息
     */
    public static void handelBrand(BrandAppVO brandVO) {
        BrandLangVO brandLangVO = handelBrandLang(brandVO.getLangList());
        brandVO.setName(brandLangVO.getName());
        brandVO.setLangList(null);
    }
    /**
     * 处理分类国际化信息
     * @param brandLangList 品牌语言列表信息
     */
    public static BrandLangVO handelBrandLang(List<BrandLangVO> brandLangList) {
        Map<Integer, BrandLangVO> map = brandLangList.stream().collect(Collectors.toMap(BrandLangVO::getLang, b -> b));
        BrandLangVO brandLangVO = map.get(I18nMessage.getLang());
        if (Objects.isNull(brandLangVO)) {
            brandLangVO = map.get(Constant.DEFAULT_LANG);
        }
        return brandLangVO;
    }

    /**
     * 处理购物车项国际化信息
     * @param shopCartItems
     */
    public static void shopCartItemList(List<ShopCartItemVO> shopCartItems) {
        for (ShopCartItemVO shopCartItem : shopCartItems) {
            Map<Integer, String> spuMap = shopCartItem.getSpuLangList().stream().collect(Collectors.toMap(OrderSpuLangVO::getLang, OrderSpuLangVO::getSpuName));
            Map<Integer, String> skuMap = shopCartItem.getSkuLangList().stream().collect(Collectors.toMap(OrderSkuLangVO::getLang, OrderSkuLangVO::getSkuName));
            shopCartItem.setSpuName(LangUtil.getLangValue(spuMap));
            shopCartItem.setSkuName(LangUtil.getLangValue(skuMap));
        }

    }
}
