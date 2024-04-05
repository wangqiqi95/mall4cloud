package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.biz.dto.cp.externalcontact.AttachmentExtApiDTO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;

/**
 * @Author: Administrator
 * @Description: 企业微信客户操作服务
 * @Date: 2022-02-10 15:10
 * @Version: 1.0
 */
public interface WxCpCustNotifyService {
    /**
     * 添加客户回调
     * @param userId 成员的企业微信id
     * @param externalUserId 客户的外部联系人id
     */
    void create(String userId, String externalUserId, WxCpExternalContactInfo wxCpExternalContactInfo) ;
    /**
     *更新客户回调
     * @param userId 成员的企业微信id
     * @param externalUserId 客户的外部联系人id
     */
    void update(String userId, String externalUserId, WxCpExternalContactInfo wxCpExternalContactInfo);

    /**
     * 删除客户回调
     * @param userId 成员的企业微信id
     * @param externalUserId 客户的外部联系人id
     */
    void delete(String userId, String externalUserId);

    /**
     * 发送欢迎语逻辑
     * @param welcomeCode 欢迎语码
     * @param state  活码标宋
     * @param userId
     */
    void sendWelcomeMsg(String welcomeCode, String state, String userId,String userName);

    /**
     * 给客户加标签
     * @param state
     * @param userId
     */
    void addTagToCust(String state, String userId,String externalUserId );


    /**
     * 提供给群发任务对小程序链接进行参数拼接
     *
     * */
    ServerResponseEntity<String> defineStaffMiniProgram(AttachmentExtApiDTO attachmentExtApiDTO, StaffVO staffVO);
}
