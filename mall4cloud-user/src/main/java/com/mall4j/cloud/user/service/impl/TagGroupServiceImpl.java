package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.AddTagGroupDTO;
import com.mall4j.cloud.user.dto.EditParentTagGroupDTO;
import com.mall4j.cloud.user.dto.EditTagGroupDTO;
import com.mall4j.cloud.user.dto.QueryTagGroupGradeDTO;
import com.mall4j.cloud.user.manager.TagGroupManager;
import com.mall4j.cloud.user.manager.TagManager;
import com.mall4j.cloud.user.model.TagGroup;
import com.mall4j.cloud.user.mapper.TagGroupMapper;
import com.mall4j.cloud.user.service.TagGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.user.vo.TagGroupVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 * 标签组表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
@Service
public class TagGroupServiceImpl extends ServiceImpl<TagGroupMapper, TagGroup> implements TagGroupService {

    @Autowired
    private MapperFacade mapperFacade;

    private static final Integer GROUP_NAME_MAX_LENGTH = 25;

    @Autowired
    private TagGroupManager tagGroupManager;

    @Autowired
    private TagManager tagManager;

    /**
     * 新建标签组
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity addTagGroup(AddTagGroupDTO addTagGroupDTO) {

//        UserInfoInTokenBO user = AuthUserContext.get();
        UserInfoInTokenBO user = new UserInfoInTokenBO();
        user.setUserId(1L);
        TagGroup parent = new TagGroup();
        if (addTagGroupDTO.getFirstGrade().length() > GROUP_NAME_MAX_LENGTH){
            return ServerResponseEntity.showFailMsg("标签分类名最大为25个字符！");
        }

        if (addTagGroupDTO.getSecondGrade().length() > GROUP_NAME_MAX_LENGTH){
            return ServerResponseEntity.showFailMsg("标签组名最大为25个字符！");
        }

        //判断一级分类是否已选择
        if (Objects.isNull(addTagGroupDTO.getParentId())){
            //校验一级分类是否已存在
            parent = lambdaQuery()
                    .eq(TagGroup::getGroupName, addTagGroupDTO.getFirstGrade())
                    .isNull(TagGroup::getParentId)
                    .one();

            //如果一级分类不存在先新增一级分类
            if (Objects.isNull(parent)){
                parent = new TagGroup();
                parent.setGroupName(addTagGroupDTO.getFirstGrade());
                parent.setGroupType(addTagGroupDTO.getGroupType());
                parent.setCreateUser(user.getUserId());
                baseMapper.insert(parent);
            }
        }else { //如果已经选择一级分类，直接使用当前选择的一级分类ID
            parent.setTagGroupId(addTagGroupDTO.getParentId());
        }

        TagGroup secondGrade = lambdaQuery()
//                .eq(TagGroup::getParentId, parent.getParentId())
                .eq(TagGroup::getGroupName, addTagGroupDTO.getSecondGrade())
                .one();

        if (Objects.nonNull(secondGrade)){
            return ServerResponseEntity.showFailMsg("已有同名标签组，无法创建！");
        }

        secondGrade = new TagGroup();
        secondGrade.setParentId(parent.getTagGroupId());
        secondGrade.setGroupType(addTagGroupDTO.getGroupType());
        secondGrade.setCreateUser(user.getUserId());
        secondGrade.setAuthFlag(this.authFlagStr(addTagGroupDTO.getAuthFlagArrays()));
        secondGrade.setEnableState(addTagGroupDTO.getEnableState());
        secondGrade.setSingleState(addTagGroupDTO.getSingleState());
        secondGrade.setGroupName(addTagGroupDTO.getSecondGrade());
        baseMapper.insert(secondGrade);

        return ServerResponseEntity.success();
    }


    /**
     * 修改标签组
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity updateTagGroup(EditTagGroupDTO editTagGroupDTO) {

        TagGroup tagGroup = lambdaQuery()
                .eq(TagGroup::getTagGroupId, editTagGroupDTO.getTagGroupId())
                .one();

        TagGroup check = lambdaQuery()
                .eq(TagGroup::getGroupName, editTagGroupDTO.getGroupName())
                .one();

        if (Objects.nonNull(check)){
            if (!check.getTagGroupId().equals(tagGroup.getTagGroupId())){
                return ServerResponseEntity.showFailMsg("标签组名称已重复，无法修改！");
            }
        }

        if (Objects.isNull(tagGroup)){
            return ServerResponseEntity.showFailMsg("标签组不存在！");
        }



        tagGroup.setGroupName(editTagGroupDTO.getGroupName());
        tagGroup.setEnableState(editTagGroupDTO.getEnableState());
        tagGroup.setAuthFlag(this.authFlagStr(editTagGroupDTO.getAuthFlagArrays()));
        tagGroup.setUpdateTime(LocalDateTime.now());

        baseMapper.updateById(tagGroup);
        return ServerResponseEntity.success();
    }

    /**
     * 查询一级分类
     * */
    @Override
    public ServerResponseEntity<List<TagGroupVO>> getFirstGrade(QueryTagGroupGradeDTO queryTagGroupGradeDTO) {

        LambdaQueryChainWrapper<TagGroup> lambdaQuery = lambdaQuery();
        if (Objects.nonNull(queryTagGroupGradeDTO.getGroupType())){
            lambdaQuery.eq(TagGroup::getGroupType, queryTagGroupGradeDTO.getGroupType());
        }
        List<TagGroup> firstGradeList = lambdaQuery
                .isNull(TagGroup::getParentId)
                .orderByAsc(TagGroup::getCreateTime)
                .list();
        List<TagGroupVO> tagGroupVOS = mapperFacade.mapAsList(firstGradeList, TagGroupVO.class);

        return ServerResponseEntity.success(tagGroupVOS);
    }

