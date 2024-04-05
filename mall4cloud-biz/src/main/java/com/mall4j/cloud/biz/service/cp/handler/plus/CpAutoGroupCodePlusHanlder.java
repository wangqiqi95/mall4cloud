package com.mall4j.cloud.biz.service.cp.handler.plus;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.crm.model.UpdateTagModel;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.wx.externalcontact.SendWelcomeDTO;
import com.mall4j.cloud.biz.mapper.cp.CpAutoGroupCodeUserMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.WxCpUpdateRemarkRequest;
import me.chanjar.weixin.cp.bean.external.WxCpWelcomeMsg;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Image;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 自动拉群
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CpAutoGroupCodePlusHanlder {

    private final CpAutoGroupCodeService cpAutoGroupCodeService;
    private final  CpAutoGroupCodeUserService autoGroupCodeUserService;
//    private final CrmManager crmManager;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    private final CpAutoGroupCodeUserMapper cpAutoGroupCodeUserMapper;

    public void handleAutoGroupCode(SendWelcomeDTO dto){
        CpCodeChannel cpCodeChannel=dto.getCpCodeChannel();
        String qiWeiUserId=dto.getWxMessage().getExternalUserId();//企微-客户id
        String userNickName=dto.getWxCpExternalContactInfo().getExternalContact().getName();
        StaffVO staffVO=dto.getStaffVO();
        CpAutoGroupCode cpAutoGroupCode=cpAutoGroupCodeService.getById(cpCodeChannel.getSourceId());
        if(Objects.isNull(cpAutoGroupCode)){
            log.info("外部联系人渠道处理失败，根据渠道源未获取到自动拉群信息");
            return;
        }
        WxCpXmlMessage wxMessage=dto.getWxMessage();
        WxCpExternalContactInfo wxCpExternalContactInfo=dto.getWxCpExternalContactInfo();

        log.info("外部联系人渠道处理，根据渠道源获取到自动拉群信息：", JSON.toJSONString(cpAutoGroupCode));
        CpAutoGroupCodeUser cpAutoGroupCodeUser=cpAutoGroupCodeUserMapper.getByUserId(qiWeiUserId,cpAutoGroupCode.getId());
        if(Objects.isNull(cpAutoGroupCodeUser)){
            cpAutoGroupCodeUser=new CpAutoGroupCodeUser();
            cpAutoGroupCodeUser.setBindTime(WechatUtils.formatDate(dto.getFollowedUser().getCreateTime().toString()));
            cpAutoGroupCodeUser.setCodeId(cpAutoGroupCode.getId());
            cpAutoGroupCodeUser.setStaffId(staffVO.getId());
            cpAutoGroupCodeUser.setQiWeiUserId(qiWeiUserId);
            cpAutoGroupCodeUser.setStatus(1);
            cpAutoGroupCodeUser.setSendStatus(0);
            cpAutoGroupCodeUser.setIsDelete(0);
            cpAutoGroupCodeUser.setCreateBy("通讯录回调通知");
            cpAutoGroupCodeUser.setCreateTime(new Date());
            cpAutoGroupCodeUser.setNickName(userNickName);
            cpAutoGroupCodeUser.setJoinGroup(0);
            autoGroupCodeUserService.save(cpAutoGroupCodeUser);
        }else{
            cpAutoGroupCodeUser.setBindTime(WechatUtils.formatDate(dto.getFollowedUser().getCreateTime().toString()));
            cpAutoGroupCodeUser.setCodeId(cpAutoGroupCode.getId());
            cpAutoGroupCodeUser.setStaffId(staffVO.getId());
            cpAutoGroupCodeUser.setQiWeiUserId(qiWeiUserId);
            cpAutoGroupCodeUser.setStatus(1);
            cpAutoGroupCodeUser.setSendStatus(0);
            cpAutoGroupCodeUser.setIsDelete(0);
            cpAutoGroupCodeUser.setUpdateBy("通讯录回调通知");
            cpAutoGroupCodeUser.setUpdateTime(new Date());
            cpAutoGroupCodeUser.setNickName(userNickName);
            cpAutoGroupCodeUser.setJoinGroup(0);
            autoGroupCodeUserService.updateById(cpAutoGroupCodeUser);
        }

        try {
            //TODO 客户打标签逻辑
            if(StrUtil.isNotEmpty(cpAutoGroupCode.getTags())){
                addTagToCust(wxMessage.getState(),wxMessage.getUserId(),wxCpExternalContactInfo.getExternalContact().getUnionId(),cpAutoGroupCode.getTags());
            }
            //自动打备注
            if(Objects.nonNull(cpAutoGroupCode.getAutoRemarkState()) && cpAutoGroupCode.getAutoRemarkState()==1 && StrUtil.isNotEmpty(cpAutoGroupCode.getAutoRemark())){
                addRemark(wxCpExternalContactInfo.getExternalContact().getName(),
                        wxCpExternalContactInfo.getExternalContact().getExternalUserId(),
                        wxMessage.getUserId(),
                        cpAutoGroupCode.getAutoRemark());
            }
            //修改客户通过好友验证状态
            if(Objects.nonNull(cpAutoGroupCode.getAuthType())){
                updateAutoType(wxCpExternalContactInfo.getExternalContact().getExternalUserId(),
                        wxMessage.getUserId(),cpAutoGroupCode.getAuthType());
            }
            //发送信息：欢迎语&群活码引流页面二维码
            String slogan=SlognUtils.getSlogan(staffVO.getStaffName(),cpAutoGroupCode.getSlogan(),userNickName);
            //欢迎语
            Text text =  new Text();
            text.setContent(slogan);
            WxCpWelcomeMsg wxCpWelcomeMsg = new WxCpWelcomeMsg();
            wxCpWelcomeMsg.setWelcomeCode(wxMessage.getWelcomeCode());//欢迎语CODE
            wxCpWelcomeMsg.setText(text);
            Attachment attachment=new Attachment();
            //图片附件
            Image image=new Image();
            image.setPicUrl(cpAutoGroupCode.getDrainageUrl());//引流页面链接二维码
            attachment.setImage(image);
            wxCpWelcomeMsg.setAttachments(Lists.newArrayList(attachment));
            log.info("自动拉群----发送渠道欢迎语----{}",wxCpWelcomeMsg.toJson());
            WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().sendWelcomeMsg(wxCpWelcomeMsg);
            log.info("自动拉群--发送渠道欢迎语 success----{}",wxCpWelcomeMsg.toJson());
            cpAutoGroupCodeUser.setSendStatus(1);
            autoGroupCodeUserService.updateById(cpAutoGroupCodeUser);//修改发送状态
            log.error("外部联系人渠道处理,自动拉群添加好友发送欢迎语成功");
        }catch (WxErrorException e){
            log.error("外部联系人渠道处理,自动拉群添加好友发送欢迎语失败", e);
            cpAutoGroupCodeUser.setSendStatus(3);
            cpAutoGroupCodeUser.setSendStatusRemark(""+e.toString());
            autoGroupCodeUserService.updateById(cpAutoGroupCodeUser);//修改发送状态
        }
    }

    /**
     * TODO 给客户打标签
     * @param state
     * @param userId
     */
    public void addTagToCust(String state, String userId,String externalUserUnionId ,String tags) {
        try{
            UpdateTagModel updateTagModel=new UpdateTagModel();
            updateTagModel.setQiWeiUserUnionIds(ListUtil.toList(externalUserUnionId));
            updateTagModel.setTags(tags);
            //todo 待实现
//            crmManager.updateUserTagByCrm(updateTagModel, BuildTagFromEnum.AUTO_GROUP.getCode());
            log.info("----自动拉群 cust addTagToCust -------：【SUCCESS】");
        }catch (Exception e){
            log.error("客户加标签失败",e);
        }
    }

    /**
     * 给客户修改备注
     * @param nickName
     * @param externalUserId
     * @param userId
     * @param remark
     */
    private void addRemark(String nickName,String externalUserId,String userId,String remark){
        try{
            WxCpUpdateRemarkRequest remarkRequest=new WxCpUpdateRemarkRequest();
            remarkRequest.setExternalUserId(externalUserId);
            remarkRequest.setUserId(userId);
            remarkRequest.setRemark(remark+""+nickName);
            WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().updateRemark(remarkRequest);
            //更新客户备注
            UserStaffCpRelationDTO userStaffCpRelationDTO=new UserStaffCpRelationDTO();
            userStaffCpRelationDTO.setQiWeiStaffId(userId);
            userStaffCpRelationDTO.setQiWeiUserId(externalUserId);
            userStaffCpRelationDTO.setCpRemark(remark+""+nickName);
            userStaffCpRelationFeignClient.updateUserStaffCpRelationRemark(userStaffCpRelationDTO);
            log.info("----自动打备注 success----{}",remarkRequest.toJson());
        }catch (WxErrorException e){
            log.error("自动打备注失败",e);
        }
    }

    /**
     * 修改客户通过好友验证状态
     */
    private void updateAutoType(String externalUserId,String userId,Integer autoType){
        try{
            UserStaffCpRelationDTO userStaffCpRelationDTO=new UserStaffCpRelationDTO();
            userStaffCpRelationDTO.setQiWeiStaffId(userId);
            userStaffCpRelationDTO.setQiWeiUserId(externalUserId);
            userStaffCpRelationDTO.setAutoType(autoType);
            userStaffCpRelationFeignClient.updateUserStaffCpRelationAutoType(userStaffCpRelationDTO);
            log.info("----修改客户通过好友验证状态 success----{}",JSON.toJSONString(userStaffCpRelationDTO));
        }catch (Exception e){
            log.error("修改客户通过好友验证状态",e);
        }

    }

}
