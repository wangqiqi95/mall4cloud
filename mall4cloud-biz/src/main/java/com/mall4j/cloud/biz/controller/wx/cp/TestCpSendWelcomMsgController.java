package com.mall4j.cloud.biz.controller.wx.cp;

import com.mall4j.cloud.api.user.crm.model.UpdateTagModel;
import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.biz.manager.CpTagGroupCodeManager;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.manager.WxMpManager;
import com.mall4j.cloud.biz.service.cp.CpMediaRefService;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.biz.service.cp.handler.plus.ChatChangePlusHandler;
import com.mall4j.cloud.biz.service.cp.handler.plus.ExternalContactChangePlusHandler;
import com.mall4j.cloud.biz.service.cp.handler.plus.MsgPlusHandler;
import com.mall4j.cloud.biz.task.CpCustGroupTask;
import com.mall4j.cloud.api.biz.constant.cp.NotityTypeEunm;
import com.mall4j.cloud.biz.task.CpMaterialTask;
import com.mall4j.cloud.biz.task.StaffCodeTask;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 企业微信配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@RestController("TestCpSendWelcomMsgController")
@RequestMapping("/p/cp/test/send/welcome")
@Api(tags = "测试发送企微欢迎语")
@Slf4j
public class TestCpSendWelcomMsgController {

    private final CpTagGroupCodeManager cpTagGroupCodeManager;
//    private final CrmManager crmManager;
    private final CpMediaRefService cpMediaRefService;
    private final ExternalContactChangePlusHandler externalContactChangeHandler;
    private final MsgPlusHandler msgHandler;
    private final CpCustGroupTask cpCustGroupTask;
    private final WeixinCpExternalManager weixinCpExternalManager;
    private final ChatChangePlusHandler chatChangeHandler;//企微群消息
    private final WxCpService wxCpService;
    private final WxCpPushService wxCpPushService;
    private final CpMaterialTask cpMaterialTask;
    private final StaffCodeTask staffCodeTask;
    private final WxMpManager wxMpManager;


