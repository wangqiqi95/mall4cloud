package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.biz.dto.NotifyLogDTO;
import com.mall4j.cloud.biz.model.NotifyLog;
import com.mall4j.cloud.biz.vo.NotifyLogVO;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.mapper.NotifyLogMapper;
import com.mall4j.cloud.biz.service.NotifyLogService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
@Service
public class NotifyLogServiceImpl implements NotifyLogService {

    @Autowired
    private NotifyLogMapper notifyLogMapper;

    @Override
    public PageVO<NotifyLogVO> pageBySendTypeAndRemindType(PageDTO page, Long userId, Integer msgType, Integer status) {
        // 不要用已读未读及更新时间作为排序的条件，查询消息列表时未读消息会更新为已读消息，此时消息读取状态发生、更新时间发生改变
        return PageUtil.doPage(page, () -> notifyLogMapper.listBySendTypeAndRemindType(userId,msgType,status));
    }

    @Override
    public void updateBatchById(List<NotifyLogVO> notifyLogList) {
        notifyLogMapper.updateBatchById(notifyLogList);
    }

    @Override
    public int countUnreadBySendTypeAndRemindType(Long userId, Integer msgType) {
        List<NotifyLogVO> notifyLogList = notifyLogMapper.listBySendTypeAndRemindType(userId, msgType, StatusEnum.DISABLE.value());

        return CollectionUtil.isEmpty(notifyLogList) ? 0 : notifyLogList.size();
    }

    @Override
    public void saveBatch(List<NotifyLog> notifyLogs) {
        notifyLogMapper.saveBatch(notifyLogs);
    }

    @Override
    public PageVO<NotifyLogVO> page(PageDTO pageDTO, NotifyLogDTO notifyLogDTO) {
        PageVO<NotifyLogVO> page = PageUtil.doPage(pageDTO, () -> notifyLogMapper.list(notifyLogDTO));
        if (CollUtil.isEmpty(page.getList())) {
            return page;
        }
        return page;
    }
}