    @Override
    public ServerResponseEntity<List<TagGroupVO>> getSecondGrade(QueryTagGroupGradeDTO queryTagGroupGradeDTO) {

        LambdaQueryChainWrapper<TagGroup> lambdaQuery = lambdaQuery();

        if (CollectionUtil.isNotEmpty(queryTagGroupGradeDTO.getAuthFlagArray())){
            String authFlagStr = this.authFlagStr(queryTagGroupGradeDTO.getAuthFlagArray());
            lambdaQuery.like(TagGroup::getAuthFlag, authFlagStr);
        }

        if (StringUtils.isNotEmpty(queryTagGroupGradeDTO.getGroupName())){
            lambdaQuery.like(TagGroup::getGroupName, queryTagGroupGradeDTO.getGroupName());
        }

        if (Objects.nonNull(queryTagGroupGradeDTO.getParentId())){
            lambdaQuery.eq(TagGroup::getParentId, queryTagGroupGradeDTO.getParentId());
        }

        if (Objects.nonNull(queryTagGroupGradeDTO.getEnableState())){
             lambdaQuery.eq(TagGroup::getEnableState, queryTagGroupGradeDTO.getEnableState());
        }

        List<TagGroup> tagGroupList = lambdaQuery
                .isNotNull(TagGroup::getParentId)
                .orderByDesc(TagGroup::getEnableState) //按启用状态排序
                .orderByDesc(TagGroup::getCreateTime) //再按创建时间排序
                .list();

        if (CollectionUtil.isNotEmpty(tagGroupList)){
            List<Long> groupCreateUserIdList = tagGroupList.stream()
                    .map(TagGroup::getCreateUser)
                    .collect(Collectors.toList());

            List<SysUserVO> createUserList = tagManager.getCreateUserByIdList(groupCreateUserIdList);

            Map<Long, SysUserVO> sysUserVOMap = createUserList.stream()
                    .collect(Collectors.toMap(SysUserVO::getSysUserId, x -> x));

            List<TagGroupVO> tagGroupVOS = mapperFacade.mapAsList(tagGroupList, TagGroupVO.class);

            tagGroupVOS.stream().forEach(tagGroupVO ->{
                tagGroupVO.setAuthFlagArrays(tagGroupManager.authFlagArray(tagGroupVO.getAuthFlag()));
                SysUserVO sysUserVO;
                if (MapUtil.isNotEmpty(sysUserVOMap) && Objects.nonNull(sysUserVO = sysUserVOMap.get(tagGroupVO.getCreateUser()))){
                    tagGroupVO.setCreateUserNickName(sysUserVO.getNickName());
                }
            });
            return ServerResponseEntity.success(tagGroupVOS);
        }

        return ServerResponseEntity.success(new ArrayList<>());
    }

    @Override
    public ServerResponseEntity updateParentTagGroup(EditParentTagGroupDTO editTagGroupDTO) {

        lambdaUpdate()
                .set(TagGroup::getGroupName, editTagGroupDTO.getGroupName())
                .eq(TagGroup::getTagGroupId, editTagGroupDTO.getTagGroupId())
                .update();

        return ServerResponseEntity.success();
    }


    /**
     * 拼接权限字符串（数据库中保存一串字符创作为权限标志）
     * */
    private String authFlagStr(List<Integer> authFlags){

        String authFlagStr = "";

        if (CollectionUtil.isNotEmpty(authFlags)){

            authFlags = authFlags.stream().sorted().collect(Collectors.toList());

            for (int i = 0; i <= authFlags.size()-1; i++){
                if (i < authFlags.size()-1){
                    authFlagStr = authFlagStr+ authFlags.get(i)+",";
                }else {
                    authFlagStr = authFlagStr+authFlags.get(i);
                }
            }
        }

        return authFlagStr;
    }


}
