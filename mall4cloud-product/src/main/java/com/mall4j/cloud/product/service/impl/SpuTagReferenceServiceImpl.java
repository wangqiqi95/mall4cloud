package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SpuTagReferenceDTO;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.mapper.SpuTagReferenceMapper;
import com.mall4j.cloud.product.model.SpuTag;
import com.mall4j.cloud.product.model.SpuTagReference;
import com.mall4j.cloud.product.service.SpuTagReferenceService;
import com.mall4j.cloud.product.service.SpuTagService;
import com.mall4j.cloud.product.vo.SpuPageVO;
import com.mall4j.cloud.product.vo.SpuTagReferenceVO;
import com.mall4j.cloud.product.vo.SpuTagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品分组标签关联信息
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
@Service
public class SpuTagReferenceServiceImpl implements SpuTagReferenceService {

    @Autowired
    private SpuTagReferenceMapper spuTagReferenceMapper;

    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;

    @Autowired
    private SpuTagService spuTagService;

    @Autowired
    private SpuMapper spuMapper;

    @Override
    public PageVO<SpuTagReferenceVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> spuTagReferenceMapper.list());
    }

    @Override
    public SpuTagReferenceVO getByReferenceId(Long referenceId) {
        return spuTagReferenceMapper.getByReferenceId(referenceId);
    }

    @Override
    public void save(SpuTagReference spuTagReference) {
        spuTagReferenceMapper.save(spuTagReference);
    }

    @Override
    public void update(SpuTagReference spuTagReference) {
        spuTagReferenceMapper.update(spuTagReference);
    }

    @Override
    public void deleteById(Long referenceId) {
        spuTagReferenceMapper.deleteById(referenceId);
    }

    @Override
    public int countByStatusAndTagId(Integer status, Long id) {
        return spuTagReferenceMapper.countByStatusAndTagId(status,id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProdForTag(SpuTag spuTag, List<SpuDTO> spuList) {
        if (CollUtil.isEmpty(spuList)) {
            return;
        }
        List<SpuTagReference> prodTagReferences = new ArrayList<>();
        List<Long> spuIds = new ArrayList<>();
        for (SpuDTO product : spuList) {
            spuIds.add(product.getSpuId());
            SpuTagReference prodTagReference = new SpuTagReference();
            prodTagReference.setTagId(spuTag.getId());
            prodTagReference.setShopId(spuTag.getShopId());
            prodTagReference.setStatus(1);
            prodTagReference.setSpuId(product.getSpuId());
            prodTagReference.setSeq(0);
            prodTagReferences.add(prodTagReference);
        }
        spuTagReferenceMapper.saveBatch(prodTagReferences);

        updateSpuTagInfo(spuTag.getId(), spuIds);
    }

    @Override
    public List<SpuTagReferenceVO> listByIds(List<Long> ids) {
        return spuTagReferenceMapper.listByIds(ids);
    }

    @Override
    public void updateProdSeq(List<SpuTagReferenceDTO> spuTagReferenceDTOList) {
        List<Long> spuIds = new ArrayList<>();
        Long tagId = null;
        List<SpuTagReference> spuTagReferenceList = new ArrayList<>();
        for (SpuTagReferenceDTO spuTagReferenceDTO : spuTagReferenceDTOList) {
            if (Objects.isNull(tagId)) {
                tagId = spuTagReferenceDTO.getTagId();
            }
            spuIds.add(spuTagReferenceDTO.getSpuId());
            SpuTagReference spuTagReference = new SpuTagReference();
            spuTagReference.setTagId(tagId);
            spuTagReference.setSeq(spuTagReferenceDTO.getSeq());
            spuTagReference.setSpuId(spuTagReferenceDTO.getSpuId());
            spuTagReferenceList.add(spuTagReference);
        }
        SpuTagVO spuTagVO = spuTagService.getById(tagId);
        if (Objects.isNull(spuTagVO)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        if(!Objects.equals(spuTagVO.getShopId(), AuthUserContext.get().getTenantId())){
            // 您无权进行操作
            throw new LuckException("您无权进行操作");
        }
        spuTagReferenceMapper.updateSpuSeq(spuTagReferenceList);
        updateSpuTagInfo(tagId, spuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByProdId(Long prodTagId, List<Long> spuIds) {
        // 移除关联
        spuTagReferenceMapper.removeByIds(prodTagId, spuIds);
        updateSpuTagInfo(prodTagId, spuIds);
    }

    /**
     * 更新商品分组信息
     * @param prodTagId
     * @param spuIds
     */
    private void updateSpuTagInfo(Long prodTagId, List<Long> spuIds) {
        // 更新分组商品数量
        spuTagService.updateProdCountById(prodTagId);
        // 更新es商品信息
        spuMapper.updateSpuUpdateTime(spuIds, null, null);
    }

    @Override
    public List<Long> tagListBySpuId(Long spuId) {
        return spuTagReferenceMapper.tagListBySpuId(spuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpuData(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return;
        }
        List<Long> tagIds = spuTagReferenceMapper.listSpuTagIdBySpuIds(spuIds);
        if (CollUtil.isEmpty(tagIds)) {
            return;
        }
        spuTagReferenceMapper.deleteSpuData(spuIds);
        spuTagService.batchUpdateProdCountById(tagIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpuTagByShopIdAndCategoryIds(Long shopId, List<Long> categoryIds) {
        List<Long> tagIds = spuTagReferenceMapper.getTagIdsByShopIdAndCategoryIds(shopId, categoryIds);
        if (CollUtil.isEmpty(tagIds)) {
            return;
        }
        spuTagReferenceMapper.deleteSpuTagByShopIdAndCategoryIds(shopId, categoryIds);
        spuTagService.batchUpdateProdCountById(tagIds);
    }

    @Override
    public List<Long> spuIdsByTagId(Long tagId) {
        return spuTagReferenceMapper.spuIdsByTagId(tagId);
    }

    @Override
    public PageVO<SpuPageVO> pageSpuListByTagId(PageDTO pageDTO, SpuTagReferenceDTO spuTagReferenceDTO) {
        PageVO<SpuPageVO> spuPageVO = PageUtil.doPage(pageDTO, () -> spuMapper.listByTag(spuTagReferenceDTO));
        return spuPageVO;
    }
}
