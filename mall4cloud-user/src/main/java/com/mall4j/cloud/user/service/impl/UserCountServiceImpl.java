package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.mall4j.cloud.api.biz.dto.cp.CpCustGroupCountDTO;
import com.mall4j.cloud.api.biz.feign.CpCustGroupClient;
import com.mall4j.cloud.api.biz.vo.CustGroupStaffCountVO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryPageDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.constant.ContactChangeTypeEnum;
import com.mall4j.cloud.api.user.vo.UserCountVO;
import com.mall4j.cloud.api.user.vo.UserRelactionAddWayVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.user.dto.UserCountDTO;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.service.UserCountService;
import com.mall4j.cloud.api.user.vo.SelectUserCountVO;
import com.mall4j.cloud.user.vo.UserRelCountDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 好友统计
 *
 */
@Slf4j
@Service
public class UserCountServiceImpl implements UserCountService {
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private UserStaffCpRelationMapper cpRelationMapper;
    @Autowired
    private CpCustGroupClient cpCustGroupClient;

    @Override
    public PageVO<UserCountVO> page(UserCountDTO dto) {
        StaffQueryPageDTO queryPageDTO=new StaffQueryPageDTO();
        queryPageDTO.setIsDelete(0);
        queryPageDTO.setStatus(0);
        queryPageDTO.setPageNum(dto.getPageNum());
        queryPageDTO.setPageSize(dto.getPageSize());
        queryPageDTO.setStaffIdList(dto.getStaffIds());
        queryPageDTO.setStaffName(dto.getName());
        ServerResponseEntity<PageVO<StaffVO>> responseEntity=staffFeignClient.getStaffPage(queryPageDTO);
        ServerResponseEntity.checkResponse(responseEntity);
        if(CollUtil.isEmpty(responseEntity.getData().getList())){
            PageVO pageVO=new PageVO();
            pageVO.setList(null);
            pageVO.setTotal(0L);
            pageVO.setPages(0);
            return pageVO;
        }
        List<StaffVO> staffVOList=responseEntity.getData().getList();
        //填充统计数据
        List<UserCountVO> userCountVOS=initData(dto,staffVOList);

        PageVO<UserCountVO> pageVO=new PageVO();
        pageVO.setList(userCountVOS);
        pageVO.setTotal(responseEntity.getData().getTotal());
        pageVO.setPages(userCountVOS.size());
        return pageVO;
    }

    @Override
    public List<UserCountVO> pageList(UserCountDTO dto) {
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        staffQueryDTO.setStatus(0);
        staffQueryDTO.setStaffIdList(dto.getStaffIds());
        List<StaffVO> staffVOList = staffFeignClient.findByStaffQueryDTO(staffQueryDTO).getData();
        List<UserCountVO> userCountVOS=initData(dto,staffVOList);
        return userCountVOS;
    }

