package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.dto.PromotionColumnDTO;
import com.mall4j.cloud.group.mapper.PromotionColumnMapper;
import com.mall4j.cloud.group.model.PromotionColumn;
import com.mall4j.cloud.group.service.PromotionColumnService;
import com.mall4j.cloud.group.vo.PromotionColumnPageVO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityPageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PromotionColumnServiceImpl extends ServiceImpl<PromotionColumnMapper, PromotionColumn> implements PromotionColumnService {


    @Autowired
    PromotionColumnMapper promotionColumnMapper;

    @Override
    public PageVO<PromotionColumnPageVO> page(PageDTO pageDTO, PromotionColumnDTO param) {
        PageVO<PromotionColumnPageVO> pageVO = PageUtil.doPage(pageDTO, () -> promotionColumnMapper.list(param));
        return pageVO;
    }

    @Override
    public PromotionColumnPageVO detail(int intValue) {
        return null;
    }
}
