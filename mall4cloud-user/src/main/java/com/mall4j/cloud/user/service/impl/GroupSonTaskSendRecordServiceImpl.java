package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.ExportSonTaskStaffPageBO;
import com.mall4j.cloud.user.bo.ExportStaffSendRecordBO;
import com.mall4j.cloud.user.constant.*;
import com.mall4j.cloud.user.dto.QueryGroupPushRecordDTO;
import com.mall4j.cloud.user.dto.QuerySendRecordPageDTO;
import com.mall4j.cloud.user.dto.QuerySonTaskStaffPageDTO;
import com.mall4j.cloud.user.manager.GroupPushTaskManager;
import com.mall4j.cloud.user.manager.UserTagRelationManager;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMapper;
import com.mall4j.cloud.user.model.GroupPushSonTask;
import com.mall4j.cloud.user.model.GroupSonTaskSendRecord;
import com.mall4j.cloud.user.mapper.GroupSonTaskSendRecordMapper;
import com.mall4j.cloud.user.service.GroupSonTaskSendRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 推送完成记录表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-03-01
 */
@Slf4j
@Service
public class GroupSonTaskSendRecordServiceImpl extends ServiceImpl<GroupSonTaskSendRecordMapper, GroupSonTaskSendRecord> implements GroupSonTaskSendRecordService {


    @Autowired
    private GroupSonTaskSendRecordMapper groupSonTaskSendRecordMapper;

    @Autowired
    private UserTagRelationManager userTagRelationManager;

    @Autowired
    private GroupPushTaskManager groupPushTaskManager;

    @Autowired
    private MapperFacade mapperFacade;

    private static final Long PAGE_NUM = 1L;

    private static final Long PAGE_SIZE = 500L;

    @Override
    public ServerResponseEntity<PageVO<StaffSendRecordVO>> getTheSendRecordBySonTaskAndStaff(QuerySendRecordPageDTO pageDTO) {

        IPage<StaffSendRecordVO> staffSendRecordVOIPage = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());

        staffSendRecordVOIPage = groupSonTaskSendRecordMapper
                .selectSendRecordBySonTaskIdAndStaffId(staffSendRecordVOIPage, pageDTO);

        if (CollectionUtil.isNotEmpty(staffSendRecordVOIPage.getRecords())){

            GroupPushTaskVO pushTask = groupPushTaskManager.getByPushTaskID(pageDTO.getPushTaskId());
            List<Long> staffIdList = staffSendRecordVOIPage.getRecords().stream()
                    .map(StaffSendRecordVO::getStaffId)
                    .distinct()
                    .collect(Collectors.toList());

            List<Long> storeIdList = staffSendRecordVOIPage.getRecords().stream()
                    .map(StaffSendRecordVO::getStaffStoreId)
                    .distinct()
                    .collect(Collectors.toList());

            List<StaffVO> staffVOList = userTagRelationManager.getStaffList(staffIdList);

            List<StoreVO> serviceStoreList = userTagRelationManager.getServiceStoreList(storeIdList);

            Map<Long, StaffVO> staffVOMap = null;
            if (CollectionUtil.isNotEmpty(staffVOList)){
                staffVOMap = staffVOList.stream()
                        .filter(staffVO -> Objects.nonNull(staffVO))
                        .collect(Collectors.toMap(StaffVO::getId, staffVO -> staffVO));
            }


            Map<Long, StoreVO> storeVOMap = null;
            if (CollectionUtil.isNotEmpty(serviceStoreList)){
                storeVOMap = serviceStoreList.stream()
                        .filter(storeVO -> Objects.nonNull(storeVO))
                        .collect(Collectors.toMap(StoreVO::getStoreId, store -> store));
            }

            this.wrapperStoreAndStaff(staffSendRecordVOIPage.getRecords(), pushTask, staffVOMap, storeVOMap);

        }


        PageVO<StaffSendRecordVO> pageVO = new PageVO<>();

        pageVO.setPages((int)staffSendRecordVOIPage.getPages());
        pageVO.setTotal(staffSendRecordVOIPage.getTotal());
        pageVO.setList(staffSendRecordVOIPage.getRecords());

