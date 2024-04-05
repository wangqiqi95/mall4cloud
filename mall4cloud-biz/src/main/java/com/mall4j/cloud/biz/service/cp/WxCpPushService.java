package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.biz.dto.cp.wx.NotifyDto;

public interface WxCpPushService {

    /**
     * 全部未添加企业微信的好友
     * @param nums 数量
     * @param staffId 员工企业微信id
     */
    void unAddQwNotify(Integer nums,String staffId);

    /**
     * 全部未注册的好友
     * @param nums 数量
     * @param staffId 员工企业微信id
     */
    void unRegisterMemberNotify(Integer nums,String staffId);


    /**
     * 新加的未注册好友
     * @param userId 会员id
     * @param staffId 员工企业微信id
     */
    void inviteUserNotify(Long userId,String staffId);


    /**
     * 好友流失提醒
     * @param externalUserId 好友的企业微信id
     * @param staffUserId 员工的企业微信id
     */
    void deleteUserNotify(String staffUserId,String externalUserId);

    /**
     * 会员注册成功提醒
     * @param userId  客户userid
     * @param staffId 员工的企业微信id
     */
    void userRegisterSuccessNotify(String staffId,Long userId);

    /**
     * 服务变更提醒
     * @param qiWeiUserId  客户企业微信userid
     * @param qiWeiStaffId 员工的企业微信id
     */
    void serviceChangeNotify(String qiWeiStaffId,String qiWeiUserId);


    /**
     * 消息推送
     * @param notifyDto 消息内容
     */
    boolean createMiniprogramPush( NotifyDto notifyDto);

    /**
     * 好友流失提醒
     */
    void lossUserNotify(NotifyMsgTemplateDTO dto);

    /**
     * 跟进提醒
     */
    void followNotify(NotifyMsgTemplateDTO dto);

    /**
     * 素材浏览提醒
     */
    void materialNotify(NotifyMsgTemplateDTO dto);

    /**
     * 敏感词命中提醒
     */
    void mateNotify(NotifyMsgTemplateDTO dto);

    /**
     * 回复超时提醒
     */
    void timeOutNotify(NotifyMsgTemplateDTO dto);

    /**
     * 手机号任务提醒
     */
    void phoneTaskNotify(NotifyMsgTemplateDTO dto);
}
