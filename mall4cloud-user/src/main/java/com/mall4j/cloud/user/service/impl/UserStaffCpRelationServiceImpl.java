package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.dto.UserRegisterQiWeiMsgDTO;
import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.feign.CpCustGroupClient;
import com.mall4j.cloud.api.biz.feign.MaterialFeignClient;
import com.mall4j.cloud.api.biz.feign.SessionFileFeignClient;
import com.mall4j.cloud.api.biz.feign.UserRegisterSendMsgFeignClient;
import com.mall4j.cloud.api.biz.vo.MaterialBrowseRecordApiVO;
import com.mall4j.cloud.api.biz.vo.StaffSessionVO;
import com.mall4j.cloud.api.biz.vo.UserJoinCustGroupVO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.constant.ContactChangeTypeEnum;
import com.mall4j.cloud.api.user.constant.StaffUserRelStatusEnum;
import com.mall4j.cloud.api.user.crm.request.UpdateUserTagRequest;
import com.mall4j.cloud.api.user.crm.response.UserTag;
import com.mall4j.cloud.api.user.dto.CRMUserTagDto;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.CountNotMemberUsersVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.common.util.SystemUtils;
import com.mall4j.cloud.user.bo.GroupPushTaskCpRelationBO;
import com.mall4j.cloud.user.constant.UserJourneyEnum;
import com.mall4j.cloud.user.dto.CrmUserTags;
import com.mall4j.cloud.user.dto.UserJourneysDTO;
import com.mall4j.cloud.user.dto.UserStaffCpRelationSetTagRequest;
import com.mall4j.cloud.user.manager.UserStaffCpRelationManager;
import com.mall4j.cloud.user.mapper.CrmUserTagRelationMapper;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.mapper.UserStaffRelationFollowUpMapper;
import com.mall4j.cloud.user.model.CrmUserTagRelation;
import com.mall4j.cloud.user.model.UserGroup;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.model.UserStaffRelationFollowUp;
import com.mall4j.cloud.user.service.UserGroupService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import com.mall4j.cloud.user.service.crm.CrmService;
import com.mall4j.cloud.user.vo.CrmUserManagerVO;
import com.mall4j.cloud.user.vo.SoldUserStaffRelVo;
import com.mall4j.cloud.user.vo.UserJourneysVO;
import com.mall4j.cloud.user.vo.UserStaffCpRelationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
@Slf4j
@RequiredArgsConstructor
@Service
@RefreshScope
public class UserStaffCpRelationServiceImpl extends ServiceImpl<UserStaffCpRelationMapper,UserStaffCpRelation> implements UserStaffCpRelationService {

    private final UserStaffCpRelationMapper userStaffCpRelationMapper;
    private final StaffFeignClient staffFeignClient;
    private  final UserRegisterSendMsgFeignClient userRegisterSendMsgFeignClient;
    private  final UserStaffRelationFollowUpMapper userStaffRelationFollowUpMapper;
    private final UserStaffCpRelationManager userStaffCpRelationManager;
    private final MaterialFeignClient materialFeignClient;
    private final CrmUserTagRelationMapper crmUserTagRelationMapper;
    private final CrmService crmService;
    private final CpCustGroupClient custGroupClient;
    private final SessionFileFeignClient sessionFileFeignClient;
    private final UserGroupService userGroupService;
    @Value("${mall4cloud.user.enterpriseName}")
    private String enterpriseName="";
    private final MapperFacade mapperFacade;

    @Override
    public PageVO<UserStaffCpRelationListVO> pageWithStaff(UserStaffCpRelationSearchDTO searchDTO) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(searchDTO.getPageNum());
        pageDTO.setPageSize(searchDTO.getPageSize());

        if(StrUtil.isNotBlank(searchDTO.getStaffName())){
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffName(searchDTO.getStaffName());
            ServerResponseEntity<List<StaffVO>> serverResponse =  staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            log.info("查询导购ids列表，参数:{},查询结果:{}", JSONObject.toJSONString(searchDTO.getStaffName()),JSONObject.toJSONString(serverResponse));
            if(serverResponse!=null && serverResponse.isSuccess() && CollUtil.isNotEmpty(serverResponse.getData())){
                List<Long> staffIds = serverResponse.getData().stream().map(StaffVO::getId).collect(Collectors.toList());
                searchDTO.setStaffIds(staffIds);
            }else{
                PageVO pageVO = new PageVO();
                pageVO.setList(null);
                pageVO.setPages(0);
                pageVO.setTotal(0L);
                return pageVO;
            }
        }

        getContactChangeType(searchDTO);

