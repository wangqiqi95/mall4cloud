package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventStatus;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.platform.model.OfflineHandleEvent;
import com.mall4j.cloud.platform.mapper.OfflineHandleEventMapper;
import com.mall4j.cloud.platform.model.OfflineHandleEventItem;
import com.mall4j.cloud.platform.service.OfflineHandleEventItemService;
import com.mall4j.cloud.platform.service.OfflineHandleEventService;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 下线处理事件
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
@Service
public class OfflineHandleEventServiceImpl implements OfflineHandleEventService {

    @Autowired
    private OfflineHandleEventMapper offlineHandleEventMapper;
    @Autowired
    private OfflineHandleEventItemService offlineHandleEventItemService;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<OfflineHandleEventVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> offlineHandleEventMapper.list());
    }

    @Override
    public OfflineHandleEventVO getByEventId(Long eventId) {
        OfflineHandleEventVO offlineHandleEventVO = mapperFacade.map(offlineHandleEventMapper.getByEventId(eventId), OfflineHandleEventVO.class);
        return offlineHandleEventVO;
    }

    @Override
    public void save(OfflineHandleEvent offlineHandleEvent) {
        offlineHandleEvent.setShopId(AuthUserContext.get().getTenantId());
        offlineHandleEvent.setHandlerId(AuthUserContext.get().getUserId());
        offlineHandleEvent.setStatus(OfflineHandleEventStatus.OFFLINE_BY_PLATFORM.getValue());
        offlineHandleEventMapper.save(offlineHandleEvent);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(Long eventId, String reapplyReason) {
        // 更新事件为申请状态
        offlineHandleEventMapper.updateToApply(eventId);
        // 添加申请记录
        OfflineHandleEventItem offlineHandleEventItem = new OfflineHandleEventItem();
        offlineHandleEventItem.setEventId(eventId);
        offlineHandleEventItem.setReapplyReason(reapplyReason);
        offlineHandleEventItemService.save(offlineHandleEventItem);
    }

    @Override
    public OfflineHandleEventVO getProcessingEventByHandleTypeAndHandleId(Integer handleType, Long handleId) {
        return offlineHandleEventMapper.getProcessingEventByHandleTypeAndHandleId(handleType, handleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateToApply(Long eventId, String reapplyReason) {
        OfflineHandleEventVO eventVO = getByEventId(eventId);
        boolean canApply = !Objects.equals(eventVO.getStatus(), OfflineHandleEventStatus.OFFLINE_BY_PLATFORM.getValue()) &&
                !Objects.equals(eventVO.getStatus(), OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue());
        if (Objects.isNull(eventVO) || canApply) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        // 更新事件状态
        offlineHandleEventMapper.updateToApply(eventId);

        // 添加事件申请
        OfflineHandleEventItem offlineHandleEventItem = new OfflineHandleEventItem();
        offlineHandleEventItem.setEventId(eventId);
        offlineHandleEventItem.setReapplyReason(reapplyReason);
        offlineHandleEventItemService.save(offlineHandleEventItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditOfflineEvent(OfflineHandleEventDTO offlineHandleEventDTO) {
        OfflineHandleEvent offlineHandleEvent = offlineHandleEventMapper.getByEventId(offlineHandleEventDTO.getEventId());
        if (Objects.isNull(offlineHandleEvent) || !Objects.equals(offlineHandleEvent.getStatus(), OfflineHandleEventStatus.APPLY_BY_SHOP.getValue())) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        // 审核通过
        if (Objects.equals(offlineHandleEventDTO.getStatus(), OfflineHandleEventStatus.AGREE_BY_PLATFORM.getValue())) {
            offlineHandleEvent.setStatus(OfflineHandleEventStatus.AGREE_BY_PLATFORM.getValue());
        }
        // 审核不通过
        else if (Objects.equals(offlineHandleEventDTO.getStatus(), OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue())) {
            offlineHandleEvent.setStatus(OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue());
        }
        Date date = new Date();
        offlineHandleEvent.setHandlerId(AuthUserContext.get().getUserId());
        offlineHandleEventMapper.update(offlineHandleEvent);

        OfflineHandleEventItem offlineHandleEventItem = offlineHandleEventItemService.getNewOfflineHandleEventItem(offlineHandleEvent.getEventId());
        offlineHandleEventItem.setRefuseReason(offlineHandleEventDTO.getRefuseReason());
        offlineHandleEventItem.setAuditTime(date);
        offlineHandleEventItemService.update(offlineHandleEventItem);
    }

    @Override
    public int offlineCount(Integer handleType, Long handleId) {
        return offlineHandleEventMapper.offlineCount(handleType, handleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OfflineHandleEventDTO offlineHandleEventDto) {
        OfflineHandleEvent offlineHandleEvent = mapperFacade.map(offlineHandleEventDto, OfflineHandleEvent.class);
        offlineHandleEventMapper.update(offlineHandleEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByHandleTypeAndHandleId(Integer handleType, Long handleId) {
        List<Long> eventIds = offlineHandleEventMapper.listIdByHandleTypeAndHandleId(handleType, handleId);
        if (CollUtil.isEmpty(eventIds)) {
            return;
        }
        // 删除下线处理事件记录项
        offlineHandleEventItemService.deleteByEventIds(eventIds);
        // 删除下线处理事件
        offlineHandleEventMapper.deleteByHandleTypeAndHandleId(handleType, handleId);
    }
}
