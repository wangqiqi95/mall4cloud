package com.mall4j.cloud.user.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.AddGroupPushTaskStaffRelationBO;
import com.mall4j.cloud.user.bo.AddGroupPushTaskVipRelationBO;
import com.mall4j.cloud.user.dto.QuerySonTaskStaffPageDTO;
import com.mall4j.cloud.user.mapper.GroupPushTaskStaffRelationMapper;
import com.mall4j.cloud.user.model.GroupPushTaskStaffRelation;
import com.mall4j.cloud.user.vo.GroupPushTaskStaffRelationVO;
import com.mall4j.cloud.user.vo.SonTaskStaffPageVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class GroupPushTaskStaffRelationManager {


    @Autowired
    private GroupPushTaskStaffRelationMapper groupPushTaskStaffRelationMapper;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private MapperFacade mapperFacade;

    private static final Integer BATCH_SIZE = 500;


    public void addBatch(List<AddGroupPushTaskStaffRelationBO> relationBOList){
        List<List<AddGroupPushTaskStaffRelationBO>> partition = Lists.partition(relationBOList, BATCH_SIZE);

        partition.stream().forEach(item->{
            List<GroupPushTaskStaffRelation> relations=mapperFacade.mapAsList(item,GroupPushTaskStaffRelation.class);
            groupPushTaskStaffRelationMapper.insertBatch(relations);
        });
//        partition.stream().forEach(relationList ->
//            groupPushTaskStaffRelationMapper.insertBatch(relationList)
//        );
    }

    public void removeByPushTaskId(Long pushTaskId){
        groupPushTaskStaffRelationMapper.delete(
                new LambdaQueryWrapper<GroupPushTaskStaffRelation>()
                        .eq(GroupPushTaskStaffRelation::getGroupPushTaskId, pushTaskId)
        );
    }

    public PageVO<SonTaskStaffPageVO> getSonTaskStaffList(QuerySonTaskStaffPageDTO pageDTO){
        IPage<SonTaskStaffPageVO> staffPageVOIPage = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());

        staffPageVOIPage = groupPushTaskStaffRelationMapper.selectSonTaskStaffList(staffPageVOIPage, pageDTO);

        PageVO<SonTaskStaffPageVO> pageVO = new PageVO<>();

        pageVO.setList(staffPageVOIPage.getRecords());
        pageVO.setTotal(staffPageVOIPage.getTotal());
        pageVO.setPages((int)staffPageVOIPage.getPages());

        return pageVO;
    }


    public List<StaffVO> getByStaffNOOrNickName(String staff){
        ServerResponseEntity<List<StaffVO>> serverResponseEntity = staffFeignClient.getByStaffNOOrNickName(staff);

        if (serverResponseEntity.isSuccess() && CollectionUtil.isNotEmpty(serverResponseEntity.getData())){
            return serverResponseEntity.getData();
        }

        return null;
    }

    public GroupPushTaskStaffRelationVO getByStaffIdAndPushTaskId(Long staffId, Long pushTaskId){
        GroupPushTaskStaffRelation groupPushTaskStaffRelation = groupPushTaskStaffRelationMapper.selectOne(
                new LambdaQueryWrapper<GroupPushTaskStaffRelation>()
                        .eq(GroupPushTaskStaffRelation::getGroupPushTaskId, pushTaskId)
                        .eq(GroupPushTaskStaffRelation::getStaffId, staffId)
        );

        if (Objects.nonNull(groupPushTaskStaffRelation)){
            GroupPushTaskStaffRelationVO groupPushTaskStaffRelationVO = new GroupPushTaskStaffRelationVO();
            BeanUtils.copyProperties(groupPushTaskStaffRelation, groupPushTaskStaffRelationVO);
            return groupPushTaskStaffRelationVO;
        }

        return null;
    }


    public Integer getTheStaffCountByTaskId(Long taskId){
        Integer staffCount = groupPushTaskStaffRelationMapper.selectCount(
                new LambdaQueryWrapper<GroupPushTaskStaffRelation>()
                        .eq(GroupPushTaskStaffRelation::getGroupPushTaskId, taskId)
                        .ne(GroupPushTaskStaffRelation::getStaffId, 0)
        );

        return staffCount;
    }
}
