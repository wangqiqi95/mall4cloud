package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.dto.SpuAttrValueDTO;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.product.vo.app.SpuAttrValueAppVO;
import com.mall4j.cloud.product.mapper.SpuAttrValueMapper;
import com.mall4j.cloud.product.model.SpuAttrValue;
import com.mall4j.cloud.product.service.AttrService;
import com.mall4j.cloud.product.service.SpuAttrValueService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品规格属性关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
public class SpuAttrValueServiceImpl extends ServiceImpl<SpuAttrValueMapper, SpuAttrValue> implements SpuAttrValueService {

    @Autowired
    private SpuAttrValueMapper spuAttrValueMapper;
    @Autowired
    private SpuServiceImpl spuService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public void save(Long spuId, Long categoryId, List<SpuAttrValueDTO> spuAttrValueDTOList) {
        List<SpuAttrValue> spuAttrValueList = setSpuAttrValueInfo(spuId, categoryId, spuAttrValueDTOList);
        if (CollUtil.isEmpty(spuAttrValueList)) {
            return;
        }
        spuAttrValueMapper.batchSave(spuAttrValueList);
    }

//    @Override
//    @Cacheable(cacheNames = CacheNames.SPU_ATTR_VALUE_KEY, key = "#spuId", sync = true)
//    public List<SpuAttrValueLangVO> spuAttrValueListBySpuId(Long spuId) {
//        List<SpuAttrValueVO> spuAttrValueList = getSpuAttrsBySpuId(spuId);
//        List<SpuAttrValueLangVO> spuAttrValueLangVOList = new ArrayList<>();
//        for (SpuAttrValueVO spuAttrValueVO : spuAttrValueList) {
//            for (SpuAttrValueLangVO spuAttrValueLangVO : spuAttrValueVO.getSpuAttrValueLangList()) {
//                if (Objects.equals(spuAttrValueLangVO.getLang(), I18nMessage.getLang())) {
//                    spuAttrValueLangVOList.add(spuAttrValueLangVO);
//                }
//            }
//        }
//        return spuAttrValueLangVOList;
//    }

//    @Override
//    public void removeSpuAttrValue(List<Long> spuIds) {
//        if (CollUtil.isEmpty(spuIds)) {
//            return;
//        }
//        List<String> keys = new ArrayList<>();
//        for (Long spuId : spuIds) {
//            keys.add(CacheNames.SPU_ATTR_VALUE_KEY + CacheNames.UNION + spuId);
//        }
//        RedisUtil.deleteBatch(keys);
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpuByAttrValueIds(List<SpuAttrValue> attrValueList, Set<Long> attrValueIds) {
        if (CollUtil.isEmpty(attrValueIds)) {
            return;
        }
        List<Long> spuIds = spuAttrValueMapper.spuIdListByAttrValueIds(attrValueIds);
        // 更新商品基本属性
        if (CollUtil.isNotEmpty(attrValueList)) {
            spuAttrValueMapper.batchUpdateSpuAttrValue(attrValueList);
        }
        if (CollUtil.isNotEmpty(spuIds)) {
            spuService.updateSpuUpdateTime(spuIds, null, null);
//            SpuAttrValueServiceImpl spuAttrValueService = (SpuAttrValueServiceImpl) AopContext.currentProxy();
//            spuAttrValueService.removeSpuAttrValue(spuIds);
        }
    }

    @Override
    public List<SpuAttrValueAppVO> getSpuAttrValueAppVOBySpuId(Long spuId) {
        return spuAttrValueMapper.getSpuAttrAppBySpuId(spuId);
    }

    @Override
    public Boolean updateSpuAttrValue(List<SpuAttrValueDTO> spuAttrValues, Long spuId) {
        if (CollectionUtil.isNotEmpty(spuAttrValues)) {
            spuAttrValues.forEach(spuAttrValueDTO -> {
                this.lambdaUpdate().set(StrUtil.isNotBlank(spuAttrValueDTO.getAttrValueName()), SpuAttrValue::getAttrValueName, spuAttrValueDTO.getAttrValueName())
                        .eq(SpuAttrValue::getAttrName, spuAttrValueDTO.getAttrName())
                        .eq(SpuAttrValue::getSpuId, spuId)
                        .update();
            });
        }
        return true;
    }


