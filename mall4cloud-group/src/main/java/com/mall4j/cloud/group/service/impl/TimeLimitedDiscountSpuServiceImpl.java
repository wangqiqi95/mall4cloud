package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.TimeLimitedDiscountSpu;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountSpuMapper;
import com.mall4j.cloud.group.service.TimeLimitedDiscountSpuService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 限时调价活动 spu价格
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
@Service
public class TimeLimitedDiscountSpuServiceImpl implements TimeLimitedDiscountSpuService {

    @Autowired
    private TimeLimitedDiscountSpuMapper timeLimitedDiscountSpuMapper;

    @Override
    public PageVO<TimeLimitedDiscountSpu> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> timeLimitedDiscountSpuMapper.list());
    }

    @Override
    public TimeLimitedDiscountSpu getById(Long id) {
        return timeLimitedDiscountSpuMapper.getById(id);
    }

    @Override
    public void save(TimeLimitedDiscountSpu timeLimitedDiscountSpu) {
        timeLimitedDiscountSpuMapper.save(timeLimitedDiscountSpu);
    }

    @Override
    public void update(TimeLimitedDiscountSpu timeLimitedDiscountSpu) {
        timeLimitedDiscountSpuMapper.update(timeLimitedDiscountSpu);
    }

    @Override
    public void deleteById(Long id) {
        timeLimitedDiscountSpuMapper.deleteById(id);
    }

    @Override
    public void removeByActivity(Integer activityId) {
        timeLimitedDiscountSpuMapper.removeByActivity(activityId);
    }
}
