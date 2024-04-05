package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.constant.cp.GroupCustInfoAdmin;
import com.mall4j.cloud.api.biz.constant.cp.GroupCustInfoJoinScene;
import com.mall4j.cloud.api.biz.constant.cp.GroupCustInfoType;
import com.mall4j.cloud.api.biz.dto.cp.CpUserGroupDTO;
import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.UserJoinCustGroupVO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.cp.PageQueryCustInfo;
import com.mall4j.cloud.biz.mapper.cp.GroupCustInfoMapper;
import com.mall4j.cloud.biz.model.cp.CpGroupCustInfo;
import com.mall4j.cloud.biz.service.chat.SessionSearchService;
import com.mall4j.cloud.biz.service.cp.GroupCustInfoService;
import com.mall4j.cloud.biz.vo.cp.CpGroupCustInfoVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客户群表
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GroupCustInfoServiceImpl extends ServiceImpl<GroupCustInfoMapper,CpGroupCustInfo> implements GroupCustInfoService {

    private final GroupCustInfoMapper custInfoMapper;
    private final StaffFeignClient staffFeignClient;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    private final SessionSearchService sessionSearchService;

    @Override
    public PageVO<UserJoinCustGroupVO> appPageGroupByUser(CpUserGroupDTO dto) {
        PageDTO pageDTO=new PageDTO();
        pageDTO.setPageNum(dto.getPageNum());
        pageDTO.setPageSize(dto.getPageSize());

        PageVO<UserJoinCustGroupVO> pageVO=PageUtil.doPage(pageDTO, ()->custInfoMapper.findCustGroupByQwUserId(dto.getQiWeiUserId()));

        if(CollUtil.isNotEmpty(pageVO.getList())){
            List<String> invitorUserIds=pageVO.getList().stream().map(item->item.getInvitorUserId()).distinct().collect(Collectors.toList());
            Map<String, StaffVO> staffVOMap= MapUtil.newHashMap();
            if(CollUtil.isNotEmpty(invitorUserIds)){
                StaffQueryDTO queryDTO=new StaffQueryDTO();
                queryDTO.setQiWeiUserIds(invitorUserIds);
                ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.findByStaffQueryDTO(queryDTO);
                ServerResponseEntity.checkResponse(responseEntity);
                staffVOMap= LambdaUtils.toMap(responseEntity.getData().stream().filter(item-> StrUtil.isNotBlank(item.getQiWeiUserId())).collect(Collectors.toList()), StaffVO::getQiWeiUserId);
            }
            for (UserJoinCustGroupVO userJoinCustGroupVO : pageVO.getList()) {
                StaffVO staffVO = staffVOMap.get(userJoinCustGroupVO.getInvitorUserId());
                if(staffVO!=null){
                    userJoinCustGroupVO.setInvitorUserName(staffVO.getStaffName());
                }
                //todo 最近联系时间
                SessionFileDTO fileDTO = new SessionFileDTO();
                fileDTO.setRoomid(userJoinCustGroupVO.getChatId());
                userJoinCustGroupVO.setLastContactTime(sessionSearchService.getRoomLastTime(fileDTO));

            }
        }
        return pageVO;
    }

    @Override
    public PageVO<CpGroupCustInfoVO> page(PageDTO pageDTO, PageQueryCustInfo request) {

        if(!initSelect(request)){
            PageVO<CpGroupCustInfoVO> pageVO=new PageVO<>();
            pageVO.setTotal(0L);
            pageVO.setPages(0);
            pageVO.setList(null);
            return pageVO;
        }

        PageVO<CpGroupCustInfoVO> pageVO=PageUtil.doPage(pageDTO, () -> custInfoMapper.list(request));

        addData(pageVO.getList());

        return pageVO;
    }

    private void addData(List<CpGroupCustInfoVO> list){
        //获取员工信息
        List<String> invitorUserIds=list.stream().map(item->item.getInvitorUserId()).distinct().collect(Collectors.toList());
        invitorUserIds.addAll(list.stream().filter(item->item.getType()==1).map(item->item.getUserId()).distinct().collect(Collectors.toList()));
        List<String> userIds=list.stream().map(item->item.getUserId()).distinct().collect(Collectors.toList());
        Map<String,StaffVO> staffVOMap= MapUtil.newHashMap();
        Map<String,UserStaffCpRelationListVO> userRelVOMap= MapUtil.newHashMap();
        if(CollUtil.isNotEmpty(invitorUserIds)){
            StaffQueryDTO queryDTO=new StaffQueryDTO();
            queryDTO.setQiWeiUserIds(invitorUserIds);
            ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.findByStaffQueryDTO(queryDTO);
            ServerResponseEntity.checkResponse(responseEntity);
            staffVOMap= LambdaUtils.toMap(responseEntity.getData().stream().filter(item->StrUtil.isNotBlank(item.getQiWeiUserId())).collect(Collectors.toList()), StaffVO::getQiWeiUserId);
        }

        if(CollUtil.isNotEmpty(userIds)){
            UserStaffCpRelationSearchDTO staffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
            staffCpRelationSearchDTO.setQiWeiUserIds(userIds);
            staffCpRelationSearchDTO.setPageSize(10);
            staffCpRelationSearchDTO.setPageNum(1);
            ServerResponseEntity<List<UserStaffCpRelationListVO>>  responseEntityUc=userStaffCpRelationFeignClient.getUserStaffRelBy(staffCpRelationSearchDTO);
            ServerResponseEntity.checkResponse(responseEntityUc);
            userRelVOMap= LambdaUtils.toMap(responseEntityUc.getData(),UserStaffCpRelationListVO::getQiWeiUserId);
        }
        for (CpGroupCustInfoVO cpGroupCustInfo : list) {
            if(staffVOMap.containsKey(cpGroupCustInfo.getInvitorUserId())){
                cpGroupCustInfo.setInvitorUserName(staffVOMap.get(cpGroupCustInfo.getInvitorUserId()).getStaffName());
            }
            if(cpGroupCustInfo.getType()==1){
                if(staffVOMap.containsKey(cpGroupCustInfo.getUserId()) && StrUtil.isNotEmpty(staffVOMap.get(cpGroupCustInfo.getUserId()).getMobile())){
                    JSONArray jsonArray=new JSONArray();
                    jsonArray.add(staffVOMap.get(cpGroupCustInfo.getUserId()).getMobile());
                    cpGroupCustInfo.setPhone(jsonArray.toJSONString());
                }
            }else{
                if(userRelVOMap.containsKey(cpGroupCustInfo.getUserId())){
                    cpGroupCustInfo.setPhone(userRelVOMap.get(cpGroupCustInfo.getUserId()).getCpRemarkMobiles());
                }
            }
            if(Objects.nonNull(cpGroupCustInfo.getType())){
                cpGroupCustInfo.setTypeName(GroupCustInfoType.get(cpGroupCustInfo.getType()).getTxt());
            }
            if(Objects.nonNull(cpGroupCustInfo.getJoinScene())){
                cpGroupCustInfo.setJoinSceneName(GroupCustInfoJoinScene.get(cpGroupCustInfo.getJoinScene()).getTxt());
            }
            if(Objects.nonNull(cpGroupCustInfo.getIsAdmin())){
                cpGroupCustInfo.setIsAdminStr(GroupCustInfoAdmin.get(cpGroupCustInfo.getIsAdmin()).getTxt());
            }
        }
    }

    private boolean initSelect(PageQueryCustInfo request){
        //TODO 手机号筛选
        if(StrUtil.isNotEmpty(request.getPhone())){
            //客户
            List<String> phones=getUserQiWeiUserId(request.getPhone());
            List<String> staffIds=getStaffMobileUserIds(request.getPhone());
            if(CollUtil.isEmpty(phones) && CollUtil.isEmpty(staffIds)){
                return false;
            }
            request.getUserQiWeiUserIds().addAll(phones);
            request.getUserQiWeiUserIds().addAll(staffIds);
//            if(CollUtil.isEmpty(phones)){
//                return false;
//            }
//            request.setUserQiWeiUserIds(phones);
        }
        //TODO 邀请人名称筛选
        if(StrUtil.isNotEmpty(request.getInvitorUserName())){
            List<String> staffIds=getStaffUserIds(request.getInvitorUserName());
            if(CollUtil.isEmpty(staffIds)){
                return false;
            }
            request.setInvitorUserIds(staffIds);
        }
        return true;
    }


    @Override
    public List<CpGroupCustInfoVO> soldExcel(PageQueryCustInfo request) {
        if(!initSelect(request)){
            List<CpGroupCustInfoVO> list=new ArrayList<>();
            return list;
        }
        List<CpGroupCustInfoVO> list=custInfoMapper.list(request);

        addData(list);

        return list;
    }

    private List<UserStaffCpRelationListVO> getStaffRel(String phone){
        if(StrUtil.isEmpty(phone)){
            return ListUtil.empty();
        }
        UserStaffCpRelationSearchDTO staffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
        staffCpRelationSearchDTO.setUserPhone(phone);
        staffCpRelationSearchDTO.setPageSize(10);
        staffCpRelationSearchDTO.setPageNum(1);
        ServerResponseEntity<List<UserStaffCpRelationListVO>>  responseEntity=userStaffCpRelationFeignClient.getUserStaffRelBy(staffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(responseEntity);
        return responseEntity.getData();
    }



    private List<String> getUserQiWeiUserId(String phone){
        if(StrUtil.isEmpty(phone)){
            return ListUtil.empty();
        }
        return getStaffRel(phone).stream().map(item->item.getQiWeiUserId()).collect(Collectors.toList());
    }

    private List<String> getStaffUserIds(String staffName){
        if(StrUtil.isEmpty(staffName)){
            return ListUtil.empty();
        }
        StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
        staffQueryDTO.setStaffName(staffName);
        ServerResponseEntity<List<StaffVO>> responseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
        ServerResponseEntity.checkResponse(responseEntity);
        return responseEntity.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())).map(item->item.getQiWeiUserId()).distinct().collect(Collectors.toList());
    }

    private List<String> getStaffMobileUserIds(String phone){
        if(StrUtil.isEmpty(phone)){
            return ListUtil.empty();
        }
        StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
        staffQueryDTO.setMobile(phone);
        ServerResponseEntity<List<StaffVO>> responseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
        ServerResponseEntity.checkResponse(responseEntity);
        return responseEntity.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())).map(item->item.getQiWeiUserId()).distinct().collect(Collectors.toList());
    }

    @Override
    public CpGroupCustInfo getById(String userId, String groupId) {
        return custInfoMapper.getById(userId,groupId);
    }

    @Override
    public void saveCpGroupCustInfo(CpGroupCustInfo groupCustInfo) {
        this.save(groupCustInfo);
    }

    @Override
    public void updateCpGroupCustInfo(CpGroupCustInfo groupCustInfo) {
        this.updateById(groupCustInfo);
    }

    @Override
    public void deleteById(String userId, String groupId) {
        custInfoMapper.deleteById(userId,groupId);
    }

    @Override
    public void deleteByGroupId(String groupId,String updateBy) {
        custInfoMapper.deleteByGroupId(groupId,updateBy);
    }

}
