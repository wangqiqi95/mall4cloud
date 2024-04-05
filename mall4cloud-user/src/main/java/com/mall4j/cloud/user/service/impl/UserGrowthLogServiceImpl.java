package com.mall4j.cloud.user.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserGrowthLog;
import com.mall4j.cloud.user.mapper.UserGrowthLogMapper;
import com.mall4j.cloud.user.service.UserGrowthLogService;
import com.mall4j.cloud.user.vo.UserGrowthLogVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 用户成长值记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Service
public class UserGrowthLogServiceImpl implements UserGrowthLogService {

    @Autowired
    private UserGrowthLogMapper userGrowthLogMapper;

    @Override
    public PageVO<UserGrowthLogVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userGrowthLogMapper.list());
    }


    @Override
    public UserGrowthLog getByLogId(Long logId) {
        return userGrowthLogMapper.getByLogId(logId);
    }

    @Override
    public void save(UserGrowthLog userGrowthLog) {
        userGrowthLogMapper.save(userGrowthLog);
    }

    @Override
    public void update(UserGrowthLog userGrowthLog) {
        userGrowthLogMapper.update(userGrowthLog);
    }

    @Override
    public void deleteById(Long logId) {
        userGrowthLogMapper.deleteById(logId);
    }

    @Override
    public int saveBatch(List<UserGrowthLog> userGrowthLogs) {
        return userGrowthLogMapper.saveBatch(userGrowthLogs);
    }

    @Override
    public PageVO<UserGrowthLogVO> getPageByUserId(PageDTO pageDTO, Long userId) {
        return PageUtil.doPage(pageDTO, () -> userGrowthLogMapper.getPageByUserId(userId));
    }

    @Override
    public UserGrowthLog getByBizId(Long bizId, Integer ioType) {
        return userGrowthLogMapper.getByBizId(bizId, ioType);
    }
}