    @GetMapping("/notify")
    @ApiOperation(value = "notify", notes = "notify")
    public ServerResponseEntity<Void> notify(Integer type) {
        if(NotityTypeEunm.UNADD_QW.getCode()==type){//好友流失提醒
            NotifyMsgTemplateDTO.FollowLoss followLoss=NotifyMsgTemplateDTO.FollowLoss.builder()
                    .nickName("eury")
                    .userName("郭大大")
                    .deleteDate("2023-01-11 12:22")
                    .build();
            wxCpPushService.lossUserNotify(NotifyMsgTemplateDTO.builder().userId("1724986015480823897").qiWeiStaffId("eury").followLoss(followLoss).build());
        }else if(NotityTypeEunm.UNREGISTER_MEMBER.getCode()==type){//跟进提醒
            NotifyMsgTemplateDTO.FollowUp followUp=NotifyMsgTemplateDTO.FollowUp.builder()
                    .nickName("eury")
                    .userName("郭大大")
                    .staffName("小童")
                    .createDate(String.valueOf(RandomUtil.getRandomStr(2)))
                    .build();
            wxCpPushService.followNotify(NotifyMsgTemplateDTO.builder().userId("1724986015480823897").qiWeiStaffId("eury").followUp(followUp).build());
        }else if(NotityTypeEunm.INVITE_USER.getCode()==type){//素材浏览
            NotifyMsgTemplateDTO.Material material=NotifyMsgTemplateDTO.Material.builder()
                    .nickName("eury")
                    .userName("郭大大")
                    .materialName("测试素材")
                    .browseTime(String.valueOf(RandomUtil.getRandomStr(2)))
                    .browseDate("2023-01-22 16:22:22")
                    .tag("测试标签")
                    .build();
            wxCpPushService.materialNotify(NotifyMsgTemplateDTO.builder().userId("1724986015480823897").qiWeiStaffId("eury").material(material).build());
        }else if(NotityTypeEunm.SUB.getCode()==type){//敏感词命中
            NotifyMsgTemplateDTO.Hitkeyword hitkeyword=NotifyMsgTemplateDTO.Hitkeyword.builder()
                    .mate("测试敏感词")
                    .offendUser("苏打饼干")
                    .offendOutTime("2023-01-22 16:22:22")
                    .tag("测试标签")
                    .build();
            wxCpPushService.mateNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId("eury").hitkeyword(hitkeyword).build());
        }else if(NotityTypeEunm.USER_REGISTER_SUCCESS.getCode()==type){//回复超时提醒
            NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                    .timeOutTime(String.valueOf(RandomUtil.getRandomStr(2)))
                    .offendUser("苏打饼干")
                    .userName("苏打饼干")
                    .offendOutTime("2023-01-22 16:22:22")
                    .build();
            wxCpPushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId("eury").timeOut(timeOutMsg).build());
        }else if(NotityTypeEunm.CP_PHONE_TASK.getCode()==type){//手机号任务提醒
            NotifyMsgTemplateDTO.PhoneTask phoneTask=NotifyMsgTemplateDTO.PhoneTask.builder()
                    .startTime("2023-01-22 16:22:22")
                    .endTime("2023-02-22 16:22:22")
                    .number("5")
                    .taskId(5L)
                    .build();
            wxCpPushService.phoneTaskNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId("eury").phoneTask(phoneTask).build());
        }

        return ServerResponseEntity.success();
    }

    @PostMapping("/externalContactChangeHandler")
    @ApiOperation(value = "externalContactChangeHandler", notes = "externalContactChangeHandler")
    public ServerResponseEntity<Void> externalContactChangeHandler(@Valid @RequestBody WxCpXmlMessage wxMessage) {
        externalContactChangeHandler.handle(wxMessage,null,null,null);
        return ServerResponseEntity.success();
    }

    @PostMapping("/msgHandler")
    @ApiOperation(value = "msgHandler", notes = "msgHandler")
    public ServerResponseEntity<Void> msgHandler(@Valid @RequestBody WxCpXmlMessage wxMessage) {
        msgHandler.handle(wxMessage,null,null,null);
        return ServerResponseEntity.success();
    }

    @PostMapping("/ChatChangeHandler")
    @ApiOperation(value = "ChatChangeHandler", notes = "ChatChangeHandler")
    public ServerResponseEntity<Void> ChatChangeHandler(@Valid @RequestBody WxCpXmlMessage wxMessage) {
        chatChangeHandler.handle(wxMessage,null,null,null);
        return ServerResponseEntity.success();
    }

    @PostMapping("/refreshCustGroup")
    @ApiOperation(value = "刷新群聊数据&群成员数据", notes = "刷新群聊数据&群成员数据")
    public ServerResponseEntity<Void> refreshCustGroup() throws WxErrorException{
        cpCustGroupTask.refreshCustGroup();
        return ServerResponseEntity.success();
    }

    @PostMapping("/refreshCustGroupOwner")
    @ApiOperation(value = "刷新群主信息", notes = "刷新群主信息")
    public ServerResponseEntity<Void> refreshCustGroupOwner(@RequestBody List<String> chatIds) throws WxErrorException{
        cpCustGroupTask.refreshCustGroupOwner(chatIds);
        return ServerResponseEntity.success();
    }

    @PostMapping("/updateUserTagByCrm")
    @ApiOperation(value = "updateUserTagByCrm", notes = "updateUserTagByCrm")
    public ServerResponseEntity<String> updateUserTagByCrm(@RequestBody UpdateTagModel updateTagModel) {
//       crmManager.updateUserTagByCrm(updateTagModel,BuildTagFromEnum.AUTO_GROUP.getCode());
        return ServerResponseEntity.success();
    }

    @GetMapping("/refeshStaffAndExternalStatus")
    @ApiOperation(value = "refeshStaffAndExternalStatus", notes = "refeshStaffAndExternalStatus")
    public ServerResponseEntity<Void> refeshStaffAndExternalStatus() {
        cpTagGroupCodeManager.refeshStaffAndExternalStatus();
        return ServerResponseEntity.success();
    }

    @PostMapping("/pushCDPCpMsgDate")
    @ApiOperation(value = "测试推送消息事件内容至数云", notes = "测试推送消息事件内容至数云")
    public ServerResponseEntity<Void> pushCDPCpMsgDate(@Valid @RequestBody PushCDPCpMsgEventDTO dto) {
//        crmManager.pushCDPCpMsg(dto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/refreshCpChannelMediaId")
    @ApiOperation(value = "刷新渠道引流素材mediaId", notes = "刷新渠道引流素材mediaId")
    public ServerResponseEntity<Void> refreshCpChannelMediaId() {
        cpMediaRefService.refreshMediaId();
        return ServerResponseEntity.success();
    }

    @GetMapping("/getWxCpExternalContactInfo")
    @ApiOperation(value = "getWxCpExternalContactInfo", notes = "getWxCpExternalContactInfo")
    public ServerResponseEntity<WxCpExternalContactInfo> getWxCpExternalContactInfo(String userId,String externalUserId) {
        return ServerResponseEntity.success(weixinCpExternalManager.getWxCpExternalContactInfo(userId,externalUserId));
    }

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "getUserInfo", notes = "getUserInfo")
    public ServerResponseEntity<WxCpUser> getUserInfo(String userId) throws WxErrorException{
        return ServerResponseEntity.success(wxCpService.getUserService().getById(userId));
    }

    @PostMapping("/refreshCPMeterialMediaIdTask")
    @ApiOperation(value = "素材中心图片自动同步微信续期", notes = "素材中心图片自动同步微信续期")
    public ServerResponseEntity<Void> refreshCPMeterialMediaIdTask() {
        cpMaterialTask.refreshCPMeterialMediaIdTask();
        return ServerResponseEntity.success();
    }

    @PostMapping("/getWxMpFollowDatas")
    @ApiOperation(value = "公众号关注列表数据", notes = "公众号关注列表数据")
    public ServerResponseEntity<Void> getWxMpFollowDatas() {
        wxMpManager.getWxMpFollowDatas();
        return ServerResponseEntity.success();
    }

    @GetMapping("/refreshPicMediaIdTask")
    @ApiOperation(value = "刷新渠道活码素材", notes = "刷新渠道活码素材")
    public ServerResponseEntity<String> refreshPicMediaIdTask() {
        staffCodeTask.refreshPicMediaIdTask();
        return ServerResponseEntity.success();
    }
}
