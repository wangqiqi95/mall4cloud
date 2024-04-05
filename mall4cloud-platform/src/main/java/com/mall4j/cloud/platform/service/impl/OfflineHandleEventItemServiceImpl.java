package com.mall4j.cloud.platform.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.OfflineHandleEventItem;
import com.mall4j.cloud.platform.mapper.OfflineHandleEventItemMapper;
import com.mall4j.cloud.platform.service.OfflineHandleEventItemService;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventItemVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 下线处理事件记录项
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
@Service
public class OfflineHandleEventItemServiceImpl implements OfflineHandleEventItemService {

    @Autowired
    private OfflineHandleEventItemMapper offlineHandleEventItemMapper;

    @Override
    public PageVO<OfflineHandleEventItemVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> offlineHandleEventItemMapper.list());
    }

    @Override
    public OfflineHandleEventItemVO getByEventItemId(Long eventItemId) {
        return offlineHandleEventItemMapper.getByEventItemId(eventItemId);
    }

    @Override
    public void save(OfflineHandleEventItem offlineHandleEventItem) {
        offlineHandleEventItemMapper.save(offlineHandleEventItem);
    }

    @Override
    public void update(OfflineHandleEventItem offlineHandleEventItem) {
        offlineHandleEventItemMapper.update(offlineHandleEventItem);
    }

    @Override
    public OfflineHandleEventItem getNewOfflineHandleEventItem(Long eventId) {
        return offlineHandleEventItemMapper.getNewOfflineHandleEventItem(eventId);
    }

    @Override
    public void deleteByEventIds(List<Long> eventIds) {
        offlineHandleEventItemMapper.deleteByEventIds(eventIds);
    }
}
