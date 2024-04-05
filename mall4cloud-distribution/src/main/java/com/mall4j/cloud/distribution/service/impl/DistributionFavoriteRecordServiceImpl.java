package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionFavoriteRecord;
import com.mall4j.cloud.distribution.mapper.DistributionFavoriteRecordMapper;
import com.mall4j.cloud.distribution.service.DistributionFavoriteRecordService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分享推广-收藏记录
 *
 * @author gww
 * @date 2022-01-30 02:23:13
 */
@Service
public class DistributionFavoriteRecordServiceImpl implements DistributionFavoriteRecordService {

    @Autowired
    private DistributionFavoriteRecordMapper distributionFavoriteRecordMapper;

    @Override
    public PageVO<DistributionFavoriteRecord> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionFavoriteRecordMapper.list());
    }

    @Override
    public void save(DistributionFavoriteRecord distributionFavoriteRecord) {
        distributionFavoriteRecordMapper.save(distributionFavoriteRecord);
    }
}
