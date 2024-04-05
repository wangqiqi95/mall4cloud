package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.AttrType;
import com.mall4j.cloud.common.product.dto.AttrLangDTO;
import com.mall4j.cloud.common.product.dto.AttrValueLangDTO;
import com.mall4j.cloud.common.product.vo.AttrLangVO;
import com.mall4j.cloud.common.product.vo.AttrVO;
import com.mall4j.cloud.common.product.vo.AttrValueLangVO;
import com.mall4j.cloud.common.product.vo.AttrValueVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.product.dto.AttrDTO;
import com.mall4j.cloud.product.dto.AttrValueDTO;
import com.mall4j.cloud.product.mapper.AttrValueMapper;
import com.mall4j.cloud.product.model.AttrValue;
import com.mall4j.cloud.product.model.AttrValueLang;
import com.mall4j.cloud.product.model.SpuAttrValue;
import com.mall4j.cloud.product.service.AttrService;
import com.mall4j.cloud.product.service.AttrValueLangService;
import com.mall4j.cloud.product.service.AttrValueService;
import com.mall4j.cloud.product.service.SpuAttrValueService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 属性值信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
public class AttrValueServiceImpl implements AttrValueService {

    @Autowired
    private AttrValueMapper attrValueMapper;
    @Autowired
    private AttrValueLangService attrValueLangService;
    @Autowired
    private SpuAttrValueService spuAttrValueService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private AttrService attrService;

    @Override
    public void save(List<AttrValueDTO> attrValueList, Long attrId) {
        if (CollUtil.isEmpty(attrValueList)){
            return;
        }
        for (AttrValueDTO attrValueDTO : attrValueList) {
            attrValueDTO.setAttrId(attrId);
            checkAttrValueLangData(attrValueDTO.getValues());
            AttrValue attrValue = mapperFacade.map(attrValueDTO, AttrValue.class);
            if(CollectionUtil.isNotEmpty(attrValueDTO.getValues())){
                attrValue.setName(attrValueDTO.getValues().get(0).getValue());
            }
            attrValueMapper.save(attrValue);
            attrValueLangService.save(mapperFacade.mapAsList(attrValueDTO.getValues(), AttrValueLang.class), attrValue.getAttrValueId());
        }
    }

    private void checkAttrValueLangData(List<AttrValueLangDTO> attrValueLangList) {
        if (CollUtil.isEmpty(attrValueLangList)) {
            return;
        }
        Iterator<AttrValueLangDTO> iterator = attrValueLangList.iterator();
        boolean correct = false;
        while (iterator.hasNext()) {
            AttrValueLangDTO attrValueLangDTO = iterator.next();
            if (StrUtil.isBlank(attrValueLangDTO.getValue())) {
                iterator.remove();
                continue;
            }
            if (Objects.equals(attrValueLangDTO.getLang(), Constant.DEFAULT_LANG)) {
                correct = true;
            }
        }
        if (CollUtil.isEmpty(attrValueLangList) || !correct) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AttrDTO attrDTO, AttrVO dbAttr) {
        List<AttrValueDTO> addAttrValue = new ArrayList<>();
        List<Long> deleteAttrValue = dbAttr.getAttrValues().stream().map(AttrValueVO::getAttrValueId).collect(Collectors.toList());
        // 过滤掉不需要更新的数据，并更新属性值语言信息
        handleAttrData(attrDTO, addAttrValue, deleteAttrValue, dbAttr);
        // 删除属性值数据
        if (CollUtil.isNotEmpty(deleteAttrValue)) {
            if (Objects.equals(attrDTO.getAttrType(), AttrType.BASIC.value())) {
                spuAttrValueService.deleteUnionDataByAttId(attrDTO.getAttrId(), deleteAttrValue);
            }
            attrValueMapper.deleteBatch(deleteAttrValue);
            attrValueLangService.deleteBatch(deleteAttrValue);
        }
        // 新增属性值数据
        save(addAttrValue, dbAttr.getAttrId());
        // 销售属性的更新到此已结束，下面是基本数量关联信息（商品）的更新
        // 基本属性-更新关联的商品属性
        if (Objects.equals(attrDTO.getAttrType(), AttrType.BASIC.value())) {
            updateAttrAndAttrValueOfSpuOrSku(attrDTO, dbAttr);
        }
    }

