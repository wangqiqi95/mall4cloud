package com.mall4j.cloud.user.manager;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.user.bo.UserGroupTagRelationBO;
import com.mall4j.cloud.user.bo.UserTagOperationBO;
import com.mall4j.cloud.user.constant.TagOperationEnum;
import com.mall4j.cloud.user.dto.QueryUserTagOperationPageDTO;
import com.mall4j.cloud.user.mapper.UserTagOperationMapper;
import com.mall4j.cloud.user.model.UserTagOperation;
import com.mall4j.cloud.user.vo.UserTagOperationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTagOperationManager {

    @Autowired
    private UserTagOperationMapper userTagOperationMapper;

    @Transactional(rollbackFor = Exception.class)
    public void add(UserTagOperationBO userTagOperationBO){

        UserTagOperation userTagOperation = new UserTagOperation();

        BeanUtils.copyProperties(userTagOperationBO, userTagOperation);

        userTagOperationMapper.insert(userTagOperation);

    }

    public void addRemoveBatch(Long tagId, Long createUser, Long groupId, List<String> codeList){

        this.addBatch(tagId,createUser,groupId,TagOperationEnum.REMOVE, codeList);

    }

    public void addSaveBatch(Long tagId, Long createUser, Long groupId, List<String> codeList){
        this.addBatch(tagId,createUser,groupId,TagOperationEnum.ADD, codeList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBatch(Long tagId, Long createUser, Long groupId, TagOperationEnum operationState, List<String> codeList){
        userTagOperationMapper.insertBatchToImport(tagId,createUser,groupId,operationState.getOperationState(),codeList);

    }

    // 对单个会员进行批量取消标签操作
    public void addRemoveBatchByTagIds(List<UserGroupTagRelationBO> boList, Long createUser, String vipCode){
        this.addBatchByTagIds(boList, createUser, TagOperationEnum.REMOVE, vipCode);
    }

    // 对单个会员进行批量打标操作
    public void addSaveBatchByTagIds(List<UserGroupTagRelationBO> boList, Long createUser, String vipCode){
        this.addBatchByTagIds(boList, createUser, TagOperationEnum.ADD, vipCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBatchByTagIds(List<UserGroupTagRelationBO> boList, Long createUser,  TagOperationEnum operationState, String vipCode){

        List<UserTagOperationBO> userTagOperationBOList = boList.stream().map(bo -> {
            UserTagOperationBO userTagOperationBO = new UserTagOperationBO();
            userTagOperationBO.setTagId(bo.getTagId());
            userTagOperationBO.setDoUser(createUser);
            userTagOperationBO.setGroupId(bo.getGroupId());
            userTagOperationBO.setBeVipCode(vipCode);
            userTagOperationBO.setOperationState(operationState.getOperationState());
            return userTagOperationBO;
        }).collect(Collectors.toList());

        userTagOperationMapper.insertBatch(userTagOperationBOList);

    }

    public PageVO<UserTagOperationVO> getOperationByBeVipCode(QueryUserTagOperationPageDTO operationPageDTO){
        IPage<UserTagOperationVO> page = new Page<>(operationPageDTO.getPageNum(), operationPageDTO.getPageSize());
        page = userTagOperationMapper.selectOperationByBeVipCode(page, operationPageDTO);

        PageVO<UserTagOperationVO> pageVO = new PageVO();

        pageVO.setList(page.getRecords());
        pageVO.setPages((int) page.getPages());
        pageVO.setTotal(page.getTotal());
        return pageVO;
    }

}