    @Override
    public void update(Long spuId, Long categoryId, List<SpuAttrValueDTO> spuAttrValues) {
        List<SpuAttrValueVO> dbSpuAttrValues = getSpuAttrsBySpuId(spuId);
        if (CollUtil.isEmpty(dbSpuAttrValues) && CollUtil.isEmpty(spuAttrValues)) {
            return;
        }
        // 预防空指针
        if (Objects.isNull(spuAttrValues)) {
            spuAttrValues = new ArrayList<>();
        } else if (Objects.isNull(dbSpuAttrValues)) {
            dbSpuAttrValues = new ArrayList<>();
        }
        // 根据key(attrId:attrValueId) 来筛选数据
        Map<String, SpuAttrValueVO> dbSpuAttrValueMap = dbSpuAttrValues.stream()
                .collect(Collectors.toMap(spuAttrValueVO -> spuAttrValueVO.getAttrId() + Constant.COLON + spuAttrValueVO.getAttrValueId(), attr -> attr));
        Map<String, SpuAttrValueDTO> spuAttrValueMap = spuAttrValues.stream()
                .collect(Collectors.toMap(spuAttrValueDTO -> spuAttrValueDTO.getAttrId() + Constant.COLON + spuAttrValueDTO.getAttrValueId(), attr -> attr));
        List<SpuAttrValueDTO> saveList = new ArrayList<>();
        List<SpuAttrValueDTO> updateList = new ArrayList<>();
        // 分别筛选出新增、更新的数据
        for (String key : spuAttrValueMap.keySet()) {
            SpuAttrValueDTO spuAttrValueDTO = spuAttrValueMap.get(key);
            // 新数据-保存
            if (!dbSpuAttrValueMap.containsKey(key)) {
                saveList.add(spuAttrValueMap.get(key));
                continue;
            }
            // 已有数据且属性值不为空-更新
            if (dbSpuAttrValueMap.containsKey(key) && StrUtil.isNotBlank(spuAttrValueDTO.getAttrValueName())) {
                updateList.add(spuAttrValueMap.get(key));
                dbSpuAttrValueMap.remove(key);
                continue;
            }
        }
        // 删除商品属性
        if (MapUtil.isNotEmpty(dbSpuAttrValueMap)) {
            List<Long> deleteAttrIds = dbSpuAttrValueMap.values().stream().map(SpuAttrValueVO::getAttrId).collect(Collectors.toList());
            spuAttrValueMapper.deleteBatchBySpuIdAndAttrIds(spuId, deleteAttrIds);
        }
        // 保存商品属性
        List<SpuAttrValue> spuAttrValueList = setSpuAttrValueInfo(spuId, categoryId, saveList);
        if (CollUtil.isNotEmpty(spuAttrValueList)) {
            spuAttrValueMapper.batchSave(spuAttrValueList);
        }
        // 更新商品属性
        if (CollUtil.isNotEmpty(updateList)) {
            List<SpuAttrValue> updateSpuAttrValueLis = setSpuAttrValueInfo(spuId, categoryId, updateList);
            spuAttrValueMapper.batchUpdate(updateSpuAttrValueLis);
        }
    }


