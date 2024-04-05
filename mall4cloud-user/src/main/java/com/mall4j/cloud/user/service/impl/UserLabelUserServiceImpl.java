package com.mall4j.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserLabelUserDTO;
import com.mall4j.cloud.user.model.UserLabelUser;
import com.mall4j.cloud.user.mapper.UserLabelUserMapper;
import com.mall4j.cloud.user.service.UserLabelUserService;
import com.mall4j.cloud.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 导购标签用户信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
@Service
public class UserLabelUserServiceImpl implements UserLabelUserService {

    @Autowired
    private UserLabelUserMapper userLabelUserMapper;

    @Autowired
    private UserService userService;

    @Override
    public PageVO<UserApiVO> page(PageDTO pageDTO, UserLabelUserDTO userLabelUserDTO) {
        QueryWrapper<UserLabelUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_id", userLabelUserDTO.getLabelId());
        List<UserLabelUser> userLabelUsers = userLabelUserMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userLabelUsers)){
            return new PageVO<>();
        }
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUserIds(userLabelUsers.stream().map(UserLabelUser::getUserId).collect(Collectors.toList()));
        return userService.pageUserByStaff(pageDTO, queryDTO);
    }

    @Override
    public UserLabelUser getById(Long id) {
        return userLabelUserMapper.getById(id);
    }

    @Override
    public void save(UserLabelUser userLabelUser) {
        userLabelUserMapper.save(userLabelUser);
    }

    @Override
    public void update(UserLabelUser userLabelUser) {
        userLabelUserMapper.update(userLabelUser);
    }

    @Override
    public void deleteById(Long id) {
        userLabelUserMapper.deleteById(id);
    }

    @Override
    public void deleteByTag(Long tagId) {
        QueryWrapper<UserLabelUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_id", tagId);
        userLabelUserMapper.delete(queryWrapper);
    }

    @Override
    public void deleteByTagNotUserIn(Long tagId, List<Long> userIds) {
        QueryWrapper<UserLabelUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_id", tagId);
        queryWrapper.notIn("user_id", userIds);
        userLabelUserMapper.delete(queryWrapper);
    }

    @Override
    public List<UserLabelUser> listByTag(Long tagId) {
        QueryWrapper<UserLabelUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_id", tagId);
        return userLabelUserMapper.selectList(queryWrapper);
    }
}
