package com.mall4j.cloud.platform.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.TentacleMarketingPlan;
import com.mall4j.cloud.platform.mapper.TentacleMarketingPlanMapper;
import com.mall4j.cloud.platform.service.TentacleMarketingPlanService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 触点作业批次
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Service
public class TentacleMarketingPlanServiceImpl implements TentacleMarketingPlanService {

    @Autowired
    private TentacleMarketingPlanMapper tentacleMarketingPlanMapper;

    @Override
    public PageVO<TentacleMarketingPlan> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> tentacleMarketingPlanMapper.list());
    }

    @Override
    public TentacleMarketingPlan getById(Long id) {
        return tentacleMarketingPlanMapper.getById(id);
    }

    @Override
    public void save(TentacleMarketingPlan tentacleMarketingPlan) {
        tentacleMarketingPlanMapper.save(tentacleMarketingPlan);
    }

    @Override
    public void update(TentacleMarketingPlan tentacleMarketingPlan) {
        tentacleMarketingPlanMapper.update(tentacleMarketingPlan);
    }

    @Override
    public void deleteById(Long id) {
        tentacleMarketingPlanMapper.deleteById(id);
    }
}
