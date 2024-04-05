package com.mall4j.cloud.user.service.impl;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.user.dto.UserTagStaffDTO;
import com.mall4j.cloud.user.model.UserTag;
import com.mall4j.cloud.user.model.UserTagStaff;
import com.mall4j.cloud.user.mapper.UserTagStaffMapper;
import com.mall4j.cloud.user.model.UserTagUser;
import com.mall4j.cloud.user.service.*;
import com.mall4j.cloud.user.vo.StaffTagVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 导购标签关系信息
 *
 * @author ZengFanChang
 * @date 2022-02-13 22:25:55
 */
@Service
public class UserTagStaffServiceImpl implements UserTagStaffService {

    @Autowired
    private UserTagStaffMapper userTagStaffMapper;

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserTagUserService userTagUserService;

    @Autowired
    private UserStaffCpRelationService  userStaffCpRelationService;


    @Override
    public PageVO<UserTagStaff> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userTagStaffMapper.list());
    }

    @Override
    public UserTagStaff getById(Long id) {
        return userTagStaffMapper.getById(id);
    }

    @Override
    public void save(UserTagStaff userTagStaff) {
        userTagStaffMapper.save(userTagStaff);
    }

    @Override
    public void update(UserTagStaff userTagStaff) {
        userTagStaffMapper.update(userTagStaff);
    }

    @Override
    public void deleteById(Long id) {
        userTagStaffMapper.deleteById(id);
    }

    @Override
    public List<UserTagStaff> listByStaff(Long staffId) {
        return userTagStaffMapper.listByStaff(staffId);
    }

    @Override
    public List<StaffTagVo> listSysTagByStaff(Long staffId) {
        List<UserTagApiVO> userTagApiVOS = userTagService.listUserTagByType(2);
        if (CollectionUtils.isEmpty(userTagApiVOS)){
            return new ArrayList<>();
        }
        List<StaffTagVo> staffTagVos = new ArrayList<>();
        userTagApiVOS.forEach(userTagApiVO -> {
            StaffTagVo vo = new StaffTagVo();
            vo.setUserTagId(userTagApiVO.getTagId());
            vo.setTagName(userTagApiVO.getTagName());
            vo.setTagType(2);
            vo.setAlreadyAdd(userTagStaffMapper.getStaffTagByStaffAndTag(staffId, userTagApiVO.getTagId()) != null);
            staffTagVos.add(vo);
        });
        return staffTagVos;
    }

    @Override
    public UserTagStaff getStaffTagByStaffAndTag(Long staffId, Long tagId) {
        return userTagStaffMapper.getStaffTagByStaffAndTag(staffId, tagId);
    }

    @Override
    public List<StaffTagVo> listStaffTag(UserTagStaffDTO dto) {
        List<UserTagStaff> userTagStaffs = listByStaff(dto.getStaffId());
        if (CollectionUtils.isEmpty(userTagStaffs)){
            return new ArrayList<>();
        }
        List<UserTagApiVO> userTagList = userTagService.getStaffUserTagList(dto.getTagName(), userTagStaffs.stream().map(UserTagStaff::getTagId).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(userTagList)){
            return new ArrayList<>();
        }
        List<StaffTagVo> staffTagVos = new ArrayList<>();
        userTagList.forEach(userTagApiVO -> {
            StaffTagVo staffTagVo = new StaffTagVo();
            staffTagVo.setUserTagId(userTagApiVO.getTagId());
            staffTagVo.setTagName(userTagApiVO.getTagName());
            staffTagVo.setTagType(userTagApiVO.getTagType());
            staffTagVo.setUserNum(Optional.ofNullable(userTagUserService.listByTag(userTagApiVO.getTagId())).orElse(new ArrayList<>()).size());
            staffTagVos.add(staffTagVo);
        });
        return staffTagVos;
    }

    @Override
    public List<UserApiVO> getUserByTag(UserTagStaffDTO dto) {
        List<UserApiVO> userApiVOS = userTagUserService.listUserByStaffAndTagId(dto.getStaffId(), dto.getUserTagId());
        if (CollectionUtils.isNotEmpty(userApiVOS)){
            userApiVOS.forEach(userApiVO -> {
                userApiVO.setAddWechat(userStaffCpRelationService.getByStaffAndUser(dto.getStaffId(), userApiVO.getUserId()) == null ? 0 : 1);
            });
        }
        return userApiVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStaffTag(UserTagStaffDTO dto) {
        UserTagStaff userTagStaff = getStaffTagByStaffAndTag(dto.getStaffId(), dto.getUserTagId());
        if (null != userTagStaff){
            throw new LuckException("该分组标签已添加");
        }
        UserTagStaff newUserTagStaff = new UserTagStaff();
        if (dto.getTagType() == 2){
            UserTag userTag = userTagService.getByUserTagId(dto.getUserTagId());
            if (null == userTag){
                throw new LuckException("系统标签不存在");
            }
            newUserTagStaff.setTagId(userTag.getUserTagId());
        } else if (dto.getTagType() == 3){
            UserTag userTag = new UserTag();
            userTag.setTagName(dto.getTagName());
            userTag.setTagType(3);
            userTag.setIsSysTurnOn(0);
            userTag.setUserNum(0L);
            userTagService.save(userTag);
            newUserTagStaff.setTagId(userTag.getUserTagId());
        } else {
            throw new LuckException("标签类型有误");
        }
        newUserTagStaff.setStaffId(dto.getStaffId());
        userTagStaffMapper.save(newUserTagStaff);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStaffTag(UserTagStaffDTO dto) {
        UserTagStaff userTagStaff = getStaffTagByStaffAndTag(dto.getStaffId(), dto.getUserTagId());
        if (null == userTagStaff){
            throw new LuckException("该分组标签不存在");
        }
        if (dto.getTagType() == 2){
            userTagStaffMapper.deleteById(userTagStaff.getId());
        } else if (dto.getTagType() == 3){
            userTagService.deleteById(userTagStaff.getTagId());
            userTagUserService.deleteByTagId(userTagStaff.getTagId());
            userTagStaffMapper.deleteById(userTagStaff.getId());
        } else {
            throw new LuckException("标签类型有误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStaffTagUser(UserTagStaffDTO dto) {
        List<UserApiVO> userApiVOS = userTagUserService.listUserByStaffAndTagId(dto.getStaffId(), dto.getUserTagId());
        if (CollectionUtils.isEmpty(dto.getUserIds())){
            if (CollectionUtils.isNotEmpty(userApiVOS)){
                userTagUserService.deleteByUserAndTagId(userApiVOS.stream().map(UserApiVO::getUserId).distinct().collect(Collectors.toList()), dto.getUserTagId());
            }
        } else {
            if (CollectionUtils.isNotEmpty(userApiVOS)){
                List<Long> removeUserIds = new ArrayList<>();
                List<Long> addUserIds = new ArrayList<>();
                userApiVOS.forEach(userApiVO -> {
                    if (!dto.getUserIds().contains(userApiVO.getUserId())){
                        removeUserIds.add(userApiVO.getUserId());
                    }
                });
                dto.getUserIds().forEach(userId -> {
                    if (!userApiVOS.stream().map(UserApiVO::getUserId).distinct().collect(Collectors.toList()).contains(userId)){
                        addUserIds.add(userId);
                    }
                });
                if (CollectionUtils.isNotEmpty(removeUserIds)){
                    userTagUserService.deleteByUserAndTagId(removeUserIds, dto.getUserTagId());
                }
                if (CollectionUtils.isNotEmpty(addUserIds)){
                    List<UserTagUser> userTagUsers = new ArrayList<>();
                    addUserIds.forEach(userId -> {
                        UserTagUser userTagUser = new UserTagUser();
                        userTagUser.setUserId(userId);
                        userTagUser.setUserTagId(dto.getUserTagId());
                        userTagUsers.add(userTagUser);
                    });
                    userTagUserService.saveBatch(userTagUsers);
                }
            } else {
                List<UserTagUser> userTagUsers = new ArrayList<>();
                dto.getUserIds().forEach(userId -> {
                    UserTagUser userTagUser = new UserTagUser();
                    userTagUser.setUserId(userId);
                    userTagUser.setUserTagId(dto.getUserTagId());
                    userTagUsers.add(userTagUser);
                });
                userTagUserService.saveBatch(userTagUsers);
            }
        }
    }

    @Override
    public List<UserTagApiVO> listStaffUserTag(UserTagStaffDTO dto) {
        return userTagUserService.listUserTagsByUserId(Optional.ofNullable(userTagStaffMapper.listByStaff(dto.getStaffId())).orElse(new ArrayList<>()).stream().map(UserTagStaff::getTagId).collect(Collectors.toList()), dto.getUserId());
    }

    @Override
    public List<StaffTagVo> listStaffTagByUser(UserTagStaffDTO dto) {
        if (dto.getTagType() == 2){
            List<UserTagApiVO> userTagApiVOS = userTagService.listUserTagByType(2);
            if (CollectionUtils.isEmpty(userTagApiVOS)){
                return new ArrayList<>();
            }
            List<StaffTagVo> staffTagVos = new ArrayList<>();
            userTagApiVOS.forEach(userTagApiVO -> {
                StaffTagVo vo = new StaffTagVo();
                vo.setUserTagId(userTagApiVO.getTagId());
                vo.setTagName(userTagApiVO.getTagName());
                vo.setTagType(2);
                vo.setAlreadyAdd(userTagUserService.getByTagAndUser(userTagApiVO.getTagId(), dto.getUserId()) != null);
                staffTagVos.add(vo);
            });
            return staffTagVos;
        }
        if (dto.getTagType() == 3){
            List<UserTagStaff> userTagStaffs = userTagStaffMapper.listByStaff(dto.getStaffId());
            if (CollectionUtils.isEmpty(userTagStaffs)){
                return new ArrayList<>();
            }
            List<UserTagApiVO> userTagList = userTagService.getUserTagList(userTagStaffs.stream().map(UserTagStaff::getTagId).collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(userTagList)){
                return new ArrayList<>();
            }
            List<UserTagApiVO> collect = userTagList.stream().filter(userTagApiVO -> userTagApiVO.getTagType() == 3).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect)){
                return new ArrayList<>();
            }
            List<StaffTagVo> staffTagVos = new ArrayList<>();
            collect.forEach(userTagApiVO -> {
                StaffTagVo vo = new StaffTagVo();
                vo.setUserTagId(userTagApiVO.getTagId());
                vo.setTagName(userTagApiVO.getTagName());
                vo.setTagType(3);
                vo.setAlreadyAdd(userTagUserService.getByTagAndUser(userTagApiVO.getTagId(), dto.getUserId()) != null);
                staffTagVos.add(vo);
            });
            return staffTagVos;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserTag(UserTagStaffDTO dto) {
        if (CollectionUtils.isEmpty(dto.getUserTagIds())){
            userTagUserService.deleteByUserId(dto.getUserId());
        } else {
            List<UserTagApiVO> userTagApiVOS = userTagUserService.getUserTagsByUserId(dto.getUserId());
            if (CollectionUtils.isNotEmpty(userTagApiVOS)){
                List<Long> removeTagIds = new ArrayList<>();
                List<Long> addUserIds = new ArrayList<>();
                dto.getUserTagIds().forEach(userTagId -> {
                     if (!userTagApiVOS.stream().map(UserTagApiVO::getTagId).distinct().collect(Collectors.toList()).contains(userTagId)){
                         addUserIds.add(userTagId);
                     }
                });
                userTagApiVOS.forEach(userTagApiVO -> {
                    if (!dto.getUserTagIds().contains(userTagApiVO.getTagId())){
                        removeTagIds.add(userTagApiVO.getTagId());
                    }
                });
                if (CollectionUtils.isNotEmpty(removeTagIds)){
                    userTagUserService.deleteByTagAndUserId(removeTagIds, dto.getUserId());
                }
                if (CollectionUtils.isNotEmpty(addUserIds)){
                    List<UserTagUser> userTagUsers = new ArrayList<>();
                    addUserIds.forEach(userTagId -> {
                        UserTagUser userTagUser = new UserTagUser();
                        userTagUser.setUserId(dto.getUserId());
                        userTagUser.setUserTagId(userTagId);
                        userTagUsers.add(userTagUser);
                    });
                    userTagUserService.saveBatch(userTagUsers);
                }
            } else {
                List<UserTagUser> userTagUsers = new ArrayList<>();
                dto.getUserTagIds().forEach(userTagId -> {
                    UserTagUser userTagUser = new UserTagUser();
                    userTagUser.setUserId(dto.getUserId());
                    userTagUser.setUserTagId(userTagId);
                    userTagUsers.add(userTagUser);
                });
                userTagUserService.saveBatch(userTagUsers);
            }
        }
    }
}
