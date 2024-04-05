package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.mapper.UserTagMapper;
import com.mall4j.cloud.user.model.UserTagUser;
import com.mall4j.cloud.user.mapper.UserTagUserMapper;
import com.mall4j.cloud.user.service.UserTagUserService;
import com.mall4j.cloud.user.vo.UserTagUserVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * 用户和标签关联表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Service
public class UserTagUserServiceImpl implements UserTagUserService {

    @Autowired
    private UserTagUserMapper userTagUserMapper;

    @Autowired
    private UserTagMapper userTagMapper;

    @Override
    public PageVO<UserTagUserVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userTagUserMapper.list());
    }

    @Override
    public UserTagUser getByUserTagUserId(Long userTagUserId) {
        return userTagUserMapper.getByUserTagUserId(userTagUserId);
    }

    @Override
    public void save(UserTagUser userTagUser) {
        userTagUserMapper.save(userTagUser);
    }

    @Override
    public void update(UserTagUser userTagUser) {
        userTagUserMapper.update(userTagUser);
    }

    @Override
    public void deleteById(Long userTagUserId) {
        userTagUserMapper.deleteById(userTagUserId);
    }

    @Override
    public List<UserTagApiVO> getUserTagsByUserId(Long userId) {
        return userTagUserMapper.getUserTagsByUserId(userId);
    }

    @Override
    public List<UserTagApiVO> listUserTagsByUserId(List<Long> staffTagIds, Long userId) {
        return userTagUserMapper.listUserTagsByUserId(staffTagIds, userId);
    }

    @Override
    public void removeByUserTagId(Long userTagId) {
        userTagUserMapper.removeByUserTagId(userTagId);
    }

    @Override
    public int saveBatch(List<UserTagUser> userTagUsers) {
        return userTagUserMapper.saveBatch(userTagUsers);
    }

    @Override
    public boolean batchUpdateUserTag(UserAdminDTO userAdminDTO) {
        List<Long> userIds = userAdminDTO.getUserIds();
        List<Long> tagList = userAdminDTO.getTagList();
        HashSet<Long> longs = new HashSet<>(tagList);
        tagList.clear();
        tagList.addAll(longs);
        if (CollectionUtils.isEmpty(tagList) || CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        // 用户已有的标签
        List<UserTagUserVO> list = userTagUserMapper.listByUserIds(userIds);
        Map<Long, List<UserTagUserVO>> map = new HashMap<>(16);
        if (CollUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.groupingBy(UserTagUserVO::getUserId));
        }
        boolean empty = map.isEmpty();
        // 将用户的标签全部查询处理
        List<UserTagUser> tagUsers = new ArrayList<>();
        for (Long userId : userIds) {
            List<Long> userTagIds = new ArrayList<>();
            List<UserTagUserVO> userTagUserVOList = map.get(userId);
            if (!empty && CollUtil.isNotEmpty(userTagUserVOList)) {
                userTagIds = userTagUserVOList.stream().map(UserTagUserVO::getUserTagId).collect(Collectors.toList());
            }
            for (Long tagId : tagList) {
                // 给用户打标签 tagList
                if (userTagIds.contains(tagId)) {
                    // 已经存在标签的就不用给用户打标签了
                    continue;
                }
                UserTagUser userTagUser = new UserTagUser();
                userTagUser.setUserTagId(tagId);
                userTagUser.setUserId(userId);
                tagUsers.add(userTagUser);
            }
        }
        if (CollectionUtils.isEmpty(tagUsers)) {
            return false;
        }
        // 给用户打上标签
        int saveRows = userTagUserMapper.saveBatch(tagUsers);
        // 统计某个标签有多少个人，然后更新标签人数
        Map<Long, Integer> tagUser = new HashMap<>(16);
        Map<Long, List<UserTagUser>> collect = tagUsers.stream().collect(Collectors.groupingBy(UserTagUser::getUserTagId));
        collect.forEach((tagId,lists)->{
            int size = lists.size();
            tagUser.put(tagId,size);
        });
        userTagMapper.updateBatchTagUserNum(tagUser);
        return saveRows > 0;
    }

    @Override
    public boolean removeByUserIdAndTagId(Long userId, Long userTagId) {
        boolean a = userTagUserMapper.removeByUserIdAndTagId(userId,userTagId) > 0;
        Map<Long, Integer> tagUser = new HashMap<>(16);
        tagUser.put(userTagId,-1);
        userTagMapper.updateBatchTagUserNum(tagUser);
        return a;
    }

    @Override
    public List<UserApiVO> getUserByTagIds(List<Long> userTagIds) {
        if (CollUtil.isEmpty(userTagIds)) {
            return Collections.emptyList();
        }
        List<UserApiVO> userApiVOList = userTagUserMapper.getUserByTagIds(userTagIds);
        return userApiVOList .stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o ->o.getUserId()))),ArrayList::new)
        );
    }

    @Override
    public List<UserApiVO> listUserByStaffAndTagId(Long staffId, Long userTagId) {
        return userTagUserMapper.listUserByStaffAndTagId(staffId, userTagId);
    }

    @Override
    public void deleteByTagId(Long tagId) {
        int deleteCount = userTagUserMapper.deleteByTagId(tagId);
        System.out.println(deleteCount);
    }

    @Override
    public void deleteByUserId(Long userId) {
        userTagUserMapper.deleteByUserId(userId);
    }

    @Override
    public List<UserTagUser> listByTag(Long tagId) {
        return userTagUserMapper.listByTag(tagId);
    }

    @Override
    public void deleteByUserAndTagId(List<Long> userIds, Long tagId) {
        userTagUserMapper.deleteByUserAndTagId(userIds, tagId);
    }

    @Override
    public void deleteByTagAndUserId(List<Long> tagIds, Long userId) {
        userTagUserMapper.deleteByTagAndUserId(tagIds, userId);
    }

    @Override
    public UserTagUser getByTagAndUser(Long tagId, Long userId) {
        return userTagUserMapper.getByTagAndUser(tagId, userId);
    }
}
