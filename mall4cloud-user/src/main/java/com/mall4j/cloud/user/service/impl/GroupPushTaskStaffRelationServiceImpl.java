package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.constant.FinalSendTypeEnum;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.ExcelMarkingUserPageBO;
import com.mall4j.cloud.user.bo.ExportGroupPushTaskStatisticsBO;
import com.mall4j.cloud.user.bo.ExportSonTaskStaffPageBO;
import com.mall4j.cloud.user.dto.QueryMarkingUserPageDTO;
import com.mall4j.cloud.user.dto.QuerySonTaskStaffPageDTO;
import com.mall4j.cloud.user.manager.GroupPushTaskStaffRelationManager;
import com.mall4j.cloud.user.manager.GroupPushTaskVipRelationManager;
import com.mall4j.cloud.user.manager.GroupSonTaskSendRecordManager;
import com.mall4j.cloud.user.manager.UserTagRelationManager;
import com.mall4j.cloud.user.mapper.GroupPushTaskStaffRelationMapper;
import com.mall4j.cloud.user.model.GroupPushTaskStaffRelation;
import com.mall4j.cloud.user.service.GroupPushTaskStaffRelationService;
import com.mall4j.cloud.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 群发任务导购关联表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Slf4j
@Service
public class GroupPushTaskStaffRelationServiceImpl extends ServiceImpl<GroupPushTaskStaffRelationMapper, GroupPushTaskStaffRelation> implements GroupPushTaskStaffRelationService {

    @Autowired
    private GroupPushTaskStaffRelationManager groupPushTaskStaffRelationManager;

    @Autowired
    private UserTagRelationManager userTagRelationManager;

    @Autowired
    private GroupSonTaskSendRecordManager groupSonTaskSendRecordManager;

    @Autowired
    private GroupPushTaskVipRelationManager groupPushTaskVipRelationManager;

    @Autowired
    private MapperFacade mapperFacade;

    private static final Long PAGE_NUM = 1L;

    private static final Long PAGE_SIZE = 500L;


    @Override
    public PageVO<SonTaskStaffPageVO> getSonTaskStaffList(QuerySonTaskStaffPageDTO pageDTO) {

        List<StaffVO> staffList;
        List<Long> staffIdList;
        Map<Long, StaffVO> staffVOMap = null;
        //如果staff参数不为空，先查询相关staff信息
        if (StringUtils.isNotEmpty(pageDTO.getStaff())){
            staffList = groupPushTaskStaffRelationManager.getByStaffNOOrNickName(pageDTO.getStaff());

            staffVOMap = staffList.stream()
                    .collect(Collectors.toMap(StaffVO::getId, staffVO -> staffVO));

            staffIdList = staffList.stream()
                    .map(StaffVO::getId)
                    .collect(Collectors.toList());

            pageDTO.setStaffIdList(staffIdList);
        }

        //查询相关导购分页记录
        PageVO<SonTaskStaffPageVO> sonTaskStaffPage = groupPushTaskStaffRelationManager.getSonTaskStaffList(pageDTO);
        if (CollectionUtil.isEmpty(sonTaskStaffPage.getList())){
            return  sonTaskStaffPage;
        }

        //聚合记录中的导购数据，如果输入了相关导购信息则不需要再查
        if (Objects.isNull(staffVOMap)){
            staffIdList = sonTaskStaffPage.getList().stream()
                    .map(SonTaskStaffPageVO::getStaffId)
                    .collect(Collectors.toList());
            staffList = userTagRelationManager.getStaffList(staffIdList);
            staffVOMap = staffList.stream()
                    .collect(Collectors.toMap(StaffVO::getId, staffVO -> staffVO));
        }

        //包装数据
        wrraperSonTaskStaffPage(sonTaskStaffPage, staffVOMap);

        return sonTaskStaffPage;
    }

    /**
     * 查询
     * */
    @Override
    public List<ExportSonTaskStaffPageBO> getDataByTaskAndSonTask(Long pushTaskId, Long sonTaskId) {

        Integer pages = PAGE_NUM.intValue();
        Integer pageNum = 0;

        List<ExportSonTaskStaffPageBO> exportSonTaskStaffPageBOList = new ArrayList<>();

        PageVO<SonTaskStaffPageVO> pageVO;

        while (pageNum.longValue() < pages){

            pageNum += 1;

            QuerySonTaskStaffPageDTO pageDTO = new QuerySonTaskStaffPageDTO();

            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(PAGE_SIZE.intValue());
            pageDTO.setPushTaskId(pushTaskId);
            pageDTO.setSonTaskId(sonTaskId);

            pageVO = this.getSonTaskStaffList(pageDTO);
            if (CollectionUtil.isNotEmpty(pageVO.getList())){
                pages = pageVO.getPages();

                List<ExportSonTaskStaffPageBO> currentSonTaskStaffPageBOS = mapperFacade.mapAsList(pageVO.getList(), ExportSonTaskStaffPageBO.class);
                exportSonTaskStaffPageBOList.addAll(currentSonTaskStaffPageBOS);
            }


        }
        return exportSonTaskStaffPageBOList;
    }


    private void wrraperSonTaskStaffPage(PageVO<SonTaskStaffPageVO> pageVO, Map<Long, StaffVO> staffVOMap){
       //log.info("NOT ADD FRIEND MAP IS :{}", JSONObject.toJSONString(notAddFriendCountMap));

        pageVO.getList().stream()
                .forEach(staff -> {
                    StaffVO currentStaffVO;
                    if (MapUtil.isNotEmpty(staffVOMap) && Objects.nonNull(currentStaffVO = staffVOMap.get(staff.getStaffId()))){
                        staff.setStaffNo(currentStaffVO.getStaffNo());
                        staff.setStaffNickName(currentStaffVO.getStaffName());
                    }
                    /*StaffPushTaskIssueCountVO currentIssueCountVO;
                    if (MapUtil.isNotEmpty(issueCountMap) && Objects.nonNull(currentIssueCountVO = issueCountMap.get(staff.getStaffId()))){
                        staff.setPushIssueCount(currentIssueCountVO.getIssueCount());
                    }
                    StaffPushTaskNotAddFriendCountVO notAddFriendCountVO;
                    if (MapUtil.isNotEmpty(notAddFriendCountMap) && Objects.nonNull(notAddFriendCountVO = notAddFriendCountMap.get(staff.getStaffId()))){
                        staff.setNotAddFriendCount(notAddFriendCountVO.getNotAddCount());
                    }else {
                        staff.setNotAddFriendCount(0);
                    }
                    if (Objects.isNull(staff.getPushIssueFinishCount())){
                        staff.setPushIssueFinishCount(0);
                    }
                    if (staff.getFinalSend().equals(FinalSendTypeEnum.YES.getValue())){
                        staff.setFinalSendRemark("是");
                    }
                    if (staff.getFinalSend().equals(FinalSendTypeEnum.NO.getValue())){
                        staff.setFinalSendRemark("否");
                    }*/
                });
    }





}
