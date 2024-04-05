package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.SpuDetailVO;
import com.mall4j.cloud.common.product.vo.SpuLangVO;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.model.SpuDetail;
import com.mall4j.cloud.product.model.SpuLang;
import com.mall4j.cloud.product.mapper.SpuLangMapper;
import com.mall4j.cloud.product.service.SpuLangService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
@Service
public class SpuLangServiceImpl implements SpuLangService {

    @Autowired
    private SpuLangMapper spuLangMapper;

    @Override
    public PageVO<SpuLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> spuLangMapper.list());
    }

    @Override
    public SpuLang getBySpuId(Long spuId) {
        return spuLangMapper.getBySpuId(spuId);
    }

    @Override
    public void batchSave(List<SpuLang> spuLangList) {
        spuLangMapper.batchSave(spuLangList);
    }

    @Override
    public void batchUpdate(List<SpuLang> spuLangList) {
        spuLangMapper.batchUpdate(spuLangList);
    }

    @Override
    public void deleteById(Long spuId) {
        spuLangMapper.deleteById(spuId);
    }

    @Override
    public void update(List<SpuLang> spuLangList, Long spuId) {
        if (CollUtil.isEmpty(spuLangList)) {
            return;
        }
        List<Integer> langIds = spuLangMapper.listLangId(spuId);
        if (CollUtil.isEmpty(langIds)) {
            spuLangMapper.batchSave(spuLangList);
            return;
        }
        if (CollUtil.isEmpty(spuLangList)) {
            spuLangMapper.deleteBatchBySpuIdAndLang(spuId, langIds);
            return;
        }
        Iterator<SpuLang> iterator = spuLangList.iterator();
        List<SpuLang> saveList = new ArrayList<>();
        while (iterator.hasNext()) {
            SpuLang spuLang = iterator.next();
            if (langIds.contains(spuLang.getLang())) {
                langIds.remove(spuLang.getLang());
                continue;
            }
            saveList.add(spuLang);
            iterator.remove();
        }
        if (CollUtil.isNotEmpty(saveList)) {
            spuLangMapper.batchSave(saveList);
        }
        if (CollUtil.isNotEmpty(spuLangList)) {
            spuLangMapper.batchUpdate(spuLangList);
        }
        if (CollUtil.isNotEmpty(langIds)) {
            spuLangMapper.deleteBatchBySpuIdAndLang(spuId, langIds);
        }
    }
}
