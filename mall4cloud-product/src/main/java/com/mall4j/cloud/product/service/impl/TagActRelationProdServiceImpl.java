package com.mall4j.cloud.product.service.impl;

import com.mall4j.cloud.product.mapper.TagActRelationProdMapper;
import com.mall4j.cloud.product.model.TagActRelationProd;
import com.mall4j.cloud.product.service.TagActRelationProdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TagActRelationProdServiceImpl implements TagActRelationProdService {
    private  final TagActRelationProdMapper prodMapper;
    @Override
    public List<TagActRelationProd> listByActId(Long actId) {
        return prodMapper.listByActId(actId);
    }

    @Override
    public List<TagActRelationProd> listsByActId(Long actId) {
        return prodMapper.listsByActId(actId);
    }

    @Override
    public void save(TagActRelationProd tagActRelationProd) {
        prodMapper.save(tagActRelationProd);
    }

    @Override
    public void deleteById(Long actId, Long spuId) {
        prodMapper.deleteById(actId,spuId);
    }

    @Override
    public void deleteByActId(Long actId) {
        prodMapper.deleteByActId(actId);
    }
}
