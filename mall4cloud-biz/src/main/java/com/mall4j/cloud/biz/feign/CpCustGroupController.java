package com.mall4j.cloud.biz.feign;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.api.biz.dto.cp.CpCustGroupCountDTO;
import com.mall4j.cloud.api.biz.feign.CpCustGroupClient;
import com.mall4j.cloud.api.biz.vo.CustGroupStaffCountVO;
import com.mall4j.cloud.api.biz.vo.CustGroupVO;
import com.mall4j.cloud.api.biz.vo.TaskSonGroupVO;
import com.mall4j.cloud.api.biz.vo.UserJoinCustGroupVO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.mapper.cp.GroupCustInfoMapper;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CpCustGroupController implements CpCustGroupClient {

    @Autowired
    private CustGroupService custGroupService;
    @Autowired
    private GroupCustInfoMapper groupCustInfoMapper;
    @Autowired
    StaffFeignClient staffFeignClient;

    @Override
    public ServerResponseEntity<List<CustGroupVO>> getGroupByStaffIdList(List<Long> staffIdList) {
        List<CpCustGroup> list = custGroupService
            .list(new LambdaUpdateWrapper<CpCustGroup>()
                    .eq(CpCustGroup::getFlag,0)
                    .eq(CpCustGroup::getStatus,1)
                    .in(CpCustGroup::getAdminStatus, Arrays.asList(0,3))//0-跟进人正常/3-离职继承完成
                    .in(CpCustGroup::getOwnerId, staffIdList));
        if (CollectionUtils.isEmpty(list)) {
            return ServerResponseEntity.success(new ArrayList<>());
        } else {
            List<CustGroupVO> result = new ArrayList<>();
            for (CpCustGroup cpCustGroup : list) {
                CustGroupVO v = new CustGroupVO();
                BeanUtils.copyProperties(cpCustGroup,v);
                result.add(v);
            }
            return ServerResponseEntity.success(result);
        }
    }

    @Override
    public ServerResponseEntity<List<TaskSonGroupVO>> findCustGroupByIds(List<String> id) {
        List<CpCustGroup> groupList = custGroupService.list(new LambdaUpdateWrapper<CpCustGroup>().in(CpCustGroup::getId, id));
        List<TaskSonGroupVO> taskSonGroupVOS = new ArrayList<>();
        for (CpCustGroup cpCustGroup : groupList) {
            TaskSonGroupVO taskSonGroupVO = new TaskSonGroupVO();
            taskSonGroupVO.setGroupName(cpCustGroup.getGroupName());
            taskSonGroupVO.setGroupId(cpCustGroup.getId());
            taskSonGroupVO.setTotal(cpCustGroup.getTotal());
            taskSonGroupVOS.add(taskSonGroupVO);
        }
        return ServerResponseEntity.success(taskSonGroupVOS);
    }

    @Override
    public ServerResponseEntity<List<CustGroupStaffCountVO>> groupCountByStaff(CpCustGroupCountDTO dto) {
        return ServerResponseEntity.success(custGroupService.groupCountByStaff(dto));
    }

    @Override
    public ServerResponseEntity<List<UserJoinCustGroupVO>> findCustGroupByQwUserId(String qwUserId) {
        List<UserJoinCustGroupVO> list = groupCustInfoMapper.findCustGroupByQwUserId(qwUserId);

        List<String> invitorUserIds=list.stream().map(item->item.getInvitorUserId()).distinct().collect(Collectors.toList());

        Map<String, StaffVO> staffVOMap= MapUtil.newHashMap();
        if(CollUtil.isNotEmpty(invitorUserIds)){
            StaffQueryDTO queryDTO=new StaffQueryDTO();
            queryDTO.setQiWeiUserIds(invitorUserIds);
            ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.findByStaffQueryDTO(queryDTO);
            ServerResponseEntity.checkResponse(responseEntity);
            staffVOMap= LambdaUtils.toMap(responseEntity.getData().stream().filter(item-> StrUtil.isNotBlank(item.getQiWeiUserId())).collect(Collectors.toList()), StaffVO::getQiWeiUserId);
        }
        for (UserJoinCustGroupVO userJoinCustGroupVO : list) {
            StaffVO staffVO = staffVOMap.get(userJoinCustGroupVO.getInvitorUserId());
            if(staffVO!=null){
                userJoinCustGroupVO.setInvitorUserName(staffVO.getStaffName());
            }
            //todo 最近联系时间
//            userJoinCustGroupVO.setLastContactTime();
        }
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Integer> countCustGroupByQwUserId(String qwUserId) {
        return ServerResponseEntity.success(groupCustInfoMapper.countCustGroupByQwUserId(qwUserId));
    }
}