    @Override
    public void deleteBySpuId(Long spuId) {
        spuAttrValueMapper.deleteBySpuId(spuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUnionDataByAttId(Long attrId, List<Long> attrValueIds) {
        if (CollUtil.isEmpty(attrValueIds)) {
            return;
        }
        List<Long> spuIds = spuAttrValueMapper.spuIdListByAttrValueIds(attrValueIds);
        spuAttrValueMapper.deleteByAttId(attrId, attrValueIds);
        spuService.updateSpuUpdateTime(spuIds, null, null);
//            SpuAttrValueServiceImpl spuAttrValueService = (SpuAttrValueServiceImpl) AopContext.currentProxy();
//            spuAttrValueService.removeSpuAttrValue(spuIds);
    }

    @Override
    public void deleteByCategoryIds(Long attrId, List<Long> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return;
        }
        spuAttrValueMapper.deleteByCategoryIds(attrId, categoryIds);
        spuService.updateSpuUpdateTime(null, categoryIds, null);
    }

    @Override
    public List<SpuAttrValueVO> getSpuAttrsBySpuId(Long spuId) {
        return spuAttrValueMapper.getSpuAttrsBySpuId(spuId);
    }

    /**
     * 为商品属性插入国际化信息
     *
     * @param spuId         商品id
     * @param categoryId    分类id（用于获取完整的属性信息）
     * @param spuAttrValues 需要插入国际化信息的属性列表
     * @return
     */
    private List<SpuAttrValue> setSpuAttrValueInfo(Long spuId, Long categoryId, List<SpuAttrValueDTO> spuAttrValues) {
        if (CollUtil.isEmpty(spuAttrValues)) {
            return new ArrayList<>();
        }
        List<AttrVO> dbAttrList = attrService.getAttrsByCategoryIdAndAttrType(categoryId);
        Map<Long, AttrVO> dbAttrMap = dbAttrList.stream().collect(Collectors.toMap(AttrVO::getAttrId, a -> a));
        List<SpuAttrValue> spuAttrValueList = new ArrayList<>();
        for (SpuAttrValueDTO spuAttrValueDTO : spuAttrValues) {
            // 不保存属性值为空的数据
            if (StrUtil.isBlank(spuAttrValueDTO.getAttrValueName())) {
                continue;
            }
            AttrVO dbAttr = dbAttrMap.get(spuAttrValueDTO.getAttrId());
            // 属性值为输入的数据，没有属性值的国际化信息， 设为通用语言
            boolean isShare = (Objects.isNull(spuAttrValueDTO.getAttrId()) && Objects.isNull(spuAttrValueDTO.getAttrValueId())) || Objects.isNull(dbAttr);
            if (isShare) {
                SpuAttrValue spuAttrValue = mapperFacade.map(spuAttrValueDTO, SpuAttrValue.class);
                spuAttrValue.setSpuId(spuId);
                spuAttrValue.setLang(LanguageEnum.LANGUAGE_ZH_CN.getLang());
                spuAttrValueList.add(spuAttrValue);
                continue;
            }
            // 插入中英文国际化信息
            SpuAttrValue spuAttrValueZh = mapperFacade.map(spuAttrValueDTO, SpuAttrValue.class);
            SpuAttrValue spuAttrValueEn = mapperFacade.map(spuAttrValueDTO, SpuAttrValue.class);
            //属性国际化信息
            for (AttrLangVO attrLangVO : dbAttr.getAttrLangList()) {
                if (Objects.equals(LanguageEnum.LANGUAGE_ZH_CN.getLang(), attrLangVO.getLang())) {
                    spuAttrValueZh.setSpuId(spuId);
                    spuAttrValueZh.setLang(LanguageEnum.LANGUAGE_ZH_CN.getLang());
                    spuAttrValueZh.setAttrName(attrLangVO.getName());
                    spuAttrValueZh.setAttrDesc(attrLangVO.getDesc());
                    spuAttrValueList.add(spuAttrValueZh);
                    continue;
                }
                spuAttrValueEn.setSpuId(spuId);
                spuAttrValueEn.setLang(LanguageEnum.LANGUAGE_EN.getLang());
                spuAttrValueEn.setAttrName(attrLangVO.getName());
                spuAttrValueEn.setAttrDesc(attrLangVO.getDesc());
                spuAttrValueList.add(spuAttrValueEn);
            }
            if (Objects.isNull(spuAttrValueDTO.getAttrValueId())) {
                continue;
            }
            // 属性值国际化信息
            Map<Long, AttrValueVO> attrValueMap = dbAttr.getAttrValues().stream()
                    .filter(attrValueVO -> Objects.equals(spuAttrValueDTO.getAttrValueId(), attrValueVO.getAttrValueId()))
                    .collect(Collectors.toMap(AttrValueVO::getAttrValueId, a -> a));
            AttrValueVO attrValueVO = attrValueMap.get(spuAttrValueDTO.getAttrValueId());
            if (Objects.isNull(attrValueVO)) {
                continue;
            }
            for (AttrValueLangVO attrValueLang : attrValueVO.getValues()) {
                if (Objects.equals(LanguageEnum.LANGUAGE_ZH_CN.getLang(), attrValueLang.getLang())) {
                    spuAttrValueZh.setAttrValueName(attrValueLang.getValue());
                    continue;
                }
                spuAttrValueEn.setAttrValueName(attrValueLang.getValue());
            }
        }
        return spuAttrValueList;
    }
}