    /**
     * 处理属性值语言信息-新增、更新（处理属性时，已删除掉多余的属性值语言信息）
     * @param attrDTO 属性信息
     * @param addAttrValue 需要新增的属性值
     * @param deleteAttrValue 属性值删除列表
     * @param dbAttr 旧的属性信息
     */
    private void handleAttrData(AttrDTO attrDTO, List<AttrValueDTO> addAttrValue, List<Long> deleteAttrValue, AttrVO dbAttr) {
        List<AttrValueLangDTO> updateAttrValueLang = new ArrayList<>();
        List<AttrValueLangDTO> saveAttrValueLang = new ArrayList<>();
        List<AttrValueDTO> attrValueUpdates = new ArrayList<>();
        Map<Long, List<Integer>> attrValueLangMap = new HashMap<>(dbAttr.getAttrValues().size());
        for (AttrValueVO attrValue : dbAttr.getAttrValues()) {
            List<Integer> langList = attrValue.getValues().stream().map(AttrValueLangVO::getLang).collect(Collectors.toList());
            attrValueLangMap.put(attrValue.getAttrValueId(), langList);
        }
        // 分别筛选出增删改的数据
        Iterator<AttrValueDTO> attrValueIterator = attrDTO.getAttrValues().iterator();
        while (attrValueIterator.hasNext()) {
            // 新增的属性值
            AttrValueDTO attrValue = attrValueIterator.next();
            if (Objects.isNull(attrValue.getAttrValueId())) {
                addAttrValue.add(attrValue);
                attrValueIterator.remove();
                continue;
            }else{
                if(CollectionUtil.isNotEmpty(attrValue.getValues())){
                    attrValue.setName(attrValue.getValues().get(0).getValue());
                }
                attrValueUpdates.add(attrValue);
            }
            for (AttrValueLangDTO attrValueLang : attrValue.getValues()) {
                List<Integer> langList = attrValueLangMap.get(attrValue.getAttrValueId());
                // 新增
                if (CollUtil.isEmpty(langList) || !langList.contains(attrValueLang.getLang())) {
                    attrValueLang.setAttrValueId(attrValue.getAttrValueId());
                    saveAttrValueLang.add(attrValueLang);
                    continue;
                }
                attrValueLang.setAttrValueId(attrValue.getAttrValueId());
                updateAttrValueLang.add(attrValueLang);
            }
            deleteAttrValue.remove(attrValue.getAttrValueId());
            if (CollUtil.isEmpty(attrValue.getValues())) {
                attrValueIterator.remove();
            }
        }

        // 保存属性值数据
        attrValueLangService.saveBatch(mapperFacade.mapAsList(saveAttrValueLang, AttrValueLang.class));
        // 更新属性值数据
        if (CollUtil.isNotEmpty(updateAttrValueLang)) {
            attrValueLangService.update(mapperFacade.mapAsList(updateAttrValueLang, AttrValueLang.class));
        }
        if (CollUtil.isNotEmpty(attrValueUpdates)) {
            attrValueMapper.updateBatch(mapperFacade.mapAsList(attrValueUpdates, AttrValue.class));
        }

    }

