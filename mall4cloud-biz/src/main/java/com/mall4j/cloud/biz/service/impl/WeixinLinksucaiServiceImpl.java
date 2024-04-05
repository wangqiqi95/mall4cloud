package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinLinksucai;
import com.mall4j.cloud.biz.mapper.WeixinLinksucaiMapper;
import com.mall4j.cloud.biz.service.WeixinLinksucaiService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信素材链接表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 22:53:05
 */
@Service
public class WeixinLinksucaiServiceImpl implements WeixinLinksucaiService {

    @Autowired
    private WeixinLinksucaiMapper weixinLinksucaiMapper;

    @Override
    public PageVO<WeixinLinksucai> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinLinksucaiMapper.list());
    }

    @Override
    public WeixinLinksucai getById(String id) {
        return weixinLinksucaiMapper.getById(id);
    }

    @Override
    public void save(WeixinLinksucai weixinLinksucai) {
        weixinLinksucaiMapper.save(weixinLinksucai);
    }

    @Override
    public void update(WeixinLinksucai weixinLinksucai) {
        weixinLinksucaiMapper.update(weixinLinksucai);
    }

    @Override
    public void deleteById(Long id) {
        weixinLinksucaiMapper.deleteById(id);
    }
}
