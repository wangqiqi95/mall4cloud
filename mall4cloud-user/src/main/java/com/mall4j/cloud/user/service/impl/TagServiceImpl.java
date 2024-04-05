package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.AddTagDTO;
import com.mall4j.cloud.user.dto.EditTagDTO;
import com.mall4j.cloud.user.dto.QueryMarkingUserPageDTO;
import com.mall4j.cloud.user.dto.QueryTagPageDTO;
import com.mall4j.cloud.user.manager.*;
import com.mall4j.cloud.user.model.Tag;
import com.mall4j.cloud.user.mapper.TagMapper;
import com.mall4j.cloud.user.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.user.service.UserTagRelationService;
import com.mall4j.cloud.user.vo.MarkingUserPageVO;
import com.mall4j.cloud.user.vo.TagAndGroupRelationVO;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.user.vo.TagGroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private GroupTagRelationManager groupTagRelationManager;

    @Autowired
    private TagManager tagManager;

    @Autowired
    private UserTagRelationManager userTagRelationManager;

    @Autowired
    private UserTagOperationManager userTagOperationManager;

    @Autowired
    private TagGroupManager tagGroupManager;

    private static final Integer TAG_NAME_MAX_LENGTH = 25;

    private static final Integer pageSize = 1000;

    @Autowired
    private UserTagRelationService userTagRelationService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity<TagAndGroupRelationVO> addTag(AddTagDTO addTagDTO) {

        UserInfoInTokenBO user = AuthUserContext.get();

//        UserInfoInTokenBO user = new UserInfoInTokenBO();
//        user.setUserId(1L);

        if (addTagDTO.getTagName().length() > TAG_NAME_MAX_LENGTH){
            return ServerResponseEntity.showFailMsg("标签名长度最大为25个字符！");
        }

        //校验组内是否存在同名标签
        Integer checkCount = groupTagRelationManager.getTagCountByGroup(addTagDTO.getTagName(), addTagDTO.getGroupId());

        if (checkCount>0){
            return ServerResponseEntity.showFailMsg("组内已存在同名标签，无法创建！");
        }

        //新增tag
        Long tagId = tagManager.addTag(addTagDTO.getTagName(), user.getUserId(), addTagDTO.getEnableState(), addTagDTO.getSort());

        //绑定标签与组关系
        Long groupTagRelationId = groupTagRelationManager.addGroupTagRelation(addTagDTO.getGroupId(), tagId, user.getUserId());

        TagAndGroupRelationVO tagAndGroupRelationVO = new TagAndGroupRelationVO();

        tagAndGroupRelationVO.setGroupTagRelationId(groupTagRelationId);
        tagAndGroupRelationVO.setTagId(tagId);
        return ServerResponseEntity.success(tagAndGroupRelationVO);
    }

    @Override
    public ServerResponseEntity<PageVO<TagVO>> getTagPageVO(QueryTagPageDTO queryTagPageDTO) {

        IPage<TagVO> tagPage = tagManager.getTagPageByGroup(queryTagPageDTO);

        if (CollectionUtil.isNotEmpty(tagPage.getRecords())){
            //查询标签组相关权限
            TagGroupVO tagGroupVO = tagGroupManager.getTagGroupById(queryTagPageDTO.getGroupId());
            List<Integer> authFlagArray = tagGroupManager.authFlagArray(tagGroupVO.getAuthFlag());

            tagPage.getRecords().stream().forEach(tag -> {
                if (!queryTagPageDTO.isStaff()){
                    Integer useCount = userTagRelationManager.getTheTagVipCount(tag.getTagId());
                    tag.setMarkingVipCount(useCount);
                }
                tag.setAuthFlagArray(authFlagArray);
            });

            if (!queryTagPageDTO.isStaff()){
                List<Long> createUserIdList = tagPage.getRecords().stream()
                        .map(TagVO::getCreateUser)
                        .collect(Collectors.toList());

                if (CollectionUtil.isNotEmpty(createUserIdList)){
                    List<SysUserVO> createUserList = tagManager.getCreateUserByIdList(createUserIdList);
                    Map<Long, SysUserVO> sysUserVOMap = createUserList.stream()
                            .collect(Collectors.toMap(SysUserVO::getSysUserId, sysUser -> sysUser));
                    tagPage.getRecords().stream().forEach(tagVO -> {
                        SysUserVO sysUserVO;
                        if (Objects.nonNull(sysUserVO = sysUserVOMap.get(tagVO.getCreateUser()))) {
                            tagVO.setCreateUserNickName(sysUserVO.getNickName());
                        }
                    });
                }
            }
        }

        PageVO<TagVO> pageVO = new PageVO<>();

        pageVO.setPages((int) tagPage.getPages());
        pageVO.setList(tagPage.getRecords());
        pageVO.setTotal(tagPage.getTotal());

        return ServerResponseEntity.success(pageVO);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity editTag(EditTagDTO editTagDTO) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        Tag tag = getById(editTagDTO.getTagId());

        //如果修改了标签名称，需要校验标签名是否重复
        if (!tag.getTagName().equals(editTagDTO.getTagName())){
            Integer checkCount = groupTagRelationManager.getTagCountByGroup(editTagDTO.getTagName(), editTagDTO.getGroupId());

            if (checkCount>0){
                return ServerResponseEntity.showFailMsg("组内已存在同名标签，无法创建！");
            }
        }

        //更新标签基本信息
        tag.setTagName(editTagDTO.getTagName());
        tag.setEnableState(editTagDTO.getEnableState());
        tag.setSort(editTagDTO.getSort());
        updateById(tag);

//        //查询标签与标签组关联表ID
//        Long relationId = groupTagRelationManager.getRelationIdByGroupIdAndTagId(editTagDTO.getGroupId(), editTagDTO.getTagId());
//
//        //重新插入标签与成员关系
//        if (CollectionUtil.isNotEmpty(editTagDTO.getVipCodeList())){
//            userTagRelationManager.removeListByTagIdAndGroupId(editTagDTO.getTagId(), editTagDTO.getGroupId());
//            List<UserTagRelationBO> userTagRelationList = this.wrapperUserTagRelationBO(editTagDTO.getVipCodeList(), editTagDTO.getTagId(), editTagDTO.getGroupId(), tokenUser.getUserId(), relationId);
//            userTagRelationManager.addList(userTagRelationList);
//        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<TagVO> getTag(Long tagId) {

        Tag tag = lambdaQuery()
                .eq(Tag::getTagId, tagId)
                .one();

        TagVO tagVO = new TagVO();
        if (Objects.nonNull(tag)){
            BeanUtils.copyProperties(tag, tagVO);
            Integer markingCount = userTagRelationManager.getTheTagVipCount(tagId);
            tagVO.setMarkingVipCount(markingCount);
        }

        return ServerResponseEntity.success(tagVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity removeTag(Long tagId, Long groupId) {

//        UserInfoInTokenBO tokenUser = AuthUserContext.get();

//        UserInfoInTokenBO tokenUser = new UserInfoInTokenBO();
//        tokenUser.setUserId(1L);

        userTagRelationService.removeByTag(tagId);

        groupTagRelationManager.removeByTagIdAndGroupId(groupId, tagId);

        removeById(tagId);



        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity getTheTagListByVipCode(String vipCode) {
        return null;
    }

}