    /**
     * 更新属性数据时，更新商品中的属性数据
     * 若不需要同步更新商品中的属性数据，在更新属性数据时，不调用该方法即可，不影响其他流程
     * @param attrDTO 属性信息
     * @param dbAttr 属性信息
     */
    private void updateAttrAndAttrValueOfSpuOrSku(AttrDTO attrDTO, AttrVO dbAttr) {
        if (CollUtil.isEmpty(attrDTO.getAttrLangList()) && CollUtil.isEmpty(attrDTO.getAttrValues())) {
            return;
        }

        Map<Integer, AttrLangDTO> attrLangMap = attrDTO.getAttrLangList().stream().collect(Collectors.toMap(AttrLangDTO::getLang, a -> a));
        // 检验属性名是否改变
        boolean isChange = true;
        if (Objects.equals(dbAttr.getAttrLangList().size(), attrDTO.getAttrLangList().size())) {
            for (AttrLangVO attrLangVO : dbAttr.getAttrLangList()) {
                if (!attrLangMap.containsKey(attrLangVO.getLang())) {
                    isChange = true;
                    break;
                }
                AttrLangDTO attrLangDb = attrLangMap.get(attrLangVO.getLang());
                // 属性名与属性描述没有改变
                if (Objects.equals(attrLangVO.getName(), attrLangDb.getName()) && Objects.equals(attrLangVO.getDesc(), attrLangDb.getDesc())) {
                    isChange = false;
                    continue;
                }
                isChange = true;
                break;
            }
        }
        List<SpuAttrValue> spuAttrValueList = new ArrayList<>();
        Map<Long, AttrValueDTO> attrValueMap = attrDTO.getAttrValues().stream()
                .filter(attrValueDTO -> Objects.nonNull(attrValueDTO.getAttrValueId()))
                .collect(Collectors.toMap(AttrValueDTO::getAttrValueId, a -> a));
        // 筛选需要更新的商品属性值
        Set<Long> attrValueIds = new HashSet<>();
        for (AttrValueVO attrValueDb : dbAttr.getAttrValues()) {
            // 属性值已删除
            if (!attrValueMap.containsKey(attrValueDb.getAttrValueId())) {
                continue;
            }
            AttrValueDTO attrValueDTO = attrValueMap.get(attrValueDb.getAttrValueId());
            Map<Integer, AttrValueLangDTO> valueLangMap = attrValueDTO.getValues().stream().collect(Collectors.toMap(AttrValueLangDTO::getLang, a -> a));
            for (AttrValueLangVO valueDb : attrValueDb.getValues()) {
                // 新增的语言信息，不要更新
                if (!valueLangMap.containsKey(valueDb.getLang())) {
                    attrValueIds.add(attrValueDb.getAttrValueId());
                    continue;
                }
                // 更新-属性名或属性值发生改变
                AttrValueLangDTO attrValueLangDTO = valueLangMap.get(valueDb.getLang());
                if (isChange || !Objects.equals(attrValueLangDTO.getValue(), valueDb.getValue())) {
                    attrValueIds.add(attrValueDb.getAttrValueId());

                    SpuAttrValue spuAttrValue = new SpuAttrValue();
                    spuAttrValue.setAttrId(attrDTO.getAttrId());
                    spuAttrValue.setLang(valueDb.getLang());
                    AttrLangDTO attrLangDTO = attrLangMap.get(valueDb.getLang());
                    spuAttrValue.setAttrName(attrLangDTO.getName());
                    spuAttrValue.setAttrDesc(attrLangDTO.getDesc());
                    spuAttrValue.setAttrValueId(attrValueDb.getAttrValueId());
                    spuAttrValue.setAttrValueName(attrValueLangDTO.getValue());
                    spuAttrValueList.add(spuAttrValue);
                }

            }
        }
        // 更新es中商品的属性信息及清除商品属性缓存
        if (isChange) {
            attrValueIds = attrValueMap.keySet();
        }
        spuAttrValueService.updateSpuByAttrValueIds(spuAttrValueList, attrValueIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByAttrId(Long attrId) {
        List<Long> attrValueIds = attrValueMapper.getAttrValueIdsByAttrId(attrId);
        attrValueMapper.deleteByAttrId(attrId);
        attrValueLangService.deleteByAttrValueIds(attrValueIds);
        AttrVO attrVO = attrService.getByAttrId(attrId);
        if (Objects.isNull(attrVO)) {
            throw new LuckException("属性不存在或已删除");
        }
        // 清除商品属性关联数据
        if (Objects.equals(attrVO.getAttrType(), AttrType.BASIC.value())) {
            spuAttrValueService.deleteUnionDataByAttId(attrId,attrValueIds);
        }
    }
}
