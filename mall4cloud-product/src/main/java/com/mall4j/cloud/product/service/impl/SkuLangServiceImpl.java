package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.SkuLang;
import com.mall4j.cloud.product.mapper.SkuLangMapper;
import com.mall4j.cloud.product.service.SkuLangService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * sku-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
@Service
public class SkuLangServiceImpl implements SkuLangService {

    @Autowired
    private SkuLangMapper skuLangMapper;

    @Override
    public PageVO<SkuLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> skuLangMapper.list());
    }

    @Override
    public SkuLang getBySkuId(Long skuId) {
        return skuLangMapper.getBySkuId(skuId);
    }

    @Override
    public void save(SkuLang skuLang) {
        skuLangMapper.save(skuLang);
    }

    @Override
    public void update(SkuLang skuLang) {
        skuLangMapper.update(skuLang);
    }

    @Override
    public void deleteById(Long skuId) {
        skuLangMapper.deleteById(skuId);
    }

    @Override
    public void batchSave(List<SkuLang> skuLangList) {
        if (CollUtil.isEmpty(skuLangList)) {
            return;
        }
        skuLangMapper.batchSave(skuLangList);
    }
}