    private List<UserCountVO> initData(UserCountDTO dto,List<StaffVO> staffVOList){
        List<Long> staffIds=staffVOList.stream().map(item->item.getId()).collect(Collectors.toList());
        //获取员工下新增好友
        dto.setStaffIds(staffIds);

        List<SelectUserCountVO>  addUserCounts = cpRelationMapper.getUserCount(dto);
        Map<Long, SelectUserCountVO> addUserCountMap= LambdaUtils.toMap(addUserCounts,SelectUserCountVO::getStaffId);
        dto.setStatus(null);
        //获取员工删除好友
        dto.setContactChangeType(ContactChangeTypeEnum.DEL_ALL.getCode());
        List<SelectUserCountVO> delUserCounts = cpRelationMapper.getUserAndChangeType(dto);
        Map<Long, SelectUserCountVO> delUserCountMap= LambdaUtils.toMap(delUserCounts,SelectUserCountVO::getStaffId);

        //获取好友流失
        dto.setContactChangeType(ContactChangeTypeEnum.DEL_FOLLOW_USER.getCode());
        List<SelectUserCountVO> lossUserUserCounts = cpRelationMapper.getUserAndChangeType(dto);
        Map<Long, SelectUserCountVO> lossUserUserCountMap= LambdaUtils.toMap(lossUserUserCounts,SelectUserCountVO::getStaffId);

        //群统计数据
        CpCustGroupCountDTO groupCountDTO=new CpCustGroupCountDTO();
        groupCountDTO.setStartTime(dto.getStartTime());
        groupCountDTO.setEndTime(dto.getEndTime());
        groupCountDTO.setStaffIds(dto.getStaffIds());
        ServerResponseEntity<List<CustGroupStaffCountVO>> groupResponseEntity=cpCustGroupClient.groupCountByStaff(groupCountDTO);
        ServerResponseEntity.checkResponse(groupResponseEntity);
        Map<Long, CustGroupStaffCountVO> groupCountMap= LambdaUtils.toMap(groupResponseEntity.getData(),CustGroupStaffCountVO::getStaffId);

        List<UserCountVO> userCountVOS = new ArrayList<>();
        for (StaffVO vo:staffVOList) {
            UserCountVO countVO = new UserCountVO();
            //获取员工下新增好友
            int addUser = addUserCountMap.containsKey(vo.getId())?addUserCountMap.get(vo.getId()).getCount():0;
            //获取员工删除好友
            int delUser = delUserCountMap.containsKey(vo.getId())?delUserCountMap.get(vo.getId()).getCount():0;
            //获取好友流失
            int lossUser = lossUserUserCountMap.containsKey(vo.getId())?lossUserUserCountMap.get(vo.getId()).getCount():0;
            countVO.setName(vo.getStaffName());
            countVO.setAddUser(addUser);
            countVO.setLossUser(lossUser);
            countVO.setDelUser(delUser);
            countVO.setCountUser(NumberUtil.add(addUser,delUser,lossUser).intValue());
            countVO.setStaffId(vo.getId());

            //新增客户群
            int addRoom = groupCountMap.containsKey(vo.getId())?groupCountMap.get(vo.getId()).getNewGroupCount():0;
            countVO.setAddRoom(""+addRoom);
            //总客户群
            int countRoom = groupCountMap.containsKey(vo.getId())?groupCountMap.get(vo.getId()).getGroupCount():0;
            countVO.setCountRoom(""+countRoom);
            //新增入群人数
            int addRoomPeople = groupCountMap.containsKey(vo.getId())?groupCountMap.get(vo.getId()).getNewGroupUserCount():0;
            countVO.setAddRoomPeople(""+addRoomPeople);

            //留存率
            BigDecimal retained=NumberUtil.sub(addUser,lossUser,delUser);
            if(retained.doubleValue()>0 && addUser>0){
                BigDecimal percentage = retained.divide(new BigDecimal(addUser), 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                countVO.setRetained(percentage.doubleValue()+"%");
            }else{
                countVO.setRetained("0%");
            }

            userCountVOS.add(countVO);
        }
        return userCountVOS;
    }



    @Override
    public List<UserCountVO> getSexCount(UserCountDTO dto) {
        return cpRelationMapper.getUserSexCount(dto);
    }

    @Override
    public List<UserCountVO> getChartCount(UserCountDTO dto) {
        List<UserCountVO> list=cpRelationMapper.getChartCount(dto);
        Map<String,UserCountVO> mapData=LambdaUtils.toMap(list,UserCountVO::getMon);
        //
        List<UserCountVO> back=new ArrayList<>();
        Date startTime=DateUtil.parse(dto.getStartTime(),"yyyy-MM-dd HH:mm:ss");
        Date endTime=DateUtil.parse(dto.getEndTime(),"yyyy-MM-dd HH:mm:ss");
        List<DateTime> dateTimes=DateUtil.rangeToList(startTime,endTime, DateField.DAY_OF_YEAR);//按天
        for (DateTime dateTime : dateTimes) {
            String time=DateUtil.format(dateTime,"yyyy-MM-dd");
            UserCountVO userCountVO=new UserCountVO();
            userCountVO.setMon(time);
            userCountVO.setMonthCount(0);
            if(mapData.containsKey(time)){
                userCountVO.setMonthCount(mapData.get(time).getMonthCount());
            }
            back.add(userCountVO);
        }

        return back;
    }

    @Override
    public List<UserRelactionAddWayVO> getUserRelactionAddWays(UserCountDTO dto) {
        return cpRelationMapper.selectUserRelactionAddWays(dto);
    }

    @Override
    public UserRelCountDataVO getUserRelCountDataVO(UserCountDTO dto) {
        //累计数据
        UserCountDTO dto1=new UserCountDTO();
        dto1.setStaffIds(dto.getStaffIds());
        dto1.setCodeGroupIds(dto.getCodeGroupIds());
        dto1.setCodeChannelIds(dto.getCodeChannelIds());
//        dto1.setStartTime(dto.getStartTime());
//        dto1.setEndTime(dto.getEndTime());
        UserRelCountDataVO dataVO1=cpRelationMapper.selectUserRelCountDataVO(dto1);
        //非累计数据
        UserRelCountDataVO dataVO=cpRelationMapper.selectUserRelCountDataVO(dto);

        UserRelCountDataVO userRelCountDataVO=new UserRelCountDataVO();
        userRelCountDataVO.setAddCount(0);
        userRelCountDataVO.setLossCount(0);
        userRelCountDataVO.setDelCount(0);
        userRelCountDataVO.setAddNewCount(0);
        userRelCountDataVO.setLossNewCount(0);
        userRelCountDataVO.setDelNewCount(0);
        if(Objects.nonNull(dataVO1)){//累计数据
            userRelCountDataVO.setAddCount(dataVO1.getAddNewCount());
            userRelCountDataVO.setLossCount(dataVO1.getLossNewCount());
            userRelCountDataVO.setDelCount(dataVO1.getDelNewCount());
        }
        if(Objects.nonNull(dataVO)){//非累计数据
            userRelCountDataVO.setAddNewCount(dataVO.getAddNewCount());
            userRelCountDataVO.setLossNewCount(dataVO.getLossNewCount());
            userRelCountDataVO.setDelNewCount(dataVO.getDelNewCount());
        }

        return userRelCountDataVO;
    }
}
