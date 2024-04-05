package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.SpuExtensionStockDTO;
import com.mall4j.cloud.product.mapper.SpuExtensionMapper;
import com.mall4j.cloud.product.model.SpuExtension;
import com.mall4j.cloud.product.service.SpuExtensionService;
import com.mall4j.cloud.product.vo.SpuExtensionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
 * @author FrozenWatermelon
 * @date 2020-11-11 13:49:06
 */
@Service
public class SpuExtensionServiceImpl  implements SpuExtensionService {

    @Autowired
    private SpuExtensionMapper spuExtensionMapper;

    @Override
    public PageVO<SpuExtensionVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> spuExtensionMapper.list());
    }

    @Override
    public SpuExtensionVO getBySpuExtendId(Long spuExtendId) {
        return spuExtensionMapper.getBySpuExtendId(spuExtendId);
    }

    @Override
    public void save(SpuExtension spuExtension) {
        spuExtensionMapper.save(spuExtension);
    }

    @Override
    public void updateStock(Long spuId) {
        spuExtensionMapper.updateStock(spuId);
    }

    @Override
    public void deleteById(Long spuId) {
        spuExtensionMapper.deleteById(spuId);
    }

    @Override
    public SpuExtension getBySpuId(Long spuId) {
        return spuExtensionMapper.getBySpuId(spuId);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SPU_EXTENSION_KEY, key = "#spuExtension.spuId")
    public void update(SpuExtension spuExtension) {
        if (spuExtension.getStock() == 0 && spuExtension.getActualStock() == 0 && spuExtension.getLockStock() == 0) {
            return;
        }
        spuExtensionMapper.update(spuExtension);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SPU_EXTENSION_KEY, key = "#spuId")
    public void updateWaterSoldNumBySpuId(Long spuId, Integer waterSoldNum) {
        spuExtensionMapper.updateWaterSoldNumBySpuId(spuId, waterSoldNum);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SPU_EXTENSION_KEY, key = "#spuId")
    public void changeCommentNum(Long spuId) {
        spuExtensionMapper.changeCommentNum(spuId);
    }

    @Override
    public void updateStocks(List<SpuExtensionStockDTO> spuStocks) {
        spuExtensionMapper.updateStocks(spuStocks);
    }

    @Override
    public void zeroSetChannelsStock(Long spuId) {
        spuExtensionMapper.zeroSetChannelsStock(spuId);
    }

    @Override
    public void updateChannelsStock(Long spuId, int countStock) {
        spuExtensionMapper.updateChannelsStock(spuId, countStock);
    }
}
