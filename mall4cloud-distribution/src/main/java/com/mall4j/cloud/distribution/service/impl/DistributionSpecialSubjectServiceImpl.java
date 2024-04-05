package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.dto.DistributionSpecialSubjectDTO;
import com.mall4j.cloud.distribution.mapper.DistributionSpecialSubjectMapper;
import com.mall4j.cloud.distribution.model.DistributionSpecialSubject;
import com.mall4j.cloud.distribution.model.DistributionSubjectProduct;
import com.mall4j.cloud.distribution.model.DistributionSubjectStore;
import com.mall4j.cloud.distribution.service.DistributionSpecialSubjectService;
import com.mall4j.cloud.distribution.service.DistributionSubjectProductService;
import com.mall4j.cloud.distribution.service.DistributionSubjectStoreService;
import com.mall4j.cloud.distribution.vo.DistributionSpecialSubjectVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分销推广-分销专题
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:56
 */
@Service
public class DistributionSpecialSubjectServiceImpl implements DistributionSpecialSubjectService {

    @Autowired
    private DistributionSpecialSubjectMapper distributionSpecialSubjectMapper;

    @Autowired
    private DistributionSubjectStoreService distributionSubjectStoreService;

    @Autowired
    private DistributionSubjectProductService distributionSubjectProductService;


