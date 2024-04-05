package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.MaterialStoreMapper;
import com.mall4j.cloud.biz.model.cp.MaterialStore;
import com.mall4j.cloud.biz.service.cp.MaterialStoreService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 素材商店表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@RequiredArgsConstructor
@Service
public class MaterialStoreServiceImpl implements MaterialStoreService {

    private final MaterialStoreMapper materialStoreMapper;
    @Override
    public void save(MaterialStore materialStore) {
        materialStoreMapper.save(materialStore);
    }

    @Override
    public void deleteByMatId(Long id) {
        materialStoreMapper.deleteByMatId(id);
    }

    @Override
    public List<MaterialStore> listByMatId(Long matId) {
        MaterialStore store = new MaterialStore();
        store.setMatId(matId);
        return materialStoreMapper.list(store);
    }

}
