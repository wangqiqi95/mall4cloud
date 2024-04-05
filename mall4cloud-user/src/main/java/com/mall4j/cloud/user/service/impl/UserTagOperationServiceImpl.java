package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.QueryUserTagOperationPageDTO;
import com.mall4j.cloud.user.manager.TagGroupManager;
import com.mall4j.cloud.user.manager.UserTagOperationManager;
import com.mall4j.cloud.user.model.UserTagOperation;
import com.mall4j.cloud.user.mapper.UserTagOperationMapper;
import com.mall4j.cloud.user.service.UserTagOperationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.user.vo.TagGroupVO;
import com.mall4j.cloud.user.vo.UserTagOperationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-09
 */
@Service
public class UserTagOperationServiceImpl extends ServiceImpl<UserTagOperationMapper, UserTagOperation> implements UserTagOperationService {


    @Autowired
    private UserTagOperationManager userTagOperationManager;

    @Autowired
    private TagGroupManager tagGroupManager;

    @Override
    public ServerResponseEntity<PageVO<UserTagOperationVO>> getOperationByBeVipCode(QueryUserTagOperationPageDTO queryUserTagOperationPageDTO) {

        PageVO<UserTagOperationVO> operationPage = userTagOperationManager.getOperationByBeVipCode(queryUserTagOperationPageDTO);

        List<Long> parenGroupIdList = operationPage.getList().stream()
                .map(UserTagOperationVO::getParentGroupId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(parenGroupIdList)){
            List<TagGroupVO> groupList = tagGroupManager.getTagGroupListByIdList(parenGroupIdList);

            if (CollectionUtil.isNotEmpty(groupList)){
                Map<Long, TagGroupVO> tagGroupVOMap = groupList.stream().collect(Collectors.toMap(TagGroupVO::getTagGroupId, parent -> parent));

                operationPage.getList().stream().forEach(operation -> {

                    TagGroupVO tagGroupVO;

                    if (Objects.nonNull(tagGroupVO = tagGroupVOMap.get(operation.getParentGroupId()))){
                        operation.setParentGroupName(tagGroupVO.getGroupName());
                    }
                });
            }
        }


        return ServerResponseEntity.success(operationPage);
    }
}
