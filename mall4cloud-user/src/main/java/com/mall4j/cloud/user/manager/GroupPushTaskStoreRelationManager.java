package com.mall4j.cloud.user.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall4j.cloud.api.platform.dto.StoreSelectedParamFeignDTO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.AddPushTaskStoreRelationBO;
import com.mall4j.cloud.user.mapper.GroupPushTaskStoreRelationMapper;
import com.mall4j.cloud.user.model.GroupPushTask;
import com.mall4j.cloud.user.model.GroupPushTaskStoreRelation;
import com.mall4j.cloud.user.vo.GroupPushTaskStoreCountVO;
import com.mall4j.cloud.user.vo.GroupPushTaskStoreRelationVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupPushTaskStoreRelationManager {

    @Autowired
    private GroupPushTaskStoreRelationMapper groupPushTaskStoreRelationMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private StoreFeignClient storeFeignClient;


    @Transactional(rollbackFor = Exception.class)
    public void addBatch(List<AddPushTaskStoreRelationBO> relationBOList){
        groupPushTaskStoreRelationMapper.insertBatch(relationBOList);
    }

    public List<GroupPushTaskStoreCountVO> getTheCountByTaskIdList(List<Long> taskIdList){
        return groupPushTaskStoreRelationMapper.selectTheCountByTaskIdList(taskIdList);
    }

    public Integer getTheCountByTaskId(Long taskId){

        Integer storeCount = groupPushTaskStoreRelationMapper.selectCount(
                new LambdaQueryWrapper<GroupPushTaskStoreRelation>()
                        .eq(GroupPushTaskStoreRelation::getGroupPushTaskId, taskId)
        );

        return storeCount;
    }


    public List<GroupPushTaskStoreRelationVO> getTheStoreListByPushTaskId(Long pushTaskId){

        List<GroupPushTaskStoreRelation> groupPushTaskStoreRelationList = groupPushTaskStoreRelationMapper.selectList(
                new LambdaQueryWrapper<GroupPushTaskStoreRelation>()
                        .eq(GroupPushTaskStoreRelation::getGroupPushTaskId, pushTaskId)
        );

        List<GroupPushTaskStoreRelationVO> relationVOList = mapperFacade.mapAsList(groupPushTaskStoreRelationList, GroupPushTaskStoreRelationVO.class);
        return relationVOList;
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeListByPushTaskId(Long pushTaskId){
        groupPushTaskStoreRelationMapper.delete(
                new LambdaQueryWrapper<GroupPushTaskStoreRelation>()
                        .eq(GroupPushTaskStoreRelation::getGroupPushTaskId, pushTaskId)
        );
    }


    public PageVO<SelectedStoreVO> getStorePage(StoreSelectedParamFeignDTO storeSelectedParamDTO){
        ServerResponseEntity<PageVO<SelectedStoreVO>> pageVOServerResponseEntity = storeFeignClient.selectedPage(storeSelectedParamDTO);

        if (pageVOServerResponseEntity.isSuccess()){
            return pageVOServerResponseEntity.getData();
        }

        return null;
    }

    public List<Long> getStoreIdListByPushTaskId(Long pushTaskId){
        List<GroupPushTaskStoreRelation> groupPushTaskStoreRelations = groupPushTaskStoreRelationMapper.selectList(
                new LambdaQueryWrapper<GroupPushTaskStoreRelation>()
                        .eq(GroupPushTaskStoreRelation::getGroupPushTaskId, pushTaskId)
        );
        List<Long> storeIdList = groupPushTaskStoreRelations.stream()
                .map(GroupPushTaskStoreRelation::getStoreId)
                .collect(Collectors.toList());
        return storeIdList;
    }
}
