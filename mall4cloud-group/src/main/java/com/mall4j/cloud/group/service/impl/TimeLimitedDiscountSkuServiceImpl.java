package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.TimeLimitedDiscountSku;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountSkuMapper;
import com.mall4j.cloud.group.service.TimeLimitedDiscountSkuService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 限时调价活动 sku价格
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
@Service
public class TimeLimitedDiscountSkuServiceImpl implements TimeLimitedDiscountSkuService {

    @Autowired
    private TimeLimitedDiscountSkuMapper timeLimitedDiscountSkuMapper;

    @Override
    public PageVO<TimeLimitedDiscountSku> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> timeLimitedDiscountSkuMapper.list());
    }

    @Override
    public TimeLimitedDiscountSku getById(Long id) {
        return timeLimitedDiscountSkuMapper.getById(id);
    }

    @Override
    public void save(TimeLimitedDiscountSku timeLimitedDiscountSku) {
        timeLimitedDiscountSkuMapper.save(timeLimitedDiscountSku);
    }

    @Override
    public void update(TimeLimitedDiscountSku timeLimitedDiscountSku) {
        timeLimitedDiscountSkuMapper.update(timeLimitedDiscountSku);
    }

    @Override
    public void deleteById(Long id) {
        timeLimitedDiscountSkuMapper.deleteById(id);
    }
}
