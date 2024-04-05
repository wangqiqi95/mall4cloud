package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.user.mapper.UserLevelRightsMapper;
import com.mall4j.cloud.user.model.UserLevel;
import com.mall4j.cloud.user.service.UserLevelRightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 等级-权益关联信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Service
public class UserLevelRightsServiceImpl implements UserLevelRightsService {

    @Autowired
    private UserLevelRightsMapper userLevelRightsMapper;

    @Override
    public void save(Long userLevelId,  List<Long> rightIds) {
        if(CollectionUtil.isEmpty(rightIds)) {
            return;
        }
        userLevelRightsMapper.saveBatch(userLevelId, rightIds);
    }

    @Override
    public void update(Long userLevelId, List<Long> rightIds) {
        userLevelRightsMapper.deleteByUserLevelId(userLevelId);
        if (CollUtil.isNotEmpty(rightIds)) {
            save(userLevelId, rightIds);
        }
    }

    @Override
    public void deleteByUserLevelId(Long userLevelId) {
        userLevelRightsMapper.deleteByUserLevelId(userLevelId);
    }

    @Override
    public List<UserLevel> listUserLevelIdByRightId(Long rightId) {
        return userLevelRightsMapper.listUserLevelIdByRightId(rightId);
    }


}
