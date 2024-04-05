package com.mall4j.cloud.biz.service.chat.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.constant.cp.SessionAgreeStatus;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.utils.UserStaffRelUtils;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.chat.SessionAgreeDTO;
import com.mall4j.cloud.biz.mapper.chat.UserSessionAgreeMapper;
import com.mall4j.cloud.biz.service.chat.SessionAgreeService;
import com.mall4j.cloud.biz.vo.chat.SessionAgreeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会话同意实现类
 *
 */
@Service
public class SessionAgreeServiceImpl implements SessionAgreeService {

    @Autowired
    private UserSessionAgreeMapper sessionAgreeMapper;
    @Autowired
    private StaffFeignClient feignClient;
    @Autowired
    private UserStaffCpRelationFeignClient relationFeignClient;

    @Override
    public PageVO<SessionAgreeVO> page(PageDTO pageDTO, SessionAgreeDTO dto) {
        if(!initSelect(dto)){
            PageVO pageVO=new PageVO<SessionAgreeVO>();
            pageVO.inint();
            return pageVO;
        }
        PageVO<SessionAgreeVO> VO = PageUtil.doPage(pageDTO, () -> sessionAgreeMapper.list(dto));
        List<SessionAgreeVO> list = VO.getList();
        addData(list);
        return PageUtil.mongodbPage(pageDTO, list,VO.getTotal());
    }

    @Override
    public List<SessionAgreeVO> soldExcel(SessionAgreeDTO dto) {
        List<SessionAgreeVO> list=sessionAgreeMapper.list(dto);
        addData(list);
        return list;
    }

    private boolean initSelect(SessionAgreeDTO dto){
        if(StrUtil.isNotEmpty(dto.getUserName()) || StrUtil.isNotEmpty(dto.getUserPhone())){
            UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
            userStaffCpRelationSearchDTO.initPage();
            userStaffCpRelationSearchDTO.setQiWeiNickName(dto.getUserName());
            userStaffCpRelationSearchDTO.setUserPhone(dto.getUserPhone());
            ServerResponseEntity<List<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
            ServerResponseEntity.checkResponse(cpListVO);
            List<String> userIds=cpListVO.getData().stream()
                    .filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId()))
                    .map(item->item.getQiWeiUserId()).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(userIds)){
                dto.setExteranalOpenIds(userIds);
                return true;
            }
            return false;
        }
        return true;
    }

    private void addData(List<SessionAgreeVO> list){
        List<String> staffId = list.stream().map(SessionAgreeVO:: getUserId).collect(Collectors.toList());
        List<String> customId = list.stream().map(SessionAgreeVO:: getExteranalOpenId).collect(Collectors.toList());

        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(staffId);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staffList);
        Map<String,StaffVO> staffVOMap = LambdaUtils.toMap(staffList.getData().stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId())),StaffVO::getQiWeiUserId);

        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.setPageSize(10);
        userStaffCpRelationSearchDTO.setPageNum(1);
        userStaffCpRelationSearchDTO.setQiWeiUserIds(customId);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.getUserStaffRelAll(userStaffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(cpListVO);
//        Map<String,UserStaffCpRelationListVO> relationListVOMap = LambdaUtils.toMap(cpListVO.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),UserStaffCpRelationListVO::getQiWeiUserId);
        Map<String,UserStaffCpRelationListVO> relationListVOMap = UserStaffRelUtils.mapByStaffAndUser(cpListVO.getData());

//        StaffQueryDTO staffDto = new StaffQueryDTO();
//        staffDto.setQiWeiUserIds(staffId);
//        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
//        List<StaffVO> vo = staffList.getData();

//        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
//        userStaffCpRelationSearchDTO.setPageSize(pageDTO.getPageSize());
//        userStaffCpRelationSearchDTO.setPageNum(pageDTO.getPageNum());
//        userStaffCpRelationSearchDTO.setQiWeiUserIds(customId);
//        ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.pageWithStaff(userStaffCpRelationSearchDTO);
//        List<UserStaffCpRelationListVO> cpList = new ArrayList<>();
//        if(cpListVO!=null && cpListVO.getData()!=null){
//            cpList = cpListVO.getData().getList();
//        }
        for (SessionAgreeVO agreeVO:list) {
            if(StrUtil.isNotEmpty(agreeVO.getAgreeStatus())){
                agreeVO.setAgreeStatusName(SessionAgreeStatus.get(agreeVO.getAgreeStatus()).getTxt());
            }
            if(staffVOMap.containsKey(agreeVO.getUserId())){
                agreeVO.setStaffName(staffVOMap.get(agreeVO.getUserId()).getStaffName());
            }
            String key=agreeVO.getExteranalOpenId()+agreeVO.getUserId();
            if(relationListVOMap.containsKey(key)){
                agreeVO.setUserName(relationListVOMap.get(key).getQiWeiNickName());
                agreeVO.setCpRemark(relationListVOMap.get(key).getCpRemark());
                agreeVO.setPhone(relationListVOMap.get(key).getCpRemarkMobiles());
                agreeVO.setCorpFullName(relationListVOMap.get(key).getCorpFullName());
            }

//            if(vo!=null && vo.size()>0){
//                for (StaffVO staffVO :vo) {
//                    if(StringUtils.isNotBlank(staffVO.getQiWeiUserId()) && staffVO.getQiWeiUserId().equals(agreeVO.getUserId())){
//                        agreeVO.setStaffName(staffVO.getStaffName());
//                        break;
//                    }
//                }
//            }
//            if(cpList!=null && cpList.size()>0){
//                for (UserStaffCpRelationListVO listVO:cpList) {
//                    if(StringUtils.isNotBlank(listVO.getQiWeiUserId()) && listVO.getQiWeiUserId().equals(agreeVO.getExteranalOpenId())){
//                        agreeVO.setUserName(listVO.getQiWeiNickName());
//                        agreeVO.setPhone(listVO.getCpRemarkMobiles());
//                        break;
//                    }
//                }
//            }
        }
    }

    @Override
    public List<SessionAgreeVO> agreeProportion(SessionAgreeDTO dto) {
        return sessionAgreeMapper.agreeProportion(dto);
    }

    @Override
    public List<SessionAgreeVO> agreeMonCount(SessionAgreeDTO dto) {
        return sessionAgreeMapper.agreeMonCount(dto);
    }

    @Override
    public SessionAgreeVO agreeSum(SessionAgreeDTO dto) {
        return sessionAgreeMapper.agreeSum(dto);
    }


}
