package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.TagMapper;
import com.mall4j.cloud.biz.model.cp.Tag;
import com.mall4j.cloud.biz.service.cp.TagService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;

import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 客户标签配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public List<Tag> list() {
        return  tagMapper.list();
    }

    @Override
    public Tag getById(Long id) {
        return tagMapper.getById(id);
    }

    @Override
    public void save(Tag tag) {
        tagMapper.save(tag);
    }

    @Override
    public void update(Tag tag) {
        tagMapper.update(tag);
    }

    @Override
    public void deleteById(Long id) {
        tagMapper.deleteById(id);
    }
}
