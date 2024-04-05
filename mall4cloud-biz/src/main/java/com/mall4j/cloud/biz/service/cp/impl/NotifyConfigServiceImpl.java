package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.api.biz.constant.cp.NotityTypeEunm;
import com.mall4j.cloud.biz.mapper.cp.NotifyConfigMapper;
import com.mall4j.cloud.biz.model.cp.NotifyConfig;
import com.mall4j.cloud.biz.service.cp.NotifyConfigService;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;

import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 应用消息配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@Service
public class NotifyConfigServiceImpl implements NotifyConfigService {

    private final NotifyConfigMapper messageMapper;

    @Override
    public PageVO<NotifyConfig> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> messageMapper.list(null,null));
    }

    @Override
    public NotifyConfig getById(Long id) {
        return messageMapper.getById(id);
    }

    @Override
    public void save(NotifyConfig message) {
        messageMapper.save(message);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.CONFIG_MSG_ALL),
            @CacheEvict(cacheNames = CacheNames.CONFIG_MSG_TYPE)
    })
    public void update(NotifyConfig message) {
        messageMapper.update(message);
    }

    @Override
    public void deleteById(Long id) {
        messageMapper.deleteById(id);
    }

    @Override
//    @Cacheable(cacheNames = CacheNames.CONFIG_MSG_TYPE, key = "#type")
    public NotifyConfig getNotifyByType(NotityTypeEunm type) {
        List<NotifyConfig> list =  messageMapper.list(type.getCode(),null);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @PostConstruct
    @Cacheable(cacheNames = CacheNames.CONFIG_MSG_ALL, key = "'all'")
    public List<NotifyConfig> list () {
        return messageMapper.list(null,1);
    }



}
