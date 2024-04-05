package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.dto.DistributionMomentsDTO;
import com.mall4j.cloud.distribution.mapper.DistributionMomentsMapper;
import com.mall4j.cloud.distribution.model.DistributionMoments;
import com.mall4j.cloud.distribution.model.DistributionMomentsProduct;
import com.mall4j.cloud.distribution.model.DistributionMomentsStore;
import com.mall4j.cloud.distribution.service.DistributionMomentsProductService;
import com.mall4j.cloud.distribution.service.DistributionMomentsService;
import com.mall4j.cloud.distribution.service.DistributionMomentsStoreService;
import com.mall4j.cloud.distribution.vo.DistributionMomentsVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Service
public class DistributionMomentsServiceImpl implements DistributionMomentsService {

    @Autowired
    private DistributionMomentsMapper distributionMomentsMapper;

    @Autowired
    private DistributionMomentsStoreService distributionMomentsStoreService;

    @Autowired
    private DistributionMomentsProductService distributionMomentsProductService;


    @Override
    public PageVO<DistributionMomentsVO> page(PageDTO pageDTO, DistributionMomentsDTO dto) {
//        if (null != dto.getQueryStoreId()) {
//            List<DistributionMomentsStore> storeList = distributionMomentsStoreService.listByStoreId(dto.getQueryStoreId());
//            if (CollectionUtils.isNotEmpty(storeList)){
//                dto.setIds(storeList.stream().map(DistributionMomentsStore::getMomentsId).distinct().collect(Collectors.toList()));
//            }
//        }


        PageVO<DistributionMoments> objectPageVO = PageUtil.doPage(pageDTO, () -> distributionMomentsMapper.list(dto));

        PageVO<DistributionMomentsVO> pageVO = new PageVO<>();

        List<DistributionMomentsVO> list = new ArrayList<>();
        objectPageVO.getList().forEach(distributionMoments -> {
            DistributionMomentsVO vo = new DistributionMomentsVO();
            BeanUtils.copyProperties(distributionMoments, vo);
            vo.setStoreNum(distributionMomentsStoreService.countByMomentsId(distributionMoments.getId()));
            list.add(vo);
        });
        pageVO.setList(list);
        pageVO.setTotal(objectPageVO.getTotal());
        pageVO.setPages(PageUtil.getPages(objectPageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public DistributionMomentsDTO getMomentsById(Long id) {
        DistributionMoments moments = distributionMomentsMapper.getById(id);
        if (null != moments){
            DistributionMomentsDTO dto = new DistributionMomentsDTO();
            BeanUtils.copyProperties(moments, dto);
            if (moments.getStoreType() == 1){
                dto.setStoreList(distributionMomentsStoreService.listByMomentsId(id));
            }
            dto.setProductList(distributionMomentsProductService.listByMomentsId(id));
            return dto;
        }
        return null;
    }

    @Override
    public DistributionMoments getById(Long id) {
        return distributionMomentsMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DistributionMomentsDTO dto) {
        DistributionMoments distributionMoments = new DistributionMoments();
        BeanUtils.copyProperties(dto, distributionMoments);
        distributionMomentsMapper.save(distributionMoments);
        dto.setId(distributionMoments.getId());
        saveStoreInfo(dto);
        saveProduct(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DistributionMomentsDTO dto) {
        DistributionMoments distributionMoments = new DistributionMoments();
        BeanUtils.copyProperties(dto, distributionMoments);
        distributionMomentsMapper.update(distributionMoments);
        saveStoreInfo(dto);
        saveProduct(dto);
    }

    private void saveStoreInfo(DistributionMomentsDTO dto) {
        if (dto.getStoreType() == 0) {
            distributionMomentsStoreService.deleteByMomentsId(dto.getId());
            return;
        }
        if (CollectionUtils.isEmpty(dto.getStoreList())) {
            throw new LuckException("作用门店为空");
        }
        distributionMomentsStoreService.deleteByMomentsIdNotInStoreIds(dto.getId(), dto.getStoreList().stream().map(DistributionMomentsStore::getStoreId).distinct().collect(Collectors.toList()));

        List<DistributionMomentsStore> storeList = distributionMomentsStoreService.listByMomentsId(dto.getId());
        Map<String, DistributionMomentsStore> storeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeList)){
            storeMap = storeList.stream().collect(Collectors.toMap(s -> s.getMomentsId() + "_" + s.getStoreId(), s -> s));
        }

        Map<String, DistributionMomentsStore> finalStoreMap = storeMap;
        dto.getStoreList().forEach(s -> {
            if (null == finalStoreMap.get(dto.getId() + "_" + s.getStoreId())) {
                DistributionMomentsStore store = new DistributionMomentsStore();
                store.setMomentsId(dto.getId());
                store.setStoreId(s.getStoreId());
                distributionMomentsStoreService.save(store);
            }
        });
    }

    private void saveProduct(DistributionMomentsDTO dto) {
        if (CollectionUtils.isEmpty(dto.getProductList())) {
            distributionMomentsProductService.deleteByMomentsId(dto.getId());
            return;
        }
        distributionMomentsProductService.deleteByMomentsId(dto.getId());
        for (int i = 0; i < dto.getProductList().size(); i++) {
            DistributionMomentsProduct product = dto.getProductList().get(i);
            product.setMomentsId(dto.getId());
            product.setShowSort(i);
            distributionMomentsProductService.save(product);
        }
    }

    @Override
    public void deleteById(Long id) {
        distributionMomentsMapper.deleteById(id);
        distributionMomentsProductService.deleteByMomentsId(id);
        distributionMomentsStoreService.deleteByMomentsId(id);
    }

    @Override
    public void updateStatusBatch(List<Long> ids, Integer status) {
        distributionMomentsMapper.updateStatusBatch(ids, status);
    }

    @Override
    public void momentsTop(Long id, Integer top) {
        if (top ==1 && distributionMomentsMapper.countMomentsTopNum() >= 3){
            throw new LuckException("置顶数量已上限");
        }
        distributionMomentsMapper.momentsTop(id, top);
    }

    @Override
    public PageVO<DistributionMoments> pageEffect(PageDTO pageDTO, DistributionMomentsDTO dto) {
        List<DistributionMomentsStore> storeList = distributionMomentsStoreService.listByStoreId(dto.getQueryStoreId());
        if (CollectionUtils.isNotEmpty(storeList)){
            dto.setIds(storeList.stream().map(DistributionMomentsStore::getMomentsId).distinct().collect(Collectors.toList()));
        }
        PageVO<DistributionMoments> pageVO = PageUtil.doPage(pageDTO, () -> distributionMomentsMapper.pageEffect(dto));
        if (Objects.nonNull(pageVO) && CollectionUtils.isNotEmpty(pageVO.getList())) {
            List<Long> momentsIdList = pageVO.getList().stream().map(DistributionMoments :: getId).collect(Collectors.toList());
            Map<Long, List<DistributionMomentsProduct>>  momentsProductMap = distributionMomentsProductService
                    .listByMomentsIdList(momentsIdList).stream().collect(Collectors.groupingBy(DistributionMomentsProduct :: getMomentsId));
            pageVO.getList().stream().forEach(m -> {
                m.setProductList(momentsProductMap.get(m.getId()));
            });
        }
        return pageVO;
    }
}
