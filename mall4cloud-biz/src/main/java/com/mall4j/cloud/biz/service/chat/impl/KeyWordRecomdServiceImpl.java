package com.mall4j.cloud.biz.service.chat.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.chat.KeyWordRecomdMapper;
import com.mall4j.cloud.biz.model.chat.KeyWordRecomd;
import com.mall4j.cloud.biz.service.chat.KeyWordRecomdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会话关键词推荐回复内容
 *
 * @author gmq
 * @date 2024-01-05 15:19:52
 */
@Service
public class KeyWordRecomdServiceImpl extends ServiceImpl<KeyWordRecomdMapper,KeyWordRecomd> implements KeyWordRecomdService {

    @Autowired
    private KeyWordRecomdMapper keyWordRecomdMapper;

    @Override
    public void deleteByKeyWordId(Long keyWordId) {
        keyWordRecomdMapper.deleteByKeyWordId(keyWordId);
    }
}
