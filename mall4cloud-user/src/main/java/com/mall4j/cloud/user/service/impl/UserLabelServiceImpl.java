package com.mall4j.cloud.user.service.impl;

import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.user.dto.UserLabelDTO;
import com.mall4j.cloud.user.model.UserLabel;
import com.mall4j.cloud.user.mapper.UserLabelMapper;
import com.mall4j.cloud.user.model.UserLabelUser;
import com.mall4j.cloud.user.model.UserTagUser;
import com.mall4j.cloud.user.service.UserLabelService;
import com.mall4j.cloud.user.service.UserLabelUserService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserTagUserService;
import com.mall4j.cloud.user.vo.UserLabelVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导购标签信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
@Service
public class UserLabelServiceImpl implements UserLabelService {

    @Autowired
    private UserLabelMapper userLabelMapper;

    @Autowired
    private UserLabelUserService userLabelUserService;

    @Autowired
    private UserTagUserService userTagUserService;

    @Autowired
    private UserService userService;

    @Override
    public PageVO<UserLabelVO> page(PageDTO pageDTO, UserLabelDTO userLabelDTO) {
        return PageUtil.doPage(pageDTO, () -> userLabelMapper.list(userLabelDTO));
    }

    @Override
    public UserLabel getById(Long id) {
        return userLabelMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserLabel userLabel) {
        userLabelMapper.save(userLabel);
        if (userLabel.getLabelType() == 2){
            List<UserTagUser> userTagUsers = userTagUserService.listByTag(userLabel.getTagId());
            if (CollectionUtils.isEmpty(userTagUsers)){
                return;
            }
            UserQueryDTO queryDTO = new UserQueryDTO();
            queryDTO.setStaffId(userLabel.getStaffId());
            queryDTO.setUserIds(userTagUsers.stream().map(UserTagUser::getUserId).distinct().collect(Collectors.toList()));
            List<UserApiVO> users = userService.listUserByStaff(queryDTO);
            if (CollectionUtils.isEmpty(users)){
                return;
            }
            users.forEach(userApiVO -> {
                UserLabelUser labelUser = new UserLabelUser();
                labelUser.setLabelId(userLabel.getId());
                labelUser.setUserId(userApiVO.getUserId());
                userLabelUserService.save(labelUser);
            });
        }
    }

    @Override
    public void update(UserLabel userLabel) {
        userLabelMapper.update(userLabel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        userLabelMapper.deleteById(id);
        userLabelUserService.deleteByTag(id);
    }

    @Override
    public void addUser(UserLabelDTO userLabelDTO) {
        UserLabel label = userLabelMapper.getById(userLabelDTO.getId());
        if (null == label){
            throw new LuckException("标签不存在");
        }
        if (CollectionUtils.isEmpty(userLabelDTO.getUserIds())){
            userLabelUserService.deleteByTag(userLabelDTO.getId());
            return;
        }
        List<UserLabelUser> userLabelUsers = userLabelUserService.listByTag(userLabelDTO.getId());
        Map<Long, UserLabelUser> map = new HashMap<>(2);
        if (CollectionUtils.isNotEmpty(userLabelUsers)){
            map = userLabelUsers.stream().collect(Collectors.toMap(UserLabelUser::getUserId, u -> u));
            userLabelUserService.deleteByTagNotUserIn(userLabelDTO.getId(), userLabelDTO.getUserIds());
        }
        Map<Long, UserLabelUser> finalMap = map;
        userLabelDTO.getUserIds().forEach(id -> {
            if (null == finalMap.get(id)){
                UserLabelUser userLabelUser = new UserLabelUser();
                userLabelUser.setUserId(id);
                userLabelUser.setLabelId(userLabelDTO.getId());
                userLabelUserService.save(userLabelUser);
            }
        });
    }
}
