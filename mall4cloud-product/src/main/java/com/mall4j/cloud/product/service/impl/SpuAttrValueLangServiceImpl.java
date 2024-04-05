package com.mall4j.cloud.product.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.SpuAttrValueLang;
import com.mall4j.cloud.product.mapper.SpuAttrValueLangMapper;
import com.mall4j.cloud.product.service.SpuAttrValueLangService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 属性值-国际化表
 *
 * @author YXF
 * @date 2021-04-15 16:47:33
 */
@Service
public class SpuAttrValueLangServiceImpl implements SpuAttrValueLangService {

    @Autowired
    private SpuAttrValueLangMapper spuAttrValueLangMapper;

    @Override
    public PageVO<SpuAttrValueLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> spuAttrValueLangMapper.list());
    }

    @Override
    public SpuAttrValueLang getBySpuAttrValueId(Long spuAttrValueId) {
        return spuAttrValueLangMapper.getBySpuAttrValueId(spuAttrValueId);
    }

    @Override
    public void save(SpuAttrValueLang spuAttrValueLang) {
        spuAttrValueLangMapper.save(spuAttrValueLang);
    }

    @Override
    public void update(SpuAttrValueLang spuAttrValueLang) {
        spuAttrValueLangMapper.update(spuAttrValueLang);
    }

    @Override
    public void deleteById(Long spuAttrValueId) {
        spuAttrValueLangMapper.deleteById(spuAttrValueId);
    }
}
