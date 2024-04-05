package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.vo.AttrValueLangVO;
import com.mall4j.cloud.product.model.AttrValueLang;
import com.mall4j.cloud.product.mapper.AttrValueLangMapper;
import com.mall4j.cloud.product.service.AttrValueLangService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 属性值-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
@Service
public class AttrValueLangServiceImpl implements AttrValueLangService {

    @Autowired
    private AttrValueLangMapper attrValueLangMapper;

    @Override
    public PageVO<AttrValueLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> attrValueLangMapper.list());
    }

    @Override
    public AttrValueLang getByAttrValueId(Long attrValueId) {
        return attrValueLangMapper.getByAttrValueId(attrValueId);
    }

    @Override
    public void save(List<AttrValueLang> attrValueLangList, Long attrValueId) {
        for (AttrValueLang attrValueLang : attrValueLangList) {
            attrValueLang.setAttrValueId(attrValueId);
        }
        saveBatch(attrValueLangList);
    }

    @Override
    public void update(List<AttrValueLang> attrValueLangList) {
        if(CollUtil.isEmpty(attrValueLangList)) {
            return;
        }
        attrValueLangMapper.batchUpdate(attrValueLangList);
    }

    @Override
    public void deleteById(Long attrValueId) {
        attrValueLangMapper.deleteById(attrValueId);
    }

    @Override
    public void deleteBatch(List<Long> attrValueIds) {
        attrValueLangMapper.deleteBatch(attrValueIds);
    }

    @Override
    public void deleteByAttrValueIds(List<Long> attrValueIds) {
        if (CollUtil.isEmpty(attrValueIds)) {
            return;
        }
        attrValueLangMapper.deleteByAttrValueIds(attrValueIds);
    }

    @Override
    public void saveBatch(List<AttrValueLang> attrValueLangList) {
        if (CollUtil.isEmpty(attrValueLangList)) {
            return;
        }
        Iterator<AttrValueLang> iterator = attrValueLangList.iterator();
        while (iterator.hasNext()){
            AttrValueLang attrValueLang = iterator.next();
            if (StrUtil.isNotBlank(attrValueLang.getValue())) {
                continue;
            }
            if (Objects.equals(attrValueLang.getLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
                throw new LuckException("中文属性值不能为空");
            } else {
                iterator.remove();
            }
        }
        attrValueLangMapper.batchSave(attrValueLangList);
    }
}