    @Override
    public PageVO<DistributionSpecialSubjectVO> page(PageDTO pageDTO, DistributionSpecialSubjectDTO dto) {
        if (CollectionUtils.isNotEmpty(dto.getStoreList())) {
            List<DistributionSubjectStore> storeList = distributionSubjectStoreService.listInStoreIds(dto.getStoreList().stream().map(DistributionSubjectStore::getStoreId).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(storeList)) {
                return new PageVO<>();
            } else {
                dto.setIds(storeList.stream().map(DistributionSubjectStore::getSubjectId).distinct().collect(Collectors.toList()));
            }
        }
        PageVO<DistributionSpecialSubject> objectPageVO = PageUtil.doPage(pageDTO, () -> distributionSpecialSubjectMapper.list(dto));
        PageVO<DistributionSpecialSubjectVO> pageVO = new PageVO<>();

        List<DistributionSpecialSubjectVO> list = new ArrayList<>();
        objectPageVO.getList().forEach(specialSubject -> {
            DistributionSpecialSubjectVO vo = new DistributionSpecialSubjectVO();
            BeanUtils.copyProperties(specialSubject, vo);
            vo.setStoreNum(distributionSubjectStoreService.countBySpecialSubjectId(specialSubject.getId()));
            list.add(vo);
        });
        pageVO.setList(list);
        pageVO.setTotal(objectPageVO.getTotal());
        pageVO.setPages(PageUtil.getPages(objectPageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public DistributionSpecialSubjectDTO getSpecialSubjectById(Long id) {
        DistributionSpecialSubject subject = distributionSpecialSubjectMapper.getById(id);
        if (null == subject) {
            return null;
        }
        DistributionSpecialSubjectDTO dto = new DistributionSpecialSubjectDTO();
        BeanUtils.copyProperties(subject, dto);
        if (subject.getStoreType() == 1){
            dto.setStoreList(distributionSubjectStoreService.listBySubjectId(id));
        }
        if (subject.getProductType() == 1){
            dto.setProductList(distributionSubjectProductService.listBySubjectId(id));
        }
        return dto;
    }

    @Override
    public DistributionSpecialSubject getById(Long id) {
        return distributionSpecialSubjectMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DistributionSpecialSubjectDTO dto) {
        DistributionSpecialSubject distributionSpecialSubject = new DistributionSpecialSubject();
        BeanUtils.copyProperties(dto, distributionSpecialSubject);
        distributionSpecialSubjectMapper.save(distributionSpecialSubject);
        dto.setId(distributionSpecialSubject.getId());
        //保存门店
        saveStoreInfo(dto);
        //保存商品
        saveProduct(dto);
    }

    private void saveStoreInfo(DistributionSpecialSubjectDTO dto) {
        if (dto.getStoreType() == 0) {
            distributionSubjectStoreService.deleteBySubjectId(dto.getId());
            return;
        }
        if (CollectionUtils.isEmpty(dto.getStoreList())) {
            throw new LuckException("作用门店为空");
        }
        distributionSubjectStoreService.deleteBySubjectIdNotInStoreIds(dto.getId(), dto.getStoreList().stream().map(DistributionSubjectStore::getStoreId).distinct().collect(Collectors.toList()));

        List<DistributionSubjectStore> storeList = distributionSubjectStoreService.listBySubjectId(dto.getId());
        Map<String, DistributionSubjectStore> storeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeList)){
            storeMap = storeList.stream().collect(Collectors.toMap(s -> s.getSubjectId() + "_" + s.getStoreId(), s -> s));
        }

        Map<String, DistributionSubjectStore> finalStoreMap = storeMap;
        dto.getStoreList().forEach(s -> {
            if (null == finalStoreMap.get(dto.getId() + "_" + s.getStoreId())){
                DistributionSubjectStore store = new DistributionSubjectStore();
                store.setSubjectId(dto.getId());
                store.setStoreId(s.getStoreId());
                distributionSubjectStoreService.save(store);
            }
        });
    }

    private void saveProduct(DistributionSpecialSubjectDTO dto) {
        if (dto.getProductType() == 0) {
            distributionSubjectProductService.deleteBySubjectId(dto.getId());
            return;
        }
        if (CollectionUtils.isEmpty(dto.getProductList())) {
            throw new LuckException("选择商品为空");
        }
        distributionSubjectProductService.deleteBySubjectIdNotInProductIds(dto.getId(), dto.getProductList().stream().map(DistributionSubjectProduct::getProductId).distinct().collect(Collectors.toList()));

        List<DistributionSubjectProduct> productList = distributionSubjectProductService.listBySubjectId(dto.getId());
        Map<String, DistributionSubjectProduct> productMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(productList)){
            productMap = productList.stream().collect(Collectors.toMap(p -> p.getSubjectId() + "_" + p.getProductId(), p -> p));
        }

        Map<String, DistributionSubjectProduct> finalProductMap = productMap;
        dto.getProductList().forEach(p -> {
            if (null == finalProductMap.get(dto.getId() + "_" + p.getProductId())){
                DistributionSubjectProduct product = new DistributionSubjectProduct();
                product.setProductId(p.getProductId());
                product.setSubjectId(dto.getId());
                distributionSubjectProductService.save(product);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DistributionSpecialSubjectDTO dto) {
        DistributionSpecialSubject distributionSpecialSubject = new DistributionSpecialSubject();
        BeanUtils.copyProperties(dto, distributionSpecialSubject);
        distributionSpecialSubjectMapper.update(distributionSpecialSubject);
        saveStoreInfo(dto);
        saveProduct(dto);
    }

    @Override
    public void updateStatusBatch(List<Long> ids, Integer status) {
        distributionSpecialSubjectMapper.updateStatusBatch(ids, status);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        distributionSpecialSubjectMapper.deleteById(id);
        distributionSubjectStoreService.deleteBySubjectId(id);
        distributionSubjectProductService.deleteBySubjectId(id);
    }

    @Override
    public void specialSubjectTop(Long id, Integer top) {
        if (top == 1 && distributionSpecialSubjectMapper.countSpecialSubjectTopNum() >= 3) {
            throw new LuckException("置顶数量已上限");
        }
        distributionSpecialSubjectMapper.specialSubjectTop(id, top);
    }

    @Override
    public PageVO<DistributionSpecialSubjectVO> pageEffect(PageDTO pageDTO, DistributionSpecialSubjectDTO dto) {
        List<DistributionSubjectStore> storeList = distributionSubjectStoreService.listByStoreId(dto.getQueryStoreId());
        if (CollectionUtils.isNotEmpty(storeList)){
            dto.setIds(storeList.stream().map(DistributionSubjectStore::getSubjectId).distinct().collect(Collectors.toList()));
        }
        return PageUtil.doPage(pageDTO, () -> distributionSpecialSubjectMapper.pageEffect(dto));
    }
}
