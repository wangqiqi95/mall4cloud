package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.dto.StoreSelectedParamFeignDTO;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.QueryStorePageByPushTaskPageDTO;
import com.mall4j.cloud.user.manager.GroupPushTaskStoreRelationManager;
import com.mall4j.cloud.user.manager.UserTagRelationManager;
import com.mall4j.cloud.user.mapper.GroupPushTaskStoreRelationMapper;
import com.mall4j.cloud.user.model.GroupPushTaskStoreRelation;
import com.mall4j.cloud.user.service.GroupPushTaskStoreRelationService;
import com.mall4j.cloud.user.vo.GroupPushTaskStoreRelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 群发任务店铺关联表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Service
public class GroupPushTaskStoreRelationServiceImpl extends ServiceImpl<GroupPushTaskStoreRelationMapper, GroupPushTaskStoreRelation> implements GroupPushTaskStoreRelationService {


    @Autowired
    private GroupPushTaskStoreRelationManager groupPushTaskStoreRelationManager;


    @Override
    public ServerResponseEntity<PageVO<SelectedStoreVO>> getTheStorePageByPushTaskId(QueryStorePageByPushTaskPageDTO pageDTO) {

        PageVO<SelectedStoreVO> storePage = new PageVO<>();

        List<GroupPushTaskStoreRelation> groupPushTaskStoreRelationList = lambdaQuery()
                .eq(GroupPushTaskStoreRelation::getGroupPushTaskId, pageDTO.getPushTaskId())
                .list();

        List<Long> storeIdList = groupPushTaskStoreRelationList.stream()
                .map(GroupPushTaskStoreRelation::getStoreId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(storeIdList)){
            StoreSelectedParamFeignDTO storeSelectedParamFeignDTO = new StoreSelectedParamFeignDTO();

            storeSelectedParamFeignDTO.setStoreIdList(storeIdList);
            storeSelectedParamFeignDTO.setKeyword(pageDTO.getStoreKeyword());

            storeSelectedParamFeignDTO.setPageSize(pageDTO.getPageSize());
            storeSelectedParamFeignDTO.setPageNum(pageDTO.getPageNum());

            storePage = groupPushTaskStoreRelationManager.getStorePage(storeSelectedParamFeignDTO);
        }



        return ServerResponseEntity.success(storePage);
    }

    @Override
    public ServerResponseEntity<List<Long>> getTheStoreIdListByTask(Long pushTaskId) {

        List<GroupPushTaskStoreRelationVO> storeList = groupPushTaskStoreRelationManager.getTheStoreListByPushTaskId(pushTaskId);

        List<Long> storeIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(storeList)){
            storeIdList = storeList.stream().map(GroupPushTaskStoreRelationVO::getStoreId).collect(Collectors.toList());
        }

        return ServerResponseEntity.success(storeIdList);
    }
}
