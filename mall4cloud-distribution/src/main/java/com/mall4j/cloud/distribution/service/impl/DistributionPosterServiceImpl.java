package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.distribution.dto.DistributionPosterDTO;
import com.mall4j.cloud.distribution.mapper.DistributionPosterMapper;
import com.mall4j.cloud.distribution.model.DistributionPoster;
import com.mall4j.cloud.distribution.model.DistributionPosterStore;
import com.mall4j.cloud.distribution.service.DistributionPosterService;
import com.mall4j.cloud.distribution.service.DistributionPosterStoreService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分销推广-推广海报
 *
 * @author ZengFanChang
 * @date 2021-12-20 20:26:44
 */
@Service
public class DistributionPosterServiceImpl implements DistributionPosterService {

    @Autowired
    private DistributionPosterMapper distributionPosterMapper;

    @Autowired
    private DistributionPosterStoreService distributionPosterStoreService;

    @Override
    public PageVO<DistributionPoster> page(PageDTO pageDTO, DistributionPosterDTO distributionPosterDTO) {
        PageVO<DistributionPoster> pageVO = PageUtil.doPage(pageDTO, () -> distributionPosterMapper.list(distributionPosterDTO));
        if (CollectionUtils.isNotEmpty(pageVO.getList())) {
            List<Long> posterIdList = pageVO.getList().stream().map(DistributionPoster :: getId).collect(Collectors.toList());
            List<DistributionPosterStore> posterStoreList = distributionPosterStoreService.listByPosterIdList(posterIdList);
            Map<Long, List<DistributionPosterStore>> posterStoreMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(posterStoreList)) {
                posterStoreMap = posterStoreList.stream().collect(Collectors.groupingBy(DistributionPosterStore :: getPosterId));
            }
            Map<Long, List<DistributionPosterStore>> finalPosterStoreMap = posterStoreMap;
            pageVO.getList().stream().forEach(p -> {
                if (p.getStoreType() == 1 && finalPosterStoreMap.containsKey(p.getId())) {
                    p.setStoreIdList(finalPosterStoreMap.get(p.getId()).stream().map(DistributionPosterStore :: getStoreId).collect(Collectors.toList()));
                }
            });
        }
        return pageVO;
    }

    @Override
    public DistributionPoster getById(Long id) {
        return distributionPosterMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DistributionPosterDTO dto) {
        DistributionPoster distributionPoster = new DistributionPoster();
        BeanUtils.copyProperties(dto, distributionPoster);
        buildDistributionPoster(distributionPoster);
        distributionPosterMapper.save(distributionPoster);
        dto.setId(distributionPoster.getId());
        saveStoreInfo(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DistributionPosterDTO dto) {
        DistributionPoster distributionPoster = new DistributionPoster();
        BeanUtils.copyProperties(dto, distributionPoster);
        buildDistributionPoster(distributionPoster);
        distributionPosterMapper.update(distributionPoster);
        saveStoreInfo(dto);
    }

    private void buildDistributionPoster(DistributionPoster distributionPoster) {
        if (null == distributionPoster.getMaterialId() &&
                (distributionPoster.getPosterType() != 1 || distributionPoster.getPosterType() != 2)) {
            distributionPoster.setMaterialId(0L);
        }
        if (distributionPoster.getPosterType() != 3 || StringUtils.isNotEmpty(distributionPoster.getPublicityText())) {
            return;
        }
        distributionPoster.setPublicityText("扫码有惊喜，好店在这里！");
    }

    private void saveStoreInfo(DistributionPosterDTO dto) {
        if (dto.getStoreType() == 0) {
            distributionPosterStoreService.deleteByPosterId(dto.getId());
            return;
        }
        if (CollectionUtils.isEmpty(dto.getStoreList())) {
            throw new LuckException("作用门店为空");
        }
        distributionPosterStoreService.deleteByPosterAndNotInStore(dto.getId(), dto.getStoreList().stream().map(DistributionPosterStore::getStoreId).distinct().collect(Collectors.toList()));

        List<DistributionPosterStore> storeList = distributionPosterStoreService.listByPosterId(dto.getId());
        Map<String, DistributionPosterStore> storeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeList)) {
            storeMap = storeList.stream().collect(Collectors.toMap(s -> s.getPosterId() + "_" + s.getStoreId(), s -> s));
        }

        Map<String, DistributionPosterStore> finalStoreMap = storeMap;
        dto.getStoreList().forEach(s -> {
            if (null == finalStoreMap.get(dto.getId() + "_" + s.getStoreId())) {
                DistributionPosterStore store = new DistributionPosterStore();
                store.setPosterId(dto.getId());
                store.setStoreId(s.getStoreId());
                distributionPosterStoreService.save(store);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        distributionPosterMapper.deleteById(id);
    }

    @Override
    public PageVO<DistributionPoster> pageEffect(PageDTO pageDTO, DistributionPosterDTO distributionPosterDTO) {
        List<DistributionPosterStore> storeList = distributionPosterStoreService.listByStoreId(distributionPosterDTO.getQueryStoreId());
        if (CollectionUtils.isNotEmpty(storeList)) {
            distributionPosterDTO.setIds(storeList.stream().map(DistributionPosterStore::getPosterId).distinct().collect(Collectors.toList()));
        }
        return PageUtil.doPage(pageDTO, () -> distributionPosterMapper.pageEffect(distributionPosterDTO));
    }
}
