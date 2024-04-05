package com.mall4j.cloud.product.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.SpuSkuAttrValueLang;
import com.mall4j.cloud.product.mapper.SpuSkuAttrValueLangMapper;
import com.mall4j.cloud.product.service.SpuSkuAttrValueLangService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品sku销售属性关联信息-国际化
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
@Service
public class SpuSkuAttrValueLangServiceImpl implements SpuSkuAttrValueLangService {

    @Autowired
    private SpuSkuAttrValueLangMapper spuSkuAttrValueLangMapper;

    @Override
    public PageVO<SpuSkuAttrValueLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> spuSkuAttrValueLangMapper.list());
    }

    @Override
    public SpuSkuAttrValueLang getBySpuSkuAttrId(Long spuSkuAttrId) {
        return spuSkuAttrValueLangMapper.getBySpuSkuAttrId(spuSkuAttrId);
    }

    @Override
    public void save(SpuSkuAttrValueLang spuSkuAttrValueLang) {
        spuSkuAttrValueLangMapper.save(spuSkuAttrValueLang);
    }

    @Override
    public void update(SpuSkuAttrValueLang spuSkuAttrValueLang) {
        spuSkuAttrValueLangMapper.update(spuSkuAttrValueLang);
    }

    @Override
    public void deleteById(Long spuSkuAttrId) {
        spuSkuAttrValueLangMapper.deleteById(spuSkuAttrId);
    }
}
