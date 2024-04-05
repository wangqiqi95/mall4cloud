package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinNewsItem;
import com.mall4j.cloud.biz.mapper.WeixinNewsItemMapper;
import com.mall4j.cloud.biz.service.WeixinNewsItemService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信图文模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:35
 */
@Service
public class WeixinNewsItemServiceImpl implements WeixinNewsItemService {

    @Autowired
    private WeixinNewsItemMapper weixinNewsItemMapper;

    @Override
    public PageVO<WeixinNewsItem> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinNewsItemMapper.list());
    }

    @Override
    public WeixinNewsItem getById(Long id) {
        return weixinNewsItemMapper.getById(id);
    }

    @Override
    public void save(WeixinNewsItem weixinNewsItem) {
        weixinNewsItemMapper.save(weixinNewsItem);
    }

    @Override
    public void update(WeixinNewsItem weixinNewsItem) {
        weixinNewsItemMapper.update(weixinNewsItem);
    }

    @Override
    public void deleteById(Long id) {
        weixinNewsItemMapper.deleteById(id);
    }
}
