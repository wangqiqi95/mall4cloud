package com.mall4j.cloud.user.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
import com.mall4j.cloud.api.biz.feign.CrmFeignClient;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.constant.ContactChangeTypeEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserStaffCpRelationPhoneService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import com.xxl.job.core.handler.annotation.XxlJob;
import me.chanjar.weixin.cp.bean.external.contact.ExternalContact;
import me.chanjar.weixin.cp.bean.external.contact.FollowedUser;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactBatchInfo;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 同步历史客户与联系人数据
 */
@Component
public class UserStaffRelationTask {

    private static final Logger log = LoggerFactory.getLogger(UserStaffRelationTask.class);
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private WxCpApiFeignClient wxCpApiFeignClient;
    @Autowired
    private UserStaffCpRelationService userStaffCpRelationService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStaffCpRelationPhoneService relationPhoneService;
    @Autowired
    private CrmFeignClient crmFeignClient;


    /**
     * 同步历史客户与联系人数据
     */
    @XxlJob("syncUserStaffRelation")
    public void syncUserStaffRelation(){
        log.info("==============同步历史客户与联系人数据开始");
        // 获取企业微信群发任务结果进行同步
        Long startTime=System.currentTimeMillis();
        StaffQueryDTO queryDTO=new StaffQueryDTO();
        queryDTO.setStatus(0);
        queryDTO.setQiweiUserStatus(1);
        ServerResponseEntity<List<StaffVO>>  responseEntity=staffFeignClient.findByStaffQueryDTO(queryDTO);
        if(responseEntity.isFail() || CollUtil.isEmpty(responseEntity.getData())){
            log.info("==============同步历史客户与联系人数据失败，未获取到员工数据1");
        }
        List<StaffVO> staffVOS=responseEntity.getData().stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId())).collect(Collectors.toList());
        if(CollUtil.isEmpty(staffVOS)){
            log.info("==============同步历史客户与联系人数据失败，未获取到员工数据2");
        }
        List<UserStaffCpRelation> relations=new ArrayList<>();//内部保存
        Map<String,WxCpExternalContactInfo> contactInfoMaps=new HashMap<>();
        for (StaffVO staffVO : staffVOS) {
            List<WxCpExternalContactInfo> contactInfos=batchExternalcontactList(staffVO.getQiWeiUserId());
            if(CollUtil.isEmpty(contactInfos)){
                log.info("==============同步历史客户与联系人数据失败，根据员工id:【{}】未获取到外部联系人数据",staffVO.getQiWeiUserId());
                continue;
            }
            //获取员工好友关系数据
            Map<String,UserStaffCpRelation> relationMap=getUserStaffCpRelations(staffVO.getId());
            for (WxCpExternalContactInfo contactInfo : contactInfos) {
                if(!contactInfoMaps.containsKey(contactInfo.getExternalContact().getExternalUserId())){
                    contactInfoMaps.put(contactInfo.getExternalContact().getExternalUserId(),contactInfo);
                }
                //匹配系统是否存在好友关系数据
                UserStaffCpRelation userStaffCpRelation = relationMap.get(contactInfo.getExternalContact().getExternalUserId());

                //客户信息
                ExternalContact externalContact=contactInfo.getExternalContact();
                //客户备注信息
                FollowedUser followedUser=this.getFollowUserByUserId(staffVO.getQiWeiUserId(),contactInfo);

                if(Objects.isNull(userStaffCpRelation)){
                    //保存
                    userStaffCpRelation = new UserStaffCpRelation();
                    userStaffCpRelation.setAutoType(1);
                    userStaffCpRelation.setCreateTime(new Date());
                    userStaffCpRelation.setCpCreateTime(new Date());
                }
                userStaffCpRelation.setStaffId(staffVO.getId());
                userStaffCpRelation.setQiWeiStaffId(staffVO.getQiWeiUserId());
                userStaffCpRelation.setQiWeiUserId(externalContact.getExternalUserId());
                userStaffCpRelation.setUserUnionId(externalContact.getUnionId());
                userStaffCpRelation.setStatus(1);
                userStaffCpRelation.setUpdateTime(new Date());
                userStaffCpRelation.setType(externalContact.getType());
                userStaffCpRelation.setQiWeiNickName(externalContact.getName());
                userStaffCpRelation.setContactChange("add_external_contact");
                userStaffCpRelation.setContactChangeType(ContactChangeTypeEnum.getCode(userStaffCpRelation.getContactChange()));
                userStaffCpRelation.setAvatar(externalContact.getAvatar());
                userStaffCpRelation.setCorpName(StrUtil.isNotEmpty(externalContact.getCorpName())?externalContact.getCorpName():null);
                userStaffCpRelation.setCorpFullName(StrUtil.isNotEmpty(externalContact.getCorpFullName())?externalContact.getCorpFullName():null);
                userStaffCpRelation.setGender(externalContact.getGender());
                if(Objects.nonNull(followedUser)){
                    userStaffCpRelation.setCpRemark(followedUser.getRemark());
                    userStaffCpRelation.setCpDescription(followedUser.getDescription());
                    userStaffCpRelation.setCpRemarkMobiles(Objects.nonNull(followedUser.getRemarkMobiles())?JSON.toJSONString(followedUser.getRemarkMobiles()):null);
                    userStaffCpRelation.setCpState(followedUser.getState());
                    userStaffCpRelation.setCpOperUserId(followedUser.getOperatorUserId());
                    userStaffCpRelation.setCpAddWay(followedUser.getAddWay());
                    userStaffCpRelation.setCpRemarkCorpName(StrUtil.isNotEmpty(followedUser.getRemarkCorpName())?followedUser.getRemarkCorpName():null);
                    if(Objects.nonNull(followedUser.getCreateTime())){
                        userStaffCpRelation.setCpCreateTime(formatDate(followedUser.getCreateTime().toString()));
                    }
                }
                relations.add(userStaffCpRelation);
            }
        }
        if(CollUtil.isNotEmpty(relations)){
            userStaffCpRelationService.saveOrUpdateBatch(relations);
            log.info("==============同步历史客户与联系人数据，需要保存数据行数：【{}】",relations.size());

            List<List<UserStaffCpRelation>> list = Lists.partition(relations, 100);
            for (List<UserStaffCpRelation> userStaffCpRelations : list) {
                for (UserStaffCpRelation relation : userStaffCpRelations) {
                    //保存手机号关联
                    relationPhoneService.saveTo(relation.getId(),
                            relation.getQiWeiUserId(),
                            relation.getStaffId(),
                            relation.getQiWeiStaffId(), relation.getCpRemarkMobiles(),
                            relation.getStatus());

                    userService.createUserByUnionId(relation);//同时注册用户表: user 单个处理
                }
                userService.batchCreateUserByUnionId(userStaffCpRelations.stream()
                        .filter(item->StrUtil.isNotEmpty(item.getUserUnionId())).collect(Collectors.toList()));//同时注册用户表: user 批量处理
            }

        }
        if(CollUtil.isNotEmpty(contactInfoMaps)){
            List<WxCpExternalContactInfo> contactInfos=new ArrayList<>(contactInfoMaps.values());//需要推送数云
            //TODO 需要同步给数云
            log.info("历史好友关系同步给数云：{}",JSON.toJSONString(contactInfos));
            for (WxCpExternalContactInfo contactInfo : contactInfos) {
                log.info("历史好友关系同步给数云：{}",contactInfo.toString());
                PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
                dto.setMsgType("3");
                dto.setChangetype("add_external_contact");
                dto.setWxCpExternalContactInfo(contactInfo);
                crmFeignClient.pushCDPCpMsg(dto);
            }

        }
        log.info("==============同步历史客户与联系人数据结束，耗时: {}s",System.currentTimeMillis() - startTime);
    }

    public Map<String,UserStaffCpRelation> getUserStaffCpRelations(Long staffId){
        LambdaQueryWrapper<UserStaffCpRelation> lambdaQueryWrapper=new LambdaQueryWrapper<UserStaffCpRelation>();
        lambdaQueryWrapper.eq(UserStaffCpRelation::getStatus,1);
        lambdaQueryWrapper.eq(UserStaffCpRelation::getStaffId,staffId);
        List<UserStaffCpRelation> list=userStaffCpRelationService.list(lambdaQueryWrapper);
        return list.stream().collect(Collectors.toMap(UserStaffCpRelation::getQiWeiUserId, s->s, (v1, v2)->v2));
    }

    public List<WxCpExternalContactInfo> batchExternalcontactList(String userId) {
        try {
            List<WxCpExternalContactInfo> externalContactInfos=new ArrayList<>();
            String cursor="first";//上次请求返回的next_cursor【非必填 企微API文档2023/05/19更新】
            int limit=100;//最大获取100
            do {
                if (("first").equals(cursor)) {
                    cursor = "";
                }
                ServerResponseEntity<WxCpExternalContactBatchInfo> response = wxCpApiFeignClient.batchExternalcontactList(userId,cursor,limit);
                if(response.isFail() || Objects.isNull(response.getData())){
                    log.info("==============同步历史客户与联系人数据失败，根据员工id:【{}】未获取到外部联系人数据",userId);
                    continue;
                }
                WxCpExternalContactBatchInfo contactDetailBatch=response.getData();
                for (WxCpExternalContactBatchInfo.ExternalContactInfo externalContactInfo : contactDetailBatch.getExternalContactList()) {
                    WxCpExternalContactInfo cpExternalContactInfo=new WxCpExternalContactInfo();
                    cpExternalContactInfo.setExternalContact(externalContactInfo.getExternalContact());
                    cpExternalContactInfo.setFollowedUsers(Arrays.asList(externalContactInfo.getFollowInfo()));
                    cpExternalContactInfo.setNextCursor(contactDetailBatch.getNextCursor());
                    externalContactInfos.add(cpExternalContactInfo);
                }
                cursor=contactDetailBatch.getNextCursor();
                log.info("batchExternalcontactList->userId:{} cursor:{} 数据行数:{}",userId,cursor,externalContactInfos.size());
                if(StrUtil.isEmpty(cursor)){
                    break;
                }
            }while (StrUtil.isNotEmpty(cursor));
            return externalContactInfos;
        }catch (Exception e){
            log.info("batchExternalcontactList error: {} {}",e,e.getMessage());
        }
        return null;

    }

    /**
     * 根据当前员工获取外部联系人对应备注信息
     * @param userId
     * @param wxCpExternalContactInfo
     */
    private FollowedUser getFollowUserByUserId(String userId, WxCpExternalContactInfo wxCpExternalContactInfo){
        if(CollUtil.isNotEmpty(wxCpExternalContactInfo.getFollowedUsers())){
            Map<String,FollowedUser> followedUserMap= LambdaUtils.toMap(wxCpExternalContactInfo.getFollowedUsers(), FollowedUser::getUserId);
            return followedUserMap.get(userId);
        }
        return null;
    }

    public static Date formatDate(String createTime) {
        if(StrUtil.isEmpty(createTime) || createTime.equals("0")){
            return null;
        }
        // 将微信传入的CreateTime转换成long类型，再乘以1000
        long msgCreateTime = Long.parseLong(createTime) * 1000L;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Date(msgCreateTime);
    }

}
