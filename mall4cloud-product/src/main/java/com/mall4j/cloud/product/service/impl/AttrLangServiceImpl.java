package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.AttrLangVO;
import com.mall4j.cloud.common.product.vo.AttrVO;
import com.mall4j.cloud.product.mapper.AttrValueLangMapper;
import com.mall4j.cloud.product.model.AttrLang;
import com.mall4j.cloud.product.mapper.AttrLangMapper;
import com.mall4j.cloud.product.service.AttrLangService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 属性-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
@Service
public class AttrLangServiceImpl implements AttrLangService {

    @Autowired
    private AttrLangMapper attrLangMapper;
    @Autowired
    private AttrValueLangMapper attrValueLangMapper;

    @Override
    public PageVO<AttrLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> attrLangMapper.list());
    }

    @Override
    public AttrLang getByAttrId(Long attrId) {
        return attrLangMapper.getByAttrId(attrId);
    }

    @Override
    public void save(List<AttrLang> attrLangList, Long attrId) {
        for (AttrLang attrLang : attrLangList) {
            attrLang.setAttrId(attrId);
        }
        attrLangMapper.batchSave(attrLangList);
    }

    @Override
    public void update(List<AttrLang> attrLangList, AttrVO attrVO) {
        Long attrId = attrVO.getAttrId();
        Map<Integer, AttrLangVO> attrLangMap = attrVO.getAttrLangList().stream().collect(Collectors.toMap(AttrLangVO::getLang, a -> a));
        List<AttrLang> saveList = new ArrayList<>();
        List<AttrLang> updateList = new ArrayList<>();
        Set<Integer> addAttrIds = new HashSet<>();
        for (AttrLang attrLang : attrLangList) {
            if (!attrLangMap.containsKey(attrLang.getLang())) {
                attrLang.setAttrId(attrId);
                saveList.add(attrLang);
                addAttrIds.add(attrLang.getLang());
            } else {
                updateList.add(attrLang);
                attrLangMap.remove(attrLang.getLang());
            }
        }
        if (CollUtil.isNotEmpty(saveList)) {
            attrLangMapper.batchSave(saveList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            attrLangMapper.batchUpdate(updateList);
        }
        Set<Integer> keySet = attrLangMap.keySet();
        // 获取要删除的语言信息（默认语言不能删）
        if (CollUtil.isNotEmpty(keySet)) {
            attrLangMapper.batchDelete(keySet, attrId);
            attrValueLangMapper.deleteByAttrIdAndLangs(attrId, keySet);
        }
    }

    @Override
    public void deleteById(Long attrId) {
        attrLangMapper.deleteById(attrId);
    }
}