        PageVO<UserStaffCpRelationListVO> pageVO = PageUtil.doPage(pageDTO, () -> userStaffCpRelationMapper.pageWithStaff(searchDTO));
        Map<Long,String> stageParentGroupName = new HashMap();
        if (CollectionUtil.isNotEmpty(pageVO.getList())) {

            //获取客户分组信息
            getUserGroup(pageVO.getList(),stageParentGroupName);

            List<Long> staffIds = pageVO.getList().stream().map(UserStaffCpRelationListVO::getStaffId).collect(Collectors.toList());
            ServerResponseEntity<List<StaffVO>> staffVOs = staffFeignClient.getStaffByIds(staffIds);
            ServerResponseEntity.checkResponse(staffVOs);
            Map<Long, StaffVO> StaffMaps = staffVOs.getData().stream().collect(Collectors.toMap(StaffVO::getId,Function.identity()));

            //获取用户标签
            CRMUserTagDto userTagDto=new CRMUserTagDto();
            userTagDto.setUnionIds(pageVO.getList().stream().map(UserStaffCpRelationListVO::getUserUnionId).distinct().collect(Collectors.toList()));
//            Map<String, List<String>>  userTagNames=userStaffCpRelationManager.mergeGetUserTagNams(userTagDto);
            Map<String, Map<String, StringBuilder>>  userTagNames=userStaffCpRelationManager.mergeGetUserTagNams1(userTagDto);

            pageVO.getList().stream().forEach(us -> {
                if(StaffMaps.get(us.getStaffId())!=null){
                    us.setStaffName(StaffMaps.get(us.getStaffId()).getStaffName());
                }
                if(stageParentGroupName.get(us.getStageId())!=null){
                    us.setParentGroupName(stageParentGroupName.get(us.getStageId()));
                }
                us.setEnterpriseName(enterpriseName);
                us.setServiceRelation(2);
                if (us.getServiceStaffId() != null && us.getServiceStaffId() > 0
                        && StaffMaps.containsKey(us.getServiceStaffId())) {
                    StaffVO serviceStaff = StaffMaps.get(us.getServiceStaffId());
                    us.setServiceStaffName(serviceStaff.getStaffName());
                    us.setServiceQiWeiStaffId(serviceStaff.getQiWeiUserId());
                    us.setServiceRelation(serviceStaff.getId()==us.getStaffId().longValue()?1:2);
                }
                //获取用户标签
                if(userTagNames.containsKey(us.getUserUnionId())){
                    us.setTagName(userTagNames.get(us.getUserUnionId()).values().stream().distinct().collect(Collectors.joining(",")));
                }
            });
        }
        return pageVO;
    }

    @Override
    public List<SoldUserStaffRelVo> soldStaffUser(UserStaffCpRelationSearchDTO searchDTO) {
        if(StrUtil.isNotBlank(searchDTO.getStaffName())){
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffName(searchDTO.getStaffName());
            ServerResponseEntity<List<StaffVO>> serverResponse =  staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            log.info("查询导购ids列表，参数:{},查询结果:{}", JSONObject.toJSONString(searchDTO.getStaffName()),JSONObject.toJSONString(serverResponse));
            if(serverResponse!=null && serverResponse.isSuccess() && CollUtil.isNotEmpty(serverResponse.getData())){
                List<Long> staffIds = serverResponse.getData().stream().map(StaffVO::getId).collect(Collectors.toList());
                searchDTO.setStaffIds(staffIds);
            }else{
                return null;
            }
        }

        //处理客户状态筛选条件
        getContactChangeType(searchDTO);

        List<UserStaffCpRelationListVO> list = userStaffCpRelationMapper.pageWithStaff(searchDTO);
        Map<Long,String> stageParentGroupName = new HashMap();

        List<SoldUserStaffRelVo> backList=new ArrayList<>();

        if (CollectionUtil.isNotEmpty(list)) {

            //获取客户分组信息
            getUserGroup(list,stageParentGroupName);

            List<Long> staffIds = list.stream().map(UserStaffCpRelationListVO::getStaffId).collect(Collectors.toList());
            ServerResponseEntity<List<StaffVO>> staffVOs = staffFeignClient.getStaffByIds(staffIds);
            ServerResponseEntity.checkResponse(staffVOs);
            Map<Long, StaffVO> StaffMaps = staffVOs.getData().stream().collect(Collectors.toMap(StaffVO::getId,Function.identity()));

            //获取用户标签
            CRMUserTagDto userTagDto=new CRMUserTagDto();
            userTagDto.setUnionIds(list.stream().map(UserStaffCpRelationListVO::getUserUnionId).distinct().collect(Collectors.toList()));
            Map<String, Map<String, StringBuilder>>  userTagNames=userStaffCpRelationManager.mergeGetUserTagNams1(userTagDto);
            list.stream().forEach(us -> {
                if(StaffMaps.get(us.getStaffId())!=null){
                    us.setStaffName(StaffMaps.get(us.getStaffId()).getStaffName());
                }
                if(stageParentGroupName.get(us.getStageId())!=null){
                    us.setParentGroupName(stageParentGroupName.get(us.getStageId()));
                }
                us.setEnterpriseName(enterpriseName);
                us.setServiceRelation(2);
                if (us.getServiceStaffId() != null && us.getServiceStaffId() > 0
                        && StaffMaps.containsKey(us.getServiceStaffId())) {
                    StaffVO serviceStaff = StaffMaps.get(us.getServiceStaffId());
                    us.setServiceStaffName(serviceStaff.getStaffName());
                    us.setServiceQiWeiStaffId(serviceStaff.getQiWeiUserId());
                    us.setServiceRelation(serviceStaff.getId()==us.getStaffId().longValue()?1:2);
                }
                //获取用户标签
                if(userTagNames.containsKey(us.getUserUnionId())){
                    us.setTagName(userTagNames.get(us.getUserUnionId()).values().stream().distinct().collect(Collectors.joining(",")));
                }

                //处理信息
                SoldUserStaffRelVo soldUserStaffRelVo=mapperFacade.map(us,SoldUserStaffRelVo.class);
                //分组等级
                soldUserStaffRelVo.setGroupName(StrUtil.isNotEmpty(us.getParentGroupName())?us.getParentGroupName():"");
                if(StrUtil.isNotEmpty(us.getParentGroupName()) && StrUtil.isNotEmpty(us.getGroupName())){
                    soldUserStaffRelVo.setGroupName(us.getParentGroupName()+"-"+us.getGroupName());
                }

                if(StrUtil.isNotEmpty(us.getCpRemarkMobiles())){
                    List<String> phones=JSONArray.parseArray(us.getCpRemarkMobiles(),String.class);
                    if(CollUtil.isNotEmpty(phones)){
                        soldUserStaffRelVo.setCpRemarkMobiles(phones.stream().distinct().collect(Collectors.joining(",")));
                    }
                    if(us.getCpRemarkMobiles().equals("[]")){
                        soldUserStaffRelVo.setCpRemarkMobiles("");
                    }
                }

                //企业
                if(Objects.nonNull(us.getType())){
                    if(us.getType()==1){//外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
                        soldUserStaffRelVo.setCpRemarkCorpName(us.getCpRemarkCorpName());
                    }else if(us.getType()==2){
                        soldUserStaffRelVo.setCpRemarkCorpName(us.getCorpFullName());
                    }
                }
                soldUserStaffRelVo.setStateName(ContactChangeTypeEnum.getDescName(us.getContactChangeType()));

                backList.add(soldUserStaffRelVo);
            });

        }


        return backList;
    }


    /**
     * 获取客户分组阶段信息
     * @param relationListVOS
     * @param stageParentGroupName
     */
    private void getUserGroup(List<UserStaffCpRelationListVO> relationListVOS,Map<Long,String> stageParentGroupName){
        List<Long> stageIds = relationListVOS.stream().filter(s->s.getStageId()!=null).map(UserStaffCpRelationListVO::getStageId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(stageIds)){
            List<UserGroup> userGroups = userGroupService.list(new LambdaQueryWrapper<UserGroup>().eq(UserGroup::getIsDelete,0).in(UserGroup::getId,stageIds));
            if(CollUtil.isNotEmpty(userGroups)){
                List<Long> parentStageIds = userGroups.stream().map(UserGroup::getParentId).collect(Collectors.toList());
                Map<Long,UserGroup> parentUserGroups = userGroupService.list(new LambdaQueryWrapper<UserGroup>().in(UserGroup::getId,parentStageIds))
                        .stream().collect(Collectors.toMap(UserGroup::getId, Function.identity()));
                for (UserGroup userGroup : userGroups) {
                    //拿到当前阶段的父级分组对象
                    UserGroup group = parentUserGroups.get(userGroup.getParentId());
                    stageParentGroupName.put(userGroup.getId(),group.getGroupName());
                }
            }
        }
    }

    /**
     * 处理客户状态筛选条件
     * @param searchDTO
     */
    private void getContactChangeType(UserStaffCpRelationSearchDTO searchDTO){
        if(Objects.nonNull(searchDTO.getContactChangeType())){
            if(searchDTO.getContactChangeType()==ContactChangeTypeEnum.NORMAL.getCode()){
                //查询状态正常的客户
                searchDTO.setContactChangeTypes(Arrays.asList(ContactChangeTypeEnum.ADD_EXTERNAL_CONTACT.getCode(),
                        ContactChangeTypeEnum.EDIT_EXTERNAL_CONTACT.getCode(),
                        ContactChangeTypeEnum.ADD_HALF_EXTERNAL_CONTACT.getCode()));
            }else{
                searchDTO.setContactChangeTypes(Arrays.asList(searchDTO.getContactChangeType()));
            }
        }
    }

    @Override
    public List<UserStaffCpRelationListVO> getUserStaffRelBy(UserStaffCpRelationSearchDTO searchDTO) {
        //查询标签下的会员列表的unionid
        if(CollUtil.isNotEmpty(searchDTO.getTagId())){
            List<String> unionIds = crmUserTagRelationMapper.listUnionIdByTagId(searchDTO.getTagId());
            searchDTO.setUnionIds(unionIds);
        }
        List<UserStaffCpRelationListVO> list=userStaffCpRelationMapper.pageWithStaff(searchDTO).stream().distinct().collect(Collectors.toList());
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
//        log.info("getUserStaffRelBy1:{}",JSON.toJSONString(list));
//        //qiWeiUserId 去重复【一个客户会对应多个员工】
//        LinkedHashMap<String, UserStaffCpRelationListVO> userMap = new LinkedHashMap<>();
//        for (UserStaffCpRelationListVO listVO : list) {
//            if(!userMap.containsKey(listVO.getQiWeiUserId())){
//                userMap.put(listVO.getQiWeiUserId(),listVO);
//            }
//        }
//        log.info("getUserStaffRelBy2:{}",JSON.toJSONString(userMap.values()));
//        return new ArrayList<>(userMap.values());
        return new ArrayList<>(list.stream().collect(Collectors.toMap(UserStaffCpRelationListVO::getQiWeiUserId, s->s, (v1,v2)->v2)).values());
    }

    /**
     * 会包含重复的客户，但是客户是对应员工的
     * @param searchDTO
     * @return
     */
    @Override
    public List<UserStaffCpRelationListVO> getUserStaffRelAll(UserStaffCpRelationSearchDTO searchDTO) {
        //查询标签下的会员列表的unionid
        if(CollUtil.isNotEmpty(searchDTO.getTagId())){
            List<String> unionIds = crmUserTagRelationMapper.listUnionIdByTagId(searchDTO.getTagId());
            searchDTO.setUnionIds(unionIds);
        }
        List<UserStaffCpRelationListVO> list=userStaffCpRelationMapper.pageWithStaff(searchDTO).stream().distinct().collect(Collectors.toList());
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public UserStaffCpRelation getByQiWeiStaffIdAndQiWeiUserId(String qiWeiStaffId, String qiWeiUserId) {
        return userStaffCpRelationMapper.getByQiWeiStaffIdAndQiWeiUserId(qiWeiStaffId, qiWeiUserId);
    }

    @Override
    public UserStaffCpRelation getByQiWeiUserId(String qiWeiUserId,Long staffId) {
        return userStaffCpRelationMapper.getByQiWeiUserId(qiWeiUserId, staffId);
    }

    @Override
    public UserStaffCpRelation getByStaffAndUser(Long staffId, Long userId) {
        return userStaffCpRelationMapper.getByStaffAndUser(staffId, userId);
    }

    @Override
    public void bindUserId(String userUnionId, Long userId,Long staffId) {
        userStaffCpRelationMapper.bindUserId(userUnionId, userId);
        //通知biz
        List<UserStaffCpRelation> staffCpRelations = userStaffCpRelationMapper.listByUserIdList(Lists.newArrayList(userId));
        /**
         * 只给注册用户的服务导购发送用户注册通知。
         */
        if(CollectionUtil.isNotEmpty(staffCpRelations)) {
            if(staffId==null){
                return;
            }
            staffCpRelations = staffCpRelations.stream().filter(s->staffId.equals(s.getStaffId())).collect(Collectors.toList());
            if(CollectionUtil.isEmpty(staffCpRelations)){
                return;
            }
            List<String> qiWeiStaffIdList = staffCpRelations.stream().map(UserStaffCpRelation::getQiWeiStaffId).collect(Collectors.toList());
            userRegisterSendMsgFeignClient.userRegisterSuccessNotify(new UserRegisterQiWeiMsgDTO(qiWeiStaffIdList,userId));
        }
    }

    @Override
    public void bindStaffId(String qiWeiStaffId, Long staffId) {
        userStaffCpRelationMapper.bindStaffId(qiWeiStaffId, staffId);
    }

    @Override
    public void deleteById(Long id,String contactChangeType) {
        this.update(new LambdaUpdateWrapper<UserStaffCpRelation>()
                .eq(UserStaffCpRelation::getId,id)
                .set(UserStaffCpRelation::getStatus,3)
                .set(UserStaffCpRelation::getContactChange,contactChangeType)
                .set(UserStaffCpRelation::getContactChangeType,ContactChangeTypeEnum.getCode(contactChangeType))
                );
    }

    @Override
    public UserStaffCpRelationListVO getUserInfoByQiWeiUserId(String qiWeiStaffId, String qiWeiUserId) {
        return userStaffCpRelationMapper.getUserInfoByQiWeiUserId(qiWeiStaffId,qiWeiUserId);
    }

    @Override
    public List<CountNotMemberUsersVO> countNotMemberUsers() {
        return userStaffCpRelationMapper.countNotMemberUsers();
    }

    @Override
    public List<CountNotMemberUsersVO> countNotQiWeiUsers() {
        return userStaffCpRelationMapper.countNotQiWeiUsers();
    }

    /**
     * 根据导购和用户的企业微信ID判断是否存在好友关系，进行校验
     * @param staffId 导购企业微信ID
     * @param userId 会员企业微信ID
     * @return
     */
    @Override
    public GroupPushTaskCpRelationBO getCpRelationDataByStaffCpUserIdAndVipCpUserId(Long staffId, Long userId) {
        return userStaffCpRelationMapper.getCpRelationDataByStaffCpUserIdAndVipCpUserId(staffId, userId);
    }

    @Override
    public CrmUserManagerVO getCrmUserManagerVO(Long relationId) {
        CrmUserManagerVO crmUserManagerVO = new CrmUserManagerVO();
        UserStaffCpRelation userStaffCpRelation = this.getById(relationId);
        Assert.isNull(userStaffCpRelation,"会员信息为空，请检查参数后再试");
        crmUserManagerVO.setUserStaffCpRelation(userStaffCpRelation);
        ServerResponseEntity<StaffVO> serverResponse = staffFeignClient.getStaffById(userStaffCpRelation.getStaffId());
        if(serverResponse==null || serverResponse.isFail() || serverResponse.getData()==null){
            Assert.faild("查询导购失败，请稍后再试。");
        }
        crmUserManagerVO.setStaffVO(serverResponse.getData());
        //todo 查询积分信息 待数云接口ok再调试
//        Double point = userStaffCpRelationManager.getUserPoint(relationId);
        //todo 查询标签信息 待数云接口ok再调试
        List<UserTag> userTags = userStaffCpRelationManager.mergeGetUserTag(relationId,userStaffCpRelation.getQiWeiUserId(),userStaffCpRelation.getUserUnionId());
        crmUserManagerVO.setUserTags(userTags);

        // 今日沟通信息条数 TODO 目前不需要展示，此接口查询慢需要30s左右需要优化
        crmUserManagerVO.setSendSum(0);
        long startTime=new Date().getTime();
        SessionFileDTO dto = new SessionFileDTO();
        dto.setStartTime(DateUtil.beginOfDay(new Date()).toString());
        dto.setEndTime(DateUtil.endOfDay(new Date()).toString());
        dto.setFrom(userStaffCpRelation.getQiWeiStaffId());
        dto.setTolist(userStaffCpRelation.getQiWeiUserId());
        ServerResponseEntity<StaffSessionVO> sessionVOServerResponse = sessionFileFeignClient.getStaffCount(dto);
        if(sessionVOServerResponse!=null && sessionVOServerResponse.isSuccess()){
            crmUserManagerVO.setSendSum(sessionVOServerResponse.getData().getSendSum());
        }
        long endTime=new Date().getTime();

        return crmUserManagerVO;
    }

    @Override
    public void setTag(UserStaffCpRelationSetTagRequest request) {
        if(CollUtil.isNotEmpty(request.getTags())){
            for (CrmUserTags tag : request.getTags()) {
                UpdateUserTagRequest crmRequest = new UpdateUserTagRequest();
                UserTag userTag = new UserTag();
                userTag.setTagId(tag.getTagId());
                userTag.setMark(true);
                if(CollUtil.isNotEmpty(tag.getTagValue())){
                    userTag.setTagValues(tag.getTagValue());
                    userTag.setOperateTagType("REPLACE");
                }

                List<UserTag> userTags = new ArrayList<>();
                userTags.add(userTag);
                crmRequest.setOperateTags(userTags);

                for (Long id : request.getIds()) {
                    UserStaffCpRelation cpRelation = this.getById(id);
                    crmRequest.setUnionId(cpRelation.getUserUnionId());
                    crmService.updateMemberTag(crmRequest);
                }
            }

        }

    }

    @Override
    public void setStage(UserStaffCpRelationSetTagRequest request) {
        for (Long id : request.getIds()) {
            UserStaffCpRelation relation=new UserStaffCpRelation();
            relation.setStageId(request.getStageId());
            relation.setId(id);
            relation.setUpdateTime(new Date());
            relation.setUpdateBy(AuthUserContext.get().getUsername()+"批量转移分组");
            this.updateById(relation);
        }
    }

    @Override
    public void removeStage(UserStaffCpRelationSetTagRequest request) {
        userStaffCpRelationMapper.removeUserStage(request.getIds());
    }

    @Override
    public CrmUserManagerVO getMobileCrmUserManagerVO(Long relationId) {
        CrmUserManagerVO crmUserManagerVO = this.getCrmUserManagerVO(relationId);

        //阶段名称 分组名称
        if(crmUserManagerVO.getUserStaffCpRelation().getStageId() != null){
            long start=new Date().getTime();
            UserGroup userGroup = userGroupService.getById(crmUserManagerVO.getUserStaffCpRelation().getStageId());
            log.info("开始获取阶段名称 分组名称 userGroup:{}", JSON.toJSONString(userGroup));
            if(userGroup!=null && !DeleteEnum.offlineOrDelete(userGroup.getIsDelete())){
                crmUserManagerVO.setStageName(userGroup.getGroupName());
                if(userGroup.getParentId()!=null){
                    UserGroup parentGroup = userGroupService.getById(userGroup.getParentId());
                    crmUserManagerVO.setGroupName(parentGroup!=null?parentGroup.getGroupName():"");
                    crmUserManagerVO.setGroupId(parentGroup!=null?parentGroup.getId():null);
                }
            }
            long end=new Date().getTime();
            log.info("结束开始获取阶段名称 分组名称",((end - start) / 1000.0));
        }

        //企业好友列表
        crmUserManagerVO.setUserStaffCpRelationCount(userStaffCpRelationMapper.getCountByUserUnionId(crmUserManagerVO.getUserStaffCpRelation().getUserUnionId()));

        //TODO 所加群聊信息
        ServerResponseEntity<Integer> responseEntity=custGroupClient.countCustGroupByQwUserId(crmUserManagerVO.getUserStaffCpRelation().getQiWeiUserId());
        ServerResponseEntity.checkResponse(responseEntity);
        crmUserManagerVO.setUserJoinCustGroupCount(responseEntity.getData());
//        log.info("开始获取所加群聊信息");
//        long start=new Date().getTime();
//        ServerResponseEntity<List<UserJoinCustGroupVO>> custGroupResponse = custGroupClient.findCustGroupByQwUserId(crmUserManagerVO.getUserStaffCpRelation().getQiWeiUserId());
//        if(custGroupResponse!=null && custGroupResponse.isSuccess()){
//            crmUserManagerVO.setUserJoinCustGroupVOs(custGroupResponse.getData());
//        }
//        long end=new Date().getTime();
//        log.info("结束获取所加群聊信息",((end - start) / 1000.0));

        return crmUserManagerVO;
    }

    /**
     * 根据客户获取企业好友列表
     * @return
     */
    @Override
    public PageVO<UserStaffCpRelationVO> pageStaffByUser(UserStaffCpRelationSearchDTO searchDTO) {
        if(Objects.isNull(searchDTO.getRelationId())){
            throw new LuckException("客户ID不能为空");
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(searchDTO.getPageNum());
        pageDTO.setPageSize(searchDTO.getPageSize());
        UserStaffCpRelation userStaffCpRelation = this.getById(searchDTO.getRelationId());
        if(Objects.isNull(userStaffCpRelation)){
            throw new LuckException("客户不存在");
        }
        PageVO<UserStaffCpRelationVO> pageVO = PageUtil.doPage(pageDTO, () -> userStaffCpRelationMapper.getByUserUnionId(userStaffCpRelation.getUserUnionId()));
        List<Long> staffIds =pageVO.getList().stream().map(UserStaffCpRelationVO::getStaffId).collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> staffVOs = staffFeignClient.getStaffByIds(staffIds);
        Map<Long, StaffVO> StaffMaps = staffVOs.getData().stream().collect(Collectors.toMap(StaffVO::getId,Function.identity()));
        for (UserStaffCpRelationVO userStaffCpRelationVO : pageVO.getList()) {
            userStaffCpRelationVO.setAvatar(null);
            String userId=userStaffCpRelationVO.getQiWeiUserId();
            String staffUserId=userStaffCpRelationVO.getQiWeiStaffId();
            if(StaffMaps.containsKey(userStaffCpRelationVO.getStaffId())){
                StaffVO staffVO = StaffMaps.get(userStaffCpRelationVO.getStaffId());
                userStaffCpRelationVO.setStaffName(staffVO.getStaffName());
                userStaffCpRelationVO.setStaffPosition(staffVO.getPosition());
                userStaffCpRelationVO.setQiWeiUserId(staffVO.getQiWeiUserId());
                userStaffCpRelationVO.setAvatar(staffVO.getAvatar());
            }
            //todo 最近联系时间
            SessionFileDTO fileDTO = new SessionFileDTO();
            fileDTO.setFrom(userId);
            fileDTO.setTolist(staffUserId);
            ServerResponseEntity<Date> lastTime = sessionFileFeignClient.getLastTime(fileDTO);
            userStaffCpRelationVO.setLastContactTime(lastTime.getData());

        }

        return pageVO;
    }

    /**
     * 员工离职
     * @param staffIds
     */
    @Async
    @Override
    public void staffDimission(List<Long> staffIds) {
        if(CollUtil.isEmpty(staffIds)){
            return;
        }
        log.info("员工离职更新好友关系 staffDimission---",JSON.toJSONString(staffIds));
        LambdaUpdateWrapper<UserStaffCpRelation> lambdaUpdateWrapper= new LambdaUpdateWrapper<UserStaffCpRelation>();
        lambdaUpdateWrapper.eq(UserStaffCpRelation::getStatus,1);
        lambdaUpdateWrapper.in(UserStaffCpRelation::getStaffId, staffIds);
        List<UserStaffCpRelation> relations=userStaffCpRelationMapper.selectList(lambdaUpdateWrapper);
        if(CollUtil.isNotEmpty(relations)){
            List<UserStaffCpRelation> updates=new ArrayList<>();
            for (UserStaffCpRelation relation : relations) {
                UserStaffCpRelation update=new UserStaffCpRelation();
                update.setId(relation.getId());
                update.setUpdateBy("绑定员工离职");
                update.setUpdateTime(new Date());
                update.setStatus(StaffUserRelStatusEnum.STAFF_DIMISSION.getCode());
                updates.add(update);
            }
            this.updateBatchById(updates);
        }
    }

    @Override
    public List<UserJourneysVO> journeys(UserJourneysDTO request,Long adminStaffId) {
        List<UserJourneysVO> userJourneysVOList = new ArrayList<>();
        // 1电话 2邮件 3企微会话 4短信 5跟进记录 6美洽 7修改跟进 8行为轨迹
        if(CollectionUtil.isEmpty(request.getType()) || request.getType().contains(UserJourneyEnum.PHONE.value())){
            buildUserPhoneJourneys(request,userJourneysVOList);
        }
        if(CollectionUtil.isEmpty(request.getType()) || request.getType().contains(UserJourneyEnum.MAILBOX.value())){
            buildUserMailBoxJourneys(request,userJourneysVOList);
        }
        if(CollectionUtil.isEmpty(request.getType()) || request.getType().contains(UserJourneyEnum.CP_CHATS.value())){
            buildUserCpChatsJourneys(request,userJourneysVOList);
        }
        if(CollectionUtil.isEmpty(request.getType()) || request.getType().contains(UserJourneyEnum.SMS.value())){
            buildUserSmsJourneys(request,userJourneysVOList);
        }
        if(CollectionUtil.isEmpty(request.getType()) || request.getType().contains(UserJourneyEnum.FOLLOW_UP.value())){
            buildUserFollowUpJourneys(request,userJourneysVOList,adminStaffId);
        }
        if(CollectionUtil.isEmpty(request.getType()) || request.getType().contains(UserJourneyEnum.MEIQIA.value())){
            buildUserMeiQiaJourneys(request,userJourneysVOList);
        }
        if(CollectionUtil.isEmpty(request.getType()) || request.getType().contains(UserJourneyEnum.UPDATE_FOLLOW_UP.value())){
            buildUserUpdateFollowUpJourneys(request,userJourneysVOList,adminStaffId);
        }
        if(CollectionUtil.isEmpty(request.getType()) || request.getType().contains(UserJourneyEnum.BEHAVIORAL.value())){
            buildUserBehavioraLJourneys(request,userJourneysVOList);
        }

        //集合倒序排序
        if(CollectionUtil.isNotEmpty(userJourneysVOList)){
            userJourneysVOList = userJourneysVOList.stream().sorted(Comparator.comparing(UserJourneysVO::getRecordTime).reversed()).collect(Collectors.toList());
        }
        return userJourneysVOList;
    }

    /**
     * 修改跟进
     * @param request
     * @param userJourneysVOList
     */
    private void buildUserUpdateFollowUpJourneys(UserJourneysDTO request, List<UserJourneysVO> userJourneysVOList,Long adminStaffId) {
        UserStaffCpRelation relation=this.getById(request.getRelationId());
        request.setUnionId(relation.getUserUnionId());
        List<UserStaffRelationFollowUp> followUps = userStaffRelationFollowUpMapper.updateFollwUpList(request);

        for (UserStaffRelationFollowUp followUp : followUps) {
            UserJourneysVO journeysVO = new UserJourneysVO();
            journeysVO.setType(UserJourneyEnum.UPDATE_FOLLOW_UP.value());
            journeysVO.setRecordTime(followUp.getCreateTime().getTime());
            journeysVO.setData(followUp);
            if(Objects.nonNull(adminStaffId)){
                if(followUp.getCreateById().toString().equals(adminStaffId.toString())){
                    journeysVO.setEditStatus(1);
                }
            }
            userJourneysVOList.add(journeysVO);
        }
    }

    /**
     * 美恰
     * @param request
     * @param userJourneysVOList
     */
    private void buildUserMeiQiaJourneys(UserJourneysDTO request, List<UserJourneysVO> userJourneysVOList) {
    }

    public static void main(String[] s ){
        String endTime=DateUtil.format(DateUtil.parse("2024-02-21 23:59:59","yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd");
        String nowTime=DateUtil.format(new Date(),"yyyy-MM-dd");

        System.out.println(endTime);
        System.out.println(nowTime);

        if(nowTime.equals(endTime)){
            DateTime offDate=DateUtil.offsetDay(new Date(),-1);
            System.out.println(offDate);
            System.out.println(DateUtil.format(DateUtil.endOfDay(offDate),"yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 企微会话记录
     * @param request
     * @param userJourneysVOList
     */
    private void buildUserCpChatsJourneys(UserJourneysDTO request, List<UserJourneysVO> userJourneysVOList) {
        UserStaffCpRelation cpRelation = this.getById(request.getRelationId());
        // 今日沟通信息条数
        SessionFileDTO dto = new SessionFileDTO();
        dto.setStartTime(request.getStartTime());
        //当前数据不返回
        String endTime=DateUtil.format(DateUtil.parse(request.getEndTime(),"yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd");
        String nowTime=DateUtil.format(new Date(),"yyyy-MM-dd");
        String offsetTime="";
        if(nowTime.equals(endTime)){
            DateTime offDate=DateUtil.offsetDay(new Date(),-1);
            offsetTime=DateUtil.format(DateUtil.endOfDay(offDate),"yyyy-MM-dd HH:mm:ss");
            dto.setEndTime(offsetTime);
        }else{
            dto.setEndTime(request.getEndTime());
        }
        log.info("旅程企微会话记录-->nowTime:{} endTime:{} offsetTime:{}",nowTime,endTime,offsetTime);

        dto.setFrom(cpRelation.getQiWeiUserId());
        ServerResponseEntity<List<StaffSessionVO>> sessionVOServerResponse = sessionFileFeignClient.getUserAndStaffCount(dto);
        if(sessionVOServerResponse!=null && sessionVOServerResponse.isSuccess() && CollUtil.isNotEmpty(sessionVOServerResponse.getData())){
//            //发送人userid列表
//            List<String> tolist = sessionVOServerResponse.getData().stream().map(StaffSessionVO::getTolist).distinct().collect(Collectors.toList());
//            //接受人userid列表
//            List<String> fromlist = sessionVOServerResponse.getData().stream().map(StaffSessionVO::getFrom).distinct().collect(Collectors.toList());
//            //合并两个集合并排除掉用户的userid，剩下的全部都是员工的userid
//            List<String> yungongs = new ArrayList<>();
//            yungongs.addAll(tolist);
//            yungongs.addAll(fromlist);
//            yungongs = yungongs.stream().filter(s -> !StrUtil.equals(s,cpRelation.getQiWeiUserId())).collect(Collectors.toList());

            for (StaffSessionVO datum : sessionVOServerResponse.getData()) {
                String yuangongUserId = "";
                //如果发送人为客户。 则接收人为员工
                if(datum.getFrom().contains(cpRelation.getQiWeiUserId())){
                    //接收人数据格式为数组。且这里接收人只有一个
                    yuangongUserId = JSONArray.parseObject(datum.getTolist(),String[].class)[0];
                }else{
                    yuangongUserId = datum.getFrom();
                }
                ServerResponseEntity<StaffVO> staffVOServerResponse = staffFeignClient.getStaffByQiWeiUserId(yuangongUserId);
                StaffVO staffVO = staffVOServerResponse.getData();

                String chatScript = StrUtil.format("{},会话数量{}条。",staffVO.getStaffName(),datum.getSendCount());

                UserJourneysVO userJourneysVO = new UserJourneysVO();
                userJourneysVO.setRecordTime(DateUtil.parse(datum.getCreateTime()+" 23:59:59").getTime());
                userJourneysVO.setType(UserJourneyEnum.CP_CHATS.value());

                Map<String,String> map = new HashMap<>();
                map.put("content",chatScript);
                map.put("time",datum.getCreateTime()+" 23:59:59");
                map.put("qiWeiUserId",staffVO.getQiWeiUserId());
                userJourneysVO.setData(map);
                userJourneysVOList.add(userJourneysVO);
            }
        }
    }

    /**
     * 行为轨迹
     * @param request
     * @param userJourneysVOList
     */
    private void buildUserBehavioraLJourneys(UserJourneysDTO request, List<UserJourneysVO> userJourneysVOList) {
        UserStaffCpRelation cpRelation = this.getById(request.getRelationId());
        //查询素材浏览记录表
        ServerResponseEntity<List<MaterialBrowseRecordApiVO>> serverResponse = materialFeignClient.getBrowseRecord(cpRelation.getUserUnionId(),request.getStartTime(),request.getEndTime());
        if(serverResponse==null || serverResponse.isFail() ){
            Assert.faild("行为轨迹查询异常，请稍后再试。");
        }

        List<Long> staffIds = serverResponse.getData().stream().map(MaterialBrowseRecordApiVO::getStaffId).collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> staffVOs = staffFeignClient.getStaffByIds(staffIds);
        Map<Long, StaffVO> StaffMaps = staffVOs.getData().stream().collect(Collectors.toMap(StaffVO::getId,Function.identity()));

        for (MaterialBrowseRecordApiVO datum : serverResponse.getData()) {

            if(StaffMaps.get(datum.getStaffId())!=null){
                datum.setStaffName(StaffMaps.get(datum.getStaffId()).getStaffName());
            }
            UserJourneysVO userJourneysVO = new UserJourneysVO();
            userJourneysVO.setData(datum);
            userJourneysVO.setType(UserJourneyEnum.BEHAVIORAL.value());
            userJourneysVO.setRecordTime(datum.getCreateTime().getTime());

            userJourneysVOList.add(userJourneysVO);
        }
    }

    /**
     * 短信记录
     * @param request
     * @param userJourneysVOList
     */
    private void buildUserSmsJourneys(UserJourneysDTO request, List<UserJourneysVO> userJourneysVOList) {
    }

    /**
     * 邮箱记录
     * @param request
     * @param userJourneysVOList
     */
    private void buildUserMailBoxJourneys(UserJourneysDTO request, List<UserJourneysVO> userJourneysVOList) {
    }

    /**
     * 电话记录
     * @param request
     * @param userJourneysVOList
     */
    private void buildUserPhoneJourneys(UserJourneysDTO request, List<UserJourneysVO> userJourneysVOList) {
    }

    /**
     * 用户跟进记录
     * @param request
     * @param userJourneysVOList
     */
    private void buildUserFollowUpJourneys(UserJourneysDTO request, List<UserJourneysVO> userJourneysVOList,Long adminStaffId) {
        UserStaffCpRelation relation=this.getById(request.getRelationId());
        request.setUnionId(relation.getUserUnionId());
        List<UserStaffRelationFollowUp> followUps = userStaffRelationFollowUpMapper.follwUpList(request);

        for (UserStaffRelationFollowUp followUp : followUps) {
            UserJourneysVO journeysVO = new UserJourneysVO();
            journeysVO.setType(UserJourneyEnum.FOLLOW_UP.value());
            journeysVO.setRecordTime(followUp.getCreateTime().getTime());
            journeysVO.setData(followUp);
            if(Objects.nonNull(adminStaffId)){
                if(followUp.getCreateById().toString().equals(adminStaffId.toString())){
                    journeysVO.setEditStatus(1);
                }
            }
            userJourneysVOList.add(journeysVO);
        }
    }
}
