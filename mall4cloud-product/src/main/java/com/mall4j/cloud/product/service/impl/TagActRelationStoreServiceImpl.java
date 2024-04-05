package com.mall4j.cloud.product.service.impl;

import com.mall4j.cloud.product.mapper.TagActRelationStoreMapper;
import com.mall4j.cloud.product.model.TagActRelationStore;
import com.mall4j.cloud.product.service.TagActRelationStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TagActRelationStoreServiceImpl implements TagActRelationStoreService {
    private  final TagActRelationStoreMapper storeMapper;

    @Override
    public List<TagActRelationStore> listByActId(Long actId) {
        return storeMapper.listByActId(actId);
    }

    @Override
    public List<TagActRelationStore> listsByActId(Long actId) {
        return storeMapper.listsByActId(actId);
    }

    @Override
    public void save(TagActRelationStore tagActRelationStore) {
        storeMapper.save(tagActRelationStore);
    }

    @Override
    public void deleteById(Long actId, Long storeId) {
        storeMapper.deleteById(actId,storeId);
    }

    @Override
    public void deleteByActId(Long actId) {
        storeMapper.deleteByActId(actId);
    }
}
