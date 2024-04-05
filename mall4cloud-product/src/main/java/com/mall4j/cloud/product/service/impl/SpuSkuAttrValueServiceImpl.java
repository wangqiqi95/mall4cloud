package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueDTO;
import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueLangDTO;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LangUtil;
import com.mall4j.cloud.product.mapper.SpuSkuAttrValueLangMapper;
import com.mall4j.cloud.product.mapper.SpuSkuAttrValueMapper;
import com.mall4j.cloud.product.model.SpuAttrValue;
import com.mall4j.cloud.product.model.SpuSkuAttrValue;
import com.mall4j.cloud.product.model.SpuSkuAttrValueLang;
import com.mall4j.cloud.product.service.AttrService;
import com.mall4j.cloud.product.service.SpuSkuAttrValueService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品sku销售属性关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
public class SpuSkuAttrValueServiceImpl extends ServiceImpl<SpuSkuAttrValueMapper,SpuSkuAttrValue> implements SpuSkuAttrValueService {

    @Autowired
    private SpuSkuAttrValueMapper spuSkuAttrValueMapper;
    @Autowired
    private SpuSkuAttrValueLangMapper spuSkuAttrValueLangMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private AttrService attrService;

    @Override
    public void save(List<SpuSkuAttrValueDTO> spuSkuAttrValueList) {
        if (CollUtil.isEmpty(spuSkuAttrValueList)) {
            return;
        }
        spuSkuAttrValueMapper.batchSave(spuSkuAttrValueList);
        List<SpuSkuAttrValueLang> spuSkuAttrValueLangList = loadSpuSkuAttrValueInfo(spuSkuAttrValueList);
        spuSkuAttrValueLangMapper.batchSave(spuSkuAttrValueLangList);
    }

    @Override
    public void updateBatch(List<SpuSkuAttrValue> spuSkuAttrValues) {
        if (CollUtil.isNotEmpty(spuSkuAttrValues)) {
            spuSkuAttrValueMapper.updateBatch(spuSkuAttrValues);
        }
    }

    @Override
    public void deleteById(Long spuSkuAttrId) {
        spuSkuAttrValueMapper.deleteById(spuSkuAttrId);
    }

    @Override
    public void updateBySpuId(Long spuId) {
        spuSkuAttrValueMapper.updateBySpuId(spuId);
    }

    @Override
    public void changeStatusBySkuId(List<Long> skuIds, Integer status) {
        spuSkuAttrValueMapper.changeStatusBySkuId(skuIds, status);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SKU_WITH_ATTR_LIST_KEY, key = "#spuId",sync = true)
    public List<SpuSkuAttrValue> getSpuSkuAttrValueBySkuId(Long spuId) {
        return this.list(new LambdaQueryWrapper<SpuSkuAttrValue>().eq(SpuSkuAttrValue::getStatus,1).eq(SpuSkuAttrValue::getSpuId,spuId));
    }

    /**
     * 加载销售属性国际化信息
     * @param spuSkuAttrValueList
     * @return 属性国际化信息列表
     */
    private List<SpuSkuAttrValueLang> loadSpuSkuAttrValueInfo(List<SpuSkuAttrValueDTO> spuSkuAttrValueList) {
        Map<Long, AttrVO> dbAttrMap = null;
        List<SpuSkuAttrValueLang> spuSkuAttrValueLangList = new ArrayList<>();
        for (SpuSkuAttrValueDTO spuSkuAttrValueDTO : spuSkuAttrValueList) {
            if (CollUtil.isNotEmpty(spuSkuAttrValueDTO.getSpuSkuAttrValueLangList())) {
                for (SpuSkuAttrValueLangDTO spuSkuAttrValueLangDTO : spuSkuAttrValueDTO.getSpuSkuAttrValueLangList()) {
                    spuSkuAttrValueLangDTO.setSpuSkuAttrId(spuSkuAttrValueDTO.getSpuSkuAttrId());
                }
                spuSkuAttrValueLangList.addAll(mapperFacade.mapAsList(spuSkuAttrValueDTO.getSpuSkuAttrValueLangList(), SpuSkuAttrValueLang.class));
                continue;
            }
            if (Objects.isNull(dbAttrMap)) {
                List<AttrVO> shopAttrs = attrService.getShopAttrs(AuthUserContext.get().getTenantId());
                dbAttrMap = shopAttrs.stream().collect(Collectors.toMap(AttrVO::getAttrId, a -> a));
            }
            AttrVO attrVO = dbAttrMap.get(spuSkuAttrValueDTO.getAttrId());
            // 通用
            boolean isShare = (Objects.isNull(spuSkuAttrValueDTO.getAttrId()) || Objects.isNull(spuSkuAttrValueDTO.getAttrValueId()))
                    || Objects.isNull(attrVO);
            if (isShare) {
                SpuSkuAttrValueLang spuSkuAttrValueLang = new SpuSkuAttrValueLang();
                spuSkuAttrValueLang.setSpuSkuAttrId(spuSkuAttrValueDTO.getSpuSkuAttrId());
                spuSkuAttrValueLang.setLang(LanguageEnum.LANGUAGE_ZH_CN.getLang());
                spuSkuAttrValueLang.setAttrName(spuSkuAttrValueDTO.getAttrName());
                spuSkuAttrValueLang.setAttrValueName(spuSkuAttrValueDTO.getAttrValueName());
                spuSkuAttrValueLangList.add(spuSkuAttrValueLang);
                continue;
            }

            Map<Integer, String> attrNameMap = attrVO.getAttrLangList().stream().collect(Collectors.toMap(AttrLangVO::getLang, AttrLangVO::getName));
            Map<Integer, String> attrValueMap = new HashMap<>(0);
            for (AttrValueVO attrValue : attrVO.getAttrValues()) {
                if (!Objects.equals(attrValue.getAttrValueId(), spuSkuAttrValueDTO.getAttrValueId())) {
                    continue;
                }
                attrValueMap = attrValue.getValues().stream().collect(Collectors.toMap(AttrValueLangVO::getLang, AttrValueLangVO::getValue));
            }
            for (LanguageEnum languageEnum : LanguageEnum.values()) {
                Integer lang = languageEnum.getLang();
                if (!attrNameMap.containsKey(lang) && !attrValueMap.containsKey(lang)) {
                    continue;
                }
                SpuSkuAttrValueLang spuSkuAttrValueLang = new SpuSkuAttrValueLang();

                spuSkuAttrValueLang.setSpuSkuAttrId(spuSkuAttrValueDTO.getSpuSkuAttrId());
                spuSkuAttrValueLang.setLang(languageEnum.getLang());
                spuSkuAttrValueLang.setAttrName(LangUtil.getLangValue(attrNameMap, lang));
                spuSkuAttrValueLang.setAttrValueName(LangUtil.getLangValue(attrValueMap, lang));
                spuSkuAttrValueLangList.add(spuSkuAttrValueLang);
            }
        }
        return spuSkuAttrValueLangList;
    }
}
