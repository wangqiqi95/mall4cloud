package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.biz.vo.WeixinWebAppInfoVo;
import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgSendResultVO;
import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgTaskVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * @date 2023年03月20日
 * @author Peter_Tan
 */
public interface WxCpGroupPushTaskService {

    /**
     * 调用企业微信获取群发成员发送任务列表接口
     * @param msgId 群发消息的id
     * @param limit 返回的最大记录数
     * @param cursor 用于分页查询的游标
     * @return
     */
    ServerResponseEntity<WxCpGroupMsgTaskVO> getGroupMessageTask(String msgId, Integer limit, String cursor);

    /**
     * 调用企业微信获取企业群发成员执行结果
     * @param msgId 群发消息的id
     * @param staffCpUserId 发送成员userid(发送者的企业微信ID)
     * @param limit 返回的最大记录数
     * @param cursor 用于分页查询的游标
     * @return
     */
    ServerResponseEntity<WxCpGroupMsgSendResultVO> getGroupMessageSendResult(String msgId, String staffCpUserId, Integer limit, String cursor);

}
