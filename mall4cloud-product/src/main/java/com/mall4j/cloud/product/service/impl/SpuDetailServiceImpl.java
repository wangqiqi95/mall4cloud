package com.mall4j.cloud.product.service.impl;

import ch.qos.logback.core.joran.action.AbstractEventEvaluatorAction;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.product.vo.SpuDetailVO;
import com.mall4j.cloud.product.mapper.SpuDetailMapper;
import com.mall4j.cloud.product.model.SpuDetail;
import com.mall4j.cloud.product.service.SpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品详情信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
public class SpuDetailServiceImpl extends ServiceImpl<SpuDetailMapper,SpuDetail> implements SpuDetailService {

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Override
    public void batchSave(List<SpuDetail> spuDetailList) {
        spuDetailMapper.batchSave(spuDetailList);
    }

    @Override
    public void batchUpdate(List<SpuDetail> spuDetailList) {
        spuDetailMapper.batchUpdate(spuDetailList);
    }

    @Override
    public void deleteById(Long spuId) {
        spuDetailMapper.deleteById(spuId);
    }

    @Override
    public void update(List<SpuDetail> spuDetailList, List<SpuDetailVO> detailListDb, Long spuId) {
        if (CollUtil.isEmpty(spuDetailList) && CollUtil.isEmpty(detailListDb)) {
            return;
        }
        if (CollUtil.isEmpty(detailListDb)) {
            spuDetailMapper.batchSave(spuDetailList);
            return;
        }
        List<Integer> langList = detailListDb.stream().map(SpuDetailVO::getLang).collect(Collectors.toList());
        if (CollUtil.isEmpty(spuDetailList)) {
            spuDetailMapper.deleteBatchBySpuIdAndLang(spuId, langList);
            return;
        }
        Iterator<SpuDetail> iterator = spuDetailList.iterator();
        List<SpuDetail> saveList = new ArrayList<>();
        while (iterator.hasNext()) {
            SpuDetail spuDetail = iterator.next();
            if (langList.contains(spuDetail.getLang())) {
                langList.remove(spuDetail.getLang());
                continue;
            }
            saveList.add(spuDetail);
            iterator.remove();
        }
        if (CollUtil.isNotEmpty(saveList)) {
            spuDetailMapper.batchSave(saveList);
        }
        if (CollUtil.isNotEmpty(spuDetailList)) {
            spuDetailMapper.batchUpdate(spuDetailList);
        }
        if (CollUtil.isNotEmpty(langList)) {
            spuDetailMapper.deleteBatchBySpuIdAndLang(spuId, langList);
        }
    }
}