        return ServerResponseEntity.success(pageVO);
    }


    @Override
    public List<ExportStaffSendRecordBO> getDataByTaskAndSonTask(Long pushTaskId, Long sonTaskId) {

        Integer pages = PAGE_NUM.intValue();
        Integer pageNum = 0;

        List<ExportStaffSendRecordBO> exportStaffSendRecordBOList = new ArrayList<>();

        PageVO<StaffSendRecordVO> pageVO;
        QuerySendRecordPageDTO pageDTO = new QuerySendRecordPageDTO();
        pageDTO.setPageSize(PAGE_SIZE.intValue());
        pageDTO.setPushTaskId(pushTaskId);
        pageDTO.setSonTaskId(sonTaskId);

        while (pageNum.longValue() < pages){

            pageNum += 1;

            pageDTO.setPageNum(pageNum);

            pageVO = this.getTheSendRecordBySonTaskAndStaff(pageDTO).getData();
            log.info("THE {} PAGE DATA IS:", JSONObject.toJSONString(pageVO));
            if (CollectionUtil.isNotEmpty(pageVO.getList())){
                pages = pageVO.getPages();
                pageVO.getList().stream().forEach(vo -> {


                    boolean friendState = Objects.nonNull(vo.getFriendState())
                            && vo.getFriendState().equals(StaffCpFriendStateEnum.YES.getFriendState());

                    //有可能是发送后好友关系被删，所以之前的任务仍然需要记录已添加好友
                    boolean deleteBySend;

                    boolean notFriendForCallBack = Objects.nonNull(vo.getSendStatus())
                            && vo.getSendStatus().equals(GroupPushTaskSendStatusEnum.NOT_ADD_FRIEND_FAIL.getSendStatus());

                    if (deleteBySend = Objects.nonNull(vo.getSendStatus())
                            && vo.getSendStatus().equals(StaffCpMsgSendStatusEnum.SUCCESS.getSendStatus())){
                        vo.setSendRemark("已触达");
                    }else {
                        vo.setSendRemark("未触达");
                    }

                    if (friendState || deleteBySend){
                        vo.setAddRemark("已添加");
                    }
                    if (!friendState || notFriendForCallBack){
                        vo.setAddRemark("未添加");
                    }
                    if (Objects.nonNull(vo.getSendModel())
                            && vo.getSendModel().equals(GroupPushTaskSendModelEnum.TO_GROUP_PUSH.value())){
                        vo.setSendModelRemark(GroupPushTaskSendModelEnum.TO_GROUP_PUSH.getSendModel());
                    }
                    if (Objects.nonNull(vo.getSendModel())
                            && vo.getSendModel().equals(GroupPushTaskSendModelEnum.TO_ONCE_PUSH.value())){
                        vo.setSendModelRemark(GroupPushTaskSendModelEnum.TO_ONCE_PUSH.getSendModel());
                    }

                });
                List<ExportStaffSendRecordBO> currentBOS = mapperFacade.mapAsList(pageVO.getList(), ExportStaffSendRecordBO.class);

                exportStaffSendRecordBOList.addAll(currentBOS);
            }


        }
        return exportStaffSendRecordBOList;
    }

    @Override
    public List<GroupPushTaskRecordStatisticVO> getTaskRecordStatistic(QueryGroupPushRecordDTO params) {
        return groupSonTaskSendRecordMapper.getTaskRecordStatistic(params);
    }

    @Override
    public Integer getGroupPushStatusCountByStaffIdAndSonTaskId(Long staffId, Long sonTaskId) {
        Integer count = groupSonTaskSendRecordMapper.selectCount(new QueryWrapper<GroupSonTaskSendRecord>()
                .lambda()
                .eq(GroupSonTaskSendRecord::getStaffId, staffId)
                .eq(GroupSonTaskSendRecord::getPushSonTaskId, sonTaskId)
                .eq(GroupSonTaskSendRecord::getSendModel, GroupPushTaskSendModelEnum.TO_GROUP_PUSH.value())
        );
        return count;
    }

    void wrapperStoreAndStaff(List<StaffSendRecordVO> staffSendRecordVOList, GroupPushTaskVO pushTask,
                         Map<Long, StaffVO> staffVOMap, Map<Long, StoreVO> storeVOMap){
        staffSendRecordVOList.stream().forEach(record -> {
            StaffVO staffVO;
            if (MapUtil.isNotEmpty(staffVOMap) && Objects.nonNull(staffVO = staffVOMap.get(record.getStaffId()))){
                record.setStaffNo(staffVO.getStaffNo());
                record.setStaffNickName(staffVO.getStaffName());
            }

            StoreVO storeVO;
            if (MapUtil.isNotEmpty(storeVOMap) && Objects.nonNull(storeVO = storeVOMap.get(record.getStaffStoreId()))){
                record.setStaffStoreName(storeVO.getName());
                record.setStaffStoreCode(storeVO.getStoreCode());
            }else {
                record.setStaffStoreName(GarbageDataForStoreEnum.UNKNOWN.getStoreName());
                record.setStaffStoreCode(GarbageDataForStoreEnum.UNKNOWN.getStoreCode());
                record.setStaffStoreId(GarbageDataForStoreEnum.UNKNOWN.getStoreId());
            }
            record.setPushTaskId(pushTask.getGroupPushTaskId());
            record.setPushTaskName(pushTask.getTaskName());

        });
    }



}
