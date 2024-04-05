package com.mall4j.cloud.user.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserStaffRelationFollowUp;
import com.mall4j.cloud.user.mapper.UserStaffRelationFollowUpMapper;
import com.mall4j.cloud.user.service.UserStaffRelationFollowUpService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 会员跟进记录
 *
 * @author FrozenWatermelon
 * @date 2023-11-13 17:37:14
 */
@Service
public class UserStaffRelationFollowUpServiceImpl implements UserStaffRelationFollowUpService {

    @Autowired
    private UserStaffRelationFollowUpMapper userStaffRelationFollowUpMapper;

    @Override
    public PageVO<UserStaffRelationFollowUp> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userStaffRelationFollowUpMapper.list());
    }

    @Override
    public UserStaffRelationFollowUp getById(Long id) {
        return userStaffRelationFollowUpMapper.getById(id);
    }

    @Override
    public void save(UserStaffRelationFollowUp userStaffRelationFollowUp) {
        userStaffRelationFollowUpMapper.save(userStaffRelationFollowUp);
    }

    @Override
    public void update(UserStaffRelationFollowUp userStaffRelationFollowUp) {
        userStaffRelationFollowUpMapper.update(userStaffRelationFollowUp);
    }

    @Override
    public void deleteById(Long id) {
        userStaffRelationFollowUpMapper.deleteById(id);
    }
}
