package com.mall4j.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.user.dto.UserGroupSelectDTO;
import com.mall4j.cloud.user.mapper.UserGroupMapper;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.model.UserGroup;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.UserGroupService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户分组阶段配置
 *
 * @author gmq
 * @date 2023-11-10 11:01:57
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper,UserGroup> implements UserGroupService {

    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private UserStaffCpRelationMapper userStaffCpRelationMapper;

    @Override
    public PageVO<UserGroup> page(PageDTO pageDTO, UserGroupSelectDTO dto) {
        return PageUtil.doPage(pageDTO, () -> userGroupMapper.list(dto));
    }

    @Override
    public List<UserGroup> getGroupList(String groupName) {
        UserGroupSelectDTO dto=new UserGroupSelectDTO();
        dto.setGroupName(groupName);
        dto.setType(0);
        return userGroupMapper.list(dto);
    }

    @Override
    public UserGroup getById(Long id) {
        return userGroupMapper.getById(id);
    }

    @Override
    public void saveUserGroup(List<UserGroup> userGroups) {
        UserGroupSelectDTO dto=new UserGroupSelectDTO();
        dto.setParentId(userGroups.get(0).getParentId());
        List<UserGroup> groups=userGroupMapper.list(dto);
        Map<Integer,UserGroup> userGroupMap= LambdaUtils.toMap(groups,UserGroup::getWeight);
        //校验阶段重复
        for (UserGroup group : userGroups) {
            if(userGroupMap.containsKey(group.getWeight())){
                throw new LuckException("阶段名称【"+group.getGroupName()+"】包含阶段重复：【"+group.getWeight()+"】");
            }
            group.setIsDelete(0);
            group.setCreateBy(AuthUserContext.get().getUsername());
            group.setCreateTime(new Date());
        }
        this.saveBatch(userGroups);
    }

    @Override
    public void updateUserGroup(UserGroup userGroup) {
        if(Objects.isNull(this.getById(userGroup.getId()))){
            throw new LuckException("操作失败，未获取到信息");
        }
        userGroup.setUpdateBy(AuthUserContext.get().getUsername());
        userGroup.setUpdateTime(new Date());
        this.updateById(userGroup);
    }

    @Override
    public void deleteById(Long id) {
        //校验是否有下级分类
        LambdaQueryWrapper<UserGroup> userGroupLambdaQueryWrapper=new LambdaQueryWrapper<>();
        userGroupLambdaQueryWrapper.eq(UserGroup::getParentId,id);
        userGroupLambdaQueryWrapper.eq(UserGroup::getIsDelete,0);
        if(this.count(userGroupLambdaQueryWrapper)>0){
            throw new LuckException("操作失败，包含下级分组无法删除");
        }
        //校验是否使用中
        LambdaQueryWrapper<UserStaffCpRelation> relationLambdaQueryWrapper=new LambdaQueryWrapper();
        relationLambdaQueryWrapper.eq(UserStaffCpRelation::getStatus,1);
        relationLambdaQueryWrapper.eq(UserStaffCpRelation::getStageId,id);
        if(userStaffCpRelationMapper.selectCount(relationLambdaQueryWrapper)>0){
            throw new LuckException("操作失败，分组使用中无法删除");
        }
        this.update(new LambdaUpdateWrapper<UserGroup>()
                .eq(UserGroup::getId,id)
                .set(UserGroup::getIsDelete,1)
                .set(UserGroup::getUpdateBy, AuthUserContext.get().getUsername())
                .set(UserGroup::getUpdateTime, new Date()));
    }
}
