package com.mall4j.cloud.user.manager;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.platform.feign.SysUserClient;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.constant.ImportTagUserStatusEnum;
import com.mall4j.cloud.user.dto.QueryTagPageDTO;
import com.mall4j.cloud.user.mapper.TagMapper;
import com.mall4j.cloud.user.model.Tag;
import com.mall4j.cloud.api.user.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TagManager {


    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private SysUserClient sysUserClient;


    @Transactional(rollbackFor = Exception.class)
    public Long addTag(String tagName, Long userId, Integer enableState, Integer sort){

        Tag tag = new Tag();

        tag.setTagName(tagName);
        tag.setCreateUser(userId);
        tag.setEnableState(enableState);
        tag.setSort(sort);

        tagMapper.insert(tag);

        return tag.getTagId();
    }

    public IPage<TagVO> getTagPageByGroup(QueryTagPageDTO queryTagPageDTO){

        IPage<TagVO> tagVOPage = new Page<>(queryTagPageDTO.getPageNum().longValue(), queryTagPageDTO.getPageSize().longValue());

        tagVOPage = tagMapper.getTagPageByGroup(tagVOPage,queryTagPageDTO);

        return tagVOPage;
    }

    public List<TagVO> getTagListByGroup(Long groupId){

        return tagMapper.getTagListByGroup(groupId);

    }

    public TagVO tagById(Long tagId){


        Tag tag = tagMapper.selectById(tagId);

        TagVO tagVO = new TagVO();
        if (Objects.nonNull(tag)){
            BeanUtils.copyProperties(tag, tagVO);
        }else {
            return null;
        }

        return tagVO;
    }

    public List<SysUserVO> getCreateUserByIdList(List<Long> createUserIdList){
        ServerResponseEntity<List<SysUserVO>> serverResponseEntity = sysUserClient.getSysUserList(createUserIdList);

        if (serverResponseEntity.isSuccess() && CollectionUtil.isNotEmpty(serverResponseEntity.getData())){
            return serverResponseEntity.getData();
        }

        return null;
    }


    public void updateImportStatus(ImportTagUserStatusEnum importTagUserStatusEnum, Long tagId){

        Tag tag = tagMapper.selectById(tagId);
        tag.setImportStatus(importTagUserStatusEnum.getImportStatus());
        tag.setImportTime(LocalDateTime.now());
        tagMapper.updateById(tag);
    }

}
