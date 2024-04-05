//package com.mall4j.cloud.biz.manager;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.mall4j.cloud.api.biz.constant.PushCrmCpTypeEnum;
//import com.mall4j.cloud.api.user.constant.BuildTagFromEnum;
//import com.mall4j.cloud.api.user.crm.CrmMethod;
//import com.mall4j.cloud.api.user.crm.model.UpdateTag;
//import com.mall4j.cloud.api.user.crm.model.UpdateTagModel;
//import com.mall4j.cloud.api.user.crm.request.UpdateUserTagRequest;
//import com.mall4j.cloud.api.user.crm.response.CrmResult;
//import com.mall4j.cloud.api.user.crm.response.UserTag;
//import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
//import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
//import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
//import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
//import com.mall4j.cloud.biz.config.WxCpConfiguration;
//import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
//import com.mall4j.cloud.api.biz.dto.crm.*;
//import com.mall4j.cloud.biz.util.crm.CrmClients;
//import com.mall4j.cloud.common.response.ServerResponseEntity;
//import com.mall4j.cloud.common.util.Json;
//import lombok.extern.slf4j.Slf4j;
//import ma.glasnost.orika.MapperFacade;
//import me.chanjar.weixin.common.error.WxErrorException;
//import me.chanjar.weixin.cp.api.WxCpService;
//import me.chanjar.weixin.cp.bean.WxCpDepart;
//import me.chanjar.weixin.cp.bean.WxCpUser;
//import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
//import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
//import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * 对接数云-企微数据接收
// */
//@Slf4j
//@Component
//@RefreshScope
//public class CrmManager {
//
//    @Value("${scrm.biz.openPushCrmCpMsg}")
//    private boolean openPushCrmCpMsg=false;
//
//    @Autowired
//    private MapperFacade mapperFacade;
//    @Autowired
//    private WxCpService wxCpService;
//    @Autowired
//    private CrmUserFeignClient crmUserFeignClient;
//    @Autowired
//    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
//    @Autowired
//    private WeixinCpExternalManager weixinCpExternalManager;
//
//
//    /**
//     * 数云打标签
//     * 打标签动作枚举：BuildTagFromEnum
//     */
//    @Async
//    public void updateUserTagByCrm(UpdateTagModel updateTagModel,Integer buildTagFrom){
//        log.info("给客户打标签来源：{} 标签数据:{}", BuildTagFromEnum.getDesc(buildTagFrom),JSON.toJSONString(updateTagModel));
//        if(CollUtil.isEmpty(updateTagModel.getQiWeiUserIds()) && CollUtil.isEmpty(updateTagModel.getQiWeiUserUnionIds())){
//            log.info("给客户打标签失败,客户对象为空");
//            return;
//        }
//        if(StrUtil.isEmpty(updateTagModel.getTags())){
//            log.info("给客户打标签失败,标签内容为空");
//            return;
//        }
//        //换取客户unionId
//        List<String> unionIds=updateTagModel.getQiWeiUserUnionIds();
//        if(CollUtil.isEmpty(updateTagModel.getQiWeiUserUnionIds())){
//            UserStaffCpRelationSearchDTO dto=new UserStaffCpRelationSearchDTO();
//            dto.setPageNum(1);
//            dto.setPageSize(10);
//            dto.setQiWeiUserIds(updateTagModel.getQiWeiUserIds());
//            dto.setStatus(1);
//            ServerResponseEntity<List<UserStaffCpRelationListVO>> serverResponseEntity=userStaffCpRelationFeignClient.getUserStaffRelBy(dto);
//            if(serverResponseEntity.isFail() || CollUtil.isEmpty(serverResponseEntity.getData())){
//                log.info("给客户打标签失败,根据unionId换取userId失败");
//                return;
//            }
//            unionIds=serverResponseEntity.getData().stream().map(item->item.getUserUnionId()).collect(Collectors.toList());
//        }
//        if(CollUtil.isEmpty(unionIds)){
//            log.info("给客户打标签失败,未获取对应客户信息");
//            return;
//        }
//        for (String unionId :unionIds) {
//            UpdateUserTagRequest request=new UpdateUserTagRequest();
//            request.setUnionId(unionId);
//            request.setBuildTagFrom(buildTagFrom);
//            List<UpdateTag> updateTags= JSONArray.parseArray(updateTagModel.getTags(),UpdateTag.class);
//            List<UserTag> userTags=new ArrayList<>();
//            for (UpdateTag updateTag : updateTags) {
//                UserTag userTag=new UserTag();
//                userTag.setTagId(updateTag.getTagId());
//                userTag.setName(updateTag.getTagName());
//                userTag.setTagValues(updateTag.getTagValues());
//                userTag.setMark(true);
//                userTag.setOperateTagType("REPLACE");
//                userTags.add(userTag);
//            }
//            request.setOperateTags(userTags);
//            try {
//                ServerResponseEntity responseEntity=crmUserFeignClient.updateMemberTag(request);
//                log.info("给客户打标签内容【{}】，执行结果： {}",JSON.toJSONString(request),responseEntity.getMsg());
//            }catch (Exception e){
//                log.info("给客户打标签内容【{}】，执行失败： {}",JSON.toJSONString(request),e);
//            }
//        }
//
//    }
//
//    @Async
//    public void testPush(String msgType,WxCpXmlMessage wxMessage){
//
//        log.info("对接数云-企微数据接收 开启推送状态：{}",openPushCrmCpMsg);
//        if(!openPushCrmCpMsg){
//            return;
//        }
//
//        PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
//        dto.setMsgType(msgType);
//        dto.setWxMessage(wxMessage);
//        dto.setChangetype(wxMessage.getChangeType());
//        if(msgType.equals(PushCrmCpTypeEnum.DEPART.getValue())){//外部联系人数据
//            dto.setDepart(getWxCpDepart(wxMessage.getId()));
//        }
//        if(msgType.equals(PushCrmCpTypeEnum.STAFF.getValue())){//外部联系人数据
//            dto.setWxCpUser(getWxCpUser(wxMessage.getUserId()));
//        }
//        if(msgType.equals(PushCrmCpTypeEnum.EXTERNAL.getValue())){//外部联系人数据
//            WxCpExternalContactInfo wxCpExternalContactInfo=weixinCpExternalManager.getWxCpExternalContactInfo(wxMessage.getUserId(),wxMessage.getExternalUserId());
//            dto.setWxCpExternalContactInfo(wxCpExternalContactInfo);
//        }
//        if(msgType.equals(PushCrmCpTypeEnum.CUST_GROUP.getValue())){//群聊数据
//            WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(wxMessage.getChatId());
//            dto.setGroupChat(groupChat);
//        }
//        pushCDPCpMsgDate(dto);
//    }
//
//    public void pushCDPCpMsg(PushCDPCpMsgEventDTO dto){
//        pushCDPCpMsgDate(dto);
//    }
//
//
//    @Async
//    public void pushCDPCpMsgDate(PushCDPCpMsgEventDTO dto){
//
//        log.info("对接数云-企微数据接收 开启推送状态：{}",openPushCrmCpMsg);
//        if(!openPushCrmCpMsg){
//            return;
//        }
//
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("type",dto.getMsgType());
//        if(dto.getMsgType().equals(PushCrmCpTypeEnum.DEPART.getValue())){ //部门数据
//            CpCrmDepart cpDepart=new CpCrmDepart();
//            cpDepart.setDepartmentEnName(dto.getDepart().getEnName());
//            cpDepart.setDepartmentName(dto.getDepart().getName());
//            cpDepart.setChange_type(dto.getChangetype());
//            cpDepart.setDepartmentId(dto.getDepart().getId());
//            cpDepart.setParentId(dto.getDepart().getParentId());
//            cpDepart.setOrder(dto.getDepart().getOrder());
//            jsonObject.put("data",cpDepart);
//        }
//        if(dto.getMsgType().equals(PushCrmCpTypeEnum.STAFF.getValue())){ //员工数据
//            CpCrmWxUser cpCrmWxUser=new CpCrmWxUser();
//            cpCrmWxUser.setUserId(dto.getWxCpUser().getUserId());
//            cpCrmWxUser.setName(dto.getWxCpUser().getName());
//            cpCrmWxUser.setMobile(dto.getWxCpUser().getMobile());
//            cpCrmWxUser.setPosition(dto.getWxCpUser().getPosition());
//            cpCrmWxUser.setGender(Objects.nonNull(dto.getWxCpUser().getGender())?Integer.parseInt(dto.getWxCpUser().getGender().getCode()):null);
//            cpCrmWxUser.setEmail(dto.getWxCpUser().getEmail());
//            cpCrmWxUser.setAvatar(dto.getWxCpUser().getAvatar());
//            cpCrmWxUser.setThumbAvatar(dto.getWxCpUser().getThumbAvatar());
//            cpCrmWxUser.setAlias(dto.getWxCpUser().getAlias());
//            cpCrmWxUser.setStatus(dto.getWxCpUser().getStatus());
//            cpCrmWxUser.setAddress(dto.getWxCpUser().getAddress());
//            cpCrmWxUser.setOpenUserId(dto.getWxCpUser().getOpenUserId());
//            cpCrmWxUser.setDepartmentId(dto.getWxCpUser().getDepartIds()!=null?Arrays.toString(dto.getWxCpUser().getDepartIds()):null);
//            cpCrmWxUser.setChange_type(dto.getChangetype());
//            jsonObject.put("data",cpCrmWxUser);
//        }
//        if(dto.getMsgType().equals(PushCrmCpTypeEnum.EXTERNAL.getValue())){ //外部联系人数据
//            CpCrmExternalContact externalContact=mapperFacade.map(dto.getWxCpExternalContactInfo().getExternalContact(), CpCrmExternalContact.class);
//            externalContact.setChange_type(dto.getChangetype());
//            List<CpCrmFollowedUser> follow_user=new ArrayList<>();
//            if(CollUtil.isNotEmpty(dto.getWxCpExternalContactInfo().getFollowedUsers())){
//                for (me.chanjar.weixin.cp.bean.external.contact.FollowedUser followedUser : dto.getWxCpExternalContactInfo().getFollowedUsers()) {
//                    CpCrmFollowedUser user=mapperFacade.map(followedUser, CpCrmFollowedUser.class);
//                    user.setCreated(followedUser.getCreateTime());
//                    follow_user.add(user);
//                }
//                externalContact.setFollow_user(follow_user);
//            }
//            jsonObject.put("data",externalContact);
//        }
//        if(dto.getMsgType().equals(PushCrmCpTypeEnum.CUST_GROUP.getValue())){ //群聊数据
//            CpCrmGroup cpCrmGroup=mapperFacade.map(dto.getGroupChat(),CpCrmGroup.class);
//            if(Objects.nonNull(dto.getGroupChat().getCreateTime())){
//                cpCrmGroup.setCreated(dto.getGroupChat().getCreateTime());
//            }
//            cpCrmGroup.setChange_type(dto.getChangetype());
//            if(CollUtil.isNotEmpty(dto.getGroupChat().getMemberList())){
//                List<CpCrmGroupMember> member_list=new ArrayList<>();
//                for (WxCpUserExternalGroupChatInfo.GroupMember member : dto.getGroupChat().getMemberList()) {
//                    CpCrmGroupMember cpCrmGroupMember=mapperFacade.map(member, CpCrmGroupMember.class);
//                    cpCrmGroupMember.setNickName(member.getGroupNickname());
//                    if(Objects.nonNull(member.getInvitor())){
//                        cpCrmGroupMember.setInvitorUserid(member.getInvitor().getUserId());
//                    }
//                    member_list.add(cpCrmGroupMember);
//                }
//            }
//            jsonObject.put("data",cpCrmGroup);
//        }
//        //执行推送
//        log.info("执行推送数云-企微数据接收接口 msgType: {}  body: {}",dto.getMsgType(),JSONObject.toJSONString(jsonObject));
//        pushData(JSONObject.toJSONString(jsonObject));
//    }
//
//
//    private void pushData(String body){
//        if (null == body) {
//            log.info("调用-企微数据接收接口失败，入参为空");
//        }
//        long startTime = System.currentTimeMillis();
//        String result = CrmClients.clients().postCrm(CrmMethod.MEMBER_QYWX_SAVE.uri(), body);
//
//        if (StringUtils.isBlank(result)) {
//            log.info("调用-企微数据接收接口无响应，耗时：{}",System.currentTimeMillis() - startTime);
//        }
//        CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);
//        log.info("调用-企微数据接收接口，结果：{}，耗时：{}ms ", JSON.toJSONString(crmResult),System.currentTimeMillis() - startTime);
//    }
//
//
//    /**
//     * 获取企微群详情
//     * @param groupId
//     * @return
//     */
//    private WxCpUserExternalGroupChatInfo.GroupChat getChatDetail(String groupId){
//        try {
//            WxCpUserExternalGroupChatInfo result =  WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getExternalContactService().getGroupChat(groupId, 1);
//            return result.getGroupChat();
//        }catch (WxErrorException we){
//            log.error("查询群详情出错 errorCode:【"+we.getError().getErrorCode()+"】 msg:【"+we.getError().getErrorMsg()+"】");
//            return null;
//        }
//    }
//
//    /**
//     * 获取部门详情
//     * @param id
//     * @return
//     */
//    private WxCpDepart getWxCpDepart(String id){
//        try {
//            log.info("----getWxCpDepart start -------id：{}",id);
//            WxCpDepart depart = wxCpService.getDepartmentService().get(Long.parseLong(id));
//            log.info("----getWxCpDepart back -------：【"+Json.toJsonString(depart)+"】");
//            return depart;
//        }catch (WxErrorException e){
//            log.error("",e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取员工详情
//     * @param userId
//     * @return
//     */
//    private WxCpUser getWxCpUser(String userId){
//        try {
//            log.info("----getWxCpUser start -------id：{}",userId);
//            WxCpUser wxCpUser = wxCpService.getUserService().getById(userId);
//            log.info("----getWxCpUser back -------：【"+Json.toJsonString(wxCpUser)+"】");
//            return wxCpUser;
//        }catch (WxErrorException e){
//            log.error("",e);
//        }
//        return null;
//    }
//
//}
