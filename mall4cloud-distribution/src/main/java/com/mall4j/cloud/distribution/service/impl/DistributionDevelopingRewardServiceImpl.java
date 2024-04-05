package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.dto.DistributionDevelopingRewardDTO;
import com.mall4j.cloud.distribution.mapper.DistributionDevelopingRewardMapper;
import com.mall4j.cloud.distribution.model.DistributionDevelopingReward;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardStore;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardService;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardStoreService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分销推广-发展奖励
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
@Service
public class DistributionDevelopingRewardServiceImpl implements DistributionDevelopingRewardService {

    @Autowired
    private DistributionDevelopingRewardMapper distributionDevelopingRewardMapper;

    @Autowired
    private DistributionDevelopingRewardStoreService distributionDevelopingRewardStoreService;

    @Override
    public PageVO<DistributionDevelopingReward> page(PageDTO pageDTO, DistributionDevelopingRewardDTO dto) {
        if (null != dto.getQueryStoreId()) {
            List<DistributionDevelopingRewardStore> storeList = distributionDevelopingRewardStoreService.listByStoreId(dto.getQueryStoreId());
            if (CollectionUtils.isEmpty(storeList)) {
                return new PageVO<>();
            } else {
                dto.setIds(storeList.stream().map(DistributionDevelopingRewardStore::getStoreId).distinct().collect(Collectors.toList()));
            }
        }
        return PageUtil.doPage(pageDTO, () -> distributionDevelopingRewardMapper.list(dto));
    }

    @Override
    public DistributionDevelopingRewardDTO getById(Long id) {
        DistributionDevelopingReward reward = distributionDevelopingRewardMapper.getById(id);
        if (null != reward) {
            DistributionDevelopingRewardDTO dto = new DistributionDevelopingRewardDTO();
            BeanUtils.copyProperties(reward, dto);
            if (reward.getStoreType() == 1) {
                dto.setStoreList(distributionDevelopingRewardStoreService.listByRewardId(id));
            }
            return dto;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DistributionDevelopingRewardDTO dto) {
        DistributionDevelopingReward distributionDevelopingReward = new DistributionDevelopingReward();
        BeanUtils.copyProperties(dto, distributionDevelopingReward);
        distributionDevelopingReward.setNumbering((int)((Math.random() * 9 + 1) * 100000));
        distributionDevelopingReward.setStaffNum(0);
        distributionDevelopingReward.setMemberNum(0);
        distributionDevelopingReward.setTotalReward(0L);
        distributionDevelopingRewardMapper.save(distributionDevelopingReward);
        dto.setId(distributionDevelopingReward.getId());
        saveStoreInfo(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DistributionDevelopingRewardDTO dto) {
        DistributionDevelopingReward distributionDevelopingReward = new DistributionDevelopingReward();
        BeanUtils.copyProperties(dto, distributionDevelopingReward);
        distributionDevelopingRewardMapper.update(distributionDevelopingReward);
        saveStoreInfo(dto);
    }

    private void saveStoreInfo(DistributionDevelopingRewardDTO dto) {
        if (dto.getStoreType() == 0) {
            distributionDevelopingRewardStoreService.deleteByRewardId(dto.getId());
            return;
        }
        if (CollectionUtils.isEmpty(dto.getStoreList())) {
            throw new LuckException("作用门店为空");
        }
        distributionDevelopingRewardStoreService.deleteByRewardIdNotInStoreIds(dto.getId(), dto.getStoreList().stream().map(DistributionDevelopingRewardStore::getStoreId).distinct().collect(Collectors.toList()));

        List<DistributionDevelopingRewardStore> storeList = distributionDevelopingRewardStoreService.listByRewardId(dto.getId());
        Map<String, DistributionDevelopingRewardStore> storeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeList)){
            storeMap = storeList.stream().collect(Collectors.toMap(s -> s.getDevelopingRewardId() + "_" + s.getStoreId(), s -> s));
        }

        Map<String, DistributionDevelopingRewardStore> finalStoreMap = storeMap;
        dto.getStoreList().forEach(s -> {
            if (null == finalStoreMap.get(dto.getId() + "_" + s.getStoreId())){
                DistributionDevelopingRewardStore store = new DistributionDevelopingRewardStore();
                store.setDevelopingRewardId(dto.getId());
                store.setStoreId(s.getStoreId());
                distributionDevelopingRewardStoreService.save(store);
            }

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        distributionDevelopingRewardMapper.deleteById(id);
        distributionDevelopingRewardStoreService.deleteByRewardId(id);
    }

    @Override
    public List<DistributionDevelopingReward> listEffectRewardByIds(List<Long> ids) {
        return distributionDevelopingRewardMapper.listEffectRewardByIds(ids);
    }
}
