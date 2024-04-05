package com.mall4j.cloud.user.manager;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.user.bo.GroupSonTaskSendRecordBO;
import com.mall4j.cloud.user.bo.AddGroupSonTaskSendRecordBO;
import com.mall4j.cloud.user.constant.GroupPushTaskSendStatusEnum;
import com.mall4j.cloud.user.mapper.GroupSonTaskSendRecordMapper;
import com.mall4j.cloud.user.model.GroupSonTaskSendRecord;
import com.mall4j.cloud.user.vo.GroupSonTaskSendRecordVO;
import com.mall4j.cloud.user.vo.StaffSonTaskSendCountVO;
import com.mall4j.cloud.user.vo.StaffSonTaskSendDataVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GroupSonTaskSendRecordManager {

    @Autowired
    private GroupSonTaskSendRecordMapper groupSonTaskSendRecordMapper;

    @Autowired
    private MapperFacade mapperFacade;

    public List<StaffSonTaskSendCountVO> selectSendCountBySonTaskIdAndStaffList(Long sonTaskId,  List<Long> staffIdList){
        return groupSonTaskSendRecordMapper.selectSendCountBySonTaskIdAndStaffList(sonTaskId, staffIdList);
    }

    public List<Long> selectSendUserIdsBySonTaskIdAndUserList(Long staffId, Long sonTaskId, List<Long> userIdList){
        return groupSonTaskSendRecordMapper.selectSendUserIdsBySonTaskIdAndUserList(staffId, sonTaskId, userIdList);
    }

    public List<StaffSonTaskSendDataVO> selectSendDataByStaffId(Long staffId){
        return groupSonTaskSendRecordMapper.selectSendCountByStaff(staffId);
    }
    public Integer getSuccessCountBySonTaskId(Long sonTaskId){

        Integer sendCount = groupSonTaskSendRecordMapper.selectCount(
                new LambdaQueryWrapper<GroupSonTaskSendRecord>()
                        .eq(GroupSonTaskSendRecord::getPushSonTaskId, sonTaskId)
                        .eq(GroupSonTaskSendRecord::getSendStatus, GroupPushTaskSendStatusEnum.SUCCESS.getSendStatus())
        );
        return sendCount;
    }

    public Integer getSuccessCountBySonTaskIdAndStaffId(Long sonTaskId, Long staffId){

        Integer sendCount = groupSonTaskSendRecordMapper.selectCount(
                new LambdaQueryWrapper<GroupSonTaskSendRecord>()
                        .eq(GroupSonTaskSendRecord::getPushSonTaskId, sonTaskId)
                        .eq(GroupSonTaskSendRecord::getStaffId, staffId)
                        .eq(GroupSonTaskSendRecord::getSendStatus, GroupPushTaskSendStatusEnum.SUCCESS.getSendStatus())
        );
        return sendCount;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBatch(List<AddGroupSonTaskSendRecordBO> recordBOList){
        groupSonTaskSendRecordMapper.insertBatch(recordBOList);
    }

    /**
     * 根据群发子任务ID和导购ID、会员企业微信ID修改 t_group_son_task_send_record 表中完成状态，改为已完成
     * @param sonTaskId 群发子任务ID
     * @param staffId 导购ID
     * @param sendModel 发送类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSendStatusByVipCpUserId(Long sonTaskId, Long staffId, Integer sendModel, Integer sendStatus, String vipCpUserId){
        groupSonTaskSendRecordMapper.updateSendStatus(sonTaskId, staffId, sendModel, sendStatus, null, vipCpUserId);
    }

    /**
     * 根据群发子任务ID和导购ID、会员ID修改 t_group_son_task_send_record 表中完成状态，改为已完成
     * @param sonTaskId 群发子任务ID
     * @param staffId 导购ID
     * @param sendModel 发送类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSendStatusByUserId(Long sonTaskId, Long staffId, Integer sendModel, Integer sendStatus, Long userId){
        groupSonTaskSendRecordMapper.updateSendStatus(sonTaskId, staffId, sendModel, sendStatus, userId, null);
    }

    public List<GroupSonTaskSendRecordVO> getBySonTaskIdAndStaffId(Long sonTaskId, Long staffId){
        List<GroupSonTaskSendRecord> groupSonTaskSendRecordList = groupSonTaskSendRecordMapper.selectList(
                new LambdaQueryWrapper<GroupSonTaskSendRecord>()
                        .eq(GroupSonTaskSendRecord::getPushSonTaskId, sonTaskId)
                        .eq(GroupSonTaskSendRecord::getStaffId, staffId)
        );

        List<GroupSonTaskSendRecordVO> groupSonTaskSendRecordVOS = mapperFacade.mapAsList(groupSonTaskSendRecordList, GroupSonTaskSendRecordVO.class);
        return groupSonTaskSendRecordVOS;
    }

    /*public GroupSonTaskSendRecordVO getBySonTaskIdAndStaffIdAndUserId(Long sonTaskId, Long staffId, Long userId){
        GroupSonTaskSendRecord groupSonTaskSendRecord = groupSonTaskSendRecordMapper.selectOne(
                new LambdaQueryWrapper<GroupSonTaskSendRecord>()
                        .eq(GroupSonTaskSendRecord::getPushSonTaskId, sonTaskId)
                        .eq(GroupSonTaskSendRecord::getStaffId, staffId)
                        .eq(GroupSonTaskSendRecord::getUserId, userId)
        );



        if(Objects.nonNull(groupSonTaskSendRecord)){
            GroupSonTaskSendRecordVO groupSonTaskSendRecordVO = new GroupSonTaskSendRecordVO();
            BeanUtils.copyProperties(groupSonTaskSendRecord, groupSonTaskSendRecordVO);
            return groupSonTaskSendRecordVO;
        }
        //List<GroupSonTaskSendRecordVO> groupSonTaskSendRecordVOS = mapperFacade.mapAsList(groupSonTaskSendRecordList, GroupSonTaskSendRecordVO.class);
        return null;
    }*/

}
