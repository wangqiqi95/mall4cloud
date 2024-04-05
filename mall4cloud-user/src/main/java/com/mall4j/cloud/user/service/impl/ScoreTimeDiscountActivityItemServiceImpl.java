package com.mall4j.cloud.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.ScoreTimeDiscountActivityItem;
import com.mall4j.cloud.user.mapper.ScoreTimeDiscountActivityItemMapper;
import com.mall4j.cloud.user.service.ScoreTimeDiscountActivityItemService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 积分限时折扣兑换券
 *
 * @author gmq
 * @date 2022-07-11 15:12:37
 */
@Service
public class ScoreTimeDiscountActivityItemServiceImpl extends ServiceImpl<ScoreTimeDiscountActivityItemMapper, ScoreTimeDiscountActivityItem> implements ScoreTimeDiscountActivityItemService {

    @Autowired
    private ScoreTimeDiscountActivityItemMapper scoreTimeDiscountActivityItemMapper;

    @Override
    public PageVO<ScoreTimeDiscountActivityItem> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> scoreTimeDiscountActivityItemMapper.list());
    }

    @Override
    public ScoreTimeDiscountActivityItem getById(Long id) {
        return scoreTimeDiscountActivityItemMapper.getById(id);
    }

    @Override
    public void saveTo(ScoreTimeDiscountActivityItem scoreTimeDiscountActivityItem) {

    }

    @Override
    public void updateTo(ScoreTimeDiscountActivityItem scoreTimeDiscountActivityItem) {

    }


    @Override
    public void deleteById(Long id) {
        scoreTimeDiscountActivityItemMapper.deleteById(id);
    }
}
